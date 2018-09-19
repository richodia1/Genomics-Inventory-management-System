/**
 * 
 */
package org.iita.inventory.action;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.iita.inventory.model.Variables;
import org.iita.inventory.service.VariablesService;

import com.opensymphony.xwork2.Action;
import com.opensymphony.xwork2.Preparable;

/**
 * @author KOraegbunam
 *
 */
@SuppressWarnings("serial")
public class VariablesAction extends BaseAction implements Preparable  {
	/** Logger */
	private static final Log log = LogFactory.getLog(VariablesService.class);
	/** Lot service */
	private VariablesService service;
	/** Location ID passed through HTTP query string */
	private Long id;
	

	private List<Variables> varResults;
	private Variables variable;
	
	public VariablesAction (VariablesService service) {
		this.service = service;
	}
	
	/**
	 * Load variable from id querystring
	 */
	public void prepare() {
		super.prepare();
		// Load variable if variable id is provided
		if (this.id != null)
			this.variable = this.service.loadVariable(this.id);
	}
	
	/** Default action: list items in selected location */
	public String execute() {
		log.info("Listing variables");
		try {
			varResults = this.service.listVariables();
		} catch (Exception e) {
			addActionError(e.getMessage());
		}

		return Action.SUCCESS;
	}
	
	public String update() {
		try {
			this.service.update(variable);
			if(this.id==null)
				addActionMessage("Variable added successfully!");
			else
				addActionMessage("Variable updated successfully!");
		} catch (Exception e) {
			addActionError(e.getMessage());
		}
		return Action.SUCCESS;
	}
	
	public String remove() {
		try {
			this.service.remove(variable);
			addActionMessage("Variable deleted successfully!");
			return "deleted";
		} catch (Exception e) {
			addActionError(e.getMessage());
		}
		return Action.SUCCESS;
	}
	
	/**
	 * @param variable the variable to set
	 */
	public void setVariable(Variables variable) {
		this.variable = variable;
	}

	/**
	 * @return the variable
	 */
	public Variables getVariable() {
		return variable;
	}

	/**
	 * @param varResults the varResults to set
	 */
	public void setVarResults(List<Variables> varResults) {
		this.varResults = varResults;
	}

	/**
	 * @return the varResults
	 */
	public List<Variables> getVarResults() {
		return varResults;
	}
	
	/**
	 * @return the id
	 */
	public Long getId() {
		return id;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(Long id) {
		this.id = id;
	}
}
