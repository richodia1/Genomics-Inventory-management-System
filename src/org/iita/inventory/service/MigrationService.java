/**
 * 
 */
package org.iita.inventory.service;

import org.iita.inventory.model.Location;
import org.iita.inventory.model.Migration;
import org.iita.util.PagedResult;

/**
 * @author mobreza
 * 
 */
public interface MigrationService {
	/**
	 * Get logs of migrations to a specified location, ordered by date desc
	 * 
	 * @param destination
	 * @return
	 */
	public PagedResult<Migration> getMigrationsTo(Location source, int startAt, int maxResults);

	/**
	 * Get logs of migrations from a specified location, ordered by date desc
	 * 
	 * @param source
	 * @return
	 */
	public PagedResult<Migration> getMigrationsFrom(Location source, int startAt, int maxResults);

	/**
	 * @param i
	 * @return
	 */
	public PagedResult<Migration> getLastMigrations(int startAt, int maxResults);
}
