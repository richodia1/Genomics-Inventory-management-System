/**
 * 
 */
package org.iita.inventory.service.impl;

import java.util.Collection;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.queryParser.MultiFieldQueryParser;
import org.apache.lucene.queryParser.ParseException;
import org.apache.lucene.search.Query;
import org.hibernate.search.jpa.FullTextEntityManager;
import org.hibernate.search.jpa.Search;
import org.iita.inventory.model.Lot;
import org.iita.inventory.service.SearchService;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author mobreza
 * 
 */
public class SearchServiceImpl implements SearchService {
	private static final Log LOG = LogFactory.getLog(SearchService.class);
	private EntityManager entityManager;

	/**
	 * @return the entityManager
	 */
	public EntityManager getFullTextEntityManager() {
		return this.entityManager;
	}

	/**
	 * @param entityManager the entityManager to set
	 */
	@PersistenceContext
	public void setEntityManager(EntityManager entityManager) {
		this.entityManager = entityManager;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.iita.inventory.service.SearchService#search(java.lang.String)
	 */
	@SuppressWarnings("unchecked")
	@Override
	@Transactional(readOnly=true)
	public Collection<Lot> search(String searchQuery, Class<?> clazz, String[] fields, int startAt, int maxResults) throws ParseException {
		if (searchQuery == null) {
			LOG.trace("Ignoring search for null query");
			return null;
		}

		FullTextEntityManager ftEm = Search.createFullTextEntityManager(this.entityManager);
		org.apache.lucene.search.Query luceneQuery = getLuceneQuery(searchQuery, fields);
		if (luceneQuery != null) {
			javax.persistence.Query query = ftEm.createFullTextQuery(luceneQuery, clazz);
			return query.setMaxResults(maxResults).setFirstResult(startAt).getResultList();
		} else
			return null;
	}

	/**
	 * @param searchQuery
	 * @return
	 * @throws ParseException
	 */
	private Query getLuceneQuery(String searchQuery, String[] fields) throws ParseException {
		/*
		 * String[] strings = new String[] { "item.name", "location.name", "item.description", "notes", "item.alternativeIdentifier", "location.parent.name",
		 * "location.parent.parent.name", "location.parent.parent.parent.name" };
		 */
		MultiFieldQueryParser parser = new MultiFieldQueryParser(fields, new StandardAnalyzer());
		try {
			return parser.parse(searchQuery);
		} catch (ParseException e) {
			return null;
		}
	}
}
