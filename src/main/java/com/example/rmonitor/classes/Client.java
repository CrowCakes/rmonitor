package com.example.rmonitor.classes;

public class Client {
	int clientid = 0;
	String name;
	String address;
	String contact_person;
	long contact_number;
	
	public Client(int clientid, String name, String address, String contact_person, long contact_number) {
		this.clientid = clientid;
		this.name = name;
		this.address = address;
		this.contact_person = contact_person;
		this.contact_number = contact_number;
	}

	public int getClientid() {
		return clientid;
	}

	public void setClientid(int clientid) {
		this.clientid = clientid;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getContact_person() {
		return contact_person;
	}

	public void setContact_person(String contact_person) {
		this.contact_person = contact_person;
	}

	public long getContact_number() {
		return contact_number;
	}

	public void setContact_number(long contact_number) {
		this.contact_number = contact_number;
	}
	
	public String getContact_numberStr() {
		return String.valueOf(contact_number);
	}

	public void setContact_number(String contact_number) {
		this.contact_number = Long.parseLong(contact_number);
	}

	public boolean isPersisted() {
		if (clientid != 0) {
			return Boolean.TRUE;
		}
		else {
			return Boolean.FALSE;
		}
	}
}
