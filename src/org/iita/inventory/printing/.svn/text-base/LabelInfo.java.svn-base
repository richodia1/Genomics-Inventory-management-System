/**
 * 
 */
package org.iita.inventory.printing;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Version;

/**
 * LabelInfo contains information on label dimensions, columns; that is loaded to a specific printer.
 * 
 * @author mobreza
 * 
 */
@Entity
public class LabelInfo {
	private Integer id;
	private Integer version;
	private String name;
	private String shortName;
	private int columns;
	private int labelWidth;
	private int labelHeight;
	private int marginVertical;
	private int marginHorizontal;

	/**
	 * @return the id
	 */
	@Id
	@GeneratedValue
	public Integer getId() {
		return this.id;
	}

	/**
	 * @return the version
	 */
	@Version
	public Integer getVersion() {
		return this.version;
	}

	/**
	 * @return the name
	 */
	@Column(nullable = false, length = 200)
	public String getName() {
		return this.name;
	}

	/**
	 * @return the columns
	 */
	public int getColumns() {
		return this.columns;
	}

	/**
	 * @return the labelWidth
	 */
	public int getLabelWidth() {
		return this.labelWidth;
	}

	/**
	 * @return the labelHeight
	 */
	public int getLabelHeight() {
		return this.labelHeight;
	}

	/**
	 * @return the marginVertical
	 */
	public int getMarginVertical() {
		return this.marginVertical;
	}

	/**
	 * @return the marginHorizontal
	 */
	public int getMarginHorizontal() {
		return this.marginHorizontal;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(Integer id) {
		this.id = id;
	}

	/**
	 * @param version the version to set
	 */
	public void setVersion(Integer version) {
		this.version = version;
	}

	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return the shortName
	 */
	@Column(length = 20)
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
	 * @param columns the columns to set
	 */
	public void setColumns(int columns) {
		this.columns = columns;
	}

	/**
	 * @param labelWidth the labelWidth to set
	 */
	public void setLabelWidth(int labelWidth) {
		this.labelWidth = labelWidth;
	}

	/**
	 * @param labelHeight the labelHeight to set
	 */
	public void setLabelHeight(int labelHeight) {
		this.labelHeight = labelHeight;
	}

	/**
	 * @param marginVertical the marginVertical to set
	 */
	public void setMarginVertical(int marginVertical) {
		this.marginVertical = marginVertical;
	}

	/**
	 * @param marginHorizontal the marginHorizontal to set
	 */
	public void setMarginHorizontal(int marginHorizontal) {
		this.marginHorizontal = marginHorizontal;
	}

}
