/**
 * 
 */
package org.iita.inventory.model;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;

import org.iita.entity.VersionedEntity;

/**
 * @author KOraegbunam
 *
 */
@Entity
public class LotVariable extends VersionedEntity {

	/**
	 * 
	 */
	private static final long serialVersionUID = -705152671236739195L;
	private Variables variable;
	private Lot lot;
	private Date variabledate;
	private Double quantity;
	/** Lot transactions. */
	private Set<Transaction3> transactions = new HashSet<Transaction3>();
	
	/**
	 * @return the variable
	 */
	@ManyToOne(cascade = {}, optional = false)
	@JoinColumn(name = "variable")
	public Variables getVariable() {
		return variable;
	}
	/**
	 * @param variable the variable to set
	 */
	public void setVariable(Variables variable) {
		this.variable = variable;
	}
	/**
	 * @return the lot
	 */
	@ManyToOne(optional = false)
	@JoinColumn(name = "lot")
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
	 * @return the variabledate
	 */
	public Date getVariabledate() {
		return variabledate;
	}
	/**
	 * @param variabledate the variabledate to set
	 */
	public void setVariabledate(Date variabledate) {
		this.variabledate = variabledate;
	}
	/**
	 * @return the quantity
	 */
	public Double getQuantity() {
		return quantity;
	}
	/**
	 * @param quantity the quantity to set
	 */
	public void setQuantity(Double quantity) {
		this.quantity = quantity;
	}
	
	/**
	 * Gets the transactions.
	 * 
	 * @return the transactions
	 */
	@OneToMany(cascade = {}, mappedBy = "lotVariable")
	@OrderBy("date")
	public Set<Transaction3> getTransactions() {
		return this.transactions;
	}

	/**
	 * Sets the transactions.
	 * 
	 * @param transactions the transactions to set
	 */
	public void setTransactions(Set<Transaction3> transactions) {
		this.transactions = transactions;
	}
}
