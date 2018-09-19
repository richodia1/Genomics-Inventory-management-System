/**
 * 
 */
package org.iita.inventory.action;

import java.util.Date;
import java.util.List;

import org.iita.inventory.model.InVitroLot;
import org.iita.inventory.model.Lot;
import org.iita.inventory.model.QuantityUpdate;
import org.iita.inventory.model.QuantityUpdateBulk;
import org.iita.inventory.model.Transaction2.Type;
import org.iita.inventory.service.BarcodingService;
import org.iita.inventory.service.LotService;
import org.iita.inventory.service.QuantityUpdateService;
import org.iita.inventory.service.SelectionService;
import org.iita.service.ExportService;

import com.opensymphony.xwork2.Action;

/**
 * @author mobreza
 * 
 */
@SuppressWarnings("serial")
public class GermplasmHealthEditor extends QuantityUpdateEditor {

	/**
	 * @param service
	 * @param lotService
	 * @param exportService 
	 */
	public GermplasmHealthEditor(QuantityUpdateService service, LotService lotService, SelectionService selectionService, BarcodingService barcodingService, ExportService exportService) {
		super(service, lotService, selectionService, barcodingService, exportService);
	}

	public String addNecro() {
		this.description = new QuantityUpdateBulk();
		this.description.setDate(new Date());
		this.description.setTitle("Necrosis check");
		this.description.setTransactionType(Type.OUT);
		this.description.setTransactionSubtype("NECROSIS");
		return Action.INPUT;
	}

	public String addConta() {
		this.description = new QuantityUpdateBulk();
		this.description.setDate(new Date());
		this.description.setTitle("Contamination check");
		this.description.setTransactionType(Type.OUT);
		this.description.setTransactionSubtype("CONTAMINATION");
		return Action.INPUT;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.iita.inventory.action.QuantityUpdateEditor#scanLot()
	 */
	@Override
	public String scanLot() {
		this.itemid = null;
		if (this.barCode == null)
			return "input";

		Lot lot = this.lotService.loadByBarcode(this.barCode);

		if (lot == null) {
			this.lot = null;
			this.barCode = null;
			addActionMessage("No lot with such barcode.");
		} else if (lot instanceof InVitroLot) {
			List<QuantityUpdate> updatesForItem = this.service.findItem(this.description, lot);
			if (updatesForItem.size() == 0) {
				this.lot = new QuantityUpdate();
				this.lot.setDescription(this.description);
				this.lot.setLot(lot);
				this.lot.setScale(this.lot.getLot().getScale());
				this.lot.setQuantity(1.0d);
			} else {
				this.lot = updatesForItem.get(0);
				this.lot.setQuantity(this.lot.getQuantity() + 1);
			}
			this.service.store(this.lot);
			this.lot = null;
			this.addActionMessage("Item " + lot.getItem().getName() + " added to list.");
		} else {
			this.addActionError("Item " + lot.getItem().getName() + " is not an In-Vitro item! Not adding to germplasm health check list!");
		}
		return "input";
	}
}
