/**
 * promisCRM.Struts Apr 21, 2010
 */
package org.iita.crm.action;

import org.iita.crm.model.Tag;
import org.iita.crm.service.TagService;
import org.iita.struts.BaseAction;

import com.opensymphony.xwork2.Action;

/**
 * @author mobreza
 *
 */
@SuppressWarnings("serial")
public class TagAction extends BaseAction {
	protected Long id;
	protected TagService tagService;
	protected Tag tag;
	private String tagValue;
	
	/**
	 * @param id the id to set
	 */
	public void setId(Long id) {
		this.id = id;
	}
	

	/**
	 * @param tagValue the tagValue to set
	 */
	public void setTagValue(String tagValue) {
		this.tagValue = tagValue;
		if (this.tag != null) {
			this.tag.setTag(this.tagValue);
		}
	}
	
	/**
	 * @param crmService 
	 * 
	 */
	public TagAction(TagService tagService) {
		this.tagService=tagService;
	}
	
	/**
	 * @return the tag
	 */
	public Tag getTag() {
		LOG.error("Returning tag " + this.tag + " of type " + this.tag.getClass());
		return this.tag;
	}
	
	/**
	 * @see org.iita.struts.BaseAction#prepare()
	 */
	@Override
	public void prepare() {
		
	}
	
	public String update() {
		this.tagService.update(this.tag);
		return Action.SUCCESS;
	}
	
	public String remove() {
		this.tagService.remove(this.tag);
		return Action.SUCCESS;
	}
}
