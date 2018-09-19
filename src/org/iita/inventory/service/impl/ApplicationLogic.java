/**
 * inventory.Struts Oct 22, 2009
 */
package org.iita.inventory.service.impl;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.iita.crm.model.EntityTag;
import org.iita.crm.model.Tag;
import org.iita.crm.model.Taggable;
import org.iita.crm.service.TagCloud;
import org.iita.crm.service.TagService;
import org.iita.inventory.model.GenebankOrder;
import org.iita.inventory.model.GenebankOrderItem;
import org.iita.inventory.model.Item;
import org.iita.inventory.model.Order;
import org.iita.inventory.model.Order.OrderState;
import org.iita.inventory.model.OrderItem.OrderItemStatus;
import org.iita.inventory.model.OrderTag;
import org.iita.inventory.model.QuantityUpdateBulk;
import org.iita.inventory.model.Transaction2.Type;
import org.iita.service.EmailService;
import org.iita.inventory.service.ItemService;
import org.iita.inventory.service.OrderException;
import org.iita.inventory.service.OrderService;
import org.iita.security.model.User;
import org.iita.service.EmailException;
import org.iita.service.TemplatingException;
import org.iita.service.TemplatingService;
import org.iita.struts.webfile.ServerFile;
import org.iita.util.NaturalOrderComparator;
import org.iita.util.PagedResult;
import org.springframework.security.annotation.Secured;
import org.springframework.transaction.annotation.Transactional;

/**
 * A general Genebank Application business logic implementation
 * 
 * @author mobreza
 */
public class ApplicationLogic implements OrderService, TagService {
	private static final Log LOG = LogFactory.getLog(ApplicationLogic.class);
	private EntityManager entityManager;
	private ItemService itemService;
	private File ordersDirectory;
	private EmailService emailService;
	private TemplatingService templatingService;

	/**
	 * @param itemService
	 */
	public ApplicationLogic(ItemService itemService) {
		this.itemService = itemService;
	}
	
	/**
	 * @param emailService the emailService to set
	 */
	public void setEmailService(EmailService emailService) {
		this.emailService = emailService;
	}

	/**
	 * @param templatingService the templatingService to set
	 */
	public void setTemplatingService(TemplatingService templatingService) {
		this.templatingService = templatingService;
	}

	/**
	 * Set the root directory for order files
	 * 
	 * @param ordersDirectory the ordersDirectory to set
	 */
	public void setOrdersDirectory(String ordersDirectory) {
		this.ordersDirectory = new File(ordersDirectory);
		if (!this.ordersDirectory.exists()) {
			LOG.info("Creating root directory for orders " + ordersDirectory);
			this.ordersDirectory.mkdirs();
		}
		if (!this.ordersDirectory.canRead() || !this.ordersDirectory.canWrite()) {
			LOG.info("Cannot read or write orders directory " + ordersDirectory);
			throw new RuntimeException("Cannot read or write orders directory " + ordersDirectory);
		}
	}

	/**
	 * @param entityManager the entityManager to set
	 */
	@PersistenceContext
	public void setEntityManager(EntityManager entityManager) {
		this.entityManager = entityManager;
	}

	@Override
	@Secured({ "ROLE_ORDER", "ROLE_BREEDER" })
	public GenebankOrder createOrder(User user) {
		GenebankOrder order = new GenebankOrder();
		SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
		order.setTitle("Incoming order " + df.format(new Date()));
		order.setOrderDate(new Date());
		order.setState(OrderState.NEW);
		
		if(user!=null){
			order.setTitle("Breeder Incoming order " + df.format(new Date()));
			if(order.getLastName()==null)
				order.setLastName(user.getLastName());
			
			if(order.getFirstName()==null)
				order.setFirstName(user.getFirstName());
			
			if(order.getMail()==null)
				order.setMail(user.getMail());
		}
		
		return order;
	}

	/**
	 * Method called when JMS message received on new Order. Note: No security restrictions on this method!
	 * 
	 * @return
	 */
	@Override
	public GenebankOrder createInternetOrder() {
		return createOrder(null);
	}

	@Override
	@Transactional(readOnly = true)
	public PagedResult<? extends Order> listOrders(OrderState state, int startAt, int maxRequests) {
		PagedResult<GenebankOrder> paged = new PagedResult<GenebankOrder>(startAt, maxRequests);
		if (state == null || state.equals(OrderState.ALL)) {
			paged.setResults(this.entityManager.createQuery("from GenebankOrder o order by o.createdDate desc").setFirstResult(startAt).setMaxResults(maxRequests)
					.getResultList());
			if (paged.getResults().size() > 0)
				paged.setTotalHits(((Long) this.entityManager.createQuery("select count(o) from GenebankOrder o").getSingleResult()).longValue());
		} else {
			paged.setResults(this.entityManager.createQuery("from GenebankOrder o where o.state=:state order by o.createdDate desc").setParameter("state", state)
					.setFirstResult(startAt).setMaxResults(maxRequests).getResultList());
			if (paged.getResults().size() > 0)
				paged.setTotalHits(((Long) this.entityManager.createQuery("select count(o) from GenebankOrder o where o.state=:state")
						.setParameter("state", state).getSingleResult()).longValue());
		}
		return paged;
	}

	/**
	 * @see org.iita.inventory.service.OrderService#getOrder(java.lang.Long)
	 */
	@Override
	@Transactional(readOnly = true)
	public GenebankOrder getOrder(Long id) {
		LOG.debug("Loading order " + id);
		GenebankOrder order = this.entityManager.find(GenebankOrder.class, id);
		Collections.sort(order.getItems(), new NaturalOrderComparator<Object>());
		return order;
	}

	/**
	 * @see org.iita.inventory.service.OrderService#updateOrder(org.iita.inventory.model.Order)
	 */
	@Override
	@Transactional
	@Secured({ "ROLE_ORDER", "ROLE_BREEDER" })
	public void updateOrder(Order order) {
		if (order.getId() == null) {
			LOG.info("Persisting new order");
			this.entityManager.persist(order);
		} else {
			LOG.info("Updating order " + order.getTitle() + " id=" + order.getId());
			this.entityManager.merge(order);
		}
		
		if (this.emailService != null && this.templatingService != null && order.getItems().size()>0) {
			// need to send email
			Map<String, Object> data = new Hashtable<String, Object>();
			data.put("order", order);
			
			String body;
			try {
				body = this.templatingService.fillTemplate("order-alert", data);
				this.emailService.sendEmail("software@iita.org", "iita.genebank@iita.org", "Germplasm breeder order", body);
			} catch (TemplatingException e) {
				LOG.error(e, e);
				throw new RuntimeException("An error occured while trying to save your request. Please try again later.");
			} catch (EmailException e) {
				LOG.error(e, e);
				throw new RuntimeException("An error occured while trying to save your request. Please try again later.");
			}
		}
	}

	/**
	 * Method called when JMS order message is received. Note: No security restrictions!
	 * 
	 * @param order
	 */
	@Override
	@Transactional
	public void updateInternetOrder(Order order) {
		updateOrder(order);
	}

	/**
	 * Split up accessionNames and search DB for items, add items to order list if they don't exist already
	 * 
	 * @throws OrderException
	 * @see org.iita.inventory.service.OrderService#addItemsByName(org.iita.inventory.model.Order, java.lang.String)
	 */
	@Override
	@Transactional
	@Secured({ "ROLE_ORDER", "ROLE_BREEDER" })
	public void addItemsByName(Order order, String accessionNames) throws OrderException {
		List<Item> items = this.itemService.findItemsByName(accessionNames);
		order.addItems(items);
		this.entityManager.merge(order);
	}

	/**
	 * @throws IOException
	 * @see org.iita.inventory.service.OrderService#getOrderFile(org.iita.inventory.model.GenebankOrder, java.lang.String, java.lang.String)
	 */
	@Override
	public ServerFile getOrderFile(GenebankOrder order, String subDirectory, String fileName) throws IOException {
		return ServerFile.getServerFile(getRootDirectory(order), subDirectory, fileName);
	}

	/**
	 * @throws IOException
	 * @see org.iita.inventory.service.OrderService#getOrderFiles(org.iita.inventory.model.GenebankOrder, java.lang.String)
	 */
	@Override
	public List<ServerFile> getOrderFiles(GenebankOrder order, String subDirectory) throws IOException {
		return ServerFile.getServerFiles(getRootDirectory(order), subDirectory);
	}

	/**
	 * @see org.iita.inventory.service.OrderService#getRootDirectory(org.iita.inventory.model.GenebankOrder)
	 */
	@Override
	public File getRootDirectory(GenebankOrder order) {
		if (order == null) {
			LOG.info("Cannot get root directory for null order");
			return null;
		}

		LOG.debug("Root directory for all order files is: " + this.ordersDirectory);
		File orderDirectory = new File(this.ordersDirectory, "" + order.getId());
		if (!orderDirectory.exists()) {
			LOG.info("Creating directory for order " + order.getId());
			orderDirectory.mkdirs();
		}
		return orderDirectory;
	}

	/**
	 * @return
	 * @see org.iita.inventory.service.OrderService#startProcessing(org.iita.inventory.model.GenebankOrder)
	 */
	@Transactional
	@Override
	@Secured({ "ROLE_ORDER" })
	public QuantityUpdateBulk startProcessing(GenebankOrder order) {
		int lotsToDistribute = 0;
		// check if any lots are assigned
		for (GenebankOrderItem item : order.getItems()) {
			if (item.getStatus() == OrderItemStatus.DISPATCHED)
				continue;

			if (item.getLot() != null) {
				if (item.getQuantity() > item.getLot().getQuantity())
					throw new RuntimeException("Insufficient stock available for item " + item.getItem().getName() + "");
				lotsToDistribute++;
			}
		}

		if (lotsToDistribute == 0)
			throw new RuntimeException("There are no lots selected to distribute.");

		QuantityUpdateBulk update = new QuantityUpdateBulk();
		update.setAffectingInventory(true);
		update.setDate(new Date());
		update.setTitle("Germplasm request " + order.getLastName() + ", " + order.getOrganization());
		update.setDescription("Germplasm request #" + order.getId() + ":\n" + order.getDescription());
		update.setOrder(order);
		update.setStatus(0);
		update.setTransactionType(Type.OUT);
		update.setTransactionSubtype("DISTRIBUTION");
		this.entityManager.persist(update);
		order.setState(OrderState.APPROVED);
		this.entityManager.merge(order);

		return update;
	}

	/**
	 * @see org.iita.inventory.service.OrderService#rejectOrder(org.iita.inventory.model.GenebankOrder)
	 */
	@Override
	@Transactional
	@Secured({ "ROLE_ORDER" })
	public void rejectOrder(GenebankOrder order) {
		this.entityManager.refresh(order);
		order.setState(OrderState.REJECTED);
		this.entityManager.persist(order);
	}

	/**
	 * @throws OrderException
	 * @see org.iita.inventory.service.OrderService#split(org.iita.inventory.model.GenebankOrder)
	 */
	@Override
	@Transactional(rollbackFor = Throwable.class)
	public GenebankOrder split(GenebankOrder order) throws OrderException {
		int dispatchedItems = 0;
		for (GenebankOrderItem item : order.getItems()) {
			if (item.getStatus() == OrderItemStatus.DISPATCHED)
				dispatchedItems++;
		}
		if (dispatchedItems == order.getItems().size())
			throw new OrderException("All items in this order are marked as DISPATCHED. Nothing to split.");

		if (dispatchedItems == 0)
			throw new OrderException("No items in this order are marked as DISPATCHED. Refusing to split.");

		GenebankOrder splitOrder = new GenebankOrder(order);

		// update state of original order
		order.setState(OrderState.CLOSED);
		this.entityManager.merge(order);

		// save split order
		this.entityManager.persist(splitOrder);

		// move items
		for (GenebankOrderItem item : order.getItems()) {
			if (item.getStatus() != OrderItemStatus.DISPATCHED)
				item.setOrder(splitOrder);
			this.entityManager.merge(item);
		}

		// copy tags
		for (OrderTag tag : order.getTags()) {
			OrderTag newTag = new OrderTag(tag);
			newTag.setEntity(splitOrder);
			this.entityManager.persist(newTag);
		}

		return splitOrder;
	}

	/**
	 * @see org.iita.crm.service.TagService#list(java.lang.String, int, int)
	 */
	@Override
	@Transactional(readOnly = true)
	public PagedResult<? extends Tag> list(String tag, int startAt, int maxResults) {
		LOG.info("Listing everything with tag: " + tag);
		PagedResult<? extends Tag> paged = new PagedResult<Tag>(startAt, maxResults);
		paged.setResults(this.entityManager.createQuery("from Tag t where t.tag=:tag order by t.tag").setParameter("tag", tag).setFirstResult(startAt)
				.setMaxResults(maxResults).getResultList());
		if (paged.getResults().size() > 0)
			paged.setTotalHits(((Long) this.entityManager.createQuery("select count(t) from Tag t where t.tag=:tag").setParameter("tag", tag).getSingleResult())
					.longValue());
		return paged;
	}

	/**
	 * @see org.iita.crm.service.TagService#getCloud()
	 */
	@SuppressWarnings("unchecked")
	@Override
	@Transactional(readOnly = true)
	public TagCloud getCloud(int size) {
		List<Object[]> usedTags = this.entityManager.createQuery("select t.tag, count(t) from Tag t group by t.tag order by count(t) desc").setMaxResults(size)
				.getResultList();
		TagCloud cloud = new TagCloud();
		for (Object[] o : usedTags) {
			cloud.add((String) o[0], (Long) o[1]);
		}

		return cloud.prepare();
	}

	/**
	 * @see org.iita.crm.service.TagService#update(org.iita.crm.model.Tag)
	 */
	@Override
	@Transactional
	// @Secured("ROLE_CRM")
	public void update(Tag tag) {
		if (tag.getId() == null)
			this.entityManager.persist(tag);
		else
			this.entityManager.merge(tag);
	}

	/**
	 * @see org.iita.crm.service.TagService#removeTag(org.iita.crm.model.Tag)
	 */
	@Override
	@Transactional
	// @Secured("ROLE_CRM")
	public void remove(Tag tag) {
		this.entityManager.remove(tag);
	}

	/**
	 * @see org.iita.crm.service.TagService#getTagCategories(java.lang.Class)
	 */
	@SuppressWarnings("unchecked")
	@Override
	@Transactional(readOnly = true)
	public <T> List<String> getTagCategories(Class<T> clazz) {
		List<String> tags = this.entityManager.createQuery("select distinct(t.tag) from " + clazz.getName() + " t where t.tag like '%:%'").getResultList();
		List<String> categories = new ArrayList<String>();
		for (String tag : tags) {
			String category = tag.substring(0, tag.lastIndexOf(':'));
			if (!categories.contains(category))
				categories.add(category);
		}
		Collections.sort(categories);
		return categories;
	}

	/**
	 * @see org.iita.crm.service.TagService#getTagsForCategory(java.lang.Class, java.lang.String)
	 */
	@SuppressWarnings("unchecked")
	@Override
	@Transactional(readOnly = true)
	public <T> List<String> getTagsForCategory(Class<T> clazz, String category) {
		if (category == null || category.trim().length() == 0)
			return this.entityManager.createQuery("select distinct(t.tag) from " + clazz.getName() + " t where t.tag not like :category order by t.tag")
					.setParameter("category", "%:%").getResultList();
		else
			return this.entityManager.createQuery("select distinct(t.tag) from " + clazz.getName() + " t where t.tag like :category order by t.tag")
					.setParameter("category", category + ":%").getResultList();
	}

	@SuppressWarnings("unchecked")
	@Override
	@Transactional(readOnly = true)
	public List<String> getTagsInCategory(Taggable<?> entity, String category) {
		return this.entityManager.createQuery("select t.tag from " + entity.getClass().getName() + " e join e.tags t where e=:entity and t.tag like :category")
				.setParameter("entity", entity).setParameter("category", String.format("%1$s:%%", category)).getResultList();
	}

	//@Secured( { "ROLE_CGO", "ROLE_CRM" })
	@Override
	@Transactional
	public <T extends Taggable<T>> void bulkUpdateTags(T taggableEntity, java.util.List<String> usedTags, java.util.Map<String, Double> tagValues) {
		List<? extends EntityTag<T>> existingTags = taggableEntity.getTags();
		for (String usedTag : usedTags) {
			Double tagValue = tagValues.get(usedTag);
			LOG.debug("Using tag: " + usedTag + " with value=" + tagValue);

			boolean found = false;
			// update existing tag
			for (EntityTag<T> existingTag : existingTags) {
				if (existingTag.getTag().equals(usedTag)) {
					found = true;
					LOG.debug("Found existing tag: " + existingTag);
					existingTag.setPercentage(tagValue);
					this.entityManager.merge(existingTag);
					break;
				}
			}
			// create new tag
			if (!found) {
				LOG.info("Creating new tag: " + usedTag + " with value " + tagValue);
				EntityTag<T> newTag;
				try {
					newTag = taggableEntity.getTagClass().newInstance();
				} catch (Exception e) {
					LOG.error(e, e);
					throw new RuntimeException(e.getMessage(), e);
				}
				newTag.setTag(usedTag);
				newTag.setEntity(taggableEntity);
				newTag.setPercentage(tagValue);
				this.entityManager.persist(newTag);
			}
		}

		// remove unused tags
		for (int i = existingTags.size() - 1; i >= 0; i--) {
			if (!usedTags.contains(existingTags.get(i).getTag())) {
				LOG.info("Removing unused tag: " + existingTags.get(i));
				this.entityManager.remove(existingTags.remove(i));
			}
		}
	}

	/**
	 * @see org.iita.crm.service.CoreCRMServiceImpl#autocompleteTag(java.lang.String, int)
	 */
	@SuppressWarnings("unchecked")
	@Override
	@Transactional(readOnly = true)
	public List<String> autocompleteTag(String text, int i) {
		if (text == null || text.length() == 0)
			return null;
		text = text.trim();
		LOG.debug("Listing tags matching: " + text);
		List list = this.entityManager.createQuery("select distinct t.tag from Tag t where t.tag like :text order by t.tag").setMaxResults(i)
				.setParameter("text", text + "%").getResultList();
		LOG.debug("Got " + list.size() + " matching tags");
		return list;
	}

	/**
	 * @see org.iita.crm.service.TagService#loadTag(java.lang.Long)
	 */
	@Override
	@Transactional(readOnly = true)
	public Tag loadTag(Class<? extends Tag> clazz, Long id) {
		return this.entityManager.find(clazz, id);
	}
	
	@Override
	public PagedResult<? extends Order> listOrders(OrderState state, int startAt, int maxRequests, User requester) {
		//LOG.info("Requester ID: " + requester.getId());
		//LOG.info("Order State: " + state);
		PagedResult<GenebankOrder> paged = new PagedResult<GenebankOrder>(startAt, maxRequests);
		if (state == null || state.equals(OrderState.ALL)) {
			paged.setResults(this.entityManager.createQuery("from GenebankOrder o where requester=:requester order by o.id desc").setParameter("requester", requester).setFirstResult(startAt).setMaxResults(maxRequests)
					.getResultList());
			if (paged.getResults().size() > 0)
				paged.setTotalHits(((Long) this.entityManager.createQuery("select count(o) from GenebankOrder o where requester=:requester").setParameter("requester", requester).getSingleResult()).longValue());
		} else {
			paged.setResults(this.entityManager.createQuery("from GenebankOrder o where o.state=:state and requester=:requester order by o.id desc").setParameter("state", state).setParameter("requester", requester)
					.setFirstResult(startAt).setMaxResults(maxRequests).getResultList());
			if (paged.getResults().size() > 0)
				paged.setTotalHits(((Long) this.entityManager.createQuery("select count(o) from GenebankOrder o where o.state=:state and requester=:requester")
						.setParameter("state", state).setParameter("requester", requester).getSingleResult()).longValue());
		}
		return paged;
	}
}
