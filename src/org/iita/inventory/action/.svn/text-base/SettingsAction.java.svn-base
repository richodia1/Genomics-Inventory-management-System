/**
 * inventory.Struts Nov 5, 2010
 */
package org.iita.inventory.action;

import com.opensymphony.xwork2.ActionContext;

/**
 * @author mobreza
 */
@SuppressWarnings("serial")
public class SettingsAction extends BaseAction {
	private boolean showHiddenLots;

	/**
	 * @param showHiddenLots the showHiddenLots to set
	 */
	public void setShowHiddenLots(boolean showHiddenLots) {
		this.showHiddenLots = showHiddenLots;
	}

	@SuppressWarnings("unchecked")
	public String update() {
		LOG.info("Updating session settings 'hidden lots' to: " +this.showHiddenLots);
		ActionContext.getContext().getSession().put(SHOW_HIDDEN_LOTS, this.showHiddenLots);
		return "referer";
	}
}
