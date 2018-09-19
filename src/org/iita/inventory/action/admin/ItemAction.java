/**
 * 
 */
package org.iita.inventory.action.admin;

import java.util.List;

import javax.persistence.PersistenceException;

import org.apache.struts2.interceptor.validation.SkipValidation;
import org.iita.inventory.action.BaseAction;
import org.iita.inventory.model.Item;
import org.iita.inventory.model.ItemType;
import org.iita.inventory.model.Lot;
import org.iita.inventory.model.LotTraitLastValue;
import org.iita.inventory.service.InventoryTrialService;
import org.iita.inventory.service.ItemService;
import org.iita.inventory.service.SelectionService;
import org.iita.util.PagedResult;

import com.opensymphony.xwork2.Action;
import com.opensymphony.xwork2.Preparable;
import com.opensymphony.xwork2.validator.annotations.RequiredFieldValidator;
import com.opensymphony.xwork2.validator.annotations.RequiredStringValidator;
import com.opensymphony.xwork2.validator.annotations.Validation;
import com.opensymphony.xwork2.validator.annotations.Validations;

/**
 * Action to manage inventory items.
 * 
 * @author mobreza
 */
@Validation
public class ItemAction extends BaseAction implements Preparable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/** Item service */
	private ItemService itemService;

	/** Selection service */
	private SelectionService selectionService;

	/** Trials */
	private InventoryTrialService inventoryTrialService;

	/** Search filter string */
	private String itemNameFilter = null;
	/** Friendly */
	private String itemName = null;
	/** Item ID */
	private Long id = null;
	/** Item */
	private Item item = new Item();
	/** Search results */
	private List<Item> results = null;
	/** Search results */
	private List<Lot> lots = null;
	/** Start at */
	private int startat = 0;
	/** Page size */
	private int pagesize = 50;
	private long allResults = 0;

	private List<LotTraitLastValue> traitLastValues;

	/**
	 * @param selectionService the selectionService to set
	 */
	public void setSelectionService(SelectionService selectionService) {
		this.selectionService = selectionService;
	}

	/**
	 * @return the selectionService
	 */
	public SelectionService getSelectionService() {
		return this.selectionService;
	}

	public List<ItemType> getItemTypes() {
		return this.itemService.listItemTypes();
	}

	/**
	 * @return the itemName
	 */
	public String getItemName() {
		return this.itemName;
	}

	/**
	 * @param itemName the itemName to set
	 */
	public void setItemName(String itemName) {
		this.itemName = itemName;
	}

	/**
	 * @return the allResults
	 */
	public long getAllResults() {
		return this.allResults;
	}

	/**
	 * @return the q
	 */
	public String getQ() {
		return this.itemNameFilter;
	}

	/**
	 * @return the id
	 */
	public Long getId() {
		return this.id;
	}

	/**
	 * @return the item
	 */
	public Item getItem() {
		return this.item;
	}

	/**
	 * @return the results
	 */
	public List<Item> getResults() {
		return this.results;
	}
	
	/**
	 * @return the lots
	 */
	public List<Lot> getLots() {
		return this.lots;
	}

	/**
	 * @param q the q to set
	 */
	public void setQ(String q) {
		this.itemNameFilter = q;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(Long id) {
		this.id = id;
	}

	/**
	 * @param item the item to set
	 */
	public void setItem(Item item) {
		this.item = item;
	}

	/**
	 * @param results the results to set
	 */
	public void setResults(List<Item> results) {
		this.results = results;
	}

	/**
	 * @return the startat
	 */
	public int getStartat() {
		return this.startat;
	}

	/**
	 * @return the pagesize
	 */
	public int getPagesize() {
		return this.pagesize;
	}

	/**
	 * @param startat the startat to set
	 */
	public void setStartat(int startat) {
		this.startat = startat;
	}

	public ItemAction(ItemService itemService, InventoryTrialService inventoryTrialService) {
		this.itemService = itemService;
		this.inventoryTrialService = inventoryTrialService;
	}

	/**
	 * @return the traitLastValues
	 */
	public List<LotTraitLastValue> getTraitLastValues() {
		return this.traitLastValues;
	}

	/*
	 * (non-Javadoc)
	 * @see org.iita.inventory.action.BaseAction#prepare()
	 */
	@Override
	public void prepare() {
		super.prepare();

		if (this.id != null) {
			this.item = this.itemService.find(this.id);
			if (this.item == null) {
				addActionError("No Item with id=" + this.id);
			}
		} else if (this.itemName != null) {
			this.item = this.itemService.findByName(this.itemName);
			if (this.item == null) {
				addActionError("No Item with name '" + this.itemName + "'");
			}
		}
	}

	/**
	 * Method "list" uses search to list results.
	 * 
	 * @return
	 */
	@SkipValidation
	public String list() {
		PagedResult<Item> sr = this.itemService.search(this.itemNameFilter, this.startat, this.pagesize);

		if (sr != null) {
			this.results = sr.getResults();
			this.allResults = sr.getTotalHits();
		}

		return Action.SUCCESS;
	}

	@SkipValidation
	public String execute() {
		if (this.item.getId()!=null){
			this.traitLastValues = this.inventoryTrialService.getTraitLastValues(this.item);
			if(isShowHiddenLots())
				this.lots = (List<Lot>) this.item.getLotList();
			else
				this.lots = this.itemService.findValidLots(this.item);
		}
		
		return Action.SUCCESS;
	}

	@Validations(requiredStrings = { @RequiredStringValidator(fieldName = "item.name", message = "", key = "item.admin.nameRequired", trim = true) }, requiredFields = { @RequiredFieldValidator(fieldName = "item.itemType", message = "", key = "item.admin.itemTypeRequired") })
	public String save() {
		try {
			this.itemService.store(this.item);
		} catch (PersistenceException e) {
			addActionError("Duplicate item name '" + this.item.getName() + "' in '" + this.item.getItemType().getName() + "'. " + e.getMessage());
			this.item = this.itemService.find(this.item.getItemType(), this.item.getName());
			if (this.item != null) {
				addActionMessage("Redirecting to existing item <b>" + this.item.getName() + "</b>.");
				return "toitem";
			}
			return Action.ERROR;
		} catch (Exception e) {
			LOG.error(e);
			addActionError(e.getMessage());
			return Action.ERROR;
		}
		return "toitem";
	}

	@Validations(requiredFields = { @RequiredFieldValidator(fieldName = "item", message = "", key = "item.admin.itemRequired") })
	public String remove() {
		try {
			this.itemService.delete(this.item);
			addNotificationMessage("Item " + this.item.getName() + " completely deleted from Inventory");
			return "dashboard";
		} catch (Throwable e) {
			addActionError(e.getMessage());
			return Action.ERROR;
		}
	}
}
