/**
 * 
 */
package org.iita.inventory.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Version;

import org.hibernate.search.annotations.DocumentId;
import org.hibernate.search.annotations.Field;
import org.hibernate.search.annotations.Index;
import org.hibernate.search.annotations.Indexed;
import org.hibernate.search.annotations.Store;

/**
 * Inventory ItemType allows for grouping items by their types. In case of genebank, item types are different crops.
 * 
 * @author mobreza
 * 
 */
@Entity
@Indexed
public class ItemType {
	private Long id;
	private Integer version;
	private String name;
	private String shortName;

	/**
	 * @return the id
	 */
	@Id
	@GeneratedValue
	@DocumentId
	public Long getId() {
		return this.id;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(Long id) {
		this.id = id;
	}

	/**
	 * @return the version
	 */
	@Version
	public Integer getVersion() {
		return this.version;
	}

	/**
	 * @param version the version to set
	 */
	public void setVersion(Integer version) {
		this.version = version;
	}

	/**
	 * @return the name
	 */
	@Column(nullable = false, length = 100, unique = true)
	@Field(index = Index.TOKENIZED, store = Store.YES)
	public String getName() {
		return this.name;
	}

	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return this.name;
	}

	/**
	 * Get short name of the item type. Useful for generating URLs and stuff
	 * 
	 * @return
	 */
	@Column(nullable = false, length = 20, unique = true)
	public String getShortName() {
		return this.shortName;
	}

	public void setShortName(String shortName) {
		this.shortName = shortName;
	}
}
