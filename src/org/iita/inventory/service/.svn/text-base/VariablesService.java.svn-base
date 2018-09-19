/**
 * 
 */
package org.iita.inventory.service;

import java.util.List;

import org.iita.inventory.model.Variables;

/**
 * @author KOraegbunam
 *
 */
public interface VariablesService {	
	/**
	 * Load variable information from underlying storage.
	 * 
	 * @param id variable identifier
	 * @return Variables
	 */
	public Variables loadVariable(long id);
	
	public List<Variables> listVariables() throws Exception;

	public void update(Variables variable) throws Exception;

	public void remove(Variables variable) throws Exception ;
}
