/**
 * inventory.Struts Jul 3, 2009
 */
package org.iita.inventory.action.admin;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.iita.inventory.action.BaseActionInventory;
import org.iita.inventory.importer.LotReader;
import org.iita.inventory.importer.LotReaderException;
import org.iita.inventory.model.Lot;
import org.iita.inventory.service.LotService;

import com.opensymphony.xwork2.Action;

/**
 * @author mobreza
 * 
 */
@SuppressWarnings("serial")
public class UpdateLotsAction extends BaseActionInventory {
	private LotReader lotReader;
	private LotService lotService;

	private String[] whatToUpdate = null;

	/** The uploads. */
	private List<File> uploads = new ArrayList<File>();

	private List<? extends Lot> lots;

	/**
	 * @param lotReader
	 * @param lotService
	 * 
	 */
	public UpdateLotsAction(LotReader lotReader, LotService lotService) {
		this.lotReader = lotReader;
		this.lotService = lotService;
	}

	/**
	 * @param uploads the uploads to set
	 */
	public void setUpload(List<File> uploads) {
		this.uploads = uploads;
	}
	
	/**
	 * Getter available to list uploaded files
	 * 
	 * @return the uploads
	 */
	public List<File> getUploads() {
		return this.uploads;
	}

	/**
	 * @param uploadFileNames the uploadFileNames to set
	 */
	public void setUploadFileNames(List<String> uploadFileNames) {
	}

	/**
	 * @param uploadContentTypes the uploadContentTypes to set
	 */
	public void setUploadContentTypes(List<String> uploadContentTypes) {
	}

	/**
	 * @return the whatToUpdate
	 */
	public String[] getWhatToUpdate() {
		return this.whatToUpdate;
	}

	/**
	 * @param whatToUpdate the whatToUpdate to set
	 */
	public void setWhatToUpdate(String[] whatToUpdate) {
		this.whatToUpdate = whatToUpdate;
	}

	/**
	 * @return the lots
	 */
	public List<? extends Lot> getLots() {
		return this.lots;
	}

	/**
	 * @see com.opensymphony.xwork2.ActionSupport#execute()
	 */
	@Override
	public String execute() {
		return Action.INPUT;
	}

	public String test() {
		if (this.uploads == null || this.uploads.size() == 0) {
			addActionError("Upload a file first!");
			return Action.INPUT;
		}

		File sourceFile = this.uploads.get(0);
		this.lots = null;
		try {
			lots = this.lotReader.readLots(sourceFile);
		} catch (LotReaderException e) {
			addActionError(e.getMessage());
			return Action.ERROR;
		}

		return Action.SUCCESS;
	}

	public String update() {
		if (this.uploads == null || this.uploads.size() == 0) {
			addActionError("Upload a file first!");
			return Action.INPUT;
		}

		if (this.whatToUpdate.length == 0) {
			addActionError("Select what to update!");
			return Action.INPUT;
		}

		File sourceFile = this.uploads.get(0);

		try {
			this.lots = this.lotReader.readLots(sourceFile);
		} catch (LotReaderException e) {
			addActionError(e.getMessage());
			return Action.ERROR;
		}

		try {
			this.lots = this.lotService.updateFields(this.lots, this.whatToUpdate);
		} catch (Exception e) {
			addActionError("Failed to update lots. No data was changed. Error was: " + e.getMessage());
		}

		return Action.ERROR;
	}
}
