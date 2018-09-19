package org.iita.inventory.model;

import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.MappedSuperclass;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import org.iita.entity.VersionedEntity;
import org.iita.inventory.service.OrderException;

/**
 * A generic Order item contains the basic information for any Ordering system. Basic ordering flow is supported via {@link #state} attribute.
 * 
 * @author mobreza
 */
@MappedSuperclass
public abstract class Order extends VersionedEntity {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = -2207817253823761059L;

	/**
	 * Possible Order states
	 */
	public enum OrderState {
		/** The NEW. */
		NEW,
		/** The REJECTED. */
		REJECTED,
		/** The PENDING. */
		PENDING,
		/** The APPROVED. */
		APPROVED,
		/** The SHIPPED. */
		SHIPPED,
		/** The ALL. */
		ALL,
		/** The CLOSED. */
		CLOSED;
	}

	/** The state. */
	private OrderState state;

	/** Order date */
	private Date orderDate;

	/** The last name. */
	private String lastName;

	/** The first name. */
	private String firstName;

	/** The title. */
	private String title;

	/** The organization. */
	private String organization;

	/** The shipping address. */
	private String shippingAddress;

	/** Contact mail address of requestor */
	private String mail;

	/** Country */
	private String country;

	private String contactPerson;
	
	/**
	 * Default constructor
	 */
	public Order() {
		
	}

	/**
	 * Copy constructor 
	 *
	 * @param order
	 */
	public Order(Order order) {
		this.state=order.state;
		this.orderDate=order.orderDate;
		this.lastName=order.lastName;
		this.firstName=order.firstName;
		this.title=order.title;
		this.organization=order.organization;
		this.shippingAddress=order.shippingAddress;
		this.mail=order.mail;
		this.country=order.country;
		this.contactPerson=order.contactPerson;
	}

	/**
	 * Gets the state.
	 * 
	 * @return the state
	 */
	@Enumerated(EnumType.STRING)
	public OrderState getState() {
		return state;
	}

	/**
	 * Sets the state.
	 * 
	 * @param state the new state
	 */
	public void setState(OrderState state) {
		this.state = state;
	}

	/**
	 * Gets the last name.
	 * 
	 * @return the last name
	 */
	@Column(length = 100)
	public String getLastName() {
		return lastName;
	}

	/**
	 * Sets the last name.
	 * 
	 * @param lastName the new last name
	 */
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	/**
	 * Gets the first name.
	 * 
	 * @return the first name
	 */
	@Column(length = 100)
	public String getFirstName() {
		return firstName;
	}

	/**
	 * Sets the first name.
	 * 
	 * @param firstName the new first name
	 */
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	/**
	 * Gets the order title. This field is used to assign user-friendly names to orders. Orders generated through other services will use this field to indicate
	 * where order comes from (e.g. "On-line order #234")
	 * 
	 * @return the title
	 */
	@Column(length = 300)
	public String getTitle() {
		return title;
	}

	/**
	 * Sets the title.
	 * 
	 * @param title the new title
	 */
	public void setTitle(String title) {
		this.title = title;
	}

	/**
	 * Gets the organization.
	 * 
	 * @return the organization
	 */
	@Column(length = 100)
	public String getOrganization() {
		return organization;
	}

	/**
	 * Sets the organization.
	 * 
	 * @param organization the new organization
	 */
	public void setOrganization(String organization) {
		this.organization = organization;
	}

	/**
	 * Gets the shipping address.
	 * 
	 * @return the shipping address
	 */
	@Column(length = 500)
	public String getShippingAddress() {
		return shippingAddress;
	}

	/**
	 * Sets the shipping address.
	 * 
	 * @param shippingAddress the new shipping address
	 */
	public void setShippingAddress(String shippingAddress) {
		this.shippingAddress = shippingAddress;
	}

	@Transient
	public abstract List<? extends OrderItem> getItems();

	/**
	 * Get date order was placed
	 * 
	 * @return the orderDate
	 */
	@Temporal(TemporalType.TIMESTAMP)
	public Date getOrderDate() {
		return this.orderDate;
	}

	/**
	 * @param orderDate the orderDate to set
	 */
	public void setOrderDate(Date orderDate) {
		this.orderDate = orderDate;
	}

	public abstract void addItems(List<Item> items) throws OrderException;

	/**
	 * @return the mail
	 */
	public String getMail() {
		return this.mail;
	}

	/**
	 * @param mail the mail to set
	 */
	public void setMail(String mail) {
		this.mail = mail;
	}

	/**
	 * @return the country
	 */
	public String getCountry() {
		return this.country;
	}

	/**
	 * @param country the country to set
	 */
	public void setCountry(String country) {
		this.country = country;
	}
	
	
	/**
	 * @return the contactPerson
	 */
	public String getContactPerson() {
		return this.contactPerson;
	}

	/**
	 * @param contactPerson the contactPerson to set
	 */
	public void setContactPerson(String contactPerson) {
		this.contactPerson = contactPerson;
	}

	/**
	 * @return
	 */
	@Transient
	public abstract List<Item> getItemElements();

}
