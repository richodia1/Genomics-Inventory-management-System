/**
 * 
 */
package org.iita.inventory.service;

import java.util.List;

import org.iita.inventory.model.GenebankOrder;
import org.iita.inventory.model.Lot;
import org.iita.inventory.model.QuantityUpdate;
import org.iita.inventory.model.QuantityUpdateBulk;
import org.iita.util.PagedResult;

/**
 * Quantity update service.
 * 
 * @author mobreza
 */
public interface QuantityUpdateService {
	/**
	 * Insert new bulk update record and all individual lot updates contained in the {@link QuantityUpdateBulk#getLots()}. The lot quantities are NOT modified
	 * (see {@link QuantityUpdateService#commit(QuantityUpdateBulk)}).
	 * 
	 * @param bulkUpdate Bulk update information
	 */
	void store(QuantityUpdateBulk bulkUpdate);

	/**
	 * Try commiting the quantity update to each individual lot. If any one lot does not contain the required quantity, the whole transaction will fail
	 * (throwing an Exception). The update will only take place when {@link QuantityUpdateBulk#getStatus()} is 0 (READY). The status of a commited bulk update
	 * is set to 1 (COMMITED).
	 * 
	 * @param bulkUpdate Bulk update
	 */
	void commit(QuantityUpdateBulk bulkUpdate);

	/**
	 * Will undo the changes to individual lot quantities if the bulkUpdate was already commited and information needs to be restored. The update will only take
	 * place when {@link QuantityUpdateBulk#getStatus()} is 1 (COMMITED). The status is reverted back to 0 (READY).
	 * 
	 * @param bulkUpdate
	 */
	void rollback(QuantityUpdateBulk bulkUpdate);

	/**
	 * Delete information about the bulk update.
	 * 
	 * @param bulkUpdate Group of updates to mark as deleted
	 */
	void delete(QuantityUpdateBulk bulkUpdate);

	/**
	 * Update information about a single lot quantity update. Updating is only allowed if the bulk update was not yet commited to the individual lots.
	 * 
	 * @param singleUpdate
	 */
	void store(QuantityUpdate singleUpdate);

	/**
	 * Remove single update from database. Only available when bulk update not yet commited.
	 * 
	 * @param singleUpdate
	 */
	void delete(QuantityUpdate singleUpdate);

	/**
	 * Load bulk information from the database.
	 * 
	 * @param id bulk update record ID
	 * @return <c>null</c> if the record does not exist.
	 */
	QuantityUpdateBulk load(long id);

	/**
	 * Get list of bulk quantity updates ordered by most recent ones first.
	 * 
	 * @param startAt first record to return
	 * @param maxResults max number of records to return (aka pageSize)
	 * @return
	 */
	List<QuantityUpdateBulk> list(int startAt, int maxResults);

	/**
	 * Return count of all records in DB.
	 * 
	 * @return number of all records in DB
	 */
	long countAll();

	/**
	 * Load an individual item from bulk update batch. If the requested item is not registered within the bulk, <c>null</c> is returned.
	 * 
	 * @param parentUpdateBulk Bulk record
	 * @param itemid Subitem identifier
	 * @return Individual update item with the specified id, or <c>null</c>.
	 */
	QuantityUpdate loadItem(QuantityUpdateBulk parentUpdateBulk, long itemid);

	/**
	 * List Bulk updates by their short name
	 * 
	 * @param types
	 * @param startAt
	 * @param pageSize
	 * @return
	 */
	PagedResult<QuantityUpdateBulk> listByType(int startAt, int pageSize,  Long location, String title, String... transactionNames);

	/**
	 * @param description
	 * @param lot
	 * @return
	 */
	List<QuantityUpdate> findItem(QuantityUpdateBulk description, Lot lot);

	/**
	 * Return a distinct list of all used transaction subtypes
	 * 
	 * @return
	 */
	String[] getTransactionSubtypes();

	/**
	 * Get inventory lots affected by this quantity update
	 * 
	 * @param quantityUpdate
	 * @return
	 */
	List<Lot> getAffectedLots(QuantityUpdateBulk quantityUpdate);

	/**
	 * Generate a missing order for distribution
	 * 
	 * @param description
	 */
	GenebankOrder createOrder(QuantityUpdateBulk description);
}