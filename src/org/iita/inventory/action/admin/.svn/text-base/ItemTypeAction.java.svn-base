/**
 * 
 */
package org.iita.inventory.action.admin;

import java.util.List;

import org.apache.struts2.interceptor.validation.SkipValidation;
import org.iita.inventory.action.BaseAction;
import org.iita.inventory.model.ItemType;
import org.iita.inventory.service.ItemService;
import org.iita.inventory.service.SelectionService;
import org.iita.struts.AllowedParameters;

import com.opensymphony.xwork2.Action;
import com.opensymphony.xwork2.Preparable;
import com.opensymphony.xwork2.validator.annotations.RequiredFieldValidator;
import com.opensymphony.xwork2.validator.annotations.RequiredStringValidator;
import com.opensymphony.xwork2.validator.annotations.Validation;
import com.opensymphony.xwork2.validator.annotations.Validations;

/**
 * Action to manage inventory items.
 * 
 * @author koraegbunam
 * 
 */
@SuppressWarnings("serial")
@Validation
public class ItemTypeAction extends BaseAction implements Preparable {
	/** Item service */
	private ItemService itemService;

	/** Selection service */
	private SelectionService selectionService;

	/** Item ID */
	private Long id;
	private ItemType itype;
	private List<ItemType> typeresults = null;

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

	// Gets selected Item Type
	public ItemType getItype() {
		return itype;
	}

	// Sets selected Item Type
	public void setItype(ItemType itype) {
		this.itype = itype;
	}

	// Finds Item Type by ID for editing
	public String editType() {
		if (this.id != null) {
			this.itype = this.itemService.findType(id);
			return Action.SUCCESS;
		} else {
			this.itype = null;
			return Action.SUCCESS;
		}
	}

	/**
	 * @return the id
	 */
	public Long getId() {
		return this.id;
	}

	public List<ItemType> getTyperesults() {
		return this.typeresults;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(Long id) {
		this.id = id;
	}

	public void setTyperesults(List<ItemType> typeresults) {
		this.typeresults = typeresults;
	}

	public ItemTypeAction(ItemService itemService) {
		this.itemService = itemService;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.iita.inventory.action.BaseAction#prepare()
	 */
	@Override
	public void prepare() {
		super.prepare();
	}

	@SkipValidation
	@AllowedParameters( {})
	public String execute() {
		this.typeresults = this.itemService.listItemTypes();
		return Action.SUCCESS;
	}

	@Validations(requiredStrings = { @RequiredStringValidator(fieldName = "itype.name", message = "", key = "item.admin.nameRequired"),
			@RequiredStringValidator(fieldName = "itype.shortName", message = "", key = "item.admin.shortNameRequired") })
	@AllowedParameters( { "itype.(name|shortName|version|id)" })
	public String saveType() {
		this.itemService.storeType(this.itype);
		return "tolist";
	}

	@Validations(requiredFields = { @RequiredFieldValidator(fieldName = "id", message = "", key = "item.admin.itemRequired") })
	public String removeType() {
		try {
			this.itemService.deletetype(this.itemService.findType(id));
		} catch (Exception e) {
			LOG.error(e);
			addActionError("Could not delete item type. " + e.getMessage());
			return Action.ERROR;
		}
		return "tolist";
	}
}
