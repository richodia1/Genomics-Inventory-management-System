/**
 * 
 */
package org.iita.inventory.action.admin;

import java.util.List;

import org.apache.struts2.interceptor.validation.SkipValidation;
import org.hibernate.exception.ConstraintViolationException;
import org.iita.inventory.action.BaseAction;
import org.iita.inventory.model.ContainerType;
import org.iita.inventory.service.ItemService;
import org.iita.inventory.service.SelectionService;
import org.springframework.dao.DataIntegrityViolationException;

import com.opensymphony.xwork2.Action;
import com.opensymphony.xwork2.Preparable; // import com.opensymphony.xwork2.validator.annotations.RequiredFieldValidator;
// import com.opensymphony.xwork2.validator.annotations.RequiredStringValidator;
import com.opensymphony.xwork2.validator.annotations.Validation;

// import com.opensymphony.xwork2.validator.annotations.Validations;

/**
 * Action to manage inventory items.
 * 
 * @author koraegbunam
 * 
 */
@SuppressWarnings("serial")
@Validation
public class ContainerTypeAction extends BaseAction implements Preparable {
	/** Item service */
	private ItemService itemService;

	/** Selection service */
	private SelectionService selectionService;

	/** Item ID */
	private Long id;
	private ContainerType ctype;
	private List<ContainerType> ctyperesults = null;

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

	public List<ContainerType> getContainerTypes() {
		return this.itemService.getContainerTypes();
	}

	// Gets selected Item Type
	public ContainerType getCtype() {
		return ctype;
	}

	// Sets selected Item Type
	public void setCtype(ContainerType ctype) {
		this.ctype = ctype;
	}

	// Finds Item Type by ID for editing
	public String editContainerType() {
		if (this.id != null) {
			this.ctype = this.itemService.findContainerType(id);
			return Action.SUCCESS;
		} else {
			this.ctype = null;
			return Action.SUCCESS;
		}

	}

	/**
	 * @return the id
	 */
	public Long getId() {
		return this.id;
	}

	public List<ContainerType> getCtyperesults() {
		return this.ctyperesults;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(Long id) {
		this.id = id;
	}

	public void setCtyperesults(List<ContainerType> typeresults) {
		this.ctyperesults = typeresults;
	}

	public ContainerTypeAction(ItemService itemService) {
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
	public String execute() {
		this.ctyperesults = this.getContainerTypes();
		return Action.SUCCESS;
	}

	// @Validations(requiredStrings = { @RequiredStringValidator(fieldName =
	// "item.name", message = "", key = "item.admin.nameRequired",trim = true)
	// }, requiredFields = { @RequiredFieldValidator(fieldName =
	// "item.itemType", message = "", key = "item.admin.itemTypeRequired") })
	public String saveContainerType() {
		this.itemService.storeContainerType(this.ctype);
		return "tolist";
	}

	// @Validations(requiredFields = { @RequiredFieldValidator(fieldName =
	// "item", message = "", key = "item.admin.itemRequired") })
	public String removeContainerType() {
		try {
			if (this.itemService.deleteContainerType(this.itemService.findContainerType(id)) == false) {
				addActionError("Container could not be deleted!");
				return Action.ERROR;
			} else {
				return "tolist";
			}
		} catch (DataIntegrityViolationException e) {
			addActionError("Container could not be deleted!");
			return ERROR;
		} catch (ConstraintViolationException e) {
			addActionError("Container could not be deleted. Constraint violation.");
			return ERROR;
		}
	}
}
