package com.example.rmonitor.classes;

public enum PartTypes {
	Motherboard("Motherboard"), Monitor("Monitor"), HDD("HDD"), Processor("Processor"), Memory("Memory");
	
	private final String name;
	
	private PartTypes(String name) {
		this.name = name;
	}
	
	@Override
    public String toString() {
        return name;
    }
}
