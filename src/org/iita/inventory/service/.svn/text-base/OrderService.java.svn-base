package org.iita.inventory.service;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.iita.inventory.model.GenebankOrder;
import org.iita.inventory.model.Order;
import org.iita.inventory.model.Order.OrderState;
import org.iita.inventory.model.QuantityUpdateBulk;
import org.iita.security.model.User;
import org.iita.struts.webfile.ServerFile;
import org.iita.util.PagedResult;

/**
 * Interface to define required methods for working with Orders
 * 
 * @author mobreza
 */
public interface OrderService {
	/**
	 * Get paginated list of orders
	 * 
	 * @param startAt record starting the page
	 * @param maxRequests page size
	 * @return Paged result
	 */
	PagedResult<? extends Order> listOrders(OrderState state, int startAt, int maxRequests);

	/**
	 * Method to create new order
	 * 
	 * @return
	 */
	GenebankOrder createOrder(User user);

	/**
	 * Load order from DB
	 * 
	 * @param id
	 * @return
	 */
	GenebankOrder getOrder(Long id);

	/**
	 * Update order information
	 * 
	 * @param order
	 */
	void updateOrder(Order order);

	/**
	 * @param order
	 * @param accessionNames
	 * @throws OrderException
	 */
	void addItemsByName(Order order, String accessionNames) throws OrderException;

	/**
	 * @param order
	 * @return
	 */
	File getRootDirectory(GenebankOrder order);

	/**
	 * @param order
	 * @param subDirectory
	 * @param fileName
	 * @return
	 * @throws IOException
	 */
	ServerFile getOrderFile(GenebankOrder order, String subDirectory, String fileName) throws IOException;

	/**
	 * @param order
	 * @param subDirectory
	 * @return
	 * @throws IOException
	 */
	List<ServerFile> getOrderFiles(GenebankOrder order, String subDirectory) throws IOException;

	/**
	 * Will generate QuantityUpdateBulk record linked with the order
	 * 
	 * @param order
	 */
	QuantityUpdateBulk startProcessing(GenebankOrder order);

	/**
	 * Set order status to rejected if possible
	 * 
	 * @param order
	 */
	void rejectOrder(GenebankOrder order);

	/**
	 * Split order to two: keep DISPATCHED items in original order, all other go to new Order
	 * 
	 * @param order
	 * @return
	 * @throws OrderException if the Order cannot be split (no items to move to new order)
	 */
	GenebankOrder split(GenebankOrder order) throws OrderException;

	/**
	 * Method called when JMS message received on new Order.
	 * @return
	 */
	GenebankOrder createInternetOrder();

	/**
	 * Method called when order updated via JMS
	 * @param order
	 */
	void updateInternetOrder(Order order);

	PagedResult<? extends Order> listOrders(OrderState state, int startAt, int maxRequests, User requester);

	//GenebankOrder createOrder(User user);
}
