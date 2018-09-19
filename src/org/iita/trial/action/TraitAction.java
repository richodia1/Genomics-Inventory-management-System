/**
 * inventory.Struts Nov 2, 2010
 */
package org.iita.trial.action;

import org.iita.inventory.action.BaseAction;
import org.iita.trial.model.Trait;
import org.iita.trial.service.TraitService;
import org.iita.util.PagedResult;

import com.opensymphony.xwork2.Action;

/**
 * @author mobreza
 */
@SuppressWarnings("serial")
public class TraitAction extends BaseAction {
	private static final int maxRecords = 20;
	private TraitService traitService;
	private Long id;
	private Trait trait;
	private int startAt=0;
	private PagedResult<Trait> paged;

	/**
	 * @return the paged
	 */
	public PagedResult<Trait> getPaged() {
		return this.paged;
	}
	
	/**
	 * @param id the id to set
	 */
	public void setId(Long id) {
		this.id = id;
	}

	/**
	 * @return the trait
	 */
	public Trait getTrait() {
		return this.trait;
	}
	
	/**
	 * @param startAt the startAt to set
	 */
	public void setStartAt(int startAt) {
		this.startAt = startAt;
	}

	/**
	 * @see org.iita.inventory.action.BaseAction#prepare()
	 */
	@Override
	public void prepare() {
		if (this.id != null)
			this.trait = this.traitService.findTrait(this.id);
		if (this.trait == null)
			this.trait = new Trait();
	}

	/**
	 * @param traitService
	 */
	public TraitAction(TraitService traitService) {
		this.traitService = traitService;
	}

	/**
	 * Default action: list traits
	 * 
	 * @see org.iita.struts.BaseAction#execute()
	 */
	@Override
	public String execute() {
		this.paged=this.traitService.listTraits(this.startAt, TraitAction.maxRecords);
		return Action.SUCCESS;
	}

	/**
	 * Trait profile action
	 * 
	 * @return
	 */
	public String profile() {
		return Action.SUCCESS;
	}

	/**
	 * Action: update trait information
	 * 
	 * @return
	 */
	public String update() {
		return "refresh";
	}

	/**
	 * Action: remove trait
	 * 
	 * @return
	 */
	public String remove() {
		return "to-list";
	}
}
