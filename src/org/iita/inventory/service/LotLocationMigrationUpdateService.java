/**
 * 
 */
package org.iita.inventory.service;

import org.iita.inventory.model.MigrationLotLocationUpdate;
import org.iita.inventory.model.MigrationLotLocationUpdateBulk;

/**
 * @author KOraegbunam
 *
 */
public interface LotLocationMigrationUpdateService {
	void store(MigrationLotLocationUpdateBulk lotLocationMigrationUpdate);

	void commit(MigrationLotLocationUpdateBulk lotSubtypeUpdate);

	/**
	 * Delete information about the bulk update.
	 * 
	 * @param bulkUpdate Group of updates to mark as deleted
	 */
	void delete(MigrationLotLocationUpdateBulk bulkUpdate);

	/**
	 * Update information about a single lot quantity update. Updating is only allowed if the bulk update was not yet commited to the individual lots.
	 * 
	 * @param singleUpdate
	 */
	void store(MigrationLotLocationUpdate singleUpdate);

	/**
	 * Remove single update from database. Only available when bulk update not yet commited.
	 * 
	 * @param singleUpdate
	 */
	void delete(MigrationLotLocationUpdate singleUpdate);

	/**
	 * Load bulk information from the database.
	 * 
	 * @param id bulk update record ID
	 * @return <c>null</c> if the record does not exist.
	 */
	MigrationLotLocationUpdateBulk load(long id);
}
