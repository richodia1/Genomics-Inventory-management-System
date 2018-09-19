package org.iita.inventory.model;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Transient;

import org.hibernate.search.annotations.Indexed;

@Entity
@Indexed
public class CryoLotTemp extends Lot{

	
	/**
	 * 
	 */
	private static final long serialVersionUID = -8063487540548148352L;
	private static final String[] CRYO_UOMS = new String[] { "vials" };
	/** Introduction to genebank */
	private Date introductionDate;
	private static final String[] healthstatus = new String[] { "EF/VF" };
	/** cryo date */
	private Date cryoDate;
	/** general remarks */
	private String remarks;
	private Double numbermeristermtemp=null;
	/** viability culture date */
	private Date viabcultureDate;
	/** Quantity tested for viability */
	private Double qtytestedforviability=null;
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

	
	public Double getNumbermeristermtemp() {
		return numbermeristermtemp;
	}

	public void setNumbermeristermtemp(Double numbermeristermtemp) {
		this.numbermeristermtemp = numbermeristermtemp;
	}

	private Date observationdate;
	/** regeneration rate between 1 to 100% */
	private Double regrowthrate=null;

	public Date getObservationdate() {
		return observationdate;
	}

	public void setObservationdate(Date observationdate) {
		this.observationdate = observationdate;
	}

	public Double getRegrowthrate() {
		return regrowthrate;
	}

	public void setRegrowthrate(Double regrowthrate) {
		this.regrowthrate = regrowthrate;
	}

	public Date getViabcultureDate() {
		return viabcultureDate;
	}

	public void setViabcultureDate(Date viabcultureDate) {
		this.viabcultureDate = viabcultureDate;
	}

	public Double getQtytestedforviability() {
		return qtytestedforviability;
	}

	public void setQtytestedforviability(Double qtytestedforviability) {
		this.qtytestedforviability = qtytestedforviability;
	}
	@Override
	public void copyFrom(Lot lot) {
		super.copyFrom(lot);
		if (lot instanceof CryoLotTemp) {
			CryoLotTemp other = (CryoLotTemp) lot;
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
