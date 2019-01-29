package com.example.rmonitor.layouts;

import com.vaadin.ui.Button;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;

public class ClientFormLayout extends VerticalLayout {
	protected TextField name = new TextField("Name");
	protected TextField address = new TextField("Address");
	protected TextField contactperson = new TextField("Contact Person");
	protected TextField contactnum = new TextField("Contact#");
	
	protected Button save = new Button("Save");
	protected Button delete = new Button("Delete");
	protected Button cancel = new Button("Cancel");
	protected HorizontalLayout buttons = new HorizontalLayout(save, cancel);
}
