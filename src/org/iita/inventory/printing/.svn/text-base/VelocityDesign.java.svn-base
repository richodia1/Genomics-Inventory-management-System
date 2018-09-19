/**
 * 
 */
package org.iita.inventory.printing;

import java.io.IOException;
import java.io.StringWriter;
import java.text.SimpleDateFormat;
import java.util.Dictionary;
import java.util.Hashtable;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import org.apache.velocity.exception.MethodInvocationException;
import org.apache.velocity.exception.ParseErrorException;
import org.apache.velocity.exception.ResourceNotFoundException;
import org.iita.inventory.label.PrinterException;
import org.iita.inventory.model.Item;
import org.iita.inventory.model.Lot;

/**
 * Use Velocity templating engine to generate label data. The templates must be stored in /WEB-INF/classes/template folder and must follow a particular naming
 * scheme: <em>&lt;Lot class name>@&lt;LabelInfo class name>.vm</em> <br />
 * <br />
 * Example template names: InVitroLot.vm, InVitroLot@2col.vm
 * 
 * TODO Need to implement a Velocity designer that stores templates in a database. That way users can modify their templates on the fly.
 * 
 * @author mobreza
 * 
 */
// TODO Store velocity templates in database! Can now use TemplatingService!
public class VelocityDesign implements LabelDesigner {
	private static final Log log = LogFactory.getLog(LabelDesigner.class);
	private VelocityEngine velocityEngine;
	private Dictionary<String, Template> cache = new Hashtable<String, Template>();

	/**
	 * @param velocityEngine the velocityEngine to set
	 */
	public void setVelocityEngine(VelocityEngine velocityEngine) {
		this.velocityEngine = velocityEngine;
	}

	/**
	 * Velocity label design
	 * 
	 * @see org.iita.inventory.printing.LabelDesigner#printLabel(org.iita.inventory.model.Lot)
	 */
	@Override
	public String printLabel(LabelInfo labelInfo, Lot lot) {
		StringWriter sw = new StringWriter(265);
		streamLabel(labelInfo, sw, lot);
		//System.out.println(sw.toString());
		return sw.toString();
	}

	/**
	 * Get appropriate label design and use Velocity to merge the template into string.
	 * 
	 * @param labelInfo Label information
	 * @param sw Ouput stream
	 * @param lot Lot to label
	 */
	private void streamLabel(LabelInfo labelInfo, StringWriter sw, Lot lot) {
		Template template;
		try {
			template = getTemplate(labelInfo, lot);
		} catch (PrinterException e1) {
			e1.printStackTrace();
			return;
		} catch (Exception e) {
			e.printStackTrace();
			return;
		}

		VelocityContext context = new VelocityContext();
		// include tools POJOs
		context.put("number", new org.apache.velocity.tools.generic.NumberTool());
		context.put("date", new org.apache.velocity.tools.generic.DateTool());
		// label and format data
		context.put("title", getLabelTitle(lot.getItem()));
		context.put("label", labelInfo);
		context.put("lot", lot);
		context.put("dateFormat", new SimpleDateFormat("dd/MM/yyyy"));

		try {
			template.merge(context, sw);
		} catch (ResourceNotFoundException e) {
			e.printStackTrace();
		} catch (ParseErrorException e) {
			e.printStackTrace();
		} catch (MethodInvocationException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Get Velocity template for selected {@link LabelInfo} and {@link Lot}. Checks cache if template was already loaded, otherwise loads template from disk
	 * using {@link #findTemplate(String)}.
	 * 
	 * @param labelInfo
	 * @param lot
	 * @return cached template object
	 * @throws Exception
	 */
	private Template getTemplate(LabelInfo labelInfo, Lot lot) throws Exception {
		String cacheName = lot.getClass().getSimpleName() + "@" + labelInfo.getShortName();
		Template template = cache.get(cacheName);
		if (template == null) {
			// try loading full name template
			try {
				template = findTemplate(cacheName);
			} catch (ResourceNotFoundException e) {
				// do nothing, try loading with shorter name
				log.warn(e.getMessage());
			} catch (ParseErrorException e) {
				log.error(e.getMessage());
				throw new PrinterException(e.getMessage(), e);
			}

			if (template == null) {
				try {
					template = findTemplate(lot.getClass().getSimpleName());
				} catch (ResourceNotFoundException e) {
					log.error(e.getMessage());
					throw new PrinterException(e.getMessage(), e);
				} catch (ParseErrorException e) {
					log.error(e.getMessage());
					throw new PrinterException(e.getMessage(), e);
				}
			}

			// cache it!
			if (template != null)
				this.cache.put(cacheName, template);
		}

		return template;
	}

	/**
	 * Load template from template folder
	 * 
	 * @param cacheName
	 * @return
	 * @throws Exception
	 */
	private Template findTemplate(String cacheName) throws Exception {
		Template template;
		log.info("Loading" + "/template/" + cacheName + ".vm");
		template = velocityEngine.getTemplate("/template/" + cacheName + ".vm");
		return template;
	}

	/**
	 * Get label title for item.
	 * 
	 * Label title is generated by the following rules: if the item has IITA Prefix and AccessionIdentifier assigned, the label title is
	 * 
	 * <pre>
	 * Prefix + &quot;-&quot; + AccessionIdentifier
	 * </pre>
	 * 
	 * If AccessionIdentifier has not been assigned to the item, item name {@link Item#getName()} is used.
	 * 
	 * @param item
	 * @return Label title.
	 */
	private String getLabelTitle(Item item) {
		if (item.getPrefix() != null && item.getAccessionIdentifier() != null)
			return String.format("%1s-%1d", item.getPrefix(), item.getAccessionIdentifier());
		else if (item.getName().length() < 14)
			return item.getName();
		else
			return "*" + item.getName().substring(item.getName().length() - 14);
	}
}
