package com.example.rmonitor.content;

import com.example.rmonitor.classes.Accessory;
import com.example.rmonitor.classes.Computer;
import com.example.rmonitor.classes.ConnectionManager;
import com.example.rmonitor.classes.Delivery;
import com.example.rmonitor.classes.HTMLGenerator;
import com.example.rmonitor.classes.ObjectConstructor;
import com.example.rmonitor.classes.Parts;
import com.example.rmonitor.classes.PullOutForm;
import com.example.rmonitor.classes.SmallAccessory;
import com.example.rmonitor.content.PullForm;
import com.example.rmonitor.content.PullView;
import com.example.rmonitor.layouts.PullFormLayout;
import com.vaadin.server.BrowserWindowOpener;
import com.vaadin.server.ExternalResource;
import com.vaadin.server.RequestHandler;
import com.vaadin.server.Resource;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinResponse;
import com.vaadin.server.VaadinServlet;
import com.vaadin.server.VaadinSession;
import com.vaadin.shared.ui.ContentMode;
import com.vaadin.ui.AbstractComponent;
import com.vaadin.ui.Component;
import com.vaadin.ui.Grid;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Notification;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.renderers.ComponentRenderer;

import java.io.IOException;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.joda.time.DateTime;
import org.vaadin.dialogs.ConfirmDialog;


public class PullForm
extends PullFormLayout {
    ConnectionManager manager;
    ObjectConstructor constructor;
    int current_delivery;
    String current_po;
    PullView pull_view;
    PullOutForm po_form;
    List<Accessory> acc_for_pull;
    List<Computer> comp_for_pull;
    List<SmallAccessory> perp_for_pull;
    List<String> po_data;

    public PullForm(PullView pull_view) {
        this.manager = new ConnectionManager();
        this.constructor = new ObjectConstructor();
        this.acc_for_pull = new ArrayList<>();
        this.comp_for_pull = new ArrayList<>();
        this.perp_for_pull = new ArrayList<>();
        this.po_data = new ArrayList<>();
        this.pull_view = pull_view;
        this.delivery_comp.setSelectionMode(Grid.SelectionMode.MULTI);
        this.delivery_acc.setSelectionMode(Grid.SelectionMode.MULTI);
        this.delivery_per.setSelectionMode(Grid.SelectionMode.MULTI);
        this.prepared_comp.setSelectionMode(Grid.SelectionMode.MULTI);
        this.prepared_acc.setSelectionMode(Grid.SelectionMode.MULTI);
        this.prepared_per.setSelectionMode(Grid.SelectionMode.MULTI);
        VerticalLayout layout = new VerticalLayout();
        VerticalLayout search = new VerticalLayout(
        		new Component[]{this.delv_label, this.back, 
        				new HorizontalLayout(new Component[]{this.delv_num, this.get_delivery}), 
        				new HorizontalLayout(new Component[]{this.select_po, this.create_po})});
        this.prepare_grid();
        this.prepare_buttons();
        layout.addComponents(new Component[]{search, this.display});
        layout.setSizeUndefined();
        search.setSizeUndefined();
        this.display.setSizeUndefined();
        search.setMargin(false);
        this.display.setMargin(false);
        this.setContent((Component)layout);
        this.setHeight("700px");
        this.setWidth("1125px");
        this.select_po.setPlaceholder("Enter a new Pull-out Form #");
        this.select_po.setVisible(false);
        this.create_po.setVisible(false);
        this.display.setVisible(false);
    }

    private void prepare_buttons() {
        this.create_po.addClickListener(e -> {
            if (this.select_po.getValue() == "") {
                Notification.show((String)"Error", (String)"Please enter a non-empty name", (Notification.Type)Notification.Type.ERROR_MESSAGE);
                return;
            }
            this.manager.connect();
            boolean foo = this.constructor.isPOFormExisting(this.manager, this.current_delivery, this.select_po.getValue());
            this.manager.disconnect();
            if (foo) {
                Notification.show((String)"Error", (String)"That Number already exists", (Notification.Type)Notification.Type.ERROR_MESSAGE);
            } else {
                this.current_po = this.select_po.getValue();
                String query = String.format("InsertNewPullOut\r\n%s\r\n%s\r\n%s\r\n%s", this.current_delivery, this.current_po, new Date(DateTime.now().getMillis()), "Ongoing");
                this.manager.connect();
                this.manager.send(query);
                this.manager.disconnect();
                this.manager.connect();
                foo = this.constructor.isPOFormExisting(this.manager, this.current_delivery, this.select_po.getValue());
                this.manager.disconnect();
                if (foo) {
                    this.select_po.setReadOnly(true);
                    this.create_po.setEnabled(false);
                    this.refresh_grids();
                } else {
                    Notification.show((String)"Error", (String)"Something went wrong trying to update the database. Please contact the developer for assistance.", (Notification.Type)Notification.Type.ERROR_MESSAGE);
                }
            }
        });
        
        this.get_delivery.addClickListener(e -> {
            try {
                if (this.delv_num.getValue() == "") {
                    Notification.show((String)"Error", (String)"Please enter numbers only", (Notification.Type)Notification.Type.ERROR_MESSAGE);
                    return;
                }
                this.current_delivery = Integer.parseInt(this.delv_num.getValue());
                this.manager.connect();
                Boolean foo = this.constructor.isDeliveryExisting(this.manager, String.valueOf(this.current_delivery));
                this.manager.disconnect();
                if (foo.booleanValue()) {
                    this.select_po.setVisible(true);
                    this.create_po.setVisible(true);
                    this.delv_num.setReadOnly(true);
                    this.get_delivery.setEnabled(false);
                } else {
                    Notification.show((String)"Error", (String)"That Delivery does not exist", (Notification.Type)Notification.Type.ERROR_MESSAGE);
                }
            }
            catch (NumberFormatException ex) {
                Notification.show((String)"Error", (String)"Please enter numbers only", (Notification.Type)Notification.Type.ERROR_MESSAGE);
            }
        });
        
        this.back.addClickListener(e -> this.back());
        
        this.generate_first.addClickListener(e -> this.prepare_full_report());
        
        this.prepare_comp.addClickListener(e -> ConfirmDialog.show(
        		(UI)this.getUI(), (String)"Confirmation", (String)"Mark for Pull-out?", (String)"OK", (String)"Cancel", 
        		new ConfirmDialog.Listener() {
		        	public void onClose(ConfirmDialog dialog) {
		        		if (dialog.isConfirmed()) {
		        			setComputerPending();
		        		}
		        		else {
		        			
		        		}
		        	}
        		}
        	)
        );
        
        this.prepare_acc.addClickListener(e -> ConfirmDialog.show((UI)this.getUI(), 
        		(String)"Confirmation", (String)"Mark this Accessory for Pull-out?", (String)"OK", (String)"Cancel", 
        		new ConfirmDialog.Listener() {
		        	public void onClose(ConfirmDialog dialog) {
		        		if (dialog.isConfirmed()) {
		        			setAccessoryPending();
		        		}
		        		else {
		        			
		        		}
		        	}
        		}
        	)
        );
        
        this.prepare_per.addClickListener(e -> ConfirmDialog.show((UI)this.getUI(), 
        		(String)"Confirmation", (String)"Mark this Accessory for Pull-out?", (String)"OK", (String)"Cancel", 
        		new ConfirmDialog.Listener() {
		        	public void onClose(ConfirmDialog dialog) {
		        		if (dialog.isConfirmed()) {
		        			setPeripheralPending();
		        		}
		        		else {
		        			
		        		}
		        	}
        		}
        	)
        );
        
        this.return_comp.addClickListener(e -> ConfirmDialog.show((UI)this.getUI(), 
        		(String)"Confirmation", (String)"Unmark this Unit from Pull-out?", (String)"Yes", (String)"No", 
        		new ConfirmDialog.Listener() {
		        	public void onClose(ConfirmDialog dialog) {
		        		if (dialog.isConfirmed()) {
		        			unmarkComputer();
		        		}
		        		else {
		        			
		        		}
		        	}
		        }
        	)
        );
        
        this.return_acc.addClickListener(e -> ConfirmDialog.show((UI)this.getUI(), 
        		(String)"Confirmation", (String)"Unmark this Accessory from Pull-out?", (String)"Yes", (String)"No", 
        		new ConfirmDialog.Listener() {
		        	public void onClose(ConfirmDialog dialog) {
		        		if (dialog.isConfirmed()) {
		        			unmarkAccessory();
		        		}
		        		else {
		        			
		        		}
		        	}
				}
        	)
        );
        
        this.return_per.addClickListener(e -> ConfirmDialog.show((UI)this.getUI(), 
        		(String)"Confirmation", (String)"Unmark this Accessory from Pull-out?", (String)"Yes", (String)"No", 
        		new ConfirmDialog.Listener() {
		        	public void onClose(ConfirmDialog dialog) {
		        		if (dialog.isConfirmed()) {
		        			unmarkPeripheral();
		        		}
		        		else {
		        			
		        		}
		        	}
				}
        	)
        );
        
        this.delete.addClickListener(e -> ConfirmDialog.show((UI)this.getUI(), 
        		(String)"Confirmation", (String)"Mark this Pull-out as Returned?", (String)"Yes", (String)"No", 
        		new ConfirmDialog.Listener() {
		        	public void onClose(ConfirmDialog dialog) {
		        		if (dialog.isConfirmed()) {
		        			returnDelivery();
		        		}
		        		else {
		        			
		        		}
		        	}
				}
        	)
        );
        
        this.delete_form.addClickListener(e -> ConfirmDialog.show((UI)this.getUI(), 
        		(String)"Confirmation", (String)"Delete this Pull-out?", (String)"Yes", (String)"No", 
        		new ConfirmDialog.Listener() {
		        	public void onClose(ConfirmDialog dialog) {
		        		if (dialog.isConfirmed()) {
		        			deleteForm();
		        		}
		        		else {
		        			
		        		}
		        	}
        		}
        	)
        );
    }

    public void setPullOut(PullOutForm po_form) {
        int deliveryID = po_form.getDeliveryID();
        String formNumber = po_form.getFormNumber();
        this.po_form = po_form;
        this.delv_num.setValue(String.valueOf(deliveryID));
        this.delv_num.setReadOnly(true);
        this.current_delivery = deliveryID;
        this.get_delivery.setEnabled(false);
        this.select_po.setVisible(true);
        this.create_po.setVisible(true);
        this.select_po.setValue(formNumber);
        this.select_po.setReadOnly(true);
        this.current_po = formNumber;
        this.create_po.setEnabled(false);
        this.acc_for_pull = new ArrayList<>();
        this.comp_for_pull = new ArrayList<>();
        this.perp_for_pull = new ArrayList<>();
        this.prepare_html();
        this.refresh_grids();
    }

    private void prepare_html() {
        String servletPath = VaadinServlet.getCurrent().getServletContext().getContextPath();
        ExternalResource resource = new ExternalResource(String.valueOf(servletPath) + "/pullform.html");
        BrowserWindowOpener bwo = new BrowserWindowOpener((Resource)resource);
        bwo.setWindowName("Rental Form for Delivery " + this.current_delivery);
        String param = "delivery_id";
        bwo.setParameter(param, String.valueOf(this.current_delivery));
        bwo.setFeatures("height=600,width=800,resizeable");
        bwo.extend((AbstractComponent)this.generate);
        System.out.println("TRACE BrowserWindowOpener URL: " + bwo.getUrl());
        
        VaadinSession.getCurrent().addRequestHandler(
        		new RequestHandler() {
        			
        			@Override
                    public boolean handleRequest ( VaadinSession session ,
                                                   VaadinRequest request ,
                                                   VaadinResponse response )
                            throws IOException {
        				if ("/pullform.html".equals(request.getPathInfo())) {
        					//String uuidString = request.getParameter( "delivery_id" );  // In real-work, validate the results here.
                            //UUID uuid = UUID.fromString( uuidString ); // Reconstitute a `UUID` object from that hex-string. In real-work, validate the results here.
                            
                            HTMLGenerator generator = new HTMLGenerator();
                            ConnectionManager manager = new ConnectionManager();
                            ObjectConstructor constructor = new ObjectConstructor();
                            
                            manager.connect();
                            Delivery d = constructor.findDelivery(manager, String.valueOf(current_delivery));
                            manager.disconnect();
                            
                            // Build HTML.
                            String html = generator.generate_pull_print_html(d, 
                            		comp_for_pull, acc_for_pull, perp_for_pull,
                            		current_po);
                            
                            // Send out the generated text as HTML, in UTF-8 character encoding.
                            response.setContentType( "text/html; charset=utf-8" );
                            response.getWriter().append( html );
        					return true;
        				}
        				else return false;
        			}
        		}
        );
    }

    private void prepare_full_report() {
        String servletPath = VaadinServlet.getCurrent().getServletContext().getContextPath();
        ExternalResource resource = new ExternalResource(String.valueOf(servletPath) + "/pullform_full.html");
        BrowserWindowOpener bwo = new BrowserWindowOpener((Resource)resource);
        bwo.setWindowName("Rental Form for Delivery " + this.current_delivery);
        String param = "delivery_id";
        bwo.setParameter(param, String.valueOf(this.current_delivery));
        bwo.setFeatures("height=600,width=800,resizeable");
        bwo.extend((AbstractComponent)this.generate_first);
        System.out.println("TRACE BrowserWindowOpener URL: " + bwo.getUrl());
        
        VaadinSession.getCurrent().addRequestHandler(
        		new RequestHandler() {
        			
        			@Override
                    public boolean handleRequest ( VaadinSession session ,
                                                   VaadinRequest request ,
                                                   VaadinResponse response )
                            throws IOException {
        				if ("/pullform_full.html".equals(request.getPathInfo())) {
        					//String uuidString = request.getParameter( "delivery_id" );  // In real-work, validate the results here.
                            //UUID uuid = UUID.fromString( uuidString ); // Reconstitute a `UUID` object from that hex-string. In real-work, validate the results here.
                            
                            HTMLGenerator generator = new HTMLGenerator();
                            ConnectionManager manager = new ConnectionManager();
                            ObjectConstructor constructor = new ObjectConstructor();
                            
                            manager.connect();
                            Delivery d = constructor.findDelivery(manager, String.valueOf(current_delivery));
                            manager.disconnect();
                            
                            // Build HTML.
                            String html = generator.generate_delivery_print_html(d);
                            
                            // Send out the generated text as HTML, in UTF-8 character encoding.
                            response.setContentType( "text/html; charset=utf-8" );
                            response.getWriter().append( html );
        					return true;
        				}
        				else return false;
        			}
        		}
        );
    }

    private void prepare_grid() {
        this.delivery_comp.setRowHeight(300.0);
        this.delivery_comp.setHeaderRowHeight(-1.0);
        this.delivery_comp.addColumn(Computer::getRentalNumber).setCaption("Rental Asset#");
        this.delivery_comp.addColumn(parts -> {
            Label label = new Label(this.generate_parts_label(parts), ContentMode.PREFORMATTED);
            return label;
        }, new ComponentRenderer()).setCaption("Parts");
        this.delivery_comp.addColumn(Computer::getPriceStr).setCaption("Price");
        this.delivery_acc.addColumn(Accessory::getRentalNumber).setCaption("Rental Asset#");
        this.delivery_acc.addColumn(Accessory::getName).setCaption("Name");
        this.delivery_acc.addColumn(Accessory::getAccessoryType).setCaption("Type");
        this.delivery_per.addColumn(SmallAccessory::getName).setCaption("Name");
        this.delivery_per.addColumn(SmallAccessory::getQuantity).setCaption("Quantity");
        this.prepared_comp.setRowHeight(300.0);
        this.prepared_comp.setHeaderRowHeight(-1.0);
        this.prepared_comp.addColumn(Computer::getRentalNumber).setCaption("Rental Asset#");
        this.prepared_comp.addColumn(parts -> {
            Label label = new Label(this.generate_parts_label(parts), ContentMode.PREFORMATTED);
            return label;
        }, new ComponentRenderer()).setCaption("Parts");
        this.prepared_comp.addColumn(Computer::getPriceStr).setCaption("Price");
        this.prepared_acc.addColumn(Accessory::getRentalNumber).setCaption("Rental Asset#");
        this.prepared_acc.addColumn(Accessory::getName).setCaption("Name");
        this.prepared_acc.addColumn(Accessory::getAccessoryType).setCaption("Type");
        this.prepared_per.addColumn(SmallAccessory::getName).setCaption("Name");
        this.prepared_per.addColumn(SmallAccessory::getQuantity).setCaption("Quantity");
    }

    private void returnDelivery() {
        String query = String.format("ReturnDelivery\r\n%s\r\n%s", this.current_delivery, this.current_po);
        this.manager.connect();
        String computers = this.manager.send(query);
        this.manager.disconnect();
        
        List<Computer> foo = new ArrayList<>();
        this.manager.connect();
        foo = this.constructor.fetchActiveCompleteComputers(this.manager, this.current_delivery);
        this.manager.disconnect();
        
        List<Accessory> bar = new ArrayList<>();
        this.manager.connect();
        bar = this.constructor.fetchActiveDeliveryAccessories(this.manager, this.current_delivery);
        this.manager.disconnect();
        
        if (foo.isEmpty() && bar.isEmpty()) {
            this.manager.connect();
            query = String.format("CompleteDelivery\r\n%s", this.current_delivery);
            computers = this.manager.send(query);
            this.manager.disconnect();
        }
        
        this.back();
        Notification.show((String)"Return Delivery", (String)computers, (Notification.Type)Notification.Type.HUMANIZED_MESSAGE);
    }

    private void deleteForm() {
        String result;
        String query;
        
        List<Computer> foo3 = new ArrayList<>();
        this.manager.connect();
        foo3 = this.constructor.fetchPendingComputers(this.manager, this.current_delivery, this.current_po);
        this.manager.disconnect();
        
        List<Accessory> bar3 = new ArrayList<>();
        this.manager.connect();
        bar3 = this.constructor.fetchPendingDeliveryAccessories(this.manager, this.current_delivery, this.current_po);
        this.manager.disconnect();
        
        for (Computer delivery : foo3) {
            this.manager.connect();
            query = String.format("SetRentalUnavailable\r\n%s", delivery.getRentalNumber());
            result = this.manager.send(query);
            this.manager.disconnect();
            System.out.println("return_comp: " + result);
            query = String.format("DeletePullOutListItem\r\n%s\r\n%s\r\n%s", this.current_delivery, this.current_po, delivery.getRentalNumber());
            this.manager.connect();
            result = this.manager.send(query);
            this.manager.disconnect();
            System.out.println("return_comp: " + result);
        }
        
        for (Accessory acc : bar3) {
            query = String.format("SetRentalUnavailable\r\n%s", acc.getRentalNumber());
            this.manager.connect();
            result = this.manager.send(query);
            this.manager.disconnect();
            System.out.println("return_acc: " + result);
            query = String.format("DeletePullOutListItem\r\n%s\r\n%s\r\n%s", this.current_delivery, this.current_po, acc.getRentalNumber());
            this.manager.connect();
            result = this.manager.send(query);
            this.manager.disconnect();
            System.out.println("return_acc: " + result);
        }
        
        this.manager.connect();
        String query2 = String.format("DeletePullOutList\r\n%s\r\n%s", this.current_delivery, this.current_po);
        String computers = this.manager.send(query2);
        this.manager.disconnect();
        
        System.out.println("DeletePullOutList: " + computers);
        
        this.manager.connect();
        query2 = String.format("DeletePullOutPeripherals\r\n%s\r\n%s", this.current_delivery, this.current_po);
        computers = this.manager.send(query2);
        this.manager.disconnect();
        
        System.out.println("DeletePullOut: " + computers);
        
        this.manager.connect();
        query2 = String.format("DeletePullOut\r\n%s\r\n%s", this.current_delivery, this.current_po);
        computers = this.manager.send(query2);
        this.manager.disconnect();
        
        System.out.println("DeletePullOut: " + computers);
        
        this.back();
        Notification.show((String)"Delete Pull-out", (String)computers, (Notification.Type)Notification.Type.HUMANIZED_MESSAGE);
    }
    
    private void setComputerPending() {
    	if (delivery_comp.asMultiSelect().getSelectedItems().isEmpty()) {
    		Notification.show("Result", "You didn't select anything.", Notification.Type.HUMANIZED_MESSAGE);
    		return;
    	}
    	
    	String result = "";
    	for (Computer c : delivery_comp.asMultiSelect().getSelectedItems()) {
    		String query = String.format("SetRentalPending\r\n%s", c.getRentalNumber());
    		manager.connect();
    		result = manager.send(query);
    		manager.disconnect();
    		
    		query = String.format("InsertPullOutListItem\r\n%s\r\n%s\r\n%s", 
    				current_delivery,
    				current_po,
    				c.getRentalNumber());
    		manager.connect();
    		result = manager.send(query);
    		manager.disconnect();
    	}
    	
    	refresh_grids();
    	Notification.show("Result", result, Notification.Type.HUMANIZED_MESSAGE);
    }
    
    private void setAccessoryPending() {
    	if (delivery_acc.asMultiSelect().getSelectedItems().isEmpty()) {
    		Notification.show("Result", "You didn't select anything.", Notification.Type.HUMANIZED_MESSAGE);
    		return;
    	}
    	
    	String result = "";
    	for (Accessory c : delivery_acc.asMultiSelect().getSelectedItems()) {
    		String query = String.format("SetRentalPending\r\n%s", c.getRentalNumber());
    		manager.connect();
    		result = manager.send(query);
    		manager.disconnect();
    		
    		query = String.format("InsertPullOutListItem\r\n%s\r\n%s\r\n%s", 
    				current_delivery,
    				current_po,
    				c.getRentalNumber());
    		manager.connect();
    		result = manager.send(query);
    		manager.disconnect();
    	}
    	
    	refresh_grids();
    	Notification.show("Result", result, Notification.Type.HUMANIZED_MESSAGE);
    }
    
    private void setPeripheralPending() {
    	if (delivery_per.asMultiSelect().getSelectedItems().isEmpty()) {
    		Notification.show("Result", "You didn't select anything.", Notification.Type.HUMANIZED_MESSAGE);
    		return;
    	}
    	
    	String result = "";
    	for (SmallAccessory c : delivery_per.asMultiSelect().getSelectedItems()) {
    		String query = String.format("SetPeripheralPending\r\n%s\r\n%s\r\n%s\r\n%s",
    				current_delivery,
    				current_po,
    				c.getName(),
    				c.getQuantity());
    		manager.connect();
    		result=manager.send(query);
    		manager.disconnect();
    	}
    	
    	refresh_grids();
    	Notification.show("Result", result, Notification.Type.HUMANIZED_MESSAGE);
    }
    
    private void unmarkComputer() {
    	if (prepared_comp.asMultiSelect().getSelectedItems().isEmpty()) {
    		Notification.show("Result", "You didn't select anything.", Notification.Type.HUMANIZED_MESSAGE);
    		return;
    	}
    	
    	String result = "";
    	for (Computer c : prepared_comp.asMultiSelect().getSelectedItems()) {
    		String query = String.format("SetRentalUnavailable\r\n%s", c.getRentalNumber());
    		manager.connect();
    		result=manager.send(query);
    		manager.disconnect();
    		
    		query = String.format("DeletePullOutListItem\r\n%s\r\n%s\r\n%s", 
    				current_delivery,
    				current_po,
    				c.getRentalNumber());
    		manager.connect();
    		result=manager.send(query);
    		manager.disconnect();
    	}
    	
    	refresh_grids();
    	Notification.show("Result", result, Notification.Type.HUMANIZED_MESSAGE);
    }
    
    private void unmarkAccessory() {
    	if (prepared_acc.asMultiSelect().getSelectedItems().isEmpty()) {
    		Notification.show("Result", "You didn't select anything.", Notification.Type.HUMANIZED_MESSAGE);
    		return;
    	}
    	
    	String result = "";
    	for (Accessory c : prepared_acc.asMultiSelect().getSelectedItems()) {
    		String query = String.format("SetRentalUnavailable\r\n%s", c.getRentalNumber());
    		manager.connect();
    		result=manager.send(query);
    		manager.disconnect();
    		
    		query = String.format("DeletePullOutListItem\r\n%s\r\n%s\r\n%s", 
    				current_delivery,
    				current_po,
    				c.getRentalNumber());
    		manager.connect();
    		result=manager.send(query);
    		manager.disconnect();
    	}
    	
    	refresh_grids();
    	Notification.show("Result", result, Notification.Type.HUMANIZED_MESSAGE);
    }
    
    private void unmarkPeripheral() {
    	if (prepared_per.asMultiSelect().getSelectedItems().isEmpty()) {
    		Notification.show("Result", "You didn't select anything.", Notification.Type.HUMANIZED_MESSAGE);
    		return;
    	}
    	
    	String result = "";
    	for (SmallAccessory c : prepared_per.asMultiSelect().getSelectedItems()) {
    		String query = String.format("DeletePullOutPeripheralsItem\r\n%s\r\n%s\r\n%s",
    				current_delivery,
    				current_po,
    				c.getName());
    		manager.connect();
    		result=manager.send(query);
    		manager.disconnect();
    	}
    	
    	refresh_grids();
    	Notification.show("Result", result, Notification.Type.HUMANIZED_MESSAGE);
    }

    private String generate_parts_label(Computer c) {
        StringBuilder label = new StringBuilder();
        for (int i = 0; i < c.getPartIDs().size(); ++i) {
            this.manager.connect();
            String result = ((Parts)this.constructor.fetchParts(this.manager, ((Integer)c.getPartIDs().get(i)).intValue()).get(0)).getStringRepresentation();
            this.manager.disconnect();
            label.append(result);
            label.append("\r\n");
        }
        return label.toString();
    }

    private List<String> extract_form_numbers(List<PullOutForm> list) {
        ArrayList<String> parsed_data = new ArrayList<String>();
        for (PullOutForm form : list) {
            parsed_data.add(form.getFormNumber());
        }
        return parsed_data;
    }
    
    /***
     * Updates all grids on the Pull-out form.
     */
    private void refresh_grids() {
        refreshComputers();
        refreshAccessories();
        refreshPeripherals();
        
        this.display.setVisible(true);
    }
    
    /***
     * Updates the Computer grids on the Pull-out form.
     */
    private void refreshComputers() {
    	List<Computer> foo = new ArrayList<>();
        this.manager.connect();
        foo = this.constructor.fetchActiveCompleteComputers(this.manager, this.current_delivery);
        this.manager.disconnect();
        
        List<Computer> foo3 = new ArrayList<>();
        this.manager.connect();
        foo3 = this.constructor.fetchPendingComputers(this.manager, this.current_delivery, this.current_po);
        this.manager.disconnect();
        
        foo.removeAll(foo3);
        
        this.delivery_comp.setItems(foo);
        this.prepared_comp.setItems(foo3);
        this.comp_for_pull = foo3;
    }
    
    /***
     * Updates the Accessory grids on the Pull-out form.
     */
    private void refreshAccessories() {
    	List<Accessory> bar = new ArrayList<>();
        this.manager.connect();
        bar = this.constructor.fetchActiveDeliveryAccessories(this.manager, this.current_delivery);
        this.manager.disconnect();
        
        List<Accessory> bar3 = new ArrayList<>();
        this.manager.connect();
        bar3 = this.constructor.fetchPendingDeliveryAccessories(this.manager, this.current_delivery, this.current_po);
        this.manager.disconnect();
        
        bar = removeAccessoryIntersection(bar, bar3);
        
        this.delivery_acc.setItems(bar);
        this.prepared_acc.setItems(bar3);
        this.acc_for_pull = bar3;
    }
    
    /***
     * Updates all the SmallAccessory grids on the Pull-out form.
     */
    private void refreshPeripherals() {
    	List<SmallAccessory> perp = new ArrayList<>();
        this.manager.connect();
        perp = this.constructor.fetchDeliveryPeripherals(this.manager, this.current_delivery);
        this.manager.disconnect();
        
        List<SmallAccessory> pending_perp = new ArrayList<>();
        this.manager.connect();
        pending_perp = this.constructor.fetchPendingPeripherals(this.manager, this.current_delivery, this.current_po);
        this.manager.disconnect();
        
        perp = removePeripheralIntersection(perp, pending_perp);
        
        this.delivery_per.setItems(perp);
        this.prepared_per.setItems(pending_perp);
        this.perp_for_pull = pending_perp;
    }
    
    /***
     * Removes the intersection of the two lists from the first supplied list.
     * @param target - the list from which to remove from
     * @param fodder - the list containing the Accessories to remove
     */
    private List<Accessory> removeAccessoryIntersection(List<Accessory> target, List<Accessory> fodder) {
    	List<Accessory> result = target;
    	List<Accessory> dummy = new ArrayList<>();
    	
    	for (Accessory c : target) {
    		for (Accessory d : fodder) {
    			if (d.getRentalNumber().equals(c.getRentalNumber())) {
    				dummy.add(c);
    				break;
    			}
    		}
    	}
    	
    	result.removeAll(dummy);
    	return result;
    }
    
    /***
     * Removes the intersection of the two lists from the first supplied list.
     * @param target - the list from which to remove from
     * @param fodder - the list containing the SmallAccessories to remove
     */
    private List<SmallAccessory> removePeripheralIntersection(List<SmallAccessory> target, List<SmallAccessory> fodder) {
    	List<SmallAccessory> result = target;
    	List<SmallAccessory> dummy = new ArrayList<>();
    	
    	for (SmallAccessory c : target) {
    		for (SmallAccessory d : fodder) {
    			if (d.getName().equals(c.getName())) {
    				dummy.add(c);
    				break;
    			}
    		}
    	}
    	
    	result.removeAll(dummy);
    	return result;
    }

    private void back() {
        this.setVisible(false);
        this.pull_view.refreshView();
    }
}

