/**
 * inventory.Struts Jun 30, 2010
 */
package org.iita.trial.model;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;

import org.hibernate.annotations.IndexColumn;
import org.iita.entity.VersionedEntity;

/**
 * <p>
 * Traig groups provide means to group traits under one name. Example "experiments" would be "Cassava characterization", "Yam harvest", "Virus indexing"
 * </p>
 * 
 * @author mobreza
 */
@Entity
public class TraitGroup extends VersionedEntity {
	private static final long serialVersionUID = 4201630336979487488L;
	private String title;
	private String shortName;
	private String description;

	// list of traits captured in this experiment
	private List<Trait> traits;

	/**
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
	 * @return the shortName
	 */
	public String getShortName() {
		return this.shortName;
	}

	/**
	 * @param shortName the shortName to set
	 */
	public void setShortName(String shortName) {
		this.shortName = shortName;
	}

	/**
	 * @return the description
	 */
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
	 * @return the traits
	 */
	@ManyToMany(cascade = {})
	@JoinTable(name = "TraitGroupTraits")
	@IndexColumn(name = "position")
	public List<Trait> getTraits() {
		return this.traits;
	}

	/**
	 * @param traits the traits to set
	 */
	public void setTraits(List<Trait> traits) {
		this.traits = traits;
	}
}
