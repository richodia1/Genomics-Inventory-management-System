/**
 * inventory.Struts Jul 13, 2010
 */
package org.iita.inventory.action;

import java.util.Date;
import java.util.List;

import org.iita.inventory.model.Location;
import org.iita.inventory.model.Lot;
import org.iita.inventory.model.LotTrial;
import org.iita.inventory.model.QuantityUpdateBulk;
import org.iita.inventory.service.InventoryTrialService;
import org.iita.inventory.service.LocationService;
import org.iita.inventory.service.LotService;
import org.iita.inventory.service.QuantityUpdateService;
import org.iita.inventory.service.SelectionService;
import org.iita.trial.model.TraitGroup;

import com.opensymphony.xwork2.Action;
import com.opensymphony.xwork2.conversion.annotations.TypeConversion;
import com.opensymphony.xwork2.validator.annotations.RequiredFieldValidator;
import com.opensymphony.xwork2.validator.annotations.RequiredStringValidator;
import com.opensymphony.xwork2.validator.annotations.Validations;

/**
 * @author mobreza
 */
@SuppressWarnings("serial")
public class LotTrialCreateAction extends BaseAction {
	private InventoryTrialService inventoryTrialService;
	private SelectionService selectionService;
	private QuantityUpdateService quantityUpdateService;
	private LocationService locationService;
	private LotService lotService;
	private LotTrial trial = new LotTrial();
	private Long bulkId;
	private Long locId;
	private String source;

	private TraitGroup traitGroup;
	private List<TraitGroup> traitGroups;
	private QuantityUpdateBulk quantityUpdate;
	private Location location;
	private List<Lot> importedLots;

	/**
	 * @param inventoryTrialService the inventoryTrialService to set
	 */
	public void setInventoryTrialService(InventoryTrialService inventoryTrialService) {
		this.inventoryTrialService = inventoryTrialService;
	}

	/**
	 * @param lotService the lotService to set
	 */
	public void setLotService(LotService lotService) {
		this.lotService = lotService;
	}

	/**
	 * @param locationService the locationService to set
	 */
	public void setLocationService(LocationService locationService) {
		this.locationService = locationService;
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
	 * @param source the source to set
	 */
	public void setSource(String source) {
		this.source = source;
	}

	/**
	 * @return the source
	 */
	public String getSource() {
		return this.source;
	}

	/**
	 * @return the trial
	 */
	public LotTrial getTrial() {
		return this.trial;
	}

	/**
	 * @param bulkId the bulkId to set
	 */
	public void setBulkId(Long bulkId) {
		this.bulkId = bulkId;
	}

	/**
	 * @param locId the locId to set
	 */
	public void setLocId(Long locId) {
		this.locId = locId;
	}

	/**
	 * @return the locId
	 */
	public Long getLocId() {
		return this.locId;
	}

	/**
	 * @return the bulkId
	 */
	public Long getBulkId() {
		return this.bulkId;
	}

	/**
	 * @param traitGroup the traitGroup to set
	 */
	@TypeConversion(converter = "genericConverter")
	public void setTraitGroup(TraitGroup traitGroup) {
		this.traitGroup = traitGroup;
	}

	/**
	 * @return the traitGroup
	 */
	public TraitGroup getTraitGroup() {
		return this.traitGroup;
	}

	/**
	 * @return the traitGroups
	 */
	public List<TraitGroup> getTraitGroups() {
		return this.traitGroups;
	}

	/**
	 * @return the importedLots
	 */
	public List<Lot> getImportedLots() {
		return this.importedLots;
	}

	/**
	 * @see org.iita.inventory.action.BaseAction#prepare()
	 */
	@Override
	public void prepare() {
		if (this.trial.getDate() == null)
			this.trial.setDate(new Date());

		if (this.bulkId != null)
			this.quantityUpdate = this.quantityUpdateService.load(this.bulkId);
		if (this.locId != null)
			this.location = this.locationService.load(this.locId);

		if (this.source != null && this.source.equalsIgnoreCase("selection")) {
			this.importedLots = this.lotService.getLots(this.selectionService.getSelectedList().getSelectedLots());
		} else if (this.source != null && this.source.equalsIgnoreCase("bulk") && this.quantityUpdate != null) {
			this.importedLots = this.quantityUpdateService.getAffectedLots(quantityUpdate);
		} else if (this.source != null && this.source.equalsIgnoreCase("location") && this.location != null) {
			// get max of 1000 lots!
			this.importedLots = this.lotService.list(this.location, 1000);
		}

		this.traitGroups = this.inventoryTrialService.listTraitGroups();
	}

	/**
	 * @see org.iita.struts.BaseAction#execute()
	 */
	@Override
	public String execute() {
		return Action.SUCCESS;
	}

	@Validations(requiredStrings = { @RequiredStringValidator(fieldName = "trial.name", message = "Trial title is required") }, requiredFields = { @RequiredFieldValidator(fieldName = "trial.date", message = "Trial date is required") })
	public String create() {
		if (this.traitGroup == null) {
			addNotificationMessage("Select a trait group.");
			return execute();
		}

		this.trial.setTraitGroup(this.traitGroup);
		this.inventoryTrialService.update(this.trial);

		if (this.importedLots != null && this.importedLots.size() > 0) {
			addNotificationMessage("Created trial with selected lots.");
			this.inventoryTrialService.ensureEntitiesTrialed(this.trial, this.importedLots);
			return "profile";
		} else {
			addNotificationMessage("Created empty trial.");
			return "profile";
		}
	}
}
