/**
 * 
 */
package org.iita.inventory.service;

import java.util.List;

import org.iita.inventory.model.LotSelection;
import org.iita.security.model.User;

/**
 * @author mobreza
 */
public interface SelectionService {

	/**
	 * Get currently selected list from session.
	 * 
	 * @return Selected list or <c>null</c>.
	 */
	public LotSelection getSelectedList();

	/**
	 * @param list
	 * @param name
	 */
	void save(LotSelection list);

	/**
	 * @param owner
	 * @return
	 */
	List<LotSelection> getLists(User owner);

	/**
	 * @param listId
	 * @param user
	 * @return
	 */
	public LotSelection loadList(Long listId, User user);

	/**
	 * @return
	 */
	LotSelection createNewList();

	/**
	 * @param list
	 */
	public void delete(LotSelection list, User owner);
}