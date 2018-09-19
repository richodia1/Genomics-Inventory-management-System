/**
 * 
 */
package org.iita.inventory.action;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.iita.inventory.model.Location;
import org.iita.inventory.model.Lot;
import org.iita.inventory.service.LocationService;
import org.iita.util.NaturalOrderComparator;

import com.opensymphony.xwork2.Action;
import com.opensymphony.xwork2.Preparable;
import com.opensymphony.xwork2.validator.annotations.RequiredStringValidator;
import com.opensymphony.xwork2.validator.annotations.Validations;

/**
 * Location management.
 * 
 * @author mobreza
 */
@SuppressWarnings("serial")
public class LocationAction extends BaseAction implements Preparable {
	/** Logger */
	private static final Log log = LogFactory.getLog(LocationService.class);

	/** Location ID passed through HTTP query string */
	private Long locid;

	/** Edited location ID */
	private Long editid;

	/** Paging */
	private int startAt = 0;

	/** Paging */
	private int pageSize = 50;

	/** Paging */
	private int totalRecords = 0;

	/** Lots in the selected location */
	private List<Lot> results;

	/** Location for which to display lots */
	private Location location = null;

	/** Edited location */
	private Location editedLocation = null;
	

	/** Location service */
	private LocationService locationService;

	/** Sublocation list */
	private Collection<Location> subLocations;

	public LocationAction(LocationService locationService) {
		this.locationService = locationService;
	}

	/** Default action: list items in selected location */
	public String execute() {
		if (this.location != null) {
			this.editedLocation = this.location;
		}

		return Action.SUCCESS;
	}

	/**
	 * @return the location
	 */
	public Location getLocation() {
		return this.location;
	}

	/**
	 * Get full location path
	 * 
	 * @return Location path (ROOT > Parent > Parent > this.location)
	 */
	public List<Location> getLocationPath() {
		return this.location == null ? null : this.location.getPath();
	}

	/**
	 * @return the locid
	 */
	public Long getLocid() {
		return this.locid;
	}

	/**
	 * @return the pageSize
	 */
	public int getPageSize() {
		return this.pageSize;
	}

	/**
	 * @return the results
	 */
	public List<Lot> getResults() {
		return this.results;
	}

	/**
	 * @return the startAt
	 */
	public int getStartAt() {
		return this.startAt;
	}

	/**
	 * Get list of sublocations sorted by natural order.
	 * 
	 * @return List of sublocations sorted by natural order
	 */
	public Collection<Location> getSubLocations() {
		if (this.subLocations == null) {
			List<Location> loc = new ArrayList<Location>(this.location == null ? this.locationService.listLocations() : this.location.getChildren());
			Collections.sort(loc, new NaturalOrderComparator<Object>());
			this.subLocations = loc;
		}
		return this.subLocations;
	}

	/**
	 * @return the totalRecords
	 */
	public int getTotalRecords() {
		return this.totalRecords;
	}

	/**
	 * Load location from locid querystring
	 */
	public void prepare() {
		super.prepare();
		// Load location if location id is provided
		if (this.locid != null)
			this.location = this.locationService.load(this.locid);
		if (this.editid != null)
			this.editedLocation = this.locationService.load(this.editid);
	}

	/**
	 * @param locid the locid to set
	 */
	public void setLocid(Long locid) {
		this.locid = locid;
	}

	/**
	 * @param startAt the startAt to set
	 */
	public void setStartAt(int startAt) {
		this.startAt = startAt;
	}

	/**
	 * @return the editedLocation
	 */
	public Location getEditedLocation() {
		return this.editedLocation;
	}

	/**
	 * @param editedLocation the editedLocation to set
	 */
	public void setEditedLocation(Location editedLocation) {
		this.editedLocation = editedLocation;
	}

	/**
	 * @return the editid
	 */
	public Long getEditid() {
		return this.editid;
	}

	/**
	 * @param editid the editid to set
	 */
	public void setEditid(Long editid) {
		this.editid = editid;
	}

	/**
	 * Update edited location
	 * 
	 * @return
	 */
	@Validations(requiredStrings = { @RequiredStringValidator(fieldName = "editedLocation.name", message = "Please enter location name") })
	public String update() {
		if (this.editedLocation.getId() == null)
			this.editedLocation.setParent(this.location);
		this.locationService.store(this.editedLocation);
		return "redirect";
	}

	/**
	 * Remove edited location if it has no children.
	 * 
	 * @return
	 */
	public String remove() {
		if (this.editedLocation.getChildren().size() > 0) {
			addActionError("Location cannot be removed as it contains sub locations. Remove those first");
			return Action.ERROR;
		}
		if (this.editedLocation.getId() != null) {
			try {
				this.locationService.remove(this.editedLocation);
				if (this.location.getParent() == null)
					this.locid = null;
				else
					this.locid = this.location.getParent().getId();
				return "redirect";
			} catch (Exception e) {
				log.error("Cannot remove location " + this.editedLocation.getId(), e);
				addActionError("Cannot delete location it is referenced by other locations or lots. (" + e.getMessage() + ")");
				this.editedLocation = null;
				this.location = null;
				return Action.ERROR;
			}
		} else {
			addActionError("Cannot remove a non-persisted location");
			return Action.ERROR;
		}
	}

	/**
	 * Add sub-location
	 */
	public String add() {
		this.editedLocation = new Location();
		this.editedLocation.setParent(this.location);
		return Action.INPUT;
	}

	/**
	 * Action method to regenerate LocationFlat data
	 * 
	 * @see com.opensymphony.xwork2.ActionSupport#validate()
	 */
	public String locationflat() {
		this.locationService.regenerateLocationFlat();
		return Action.SUCCESS;
	}
}
