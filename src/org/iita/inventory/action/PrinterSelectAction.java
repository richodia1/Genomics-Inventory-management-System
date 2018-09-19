/**
 * inventory.Struts Nov 3, 2009
 */
package org.iita.inventory.action;

import java.util.List;

import org.apache.struts2.ServletActionContext;
import org.iita.inventory.label.PrinterException;
import org.iita.inventory.printing.PrinterInfo;
import org.iita.inventory.service.PrinterService;

import com.opensymphony.xwork2.Action;

/**
 * @author mobreza
 * 
 */
@SuppressWarnings("serial")
public class PrinterSelectAction extends BaseAction {
	private PrinterService printerService;
	private List<PrinterInfo> printers;
	private Integer myPrinter;

	/**
	 * @param printerService 
	 * 
	 */
	public PrinterSelectAction(PrinterService printerService) {
		this.printerService=printerService;
	}
	
	/**
	 * @return the printers
	 */
	public List<PrinterInfo> getPrinters() {
		return this.printers;
	}
	
	/**
	 * @param myPrinter the myPrinter to set
	 */
	public void setMyPrinter(Integer myPrinter) {
		this.myPrinter = myPrinter;
	}
	
	/**
	 * @see org.iita.inventory.action.BaseAction#prepare()
	 */
	@Override
	public void prepare() {
		this.printers = this.printerService.getPrinters();
	}
	
	/**
	 * Default action renders list of available printers
	 * 
	 * @see org.iita.struts.BaseAction#execute()
	 */
	@Override
	public String execute() {
		return Action.INPUT;
	}
	
	public String select() {
		LOG.info("Trying to select printer " + myPrinter + " for this session.");
		for (PrinterInfo printer : this.printers) {
			LOG.debug("Checking printer " + printer.getName() + " id=" + printer.getId());
			if (printer.getId().equals(this.myPrinter))
			{
				LOG.info("Found selected printer " + printer.getName() + ". Selecting now.");
				try {
					this.printerService.selectPrinter(ServletActionContext.getRequest().getSession().getId(), printer);
					return Action.SUCCESS;
				} catch (PrinterException e) {
					LOG.error(e);
					addActionError(e.getMessage());
					return Action.INPUT;
				}
			}
		}
		addActionError("Could not locate selected printer");
		return Action.ERROR;
	}
}
