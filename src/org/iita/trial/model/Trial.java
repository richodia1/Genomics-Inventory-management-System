/**
 * inventory.Struts Jun 30, 2010
 */
package org.iita.trial.model;

import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.MappedSuperclass;
import javax.persistence.OneToMany;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.iita.entity.VersionedEntity;

/**
 * @author mobreza
 */
@MappedSuperclass
public class Trial<TRAITVALUE> extends VersionedEntity {
	private static final long serialVersionUID = -5295035365121287879L;
	private String name;
	private String description;
	private Date date;
	private List<TRAITVALUE> traitValues;
	private TraitGroup traitGroup;
	private TrialStatus status = TrialStatus.OPEN;

	public enum TrialStatus {
		OPEN, CLOSED
	}

	/**
	 * @return the name
	 */
	@Column(length = 200, nullable = false)
	public String getName() {
		return this.name;
	}

	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
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

	/**
	 * @return the traitValues
	 */
	@OneToMany(cascade = { CascadeType.REMOVE }, mappedBy = "trial", fetch = FetchType.LAZY)
	public List<TRAITVALUE> getTraitValues() {
		return this.traitValues;
	}

	/**
	 * @param traitValues the traitValues to set
	 */
	public void setTraitValues(List<TRAITVALUE> traitValues) {
		this.traitValues = traitValues;
	}

	/**
	 * @return the measuredTraits
	 */
	@ManyToOne(cascade = {})
	public TraitGroup getTraitGroup() {
		return this.traitGroup;
	}

	/**
	 * @param measuredTraits the measuredTraits to set
	 */
	public void setTraitGroup(TraitGroup traitGroup) {
		this.traitGroup = traitGroup;
	}

	/**
	 * @return the status
	 */
	@Enumerated(EnumType.STRING)
	public TrialStatus getStatus() {
		return this.status;
	}

	/**
	 * @param status the status to set
	 */
	public void setStatus(TrialStatus status) {
		this.status = status;
	}
}
