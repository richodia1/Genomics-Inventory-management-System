/**
 * inventory.Struts Oct 7, 2010
 */
package org.iita.inventory.remote;

import java.io.StringReader;
import java.util.Enumeration;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Session;
import javax.jms.TextMessage;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.iita.inventory.model.GenebankOrder;
import org.iita.inventory.model.GenebankOrderItem;
import org.iita.inventory.service.ItemService;
import org.iita.inventory.service.OrderService;
import org.springframework.jms.listener.SessionAwareMessageListener;
import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

/**
 * @author mobreza
 */
public class OrderJMSReceiver implements SessionAwareMessageListener {
	private static Log LOG = LogFactory.getLog(OrderJMSReceiver.class);
	private ItemService itemService;
	private OrderService orderService;

	/**
	 * @param itemService
	 * @param orderService
	 */
	public OrderJMSReceiver(ItemService itemService, OrderService orderService) {
		this.itemService = itemService;
		this.orderService = orderService;
	}

	/**
	 * @see org.springframework.jms.listener.SessionAwareMessageListener#onMessage(javax.jms.Message, javax.jms.Session)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public void onMessage(Message message, Session session) throws JMSException {
		LOG.debug("Received JSM message");
		Enumeration<String> x = message.getPropertyNames();
		while (x.hasMoreElements()) {
			String propertyName = (String) x.nextElement();
			LOG.debug("Prop: " + propertyName);
		}

		TextMessage textMessage = (TextMessage) message;
		Long remoteOrderId = textMessage.getLongProperty("orderId");
		LOG.info("Received information about remote order id #" + remoteOrderId);
		LOG.debug("XML\n" + textMessage.getStringProperty("xml"));

		// create Order
		final GenebankOrder order = this.orderService.createInternetOrder();
		order.setRemoteOrderId(remoteOrderId);

		try {
			SAXParser parser = SAXParserFactory.newInstance().newSAXParser();
			parser.parse(new InputSource(new StringReader(textMessage.getStringProperty("xml"))), new DefaultHandler() {
				private StringBuffer textbuffer = new StringBuffer();
				private GenebankOrderItem orderItem;
				private String address = "";

				@Override
				public void startElement(String uri, String localName, String name, Attributes attributes) throws SAXException {
					LOG.debug("start element: uri=" + uri + " local=" + localName + " name=" + name + " attr=" + attributes.getLength());
					if (attributes != null) {
						for (int i = 0; i < attributes.getLength(); i++) {
							LOG.debug("\tattr " + attributes.getLocalName(i) + " = " + attributes.getValue(i));
						}
					}
					textbuffer.replace(0, textbuffer.length(), "");
					if ("requestor".equals(name)) {

					} else if ("request".equals(name)) {

					} else if ("requestitem".equals(name)) {
						if (attributes.getValue("reference") == null) {
							LOG.debug("New item starting");
							this.orderItem = new GenebankOrderItem();
							this.orderItem.setOrder(order);
						}
					}
				}

				@Override
				public void endElement(String uri, String localName, String name) throws SAXException {
					if (textbuffer.length() > 0)
						LOG.debug("#CDATA: " + textbuffer.toString());
					LOG.debug("end element: uri=" + uri + " local=" + localName + " name=" + name);

					if ("requestor".equals(name)) {

					} else if ("lastName".equals(name)) {
						order.setLastName(textbuffer.toString());
					} else if ("firstName".equals(name)) {
						order.setFirstName(textbuffer.toString());
					} else if ("organization".equals(name)) {
						order.setOrganization(textbuffer.toString());
					} else if ("organizationType".equals(name)) {
						order.setOrganizationCategory(textbuffer.toString());
					} else if ("email".equals(name)) {
						order.setMail(textbuffer.toString());
					} else if ("address".equals(name)) {
						address += textbuffer.toString() + "\n";
					} else if ("postalCode".equals(name)) {
						address += "Postal code: " + textbuffer.toString() + "\n";
					} else if ("city".equals(name)) {
						address += textbuffer.toString() + "\n";
					} else if ("country".equals(name)) {
						order.setCountry(textbuffer.toString());
					} else if ("quantity".equals(name)) {
						this.orderItem.setQuantity(Double.parseDouble(textbuffer.toString()));
					} else if ("uom".equals(name)) {
						this.orderItem.setScale(textbuffer.toString());
					} else if ("accession".equals(name)) {
						this.orderItem.setItemName(textbuffer.toString());
						this.orderItem.setItem(itemService.findByName(textbuffer.toString()));
					} else if ("requestitem".equals(name)) {
						if (this.orderItem!=null && this.orderItem.getItemName() != null) {
							LOG.debug("Adding item: " + this.orderItem);
							order.getItems().add(this.orderItem);
						}
						this.orderItem = null;
					} else if ("request".equals(name)) {
						order.setShippingAddress(address);
					} else if ("smtaAccepted".equals(name)) {

					} else if ("requiresImportPermit".equals(name)) {

					} else if ("organizationType".equals(name)) {
						order.setOrganizationCategory(textbuffer.toString());
					} else if ("intendedUse".equals(name)) {
						order.setPurpose(textbuffer.toString());
					} else if ("description".equals(name)) {
						order.setDescription(textbuffer.toString());
					}

					textbuffer.replace(0, textbuffer.length(), "");
				}

				public void characters(char[] ch, int start, int length) throws SAXException {
					textbuffer.append(ch, start, length);
				}
			});

			this.orderService.updateInternetOrder(order);

		} catch (Exception e) {
			LOG.error(e, e);
			throw new RuntimeException("Error parsing XML order!");
		}

		message.acknowledge();
	}
}
