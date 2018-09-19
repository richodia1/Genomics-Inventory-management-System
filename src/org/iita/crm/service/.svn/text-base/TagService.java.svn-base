/**
 * promisCRM.Struts Apr 21, 2010
 */
package org.iita.crm.service;

import java.util.List;
import java.util.Map;

import org.iita.crm.model.Tag;
import org.iita.crm.model.Taggable;
import org.iita.util.PagedResult;

/**
 * @author mobreza
 * 
 */
public interface TagService {

	/**
	 * @param clazz
	 * @param id
	 * @return
	 */
	Tag loadTag(Class<? extends Tag> clazz, Long id);

	/**
	 * @param tag
	 */
	void update(Tag tag);

	void remove(Tag tag);

	/**
	 * @param text
	 * @param i
	 * @return
	 */
	List<String> autocompleteTag(String text, int i);

	/**
	 * List all objects with some tag
	 * 
	 * @param tag
	 * @param startAt
	 * @param maxResults
	 * @return
	 */
	PagedResult<?> list(String tag, int startAt, int maxResults);

	/**
	 * @param size Number of tags to include in cloud
	 * @return
	 */
	TagCloud getCloud(int size);

	/**
	 * Get all available tags for particular category
	 * 
	 * @param <T>
	 * @param clazz
	 * @param category
	 * @return
	 */
	<T> List<String> getTagsForCategory(Class<T> clazz, String category);
	
	/**
	 * Get list of tag categories
	 * @param <T>
	 * @param clazz
	 * @return
	 */
	<T> List<String> getTagCategories(Class<T> clazz);
	
	/**
	 * Bulk update tags
	 * @param <T>
	 * @param entity
	 * @param usedTags
	 * @param map
	 */
	<T extends Taggable<T>> void bulkUpdateTags(T entity, List<String> usedTags, Map<String, Double> map);

	/**
	 * @param entity
	 * @param category
	 * @return
	 */
	List<String> getTagsInCategory(Taggable<?> entity, String category);
}
