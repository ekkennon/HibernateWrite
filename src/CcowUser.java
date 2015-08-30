/**
 * 
 */
package com.bjc.ekk.ccowhib;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

/**
 * @author ekk9418
 *
 */
@Entity
@Table(name="Gen_Ccow")
public class CcowUser {
	private String guid;
	private String uid;
	private String fn;
	private String ln;
	private String mi;
	private String title;
	private String appEntryData;
	private Date lastImported;
	
	/**
	 * @return the guid
	 */
	@Id
	@GenericGenerator(name = "uuid", strategy = "uuid2", parameters = {})
    @GeneratedValue(generator = "uuid")
	@Column(columnDefinition="uniqueidentifier default newId()", name="GUID")
	public String getGuid() {
		return guid;
	}
	
	/**
	 * @param guid the guid to set
	 */
	/*
	 * primary key setter private so only hibernate can update this
	 */
	private void setGuid(String guid) {
		this.guid = guid;
	}

	/**
	 * @return the uid
	 */
	@Column(nullable=false, length=25, name="UID")
	public String getUid() {
		return uid;
	}
	
	/**
	 * @param uid the uid to set
	 */
	public void setUid(String uid) {
		this.uid = uid;
	}
	
	/**
	 * @return the fn
	 */
	@Column(length=25, name="FN")
	public String getFn() {
		return fn;
	}
	
	/**
	 * @param fn the fn to set
	 */
	public void setFn(String fn) {
		this.fn = fn;
	}
	
	/**
	 * @return the mi
	 */
	@Column(length=15, name="MI")
	public String getMi() {
		return mi;
	}
	
	/**
	 * @param mi the mi to set
	 */
	public void setMi(String mi) {
		this.mi = mi;
	}
	
	/**
	 * @return the ln
	 */
	@Column(length=35, name="LN")
	public String getLn() {
		return ln;
	}
	
	/**
	 * @param ln the ln to set
	 */
	public void setLn(String ln) {
		this.ln = ln;
	}
	
	/**
	 * @return the title
	 */
	@Column(length=10, name="Title")
	public String getTitle() {
		return title;
	}
	
	/**
	 * @param title the title to set
	 */
	public void setTitle(String t) {
		title = t;
	}
	
	/**
	 * @return the appEntryData
	 */
	@Column(name="AppEntryData",length=512)
	public String getAppEntryData() {
		return appEntryData;
	}
	
	/**
	 * @param appEntryData the appEntryData to set
	 */
	public void setAppEntryData(String suffix) {
		if (appEntryData != null) {
			appEntryData += suffix + ",";
		}
		else {
			appEntryData = suffix + ",";
		}
	}
	
	/**
	 * @return the lastImported
	 */
	@Column(name="LastImported",columnDefinition="datetime default getDate()")
	public Date getLastImported() {
		return lastImported;
	}
	
	/**
	 * @param lastImported the lastImported to set
	 */
	public void setLastImported(Date li) {
		lastImported = li;
	}

	/*
	 * default constructor required by hibernate
	 */
	public CcowUser() {
	}

	/**
	 * @param id
	 * @param fName
	 * @param lName
	 * @param mi
	 * @param t
	 */	
	public CcowUser(String id, String fName, String lName, String m, String t){
		setUid(id);
		setFn(fName);
		setLn(lName);
		setMi(m);
		setTitle(t);
		setLastImported(new Date());
	}
	
	@Override
	public String toString() {
		return "uid = " + getUid() + ", name = " + getFn() + " " + getMi() + " " + getLn() + ", Apps: " + getAppEntryData() + ";";
	}
}