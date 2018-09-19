/**
 * 
 */
package org.iita.inventory.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.Vector;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Transient;
import javax.persistence.Version;

import org.hibernate.search.annotations.ClassBridge;
import org.hibernate.search.annotations.DocumentId;
import org.hibernate.search.annotations.Field;
import org.hibernate.search.annotations.Index;
import org.hibernate.search.annotations.Indexed;
import org.hibernate.search.annotations.IndexedEmbedded;
import org.hibernate.search.annotations.Store;

/**
 * Inventory location.
 * 
 * @author mobreza
 */
@Entity
@Indexed
@ClassBridge(name = "fullpath", index = Index.TOKENIZED, store = Store.NO, impl = org.iita.inventory.lucene.LocationBridge.class)
public class Location {
	/** Unique identifier */
	private Long id;

	/** Hibernate versioning */
	private Integer version;

	/** Unique location name */
	private String name;

	/** Location type */
	private String locationType;

	/** Parent location */
	// disabled cascading of parent, parent must exist prior to persisting this location
	private Location parent;

	/** Parent location */
	private Set<Location> children = new HashSet<Location>();

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
	@Column(nullable = false, length = 100)
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

	/**
	 * @return the parent
	 */
	@ManyToOne(cascade = {}, optional = true)
	@JoinColumn(name = "parentId", nullable = true)
	@IndexedEmbedded(depth = 2)
	public Location getParent() {
		return this.parent;
	}

	/**
	 * @param parent the parent to set
	 */
	public void setParent(Location parent) {
		this.parent = parent;
	}

	/**
	 * @return the children
	 */
	@OrderBy("name")
	@OneToMany(mappedBy = "parent", cascade = CascadeType.ALL)
	// @ContainedIn
	public Set<Location> getChildren() {
		return this.children;
	}

	@Transient
	/*
	 * @param maxDepth
	 * @return
	 */
	public Collection<Location> getSubset(int maxDepth) {
		Set<Location> s = new HashSet<Location>();
		s.add(this);
		if (maxDepth > 0) {
			for (Location ch : this.getChildren()) {
				s.addAll(ch.getSubset(maxDepth - 1));
			}
		}
		return s;
	}

	/**
	 * @param children the children to set
	 */
	public void setChildren(Set<Location> children) {
		this.children = children;
	}

	/**
	 * Register a child location.
	 * 
	 * @param location2
	 */
	public void addChild(Location location2) {
		this.children.add(location2);
		location2.setParent(this);
	}

	/**
	 * Flaten list of all child locations
	 * 
	 * @return flat list of this item + children
	 */
	public List<Location> flatten() {
		List<Location> flat = new Vector<Location>();
		flat.add(this);
		for (Location c : this.getChildren())
			flat.addAll(c.flatten());
		return flat;
	}

	/**
	 * @return the locationType
	 */
	@Column(length = 25)
	@Field(index = Index.TOKENIZED, store = Store.YES)
	public String getLocationType() {
		return this.locationType;
	}

	/**
	 * @param locationType the locationType to set
	 */
	public void setLocationType(String locationType) {
		this.locationType = locationType;
	}

	/**
	 * @return
	 */
	@Transient
	public List<Location> getPath() {
		List<Location> path = new ArrayList<Location>();
		if (this.getParent() != null)
			path.addAll(this.getParent().getPath());
		path.add(this);
		return path;
	}

	/**
	 * Return a string representation of path in format: Location 1 > Location 2 > Location 3 > This location
	 * 
	 * @return A string representation of path in format: Location 1 > Location 2 > Location 3 > This location
	 */
	@Transient
	public String getPathNames() {
		String pathNames = "";
		for (Location pathloc : this.getPath()) {
			if (pathNames.length() > 0)
				pathNames += " > ";
			pathNames += pathloc.getName();
		}
		return pathNames;
	}

	/*
	 * (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return this.name;
	}

}
