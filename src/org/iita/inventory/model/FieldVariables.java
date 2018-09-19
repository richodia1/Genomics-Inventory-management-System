/**
 * 
 */
package org.iita.inventory.model;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;

import org.iita.entity.VersionedEntity;

/**
 * @author KOraegbunam
 *
 */
@Entity
public class FieldVariables extends VersionedEntity {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String var;
	private String qty;
	private Date date;
	private Lot lot;
	
	
	/**
	 * @return the lot
	 */
	@ManyToOne(cascade = {}, optional = false)
	public Lot getLot() {
		return lot;
	}
	/**
	 * @param lot the lot to set
	 */
	public void setLot(Lot lot) {
		this.lot = lot;
	}
	/**
	 * @param var the var to set
	 */
	public void setVar(String var) {
		this.var = var;
	}
	/**
	 * @return the var
	 */
	public String getVar() {
		return var;
	}
	/**
	 * @param qty the qty to set
	 */
	public void setQty(String qty) {
		this.qty = qty;
	}
	/**
	 * @return the qty
	 */
	public String getQty() {
		return qty;
	}
	/**
	 * @param date the date to set
	 */
	public void setDate(Date date) {
		this.date = date;
	}
	/**
	 * @return the date
	 */
	public Date getDate() {
		return date;
	}
}
