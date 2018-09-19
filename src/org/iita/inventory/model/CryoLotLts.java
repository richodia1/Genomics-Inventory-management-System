package org.iita.inventory.model;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Transient;

import org.hibernate.search.annotations.Indexed;
@Entity
@Indexed
public class CryoLotLts extends Lot {

	/**
	 * 
	 */
	private static final long serialVersionUID = -943111871365045878L;

	/** Number of meristem LTS =120(12*10vials)*/
	private static final String[] CRYO_UOMS = new String[] { "vials" };
	/** Introduction to genebank */
	private Date introductionDate;
	private static final String[] healthstatus = new String[] { "EF/VF" };
	/** cryo date */
	private Date cryoDate;
	/** general remarks */
	private String remarks;
	public Date getIntroductionDate() {
		return introductionDate;
	}

	public void setIntroductionDate(Date introductionDate) {
		this.introductionDate = introductionDate;
	}

	public Date getCryoDate() {
		return cryoDate;
	}

	public void setCryoDate(Date cryoDate) {
		this.cryoDate = cryoDate;
	}

	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

	public static String[] getCryoUoms() {
		return CRYO_UOMS;
	}

	public static String[] getHealthstatus() {
		return healthstatus;
	}

	private Double numbermistermlts=null;
	private Double regrowthvalidationratelts=null;
	
	public Double getNumbermistermlts() {
		return numbermistermlts;
	}
	public void setNumbermistermlts(Double numbermistermlts) {
		this.numbermistermlts = numbermistermlts;
	}
	/** Regrowth validation rate*/
	
	
	public Double getRegrowthvalidationratelts() {
		return regrowthvalidationratelts;
	}
	public void setRegrowthvalidationratelts(Double regrowthvalidationratelts) {
		this.regrowthvalidationratelts = regrowthvalidationratelts;
	}
	
	@Override
	public void copyFrom(Lot lot) {
		super.copyFrom(lot);
		if (lot instanceof CryoLotLts) {
			CryoLotLts other = (CryoLotLts) lot;
			this.cryoDate=other.cryoDate;
			this.introductionDate=other.introductionDate;
		}
	}
	
	@Override
	@Transient
	public String[] getUoms() {
		return CRYO_UOMS;
	}
}