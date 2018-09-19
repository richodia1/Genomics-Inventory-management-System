/**
 * inventory.Struts Jun 29, 2010
 */
package org.iita.inventory.remote.service;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.PropertyValueException;
import org.iita.inventory.model.FieldVariables;
import org.iita.inventory.model.LotSelection;
import org.iita.inventory.model.LotVariableUpdate;
import org.iita.inventory.model.LotVariableUpdateBulk;
import org.iita.inventory.model.MigrationLotLocationUpdate;
import org.iita.inventory.model.MigrationLotLocationUpdateBulk;
import org.iita.inventory.model.QuantityUpdate;
import org.iita.inventory.model.QuantityUpdateBulk;
import org.iita.inventory.model.Transaction2.Type;
import org.iita.inventory.remote.ExistingLocationMigration;
import org.iita.inventory.remote.FieldVariablesList;
import org.iita.inventory.remote.InventoryService;
import org.iita.inventory.remote.Location;
import org.iita.inventory.remote.Lot;
import org.iita.inventory.remote.LotFieldVariable;
import org.iita.inventory.remote.LotList;
import org.iita.inventory.remote.LotQuantity;
import org.iita.inventory.remote.LotSubtypeQuantity;
import org.iita.inventory.remote.LotVariable;
import org.iita.inventory.remote.NewLocationMigration;
import org.iita.inventory.remote.Variables;
import org.iita.inventory.service.InventoryException;
import org.iita.inventory.service.LocationService;
import org.iita.inventory.service.LotLocationMigrationUpdateService;
import org.iita.inventory.service.LotService;
import org.iita.inventory.service.LotSubtypeUpdateService;
import org.iita.inventory.service.LotVariableService;
import org.iita.inventory.service.LotVariableUpdateService;
import org.iita.inventory.service.QuantityUpdateService;
import org.iita.inventory.service.SelectionService;
import org.iita.inventory.service.VariablesService;
import org.iita.security.Authorize;
import org.iita.security.model.User;
import org.springframework.transaction.annotation.Transactional;

/**
 * Exposes inventory functions for remoting
 * 
 * @author mobreza
 */
public class InventoryServiceImpl implements InventoryService {
	private static Log LOG = LogFactory.getLog(InventoryServiceImpl.class);
	private static final int MAX_LOTS_IN_LIST = 6000;
	private LotService lotService;
	private VariablesService variablesService;
	private LotVariableService lotVariableService;
	private LotVariableUpdateService lotVariableUpdateService;
	private LocationService locationService;
	private SelectionService selectionService;
	private QuantityUpdateService quantityUpdateService;
	private LotSubtypeUpdateService lotSubtypeUpdateService;
	private LotLocationMigrationUpdateService lotLocationMigrationUpdateService;

	/**
	 * @param locationService the locationService to set
	 */
	public void setLocationService(LocationService locationService) {
		this.locationService = locationService;
	}

	/**
	 * @param lotSubtypeUpdateService the lotSubtypeUpdateService to set
	 */
	public void setLotSubtypeUpdateService(LotSubtypeUpdateService lotSubtypeUpdateService) {
		this.lotSubtypeUpdateService = lotSubtypeUpdateService;
	}
	
	/**
	 * @param lotLocationMigrationUpdateService the lotLocationMigrationUpdateService to set
	 */
	public void setLotLocationMigrationUpdateService(LotLocationMigrationUpdateService lotLocationMigrationUpdateService) {
		this.lotLocationMigrationUpdateService = lotLocationMigrationUpdateService;
	}

	/**
	 * @param lotService the lotService to set
	 */
	public void setLotService(LotService lotService) {
		this.lotService = lotService;
	}
	
	/**
	 * @param lotVariableService the lotVariableService to set
	 */
	public void setLotVariableService(LotVariableService lotVariableService) {
		this.lotVariableService = lotVariableService;
	}
	
	/**
	 * @param variablesService the variablesService to set
	 */
	public void setVariablesService(VariablesService variablesService) {
		this.variablesService = variablesService;
	}
	
	/**
	 * @param lotVariableUpdateService the lotVariableUpdateService to set
	 */
	public void setLotVariableUpdateService(LotVariableUpdateService lotVariableUpdateService) {
		this.lotVariableUpdateService = lotVariableUpdateService;
	}
	
	/**
	 * @param selectionService the selectionService to set
	 */
	public void setSelectionService(SelectionService selectionService) {
		this.selectionService = selectionService;
	}

	/**
	 * @param quantityUpdateService the quantityUpdateService to set
	 */
	public void setQuantityUpdateService(QuantityUpdateService quantityUpdateService) {
		this.quantityUpdateService = quantityUpdateService;
	}

	/**
	 * @see org.iita.inventory.remote.InventoryService#whoAmI()
	 */
	@Override
	public String whoAmI() {
		LOG.info("Who am I?");
		User user = Authorize.getUser();
		LOG.warn("Current user: " + user);
		return user == null ? "Anonymous coward" : user.getUsername();
	}

	/**
	 * @see org.iita.inventory.remote.InventoryService#getLotByIds2(long[])
	 */
	@Override
	public List<Lot> getLotsById(long... ids) {
		List<Lot> lots = new ArrayList<Lot>();
		for (long id : ids) {
			lots.add(createRemoteLot(this.lotService.load(id)));
		}
		return lots;
	}

	/**
	 * @see org.iita.inventory.remote.InventoryService#getLotByBarcode(long)
	 */
	@Override
	public List<Lot> getLotsByBarcode(long... barCodes) {
		List<Long> lotBarcodes = new ArrayList<Long>();
		for (long barCode : barCodes)
			lotBarcodes.add(barCode);
		List<org.iita.inventory.model.Lot> inventoryLots = this.lotService.getLotsByBarcode(lotBarcodes);
		return createRemoteLots(inventoryLots);
	}
	
	/**
	 * @see org.iita.inventory.remote.InventoryService#getLotVariablesByBarcode(long)
	 */
	@Override
	public List<LotVariable> getLotVariablesByBarcode(Long barCodes) {
		//List<Long> lotVariableBarcodes = new ArrayList<Long>();
		//for (long barCode : barCodes)
		//	lotVariableBarcodes.add(barCode);
		List<org.iita.inventory.model.LotVariable> inventoryLotVariables = this.lotVariableService.getLotVariablesByBarcode(barCodes);
		return createRemoteLotVariables(inventoryLotVariables);
	}
	
	/**
	 * @see org.iita.inventory.remote.InventoryService#getLotFieldVariablesByLotId(long)
	 */
	@Override
	public List<LotFieldVariable> getLotFieldVariablesByLotId(Long lotId) {
		List<org.iita.inventory.model.FieldVariables> inventoryFieldVariables = this.lotService.getLotFieldVariablesByLotId(lotId);
		return createRemoteFieldVariables(inventoryFieldVariables);
	}
	
	/**
	 * @see org.iita.inventory.remote.InventoryService#getFieldVariablesList()
	 */
	@Override
	public List<FieldVariablesList> getFieldVariablesList() {
		List<org.iita.inventory.model.FieldVariablesList> inventoryFieldVariables = this.lotVariableService.loadFieldVariablesList();
		return createRemoteFieldVariablesList(inventoryFieldVariables);
	}
	
	/**
	 * Get lots in particular location, limited to MAX_LOTS_IN_LIST
	 * 
	 * @see org.iita.inventory.remote.InventoryService#getLots(long)
	 */
	@Override
	public List<Lot> getLotsByLocation(long rootLocationId) {
		LOG.debug("Loading all lots for root location=" + rootLocationId);
		org.iita.inventory.model.Location location = this.locationService.load(rootLocationId);
		if (location == null) {
			LOG.warn("No location with id=" + rootLocationId);
			return null;
		}
		List<org.iita.inventory.model.Lot> lots = this.lotService.list(location, 0, MAX_LOTS_IN_LIST, true).getResults();
		LOG.info("Fetched " + lots.size() + " lots from location " + location.getName());
		LOG.debug("Converting to remote Lot");
		return createRemoteLots(lots);
	}

	/**
	 * @see org.iita.inventory.remote.InventoryService#getLocations(long)
	 */
	@Override
	public List<Location> getLocations(Long rootLocationId) {
		LOG.debug("Loading all lots for root location=" + rootLocationId);
		org.iita.inventory.model.Location location = null;
		if (rootLocationId != null) {
			location = this.locationService.load(rootLocationId);
			if (location == null) {
				LOG.error("No such location: " + rootLocationId);
				return null;
			}
		}
		Collection<org.iita.inventory.model.Location> locations = this.locationService.getSubLocations(location);
		return createRemoteLocations(locations);
	}

	/**
	 * @see org.iita.inventory.remote.InventoryService#getLotLists()
	 */
	@Override
	public List<LotList> getLotLists() {
		User owner = Authorize.getUser();
		List<LotSelection> inventoryLists = this.selectionService.getLists(owner);
		return createRemoteLists(inventoryLists, false);
	}
	
	/**
	 * @see org.iita.inventory.remote.InventoryService#getLotLists()
	 */
	@Override
	public LotList getLotList(Long id) {
		User owner = Authorize.getUser();
		LotSelection lot = this.selectionService.loadList(id, owner);
		return createRemoteList(lot, true);
	}
	
	/**
	 * @see org.iita.inventory.remote.InventoryService#getVariableNames()
	 */
	@Override
	public List<Variables> getVariableNames() {
		List<org.iita.inventory.model.Variables> variables = null;
		try {
			variables = this.variablesService.listVariables();
		} catch (Exception e) {
			LOG.debug(e.getMessage());
		}
		return createRemoteVariablesLists(variables, false);
	}
	
	/**
	 * @see org.iita.inventory.remote.InventoryService#getVariableName(Long)
	 */
	@Override
	public Variables getVariableName(Long id) {
		org.iita.inventory.model.Variables variable = this.variablesService.loadVariable(id);
		return createRemoteVariableList(variable, true);
	}

	/**
	 * @param inventoryLists
	 * @param includeDetails 
	 * @return
	 */
	private List<LotList> createRemoteLists(List<LotSelection> inventoryLists, boolean includeDetails) {
		List<LotList> lotLists = new ArrayList<LotList>();
		for (LotSelection selection : inventoryLists) {
			lotLists.add(createRemoteList(selection, includeDetails));
		}
		return lotLists;
	}

	/**
	 * @param selection
	 * @param includeDetails Should selected lot ids be provided
	 * @return
	 */
	private LotList createRemoteList(LotSelection selection, boolean includeDetails) {
		LotList lotList = new LotList();
		if(selection!=null){
			lotList.setName(selection.getName());
			lotList.setId(selection.getId());
			if (includeDetails)
				lotList.setSelectedLots(new ArrayList<Long>(selection.getSelectedLots()));
			else {
				// other case, don't provide selected lot data
			}
			return lotList;
		}else
			return null;
	}
	
	/**
	 * @param inventoryLists
	 * @param includeDetails 
	 * @return
	 */
	private List<Variables> createRemoteVariablesLists(List<org.iita.inventory.model.Variables> variablesLists, boolean includeDetails) {
		List<Variables> variableLists = new ArrayList<Variables>();
		for (org.iita.inventory.model.Variables variable : variablesLists) {
			variableLists.add(createRemoteVariableList(variable, includeDetails));
		}
		return variableLists;
	}

	/**
	 * @param variables
	 * @param includeDetails Should selected variables ids be provided
	 * @return
	 */
	private Variables createRemoteVariableList(org.iita.inventory.model.Variables variable, boolean includeDetails) {
		Variables variableList = new Variables();
		variableList.setName(variable.getName());
		variableList.setId(variable.getId());
		return variableList;
	}
	
	/**
	 * @param locations
	 * @return
	 */
	private List<Location> createRemoteLocations(Collection<org.iita.inventory.model.Location> locations) {
		LOG.debug("Converting " + locations.size() + " to remote locations.");
		ArrayList<Location> remoteLocations = new ArrayList<Location>();
		for (org.iita.inventory.model.Location location : locations) {
			remoteLocations.add(createRemoteLocation(location));
		}
		return remoteLocations;
	}

	/**
	 * Convert inventory location for remote systems
	 * 
	 * @param location
	 * @return
	 */
	private Location createRemoteLocation(org.iita.inventory.model.Location location) {
		Location remoteLocation = new Location();
		remoteLocation.setId(location.getId());
		remoteLocation.setName(location.getName());
		remoteLocation.setParentId(location.getParent() == null ? null : location.getParent().getId());
		return remoteLocation;
	}

	/**
	 * @param lots
	 * @return
	 */
	private List<Lot> createRemoteLots(List<org.iita.inventory.model.Lot> lots) {
		LOG.debug("Converting " + lots.size() + " to remote lots.");
		List<Lot> remoteLots = new ArrayList<Lot>();
		for (org.iita.inventory.model.Lot inventoryLot : lots) {
			remoteLots.add(createRemoteLot(inventoryLot));
		}
		return remoteLots;
	}

	/**
	 * Create remote lot from inventory lot
	 * 
	 * @param inventoryLot
	 * @return
	 */
	private Lot createRemoteLot(org.iita.inventory.model.Lot inventoryLot) {
		Lot lot = new Lot();
		lot.setId(inventoryLot.getId());
		lot.setBarCode(inventoryLot.getBarCode() == null ? null : inventoryLot.getBarCode().getId());
		lot.setItemName(inventoryLot.getItem().getName());
		lot.setLastUpdated(inventoryLot.getLastUpdated());
		lot.setLastUpdatedBy(inventoryLot.getLastUpdatedBy());
		lot.setCreatedBy(inventoryLot.getCreatedBy());
		
		String location = null;
		// get pathnames
		if (inventoryLot.getLocation() != null)
			location = inventoryLot.getLocation().getPathNames();
		
		lot.setLocation(location);
		lot.setQuantity(inventoryLot.getQuantity());
		lot.setScale(inventoryLot.getScale());
		lot.setLine(inventoryLot.getLine());
		
		if(inventoryLot.getContainer()==null)
			lot.setContainer(null);
		else
			lot.setContainer(inventoryLot.getContainer().toString());
		
		if(inventoryLot.getLocation()!=null)
			lot.setLocationId(inventoryLot.getLocation().getId());
		else
			lot.setLocationId(null);
		
		if(inventoryLot.getLocationDetail()!=null)
			lot.setLocationDetail(inventoryLot.getLocationDetail());
		else
			lot.setLocationDetail(null);
		
		if(inventoryLot instanceof org.iita.inventory.model.InVitroLot){
			lot.setIntroductionDate(((org.iita.inventory.model.InVitroLot) inventoryLot).getIntroductionDate());
			lot.setOrigin(((org.iita.inventory.model.InVitroLot) inventoryLot).getOrigin());
			lot.setVirusFree(((org.iita.inventory.model.InVitroLot) inventoryLot).getVirusFree());
			lot.setSubCulturedDate(((org.iita.inventory.model.InVitroLot) inventoryLot).getRegenerationDate());
		}else if(inventoryLot instanceof org.iita.inventory.model.SeedLot){
			lot.setIntroductionDate(null);
			lot.setOrigin(null);
			lot.setVirusFree(((org.iita.inventory.model.SeedLot) inventoryLot).getVirusFree());
			lot.setSubCulturedDate(null);
		}else{
			lot.setIntroductionDate(null);
			lot.setOrigin(null);
			lot.setVirusFree(false);
			lot.setSubCulturedDate(null);
		}
		
		return lot;
	}
	
	/**
	 * @param lotvariables
	 * @return
	 */
	private List<LotVariable> createRemoteLotVariables(List<org.iita.inventory.model.LotVariable> lotVariables) {
		LOG.debug("Converting " + lotVariables.size() + " to remote lotvariables.");
		List<LotVariable> remoteLotVariables = new ArrayList<LotVariable>();
		for (org.iita.inventory.model.LotVariable inventoryLotVariable : lotVariables) {
			remoteLotVariables.add(createRemoteLotVariable(inventoryLotVariable));
		}
		return remoteLotVariables;
	}
	
	/**
	 * Create remote lot variable from inventory lotVariable
	 * 
	 * @param inventoryLotVariable
	 * @return
	 */
	private LotVariable createRemoteLotVariable(org.iita.inventory.model.LotVariable inventoryLotVariable) {
		LotVariable lotVariable = new LotVariable();
		lotVariable.setId(inventoryLotVariable.getId());
		lotVariable.setLastUpdated(inventoryLotVariable.getLastUpdated());
		lotVariable.setLastUpdatedBy(inventoryLotVariable.getLastUpdatedBy());
		lotVariable.setVariabledate(inventoryLotVariable.getVariabledate());
		lotVariable.setLotId(inventoryLotVariable.getLot().getId());
		lotVariable.setVariableId(inventoryLotVariable.getVariable().getId());
		lotVariable.setQuantity(inventoryLotVariable.getQuantity());
		lotVariable.setBarCode(inventoryLotVariable.getLot().getBarCode()==null ? null : inventoryLotVariable.getLot().getBarCode().getId());
		lotVariable.setLotLine(inventoryLotVariable.getLot().getLine());
		return lotVariable;
	}
	
	/**
	 * @param lotFieldVariable
	 * @return
	 */
	private List<LotFieldVariable> createRemoteFieldVariables(List<org.iita.inventory.model.FieldVariables> fieldVariables) {
		LOG.debug("Converting " + fieldVariables.size() + " to remote field variables.");
		List<LotFieldVariable> remoteFieldVariables = new ArrayList<LotFieldVariable>();
		for (org.iita.inventory.model.FieldVariables inventoryFieldVariable : fieldVariables) {
			remoteFieldVariables.add(createRemoteFieldVariables(inventoryFieldVariable));
		}
		return remoteFieldVariables;
	}
	
	/**
	 * Create remote lot variable from inventory fieldVariables
	 * 
	 * @param inventoryFieldVariables
	 * @return
	 */
	private LotFieldVariable createRemoteFieldVariables(org.iita.inventory.model.FieldVariables inventoryFieldVariable) {
		LotFieldVariable fieldVariable = new LotFieldVariable();
		fieldVariable.setLotId(inventoryFieldVariable.getLot().getId());
		fieldVariable.setLastUpdated(inventoryFieldVariable.getLastUpdated());
		fieldVariable.setLastUpdatedBy(inventoryFieldVariable.getLastUpdatedBy());
		fieldVariable.setDate(inventoryFieldVariable.getDate());
		fieldVariable.setQty(inventoryFieldVariable.getQty());
		fieldVariable.setVar(inventoryFieldVariable.getVar());
		return fieldVariable;
	}
	
	/**
	 * @param fieldVariables
	 * @return
	 */
	private List<FieldVariablesList> createRemoteFieldVariablesList(List<org.iita.inventory.model.FieldVariablesList> fieldVariables) {
		LOG.debug("Converting " + fieldVariables.size() + " to remote field variables.");
		List<FieldVariablesList> remoteFieldVariables = new ArrayList<FieldVariablesList>();
		for (org.iita.inventory.model.FieldVariablesList inventoryFieldVariable : fieldVariables) {
			remoteFieldVariables.add(createRemoteFieldVariablesList(inventoryFieldVariable));
		}
		return remoteFieldVariables;
	}
	
	/**
	 * Create remote field variables from inventory fieldVariablesList
	 * 
	 * @param inventoryFieldVariables
	 * @return
	 */
	private FieldVariablesList createRemoteFieldVariablesList(org.iita.inventory.model.FieldVariablesList inventoryFieldVariable) {
		FieldVariablesList fieldVariable = new FieldVariablesList();
		fieldVariable.setVar(inventoryFieldVariable.getVar());
		return fieldVariable;
	}
	
	/**
	 * Would update user's lot list
	 * 
	 * @see org.iita.inventory.remote.InventoryService#updateLotList(org.iita.inventory.remote.LotList)
	 */
	@Override
	public void updateLotList(LotList lotList) {
		User owner = Authorize.getUser();
		LotSelection inventoryList = new LotSelection();
		
		if(lotList!=null){
			LOG.debug("LOT LIST : " + lotList.getName());
			if(lotList.getId()!=null)
				inventoryList = this.selectionService.loadList(lotList.getId(), owner);
				
			if (inventoryList == null) {
				LOG.warn("No such list id " + lotList.getId() + " for owner " + owner);
				inventoryList = new LotSelection();
			}
			LOG.debug("Updating lot list information");
			inventoryList.setName(lotList.getName());
			inventoryList.setSelectedLots(new ArrayList<Long>(lotList.getSelectedLots()));
			LOG.info("Saving list to inventory");
			this.selectionService.save(inventoryList);
		}else
			LOG.debug("Lot list is null!");
	}
	
	/**
	 * @see org.iita.inventory.remote.InventoryService#updateLotQuantities(java.util.List)
	 */
	@Override
	@Transactional
	public void updateLotQuantities(List<LotQuantity> quantities) {
		if (quantities == null || quantities.size() == 0) {
			LOG.warn("Empty quantity update, doing nothing.");
			return;
		}
		LOG.info("Creating new Inventory quantity update");
		QuantityUpdateBulk quantityUpdate = new QuantityUpdateBulk();
		quantityUpdate.setAffectingInventory(true);
		quantityUpdate.setDate(new Date());
		quantityUpdate.setDescription("Mobile Inventory quantity update");
		quantityUpdate.setTransactionType(Type.RSET);
		quantityUpdate.setTransactionSubtype("MONITOR");
		quantityUpdate.setTitle("Mobile update");
		quantityUpdate.setLots(new ArrayList<QuantityUpdate>());

		for (LotQuantity lotQuantity : quantities) {
			QuantityUpdate qu = new QuantityUpdate();
			qu.setDescription(quantityUpdate);
			qu.setLot(this.lotService.load(lotQuantity.getId()));
			qu.setOriginalQty(qu.getLot().getQuantity());
			qu.setQuantity(lotQuantity.getQuantity());
			qu.setScale(qu.getLot().getScale());

			LOG.info("Observed quantity: " + qu.getLot().getItem().getName() + " = " + qu.getQuantity() + " " + qu.getScale());
			quantityUpdate.getLots().add(qu);
		}

		LOG.info("Storing quantity update");
		this.quantityUpdateService.store(quantityUpdate);
		LOG.info("Committing quantity update");
		this.quantityUpdateService.commit(quantityUpdate);
	}
	
	/**
	 * @see org.iita.inventory.remote.InventoryService#updateLotVariables(java.util.List)
	 */
	@Override
	@Transactional
	public void updateLotVariables(List<LotVariable> lotVariables) throws InventoryException {
		if (lotVariables == null || lotVariables.size() == 0) {
			LOG.warn("Empty lot variable update, doing nothing.");
			return;
		}
		LOG.info("Creating new Inventory lot variable update");
		LotVariableUpdateBulk lotVariableUpdate = new LotVariableUpdateBulk();
		lotVariableUpdate.setAffectingInventory(true);
		lotVariableUpdate.setDate(new Date());
		lotVariableUpdate.setDescription("Mobile Inventory lot variable update");
		lotVariableUpdate.setTransactionType(org.iita.inventory.model.Transaction3.Type.RSET);
		lotVariableUpdate.setTransactionSubtype("MONITOR");
		lotVariableUpdate.setTitle("Mobile update");
		
		try{
			for (LotVariable lotVariable : lotVariables) {
				LotVariableUpdate lvu = new LotVariableUpdate();
				lvu.setDescription(lotVariableUpdate);
				
				org.iita.inventory.model.Lot lot = new org.iita.inventory.model.Lot();
				lot=this.lotService.load(lotVariable.getLotId());
				if(lot!=null)
					lvu.setLot(lot);
				else{
					if(lotVariable.getBarCode()!=null){
						lot=this.lotService.loadByBarcode(lotVariable.getBarCode());
						lvu.setLot(lot);
					}else
						throw new InventoryException("LotVariable lot could not be found.");
				}
				
				org.iita.inventory.model.Variables var = new org.iita.inventory.model.Variables();
				var = this.variablesService.loadVariable(lotVariable.getVariableId());
				if(var!=null)
					lvu.setVariable(var);
				else
					lvu.setVariable(null);
				
				org.iita.inventory.model.LotVariable lotVar = new org.iita.inventory.model.LotVariable();
				lotVar = this.lotVariableUpdateService.loadForUpdate(var, lot);
				if(lotVar!=null){
					lvu.setLotVariable(lotVar);
					LOG.info("Lot Variable found with ID: " + lotVariable.getId());
				}else{
					org.iita.inventory.model.LotVariable newLotVariable = new org.iita.inventory.model.LotVariable();
					newLotVariable.setLot(this.lotService.load(lotVariable.getLotId()));
					newLotVariable.setQuantity(lotVariable.getQuantity());
					newLotVariable.setVariabledate(lotVariable.getVariabledate());
					newLotVariable.setVariable(this.variablesService.loadVariable(lotVariable.getVariableId()));
					this.lotVariableService.updateLotVariables(newLotVariable);
					lvu.setLotVariable(newLotVariable);
					LOG.info("Lot Variable not found with ID: " + lotVariable.getId() + " and created a new entity");
				}
				
				/*lotVar = this.lotVariableService.loadLotVariable(lotVariable.getId());*/
				if(lvu.getLotVariable().getQuantity()!=null && lvu.getLotVariable().getQuantity()>0)
					lvu.setOriginalQty(lvu.getLotVariable().getQuantity());
				else
					lvu.setOriginalQty(0.0d);
				
				if(lotVariable.getQuantity()>0)
					lvu.setQuantity(lotVariable.getQuantity());
				else
					lvu.setQuantity(0.0d);
				
				
				
				if(lotVariable.getVariabledate()!=null)
					lvu.setVariableDate(lotVariable.getVariabledate());
				else
					lvu.setVariableDate(null);
				
				LOG.info("Observed lot variable: " + lvu.getLot().getItem().getName() + " = " + lvu.getQuantity());
				LOG.info("Lot Variable ID: " + lotVariable.getId());
				LOG.info("Lot Variable: " + this.lotVariableService.loadLotVariable(lotVariable.getId()));
				LOG.info("Lot: " + this.lotService.load(lotVariable.getLotId()));
				lotVariableUpdate.getLotVariables().add(lvu);
			}
			LOG.info("Storing lotvariable data update");
			this.lotVariableUpdateService.store(lotVariableUpdate);
			LOG.info("Committing lotvariable data update");
			this.lotVariableUpdateService.commit(lotVariableUpdate);
		}catch(PropertyValueException e){
			LOG.debug(e);
			throw e;
		}
	}
	

	@Override
	@Transactional
	public void updateLotSubtypes(List<LotSubtypeQuantity> lotSubtypes) {
		if (lotSubtypes == null || lotSubtypes.size() == 0) {
			LOG.warn("Empty lot subtype quantity update, doing nothing.");
			return;
		}
		LOG.info("Creating new Inventory lot subtype quantity update");
		
		//LotService lService = new org.iita.inventory.service.impl.LotServiceImpl();
		
		QuantityUpdateBulk lotSubtypeUpdate = new QuantityUpdateBulk();
		lotSubtypeUpdate.setAffectingInventory(true);
		lotSubtypeUpdate.setDate(new Date());
		lotSubtypeUpdate.setDescription("Mobile Inventory lot subtype quantity update");
		lotSubtypeUpdate.setTransactionType(org.iita.inventory.model.Transaction2.Type.OUT);
		lotSubtypeUpdate.setTransactionSubtype("MONITOR");
		lotSubtypeUpdate.setTitle("Mobile update");
		lotSubtypeUpdate.setLots(new ArrayList<QuantityUpdate>());
		
		for (LotSubtypeQuantity lotSubtypeQuantity : lotSubtypes) {
			QuantityUpdate qu = new QuantityUpdate();
			qu.setDescription(lotSubtypeUpdate);
			qu.setLot(this.lotService.load(lotSubtypeQuantity.getLotId()));
			qu.setOriginalQty(qu.getLot().getQuantity());
			qu.setQuantity(lotSubtypeQuantity.getQuantity());
			qu.setScale(qu.getLot().getScale());
			
			LOG.info("SubType: " + lotSubtypeQuantity.getSubtype().toUpperCase());
			qu.getDescription().setTransactionSubtype(lotSubtypeQuantity.getSubtype().toUpperCase());

			qu.getDescription().setLastUpdated(new Date());
			qu.getDescription().setLastUpdatedBy(lotSubtypeQuantity.getUpdatedBy());

			lotSubtypeUpdate.getLots().add(qu);
		}
		
		if(lotSubtypeUpdate.getLots().size()!=0){
			LOG.info("Storing lot subtype data update: " + lotSubtypeUpdate);
			if(this.quantityUpdateService!=null){
				this.quantityUpdateService.store(lotSubtypeUpdate);
				LOG.info("Committing lot subtype data update");
				this.quantityUpdateService.commit(lotSubtypeUpdate);
			}else{
				LOG.info("this.lotSubtypeUpdateService is NULL: " +  this.lotSubtypeUpdateService);
			}
		}
	}

	@Override
	@Transactional
	public void migrateLotToExistingLocation(List<ExistingLocationMigration> existingLocationMigration) {
		if (existingLocationMigration == null || existingLocationMigration.size() == 0) {
			LOG.warn("Empty lot location migration update, doing nothing.");
			return;
		}
		LOG.info("Updating existing Inventory lot location migration update");
		MigrationLotLocationUpdateBulk lotLocationMigrationUpdate = new MigrationLotLocationUpdateBulk();
		lotLocationMigrationUpdate.setAffectingInventory(true);
		lotLocationMigrationUpdate.setDate(new Date());
		lotLocationMigrationUpdate.setLotLocationUpdate(new ArrayList<MigrationLotLocationUpdate>());
		
		for (ExistingLocationMigration lotLocationMigration : existingLocationMigration) {
			MigrationLotLocationUpdate qu = new MigrationLotLocationUpdate();
			qu.setDescription(lotLocationMigrationUpdate);
			qu.setLot(this.lotService.load(lotLocationMigration.getLotId()));
			qu.getDescription().setLastUpdated(new Date());
			qu.getDescription().setLastUpdatedBy(lotLocationMigration.getLastUpdatedBy());
			qu.setFromLocation(lotLocationMigration.getFromLocation());
			qu.setToLocationId(lotLocationMigration.getToLocation());

			LOG.info("Observed location: " + qu.getLot().getItem().getName() + " from " + qu.getToLocation());
			lotLocationMigrationUpdate.getLotLocationUpdate().add(qu);
		}

		LOG.info("Storing lot location data update");
		this.lotLocationMigrationUpdateService.store(lotLocationMigrationUpdate);
		LOG.info("Committing lot location data update");
		this.lotLocationMigrationUpdateService.commit(lotLocationMigrationUpdate);
	}

	@SuppressWarnings("null")
	@Override
	@Transactional
	public void migrateLotToNewLocation(List<NewLocationMigration> newLocationMigration) {
		if (newLocationMigration == null || newLocationMigration.size() == 0) {
			LOG.warn("Empty lot location migration update, doing nothing.");
			return;
		}
		LOG.info("Updating existing Inventory lot location migration update");
		MigrationLotLocationUpdateBulk lotLocationMigrationUpdate = new MigrationLotLocationUpdateBulk();
		lotLocationMigrationUpdate.setAffectingInventory(true);
		lotLocationMigrationUpdate.setDate(new Date());
		lotLocationMigrationUpdate.setLotLocationUpdate(new ArrayList<MigrationLotLocationUpdate>());
		
		for (NewLocationMigration lotLocationMigration : newLocationMigration) {
			MigrationLotLocationUpdate qu = new MigrationLotLocationUpdate();
			qu.setDescription(lotLocationMigrationUpdate);
			qu.setLot(this.lotService.load(lotLocationMigration.getLotId()));
			qu.getDescription().setLastUpdated(new Date());
			//qu.getDescription().setLastUpdatedBy(lotLocationMigration.getLastUpdatedBy());
			qu.setFromLocation(lotLocationMigration.getFromLocation());
			org.iita.inventory.model.Location checkParentLoc = new org.iita.inventory.model.Location();
			org.iita.inventory.model.Location childLoc = new org.iita.inventory.model.Location();
			checkParentLoc = this.locationService.load(lotLocationMigration.getParendId());
			if(checkParentLoc!=null){
				LOG.info("NEW ENTRY");
				childLoc = this.locationService.lookUpChildLoc(checkParentLoc, lotLocationMigration.getToLocation());
				LOG.info("END NEW ENTRY");
				
				if(childLoc==null){
					childLoc = new org.iita.inventory.model.Location();
					childLoc.setName(lotLocationMigration.getToLocation());
					childLoc.setParent(checkParentLoc);
					childLoc = this.locationService.storeLocation(childLoc);
				}
				qu.setToLocationId(childLoc.getId());
				qu.setToLocation(childLoc.getName());
				
				checkParentLoc.addChild(childLoc);
				this.locationService.store(checkParentLoc);
			}else{
				LOG.info("NEW ENTRY NO PARENT");
				childLoc = this.locationService.lookUpChildLoc(checkParentLoc, lotLocationMigration.getToLocation());
				LOG.info("END NEW ENTRY NO PARENT");
				
				if(childLoc==null){
					childLoc = new org.iita.inventory.model.Location();
					childLoc.setName(lotLocationMigration.getToLocation());
					childLoc.setParent(checkParentLoc);
					childLoc = this.locationService.storeLocation(childLoc);
				}
				qu.setToLocationId(childLoc.getId());
				qu.setToLocation(childLoc.getName());
			}
			
			LOG.info("Observed location: " + qu.getLot().getItem().getName() + " from " + qu.getToLocation());
			lotLocationMigrationUpdate.getLotLocationUpdate().add(qu);
		}
		
		if(this.lotLocationMigrationUpdateService!=null){
			LOG.info("Storing lot location data update");
			this.lotLocationMigrationUpdateService.store(lotLocationMigrationUpdate);
		
			LOG.info("Committing lot location data update");
			this.lotLocationMigrationUpdateService.commit(lotLocationMigrationUpdate);
		}else
			LOG.info("Service is null on storing lot location data update");
	}
	
	/**
	 * @see org.iita.inventory.remote.InventoryService#updateLotFieldVariables(java.util.List)
	 */
	@SuppressWarnings("null")
	@Override
	@Transactional
	public void updateLotFieldVariables(List<LotFieldVariable> lotFieldVariables) {
		if (lotFieldVariables == null || lotFieldVariables.size() == 0) {
			LOG.warn("Empty lot field variable update, doing nothing.");
			return;
		}
		org.iita.inventory.model.Lot lot = new org.iita.inventory.model.Lot();
		
		//DELETE DATA FIRST
		/*for (LotFieldVariable lotFieldVariable : lotFieldVariables) {
			
			if(lotFieldVariable.getLotId() != null){
				lot = this.lotService.load(lotFieldVariable.getLotId()); 
				//if(lot.getFieldVariables()!=null){
				//	LOG.info("Removing " + lot.getFieldVariables());
				this.lotService.deleteFieldVariables(lot);
				//}
			}
		}*/
		
		//ASK FOR LOT QUANTITY COMPUTATION FROM VARIABLE QUANTITY
		for (LotFieldVariable lotFieldVariable : lotFieldVariables) {
			//LotService ltService = new LotServiceImpl();
			if(lotFieldVariable.getLotId() != null && lotFieldVariable.getVar() != null){
				//System.out.println("LOT ID: " + lotFieldVariable.getLotId());
				lot = this.lotService.load((long) lotFieldVariable.getLotId()); 
				//lot = ltService.load((long) lotFieldVariable.getLotId()); 
				
				FieldVariables lvu = new FieldVariables();
				lvu =  this.lotService.loadLotFieldVariable(lot,lotFieldVariable.getVar());
				//System.out.println("HERE IS MY LOT: " + lot);
				//lvu =  ltService.loadLotFieldVariable(lot,lotFieldVariable.getVar()); 
				if(lvu==null){
					lvu = new FieldVariables();
					lvu.setLot(lot);
					lvu.setLastUpdated(lotFieldVariable.getLastUpdated());
					lvu.setDate(lotFieldVariable.getDate());
					lvu.setQty(lotFieldVariable.getQty());
					lvu.setVar(lotFieldVariable.getVar());
				
					lot.getFieldVariables().add(lvu);
				}else{
					lvu.setLot(lot);
					lvu.setLastUpdated(lotFieldVariable.getLastUpdated());
					lvu.setDate(lotFieldVariable.getDate());
					lvu.setQty(lotFieldVariable.getQty());
					lvu.setVar(lotFieldVariable.getVar());
					
					this.lotService.mergeLotFieldVariable(lvu);
					//ltService.mergeLotFieldVariable(lvu); 
				}
			}
			LOG.info("Storing lot field variable data from mobile");
			this.lotService.updateFieldVariables(lot);
			//ltService.updateFieldVariables(lot);
		}
	}

	@Override
	public String getServerTimeStamp() {
		java.util.Date date= new java.util.Date();
		return new Timestamp(date.getTime()).toString();
	}
}
