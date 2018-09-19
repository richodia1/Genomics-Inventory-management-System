/**
 * 
 */
package org.iita.inventory.action.admin;

import java.util.List;

import org.apache.struts2.interceptor.validation.SkipValidation;
import org.iita.inventory.balance.BalanceException;
import org.iita.inventory.model.ContainerType;
import org.iita.inventory.model.CryoLotLts;
import org.iita.inventory.model.CryoLotSd;
import org.iita.inventory.model.CryoLotTemp;
import org.iita.inventory.model.FieldLot;
import org.iita.inventory.model.InVitroLot;
import org.iita.inventory.model.Item;
import org.iita.inventory.model.Lot;
import org.iita.inventory.model.OtherLot;
import org.iita.inventory.model.SeedLot;
import org.iita.inventory.printing.BalanceScaleInfo;
import org.iita.inventory.service.BalanceScaleService;
import org.iita.inventory.service.BalanceService;
import org.iita.inventory.service.ItemService;
import org.iita.inventory.service.LotService;
import org.iita.inventory.service.SelectionService;
import org.iita.struts.BaseAction;

import com.opensymphony.xwork2.Action;
import com.opensymphony.xwork2.validator.annotations.RequiredFieldValidator;
import com.opensymphony.xwork2.validator.annotations.Validation;
import com.opensymphony.xwork2.validator.annotations.Validations;

/**
 * Registering new lots is only allowed to managers. The inventory item is passed in through query string (as item).
 * 
 * @author mobreza
 */
@SuppressWarnings("serial")
@Validation
public class RegisterLotAction extends BaseAction {
	private LotService lotService;
	private ItemService itemService;
	/** Balance service */
	private BalanceService balanceService;
	
	/** Balance Scale service */
	private BalanceScaleService scaleService;
	private int scaleId;
	private String cryophase;
	public String getCryophase() {
		return cryophase;
	}

	public void setCryophase(String cryophase) {
		this.cryophase = cryophase;
	}

	private Lot lot = new Lot();
	private Item item;
	private SelectionService selectionService;
	private Long itemId;
	private String lotType = "lot";

	public RegisterLotAction(LotService lotService, ItemService itemService) {
		this.lotService = lotService;
		this.itemService = itemService;
	}

	/**
	 * @param balanceService the balanceService to set
	 */
	public void setBalanceService(BalanceService balanceService) {
		this.balanceService = balanceService;
	}

	public List<ContainerType> getContainerTypes() {
		return this.itemService.getContainerTypes();
	}

	/**
	 * @param selectionService the selectionService to set
	 */
	public void setSelectionService(SelectionService selectionService) {
		this.selectionService = selectionService;
	}
	
	/**
	 * @param scaleService the scaleService to set
	 */
	public void setScaleService(BalanceScaleService scaleService) {
		this.scaleService = scaleService;
	}

	/**
	 * @return the scaleService
	 */
	public BalanceScaleService getScaleService() {
		return this.scaleService;
	}
	
	/**
	 * @param item the item to set
	 */
	public void setItem(Long itemId) {
		this.itemId = itemId;
	}
	
	/**
	 * @return the getItem
	 */
	public Long geItemId() {
		return itemId;
	}

	@Override
	public void prepare() {
		this.item = this.itemService.find(itemId);
		
		if (this.lotType.equalsIgnoreCase("seedlot")) {
			this.lot = new SeedLot();
		} else if (this.lotType.equalsIgnoreCase("invitrolot")) {
			this.lot = new InVitroLot();
		} else if (this.lotType.equalsIgnoreCase("fieldlot")) {
			this.lot = new FieldLot();		
		} else if (this.lotType.equalsIgnoreCase("cryolotlts")) {
				this.lot = new CryoLotLts();
		} else if (this.lotType.equalsIgnoreCase("cryolottemp")) {
				this.lot = new CryoLotTemp();
		} else if (this.lotType.equalsIgnoreCase("cryolotsd")) {
			this.lot = new CryoLotSd();
		} else if (this.lotType.equalsIgnoreCase("otherlot")) {
			this.lot = new OtherLot();
		} else if(this.lotType.equalsIgnoreCase("cryolot")){
			if(this.cryophase!=null){
				if (this.cryophase.equalsIgnoreCase("temp"))
					this.lot = new CryoLotTemp();
				else if (this.cryophase.equalsIgnoreCase("lts"))
					this.lot = new CryoLotLts();
				else if (this.cryophase.equalsIgnoreCase("sd"))
					this.lot = new CryoLotSd();
			}
		}

		if (this.lot != null) {
			this.lot.setScale(lot.getUoms()[0]);
			this.lot.setItem(this.item);
		}
	}

	/**
	 * Default action, returns INPUT to render the form
	 */
	@SkipValidation
	public String execute() {
		if (this.item == null) {
			addActionError("Inventory item not provided.");
			return Action.ERROR;
		}
		
		if (this.lot == null) {
			addActionError("Invalid item type: " + this.item.getClass().getName());
			return Action.ERROR;
		}

		return Action.INPUT;
	}
	
	public String chooseCryo(){	
		if (this.item == null) {
			addActionError("Inventory item not provided.");
			return Action.ERROR;
		}
				
		return Action.INPUT;
	}
	/**
	 * Try reading the weight off the balance. Does not update lot record.
	 * 
	 * @return
	 */
	public String readBalance() {
		Double x = null;
		try {
			//x = this.balanceService.getCurrentWeight();
			BalanceScaleInfo bs = this.scaleService.find(this.scaleId);
			if(bs != null)
				x = this.balanceService.getCurrentWeight(bs.getHost(),bs.getPort());
			else{
				this.addFieldError("lot.weight", "Balance Scale not selected!");
				return Action.INPUT;
			}
		} catch (BalanceException e) {
			// on reading error, just print the error message and quit
			LOG.error("Error reading balance: " + e.getMessage());
			// this.addActionError(e.getMessage());
			this.addFieldError("lot.weight", e.getMessage());
			return Action.INPUT;
		}

		LOG.debug("Got weight: " + x);

		// skip on no weight
		if (x == null)
			return Action.INPUT;
		if (x < 0.0d)
			return Action.INPUT;

		if (this.lot instanceof SeedLot) {
			SeedLot seedLot = (SeedLot) this.lot;

			if (seedLot.getWeight100() == null || seedLot.getWeight100() <= 0.0d) {
				if (seedLot.getQuantity() != null && seedLot.getScale().equalsIgnoreCase("seed")) {
					seedLot.setWeight100(x / seedLot.getQuantity() * 100.0d);
				} else {
					LOG.debug("Just setting the weight");
					// just set the weight
					seedLot.setQuantity(x);
					seedLot.setWeight(x);
					seedLot.setScale("g");
					seedLot.setStatus(1);
				}
			} else {
				double seedCount = (double) Math.round(x / seedLot.getWeight100() * 100);
				LOG.debug("Setting seedCount: " + seedCount);
				// set quantity as seed count
				seedLot.setQuantity(seedCount);
				seedLot.setWeight(x);
				seedLot.setScale("seed");
				seedLot.setStatus(1);
			}
		}
		return Action.INPUT;
	}

	/**
	 * Register lot with the system.
	 * 
	 * @return
	 */
	@Validations(requiredFields = { @RequiredFieldValidator(fieldName = "lot.quantity", message = "Lot quantity must be specified.") })
	public String register() {
		// see if we got data
		if (this.item == null) {
			addActionError("Inventory item not provided.");
			return Action.ERROR;
		}
		if (this.lot == null) {
			addActionError("Invalid item type: " + this.item.getClass().getName());
			return Action.ERROR;
		}

		// persist lot information
		this.lotService.store(this.lot);
		addActionMessage("Registered new lot with id " + this.lot.getId());

		// add to selection
		if (this.selectionService != null)
			this.selectionService.getSelectedList().addSelection(this.lot.getId());

		return Action.SUCCESS;
	}

	/**
	 * @return the lot
	 */
	public Lot getLot() {
		return this.lot;
	}

	/**
	 * @return the lotType
	 */
	public String getLotType() {
		return this.lotType;
	}

	/**
	 * @param lotType the lotType to set
	 */
	public void setLotType(String lotType) {
		this.lotType = lotType;
	}
	
	/**
	 * @param scaleId the scaleId to set
	 */
	public void setScaleId(int scaleId) {
		this.scaleId = scaleId;
	}

	/**
	 * @return the scaleId
	 */
	public int getScaleId() {
		return scaleId;
	}
}
