/**
 * 
 */
package org.iita.inventory.action;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.iita.inventory.model.InVitroLot;
import org.iita.inventory.model.Item;
import org.iita.inventory.model.Location;
import org.iita.inventory.model.Lot;
import org.iita.inventory.service.LocationService;
import org.iita.inventory.service.LotService;
import org.iita.util.PagedResult;

import com.opensymphony.xwork2.Action;

/**
 * Inventory browsing.
 * 
 * @author mobreza
 * 
 */
@SuppressWarnings("serial")
public class InventorizeAction extends BaseAction {
	/** Logger */
	private static final Log log = LogFactory.getLog(LotService.class);

	/** Location ID passed through HTTP query string */
	private Long locid;

	/** Paging */
	private int startAt = 0;

	/** Paging */
	private int pageSize = 50;

	/** Paging */
	private long totalRecords = 0;

	/** Lots in the selected location */
	private List<Lot> results;

	/** Lot service */
	private LotService service;

	/** Location for which to display lots */
	private Location location = null;

	/** Location service */
	private LocationService locationService;

	public InventorizeAction(LotService service, LocationService locationService) {
		this.service = service;
		this.locationService = locationService;
	}

	/** Default action: list items in selected location */
	public String execute() {

		return Action.SUCCESS;
	}

	private ArrayList<Lot> generateInsertList(List<Lot> lots) {
		ArrayList<Lot> insertList = new ArrayList<Lot>();
		int itemCount = 0;
		Item lastItem = null;
		Lot prevLot = null;
		for (int i = 0; i < lots.size(); i++) {
			Lot l = lots.get(i);

			if (lastItem == null) {
				itemCount = 1;
				insertList.add(l);
				lastItem = l.getItem();
				prevLot = l;
			} else if (lastItem == l.getItem()) {
				insertList.add(l);
				itemCount++;
				prevLot = l;
			} else {
				// make 3 copies of last lot
				for (int j = 0; j < 3; j++) {
					insertList.add(createCopy(prevLot));
				}
				insertList.add(l);
				lastItem = l.getItem();
				prevLot = l;
			}
		}

		if (prevLot != null) {
			for (int j = 0; j < 3; j++) {
				insertList.add(createCopy(prevLot));
			}
		}
		return insertList;
	}

	private Lot createCopy(Lot prevLot) {
		Lot dupe = null;
		try {
			dupe = prevLot.getClass().newInstance();
			dupe.copyFrom(prevLot);
			dupe.setQuantity(null);
			dupe.setId(null);
			if (dupe instanceof InVitroLot) {
				((InVitroLot) dupe).setRegenerationDate(null);
				((InVitroLot) dupe).setVirusFree(null);
			}
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
		return dupe;
	}

	public String save() {
		List<Lot> toSave = new ArrayList<Lot>();

		// save some
		if (this.results == null) {
			this.addActionError("No results");
			return this.execute();
		}

		for (Lot l : this.results) {
			if (l.getId() != null && l.getQuantity() == null) {
				l.setQuantity(0.0d);
				l.setStatus(-100);
			} else if (l.getId() == null && (l.getQuantity() == null || l.getQuantity() == 0.0d))
				continue;

			if (l.getQuantity() == 0.0d)
				l.setStatus(-100);
			else
				l.setStatus(1);

			toSave.add(l);
		}
		this.service.store(toSave);

		this.execute();
		return "refresh";
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

	public Collection<Location> getSubLocations() {
		return this.location == null ? this.locationService.listLocations() : this.location.getChildren();
	}

	/**
	 * @return the totalRecords
	 */
	public long getTotalRecords() {
		return this.totalRecords;
	}

	/**
	 * Load location from locid querystring
	 */
	public void prepare() {
		// Load location if location id is provided
		if (this.locid != null)
			this.location = this.locationService.load(this.locid);

		System.err.println("Prepare!");

		log.info("Listing Lots pageSize=" + this.pageSize + " startAt=" + this.startAt);
		PagedResult<Lot> results = null;
		if (this.location != null && this.location.getChildren().size() > 0 && this.location.getChildren().iterator().next().getChildren().size() == 0)
			results = this.service.list(location, 1, startAt, pageSize, isShowHiddenLots());
		else
			results = this.service.list(location, 0, startAt, pageSize, isShowHiddenLots());

		List<Lot> lots = results.getResults();
		this.totalRecords = results.getTotalHits();

		// copy lots to result list and create new empty records
		this.results = generateInsertList(lots);
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
}
