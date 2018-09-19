package org.iita.inventory.test;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.iita.inventory.remote.ExistingLocationMigration;
import org.iita.inventory.remote.InventoryService;
import org.iita.inventory.remote.LotFieldVariable;
import org.iita.inventory.remote.LotQuantity;
import org.iita.inventory.remote.NewLocationMigration;

import com.opensymphony.xwork2.Action;

public class TestRemoteInventory {
	InventoryService inventoryService;
	LotFieldVariable fv = new LotFieldVariable();
	LotQuantity qu = new LotQuantity();
	NewLocationMigration lm = new NewLocationMigration();
	//ExistingLocationMigration lm = new ExistingLocationMigration();
	List<LotFieldVariable> fieldVars = new ArrayList<LotFieldVariable>();
	List<LotQuantity> subTypes = new ArrayList<LotQuantity>();
	List<NewLocationMigration> lms = new ArrayList<NewLocationMigration>();
	//List<ExistingLocationMigration> lms = new ArrayList<ExistingLocationMigration>();
	
	public TestRemoteInventory(InventoryService inventoryService){
		this.inventoryService = inventoryService;
	}

	public void prepared() {

		//String qty = "10";
		//String var = "Sprouted";
		//String lastUpdateBy = "KOraegbunam";
		//Long lotId = 213503L;
		//Date date = new Date();
		//Date lastUpdated = new Date();
		
		//fv.setVar(var);
		//fv.setQty(qty);
		//fv.setLotId(lotId);
		//fv.setDate(date);
		//fv.setLastUpdated(lastUpdated);
		//fv.setLastUpdatedBy(lastUpdateBy);
		//fieldVars.add(fv);
		//System.out.println("FieldVars: " + fieldVars.size());
		//System.out.println("Service: " + inventoryService);
		//inventoryService.updateLotFieldVariables(fieldVars);
		//for(int i=0; i<subTypes.size();i++){
		//	System.out.println("SubType LotID: " + subTypes.get(i).getLotId());
		//}
	}
	
	/**
	 * Execute
	 */
	public String execute() {
		String qty = "10";
		String var = "Sprouted";
		String lastUpdateBy = "KOraegbunam";
		Long lotId = 213944L;
		Date date = new Date();
		Date lastUpdated = new Date();
		
		fv.setVar(var);
		fv.setQty(qty);
		fv.setLotId(lotId);
		fv.setDate(date);
		fv.setLastUpdated(lastUpdated);
		fv.setLastUpdatedBy(lastUpdateBy);
		fieldVars.add(fv);
		
		fv = new LotFieldVariable();
		qty = "5";
		var = "Flower";
		lastUpdateBy = "KOraegbunam";
		lotId = 213946L;
		date = new Date();
		lastUpdated = new Date();
		
		fv.setVar(var);
		fv.setQty(qty);
		fv.setLotId(lotId);
		fv.setDate(date);
		fv.setLastUpdated(lastUpdated);
		fv.setLastUpdatedBy(lastUpdateBy);
		fieldVars.add(fv);
		
		fv = new LotFieldVariable();
		qty = "4";
		var = "Bulbils";
		lastUpdateBy = "KOraegbunam";
		lotId = 213947L;
		date = new Date();
		lastUpdated = new Date();
		
		fv.setVar(var);
		fv.setQty(qty);
		fv.setLotId(lotId);
		fv.setDate(date);
		fv.setLastUpdated(lastUpdated);
		fv.setLastUpdatedBy(lastUpdateBy);
		fieldVars.add(fv);
		
		System.out.println("LOT ID: " + lotId);
		System.out.println("Var: " + var);
		System.out.println("VALUE: " + qty);
		System.out.println("FieldVars: " + fieldVars.size());
		System.out.println("Service: " + inventoryService);
		inventoryService.updateLotFieldVariables(fieldVars);
		for(int i=0; i<subTypes.size();i++){
			System.out.println("SubType LotID: " + subTypes.get(i).getLotId());
		}
		
		return Action.SUCCESS;
		
		//qu.setId(1);
		//qu.setLotId(212768);
		//qu.setQuantity(3.0);
		//qu.setSubtype("Contaminations");
		//qu.setUpdatedBy("ws");
		//subTypes.add(qu);
		
		//qu = new LotSubtypeQuantity();
		//qu.setId(2);
		//qu.setLotId(214741);
		//qu.setQuantity(1.0);
		//qu.setSubtype("Contaminations");
		//qu.setUpdatedBy("ws");
		//subTypes.add(qu);
		
		//System.out.println("Size of SubTypes: " + subTypes.size());
		//this.inventoryService.updateLotSubtypes(subTypes);
		//return Action.SUCCESS;
		
		/*lm.setFromLocation(32667L);
		lm.setLastUpdated(new Date());
		lm.setLastUpdatedBy("ws");
		lm.setLotId(165361L);
		lm.setParendId(33465L);
		lm.setToLocation("wale");
		lms.add(lm);
		
		lm = new NewLocationMigration(); 
		lm.setFromLocation(32667L);
		lm.setLastUpdated(new Date());
		lm.setLastUpdatedBy("ws");
		lm.setLotId(165361L);
		lm.setParendId(33465L);
		lm.setToLocation("wale");
		lms.add(lm);
		
		System.out.println("Size of Lot Migration Array: " + lms.size());
		this.inventoryService.migrateLotToNewLocation(lms);
		return Action.SUCCESS;*/
	}
}
