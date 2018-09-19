/**
 * 
 */
package org.iita.inventory.action;

import java.util.Collection;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.iita.inventory.model.Location;
import org.iita.inventory.model.QuantityUpdateBulk;
import org.iita.inventory.service.LocationService;
import org.iita.inventory.service.QuantityUpdateService;
import org.iita.util.PagedResult;

import com.opensymphony.xwork2.Action;
//import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.Preparable;

/**
 * Action to edit Quantity Updates
 * 
 * @author mobreza
 *  @author KOraegbunam
 */
@SuppressWarnings("serial")
public class QuantityUpdateAction extends BaseAction implements Preparable {
	/** Logger */
	protected static final Log log = LogFactory.getLog(QuantityUpdateService.class);
	protected PagedResult<QuantityUpdateBulk> paged;
	protected QuantityUpdateService service;
	private int startAt;
	private String[] subtypes = null;
	private String[] selectedSubtypes;
	private Long locid = null;
	private Long locationid = null;
	private String title = null;
	
	/** Location service */
	private LocationService locationService;
	/** Cached sublocation list */
	private Collection<Location> subLocations;
	
	/** Location for which to display lots */
	private Location location = null;

	/**
	 * @param startAt the startAt to set
	 */
	public void setStartAt(int startAt) {
		this.startAt = startAt;
	}

	public QuantityUpdateAction(QuantityUpdateService service, LocationService locationService) {
		this.service = service;
		this.locationService = locationService;
	}

	public PagedResult<QuantityUpdateBulk> getPaged() {
		return paged;
	}
	
	public void setLocid(Long locid) {
		this.locid = locid;
	}
	
	public void setLocationid(Long locationid) {
		this.locationid = locationid;
	}

	public Long getLocid() {
		return locid;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getTitle() {
		return title;
	}

	public String execute() {
		if(this.title!=null)
			if(this.title.trim().length()==0) this.title = null;
		
		this.paged = this.service.listByType(this.startAt, 50, locationid, title, selectedSubtypes);
		return Action.SUCCESS;
	}

	public int getStartAt() {
		return startAt;
	}

	public String[] getTransactionSubtypes() {
		synchronized (this) {
			if (this.subtypes == null)
				this.subtypes = this.service.getTransactionSubtypes();
		}
		return this.subtypes;
	}

	public String[] getSelectedSubtypes() {
		return this.selectedSubtypes;
	}

	public void setType(String[] selectedSubtypes) {
		this.selectedSubtypes = selectedSubtypes;
	}

	/**
	 * Utility method to generate query string concatenates all selected types into one long
	 * 
	 * <pre>
	 * type=TYPE1&amp;type=TYPE2...
	 * </pre>
	 * 
	 * @return
	 */
	public String getSelectedTypesQueryString() {
		StringBuffer sb = new StringBuffer();
		if (this.selectedSubtypes != null)
			for (String st : this.selectedSubtypes)
				if (st.trim().length() > 0)
					sb.append(sb.length() > 0 ? "&" : "").append("type=").append(st);
		return sb.toString();
	}
	
	/**
	 * Get list of sublocations sorted by natural order.
	 * 
	 * @return List of sublocations sorted by natural order
	 */
	public Collection<Location> getSubLocations() {
		if (this.subLocations == null) {
			this.subLocations = this.locationService.getSubLocations(this.location);
		}
		return this.subLocations;
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
	 * Load location from locid querystring
	 */
	public void prepare() {
		super.prepare();
		// Load location if location id is provided
		if (this.locid != null)
			this.location = this.locationService.load(this.locid);
		else if(this.locationid != null)
			this.location = this.locationService.load(this.locationid);
	}
}
