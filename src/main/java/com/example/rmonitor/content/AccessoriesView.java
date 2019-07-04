package com.example.rmonitor.content;

import com.example.rmonitor.classes.Accessory;
import com.example.rmonitor.classes.ConnectionManager;
import com.example.rmonitor.classes.ObjectConstructor;
import com.example.rmonitor.classes.OnDemandFileDownloader;
import com.example.rmonitor.classes.ReportGenerator;
import com.example.rmonitor.content.AccessoryForm;
import com.vaadin.navigator.View;
import com.vaadin.shared.ui.ValueChangeMode;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Component;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.Grid;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Panel;
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
import java.util.Arrays;
import java.util.List;

import org.joda.time.DateTime;
import org.vaadin.viritin.button.DownloadButton;

public class AccessoriesView
extends CssLayout
implements View {
    public static final String VIEW_NAME = "Accessories";
    ConnectionManager manager = new ConnectionManager();
    ObjectConstructor constructor = new ObjectConstructor();
    ReportGenerator generator = new ReportGenerator();
    
    final HorizontalLayout button_row;
    final VerticalLayout layout = new VerticalLayout();
    final HorizontalLayout main = new HorizontalLayout();
    final Panel panel = new Panel();
    Grid<Accessory> display_accessories;
    private AccessoryForm acc_form = new AccessoryForm(this);
    TextField filter;
    
    int MAX_LIMIT = 20;
    Label display_count = new Label("");
    int offset = 0;
    int limit = MAX_LIMIT;
    int count = 0;

    /***
     * Constructs the Accessories module.
     * @param user - string denoting the user's role
     */
    public AccessoriesView(String user) {
    	limit = MAX_LIMIT;
        this.setSizeFull();
        this.addStyleName("acc-view");
        this.prepare_grid(user);
        this.button_row = this.buttonsLayout(user);
        this.layout.addComponents(new Component[]{this.button_row, this.display_accessories, fetchNextBatch()});
        this.main.addComponents(new Component[]{this.layout, this.acc_form});
        this.main.setComponentAlignment((Component)this.acc_form, Alignment.MIDDLE_RIGHT);
        this.panel.setContent((Component)this.main);
        this.panel.setHeight("650px");
        this.panel.setWidth("1500px");
        this.addComponents(new Component[]{this.panel});
        this.refreshView();
    }

    /***
     * Formats the grid that will display the Accessories.
     * @param user - string denoting the user's role
     */
    private void prepare_grid(String user) {
        this.display_accessories = new Grid<>();
        this.display_accessories.addColumn(Accessory::getRentalNumber).setCaption("Rental Asset#");
        this.display_accessories.addColumn(Accessory::getName).setCaption("Name");
        this.display_accessories.addColumn(Accessory::getAccessoryType).setCaption("Type");
        this.display_accessories.addColumn(Accessory::getStatus).setCaption("Status");
        this.display_accessories.addColumn(Accessory::getRemarks).setCaption("Remarks");

        this.display_accessories.setHeight("500px");
        this.display_accessories.setWidth("600px");
        if (user.equals("Admin")) {
            this.display_accessories.asSingleSelect().addValueChangeListener(event -> {
                if (event.getValue() == null) {
                    this.acc_form.setVisible(false);
                } else {
                    this.acc_form.setAccessory((Accessory)event.getValue());
                }
            });
        }
    }
    
    private HorizontalLayout fetchNextBatch() {
    	Button previous = new Button(String.format("Previous %d", MAX_LIMIT));
        previous.addClickListener(e -> {
        	offset = (offset - limit < 0) ? 0 : offset - limit;
        	limit = (offset + limit > count) ? count - offset : limit;
        	displayNew(offset, limit);
        	
        	display_count.setValue(String.format("%d-%d of %d", offset, offset+limit, count));
        	limit = MAX_LIMIT;
        });
        Button next = new Button(String.format("Next %d", MAX_LIMIT));
        next.addClickListener(e -> {
        	offset = (offset + limit > count) ? offset : offset + limit;
        	limit = (offset + limit > count) ? count - offset : limit;
        	displayNew(offset, limit);
        	
        	display_count.setValue(String.format("%d-%d of %d", offset, offset+limit, count));
        	limit = MAX_LIMIT;
        });
        return new HorizontalLayout(previous, display_count, next);
    }

    /***
     * Constructs the buttons used to query the database controller.
     * @param user - string denoting the user's role
     * @return a Vaadin HorizontalLayout with the Buttons ready to use
     */
    private HorizontalLayout buttonsLayout(String user) {
        HorizontalLayout row = new HorizontalLayout();
        this.filter = new TextField();
        this.filter.setPlaceholder("Filter by Rental Number");
        this.filter.addValueChangeListener(e -> {
        	if (!filter.getValue().isEmpty()) this.updateList(); 
        	else this.refreshView();
        });
        this.filter.setValueChangeMode(ValueChangeMode.LAZY);
        
        Button ViewAccessories = new Button("Refresh");
        ViewAccessories.addClickListener(e -> this.refreshView());
        
        Button addAcc = new Button("Create New Accessory");
        addAcc.addClickListener(e -> {
            this.clearSelection();
            this.acc_form.setAccessory(new Accessory("", "", "", "", "n/a", 0.0f));
        });
        if (!user.equals("Admin")) {
            addAcc.setEnabled(false);
        }
        
        Button quit = new Button("Kill Server");
        quit.addClickListener(e -> {
            
        });
        
        Button report = new Button("Generate Inventory Report");
        report.addClickListener(e -> {
        	Notification.show("Notice", "Please wait while the report is generated", Notification.Type.TRAY_NOTIFICATION);
        });
        
        OnDemandFileDownloader.OnDemandStreamResource resource = new  OnDemandFileDownloader.OnDemandStreamResource()
        {
            @Override
            public String getFilename()
            {
            	return String.format("Inventory of Accessories - %s.xls", new Date(DateTime.now().getMillis()));
            }

            @Override
            public InputStream getStream()
            {
            	return new ByteArrayInputStream(
            			generator.generateReportAccessories(manager)
            			);
            	/*
            	manager.connect();
            	String response = manager.send("ReportAccessories").trim();
            	manager.disconnect();
            	
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
        	String response = manager.send("ReportAccessories");
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
        	return String.format("Inventory of Accessories - %s.csv", new Date(DateTime.now().getMillis()));
        })
        .withCaption("Generate Inventory Report");
        
        report.setEnabled(false);*/
        
        row.addComponents(new Component[]{this.filter, ViewAccessories, addAcc, report});
        return row;
    }

    /***
     * Refreshes the grid with latest database entries for Accessories, based on 
     * the filter's content.
     */
    private void updateList() {
    	List<String> foo = Arrays.asList(filter.getValue().split("\\s*/\\s*"));
		foo.replaceAll(string -> {
			String temp = String.format(".*%s.*", string);
			return temp;
		});
		String parameter = String.join("|", foo);
    	
        List<Accessory> computers = new ArrayList<>();
        this.manager.connect();
        computers = this.constructor.constructAccessories(this.manager, parameter);
        this.manager.disconnect();
        this.display_accessories.setItems(computers);
    }

    public void clearSelection() {
        this.display_accessories.getSelectionModel().deselectAll();
    }

    /***
     * Refreshes the grid with latest database entries for Accessories.
     */
    public void refreshView() {
        List<Accessory> parts = new ArrayList<>();
        offset = 0;
        
        manager.connect();
        count = constructor.getAccessoriesCount(manager);
        manager.disconnect();
        
        limit = (offset + limit > count) ? count - offset : limit;
        
        this.manager.connect();
        parts = this.constructor.constructAccessories(this.manager, offset, limit);
        this.manager.disconnect();
        
        this.display_accessories.setItems(parts);
        display_count.setValue(String.format("%d-%d of %d", offset, offset+limit, count));
        limit = MAX_LIMIT;
    }
    
    private void displayNew(int offset, int limit) {
    	List<Accessory> parts = new ArrayList<>();
    	
    	 this.manager.connect();
         parts = this.constructor.constructAccessories(this.manager, offset, limit);
         this.manager.disconnect();
         
         this.display_accessories.setItems(parts);
    }
}

