package com.example.rmonitor.layouts;

import java.util.ArrayList;
import java.util.List;

import com.example.rmonitor.classes.ConnectionManager;
import com.example.rmonitor.classes.Delivery;
import com.example.rmonitor.classes.ObjectConstructor;
import com.vaadin.ui.Button;
import com.vaadin.ui.Grid;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Grid.SelectionMode;

public class DeliveriesLayout extends VerticalLayout {
	ConnectionManager manager = null;
	ObjectConstructor constructor = null;

	HorizontalLayout grid_row;
	VerticalLayout layout;
	HorizontalLayout main;
	Button refresh;
	Label none_found = new Label("No upcoming Deliveries are due");
	
	Grid<Delivery> display_deliveries;
	
	TextField filter;
	
	public DeliveriesLayout() {
		//initialize
		manager = new ConnectionManager();
    	constructor = new ObjectConstructor();
    	layout = new VerticalLayout();
    	main = new HorizontalLayout();
    	
    	setSizeFull();
        addStyleName("delv-view");
        
        //setup grid in function
        grid_row = gridLayout();
        refresh = prepare_button();
        
        //add to layout
        layout.addComponents(new HorizontalLayout(refresh, none_found), grid_row);
        refresh_grid();
        main.addComponents(layout);
        
        //add to view
        addComponents(main);
        
        setSizeUndefined();
	}
	
	private HorizontalLayout gridLayout() {
		HorizontalLayout grid_row = new HorizontalLayout();
		
		//columns
		display_deliveries = new Grid<>();
        display_deliveries.setHeight("600px");
        display_deliveries.addColumn(Delivery::getDeliveryID).setCaption("DelvID");
    	display_deliveries.addColumn(Delivery::getcustomerName).setCaption("Client");
    	display_deliveries.addColumn(Delivery::getReleaseDate).setCaption("Released");
    	display_deliveries.addColumn(Delivery::getDueDate).setCaption("Due");
    	display_deliveries.addColumn(Delivery::getFrequencyStr).setCaption("MOF");
    	display_deliveries.addColumn(Delivery::getStatus).setCaption("Status");
    	display_deliveries.addColumn(Delivery::getExtensionID).setCaption("Extended to?");
    	//dimensions
        display_deliveries.setWidth("1050px");
        
        //take user to delivery form when selecting an entry
        display_deliveries.setSelectionMode(SelectionMode.SINGLE);
        
        //add to layout
        grid_row.addComponents(display_deliveries);
		
		return grid_row;
	}
	
	private Button prepare_button() {
		Button refresh = new Button("Refresh");
		refresh.addClickListener(e -> refresh_grid());
		return refresh;
	}
	
	private void refresh_grid() {
		List<Delivery> parts = new ArrayList<>();
    	
    	manager.connect();
    	parts = constructor.constructUrgentDeliveries(manager);
    	manager.disconnect();
    	
    	display_deliveries.setItems(parts);
    	if (parts.size() < 1) {
    		none_found.setVisible(true);
    	}
    	else {
    		none_found.setVisible(false);
    	}
	}
}
