/**
 * 
 */
package org.iita.inventory.action;

import org.apache.struts2.ServletActionContext;

import com.opensymphony.xwork2.ActionContext;

/**
 * @author mobreza
 */
@SuppressWarnings("serial")
public class BaseAction extends org.iita.struts.BaseAction {

	/**
	 * 
	 */
	protected static final String SHOW_HIDDEN_LOTS = "__SHOW_HIDDEN_LOTS";

	/** Go back to refering page action result */
	public static final String GOBACK = "goback";

	/** Default referer */
	protected String referer = null;

	/**
	 * 
	 */
	public BaseAction() {
		super();
	}

	/**
	 * @return the referer
	 */
	public synchronized String getReferer() {
		if (this.referer == null) {
			this.referer = ServletActionContext.getRequest().getHeader("Referer");
		}
		if (this.referer == null)
			return "index.jspx";
		return this.referer;
	}

	/**
	 * @param referer the referer to set
	 */
	public void setReferer(String referer) {
		this.referer = referer;
	}

	/*
	 * (non-Javadoc)
	 * @see com.opensymphony.xwork2.Preparable#prepare()
	 */
	@Override
	public void prepare() {

	}

	public boolean isShowHiddenLots() {
		Boolean showHiddenLots = (Boolean) ActionContext.getContext().getSession().get(SHOW_HIDDEN_LOTS);
		if (showHiddenLots == null)
			return false;
		return showHiddenLots.booleanValue();
	}
}