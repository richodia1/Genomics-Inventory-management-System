/**
 * inventory.Struts Nov 3, 2009
 */
package org.iita.inventory.action.order;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.StringReader;
import java.util.Calendar;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.sax.SAXResult;
import javax.xml.transform.stream.StreamSource;

import org.apache.fop.apps.Fop;
import org.apache.fop.apps.FopFactory;
import org.apache.fop.apps.MimeConstants;
import org.apache.struts2.ServletActionContext;
import org.iita.crm.model.Tag;
import org.iita.crm.service.TagService;
import org.iita.inventory.action.BaseAction;
import org.iita.inventory.model.GenebankOrder;
import org.iita.inventory.model.Lot;
import org.iita.inventory.model.LotSelection;
import org.iita.inventory.model.Order;
import org.iita.inventory.model.OrderTag;
import org.iita.inventory.model.QuantityUpdateBulk;
import org.iita.inventory.service.LotService;
import org.iita.inventory.service.OrderException;
import org.iita.inventory.service.OrderService;
import org.iita.inventory.service.SelectionService;
import org.iita.service.TemplatingService;
import org.iita.struts.AllowedParameters;
import org.iita.struts.DownloadableStream;
import org.iita.struts.webfile.ServerFile;
import org.iita.util.DeleteFileAfterCloseInputStream;

import com.opensymphony.xwork2.Action;
import com.opensymphony.xwork2.validator.annotations.RequiredStringValidator;
import com.opensymphony.xwork2.validator.annotations.Validations;

/**
 * Action to manage (edit, create, ...) orders
 * 
 * @author mobreza
 */
@SuppressWarnings("serial")
public class OrderAction extends BaseAction implements DownloadableStream {
	private OrderService orderService;
	private TemplatingService templatingService;
	private TagService tagService;
	private GenebankOrder order = new GenebankOrder();
	private Long id;
	private String accessionNames;
	private Long bulk;
	private DeleteFileAfterCloseInputStream inputStream;
	
	private LotSelection list = new LotSelection();
	private SelectionService selectionService = null;
	private LotService lotService = null;
	private List<Lot> results;

	/**
	 * @param orderService
	 * @param templatingService
	 * @param tagService
	 */
	public OrderAction(OrderService orderService, TemplatingService templatingService, TagService tagService, LotService lotService, SelectionService selectionService) {
		this.orderService = orderService;
		this.templatingService = templatingService;
		this.tagService = tagService;
		this.lotService = lotService;
		this.selectionService = selectionService;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(Long id) {
		this.id = id;
	}

	/**
	 * @param bulk the bulk to set
	 */
	public void setBulk(Long bulk) {
		this.bulk = bulk;
	}

	/**
	 * @param accessionNames the accessionNames to set
	 */
	public void setAccessionNames(String accessionNames) {
		this.accessionNames = accessionNames;
	}

	/**
	 * @return the order
	 */
	public Order getOrder() {
		return this.order;
	}

	public List<ServerFile> getFiles() {
		try {
			return this.orderService.getOrderFiles(this.order, null);
		} catch (IOException e) {
			addActionError(e.getMessage());
		}
		return null;
	}

	/**
	 * Find existing tag
	 * 
	 * @param tag
	 * @return
	 */
	public OrderTag findTag(String tag) {
		LOG.debug("Looking up existing tag: " + tag);
		for (OrderTag entityTag : this.order.getTags()) {
			if (entityTag.getTag().equals(tag))
				return entityTag;
		}
		return null;
	}

	public Set<String> getAllTagsForCategory(List<String> defaultTags) {
		LinkedHashSet<String> properSet = new LinkedHashSet<String>(defaultTags);
		String category = null;
		if (properSet.size() > 0) {
			category = Tag.getTagCategory(properSet.iterator().next());
			if (category != null)
				properSet.addAll(this.tagService.getTagsForCategory(OrderTag.class, category));
		}
		return properSet;
	}

	/**
	 * @return the list
	 */
	public LotSelection getList() {
		return this.list;
	}
	
	/**
	 * @return the selectionService
	 */
	public SelectionService getSelectionService() {
		return this.selectionService;
	}
	
	/**
	 * Get selected Lots
	 * 
	 * @return
	 */
	public Collection<Lot> getResults() {
		return this.results;
	}
	
	/**
	 * @see org.iita.inventory.action.BaseAction#prepare()
	 */
	@Override
	public void prepare() {
		if (this.id != null)
			this.order = this.orderService.getOrder(this.id);
		else
			this.order = this.orderService.createOrder(getUser());
		
		this.list = this.selectionService.getSelectedList();
		
		if(this.list!=null){
			this.results = this.lotService.getLots(this.list.getSelectedLots());
			//clear selection
			this.list.clearSelection();
		}
	}

	/**
	 * Default action method will inspect order and return either "input" or "readonly" action result
	 * 
	 * @see org.iita.struts.BaseAction#execute()
	 */
	@Override
	public String execute() {
		if (this.order == null) {
			return "input";
		}
		addSelectedLots();
		return Action.SUCCESS;
	}

	public String create() {
		this.id = null;
		this.order = this.orderService.createOrder(getUser());
		return "input";
	}

	/**
	 * Update order information
	 * 
	 * @return
	 */
	@Validations(requiredStrings = { @RequiredStringValidator(fieldName = "order.purpose", message = "Purpose is required"),
			@RequiredStringValidator(fieldName = "order.lastName", message = "Last name of Requester is required"),
			@RequiredStringValidator(fieldName = "order.organization", message = "Organization of Requester is required") })
	public String update() {
		try {
			this.order.setRequester(getUser());
			this.orderService.updateOrder(this.order);
			return "refresh";
		} catch (Exception e) {
			LOG.error(e);
			addActionError(e.getMessage());
			return Action.ERROR;
		}
	}

	@AllowedParameters({ "id", "accessionNames" })
	public String pasteNames() {
		if (this.order == null) {
			addActionError("No order information provided!");
			return Action.ERROR;
		}
		try {
			this.orderService.addItemsByName(this.order, accessionNames);
		} catch (OrderException e) {
			addActionError(e.getMessage());
			LOG.error(e);
			return Action.ERROR;
		}
		return "refresh";
	}
	
	private void addSelectedLots(){
		StringBuilder accNames = new StringBuilder("");
		
		if(this.results!=null){
			for(Lot l : this.results){
				if(accNames.length()==0)
					accNames.append(l.getItem().getName());
				else
					accNames.append(", ").append(l.getItem().getName());
			}
		}
		if(accNames.length()>0){
			try {
				this.orderService.addItemsByName(this.order, accNames.toString());
			} catch (OrderException e) {
				addActionError(e.getMessage());
				LOG.error(e);
			}
		}
	}
	
	private String getSelectedLots(){
		StringBuilder accNames = new StringBuilder("");
		
		if(this.results!=null){
			for(Lot l : this.results){
				if(accNames.length()==0)
					accNames.append(l.getItem().getName());
				else
					accNames.append(", ").append(l.getItem().getName());
			}
		}
		return accNames.toString();
	}
	
	@AllowedParameters({ "id" })
	public String selectedNames() {
		if (this.order == null) {
			addActionError("No order information provided!");
			return Action.ERROR;
		}
		
		String accessionNames = getSelectedLots();
		
		if(accessionNames.length()>0){
			try {
				this.orderService.addItemsByName(this.order, accessionNames);
			} catch (OrderException e) {
				addActionError(e.getMessage());
				LOG.error(e);
				return Action.ERROR;
			}
		}
		return "refresh";
	}

	/**
	 * Send order for processing: i.e. create a prepared bulk update
	 * 
	 * @return
	 */
	public String process() {
		try {
			this.orderService.startProcessing(this.order);
		} catch (Exception e) {
			addActionError("Could not create distribution: " + e.getMessage());
		}
		return "refresh";
	}

	public String reject() {
		try {
			this.orderService.rejectOrder(this.order);
		} catch (Exception e) {
			addActionError("Could not reject order: " + e.getMessage());
		}
		return "refresh";
	}

	/**
	 * Action to split an order into two: keeping DISPATCHED items in one order, putting others in a new order
	 * 
	 * @return
	 */
	public String split() {
		GenebankOrder splitOrder;
		try {
			splitOrder = this.orderService.split(this.order);
			this.order = splitOrder;
			return "refresh";
		} catch (OrderException e) {
			addActionError(e.getMessage());
			return "refresh";
		}
	}

	public String smta() {
		FopFactory fopFactory = FopFactory.newInstance();

		OutputStream out = null;
		try {
			File smtaFile = File.createTempFile("smta", ".pdf");
			LOG.info("Generating to file: " + smtaFile.getAbsolutePath());

			// Setup output stream.  Note: Using BufferedOutputStream
			// for performance reasons (helpful with FileOutputStreams).
			out = new FileOutputStream(smtaFile);
			out = new BufferedOutputStream(out);

			// Construct fop with desired output format
			Fop fop = fopFactory.newFop(MimeConstants.MIME_PDF, out);

			// Setup JAXP using identity transformer
			TransformerFactory factory = TransformerFactory.newInstance();
			Transformer transformer = factory.newTransformer(); // identity transformer

			Map<String, Object> data = new HashMap<String, Object>();
			data.put("request", this.order);
			data.put("date", Calendar.getInstance());
			for (QuantityUpdateBulk bulk : this.order.getUpdates()) {
				if (bulk.getId().equals(this.bulk))
					data.put("bulk", bulk);
			}
			// Setup input stream
			Source src = new StreamSource(new StringReader(this.templatingService.fillTemplate("smta", data)));

			// Resulting SAX events (the generated FO) must be piped through to FOP
			Result res = new SAXResult(fop.getDefaultHandler());

			// Step 6: Start XSLT transformation and FOP processing
			transformer.transform(src, res);
			LOG.info("Done generating! " + smtaFile.length());
			this.inputStream = new DeleteFileAfterCloseInputStream(smtaFile);
		} catch (Exception e) {
			addActionError("Could not generate SMTA: " + e.getMessage());
			e.printStackTrace(System.err);
			return Action.ERROR;
		} finally {
			try {
				out.flush();
				out.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		ServletActionContext.getResponse().addHeader("Cache", "no-cache, private");
		return "download";
	}

	/**
	 * @see org.iita.struts.DownloadableStream#getDownloadFileName()
	 */
	@Override
	public String getDownloadFileName() {
		return String.format("SMTA-%1$d-%2$d-%3$s-%4$s.pdf", this.id, this.bulk, this.order.getOrganization(), this.order.getLastName());
	}

	/**
	 * @see org.iita.struts.DownloadableStream#getDownloadStream()
	 */
	@Override
	public InputStream getDownloadStream() {
		return this.inputStream;
	}
}
