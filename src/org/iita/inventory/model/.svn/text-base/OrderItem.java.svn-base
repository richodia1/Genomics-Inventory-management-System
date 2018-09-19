package org.iita.inventory.model;

import javax.persistence.Column;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.ManyToOne;
import javax.persistence.MappedSuperclass;
import javax.persistence.Transient;

import org.iita.entity.VersionedEntity;

/**
 * Generic Order item as part of the {@link Order}. Any order can have several ordered items.
 * 
 * @author mobreza
 */
@MappedSuperclass
public abstract class OrderItem extends VersionedEntity {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 6217631945948036374L;

	private String itemName;

	/** The item. */
	private Item item;

	/** The quantity. */
	private Double quantity;

	/** The scale. */
	private String scale;

	private OrderItemStatus status = OrderItemStatus.PENDING;

	public enum OrderItemStatus {
		PENDING, DISPATCHED
	}

	/**
	 * Gets the order.
	 * 
	 * @return the order
	 */
	@Transient
	public abstract Order getOrder();

	/**
	 * @param itemName the itemName to set
	 */
	public void setItemName(String itemName) {
		this.itemName = itemName;
	}

	/**
	 * @return the itemName
	 */
	@Column(length = 200, nullable = false)
	public String getItemName() {
		return this.itemName;
	}

	/**
	 * Gets the item.
	 * 
	 * @return the item
	 */
	@ManyToOne(cascade = {}, optional = true)
	public Item getItem() {
		return item;
	}

	/**
	 * Sets the item.
	 * 
	 * @param item the new item
	 */
	public void setItem(Item item) {
		this.item = item;
	}

	/**
	 * Gets the quantity.
	 * 
	 * @return the quantity
	 */
	public Double getQuantity() {
		return quantity;
	}

	/**
	 * Sets the quantity.
	 * 
	 * @param quantity the new quantity
	 */
	public void setQuantity(Double quantity) {
		this.quantity = quantity;
	}

	/**
	 * Gets the scale.
	 * 
	 * @return the scale
	 */
	@Column(length = 20, nullable = true)
	public String getScale() {
		return scale;
	}

	/**
	 * Sets the scale.
	 * 
	 * @param scale the new scale
	 */
	public void setScale(String scale) {
		this.scale = scale;
	}

	/**
	 * @return the status
	 */
	@Enumerated(EnumType.STRING)
	public OrderItemStatus getStatus() {
		return this.status;
	}

	/**
	 * @param status the status to set
	 */
	public void setStatus(OrderItemStatus status) {
		this.status = status;
	}
}
