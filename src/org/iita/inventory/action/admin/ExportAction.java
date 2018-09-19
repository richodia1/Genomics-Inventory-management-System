package org.iita.inventory.action.admin;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Collection;
import java.util.List;

import org.iita.inventory.action.BaseAction;
import org.iita.inventory.model.ItemType;
import org.iita.inventory.model.Location;
import org.iita.inventory.service.DataAccessService;
import org.iita.inventory.service.LocationService;
import org.iita.inventory.service.SummaryService;
import org.iita.util.DeleteFileAfterCloseInputStream;
import org.iita.util.PagedResult;

import com.opensymphony.xwork2.conversion.annotations.TypeConversion;

/**
 * Export action allows specifying filters to be applied to data, export format settings and finally the export of data itself.
 * 
 * @author mobreza
 */
public class ExportAction extends BaseAction {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/** Needed to access lots */
	private SummaryService summaryService;
	/** Need to access some other data */
	private DataAccessService daoService;
	private LocationService locationService;
	private ExportSettings exportSettings = new ExportSettings();
	private PagedResult<List<Object>> paged;
	private int startAt = 0;
	private int maxRecords = 50;
	private File downloadFile;

	public ExportAction(SummaryService summaryService, LocationService locationService, DataAccessService daoService) {
		this.summaryService = summaryService;
		this.locationService = locationService;
		this.daoService = daoService;
	}

	public ExportSettings getExport() {
		return this.exportSettings;
	}

	public PagedResult<List<Object>> getPaged() {
		return paged;
	}

	public void setStartAt(int startAt) {
		this.startAt = startAt;
	}

	/**
	 * Get list of sublocations sorted by natural order.
	 * 
	 * @return List of sublocations sorted by natural order
	 */
	public Collection<Location> getSubLocations() {
		return this.locationService.getSubLocations(this.exportSettings.getLocation());
	}

	@SuppressWarnings("unchecked")
	public List<ItemType> getItemTypes() {
		return (List<ItemType>) this.daoService.getAll(ItemType.class);
	}

	/**
	 * Startup action.
	 */
	@Override
	public String execute() {
		if (this.exportSettings.type == 0)
			paged = this.summaryService.summarizeByItemType(this.exportSettings.getLocation(), this.exportSettings.getItemType(), this.exportSettings
					.getScale(), startAt, maxRecords);
		else
			paged = this.summaryService.summarizeByItem(this.exportSettings.getLocation(), this.exportSettings.getItemType(), this.exportSettings.getScale(),
					startAt, maxRecords);

		return SUCCESS;
	}

	/**
	 * Action invoked to download XLS of the current thingy
	 * 
	 * @return
	 */
	public String downloadXLS() {
		try {
			this.downloadFile = this.summaryService.summarizeToXLS(this.exportSettings.getLocation(), this.exportSettings.getItemType(), this.exportSettings
					.getScale());
		} catch (Throwable e) {
			LOG.error(e);
			addActionError(e.getMessage());
		}
		return SUCCESS;
	}

	/**
	 * Action invoked to download Summary XLS of the current thingy
	 * 
	 * @return
	 */
	public String downloadSummaryXLS() {
		try {
			this.downloadFile = this.summaryService.summarizeToSummaryXLS(this.exportSettings.getLocation(), this.exportSettings.getItemType(), this.exportSettings
					.getScale(), this.exportSettings.getType());
		} catch (Throwable e) {
			LOG.error(e);
			addActionError(e.getMessage());
		}
		return SUCCESS;
	}
	
	/**
	 * @return the downloadStream
	 */
	public InputStream getDownloadStream() {
		try {
			return new DeleteFileAfterCloseInputStream(this.downloadFile);
		} catch (FileNotFoundException e) {
			LOG.error(e);
			return null;
		}
	}

	public int getDownloadSize() {
		if (this.downloadFile == null)
			return 0;
		return (int) this.downloadFile.length();
	}

	public String getDownloadFilename() {
		return "INV " + this.exportSettings.getLocation().getName() + ".xls";
	}

	/**
	 * Export settings
	 * 
	 * @author mobreza
	 */
	class ExportSettings {
		private ItemType itemType = null;
		private String scale = null;
		private Location location = null;
		private int type = 0;

		@TypeConversion(converter = "genericConverter")
		public ItemType getItemType() {
			return itemType;
		}

		public void setItemType(ItemType itemType) {
			this.itemType = itemType;
		}

		public String getScale() {
			return scale;
		}

		/**
		 * Set the scale to filter by
		 * 
		 * @param scale
		 */
		public void setScale(String scale) {
			if (scale != null && scale.length() == 0)
				scale = null;
			this.scale = scale;
		}

		@TypeConversion(converter = "genericConverter")
		public Location getLocation() {
			return location;
		}

		public void setLocation(Location location) {
			this.location = location;
		}

		public void setType(int reportType) {
			this.type = reportType;
		}

		public int getType() {
			return type;
		}
	}
}
