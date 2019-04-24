package com.example.rmonitor.content;

import com.example.rmonitor.classes.Accessory;
import com.example.rmonitor.classes.Computer;
import com.example.rmonitor.classes.ConnectionManager;
import com.example.rmonitor.classes.ObjectConstructor;
import com.example.rmonitor.classes.OnDemandFileDownloader;
import com.example.rmonitor.classes.ReportGenerator;
import com.example.rmonitor.content.ComputerForm;
import com.vaadin.navigator.View;
import com.vaadin.shared.ui.ValueChangeMode;
import com.vaadin.ui.Button;
import com.vaadin.ui.Component;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.Grid;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Notification;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;
import org.joda.time.DateTime;
import org.vaadin.viritin.button.DownloadButton;

public class ComputerView
extends CssLayout
implements View {
    public static final String VIEW_NAME = "Computer";
    Grid<Computer> display_response;
    TextField filter;
    ComputerForm comp_form = new ComputerForm(this);
    ConnectionManager manager = new ConnectionManager();
    ObjectConstructor constructor = new ObjectConstructor();
    ReportGenerator generator = new ReportGenerator();
    HorizontalLayout layout = new HorizontalLayout();
    HorizontalLayout button_row;
    HorizontalLayout grid_row;
    VerticalLayout main = new VerticalLayout();
    VerticalLayout cpuFilter = new VerticalLayout();
    
    int MAX_LIMIT = 20;
    Label text = new Label("");
    int offset = 0;
    int limit = MAX_LIMIT;
    int count = 0;

    /***
     * Constructs the module used to view the list of Computers.
     * @param user - string denoting the user's role
     */
    public ComputerView(String user) {
    	limit = MAX_LIMIT;
        this.setSizeFull();
        this.addStyleName("computer-view");
        this.grid_row = this.gridLayout(user);
        this.button_row = this.buttonLayout(user);
        this.layout.addComponents(new Component[]{this.grid_row});
        if ("Admin".equals(user)) {
            this.layout.addComponent((Component)this.comp_form);
        }
        this.main.addComponents(new Component[]{this.button_row, this.layout, fetchNextBatch(), this.cpuFilter});
        this.addComponent((Component)this.main);
        this.refreshList();
    }
    
    private HorizontalLayout fetchNextBatch() {
    	Button previous = new Button(String.format("Previous %d", MAX_LIMIT));
        previous.addClickListener(e -> {
        	offset = (offset - limit < 0) ? 0 : offset - limit;
        	limit = (offset + limit > count) ? count - offset : limit;
        	displayNew(offset, limit);
        	
        	text.setValue(String.format("%d-%d of %d", offset, offset+limit, count));
        	limit = MAX_LIMIT;
        });
        Button next = new Button(String.format("Next %d", MAX_LIMIT));
        next.addClickListener(e -> {
        	offset = (offset + limit > count) ? offset : offset + limit;
        	limit = (offset + limit > count) ? count - offset : limit;
        	displayNew(offset, limit);
        	
        	text.setValue(String.format("%d-%d of %d", offset, offset+limit, count));
        	limit = MAX_LIMIT;
        });
        return new HorizontalLayout(previous, text, next);
    }

    /***
     * Constructs the Buttons used to query the database.
     * @param user - string denoting user's role
     * @return a HorizontalLayout containing the buttons ready to use
     */
    private HorizontalLayout buttonLayout(String user) {
        HorizontalLayout button_row = new HorizontalLayout();
        this.filter = new TextField();
        this.filter.setPlaceholder("Filter by Rental Number");
        this.filter.addValueChangeListener(e -> {
        	if (!filter.getValue().isEmpty()) this.updateList(); 
        	else this.refreshList();
        });
        this.filter.setValueChangeMode(ValueChangeMode.LAZY);
        
        Button ViewComputers = new Button("Refresh");
        ViewComputers.addClickListener(e -> this.refreshList());
        
        Button addComputer = new Button("Create New Unit");
        addComputer.addClickListener(e -> {
            this.clearSelection();
            this.grid_row.setVisible(false);
            button_row.setVisible(false);
            this.cpuFilter.setVisible(false);
            this.comp_form.setComputer(new Computer("", "", "", "", 
            		new Date(DateTime.now().getMillis()), 
            		Boolean.FALSE, 
            		new ArrayList<Integer>(), 
            		"On Hand", 
            		"", 
            		0.0f));
        });
        if (!user.equals("Admin")) {
            addComputer.setEnabled(false);
        }
        
        Button report = new Button("Generate Inventory Report");
        report.addClickListener(e -> {
        	Notification.show("Notice", "Please wait while the report is generated", Notification.Type.TRAY_NOTIFICATION);
        });
        
        OnDemandFileDownloader.OnDemandStreamResource resource = new  OnDemandFileDownloader.OnDemandStreamResource()
        {
            @Override
            public String getFilename()
            {
            	return String.format("Inventory of Rental Units - %s.xls", new Date(DateTime.now().getMillis()));
            }

            @Override
            public InputStream getStream()
            {
            	return new ByteArrayInputStream(generator.generateReportComputers(manager));
            	/*
            	manager.connect();
            	String response = manager.send("ReportComputers").trim();
            	manager.disconnect();
            	
            	System.out.println("getStream: " + response);
            	
            	byte[] source = null;
            	
            	Path path = FileSystems.getDefault().getPath(response);
            	try {
            		source = Files.readAllBytes(path);
                	return new ByteArrayInputStream(source);
            	}
            	catch (IOException ex) {
            		System.out.println("Failed");
            		ex.printStackTrace();
            		return null;
            	}
            	*/
            }
         };
         
         OnDemandFileDownloader downloader = new OnDemandFileDownloader(
        	        resource);
         downloader.extend(report);
        
        /*
        DownloadButton report = new DownloadButton(out -> {
        	
        	this.manager.connect();
        	String response = manager.send("ReportComputers");
        	this.manager.disconnect();
        	
        	try {
        		out.write(response.getBytes());
        		//Path path = FileSystems.getDefault().getPath(response);
        		//Files.copy(path, out);
        	}
        	catch (IOException ex) {
        		
        	}
        })
        .setFileNameProvider(() -> {
        	return String.format("Inventory of Computers - %s.csv", new Date(DateTime.now().getMillis()));
        })
        .withCaption("Generate Inventory Report");
        
        report.setEnabled(false);*/
        
        button_row.addComponents(new Component[]{this.filter, ViewComputers, addComputer, report});
        return button_row;
    }

    /***
     * Constructs the grid that will be used to display the Computers.
     * @param user - string denoting the user's role
     * @return a HorizontalLayout with the module's grid
     */
    private HorizontalLayout gridLayout(String user) {
        HorizontalLayout grid_row = new HorizontalLayout();
        
        this.display_response = new Grid<>();
        this.display_response.addColumn(Computer::getRentalNumber).setCaption("Rental Asset#");
        this.display_response.addColumn(Computer::getCpu).setCaption("CPU");
        this.display_response.addColumn(Computer::getPcType).setCaption("PCType");
        this.display_response.addColumn(Computer::getOs).setCaption("OS");
        this.display_response.addColumn(Computer::getPurchaseDate).setCaption("Purchase Date");
        this.display_response.addColumn(Computer::getIsUpgraded).setCaption("Upgraded?");
        this.display_response.addColumn(Computer::getStatus).setCaption("Status");
        this.display_response.addColumn(Computer::getDescription).setCaption("Remarks");
        this.display_response.setSelectionMode(Grid.SelectionMode.SINGLE);
        display_response.setFrozenColumnCount(1);
        this.display_response.setHeight("325px");
        this.display_response.setWidth("1000px");
        
        grid_row.addComponents(new Component[]{this.display_response});
        if (user.equals("Admin")) {
            this.display_response.asSingleSelect().addValueChangeListener(event -> {
                if (event.getValue() == null) {
                    this.comp_form.setVisible(false);
                    this.button_row.setVisible(true);
                    grid_row.setVisible(true);
                    this.cpuFilter.setVisible(true);
                } else {
                    grid_row.setVisible(false);
                    this.button_row.setVisible(false);
                    this.cpuFilter.setVisible(false);
                    this.comp_form.setComputer((Computer)event.getValue());
                }
            });
        }
        return grid_row;
    }

    /***
     * Fetches the latest entries from the database based on filter input.
     */
    private void updateList() {
        List<Computer> computers = new ArrayList<>();
        this.manager.connect();
        computers = this.constructor.constructComputers(this.manager, this.filter.getValue());
        this.manager.disconnect();
        this.display_response.setItems(computers);
    }

    /***
     * Fetches the latest entries from the database. Also queries the number of CPUs of certain processor types.
     */
    private void refreshList() {
        List<Computer> computers = new ArrayList<>();
        offset = 0;
        
        manager.connect();
        count = constructor.getComputerCount(manager);
        manager.disconnect();

        limit = (offset + limit > count) ? count - offset : limit;
        
        this.manager.connect();
        computers = this.constructor.constructComputers(this.manager, offset, limit);
        this.manager.disconnect();
        this.display_response.setItems(computers);
        
        this.manager.connect();
        List<String> cpus = this.constructor.parseRawString(this.manager.send("ViewCPUQty"));
        this.manager.disconnect();
        
        this.cpuFilter.removeAllComponents();
        this.cpuFilter.addComponent((Component)new Label("Count of Units by CPU type:"));
        for (String text : cpus) {
            this.cpuFilter.addComponent((Component)new Label(text));
        }
        text.setValue(String.format("%d-%d of %d", offset, offset+limit, count));
        limit = MAX_LIMIT;
    }
    
    private void displayNew(int offset, int limit) {
    	List<Computer> computers = new ArrayList<>();
        
        this.manager.connect();
        computers = this.constructor.constructComputers(this.manager, offset, limit);
        this.manager.disconnect();
        
        this.display_response.setItems(computers);
    }

    /***
     * Deselects the grid of any selections.
     */
    public void clearSelection() {
        this.display_response.getSelectionModel().deselectAll();
    }

    /***
     * Refreshes the grid and the module's visible components.
     */
    public void resetView() {
        this.refreshList();
        this.button_row.setVisible(true);
        this.grid_row.setVisible(true);
        this.cpuFilter.setVisible(true);
    }
}

