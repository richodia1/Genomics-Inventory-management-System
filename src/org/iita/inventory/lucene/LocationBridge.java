/**
 * 
 */
package org.iita.inventory.lucene;

import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.hibernate.search.bridge.FieldBridge;
import org.iita.inventory.model.Location;

/**
 * @author mobreza
 * 
 *         Location bridge generates a full location path name to be stored in Lucene index by concatenating names of all parent locations into one Document
 *         field.
 */
public class LocationBridge implements FieldBridge {

	/**
	 * Generate document field that contains all parent location names.
	 * 
	 * @see org.hibernate.search.bridge.FieldBridge#set(java.lang.String, java.lang.Object, org.apache.lucene.document.Document,
	 *      org.apache.lucene.document.Field.Store, org.apache.lucene.document.Field.Index, java.lang.Float)
	 */
	public void set(String name, Object value, // the department instance
			// (entity) in this case
			Document document, // the Lucene document
			Field.Store store, Field.Index index, Float boost) {

		Location location = (Location) value;

		String fieldValue = "";
		while (location != null) {
			fieldValue = location.getName() + " " + fieldValue;
			location = location.getParent();
		}

		Field field = new Field(name, fieldValue, store, index);

		if (boost != null)
			field.setBoost(boost);
		document.add(field);
	}
}
