/**
 * accession2.Struts Jan 20, 2010
 */
package org.iita.inventory.action.ajax;

import java.util.List;

import org.iita.inventory.model.LotSelection;
import org.iita.inventory.service.SelectionService;

import com.googlecode.jsonplugin.annotations.SMDMethod;
import com.opensymphony.xwork2.Action;

/**
 * Inventory Ajax methods
 * 
 * @author mobreza
 */
public class InventoryAjaxService {
	private SelectionService selectionService;

	/**
	 * @param selectionService
	 */
	public InventoryAjaxService(SelectionService selectionService) {
		this.selectionService = selectionService;
	}
	
	/**
	 * Add accession to selection
	 * 
	 * @param accessionId Accession ID
	 * @return <code>true</code> if accession is on selection list
	 * @throws SelectionException
	 */
	@SMDMethod
	public boolean addToSelection(long lotId) {
		LotSelection selectedList = this.selectionService.getSelectedList();
		selectedList.addSelection(lotId);
		return selectedList.contains(lotId);
	}

	@SMDMethod
	public boolean addManyToSelection(List<Long> lotIds) {
		LotSelection selectedList = this.selectionService.getSelectedList();
		return selectedList.getSelectedLots().addAll(lotIds);
	}

	/**
	 * Remove accession from selection
	 * 
	 * @param accessionId Accession ID
	 * @return <code>true</code> if accession is no longer selected
	 */
	@SMDMethod
	public boolean removeFromSelection(long accessionId) {
		LotSelection selectedList = this.selectionService.getSelectedList();
		selectedList.removeSelection(accessionId);
		return !selectedList.contains(accessionId);
	}
	
	public String execute() {
		return Action.SUCCESS;
	}
}
