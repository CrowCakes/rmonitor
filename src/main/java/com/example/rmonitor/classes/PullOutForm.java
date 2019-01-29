package com.example.rmonitor.classes;

import java.sql.Date;
import java.time.LocalDate;

public class PullOutForm {
	int deliveryID;
	String formNumber;
	Date dateCreated;
	String status;
	
	public PullOutForm(int id, String num, Date date, String status) {
		this.deliveryID = id;
		this.formNumber = num;
		this.dateCreated = date;
		this.status = status;
	}

	public int getDeliveryID() {
		return deliveryID;
	}

	public void setDeliveryID(int deliveryID) {
		this.deliveryID = deliveryID;
	}

	public String getFormNumber() {
		return formNumber;
	}

	public void setFormNumber(String formNumber) {
		this.formNumber = formNumber;
	}

	public Date getDateCreated() {
		return dateCreated;
	}

	public void setDateCreated(Date dateCreated) {
		this.dateCreated = dateCreated;
	}
	
	public LocalDate getDateCreatedLocal() {
		return dateCreated.toLocalDate();
	}
	
	public void setDateCreated(LocalDate dateCreated) {
		this.dateCreated = Date.valueOf(dateCreated);
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}
}
