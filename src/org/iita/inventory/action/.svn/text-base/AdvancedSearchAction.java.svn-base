package org.iita.inventory.action;

import java.util.List;

import org.iita.inventory.model.Lot;
import org.iita.inventory.service.LabelService;
import org.iita.inventory.service.SearchService;
import org.iita.inventory.service.SelectionService;

import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.Preparable;

/**
 * Advanced search for inventory lots.
 * 
 * @author mobreza
 * 
 */
@SuppressWarnings("serial")
public class AdvancedSearchAction extends ActionSupport implements Preparable {
	private List<Lot> results = null;
	@SuppressWarnings("unused")
	private SearchService searchService;
	private SelectionService selectionService;
	private LabelService labelService;

	public AdvancedSearchAction(SearchService searchService) {
		this.searchService = searchService;
	}

	public List<Lot> getResults() {
		return results;
	}

	public void setSelectionService(SelectionService selectionService) {
		this.selectionService = selectionService;
	}

	public SelectionService getSelectionService() {
		return selectionService;
	}

	public void setLabelService(LabelService labelService) {
		this.labelService = labelService;
	}

	public LabelService getLabelService() {
		return labelService;
	}

	@Override
	public void prepare() throws Exception {

	}

}
