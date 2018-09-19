/**
 * 
 */
package org.iita.inventory.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinTable;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.CollectionOfElements;
import org.iita.entity.MySqlBaseEntity;
import org.iita.security.model.User;

/**
 * Bean managing selected lots
 * 
 * @author mobreza
 */
@Entity
@Table(name = "LotList")
public class LotSelection extends MySqlBaseEntity implements Serializable {
	private static final long serialVersionUID = -6017681691644644620L;

	private String name = null;
	private User owner = null;
	private List<Long> selectedLots = new ArrayList<Long>();

	public LotSelection() {

	}

	/**
	 * @return the selectedLots
	 */
	@CollectionOfElements
	@JoinTable(name = "LotListLots")
	public List<Long> getSelectedLots() {
		return this.selectedLots;
	}

	/**
	 * @param selectedLots the selectedLots to set
	 */
	public void setSelectedLots(List<Long> selectedLots) {
		this.selectedLots = selectedLots;
	}

	public void clearSelection() {
		this.selectedLots = new ArrayList<Long>();
	}

	/**
	 * Add lot identifier to selection if not present.
	 * 
	 * @param lotId
	 */
	public void addSelection(long lotId) {
		if (!this.selectedLots.contains(new Long(lotId)))
			this.selectedLots.add(lotId);
	}

	/**
	 * Add lot identifier to selection if not present.
	 * 
	 * @param lotId
	 */
	public void addSelection(List<Lot> lots) {
		for (Lot l : lots) {
			if (!this.selectedLots.contains(new Long(l.getId())))
				this.selectedLots.add(l.getId());
		}
	}

	/**
	 * Remove lot identifier from selection. No change if specified lot not in the list.
	 * 
	 * @param lotId
	 */
	public void removeSelection(long lotId) {
		if (this.selectedLots.contains(new Long(lotId)))
			this.selectedLots.remove(new Long(lotId));
	}

	/**
	 * Number of selected items.
	 * 
	 * @return number of items in selection
	 */
	@Transient
	public int getSize() {
		return this.selectedLots.size();
	}

	/**
	 * Check if selection list contains the given lot identifier.
	 * 
	 * @param lotId Lot identifier
	 * @return <code>true</code> if the selection contains the given identifier, <code>false</code> otherwise
	 */
	public boolean contains(Long lotId) {
		return this.selectedLots.contains(lotId);
	}

	/**
	 * Get selection list name.
	 * 
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
	 * Replace current selection list with provided lot list
	 * 
	 * @param lots Lots to be placed on selection list
	 */
	public void replaceSelection(List<Lot> lots) {
		this.clearSelection();
		this.addSelection(lots);
	}

	/**
	 * @return the owner
	 */
	//@ManyToOne(targetEntity=User.class)
	//@JoinColumn(name = "owner_id", referencedColumnName = "id")
	@ManyToOne(cascade = {}, optional = true)
	public User getOwner() {
		return this.owner;
	}

	/**
	 * @param owner the owner to set
	 */
	public void setOwner(User owner) {
		this.owner = owner;
	}
}
