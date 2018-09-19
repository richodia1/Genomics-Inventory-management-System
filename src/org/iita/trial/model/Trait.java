/**
 * inventory.Struts Jun 30, 2010
 */
package org.iita.trial.model;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Lob;
import javax.persistence.OneToMany;
import javax.persistence.Transient;

import org.iita.entity.VersionedEntity;
import org.iita.util.StringUtil;

/**
 * <p>
 * Traits define what data is captured and how.
 * </p>
 * 
 * @author mobreza
 */
@Entity
public class Trait extends VersionedEntity {
	private static final long serialVersionUID = -1508777586262774542L;
	private String title;
	private String var;
	private String description;
	private String uom;
	private List<TraitCoding> coding;

	/**
	 * Get trait title
	 * 
	 * @return the title
	 */
	public String getTitle() {
		return this.title;
	}

	/**
	 * @param title the title to set
	 */
	public void setTitle(String title) {
		this.title = title;
	}

	/**
	 * Get name of trait variable. E.g. PLANTHGHT for plant height.
	 * 
	 * @return the var
	 */
	@Column(length = 50)
	public String getVar() {
		return this.var;
	}

	/**
	 * Set name of trait variable
	 * 
	 * @param var the var to set
	 */
	@Column(length = 20, nullable = false, unique = true)
	public void setVar(String var) {
		this.var = var;
	}

	/**
	 * @return the description
	 */
	@Lob
	public String getDescription() {
		return this.description;
	}

	/**
	 * @param description the description to set
	 */
	public void setDescription(String description) {
		this.description = description;
	}

	/**
	 * @return the uom
	 */
	@Column(length = 20, nullable = true)
	public String getUom() {
		return this.uom;
	}

	/**
	 * @param uom the uom to set
	 */
	public void setUom(String uom) {
		this.uom = StringUtil.nullOrString(uom);
	}

	/**
	 * @return the coding
	 */
	@OneToMany(cascade = { CascadeType.ALL }, mappedBy = "trait")
	public List<TraitCoding> getCoding() {
		return this.coding;
	}

	/**
	 * @param coding the coding to set
	 */
	public void setCoding(List<TraitCoding> coding) {
		this.coding = coding;
	}

	/**
	 * Return decoded value
	 * 
	 * @param value
	 * @return
	 */
	public String decode(double value) {
		if (this.coding != null && this.coding.size() > 0) {
			for (TraitCoding coding : this.coding) {
				if (coding.getValue() == value) {
					return coding.getCoding();
				}
			}
		}
		return String.format("%1$,.2f", value);
	}

	/**
	 * Is this trait coded?
	 * 
	 * @return
	 */
	@Transient
	public boolean isCoded() {
		return this.coding != null && this.coding.size() > 0;
	}

	/**
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return String.format("%2$s [id=%1$d] as %3$s", this.id, this.title, this.var);
	}
}
