/**
 * 
 */
package org.iita.inventory.service.impl;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Dictionary;
import java.util.Hashtable;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.print.PrintService;
import javax.print.PrintServiceLookup;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.iita.inventory.label.PrinterException;
import org.iita.inventory.printing.PrinterInfo;
import org.iita.inventory.service.PrinterService;
import org.springframework.transaction.annotation.Transactional;

/**
 * Printer service handles printer configuration related data. Only one instance of this class should exist in the application.
 * 
 * @author mobreza
 */
public class PrinterServiceImpl implements PrinterService {
	private static final Log LOG = LogFactory.getLog(PrinterService.class);
	private EntityManager entityManager;
	private Dictionary<String, Integer> selectedPrinters = new Hashtable<String, Integer>();

	/**
	 * @return the entityManager
	 */
	public EntityManager getEntityManager() {
		return this.entityManager;
	}

	/**
	 * @param entityManager the entityManager to set
	 */
	@PersistenceContext
	public void setEntityManager(EntityManager entityManager) {
		this.entityManager = entityManager;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.iita.inventory.service.PrinterService#delete(org.iita.inventory.printing .PrinterInfo)
	 */
	@Override
	@Transactional
	public void delete(PrinterInfo printerInfo) {
		LOG.warn("Deleting printer: " + printerInfo);
		this.entityManager.remove(printerInfo);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.iita.inventory.service.PrinterService#find(int)
	 */
	@Override
	@Transactional(readOnly=true)
	public PrinterInfo find(int id) {
		return this.entityManager.find(PrinterInfo.class, id);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.iita.inventory.service.PrinterService#list()
	 */
	@SuppressWarnings("unchecked")
	@Override
	@Transactional(readOnly=true)
	public List<PrinterInfo> list() {
		return this.entityManager.createQuery("from PrinterInfo pi order by pi.name").getResultList();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.iita.inventory.service.PrinterService#store(org.iita.inventory.printing .PrinterInfo)
	 */
	@Override
	@Transactional
	public void store(PrinterInfo printerInfo) {
		LOG.warn("Storing printer: " + printerInfo);
		if (printerInfo.getId() == null)
			this.entityManager.persist(printerInfo);
		else
			this.entityManager.merge(printerInfo);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seeorg.iita.inventory.service.PrinterService#getPrintersForIP(java.net. InetAddress)
	 */
	@Override
	@Transactional(readOnly=true)
	public Collection<PrinterInfo> getPrintersForIP(InetAddress remoteAddress, String sessionId) {
		if (sessionId != null) {
			LOG.debug("Checking if we have selected printer for session " + sessionId);
			// check if printer was selected
			Integer printerInfoId = this.selectedPrinters.get(sessionId);
			if (printerInfoId != null) {
				PrinterInfo selectedPrinter = this.find(printerInfoId);
				if (selectedPrinter != null) {
					LOG.debug("Found selected printer in session data");
					ArrayList<PrinterInfo> printers = new ArrayList<PrinterInfo>();
					printers.add(selectedPrinter);
					LOG.info("Printer selected in session.");
					return printers;
				}
			}
			LOG.info("No printer for session " + sessionId);
		}

		System.err.println("Getting printers for: " + remoteAddress.toString());
		List<PrinterInfo> printers = this.list(), allowedPrinters = new ArrayList<PrinterInfo>();
		for (PrinterInfo pi : printers) {
			if (accessAllowed(pi.getAllowedIPaddresses(), remoteAddress))
				allowedPrinters.add(pi);
		}
		return allowedPrinters;
	}

	/**
	 * @param allowedIPaddresses
	 * @param remoteAddress
	 * @return
	 */
	private boolean accessAllowed(String allowedIPaddresses, InetAddress remoteAddress) {
		// if nothing is specified, allow access
		if (allowedIPaddresses == null || allowedIPaddresses.trim().length() == 0)
			return true;

		for (String filter : allowedIPaddresses.split("\\s+")) {
			if (containsIP(filter, remoteAddress))
				return true;
		}

		LOG.warn("Printer access denied for " + remoteAddress);
		return false;
	}

	/**
	 * @param filter
	 * @param remoteAddress
	 * @return
	 */
	private boolean containsIP(String filter, InetAddress remoteAddress) {
		if (filter.contains("/")) {
			String[] masked = filter.split("/");
			try {
				return containsIP(masked[0], masked[1], remoteAddress);
			} catch (UnknownHostException e) {
				e.printStackTrace();
				return false;
			}
		} else {
			return remoteAddress.toString().equals("/" + filter);
		}
	}

	/**
	 * @param string
	 * @param string2
	 * @param remoteAddress
	 * @return
	 * @throws UnknownHostException
	 */
	private boolean containsIP(String network, String mask, InetAddress remoteAddress) throws UnknownHostException {
		System.err.println("Checking " + network + "/" + mask + " for " + remoteAddress.toString());
		byte[] address = InetAddress.getByName(network).getAddress();
		byte[] masked = null;
		byte[] remote = remoteAddress.getAddress();

		if (mask.contains(".")) {
			masked = InetAddress.getByName(mask).getAddress();
		} else {
			// could be numeric
			try {
				int bits = 0;
				bits = Integer.parseInt(mask);
				masked = new byte[address.length];
				for (int i = 0; i < masked.length; i++) {
					masked[i] = 0;
				}
				for (int i = 0; i < bits; i++) {
					masked[i / 8] = (byte) (masked[i / 8] >> 1);
					masked[i / 8] |= 128;
				}
			} catch (NumberFormatException nfe) {
				nfe.printStackTrace();
			}
		}
		if (masked == null)
			return false;

		System.err.println("Calculated mask: " + InetAddress.getByAddress(masked).toString());

		if (remote.length != address.length)
			return false;

		for (int i = 0; i < remote.length && i < address.length; i++) {
			if ((address[i] & masked[i]) != (remote[i] & masked[i]))
				return false;
		}
		return true;
	}

	public List<String> getSystemPrinters() {
		List<String> names = new ArrayList<String>();
		PrintService[] services = PrintServiceLookup.lookupPrintServices(null, null);
		if (services == null) {
			return null;
		}

		for (PrintService ps : services) {
			names.add(ps.getName());
		}
		return names;
	}

	/**
	 * @see org.iita.inventory.service.PrinterService#getPrinters()
	 */
	@Override
	@Transactional(readOnly=true)
	public List<PrinterInfo> getPrinters() {
		return list();
	}

	/**
	 * @throws PrinterException
	 * @see org.iita.inventory.service.PrinterService#selectPrinter(java.lang.String, org.iita.inventory.printing.PrinterInfo)
	 */
	@Override
	public void selectPrinter(String sessionId, PrinterInfo printer) throws PrinterException {
		if (sessionId == null) {
			LOG.error("Session ID is null when trying to select printer");
			throw new PrinterException("Session ID cannot be null");
		}
		if (printer == null) {
			LOG.info("Unselecting printer for session " + sessionId);
			this.selectedPrinters.remove(sessionId);
		} else {
			LOG.info("Selecting printer " + printer.getName() + " for " + sessionId);
			this.selectedPrinters.put(sessionId, printer.getId());
		}
	}

	@Override
	@Transactional
	public void deletePrinter(Integer id) {
		PrinterInfo pf = this.entityManager.find(PrinterInfo.class, id);
		if(pf!=null){
			this.entityManager.remove(pf);
		}
	}
}
