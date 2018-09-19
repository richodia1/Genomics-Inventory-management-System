/**
 * promisCRM.Struts Aug 16, 2010
 */
package org.iita.crm.action;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import org.iita.crm.model.EntityTag;
import org.iita.crm.model.Taggable;
import org.iita.crm.service.TagService;
import org.iita.inventory.action.BaseAction;

import com.opensymphony.xwork2.Action;

/**
 * @author mobreza
 * 
 */
@SuppressWarnings("serial")
public abstract class TagBuilderAction<ENTITY extends Taggable<ENTITY>> extends BaseAction {
	private ENTITY entity;
	protected TagService tagService;
	private List<String> categories;
	private List<String> usedTag=new ArrayList<String>();
	private Map<String, Double> tagValue=new Hashtable<String, Double>();
	private List<? extends EntityTag<ENTITY>> tags; 

	/**
	 * 
	 */
	public TagBuilderAction(TagService tagService) {
		this.tagService = tagService;
	}

	/**
	 * @return the categories
	 */
	public List<String> getCategories() {
		return this.categories;
	}

	/**
	 * List available tags for category
	 * 
	 * @param category
	 * @return
	 */
	public List<String> getTagsForCategory(String category) {
		return this.tagService.getTagsForCategory(this.entity.getTagClass(), category);
	}

	/**
	 * @return the tags
	 */
	public List<? extends EntityTag<ENTITY>> getTags() {
		return this.tags;
	}

	/**
	 * Find existing tag
	 * 
	 * @param tag
	 * @return
	 */
	public EntityTag<ENTITY> findTag(String tag) {
		LOG.info("Looking up existing tag: " + tag);
		for (EntityTag<ENTITY> entityTag : this.tags) {
			if (entityTag.getTag().equals(tag))
				return entityTag;
		}
		return null;
	}

	/**
	 * @param id entity id
	 */
	public void setId(Long id) {
		this.entity = loadProfile(id);
	}


	/**
	 * @param id
	 * @return
	 */
	protected abstract ENTITY loadProfile(Long id);

	/**
	 * @return the entity
	 */
	public ENTITY getEntity() {
		return this.entity;
	}

	/**
	 * @return the entity
	 */
	public ENTITY getProfile() {
		return this.entity;
	}
	
	/**
	 * @return the usedTag
	 */
	public List<String> getUsedTag() {
		return this.usedTag;
	}
	
	/**
	 * @param usedTag the usedTag to set
	 */
	public void setUsedTag(List<String> usedTag) {
		this.usedTag = usedTag;
	}
	
	/**
	 * @return the tagValue
	 */
	public Map<String, Double> getTagValue() {
		return this.tagValue;
	}
	
	/**
	 * @see org.iita.struts.BaseAction#prepare()
	 */
	@Override
	public void prepare() {
		if (this.entity!=null) {
			this.tags = this.entity.getTags();
			this.categories = this.tagService.getTagCategories(this.entity.getTagClass());
		}
	}
	
	/**
	 * @see org.iita.struts.BaseAction#execute()
	 */
	@Override
	public String execute() {
		if (this.entity == null) {
			addActionError("No entity provided.");
			return Action.ERROR;
		}
		
		return Action.SUCCESS;
	}
	
	/**
	 * Action method to update tags for entity
	 * @return
	 */
	public String update() {
		this.tagService.bulkUpdateTags(this.entity, this.usedTag, this.tagValue);
		return "reload";
	}
}
