/**
 * inventory.Struts Nov 4, 2009
 */
package org.iita.inventory.action.order;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

import org.iita.inventory.model.GenebankOrder;
import org.iita.inventory.service.OrderService;
import org.iita.struts.FilesAction;
import org.iita.struts.webfile.ServerFile;

/**
 * @author mobreza
 *
 */
@SuppressWarnings("serial")
public class OrderFilesAction extends FilesAction {
	private OrderService orderService;
	private GenebankOrder order = null;
	private Long id;
	
	/**
	 * @param orderService 
	 * 
	 */
	public OrderFilesAction(OrderService orderService) {
		this.orderService = orderService;
	}
	
	/**
	 * @see org.iita.struts.BaseAction#prepare()
	 */
	@Override
	public void prepare() {
		if (this.id!=null)
			this.order = this.orderService.getOrder(this.id);
	}
	
	/**
	 * @see org.iita.struts.FilesAction#getBrowserTitle()
	 */
	@Override
	public String getBrowserTitle() {
		return this.order==null ? "" : this.order.getTitle();
	}

	/**
	 * @see org.iita.struts.FilesAction#getFiles(java.lang.String)
	 */
	@Override
	public List<ServerFile> getFiles(String subDirectory) {
		try {
			return this.orderService.getOrderFiles(this.order, subDirectory);
		} catch (IOException e) {
			addActionError(e.getMessage());
		}
		return null;
	}

	/**
	 * @see org.iita.struts.FilesAction#getId()
	 */
	@Override
	public Long getId() {
		return this.order==null ? null : this.order.getId();
	}

	/**
	 * @return the order
	 */
	public GenebankOrder getOrder() {
		return this.order;
	}
	
	/**
	 * @see org.iita.struts.FilesAction#getRootDirectory()
	 */
	@Override
	protected File getRootDirectory() throws FileNotFoundException {
		return this.orderService.getRootDirectory(this.order);
	}

	/**
	 * @see org.iita.struts.FilesAction#setId(java.lang.Long)
	 */
	@Override
	public void setId(Long id) {
		this.id=id;
	}

	/**
	 * @see org.iita.struts.FileDownloadAction#getServerFile(java.lang.String, java.lang.String)
	 */
	@Override
	public ServerFile getServerFile(String subDirectory, String fileName) throws IOException {
		return this.orderService.getOrderFile(this.order, subDirectory, fileName);
	}

	/**
	 * @see org.iita.struts.FileDownloadAction#remove()
	 */
	@Override
	public String remove() throws IOException {
		// TODO Remove Order files functionality missing
		throw new RuntimeException("Not implemented");
	}

}
