/**
 * 
 */
package org.iita.inventory.action;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.struts2.util.DateFormatter;
import org.iita.inventory.model.InVitroLot;
import org.iita.inventory.model.Lot;
import org.iita.inventory.model.QuantityUpdate;
import org.iita.inventory.model.QuantityUpdateBulk;
import org.iita.inventory.model.Transaction2;
import org.iita.inventory.service.LotService;
import org.iita.inventory.service.QuantityUpdateService;
import org.iita.inventory.service.SelectionService;

import com.opensymphony.xwork2.Action;
import com.opensymphony.xwork2.Preparable;

/**
 * @author mobreza
 * 
 */
public class SubcultureResults extends BaseAction implements Preparable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	protected QuantityUpdateService service;
	protected LotService lotService;
	protected SelectionService selectionService;

	/** QuantityUpdateBulk ID */
	protected Long id;
	/** QuantityUpdateBulk object */
	private QuantityUpdateBulk description;

	public SubcultureResults(QuantityUpdateService service, LotService lotService, SelectionService selectionService) {
		this.service = service;
		this.lotService = lotService;
		this.selectionService = selectionService;
	}

	/**
	 * @param description the description to set
	 */
	public void setDescription(QuantityUpdateBulk description) {
		this.description = description;
	}

	/**
	 * @return the description
	 */
	public QuantityUpdateBulk getDescription() {
		return this.description;
	}

	/**
	 * @return the id
	 */
	public Long getId() {
		return this.id;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(Long id) {
		this.id = id;
	}

	public String input() {
		return Action.INPUT;
	}

	public String save() {
		System.out.println("Title: " + this.description.getTitle());
		System.out.println("Desc: " + this.description.getDescription());
		// iterate over the results and remove any items not in the list
		List<QuantityUpdate> toSave = new ArrayList<QuantityUpdate>();
		for (QuantityUpdate qu : this.description.getLots()) {
			System.out.println("\t" + qu.getQuantity() + " " + qu.getScale() + ": " + qu.getLot().getItem().getName());
			if (qu.getQuantity() != null && qu.getQuantity() > 0.0) {
				toSave.add(qu);
			}
		}

		System.out.println("Would add " + toSave.size() + " records");

		List<Lot> newLots = new ArrayList<Lot>();
		for (QuantityUpdate qu : toSave) {
			System.out.println("\t" + qu.getQuantity() + " " + qu.getScale() + ": " + qu.getLot().getItem().getName());
			// need to insert new InVitroLots
			Lot lot = qu.getLot();
			lot.setQuantity(0.0d);
			newLots.add(lot);
		}

		lotService.store(newLots);
		this.description.setLots(toSave);
		this.service.store(this.description);
		this.service.commit(this.description);

		// clear current selection
		if(this.selectionService.getSelectedList()!=null)
			this.selectionService.getSelectedList().clearSelection();
		
		// add all new lots to selection list!
		this.selectionService.getSelectedList().addSelection(newLots);

		addActionMessage("The new lots have been added to your selection list. Please print labels now and then migrate the lots to appropriate location.");
		// done
		return Action.SUCCESS;
	}

	/**
	 * Generate an in-memory QuantityUpdateBulk for editing.
	 * 
	 * @return
	 */
	private QuantityUpdateBulk generateSubculturedList() {
		List<QuantityUpdate> sourceLots = this.description.getLots();

		QuantityUpdateBulk copy = new QuantityUpdateBulk();
		copy.setDate(new Date());
		DateFormatter df = new DateFormatter();
		df.setFormat(getText("date.format"));
		df.setDate(new Date());
		copy.setTitle("Subcultured " + df.getFormattedDate());
		copy.setTransactionType(Transaction2.Type.IN);
		copy.setTransactionSubtype("SUBCULTURED");

		// generate update list of lots
		List<QuantityUpdate> subculturedLots = new ArrayList<QuantityUpdate>();
		for (QuantityUpdate source : sourceLots) {
			System.err.println("Copying " + source.getLot().getItem().getName());
			QuantityUpdate result = new QuantityUpdate();
			InVitroLot resultLot = new InVitroLot();
			// copy lot properties
			resultLot.copyFrom(source.getLot());
			resultLot.setLocation(null);
			resultLot.setRegenerationDate(copy.getDate());
			result.setDescription(copy);
			result.setLot(resultLot);
			result.setScale(source.getScale());
			subculturedLots.add(result);
		}
		copy.setLots(subculturedLots);
		return copy;
	}

	@Override
	public void prepare() {
		super.prepare();
		if (this.id != null) {
			// Load source list
			System.err.println("SubcultureResults.prepare() load ID=" + this.id);
			this.description = this.service.load(this.id);
			// Generate list
			this.description = generateSubculturedList();
		} else {
			System.err.println("QuantityUpdateEditor.prepare() no ID provided");
			addActionError("Source ID not provided!");
		}
	}
}
