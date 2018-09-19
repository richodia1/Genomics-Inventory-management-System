/**
 * inventory.Struts Nov 3, 2009
 */
package org.iita.inventory.action;

import java.util.List;

import org.apache.struts2.ServletActionContext;
import org.iita.inventory.label.ScaleException;
import org.iita.inventory.printing.BalanceScaleInfo;
import org.iita.inventory.service.BalanceScaleService;

import com.opensymphony.xwork2.Action;

/**
 * @author KOraegbunam
 * 
 */
@SuppressWarnings("serial")
public class ScaleSelectAction extends BaseAction {
	private BalanceScaleService scaleService;
	private List<BalanceScaleInfo> scales;
	private Integer myScale;

	/**
	 * @param printerService 
	 * 
	 */
	public ScaleSelectAction(BalanceScaleService scaleService) {
		this.scaleService=scaleService;
	}
	
	/**
	 * @return the scales
	 */
	public List<BalanceScaleInfo> getScales() {
		return this.scales;
	}
	
	/**
	 * @param myScale the myScale to set
	 */
	public void setMyScale(Integer myScale) {
		this.myScale = myScale;
	}
	
	/**
	 * @see org.iita.inventory.action.BaseAction#prepare()
	 */
	@Override
	public void prepare() {
		this.scales = this.scaleService.getScales();
	}
	
	/**
	 * Default action renders list of available printers
	 * 
	 * @see org.iita.struts.BaseAction#execute()
	 */
	@Override
	public String execute() {
		return Action.INPUT;
	}
	
	public String select() {
		System.out.println("Trying to select scale " + myScale + " for this session.");
		for (BalanceScaleInfo scale : this.scales) {
			System.out.println("Checking scale " + scale.getHost() + " id=" + scale.getId());
			if (scale.getId().equals(this.myScale))
			{
				System.out.println("Found selected scale " + scale.getHost() + ". Selecting now.");
				try {
					this.scaleService.selectBalanceScale(ServletActionContext.getRequest().getSession().getId(), scale);
					return Action.SUCCESS;
				} catch (ScaleException e) {
					System.out.println(e);
					addActionError(e.getMessage());
					return Action.INPUT;
				}
			}
		}
		addActionError("Could not locate selected scale");
		return Action.ERROR;
	}
}
