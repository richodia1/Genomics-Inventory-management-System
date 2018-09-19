/**
 * inventory.Struts Jul 27, 2010
 */
package org.iita.trial.action;

import org.iita.struts.BaseAction;
import org.iita.trial.model.TraitGroup;
import org.iita.trial.model.Trial;
import org.iita.trial.service.TrialService;

import com.opensymphony.xwork2.Action;
import com.opensymphony.xwork2.conversion.annotations.TypeConversion;

/**
 * Action to handle single Trait group profile page
 * 
 * @author mobreza
 */
@SuppressWarnings("serial")
public class TraitGroupAction extends BaseAction {
	private TraitGroup traitGroup;
	@SuppressWarnings("unused")
	private TrialService<?, Trial<?>, ?, ?> trialService;

	/**
	 * @param trialService 
	 * 
	 */
	public TraitGroupAction(TrialService<?, Trial<?>, ?, ?> trialService) {
		this.trialService=trialService;
	}
	
	/**
	 * @param traitGroup the traitGroup to set
	 */
	@TypeConversion(converter = "genericConverter")
	public void setId(TraitGroup traitGroup) {
		this.traitGroup = traitGroup;
	}

	/**
	 * @return the traitGroup
	 */
	public TraitGroup getTraitGroup() {
		return this.traitGroup;
	}

	/**
	 * @see org.iita.struts.BaseAction#execute()
	 */
	@Override
	public String execute() {
		if (this.traitGroup == null) {
			addActionError("No such trait group.");
			return Action.ERROR;
		}
		return Action.SUCCESS;
	}
}
