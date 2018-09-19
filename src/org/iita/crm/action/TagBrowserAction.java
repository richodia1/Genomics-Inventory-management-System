/**
 * promisCRM.Struts Apr 21, 2010
 */
package org.iita.crm.action;

import org.iita.crm.service.TagCloud;
import org.iita.crm.service.TagService;
import org.iita.struts.BaseAction;
import org.iita.util.PagedResult;

import com.opensymphony.xwork2.Action;

/**
 * @author mobreza
 *
 */
@SuppressWarnings("serial")
public class TagBrowserAction extends BaseAction {
	private static final int maxResults = 20;
	private TagService tagService;
	private int startAt=0;
	private String tag;
	private PagedResult<?> paged;
	private TagCloud cloud;
	
	/**
	 * @param tagService 
	 * 
	 */
	public TagBrowserAction(TagService tagService) {
		this.tagService=tagService;
	}
	
	/**
	 * @param tag the tag to set
	 */
	public void setTag(String tag) {
		this.tag = tag;
	}
	
	/**
	 * @return the tag
	 */
	public String getTag() {
		return this.tag;
	}
	
	/**
	 * @return the paged
	 */
	public PagedResult<?> getPaged() {
		return this.paged;
	}

	/**
	 * @param startAt the startAt to set
	 */
	public void setStartAt(int startAt) {
		this.startAt = startAt;
	}
	
	/**
	 * @return the cloud
	 */
	public TagCloud getCloud() {
		return this.cloud;
	}
	
	/**
	 * @see org.iita.struts.BaseAction#execute()
	 */
	@Override
	public String execute() {
		this.paged=this.tagService.list(this.tag, startAt, maxResults);
		return Action.SUCCESS;
	}

	public String cloud() {
		this.cloud=this.tagService.getCloud(300);
		return "cloud";
	}
	
	public String inline() {
		this.cloud=this.tagService.getCloud(30);
		return "cloud-inline";
	}
}
