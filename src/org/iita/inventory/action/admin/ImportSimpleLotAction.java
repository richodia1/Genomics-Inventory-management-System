/**
 * 
 */
package org.iita.inventory.action.admin;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.iita.inventory.action.BaseAction;
import org.iita.inventory.model.Lot;
import org.iita.inventory.service.LotService;
import org.iita.security.Authorize;
import org.iita.service.impl.XLSImportException;
import org.iita.struts.FileUploadAction;

import com.opensymphony.xwork2.Action;
import com.opensymphony.xwork2.ActionContext;

/**
 * @author ken
 *
 */
public class ImportSimpleLotAction extends BaseAction implements FileUploadAction {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static final String XLSIMPORTDATA__1 = "XLSIMPORTDATA__1";
	private List<File> uploads;
	private LotService lotService;
	private List<Object[]> failedLotMappings;
	
	private List<Lot> lots;
	
	/**
	 * @param appraisalService
	 */
	public ImportSimpleLotAction(LotService lotService) {
		this.lotService = lotService;
	}
	
	/**
	 * @return the attendance
	 */
	public List<Lot> getLots() {
		return this.lots;
	}
	
	/**
	 * @see org.iita.struts.BaseAction#prepare()
	 */
	@Override
	public void prepare() {
		
	}
	
	@SuppressWarnings("unchecked")
	public String upload() {
		if (this.uploads == null || this.uploads.size() == 0) {
			addActionError("No file uploaded");
			return Action.ERROR;
		}

		try {
			//System.out.println("UPLOAD TEST TYPE: " + this.type);

			this.failedLotMappings = new ArrayList<Object[]>();

			if (Authorize.hasAny("ROLE_ADMIN"))
				this.lots = this.lotService.previewXLSLots(this.uploads.get(0), this.failedLotMappings, getUser());

			ActionContext.getContext().getSession().put(XLSIMPORTDATA__1, this.lots);
			//ActionContext.getContext().getSession().put(XLSIMPORTDATA__1, this.failedLotMappings);
		} catch (FileNotFoundException e) {
			addActionError(e.getMessage());
			return Action.ERROR;
		} catch (IOException e) {
			addActionError(e.getMessage());
			return Action.ERROR;
		} catch (XLSImportException e) {
			addActionError(e.getMessage());
			return Action.ERROR;
		}

		return Action.SUCCESS;
	}
	
	@SuppressWarnings("unchecked")
	public String save() {
		this.lots = (List<Lot>) ActionContext.getContext().getSession().get(XLSIMPORTDATA__1);
		if (this.lots == null) {
			addActionError("No lots to import or already imported.");
			return Action.ERROR;
		}

		try {
			this.lotService.importXLSLots(this.lots, getUser(), this.failedLotMappings);
			addActionMessage("Lots have been imported");
			ActionContext.getContext().getSession().remove(XLSIMPORTDATA__1);
			return Action.SUCCESS;
		} catch (Exception e) {
			addActionError(e.getMessage());
			LOG.error(e, e);
			return Action.ERROR;
		}
	}
	
	/**
	 * Uploaded files passed through FileUpload interceptor
	 * 
	 * @see org.iita.struts.FileUploadAction#setUploads(java.util.List)
	 */
	@Override
	public void setUploads(List<File> uploads) {
		this.uploads = uploads;
	}

	/**
	 * @see org.iita.struts.FileUploadAction#setUploadsContentType(java.util.List)
	 */
	@Override
	public void setUploadsContentType(List<String> uploadContentTypes) {
		// ignored
	}

	/**
	 * @see org.iita.struts.FileUploadAction#setUploadsFileName(java.util.List)
	 */
	@Override
	public void setUploadsFileName(List<String> uploadFileNames) {
		// ignored
	}
	
	/**
	 * @param failedLotMappings the failedLotMappings to set
	 */
	public void setFailedLotMappings(List<Object[]> failedLotMappings) {
		this.failedLotMappings = failedLotMappings;
	}

	/**
	 * @return the failedLotMappings
	 */
	public List<Object[]> getFailedLotMappings() {
		return failedLotMappings;
	}

}
