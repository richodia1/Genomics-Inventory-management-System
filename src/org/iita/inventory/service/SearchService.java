/**
 * 
 */
package org.iita.inventory.service;

import java.util.Collection;

/**
 * @author mobreza
 * 
 */
public interface SearchService {

	/**
	 * @param searchQuery
	 * @param clazz
	 * @param fields
	 * @param startAt
	 * @param maxResults
	 * @return
	 * @throws Exception
	 */
	Collection<?> search(String searchQuery, Class<?> clazz, String[] fields, int startAt, int maxResults) throws Exception;
}
