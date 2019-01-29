package com.example.rmonitor.layouts;

import com.example.rmonitor.classes.Accessory;
import com.example.rmonitor.classes.Computer;
import com.example.rmonitor.classes.SmallAccessory;
import com.vaadin.ui.Button;
import com.vaadin.ui.DateField;
import com.vaadin.ui.Grid;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.NativeSelect;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;

public class DeliveryFormLayout extends HorizontalLayout {
	protected TextField delv_number = new TextField("Delivery#");
	protected TextField so = new TextField("Sales Order");
	protected TextField si = new TextField("Sales Invoice");
	protected TextField ard = new TextField("ARD");
	protected TextField pos = new TextField("POS");
	protected NativeSelect<String> client = new NativeSelect<>("Client");
	protected DateField release = new DateField("Release Date");
	protected DateField due = new DateField("Due Date");
	protected TextField acct_manager = new TextField("Account Manager");
	protected NativeSelect<String> status = new NativeSelect<>("Status");
	protected TextField ext = new TextField("Extended to");
	protected TextField price = new TextField("Price");
	protected NativeSelect<String> freq = new NativeSelect<>("MOF");
	
	protected Button add_comp = new Button("Add Computer");
	protected Button edit_comp = new Button("Change Computer");
	protected Button delete_comp = new Button("Remove Computer");
	protected Button select_comp = new Button("Select");
	protected Button cancel_comp = new Button("Cancel");
	
	protected Button add_acc = new Button("Add Accessory");
	protected Button edit_acc = new Button("Edit Accessory");
	protected Button delete_acc = new Button("Delete Accessory");
	protected Button select_acc = new Button("Select");
	protected Button cancel_acc = new Button("Cancel");
	
	protected Button add_small_acc = new Button("Add Peripheral");
	protected Button edit_small_acc = new Button("Edit Peripheral");
	protected Button delete_small_acc = new Button("Delete Peripheral");
	protected Button select_small_acc = new Button("Select");
	protected Button cancel_small_acc = new Button("Cancel");
	
	protected Button save = new Button("Save");
	protected Button cancel = new Button("Cancel");
	protected Button delete = new Button("Return Delivery");
	protected Button extend = new Button("Extend");
	
	protected Grid<Computer> display_computers = new Grid<>();
	protected Grid<Computer> available_computers = new Grid<>();
	
	protected Grid<Accessory> display_acc = new Grid<>();
	protected Grid<Accessory> available_acc = new Grid<>();
	
	protected Grid<SmallAccessory> display_small_acc = new Grid<>();
	protected NativeSelect<SmallAccessory> small_accs = new NativeSelect<>("Select a Peripheral");
	protected TextField small_acc_amount = new TextField("Select an amount");
	protected Label small_acc_available_amount = new Label();
	protected VerticalLayout available_small_acc = new VerticalLayout(small_accs, 
			new VerticalLayout(small_acc_amount, small_acc_available_amount));
	
	protected TextField filter = new TextField();
	protected TextField acc_filter = new TextField();
	//protected TextField small_acc_filter = new TextField();
	
	protected HorizontalLayout comp_buttons = new HorizontalLayout(add_comp, edit_comp, delete_comp);
	protected HorizontalLayout choose_buttons = new HorizontalLayout(select_comp, cancel_comp);
	
	protected HorizontalLayout acc_buttons = new HorizontalLayout(add_acc, edit_acc, delete_acc);
	protected HorizontalLayout choose_acc = new HorizontalLayout(select_acc, cancel_acc);
	
	protected HorizontalLayout small_acc_buttons = new HorizontalLayout(add_small_acc, edit_small_acc, delete_small_acc);
	protected HorizontalLayout choose_small_acc = new HorizontalLayout(select_small_acc, cancel_small_acc);
	
	protected HorizontalLayout buttons = new HorizontalLayout(save, cancel);
	
	protected VerticalLayout main_grid = new VerticalLayout(display_computers, comp_buttons, 
			display_acc, acc_buttons, 
			display_small_acc, small_acc_buttons);
	protected VerticalLayout choose_grid = new VerticalLayout(
			filter, available_computers, choose_buttons, 
			acc_filter, available_acc, choose_acc,
			available_small_acc, choose_small_acc);
}
