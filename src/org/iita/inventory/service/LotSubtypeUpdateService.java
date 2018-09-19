/**
 * 
 */
package org.iita.inventory.service;

import java.util.List;

import org.iita.inventory.model.Lot;
import org.iita.inventory.model.LotSubtypeUpdate;
import org.iita.inventory.model.LotSubtypeUpdateBulk;
import org.iita.util.PagedResult;

/**
 * @author KOraegbunam
 *
 */
public interface LotSubtypeUpdateService {

	void store(LotSubtypeUpdateBulk lotSubtypeUpdate);

	void commit(LotSubtypeUpdateBulk lotSubtypeUpdate);
	
	/**
	 * Will undo the changes to individual lot quantities if the bulkUpdate was already commited and information needs to be restored. The update will only take
	 * place when {@link LotSubtypeUpdateBulk#getStatus()} is 1 (COMMITED). The status is reverted back to 0 (READY).
	 * 
	 * @param bulkUpdate
	 */
	void rollback(LotSubtypeUpdateBulk bulkUpdate);

	/**
	 * Delete information about the bulk update.
	 * 
	 * @param bulkUpdate Group of updates to mark as deleted
	 */
	void delete(LotSubtypeUpdateBulk bulkUpdate);

	/**
	 * Update information about a single lot quantity update. Updating is only allowed if the bulk update was not yet commited to the individual lots.
	 * 
	 * @param singleUpdate
	 */
	void store(LotSubtypeUpdate singleUpdate);

	/**
	 * Remove single update from database. Only available when bulk update not yet commited.
	 * 
	 * @param singleUpdate
	 */
	void delete(LotSubtypeUpdate singleUpdate);

	/**
	 * Load bulk information from the database.
	 * 
	 * @param id bulk update record ID
	 * @return <c>null</c> if the record does not exist.
	 */
	LotSubtypeUpdateBulk load(long id);

	/**
	 * Get list of bulk quantity updates ordered by most recent ones first.
	 * 
	 * @param startAt first record to return
	 * @param maxResults max number of records to return (aka pageSize)
	 * @return
	 */
	List<LotSubtypeUpdateBulk> list(int startAt, int maxResults);

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
	LotSubtypeUpdate loadItem(LotSubtypeUpdateBulk parentUpdateBulk, long itemid);

	/**
	 * List Bulk updates by their short name
	 * 
	 * @param types
	 * @param startAt
	 * @param pageSize
	 * @return
	 */
	PagedResult<LotSubtypeUpdateBulk> listByType(int startAt, int pageSize, String... transactionNames);

	/**
	 * @param description
	 * @param lot
	 * @return
	 */
	List<LotSubtypeUpdate> findItem(LotSubtypeUpdateBulk description, Lot lot);

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
	List<Lot> getAffectedLots(LotSubtypeUpdateBulk quantityUpdate);
	
	boolean verifyLot(Lot lot);
	
	Lot loadForUpdate(Lot lot);
}
