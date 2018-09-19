/**
 * 
 */
package org.iita.inventory.service.impl;

import java.io.StringWriter;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts2.ServletActionContext;
import org.iita.inventory.barcode.BarcodingException;
import org.iita.inventory.label.NoPrinterException;
import org.iita.inventory.label.PrinterException;
import org.iita.inventory.model.InVitroLot;
import org.iita.inventory.model.Lot;
import org.iita.inventory.printing.LabelDesigner;
import org.iita.inventory.printing.LabelInfo;
import org.iita.inventory.printing.PrinterInfo;
import org.iita.inventory.service.BarcodingService;
import org.iita.inventory.service.LabelService;
import org.iita.inventory.service.PrinterService;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author mobreza
 */
public class LabelServiceImpl2 implements LabelService {
	private static final Log LOG = LogFactory.getLog(LabelServiceImpl2.class);
	private EntityManager em;
	private LabelDesigner labelDesigner;
	private PrinterService printerService;
	private BarcodingService barcodingService;
	private int maxAllowedPrint = 500;

	/**
	 * @return the em
	 */
	public EntityManager getEntityManager() {
		return em;
	}

	/**
	 * @param em the em to set
	 */
	@PersistenceContext
	public void setEntityManager(EntityManager em) {
		this.em = em;
	}

	/**
	 * @return the labelDesigner
	 */
	public LabelDesigner getLabelDesigner() {
		return this.labelDesigner;
	}

	/**
	 * @param barcodingService the barcodingService to set
	 */
	public void setBarcodingService(BarcodingService barcodingService) {
		this.barcodingService = barcodingService;
	}

	/**
	 * @param labelDesigner the labelDesigner to set
	 */
	public void setLabelDesigner(LabelDesigner labelDesigner) {
		this.labelDesigner = labelDesigner;
	}

	/**
	 * @param printerService the printerService to set
	 */
	public void setPrinterService(PrinterService printerService) {
		this.printerService = printerService;
	}

	/**
	 * @return the maxAllowedPrint
	 */
	public int getMaxAllowedPrint() {
		return this.maxAllowedPrint;
	}

	/**
	 * @param maxAllowedPrint the maxAllowedPrint to set
	 */
	public void setMaxAllowedPrint(int maxAllowedPrint) {
		this.maxAllowedPrint = maxAllowedPrint;
	}

	/*
	 * (non-Javadoc)
	 * @see org.iita.inventory.service.LabelService#print(java.util.List)
	 */
	@Override
	public void print(List<? extends Lot> labelsToPrint) throws PrinterException, NoPrinterException {
		if (labelsToPrint == null)
			return;

		Class<? extends Lot> type = null;
		for (Lot l : labelsToPrint) {
			if (type == null)
				type = l.getClass();
			else if (!(type.isAssignableFrom(l.getClass())))
				throw new PrinterException("Cannot print different Lot labels at the same time. Do not mix lots for printing.");
		}

		Collection<PrinterInfo> printerInfo = null;

		try {
			printerInfo = this.printerService.getPrintersForIP(java.net.InetAddress.getByName(ServletActionContext.getRequest().getRemoteAddr()),
					ServletActionContext.getRequest().getSession().getId());
		} catch (UnknownHostException e) {
			LOG.warn("Unknown host exception in getPrintersForIP: " + e.getMessage());
		}

		if (printerInfo == null || printerInfo.size() == 0)
			throw new NoPrinterException("Your computer is not allowed to print labels.");
		if (printerInfo.size() > 1)
			throw new PrinterException("More than one printer available for your computer. Available printers: " + printerInfo.size());

		PrinterInfo selectedPrinter = printerInfo.iterator().next();

		labelsToPrint = makeQuantitiesAndStuff(labelsToPrint);

		int count = labelsToPrint.size();

		if (count == 0)
			return;

		if (count > this.maxAllowedPrint)
			throw new PrinterException("Configuration allows " + this.maxAllowedPrint + " labels to be printed at once. You requested " + count + ".");

		LOG.warn("Printing " + count + " labels.");
		int columns = selectedPrinter.getLabelInfo().getColumns();
		LOG.warn("Printing in " + columns + " columns.");

		// ensure they are barcoded!
		try {
			this.barcodingService.ensureBarCode(labelsToPrint);
		} catch (BarcodingException e) {
			throw new PrinterException("Error ensuring barcodes: " + e.getMessage(), e);
		}

		// buffer
		StringWriter sw = new StringWriter(1024);

		for (int i = 0; i < count; i++) {
			LOG.warn("FOOO " + i + " " + (i % columns));

			Lot lot = labelsToPrint.get(i);
			if (lot != null)
				LOG.warn("\t" + lot.getItem().getName());

			if (i % columns == 0) {
				// first close previous
				if (i > 0) {
					sw.write("^PQ1,0,1,Y^XZ");
					sw.write(0x0D); // CR
					sw.write(0x0D); // CR
				}

				sw.write("^XA");
				sw.write(0x0D); // CR
			}

			// move!
			sw.write(String.format("^LH%1d,%2d", selectedPrinter.getLabelInfo().getMarginHorizontal() + (i % columns)
					* (selectedPrinter.getLabelInfo().getLabelWidth() + selectedPrinter.getLabelInfo().getMarginHorizontal()), selectedPrinter.getLabelInfo()
					.getMarginVertical()));

			sw.write(labelDesigner.printLabel(selectedPrinter.getLabelInfo(), lot));
		}
		sw.write("^PQ1,0,1,Y^XZ");
		sw.write(0x0D); // CR
		sw.write(0x0D); // CR

		org.iita.inventory.printing.PrintFactory.print(selectedPrinter, sw.toString());
	}

	/**
	 * Utility methot to generate number of labels to print depending on Lot type. For InVitro lots we print one barcode for each tube (quantity is number of
	 * labels to print)
	 * 
	 * @param labelsToPrint
	 * @return
	 */
	private List<? extends Lot> makeQuantitiesAndStuff(List<? extends Lot> labelsToPrint) {
		List<Lot> fixed = new ArrayList<Lot>();
		for (Lot lot : labelsToPrint) {
			if (lot instanceof InVitroLot) {
				if (lot.getQuantity() > 0.0d)
					for (int i = 0; i < lot.getQuantity(); i++)
						fixed.add(lot);
			} else
				fixed.add(lot);
		}
		return fixed;
	}

	@Override
	@Transactional(readOnly = true)
	public LabelInfo find(int id) {
		return em.find(LabelInfo.class, id);
	}

	@SuppressWarnings("unchecked")
	@Override
	@Transactional(readOnly = true)
	public List<LabelInfo> findAll() {
		Query query = getEntityManager().createQuery("from LabelInfo l");
		return query.getResultList();
	}

}
