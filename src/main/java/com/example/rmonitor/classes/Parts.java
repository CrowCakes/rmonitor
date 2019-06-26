package com.example.rmonitor.classes;

public class Parts {
	int partID;
	String name;
	String partType;
	String status;
	float price;
	String parent = "";
	String originalParent = "";
	String remarks;
	
	public String getParent() {
		return parent;
	}

	public void setParent(String parent) {
		this.parent = parent;
	}

	public String getOriginalParent() {
		return originalParent;
	}

	public void setOriginalParent(String originalParent) {
		this.originalParent = originalParent;
	}

	public Parts(int pid, String n, String pt, String s, String remarks, float price) {
		this.partID = pid;
		this.name = n;
		this.partType = pt;
		//this.status = s;
		this.remarks = remarks;
		this.price = price;
		
		//if defective set unavailable
		if (this.remarks.toLowerCase().indexOf("DEFECTIVE".toLowerCase()) != -1) {
			this.status = "Unavailable";
		}
		else {
			this.status = s;
		}
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

	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}
}
