/**
 * 
 */
package org.iita.inventory.model;

import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.Vector;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.persistence.UniqueConstraint;
import javax.persistence.Version;

import org.hibernate.annotations.Where;
import org.hibernate.search.annotations.Boost;
import org.hibernate.search.annotations.ClassBridge;
import org.hibernate.search.annotations.DocumentId;
import org.hibernate.search.annotations.Field;
import org.hibernate.search.annotations.Index;
import org.hibernate.search.annotations.Indexed;
import org.hibernate.search.annotations.IndexedEmbedded;
import org.hibernate.search.annotations.Store;
import org.iita.util.StringUtil;

import com.opensymphony.xwork2.conversion.annotations.Conversion;
import com.opensymphony.xwork2.conversion.annotations.TypeConversion;

/**
 * @author mobreza
 */
@Conversion
@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@Indexed
@ClassBridge(name = "description", index = Index.TOKENIZED, store = Store.NO, impl = org.iita.inventory.lucene.ItemBridge.class)
@Table(uniqueConstraints = { @UniqueConstraint(columnNames = { "itemType", "name" }) })
public class Item {
	/** Unique identifier */
	private Long id;

	/** Hibernate versioning */
	private Integer version;

	/** Unique name */
	private String name;

	/** IITA Crop prefix */
	private String prefix;

	/** IITA Accession identifier - within prefix */
	private Long accessionIdentifier;

	/** Collection code/Alternative identifier */
	private String alternativeIdentifier;

	/** Item type */
	private ItemType itemType;

	/** Last modification date */
	private Date dateLastModified;

	/** Item notes */
	private String notes;

	/** Item lots */
	private Set<Lot> lots = new HashSet<Lot>();

	private String latinName;

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
	@Column(length = 100, nullable = false)
	@Field(index = Index.TOKENIZED, store = Store.YES)
	@Boost(value = 2.0f)
	public String getName() {
		return this.name;
	}

	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = StringUtil.nullOrString(name);
	}

	/**
	 * @return the prefix
	 */
	@Column(length = 10)
	public String getPrefix() {
		return this.prefix;
	}

	/**
	 * @param prefix the prefix to set
	 */
	public void setPrefix(String prefix) {
		this.prefix = StringUtil.nullOrString(prefix);
	}

	/**
	 * @return the accessionIdentifier
	 */
	public Long getAccessionIdentifier() {
		return this.accessionIdentifier;
	}

	/**
	 * @param accessionIdentifier the accessionIdentifier to set
	 */
	public void setAccessionIdentifier(Long accessionIdentifier) {
		this.accessionIdentifier = accessionIdentifier;
	}

	/**
	 * @return the alternativeIdentifier
	 */
	@Column(length = 255)
	@Field(index = Index.TOKENIZED, store = Store.NO)
	@Boost(value = 1.5f)
	public String getAlternativeIdentifier() {
		return this.alternativeIdentifier;
	}

	/**
	 * @param alternativeIdentifier the alternativeIdentifier to set
	 */
	public void setAlternativeIdentifier(String alternativeIdentifier) {
		this.alternativeIdentifier = StringUtil.nullOrString(alternativeIdentifier);
	}

	/**
	 * Alias
	 * 
	 * @return the accessionIdentifier
	 */
	@Transient
	public Long getAccId() {
		return this.accessionIdentifier;
	}

	/**
	 * Alias
	 * 
	 * @param accessionIdentifier the accessionIdentifier to set
	 */
	public void setAccId(Long accessionIdentifier) {
		this.accessionIdentifier = accessionIdentifier;
	}

	/**
	 * @return the itemType
	 */
	@ManyToOne(optional = false)
	@JoinColumn(name = "itemType")
	@TypeConversion(converter = "genericConverter")
	@IndexedEmbedded
	public ItemType getItemType() {
		return this.itemType;
	}

	/**
	 * @param itemType the itemType to set
	 */
	public void setItemType(ItemType itemType) {
		this.itemType = itemType;
	}

	/**
	 * @return the notes
	 */
	@Lob
	@Field(index = Index.TOKENIZED, store = Store.NO)
	public String getNotes() {
		return this.notes;
	}

	/**
	 * @param notes the notes to set
	 */
	public void setNotes(String notes) {
		this.notes = StringUtil.nullOrString(notes);
	}

	/**
	 * @return the dateLastModified
	 */
	@Column(nullable = false)
	public Date getDateLastModified() {
		return this.dateLastModified;
	}

	/**
	 * @param dateLastModified the dateLastModified to set
	 */
	public void setDateLastModified(Date dateLastModified) {
		this.dateLastModified = dateLastModified;
	}

	/**
	 * @return the lots
	 */
	@OneToMany(cascade = {}, mappedBy = "item")
	@OrderBy("location, quantity")
	@Where(clause="YEAR(lastUpdated)>=2013 or lastUpdated IS NULL")
	public Set<Lot> getLots() {
		return this.lots;
	}

	/**
	 * @param lots the lots to set
	 */
	public void setLots(Set<Lot> lots) {
		this.lots = lots;
	}

	/**
	 * @return the lots
	 */
	@Transient
	public List<Lot> getLotList() {
		return new Vector<Lot>(getLots());
	}

	/**
	 * @return the latinName
	 */
	@Column(length = 180)
	public String getLatinName() {
		return this.latinName;
	}

	/**
	 * @param latinName the latinName to set
	 */
	public void setLatinName(String latinName) {
		this.latinName = latinName;
	}

	/*
	 * (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		String x = "";
		if (this.itemType != null)
			x += this.itemType + " ";
		if (this.accessionIdentifier == null)
			return x + this.alternativeIdentifier;
		else if (this.alternativeIdentifier == null)
			return x + this.prefix + "-" + this.accessionIdentifier;
		else
			return x + this.prefix + "-" + this.accessionIdentifier + " [" + this.alternativeIdentifier + "]";
	}
}
