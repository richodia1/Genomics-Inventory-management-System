/**
 * 
 */
package org.iita.inventory.action;

import java.io.File;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

import org.iita.inventory.importer.ImportException;
import org.iita.inventory.importer.Importer;
import org.iita.inventory.model.Lot;
import org.iita.inventory.service.DataAccessService;
import org.iita.inventory.service.ItemService;
import org.iita.inventory.service.LotService;

import com.opensymphony.xwork2.Action;
import com.opensymphony.xwork2.ActionSupport;

/**
 * The Class ImportAction.
 * 
 * @author mobreza
 */
public class ImportAction extends ActionSupport {
	
	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;
	
	/** The uploads. */
	private List<File> uploads = new ArrayList<File>();
	
	/** The upload file names. */
	private List<String> uploadFileNames = new ArrayList<String>();
	
	/** The upload content types. */
	private List<String> uploadContentTypes = new ArrayList<String>();
	
	/** The lot service. */
	private LotService lotService = null;
	
	/** The importer. */
	private String importer = null;
	
	/** The charset. */
	private String charset = "UTF-8";
	
	/** The imported lots. */
	private List<? extends Lot> importedLots;
	
	/** The dao. */
	private DataAccessService dao;
	
	/** The item service. */
	private ItemService itemService;

	/**
	 * Sets the charset.
	 * 
	 * @param charset the new charset
	 */
	public void setCharset(String charset) {
		this.charset = charset;
	}
	
	public String getCharset() {
		return charset;
	}
	
	/**
	 * Gets the importer.
	 * 
	 * @return the importer
	 */
	public String getImporter() {
		return this.importer;
	}

	/**
	 * Sets the importer.
	 * 
	 * @param importer the importer to set
	 */
	public void setImporter(String importer) {
		this.importer = importer;
	}

	/**
	 * Sets the lot service.
	 * 
	 * @param lotService the lotService to set
	 */
	public void setLotService(LotService lotService) {
		this.lotService = lotService;
	}

	/**
	 * Sets the dao.
	 * 
	 * @param dao the dao to set
	 */
	public void setDao(DataAccessService dao) {
		this.dao = dao;
	}

	/**
	 * Sets the item service.
	 * 
	 * @param itemService the itemService to set
	 */
	public void setItemService(ItemService itemService) {
		this.itemService = itemService;
	}

	/**
	 * Gets the upload.
	 * 
	 * @return the upload
	 */
	public List<File> getUpload() {
		return this.uploads;
	}

	/**
	 * Sets the upload.
	 * 
	 * @param uploads the new upload
	 */
	public void setUpload(List<File> uploads) {
		this.uploads = uploads;
	}

	/**
	 * Gets the upload file name.
	 * 
	 * @return the upload file name
	 */
	public List<String> getUploadFileName() {
		return this.uploadFileNames;
	}

	/**
	 * Sets the upload file name.
	 * 
	 * @param uploadFileNames the new upload file name
	 */
	public void setUploadFileName(List<String> uploadFileNames) {
		this.uploadFileNames = uploadFileNames;
	}

	/**
	 * Gets the upload content type.
	 * 
	 * @return the upload content type
	 */
	public List<String> getUploadContentType() {
		return this.uploadContentTypes;
	}

	/**
	 * Sets the upload content type.
	 * 
	 * @param contentTypes the new upload content type
	 */
	public void setUploadContentType(List<String> contentTypes) {
		this.uploadContentTypes = contentTypes;
	}

	/**
	 * Gets the imported lots.
	 * 
	 * @return the importedLots
	 */
	public List<? extends Lot> getImportedLots() {
		return this.importedLots;
	}

	/**
	 * Test.
	 * 
	 * @return the string
	 */
	public String test() {
		if (this.uploads.size() > 0) {
			File f = this.uploads.get(0);
			Importer importerObject;
			try {
				importerObject = (Importer) Class.forName(this.importer).newInstance();
				importerObject.setItemService(itemService);
				importerObject.setDao(dao);
				importerObject.setFile(f, Charset.forName(charset));

				try {
					this.importedLots = importerObject.getLots();
				} catch (ImportException e) {
					LOG.info(e, e);
					this.addActionError("<b>Too many errors. Fix data first.</b><br /><br />" + e.getMessage().replaceAll("\n", "<br />\n")
							+ (e.getCause() == null ? "" : " (" + e.getCause().getMessage() + ")"));
					return Action.ERROR;
				} catch (Exception e) {
					LOG.info(e, e);
					this.addActionError(e.getMessage() + (e.getCause() == null ? "" : " (" + e.getCause().getMessage() + ")") + "<br />"
							+ formatStackTrace(e.getStackTrace(), 5));
					return Action.ERROR;
				}

				if (importerObject.getErrors() != null && importerObject.getErrors().length() > 0)
					this.addActionMessage("<b>Warnings encountered while parsing.</b><br /><br />" + importerObject.getErrors().replaceAll("\n", "<br />\n"));

			} catch (Exception e) {
				LOG.info(e, e);
				this.addActionError("<b>Cannot import data</b><br /><br />" + e.getMessage().replaceAll("\n", "<br />\n")
						+ (e.getCause() == null ? "" : " (" + e.getCause().getMessage() + ")"));
				return Action.ERROR;
			}
		}
		
		addActionMessage("Lot information parsed successfully");
		return Action.SUCCESS;
	}

	/**
	 * Format stack trace.
	 * 
	 * @param stackTrace the stack trace
	 * @param depth the depth
	 * 
	 * @return the string
	 */
	private String formatStackTrace(StackTraceElement[] stackTrace, int depth) {
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i <= depth && i < stackTrace.length; i++) {
			sb.append(stackTrace[i].getClassName() + " " + stackTrace[i].getMethodName() + ":" + stackTrace[i].getLineNumber() + "<br />");
		}
		return sb.toString();
	}

	/* (non-Javadoc)
	 * @see com.opensymphony.xwork2.ActionSupport#execute()
	 */
	public String execute() {
		if (this.uploads.size() > 0) {
			File f = this.uploads.get(0);
			Importer importerObject;
			try {
				importerObject = (Importer) Class.forName(this.importer).newInstance();
				importerObject.setItemService(itemService);
				importerObject.setDao(dao);
				importerObject.setFile(f);

				try {
					this.importedLots = importerObject.getLots();
				} catch (ImportException e) {
					LOG.info(e, e);
					this.addActionError("<b>Too many errors. Fix data first.</b><br /><br />" + e.getMessage().replaceAll("\n", "<br />\n")
							+ (e.getCause() == null ? "" : " (" + e.getCause().getMessage() + ")"));
					return Action.ERROR;
				} catch (Exception e) {
					LOG.info(e, e);
					this.addActionError(e.getMessage() + (e.getCause() == null ? "" : " (" + e.getCause().getMessage() + ")"));
					return Action.ERROR;
				}

				// persist lots
				this.lotService.importLots(this.importedLots);

			} catch (Exception e) {
				LOG.info(e, e);
				this.addActionError("<b>Cannot import data</b><br /><br />" + e.getMessage().replaceAll("\n", "<br />\n")
						+ (e.getCause() == null ? "" : "<br />" + e.getCause().getMessage() + (e.getCause().getCause() == null ? "" : "<br />" + e.getCause().getCause().getMessage())));
				return Action.ERROR;
			}
		}
		
		addActionMessage("Lot data imported successfully.");
		return Action.SUCCESS;
	}
}
