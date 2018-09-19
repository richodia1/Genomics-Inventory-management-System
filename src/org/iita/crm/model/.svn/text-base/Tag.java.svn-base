package org.iita.crm.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.Transient;

/**
 * Tag entity
 * 
 * @author koraegbunam
 */
@Entity
@Inheritance(strategy = InheritanceType.JOINED)
public abstract class Tag {
	private Long id;
	private String tag;

	/**
	 * Default constructor
	 */
	public Tag() {

	}

	/**
	 * Copy constructor
	 * 
	 * @param tag2
	 */
	public Tag(Tag source) {
		this.tag = source.tag;
	}

	/**
	 * @return the id
	 */
	@Id
	@GeneratedValue
	public Long getId() {
		return this.id;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(Long id) {
		this.id = id;
	}

	public void setTag(String tag) {
		this.tag = tag;
	}

	@Column(length = 200, nullable = false)
	public String getTag() {
		return tag;
	}

	/**
	 * @param category
	 * @return
	 */
	@Transient
	public boolean isInCategory(String category) {
		return Tag.getTagCategory(this.tag).equalsIgnoreCase(category);
	}

	/**
	 * Return tag without category
	 * 
	 * @return
	 */
	@Transient
	public String getTagNoCat() {
		if (tag == null || tag.length() == 0)
			return tag;
		int catPos = tag.indexOf(":");
		if (catPos == -1)
			return tag;
		else
			return tag.substring(catPos + 1);
	}

	/**
	 * Get tag category. Tag category is the left part of the tag before colon sign ("Testing: No tests" gives "Testing").
	 */
	public static final String getTagCategory(final String tag) {
		if (tag == null || tag.length() == 0)
			return "default";
		int catPos = tag.indexOf(":");
		if (catPos == -1)
			return "default";
		else
			return tag.substring(0, catPos);
	}
}
