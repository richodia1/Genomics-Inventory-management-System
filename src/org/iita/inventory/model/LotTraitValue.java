/**
 * inventory.Struts Jul 10, 2010
 */
package org.iita.inventory.model;

import javax.persistence.Entity;

import org.iita.trial.model.TraitValue;

/**
 * @author mobreza
 *
 */
@Entity
public class LotTraitValue extends TraitValue<LotTrial, Lot> {
	private static final long serialVersionUID = -40381733218571497L;

}
