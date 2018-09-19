/**
 * 
 */
package org.iita.inventory.action;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts2.ServletActionContext;

import com.opensymphony.xwork2.Action;

/**
 * @author mobreza
 */
@SuppressWarnings("serial")
public class DashboardAction extends BaseAction {
	/**
	 * @see org.iita.struts.BaseAction#execute()
	 */
	@Override
	public String execute() {
		if (isMobile())
			return "mobile";

		return Action.SUCCESS;
	}

	/**
	 * @return
	 */
	private boolean isMobile() {
		HttpServletRequest request = ServletActionContext.getRequest();
		String userAgent = request.getHeader("user-agent");
		if (userAgent != null) {
			LOG.debug("User-Agent: " + userAgent);
			userAgent = userAgent.toLowerCase();
			if (userAgent.contains("windows ce"))
				return true;
			if (userAgent.contains("iemobile"))
				return true;
		}
		return false;
	}
}
