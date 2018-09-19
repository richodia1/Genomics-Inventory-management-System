/**
 * inventory.Struts Dec 15, 2009
 */
package org.iita.inventory.action.admin;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Hashtable;
import java.util.List;
import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.Transient;

import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.iita.inventory.action.BaseAction;
import org.iita.inventory.model.InVitroLot;
import org.iita.inventory.model.SeedLot;
import org.iita.service.XLSDataImportService;
import org.iita.service.impl.XLSImportException;
import org.iita.struts.FileUploadAction;

import com.opensymphony.xwork2.Action;
import com.opensymphony.xwork2.ActionContext;

/**
 * The final lot import/update tool. Allows user to upload XLS file with lot information and then map columns from XLS file to lot properties. Appropriately
 * handles Seed and InVitro lots by allowing user to select the type of lot information they are uploading in the first step.
 * 
 * Process outline:
 * <ul>
 * <li>Select lot type (Seed/InVitro), import lot <b>ROOT</b> location (optional) and upload XLS file</li>
 * <li>Uploaded file is stored in Session to reduce the number of times file is uploaded and parsed</li>
 * <li>Based on lot type, the available properties (setters only) are mapped by user to columns in XLS file</li>
 * <li>Lot information is read from XLS file, if lot ID is provided, lot data will only be updated, otherwise new lot records are created.</li>
 * <li>Only the mapped values are modified, others may be left in uninitialized state</li>
 * <li>Inventory items are generated if so required</li>
 * <li>Resulting list is stored in user session for review and browsing</li>
 * <li>After user "commits" changes to inventory, data is persisted and lot information put to selection list</li>
 * </ul>
 * 
 * <p>
 * Major issue to resolve is the location of lots: a <b>ROOT</b> location for all lots needs to be selected. If sublocation column exists in XLS file,
 * sublocations are fetched/created under the ROOT location of import.
 * </p>
 * 
 * <p>
 * The importer needs to be able to resolve related entities: For example <code>lot.location.name</code> must be managed so that the <code>lot.location</code>
 * is fetched from storage based on <code>location.name</code> (and possibly other parameters -- in case of location, the parent location is needed). Another
 * example is <code>lot.item.name</code> which needs to do similar thing; resolve item entity. Again, this is only required when new lot information is imported
 * into the system.
 * </p>
 * 
 * @author mobreza
 * 
 */
@SuppressWarnings("serial")
public class ImportLotAction extends BaseAction implements FileUploadAction {
	/**
	 * 
	 */
	private static final String XLSIMPORTDATA__1 = "XLSIMPORTDATA__1";
	private static final Class<?>[] entities = new Class<?>[] { SeedLot.class, InVitroLot.class };
	private ImportData importData = null;
	private List<File> uploads;
	private XLSDataImportService importService;
	
	/**
	 * @param importService the importService to set
	 */
	public void setImportService(XLSDataImportService importService) {
		this.importService = importService;
	}
	
	/**
	 * Get available target entities.
	 * 
	 * @return the entities
	 */
	public static Class<?>[] getEntities() {
		return entities;
	}

	/**
	 * @param target the target to set
	 */
	public void setTarget(String target) {
		LOG.debug("Setting target : " + target + " and checking " + ImportLotAction.entities.length + " available entitites");
		if (this.importData == null)
			return;
		for (Class<?> entity : entities) {
			if (entity.getName().equalsIgnoreCase(target)) {
				this.importData.targetEntity = entity;
				LOG.info("Target entity selected: " + this.importData.targetEntity);
				break;
			} else
				LOG.trace(entity.getName() + " not matching " + target);
		}
	}

	public String getTarget() {
		return this.importData.targetEntity == null ? null : this.importData.targetEntity.getName();
	}

	/**
	 * Return list of available properties
	 * 
	 * @return
	 */
	public ArrayList<Property> getAvailableProperties() {
		return this.importData.availableMappings;
	}

	public ArrayList<String> getXlsColumns() {
		return this.importData.xlsColumns;
	}

	public Hashtable<String, String> getMapping() {
		return this.importData.mappings;
	}

	public void setMapping(Hashtable<String, String> mapping) {
		if (this.importData != null)
			this.importData.mappings = mapping;
	}
	
	public List<?> getResults() {
		return this.importData.data;
	}

	/**
	 * @see org.iita.inventory.action.BaseAction#prepare()
	 */
	@SuppressWarnings("unchecked")
	@Override
	public void prepare() {
		// get data from Session
		this.importData = (ImportData) ActionContext.getContext().getSession().get(XLSIMPORTDATA__1);
		if (this.importData == null) {
			this.importData = new ImportData();
			ActionContext.getContext().getSession().put(XLSIMPORTDATA__1, this.importData);
			LOG.debug("Putting import data object to Session");
		} else {
			LOG.debug("Got import data object from Session");
		}
	}

	/**
	 * This is the initial action method that shows the import form and allows user to upload XLS data.
	 * 
	 * @see org.iita.struts.BaseAction#execute()
	 */
	@Override
	public String execute() {
		return Action.SUCCESS;
	}

	/**
	 * Action method for selection of target entity and XLS file upload. Uploaded file is stored in Session
	 * 
	 * @return
	 */
	// @Validations(requiredFields = { @RequiredFieldValidator(fieldName = "targetEntity", message = "Target entity is required."),
	// @RequiredFieldValidator(fieldName = "uploads", message = "File needs to be uploaded.") })
	public String upload() {
		if (this.uploads == null || this.uploads.size() == 0) {
			addActionError("Upload XLS file with entity information");
			return Action.ERROR;
		}

		// parse XLS file and store to session
		HSSFWorkbook workbook = null;
		File xlsFile = this.uploads.get(0);
		FileInputStream fis;
		try {
			fis = new FileInputStream(xlsFile);
			workbook = new HSSFWorkbook(fis);
			fis.close();
		} catch (FileNotFoundException e) {
			LOG.error(e);
			addActionError(e.getMessage());
			return Action.ERROR;
		} catch (IOException e) {
			LOG.error(e);
			addActionError(e.getMessage());
			return Action.ERROR;
		}

		// store to session object
		this.importData.xls = workbook;
		HSSFSheet sheet = workbook.getSheetAt(0);
		HSSFRow toprow = sheet.getRow(0);
		this.importData.xlsColumns.clear();
		for (int i = 0; i < toprow.getLastCellNum(); i++) {
			this.importData.xlsColumns.add(toprow.getCell(i).getRichStringCellValue().getString());
		}

		// get available mappings from target entity
		this.importData.availableMappings = getMappings(this.importData.targetEntity, "", 3);
		Collections.sort(this.importData.availableMappings, new Comparator<Property>() {
			@Override
			public int compare(Property arg0, Property arg1) {
				return arg0.ognl.compareTo(arg1.ognl);
			}
		});

		return "mapping";
	}

	/**
	 * @param targetEntity
	 * @param depth
	 * @return
	 */
	private ArrayList<Property> getMappings(Class<?> targetEntity, String prefix, int depth) {
		LOG.info("Mapping " + targetEntity);
		if (depth == 0)
			// stop mappings
			return null;

		Hashtable<String, Property> mappings = new Hashtable<String, Property>();
		Method[] methods = targetEntity.getMethods();
		for (Method m : methods) {
			if (m.getName().startsWith("set") && m.getReturnType() == void.class && m.getParameterTypes().length == 1) {
				Class<?> type = m.getParameterTypes()[0];
				LOG.debug("Got setter: " + m.getName() + " with void and 1 argument of type " + type);

				if (type == List.class || type == Set.class) {
					LOG.info("Ignoring setter of type " + type + " for " + m.getName());
					continue;
				}

				String ognlName = null;
				if (m.getName().length() > 4)
					ognlName = prefix + m.getName().substring(3, 4).toLowerCase() + m.getName().substring(4);
				else
					ognlName = prefix + m.getName().substring(3, 4).toLowerCase();
				LOG.debug("Adding OGNL name for " + m.getName() + " is " + ognlName);
				mappings.put(ognlName, new Property(ognlName, m.getParameterTypes()[0]));
			}
		}

		// find getters that return @Entity objects and remove from OGNL list
		for (Method m : methods) {
			if (m.getName().startsWith("get") && m.getParameterTypes().length == 0 && m.getReturnType().getAnnotation(Entity.class) != null) {
				LOG.debug("Got getter: " + m.getName() + " returning @Entity object");
				String ognlName = null;
				if (m.getName().length() > 4)
					ognlName = prefix + m.getName().substring(3, 4).toLowerCase() + m.getName().substring(4);
				else
					ognlName = prefix + m.getName().substring(3, 4).toLowerCase();
				LOG.debug("Removing direct setter " + m.getName() + " as " + ognlName);
				mappings.remove(ognlName);
				// now go manage this object
				ArrayList<Property> childMappings = getMappings(m.getReturnType(), ognlName + ".", depth - 1);
				if (childMappings != null) {
					for (Property p : childMappings)
						mappings.put(p.ognl, p);
				}
			}
		}

		// remove transient
		for (Method m : methods) {
			if (m.getName().startsWith("get") && m.getParameterTypes().length == 0 && m.getAnnotation(Transient.class) != null) {
				LOG.debug("Got getter: " + m.getName() + " but is @Transient");
				String ognlName = null;
				if (m.getName().length() > 4)
					ognlName = prefix + m.getName().substring(3, 4).toLowerCase() + m.getName().substring(4);
				else
					ognlName = prefix + m.getName().substring(3, 4).toLowerCase();
				LOG.debug("Removing transient " + m.getName() + " as " + ognlName);
				mappings.remove(ognlName);
			}
		}
		return new ArrayList<Property>(mappings.values());
	}

	/**
	 * Mapping action
	 */
	public String mapping() {
		// if mapping exists, allow user to modify existing mapping rules

		return "mapping";
	}

	/**
	 * Action to store mapping selection and move on to Preview data
	 * 
	 * @return
	 */
	public String map() {
		// import data before returning "preview" and store to Session
		try {
			this.importData.data=this.importService.getObjectsFromXLS(this.importData.targetEntity, this.importData.mappings, this.importData.xls.getSheetAt(0));
		} catch (XLSImportException e) {
			LOG.error(e);
			addActionError(e.getMessage());
			return Action.ERROR;
		}

		return "preview";
	}

	/**
	 * Action to preview
	 */
	public String preview() {
		// check if data is stored in Session and then preview
		return "preview";
	}

	/**
	 * Persist lot information
	 * 
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public String store() {
		// persist
		LOG.info("Importing " + this.importData.data.size() + " objects");
		try {
			this.importData.data=this.importService.persistObjectsFromXLS(this.importData.targetEntity, this.importData.mappings, this.importData.xls.getSheetAt(0));
			addActionMessage("Imported " + this.importData.data.size() + " objects");
			ImportData newData = new ImportData();
			newData.data=this.importData.data;
			ActionContext.getContext().getSession().put(XLSIMPORTDATA__1, newData);
			this.importData=newData;
		} catch (XLSImportException e) {
			addActionError(e.getMessage());
			return Action.ERROR;
		}
		return "preview";
	}

	/**
	 * Bulk of data stored in Session
	 * 
	 * @author mobreza
	 */
	private class ImportData {
		public List<?> data;
		public ArrayList<String> xlsColumns = new ArrayList<String>();
		public ArrayList<Property> availableMappings = null;
		public HSSFWorkbook xls = null;
		public Class<?> targetEntity = null;
		private Hashtable<String, String> mappings = new Hashtable<String, String>();
	}

	private class Property {
		public Property(String ognlName, Class<?> class1) {
			this.type = class1;
			this.ognl = ognlName;
		}

		public Class<?> type;
		public String ognl;
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
}
