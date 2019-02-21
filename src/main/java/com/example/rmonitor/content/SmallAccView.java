package com.example.rmonitor.content;

import com.example.rmonitor.classes.AdvancedFileDownloader;
import com.example.rmonitor.classes.AdvancedFileDownloader.AdvancedDownloaderListener;
import com.example.rmonitor.classes.AdvancedFileDownloader.DownloaderEvent;
import com.example.rmonitor.classes.ConnectionManager;
import com.example.rmonitor.classes.ObjectConstructor;
import com.example.rmonitor.classes.OnDemandFileDownloader;
import com.example.rmonitor.classes.ReportGenerator;
import com.example.rmonitor.classes.SmallAccessory;
import com.example.rmonitor.content.SmallAccessoryForm;
import com.vaadin.navigator.View;
import com.vaadin.shared.ui.ValueChangeMode;
import com.vaadin.ui.Button;
import com.vaadin.ui.Component;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.Grid;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Notification;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.InvalidPathException;
import java.nio.file.Path;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

import org.joda.time.DateTime;
import org.vaadin.viritin.button.DownloadButton;

public class SmallAccView
extends CssLayout
implements View {
    public static final String VIEW_NAME = "SmallAcc";
    ConnectionManager manager = new ConnectionManager();
    ObjectConstructor constructor = new ObjectConstructor();
    ReportGenerator generator = new ReportGenerator();
    Grid<SmallAccessory> display_accessories = new Grid<>();
    private SmallAccessoryForm acc_form = new SmallAccessoryForm(this);
    HorizontalLayout button_row;
    VerticalLayout main = new VerticalLayout();
    TextField filter;

    public SmallAccView(String user) {
        this.setSizeFull();
        this.button_row = this.buttonsLayout(user);
        this.prepare_grid(user);
        this.main.addComponents(new Component[]{this.button_row, this.display_accessories, this.acc_form});
        this.addComponent((Component)this.main);
        this.refreshView();
    }

    private HorizontalLayout buttonsLayout(String user) {
        HorizontalLayout row = new HorizontalLayout();
        this.filter = new TextField();
        this.filter.setPlaceholder("Filter by Name");
        this.filter.addValueChangeListener(e -> this.updateList());
        this.filter.setValueChangeMode(ValueChangeMode.LAZY);
        
        Button ViewSmallAccessories = new Button("Refresh");
        ViewSmallAccessories.addClickListener(e -> this.refreshView());
        
        Button addSmallAcc = new Button("Create New Accessory");
        addSmallAcc.addClickListener(e -> {
            this.clearSelection();
            this.acc_form.setAccessory(new SmallAccessory("", "", 0.0f, 0, 0));
        });
        if (!user.equals("Admin")) {
            addSmallAcc.setEnabled(false);
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
            	return String.format("Inventory of Bulk Accessories - %s.xls", new Date(DateTime.now().getMillis()));
            }

            @Override
            public InputStream getStream()
            {
            	return new ByteArrayInputStream(
            			generator.generateReportSmallAccessories(manager)
            	);
            	/*
            	manager.connect();
            	String response = manager.send("ReportSmallAccessories").trim();
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
        final AdvancedFileDownloader downloader = new AdvancedFileDownloader();
        downloader.addAdvancedDownloaderListener(new AdvancedDownloaderListener() {*/

                    /*
                     * This method will be invoked just before the download
                     * starts. Thus, a new file path can be set.
                     * 
                     * @param downloadEvent
                     **/
        /*
                    @Override
                    public void beforeDownload(DownloaderEvent downloadEvent) {
                    	manager.connect();
                    	String response = manager.send("ReportSmallAccessories");
                    	manager.disconnect();

                        String filePath = response;
                        System.out.println("Received file path " + filePath);

                        downloader.setFilePath(filePath);

                        System.out.println("Starting downlad by button "
                                + filePath.substring(filePath.lastIndexOf("/")));
                    }

                });
        
        
        downloader.extend(report);*/
        
        
        /*
        DownloadButton report = new DownloadButton(out -> {
        	this.manager.connect();
        	String response = manager.send("ReportSmallAccessories");
        	this.manager.disconnect();
        	
        	FileInputStream file = null;
        	
        	try {
        		//out.write(response.getBytes());
        		
        		file = new FileInputStream(response);
        		int c;
                while ((c = file.read()) != -1) {
                   out.write(c);
                }
        		
        		//Path path = FileSystems.getDefault().getPath(response);
        		//Files.copy(path, out);
        	}
        	catch (IOException ex) {
        		System.out.println("Error writing");
        	}
        	catch (InvalidPathException ex) {
        		System.out.println("Error opening file path");
        	}
        	finally {
        		if (file != null) {
                    try {
						file.close();
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
                 }
        	}
        })
        .setFileNameProvider(() -> {
        	return String.format("Inventory of Bulk Accessories - %s.csv", new Date(DateTime.now().getMillis()));
        })
        .withCaption("Generate Inventory Report");
        */
        //report.setEnabled(false);
        
        
        row.addComponents(new Component[]{this.filter, ViewSmallAccessories, addSmallAcc, report});
        return row;
    }

    private void prepare_grid(String user) {
        this.display_accessories.addColumn(SmallAccessory::getName).setCaption("Name");
        this.display_accessories.addColumn(SmallAccessory::getAccessoryType).setCaption("Type");
        this.display_accessories.addColumn(SmallAccessory::getQuantity).setCaption("Qty");
        this.display_accessories.addColumn(SmallAccessory::getQuantityMinus).setCaption("Qty Taken");
        this.display_accessories.addColumn(SmallAccessory::getQuantityRemaining).setCaption("Qty Left");
        this.display_accessories.setHeight("300px");
        this.display_accessories.setWidth("1000px");
        
        if (user.equals("Admin")) {
            this.display_accessories.asSingleSelect().addValueChangeListener(event -> {
                if (event.getValue() == null) {
                    this.acc_form.setVisible(false);
                } else {
                    this.acc_form.setAccessory((SmallAccessory)event.getValue());
                }
            });
        }
    }

    public void refreshView() {
        int status = this.manager.connect();
        List<SmallAccessory> parts = new ArrayList<>();
        if (status == 1) {
            parts = this.constructor.constructSmallAccessories(this.manager);
            this.display_accessories.setItems(parts);
        } else {
            this.display_accessories.removeAllColumns();
        }
        status = this.manager.disconnect();
    }

    public void clearSelection() {
        this.display_accessories.getSelectionModel().deselectAll();
    }

    private void updateList() {
        List<SmallAccessory> computers = new ArrayList<>();
        
        this.manager.connect();
        computers = this.constructor.constructSmallAccessories(this.manager, this.filter.getValue());
        this.manager.disconnect();
        
        this.display_accessories.setItems(computers);
    }
}

