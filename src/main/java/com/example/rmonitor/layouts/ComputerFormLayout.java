package com.example.rmonitor.layouts;

import com.example.rmonitor.classes.Parts;
import com.vaadin.ui.Button;
import com.vaadin.ui.DateField;
import com.vaadin.ui.Grid;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.TextArea;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;

public class ComputerFormLayout extends HorizontalLayout {
	//TODO: Add label if attached to an active delivery
	protected TextField rn = new TextField("Rental Asset#");
	protected TextField cpu = new TextField("CPU");
	protected TextField pctype = new TextField("PCType");
	protected TextField os = new TextField("OS");
	protected DateField pur = new DateField("Purchase Date");
	protected TextArea description = new TextArea("Description");
	protected TextField price = new TextField("Price");
	protected TextField status = new TextField("Status");
	protected Label parent_delivery = new Label();
	
	protected Grid<Parts> display_computers_parts = new Grid<>();
	protected Grid<Parts> available_parts = new Grid<>();
	
	protected TextField filter = new TextField();
	
	protected Button addpart = new Button("Add Part");
	protected Button editpart = new Button("Change Part");
	protected Button delpart = new Button("Delete Part");
	
	protected Button choosepart = new Button("Select this Part");
	protected Button cancelpart = new Button("Cancel");
	
	protected Button save = new Button("Save");
	protected Button delete = new Button("Delete");
	protected Button cancel = new Button("Cancel");
	protected Button resetpart = new Button("Reset Parts to Original");
	
	protected HorizontalLayout select_parts = new HorizontalLayout(choosepart, cancelpart);
	protected HorizontalLayout part_buttons = new HorizontalLayout(addpart, editpart, delpart);
	protected HorizontalLayout save_cancel = new HorizontalLayout(save, cancel, delete, resetpart);
	
	protected VerticalLayout select_menu = new VerticalLayout(filter, available_parts, select_parts);
	protected VerticalLayout parts_menu = new VerticalLayout(display_computers_parts, part_buttons);
}
