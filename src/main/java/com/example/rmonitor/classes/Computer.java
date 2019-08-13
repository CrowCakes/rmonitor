package com.example.rmonitor.classes;

import java.sql.Date;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Computer {
	String rentalNumber = "";
	String cpu = "";
	String pcType = "";
	String os = "";
	Date purchaseDate;
	Boolean isUpgraded = Boolean.FALSE;
	List<Integer> partIDs = new ArrayList<>();
	String status = "On Hand";
	String description = "";
	float price;
	int history_count;
	
	public Computer(String rn, 
			String c, 
			String pc, 
			String o, 
			Date pur, Boolean is, List<Integer> parts, String status, String desc, float price) {
		this.rentalNumber = rn;
		this.cpu = c;
		this.pcType = pc;
		this.os = o;
		this.purchaseDate = pur;
		this.isUpgraded = is;
		this.partIDs = parts;
		//this.status = status;
		this.description = desc;
		this.price = price;
		
		//if defective set unavailable
		if (this.description.toLowerCase().indexOf("DEFECTIVE".toLowerCase()) != -1) {
			this.status = "Unavailable";
		}
		else if (this.description.toLowerCase().indexOf("DONATE".toLowerCase()) != -1) {
			this.status = "Unavailable";
		}
		else {
			this.status = status;
		}
	}
	
	public String getRentalNumber() {
		return rentalNumber;
	}
	public void setRentalNumber(String rentalNumber) {
		this.rentalNumber = rentalNumber;
	}
	public String getCpu() {
		return cpu;
	}
	public void setCpu(String cpu) {
		this.cpu = cpu;
	}
	public String getPcType() {
		return pcType;
	}
	public void setPcType(String pcType) {
		this.pcType = pcType;
	}
	public String getOs() {
		return os;
	}
	public void setOs(String os) {
		this.os = os;
	}
	public Boolean getIsUpgraded() {
		return isUpgraded;
	}
	public void setIsUpgraded(Boolean isUpgraded) {
		this.isUpgraded = isUpgraded;
	}
	public void setPartIDs(List<Integer> partIDs) {
		this.partIDs = partIDs;
	}
	public List<Integer> getPartIDs() {
		return partIDs;
	}

	public boolean isPersisted() {
		return rentalNumber != "";
	}

	public Date getPurchaseDate() {
		return purchaseDate;
	}
	
	public LocalDate getPurchaseLocalDate() {
		return purchaseDate.toLocalDate();
	}

	public void setPurchaseDate(Date purchaseDate) {
		this.purchaseDate = purchaseDate;
	}
	
	public void setPurchaseDateFromLocal(LocalDate purchaseDate) {
		this.purchaseDate = Date.valueOf(purchaseDate);
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public float getPrice() {
		return price;
	}

	public void setPrice(float price) {
		this.price = price;
	}
	
	public String getPriceStr() {
		return String.valueOf(price);
	}

	public void setPrice(String price) {
		this.price = Float.parseFloat(price);
	}

	public int getHistory_count() {
		return history_count;
	}

	public void setHistory_count(int history_count) {
		this.history_count = history_count;
	}
}
