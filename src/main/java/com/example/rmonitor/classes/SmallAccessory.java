package com.example.rmonitor.classes;

public class SmallAccessory {
	String name = "";
	String accessoryType = "";
	float price = 0f;
	int quantity = 0;
	int quantityMinus = 0;
	
	public SmallAccessory(String name, String accessoryType, float price, int quantity, int quantityMinus) {
		super();
		this.name = name;
		this.accessoryType = accessoryType;
		this.price = price;
		this.quantity = quantity;
		this.quantityMinus = quantityMinus;
	}
	
	public Boolean isPersisted() {
		if (name.isEmpty()) {
			return false;
		}
		else return true;
	}
	
	public int getQuantityRemaining() {
		return quantity - quantityMinus;
	}
	
	public int getQuantityMinus() {
		return quantityMinus;
	}

	public void setQuantityMinus(int quantityMinus) {
		this.quantityMinus = quantityMinus;
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
	public float getPrice() {
		return price;
	}
	public void setPrice(float price) {
		this.price = price;
	}
	public int getQuantity() {
		return quantity;
	}
	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}
	
	public String getQuantityStr() {
		return String.valueOf(quantity);
	}
	public void setQuantity(String quantity) {
		this.quantity = Integer.parseInt(quantity);
	}
}
