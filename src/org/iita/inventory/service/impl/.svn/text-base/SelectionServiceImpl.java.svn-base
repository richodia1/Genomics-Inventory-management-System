/**
 * 
 */
package org.iita.inventory.service.impl;

import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.iita.inventory.model.LotSelection;
import org.iita.inventory.service.SelectionService;
import org.iita.security.Authorize;
import org.iita.security.model.User;
import org.springframework.transaction.annotation.Transactional;

import com.opensymphony.xwork2.ActionContext;

/**
 * @author mobreza
 */
public class SelectionServiceImpl implements SelectionService {
	private static final Log LOG = LogFactory.getLog(SelectionServiceImpl.class);
	private static final Object SELECTEDLIST = "selection.my";
	private EntityManager entityManager;

	/**
	 * @param entityManager the entityManager to set
	 */
	@PersistenceContext
	public void setEntityManager(EntityManager entityManager) {
		this.entityManager = entityManager;
	}

	public SelectionServiceImpl() {

	}

	/*
	 * (non-Javadoc)
	 * @see org.iita.inventory.service.impl.SelectionService#getLists()
	 */
	@SuppressWarnings("unchecked")
	@Override
	@Transactional(readOnly = true)
	public List<LotSelection> getLists(User owner) {
		return (List<LotSelection>) this.entityManager.createQuery("select list from LotSelection list where list.owner=:user order by list.lastUpdated desc")
				.setParameter("user", owner).getResultList();
	}

	/**
	 * Add reference to selected list to session
	 * 
	 * @param selectedList
	 */
	@SuppressWarnings("unchecked")
	private void selectList(LotSelection selectedList) {
		ActionContext context = ActionContext.getContext();
		if (context == null) {
			LOG.debug("Cannot select List, ActionContext is null");
			return;
		}

		@SuppressWarnings("rawtypes")
		Map session = context.getSession();
		if (session != null)
			session.put(SELECTEDLIST, selectedList);
		else
			LOG.info("selectList(list): Could not retrieve session from ActionContext.");
	}

	/*
	 * (non-Javadoc)
	 * @see org.iita.inventory.service.impl.SelectionService#getSelectedList()
	 */
	public LotSelection getSelectedList() {
		LotSelection selectedList = (LotSelection) ActionContext.getContext().getSession().get(SELECTEDLIST);

		if (selectedList == null) {
			// add default list
			LotSelection defaultList = new LotSelection();
			LOG.debug("creating one list");
			defaultList.setName(String.format("Lot list %1$tF", new Date()));
			selectList(defaultList);
		}

		LOG.debug("returning " + selectedList);
		return selectedList;
	}

	/**
	 * Remove list from session and create new list
	 * 
	 * @return
	 */
	@Override
	public LotSelection createNewList() {
		ActionContext.getContext().getSession().remove(SELECTEDLIST);
		return getSelectedList();
	}

	@Override
	@Transactional
	public void save(LotSelection list) {
		list.setOwner(Authorize.getUser());
		LOG.debug("Set list owner to: " + list.getOwner());
		LOG.info("Saving list '" + list.getName() + "' with " + list.getSelectedLots().size() + " elements");
		if (list.getId() == null) {
			this.entityManager.persist(list);
		} else {
			this.entityManager.merge(list);
		}
	}

	/**
	 * Load list from DB and store to current list
	 * 
	 * @see org.iita.inventory.service.SelectionService#loadList(java.lang.String, org.iita.security.model.User)
	 */
	@Override
	@Transactional(readOnly = true)
	public LotSelection loadList(Long listId, User owner) {
		LOG.info("Loading list " + listId + " for user " + owner.getUsername());
		LotSelection list = new LotSelection();
		
		try {
			list = (LotSelection) this.entityManager.createQuery("select list from LotSelection list where list.owner=:user and list.id=:id")
			.setParameter("user", owner).setParameter("id", listId).getSingleResult();
		}
		catch (Exception e) {
			LOG.debug("EXECEPTION OCCURED: " + e.getMessage());
			return null;
		}
		

		// call this to initialize elements
		LOG.info("Loaded list " + list.getName() + " with " + list.getSelectedLots().size() + " elements");

		selectList(list);
		return list;
	}

	/**
	 * @see org.iita.inventory.service.SelectionService#delete(org.iita.inventory.model.LotSelection, org.iita.security.model.User)
	 */
	@Override
	@Transactional
	public void delete(LotSelection list, User owner) {
		if (list.getId() != null) {
			LotSelection dbList = loadList(list.getId(), owner);
			this.entityManager.remove(dbList);
			list.setId(null);
		}
	}
}
