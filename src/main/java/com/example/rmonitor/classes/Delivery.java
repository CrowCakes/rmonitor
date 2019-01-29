package com.example.rmonitor.classes;

import java.sql.Date;
import java.time.LocalDate;

import org.joda.time.DateTime;
import org.joda.time.Days;

public class Delivery {
	int deliveryID = 0;
	String SO = "";
	String SI = "";
	String ARD = "";
	String POS = "";
	String customerName = "";
	Date releaseDate;
	Date dueDate;
	String accountManager = "";
	String status = "";
	int extensionID = 0;
	int frequency; // expected values are 1 (day), 7 (week), 30 (month), 90 (quarter), 365 (year)
	float totalPrice = 0;
	int numberOfUnits = 0;
	
	public Delivery(int deliveryID, 
			String sO, String sI, String aRD, String pOS, 
			String customerName, 
			Date releaseDate, Date dueDate,
			String accountManager, 
			String status, 
			int extensionID,
			int frequency) {
		super();
		this.deliveryID = deliveryID;
		SO = sO;
		SI = sI;
		ARD = aRD;
		POS = pOS;
		this.customerName = customerName;
		this.releaseDate = releaseDate;
		this.dueDate = dueDate;
		this.accountManager = accountManager;
		this.status = status;
		this.extensionID = extensionID;
		this.frequency = frequency;
	}
	
	public int getNumberOfUnits() {
		return numberOfUnits;
	}

	public void setNumberOfUnits(int numberOfUnits) {
		this.numberOfUnits = numberOfUnits;
	}

	public int getFrequency() {
		return frequency;
	}
	
	public String getFrequencyStr() {
		if (this.frequency == 1) {
			return "Daily";
		}
		else if (this.frequency == 7) {
			return "Weekly";
		}
		else if (this.frequency == 30) {
			return "Monthly";
		}
		else if (this.frequency == 90) {
			return "Quarterly";
		}
		else if (this.frequency == 365) {
			return "Yearly";
		}
		else {return "Other";}
	}

	public void setFrequency(int frequency) {
		this.frequency = frequency;
	}
	
	public void setFrequency(String frequency) {
		if (frequency.equals("Daily")) {
			this.frequency = 1;
		}
		else if (frequency.equals("Weekly")) {
			this.frequency = 7;
		}
		else if (frequency.equals("Monthly")) {
			this.frequency = 30;
		}
		else if (frequency.equals("Quarterly")) {
			this.frequency = 90;
		}
		else if (frequency.equals("Yearly")) {
			this.frequency = 365;
		}
		else {
			this.frequency = 0;
		}
	}

	public float getTotalPrice() {
		return totalPrice;
	}
	
	public void updateTotalPrice(float unitPrice) {
		DateTime a = new DateTime(this.releaseDate);
		DateTime b = new DateTime(this.dueDate);
		if (this.frequency > 0) {
			this.totalPrice = unitPrice * (float)(Days.daysBetween(a,b).getDays() / this.frequency);
		}
		else {
			this.totalPrice = 0;
		}
	}
	
	public int getDeliveryID() {
		return deliveryID;
	}
	public void setDeliveryID(int deliveryID) {
		this.deliveryID = deliveryID;
	}
	public String getDeliveryIDStr() {
		return String.valueOf(deliveryID);
	}
	public void setDeliveryID(String deliveryID) {
		this.deliveryID = Integer.parseInt(deliveryID);
	}
	
	public String getSO() {
		return SO;
	}
	public void setSO(String sO) {
		this.SO = sO;
	}
	
	public String getSI() {
		return SI;
	}
	public void setSI(String sI) {
		SI = sI;
	}
	
	public String getARD() {
		return ARD;
	}
	public void setARD(String aRD) {
		ARD = aRD;
	}
	
	public String getPOS() {
		return POS;
	}
	public void setPOS(String pOS) {
		POS = pOS;
	}
	
	public String getcustomerName() {
		return customerName;
	}
	public void setcustomerName(String customerName) {
		this.customerName = customerName;
	}
	
	public Date getReleaseDate() {
		return releaseDate;
	}
	public LocalDate getReleaseLocalDate() {
		return releaseDate.toLocalDate();
	}
	public void setReleaseDate(Date releaseDate) {
		this.releaseDate = releaseDate;
	}
	public void setReleaseLocalDate(LocalDate releaseDate) {
		this.releaseDate = Date.valueOf(releaseDate);
	}
	
	public Date getDueDate() {
		return dueDate;
	}
	public void setDueDate(Date dueDate) {
		this.dueDate = dueDate;
	}
	public LocalDate getDueDateLocal() {
		return dueDate.toLocalDate();
	}
	public void setDueDateLocal(LocalDate dueDate) {
		this.dueDate = Date.valueOf(dueDate);
	}
	
	public String getAccountManager() {
		return accountManager;
	}
	public void setAccountManager(String accountManager) {
		this.accountManager = accountManager;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	
	public int getExtensionID() {
		return extensionID;
	}
	public void setExtensionID(int extensionID) {
		this.extensionID = extensionID;
	}
	public String getExtensionIDStr() {
		return String.valueOf(extensionID);
	}
	public void setExtensionID(String extensionID) {
		this.extensionID = Integer.parseInt(extensionID);
	}

	public boolean isPersisted() {
		if (this.deliveryID != 0) {return true;}
		else {return false;}
	}
}
