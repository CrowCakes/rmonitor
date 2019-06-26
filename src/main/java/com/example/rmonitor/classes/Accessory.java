package com.example.rmonitor.classes;

public class Accessory {
	String rentalNumber = "";
	String name = "";
	String accessoryType = "";
	String status = "";
	String remarks = "";
	float price;
	
	public Accessory(String rn, String n, String type, String stat, String remarks, float price) {
		this.rentalNumber = rn;
		this.name = n;
		this.accessoryType = type;
		this.remarks = remarks;
		this.price = price;
		
		//if defective set unavailable
		if (this.remarks.toLowerCase().indexOf("DEFECTIVE".toLowerCase()) != -1) {
			this.status = "Unavailable";
		}
		else {
			this.status = stat;
		}
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
		this.price = Integer.parseInt(price);
	}

	public String getRentalNumber() {
		return rentalNumber;
	}
	public void setRentalNumber(String rentalNumber) {
		this.rentalNumber = rentalNumber;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getAccessoryType() {
		return accessoryType;
	}
	public void setAccessoryType(String accessoryType) {
		this.accessoryType = accessoryType;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}

	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

	public boolean isPersisted() {
		// TODO Auto-generated method stub
		return rentalNumber != "";
	}
}
