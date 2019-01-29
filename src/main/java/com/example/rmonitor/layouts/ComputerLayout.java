package com.example.rmonitor.layouts;

import java.sql.Date;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.example.rmonitor.classes.Computer;
import com.example.rmonitor.classes.ConnectionManager;
import com.example.rmonitor.classes.ObjectConstructor;
import com.example.rmonitor.content.ComputerForm;
import com.vaadin.shared.ui.ValueChangeMode;
import com.vaadin.ui.Button;
import com.vaadin.ui.Grid;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Grid.SelectionMode;

public class ComputerLayout extends VerticalLayout {
	Grid<Computer> display_response;
    TextField filter;
    
    //computer form is too big to include in the same layout
    ComputerForm comp_form = new ComputerForm(this);
    
    ConnectionManager manager = null;
	ObjectConstructor constructor = null;
	
	final HorizontalLayout layout;
    final HorizontalLayout button_row;
    final HorizontalLayout grid_row;
    final VerticalLayout main;
    
    public ComputerLayout() {
    	//initialize
    	layout = new HorizontalLayout();
    	main = new VerticalLayout();
        manager = new ConnectionManager();
    	constructor = new ObjectConstructor();
        
        setSizeFull();
        addStyleName("computer-view");
        
        //prepare grid and buttons using function
        grid_row = gridLayout();
        button_row = buttonLayout();
        
        //add grid and buttons and computer form to main layout
        layout.addComponent(grid_row);
        layout.addComponent(comp_form);
        
        main.addComponents(button_row, layout);
        //add layout to view
        addComponent(main);
        
        setSizeUndefined();
    }
    
    private HorizontalLayout buttonLayout() {
    	HorizontalLayout button_row = new HorizontalLayout();
    	
    	//filter to search for Computers
    	filter = new TextField();
    	filter.setPlaceholder("Filter by Rental Number");
    	filter.addValueChangeListener(e -> updateList());
        filter.setValueChangeMode(ValueChangeMode.LAZY);
    	
    	//placeholder to get list of Computers from server
    	Button ViewComputers = new Button("ViewComputers");
        ViewComputers.addClickListener(e -> {
        	
        	int status = manager.connect();
        	//String response = new String();
        	List<Computer> computers = new ArrayList<>();
        	if (status == 1) {
        		computers = constructor.constructComputers(manager);
        		display_response.setItems(computers);
        	}
        	else {
        		display_response.setItems(Collections.emptyList());
        	}
        	
        	status = manager.disconnect();
        });
        
        //create new computer
        Button addComputer = new Button("Create New Computer");
        addComputer.addClickListener(e -> {
        	clearSelection();
        	grid_row.setVisible(false);
        	button_row.setVisible(false);
        	comp_form.setComputer(new Computer(
        			"", 
        			"", 
        			"",
        			"", 
        			Date.valueOf("2010-01-01"), 
        			Boolean.FALSE, 
        			new ArrayList<Integer>(),
        			"On Hand",
        			"",
        			0)
        			);
        });
        
        //add buttons to layout make them visible
        button_row.addComponents(filter, ViewComputers, addComputer);
        
        return button_row;
    }
    
    private HorizontalLayout gridLayout() {
    	HorizontalLayout grid_row = new HorizontalLayout();
    	
    	//grid columns
        display_response = new Grid<>();
        display_response.addColumn(Computer::getRentalNumber).setCaption("Rental Asset#");
    	display_response.addColumn(Computer::getCpu).setCaption("CPU");
    	display_response.addColumn(Computer::getPcType).setCaption("PCType");
    	display_response.addColumn(Computer::getOs).setCaption("OS");
    	display_response.addColumn(Computer::getPurchaseDate).setCaption("Purchase Date");
    	display_response.addColumn(Computer::getIsUpgraded).setCaption("Upgraded?");
    	display_response.addColumn(Computer::getStatus).setCaption("Status");
    	display_response.addColumn(Computer::getPrice).setCaption("Price");
        
    	//should only select one computer at a time
        display_response.setSelectionMode(SelectionMode.SINGLE);
        
        //grid dimensions
        display_response.setHeight("600px");
        display_response.setWidth("1000px");
        //display_computers_parts.setHeight("500px");
        grid_row.addComponents(display_response);
        
        
        //make computer form visible with details of selected computer
        display_response.asSingleSelect().addValueChangeListener(event -> {
            if (event.getValue() == null) {
                comp_form.setVisible(false);
                button_row.setVisible(true);
                grid_row.setVisible(true);
            } else {
            	grid_row.setVisible(false);
            	button_row.setVisible(false);
                comp_form.setComputer(event.getValue());
            }
        });
    	
    	return grid_row;
    }
    
    private void updateList() {
    	List<Computer> computers = new ArrayList<>();
    	manager.connect();
    	//filter list of Computers according to user-input string
    	computers = constructor.constructComputers(manager, filter.getValue());
    	manager.disconnect();
    	display_response.setItems(computers);
    }
    
    private void refreshList() {
    	List<Computer> computers = new ArrayList<>();
    	manager.connect();
    	//resend the list of Computers from server
    	computers = constructor.constructComputers(manager);
    	manager.disconnect();
    	display_response.setItems(computers);
    }

	public void clearSelection() {
		//remove highlights
		display_response.getSelectionModel().deselectAll();
	}
	

	public void resetView() {
		//refresh list of Computers and make buttons and grid visible again
		//called after hiding Computer form
		refreshList();
		button_row.setVisible(true);
		grid_row.setVisible(true);
	}
}
