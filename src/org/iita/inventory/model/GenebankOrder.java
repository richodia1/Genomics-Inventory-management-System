package org.iita.inventory.model;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Transient;

import org.iita.crm.model.EntityTag;
import org.iita.crm.model.Taggable;
import org.iita.inventory.model.OrderItem.OrderItemStatus;
import org.iita.inventory.service.OrderException;
import org.iita.security.model.User;
import org.iita.util.StringUtil;

/**
 * Germplasm request / Genebank Order contains additional information that genebank wants to collect from requesting party.
 * 
 * @author mobreza
 */
@Entity
public class GenebankOrder extends Order implements Taggable<GenebankOrder> {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = -7655537853689468039L;

	/** The purpose. */
	private String purpose;

	/** The description. */
	private String description;

	/** Organization category */
	private String organizationCategory;

	/** Extra contact info */
	private String contactInfo;

	private List<GenebankOrderItem> items = new ArrayList<GenebankOrderItem>();

	private List<QuantityUpdateBulk> updates = new ArrayList<QuantityUpdateBulk>();

	private Long remoteOrderId = null;

	/** Shipping cost in USD */
	private Double shippingCost;

	private List<OrderTag> tags;

	/** Type of SMTA: Signature, Shrink-wrap, Click-wrap */
	private String agreementOption;
	private boolean smta611;

	/** Person signing agreement on provider end */
	private String providerName;
	private String providerOrganization;
	
	/**User*/
	private User requester;

	/**
	 * 
	 */
	public GenebankOrder() {
		
	}
	
	/**
	 * @param order
	 */
	public GenebankOrder(GenebankOrder order) {
		super(order);
		this.purpose=order.purpose;
		this.description=order.description;
		this.organizationCategory=order.organizationCategory;
		this.contactInfo=order.contactInfo;
		this.remoteOrderId=order.remoteOrderId;
		this.shippingCost=order.shippingCost;
		this.agreementOption=order.agreementOption;
		this.smta611=order.smta611;
		this.providerName=order.providerName;
		this.providerOrganization=order.providerOrganization;
	}

	/**
	 * Gets the purpose.
	 * 
	 * @return the purpose
	 */
	@Column(length = 200, nullable = false)
	public String getPurpose() {
		return purpose;
	}

	/**
	 * Sets the purpose.
	 * 
	 * @param purpose the new purpose
	 */
	public void setPurpose(String purpose) {
		this.purpose = StringUtil.nullOrString(purpose);
	}

	/**
	 * Gets the description.
	 * 
	 * @return the description
	 */
	@Lob
	public String getDescription() {
		return description;
	}

	/**
	 * Sets the description.
	 * 
	 * @param description the new description
	 */
	public void setDescription(String description) {
		this.description = description;
	}

	@Override
	@OneToMany(mappedBy = "order", cascade = CascadeType.ALL)
	public List<GenebankOrderItem> getItems() {
		return this.items;
	}

	public void setItems(List<GenebankOrderItem> items) {
		this.items = items;
	}

	/**
	 * @see org.iita.inventory.model.Order#addItems(java.util.List)
	 */
	@Override
	public void addItems(List<Item> items) throws OrderException {
		List<GenebankOrderItem> orderItems = this.getItems();
		if (orderItems == null)
			orderItems = new ArrayList<GenebankOrderItem>();
		for (Item newItem : items) {
			//boolean found = false;
			//			for (OrderItem oi : orderItems) {
			//				if (oi.getItem().getId().longValue() == newItem.getId().longValue())
			//					found = true;
			//			}
			//if (!found) {
			GenebankOrderItem i = new GenebankOrderItem();
			i.setItem(newItem);
			i.setOrder(this);
			i.setItemName(newItem.getName());
			orderItems.add(i);
			//}
		}
		// set back reference in case we created the list
		this.items = orderItems;
	}

	/**
	 * @return the update
	 */
	@OneToMany(cascade = {}, mappedBy = "order")
	public List<QuantityUpdateBulk> getUpdates() {
		return this.updates;
	}

	/**
	 * @param update the update to set
	 */
	public void setUpdates(List<QuantityUpdateBulk> update) {
		this.updates = update;
	}

	/**
	 * @return the organizationCategory
	 */
	@Column(length = 200)
	public String getOrganizationCategory() {
		return this.organizationCategory;
	}

	/**
	 * @param organizationCategory the organizationCategory to set
	 */
	public void setOrganizationCategory(String organizationCategory) {
		this.organizationCategory = organizationCategory;
	}

	/**
	 * @return the remoteOrderId
	 */
	public Long getRemoteOrderId() {
		return this.remoteOrderId;
	}

	/**
	 * @param remoteOrderId the remoteOrderId to set
	 */
	public void setRemoteOrderId(Long remoteOrderId) {
		this.remoteOrderId = remoteOrderId;
	}

	/**
	 * Get a list of Items from {@link #items} list
	 * 
	 * @see org.iita.inventory.model.Order#getItemElements()
	 */
	@Override
	@Transient
	public List<Item> getItemElements() {
		List<GenebankOrderItem> orderedItems = this.getItems();
		if (orderedItems == null)
			return null;
		List<Item> items = new ArrayList<Item>();
		for (GenebankOrderItem orderedItem : orderedItems) {
			items.add(orderedItem.getItem());
		}
		return items;
	}

	/**
	 * Find all requested (and picked lots) that were not yet distributed (status=PENDING)
	 * 
	 * @return
	 */
	public List<GenebankOrderItem> findMissing(QuantityUpdateBulk update) {
		ArrayList<GenebankOrderItem> missing = new ArrayList<GenebankOrderItem>();

		for (GenebankOrderItem item : this.getItems()) {
			if (item.getStatus() == OrderItemStatus.DISPATCHED)
				continue;
			if (item.getLot() == null)
				continue;

			boolean found = false;
			for (QuantityUpdate u : update.getLots()) {
				if (u.getLot().getId().equals(item.getLot().getId())) {
					found = true;
					break;
				}
			}
			if (!found)
				missing.add(item);
		}

		return missing;
	}

	/**
	 * Find order item based on requested lot
	 * 
	 * @param lot
	 * @return
	 */
	public GenebankOrderItem findItem(Lot lot) {
		for (GenebankOrderItem item : this.items) {
			if (item.getLot() != null && lot.getId().equals(item.getLot().getId()))
				return item;
		}
		return null;
	}

	/**
	 * @return the contactInfo
	 */
	@Column(length = 200)
	public String getContactInfo() {
		return this.contactInfo;
	}

	/**
	 * @param contactInfo the contactInfo to set
	 */
	public void setContactInfo(String contactInfo) {
		this.contactInfo = contactInfo;
	}

	/**
	 * @see org.iita.crm.model.Taggable#getTagClass()
	 */
	@Override
	@Transient
	public Class<? extends EntityTag<GenebankOrder>> getTagClass() {
		return OrderTag.class;
	}

	/**
	 * @see org.iita.crm.model.Taggable#getTags()
	 */
	@Override
	@OneToMany(cascade = { CascadeType.REMOVE }, mappedBy = "entity")
	public List<OrderTag> getTags() {
		return this.tags;
	}

	/**
	 * @param tags the tags to set
	 */
	public void setTags(List<OrderTag> tags) {
		this.tags = tags;
	}

	/**
	 * @return the shippingCost
	 */
	public Double getShippingCost() {
		return this.shippingCost;
	}

	/**
	 * @param shippingCost the shippingCost to set
	 */
	public void setShippingCost(Double shippingCost) {
		this.shippingCost = shippingCost;
	}

	/**
	 * @return the agreementOption
	 */
	@Column(length = 50)
	public String getAgreementOption() {
		return this.agreementOption;
	}

	/**
	 * @param agreementOption the agreementOption to set
	 */
	public void setAgreementOption(String agreementOption) {
		this.agreementOption = agreementOption;
	}

	/**
	 * @return the signatory
	 */
	@Column(length = 200)
	public String getProviderName() {
		return this.providerName;
	}

	/**
	 * @param providerName the providerName to set
	 */
	public void setProviderName(String providerName) {
		this.providerName = providerName;
	}

	/**
	 * @return the providerOrganization
	 */
	@Column(length = 200)
	public String getProviderOrganization() {
		return this.providerOrganization;
	}

	/**
	 * @param providerOrganization the providerOrganization to set
	 */
	public void setProviderOrganization(String providerOrganization) {
		this.providerOrganization = providerOrganization;
	}

	/**
	 * @return the smta611
	 */
	public boolean isSmta611() {
		return this.smta611;
	}

	/**
	 * @param smta611 the smta611 to set
	 */
	public void setSmta611(boolean smta611) {
		this.smta611 = smta611;
	}
	
	/**
	 * @return the requester
	 */
	@ManyToOne(cascade = {}, optional = true)
	public User getRequester() {
		return requester;
	}

	/**
	 * @param requester the requester to set
	 */
	public void setRequester(User requester) {
		this.requester = requester;
	}
}
