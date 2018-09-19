package org.iita.inventory.model;

import java.io.Serializable;

import javax.persistence.AssociationOverride;
import javax.persistence.AssociationOverrides;
import javax.persistence.Embeddable;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

/**
 * LocationFlat stores the "flat" version of all parent-child relationships; SELECT id FROM LocationFlat WHERE parentId=23 will return all children of the
 * location and all subchildren...
 * 
 * @author mobreza
 * 
 */
@Entity
@Table(name = "LocationFlat")
@AssociationOverrides( { @AssociationOverride(name = "pk.parent", joinColumns = @JoinColumn(name = "parentId")),
		@AssociationOverride(name = "pk.child", joinColumns = @JoinColumn(name = "id")) })
public class LocationFlat {
	private LocationFlatPk pk;

	@EmbeddedId
	public LocationFlatPk getPk() {
		return pk;
	}

	public void setPk(LocationFlatPk pk) {
		this.pk = pk;
	}

	@Transient
	public Location getParent() {
		return getPk().getParent();
	}

	public void setParent(Location parent) {
		this.getPk().setParent(parent);
	}

	@Transient
	public Location getChild() {
		return getPk().getChild();
	}

	public void setChild(Location child) {
		this.getPk().setChild(child);
	}
}

@Embeddable
class LocationFlatPk implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -9038929789201584410L;
	private Location parent;
	private Location child;

	@ManyToOne(cascade = {})
	public Location getParent() {
		return parent;
	}

	public void setParent(Location parent) {
		this.parent = parent;
	}

	@ManyToOne(cascade = {})
	public Location getChild() {
		return child;
	}

	public void setChild(Location child) {
		this.child = child;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((child == null) ? 0 : child.hashCode());
		result = prime * result + ((parent == null) ? 0 : parent.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		LocationFlatPk other = (LocationFlatPk) obj;
		if (child == null) {
			if (other.child != null) {
				return false;
			}
		} else if (!child.equals(other.child)) {
			return false;
		}
		if (parent == null) {
			if (other.parent != null) {
				return false;
			}
		} else if (!parent.equals(other.parent)) {
			return false;
		}
		return true;
	}

}
