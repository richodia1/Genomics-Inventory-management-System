/**
 * 
 */
package org.iita.inventory.action.admin;

import java.util.List;

import org.iita.inventory.printing.BalanceScaleInfo;
import org.iita.inventory.service.BalanceScaleService;

import com.opensymphony.xwork2.Action;
import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.Preparable;

/**
 * @author ken
 *
 */
public class BalanceScaleAction extends ActionSupport implements Preparable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private BalanceScaleService scaleService;
	private List<BalanceScaleInfo> results;
	/** Passed in with querystring */
	private Integer id;
	private BalanceScaleInfo scale;
	
	/**
	 * @return the results
	 */
	public List<BalanceScaleInfo> getResults() {
		return this.results;
	}

	/**
	 * @return the id
	 */
	public Integer getId() {
		return this.id;
	}

	/**
	 * @return the scale
	 */
	public BalanceScaleInfo getScale() {
		return this.scale;
	}

	/**
	 * @param scaleService the scaleService to set
	 */
	public void setScaleService(BalanceScaleService scaleService) {
		this.scaleService = scaleService;
	}

	/**
	 * @param results the results to set
	 */
	public void setResults(List<BalanceScaleInfo> results) {
		this.results = results;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(Integer id) {
		this.id = id;
	}

	/**
	 * @param scale the scale to set
	 */
	public void setScale(BalanceScaleInfo scale) {
		this.scale = scale;
	}

	public String list() {
		this.results = this.scaleService.list();
		return Action.SUCCESS;
	}

	public String newSystem() {
		// this.printer = new SystemPrinterInfo();
		return input();
		// return Action.SUCCESS;
	}

	public String execute() {
		return Action.SUCCESS;
	}

	public String input() {
		return Action.INPUT;
	}

	public String save() {
		System.out.println("ScaleAction save() scale " + scale);
		try {
			scaleService.store(scale);
		} catch (Exception exp) {
			addActionError(exp.getMessage());
			return Action.ERROR;
		}

		return Action.SUCCESS;

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.opensymphony.xwork2.Preparable#prepare()
	 */
	@Override
	public void prepare() {
		System.out.println("BalanceScaleAction.prepare() id  " + this.id);
		if (this.id != null) {
			this.scale = this.scaleService.find(this.id);
		}
	}
	
	/**
	 * Delete printer service
	 */
	public String delete(){
		if (this.id != null) {
			this.scaleService.deleteScale(this.id);
		}
		if(this.scaleService.find(this.id)==null){
			addActionMessage("Scale deleted successfully.");
			return Action.SUCCESS;
		}else{
			addActionError("Scale failed to be deleted! Try again.");
			return Action.ERROR;
		}
	}

}
