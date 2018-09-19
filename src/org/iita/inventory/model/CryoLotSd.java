package org.iita.inventory.model;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Transient;

import org.hibernate.search.annotations.Indexed;
@Entity
@Indexed
public class CryoLotSd extends Lot {
	
	private static final long serialVersionUID = -3443865815551275253L;
	/**
	 * 
	 */
	private static final String[] CRYO_UOMS = new String[] { "vials" };
	/** Introduction to genebank */
	private Date introductionDate;
	private static final String[] healthstatus = new String[] { "EF/VF" };
	/** cryo date */
	private Date cryoDate;
	/** general remarks */
	private String remarks;
	private Double numbmrtmsd=null;
	private Double regrowthvalidationrate=null;
	private Date sdverificationdate;
	private Double sdverificationqty;
	private Double sdverificationrate;
	private String verificationremarks;
	
	public Date getCryoDate() {
		return cryoDate;
	}
	public Date getIntroductionDate() {
		return introductionDate;
	}
	public void setIntroductionDate(Date introductionDate) {
		this.introductionDate = introductionDate;
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
	
	public Double getNumbmrtmsd() {
		return numbmrtmsd;
	}
	public void setNumbmrtmsd(Double numbmrtmsd) {
		this.numbmrtmsd = numbmrtmsd;
	}
	
	
	public Double getRegrowthvalidationrate() {
		return regrowthvalidationrate;
	}
	public void setRegrowthvalidationrate(Double regrowthvalidationrate) {
		this.regrowthvalidationrate = regrowthvalidationrate;
	}
	public Date getSdverificationdate() {
		return sdverificationdate;
	}
	public void setSdverificationdate(Date sdverificationdate) {
		this.sdverificationdate = sdverificationdate;
	}
	public Double getSdverificationqty() {
		return sdverificationqty;
	}
	public void setSdverificationqty(Double sdverificationqty) {
		this.sdverificationqty = sdverificationqty;
	}
	public Double getSdverificationrate() {
		return sdverificationrate;
	}
	public void setSdverificationrate(Double sdverificationrate) {
		this.sdverificationrate = sdverificationrate;
	}
	public String getVerificationremarks() {
		return verificationremarks;
	}
	public void setVerificationremarks(String verificationremarks) {
		this.verificationremarks = verificationremarks;
	}
	@Override
	public void copyFrom(Lot lot) {
		super.copyFrom(lot);
		if (lot instanceof CryoLotLts) {
			CryoLotSd other = (CryoLotSd) lot;
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