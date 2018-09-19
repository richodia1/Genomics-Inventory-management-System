/**
 * inventory.Struts Jun 30, 2010
 */
package org.iita.trial.model;

import java.util.Date;

import javax.persistence.ManyToOne;
import javax.persistence.MappedSuperclass;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.UniqueConstraint;

import org.iita.entity.MySqlBaseEntity;

/**
 * Trait value
 * 
 * @author mobreza
 */
@MappedSuperclass
@Table(uniqueConstraints = { @UniqueConstraint(columnNames = { "trial_id", "entity_id", "trait_id" }) })
public class TraitValue<TRIAL extends Trial<?>, ENTITY> extends MySqlBaseEntity {
	private static final long serialVersionUID = -4746359724009267986L;
	private TRIAL trial;
	private Trait trait;
	private ENTITY entity;
	private Double value;
	private Date date;

	/**
	 * @return the trial
	 */
	@ManyToOne(cascade = {}, optional = true)
	public TRIAL getTrial() {
		return this.trial;
	}

	/**
	 * @param trial the trial to set
	 */
	public void setTrial(TRIAL trial) {
		this.trial = trial;
	}

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
	 * @return the lot
	 */
	@ManyToOne(cascade = {}, optional = false)
	public ENTITY getEntity() {
		return this.entity;
	}

	/**
	 * @param lot the lot to set
	 */
	public void setEntity(ENTITY entity) {
		this.entity = entity;
	}


	/**
	 * Get trait measured value
	 * 
	 * @return the value
	 */
	public Double getValue() {
		return this.value;
	}

	/**
	 * Set trait measured value
	 * 
	 * @param value the value to set
	 */
	public void setValue(Double value) {
		this.value = value;
	}

	/**
	 * @return the date
	 */
	@Temporal(TemporalType.TIMESTAMP)
	public Date getDate() {
		return this.date;
	}

	/**
	 * @param date the date to set
	 */
	public void setDate(Date date) {
		this.date = date;
	}
	
	@Override
	public String toString() {
		return String.format("%1$s for trait %2$s: %3$f", this.entity, this.trait, this.value);
	}
}
