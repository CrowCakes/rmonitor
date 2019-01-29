package com.example.rmonitor.classes;

public class Parts {
	int partID;
	String name;
	String partType;
	String status;
	float price;
	String parent;
	
	public String getParent() {
		return parent;
	}

	public void setParent(String parent) {
		this.parent = parent;
	}

	public Parts(int pid, String n, String pt, String s, float price) {
		this.partID = pid;
		this.name = n;
		this.partType = pt;
		this.status = s;
		this.price = price;
	}
	
	public int getPartID() {
		return partID;
	}
	public void setPartID(int partID) {
		this.partID = partID;
	}
	
	public String getPartIDStr() {
		return String.valueOf(partID);
	}
	public void setPartID(String partID) {
		this.partID = Integer.parseInt(partID);
	}
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getPartType() {
		return partType;
	}
	public void setPartType(String partType) {
		this.partType = partType;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}

	public boolean isPersisted() {
		return partID > 0;
	}

	public String getStringRepresentation() {
		return String.format("%d - %s, %s", partID, name, partType);
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
}
