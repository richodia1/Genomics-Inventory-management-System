/**
 * 
 */
package org.iita.inventory.remote;

import java.io.Serializable;


/**
 * @author KOraegbunam
 *
 */
public class FieldVariablesList implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String var;
	
	
	/**
	 * @return the var
	 */
	public String getVar() {
		return var;
	}
	/**
	 * @param var the var to set
	 */
	public void setVar(String var) {
		this.var = var;
	}	
}
