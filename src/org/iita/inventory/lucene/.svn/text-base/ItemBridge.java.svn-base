/**
 * 
 */
package org.iita.inventory.lucene;

import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.hibernate.search.bridge.FieldBridge;
import org.iita.inventory.model.Item;

/**
 * @author mobreza
 * 
 */
public class ItemBridge implements FieldBridge {

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.hibernate.search.bridge.FieldBridge#set(java.lang.String, java.lang.Object, org.apache.lucene.document.Document,
	 * org.apache.lucene.document.Field.Store, org.apache.lucene.document.Field.Index, java.lang.Float)
	 */
	public void set(String name, Object value, // the department instance
			// (entity) in this case
			Document document, // the Lucene document
			Field.Store store, Field.Index index, Float boost) {

		Item item = (Item) value;

		String fieldValue = null;
		fieldValue = item.getName();
		fieldValue += explode(item.getName());
		if (item.getAlternativeIdentifier() != null) {
			fieldValue += " " + item.getAlternativeIdentifier();
			fieldValue += explode(item.getAlternativeIdentifier());
		}
		fieldValue += " " + item.getItemType().getName();

		Field field = new Field(name, fieldValue, store, index);
		// System.err.println("Adding " + name + ": " + fieldValue);

		if (boost != null)
			field.setBoost(boost);
		document.add(field);
	}

	/**
	 * @param name
	 * @return
	 */
	private String explode(String name) {
		if (name == null)
			return "";
		String exploded = "";

		String[] split = name.split("-");
		if (split.length > 1) {
			// expanded
			for (String s : split)
				exploded += " " + s;

			// concatenated
			exploded += " ";
			for (String s : split)
				exploded += s;
		}

		return exploded;
	}

}
