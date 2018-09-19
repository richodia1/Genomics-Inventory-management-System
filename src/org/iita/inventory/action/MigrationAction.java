/**
 * 
 */
package org.iita.inventory.action;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import org.iita.inventory.model.Location;
import org.iita.inventory.model.Lot;
import org.iita.inventory.model.Migration;
import org.iita.inventory.service.LocationException;
import org.iita.inventory.service.LocationService;
import org.iita.inventory.service.LotService;
import org.iita.inventory.service.MigrationService;
import org.iita.util.NaturalOrderComparator;
import org.iita.util.PagedResult;

import com.opensymphony.xwork2.Action;
import com.opensymphony.xwork2.Preparable;

/**
 * @author mobreza
 * 
 */
public class MigrationAction extends BaseAction implements Preparable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private LotService lotService;
	private LocationService locationService;

	/** Location ID passed through HTTP query string */
	private Long locid;

	/** Location for which to display lots */
	private Location location = null;
	private Long barCode;
	private String reason;
	private MigrationService migrationService;
	private PagedResult<Migration> migrationsIn;
	private PagedResult<Migration> migrationsOut;
	private int startAt = 0;
	private static int maxResults = 50;

	public MigrationAction(LotService lotService, LocationService locationService, MigrationService migrationService) {
		this.lotService = lotService;
		this.locationService = locationService;
		this.migrationService = migrationService;
	}

	/**
	 * @return the migrationsIn
	 */
	public PagedResult<Migration> getMigrationsIn() {
		return this.migrationsIn;
	}

	/**
	 * @return the migrationsOut
	 */
	public PagedResult<Migration> getMigrationsOut() {
		return this.migrationsOut;
	}

	/**
	 * @param barCode the barCode to set
	 */
	public void setBarCode(Long barCode) {
		this.barCode = barCode;
	}

	/**
	 * @return the location
	 */
	public Location getLocation() {
		return this.location;
	}

	/**
	 * @param locid the locid to set
	 */
	public void setLocid(Long locid) {
		this.locid = locid;
	}

	/**
	 * Set paging startAt parameter
	 * 
	 * @param startAt
	 */
	public void setStartAt(int startAt) {
		this.startAt = startAt;
	}

	/**
	 * @return the reason
	 */
	public String getReason() {
		return this.reason;
	}

	/**
	 * @param reason the reason to set
	 */
	public void setReason(String reason) {
		this.reason = reason;
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
	 * Get list of sublocations sorted by natural order.
	 * 
	 * @return List of sublocations sorted by natural order
	 */
	public Collection<Location> getSubLocations() {
		List<Location> loc = new ArrayList<Location>(this.location == null ? this.locationService.listLocations() : this.location.getChildren());
		Collections.sort(loc, new NaturalOrderComparator<Object>());
		return loc;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.opensymphony.xwork2.Preparable#prepare()
	 */
	@Override
	public void prepare() {
		super.prepare();
		// Load location if location id is provided
		if (this.locid != null)
			this.location = this.locationService.load(this.locid);
	}

	/**
	 * Default action: load migration information for selected location.
	 * 
	 * Loads INs and OUTs for selected location.
	 */
	public String execute() {
		loadMigrations();

		return Action.SUCCESS;
	}

	private void loadMigrations() {
		// load migration logs
		if (this.location == null) {
			this.migrationsIn = this.migrationsOut = this.migrationService.getLastMigrations(startAt, maxResults);
		}

		if (this.location != null) {
			this.migrationsIn = this.migrationService.getMigrationsTo(this.location, startAt, maxResults);
			this.migrationsOut = this.migrationService.getMigrationsFrom(this.location, startAt, maxResults);
		}
	}

	/**
	 * Load lot with scanned barcode and migrate it to current location.
	 * 
	 * @return
	 */
	public String scanLot() {
		loadMigrations();

		if (this.location == null) {
			addActionError("Cannot migrate to ROOT location!");
			return Action.INPUT;
		}

		LOG.info("scanLot: " + this.barCode);
		if (this.barCode == null)
			return Action.INPUT;

		Lot lot = this.lotService.loadByBarcode(this.barCode);

		if (lot == null) {
			addActionError("Lot with barcode " + this.barCode + " not found.");
			return Action.ERROR;
		} else {
			try {
				Migration migration = lotService.migrate(lot, this.location, this.reason);
				// add this migration record to top
				this.migrationsIn.getResults().add(0, migration);
				// remove if overflows
				if (this.migrationsIn.getResults().size() > this.migrationsIn.getMaxResults()) {
					this.migrationsIn.getResults().remove(this.migrationsIn.getResults().size() - 1);
				}
				this.addActionMessage("Item " + lot.getItem().getName() + " migrated to selected location.");
				return Action.INPUT;
			} catch (NullPointerException e) {
				addActionError(e.getMessage());
				return Action.INPUT;
			} catch (LocationException e) {
				addActionError(e.getMessage());
				return Action.INPUT;
			}
		}
	}
}
