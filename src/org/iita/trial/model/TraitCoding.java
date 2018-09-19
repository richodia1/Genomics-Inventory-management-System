/**
 * inventory.Struts Jul 10, 2010
 */
package org.iita.trial.model;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;

import org.iita.entity.SimpleEntity;

/**
 * @author mobreza
 */
@Entity
public class TraitCoding extends SimpleEntity {
	private static final long serialVersionUID = -4980713313906575584L;
	private Trait trait;
	private double value;
	private String coding;

	/**
	 * @return the trait
	 */
	@ManyToOne(cascade = {}, optional = false)
	public Trait getTrait() {
		return this.trait;
	}

	/**
	 * @param trait the trait to set
	 */
	public void setTrait(Trait trait) {
		this.trait = trait;
	}

	/**
	 * @return the value
	 */
	public double getValue() {
		return this.value;
	}

	/**
	 * @param value the value to set
	 */
	public void setValue(double value) {
		this.value = value;
	}

	/**
	 * @return the coding
	 */
	public String getCoding() {
		return this.coding;
	}

	/**
	 * @param coding the coding to set
	 */
	public void setCoding(String coding) {
		this.coding = coding;
	}
	
	/**
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return String.format("%1$d -  %2$s", this.value, this.coding);
	}
}
