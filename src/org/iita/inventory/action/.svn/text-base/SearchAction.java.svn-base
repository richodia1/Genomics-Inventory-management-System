/**
 * 
 */
package org.iita.inventory.action;

import java.util.Collection;

import org.iita.inventory.model.Lot;
import org.iita.inventory.service.LabelService;
import org.iita.inventory.service.SearchService;
import org.iita.inventory.service.SelectionService;

import com.opensymphony.xwork2.Action;
import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.Preparable;

/**
 * @author mobreza
 * 
 */
public class SearchAction extends ActionSupport implements Preparable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/** Search query string */
	private String searchQuery = null;

	/** Search results */
	private Collection<Lot> results = null;

	private SearchService searchService;
	private SelectionService selectionService;

	/** Label service */
	private LabelService labelService = null;

	/**
	 * @return the labelService
	 */
	public LabelService getLabelService() {
		return this.labelService;
	}

	/**
	 * @param labelService the labelService to set
	 */
	public void setLabelService(LabelService labelService) {
		this.labelService = labelService;
	}

	/**
	 * @return the selectionService
	 */
	public SelectionService getSelectionService() {
		return this.selectionService;
	}
	
	/**
	 * @param selectionService the selectionService to set
	 */
	public void setSelectionService(SelectionService selectionService) {
		this.selectionService = selectionService;
	}
	
	/**
	 * @return the results
	 */
	public Collection<Lot> getResults() {
		return this.results;
	}

	/**
	 * @return the searchQuery
	 */
	public String getSearchQuery() {
		return this.searchQuery;
	}

	/**
	 * @param searchQuery the searchQuery to set
	 */
	public void setSearchQuery(String searchQuery) {
		this.searchQuery = searchQuery;
	}

	/**
	 * @return the searchQuery
	 */
	public String getQ() {
		return this.searchQuery;
	}

	/**
	 * @param searchQuery the searchQuery to set
	 */
	public void setQ(String searchQuery) {
		this.searchQuery = searchQuery;
	}

	public SearchAction(SearchService searchService) {
		this.searchService = searchService;
	}

	@SuppressWarnings("unchecked")
	public String execute() {
		try {
			this.results = (Collection<Lot>) this.searchService.search(this.searchQuery, Lot.class, new String[] { "item.name", "location.name",
					"item.description", "notes", "item.alternativeIdentifier", "location.parent.name", "location.parent.parent.name",
					"location.parent.parent.parent.name" }, 0, 100);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return Action.SUCCESS;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.opensymphony.xwork2.Preparable#prepare()
	 */
	@Override
	public void prepare() throws Exception {

	}

}
