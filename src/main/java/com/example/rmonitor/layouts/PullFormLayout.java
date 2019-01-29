package com.example.rmonitor.layouts;

import com.example.rmonitor.classes.Accessory;
import com.example.rmonitor.classes.Computer;
import com.example.rmonitor.classes.SmallAccessory;
import com.vaadin.shared.ui.ContentMode;
import com.vaadin.ui.Button;
import com.vaadin.ui.Grid;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Panel;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;

public class PullFormLayout extends Panel {
	protected Label delv_label = new Label("Pull-out - Enter a Delivery# you want to pull-out from a client");
	protected TextField delv_num = new TextField();
	protected Button get_delivery = new Button("Search");
	protected Button create_po = new Button("Create New Pull-out");
	
	protected TextField select_po = new TextField();
	
	protected Button prepare_comp = new Button("->");
	protected Button prepare_acc = new Button("->");
	protected Button prepare_per = new Button("->");
	protected Button return_comp = new Button("<-");
	protected Button return_acc = new Button("<-");
	protected Button return_per = new Button("<-");
	
	protected Button delete = new Button("Pull-out Delivery");
	protected Button generate_first = new Button("Generate Full Form");
	protected Button generate = new Button("Generate Form");
	protected Button delete_form = new Button("Delete Pull-out");
	protected Button back = new Button("Back");
	
	protected Grid<Computer> delivery_comp = new Grid<>();
	protected Grid<Accessory> delivery_acc = new Grid<>();	
	protected Grid<SmallAccessory> delivery_per = new Grid<>();
	
	protected Grid<Computer> prepared_comp = new Grid<>();
	protected Grid<Accessory> prepared_acc = new Grid<>();	
	protected Grid<SmallAccessory> prepared_per = new Grid<>();

	protected HorizontalLayout delv_grid = new HorizontalLayout(
			new VerticalLayout(new Label("<b>Active Units<//b>", ContentMode.HTML), delivery_comp), 
			new VerticalLayout(prepare_comp, return_comp), 
			new VerticalLayout(new Label("<b>Units to be Pulled-out<//b>", ContentMode.HTML), prepared_comp));
	protected HorizontalLayout acc_grid = new HorizontalLayout(
			new VerticalLayout(new Label("<b>Active Accessories<//b>", ContentMode.HTML), delivery_acc), 
			new VerticalLayout(prepare_acc, return_acc),
			new VerticalLayout(new Label("<b>Accessories to be Pulled-out<//b>", ContentMode.HTML), prepared_acc));
	protected HorizontalLayout per_grid = new HorizontalLayout(
			new VerticalLayout(new Label("<b>Active Peripherals<//b>", ContentMode.HTML), delivery_per),
			new VerticalLayout(prepare_per, return_per),
			new VerticalLayout(new Label("<b>Peripherals to be Pulled-out<//b>", ContentMode.HTML), prepared_per)
			);

	protected VerticalLayout display = new VerticalLayout(new HorizontalLayout(generate_first, generate, delete, delete_form),
			delv_grid, acc_grid, per_grid);
}
