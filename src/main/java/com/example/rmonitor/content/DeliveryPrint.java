package com.example.rmonitor.content;

import com.example.rmonitor.classes.Accessory;
import com.example.rmonitor.classes.Computer;
import com.example.rmonitor.classes.ConnectionManager;
import com.example.rmonitor.classes.Delivery;
import com.example.rmonitor.classes.HTMLGenerator;
import com.example.rmonitor.classes.ObjectConstructor;
import com.example.rmonitor.classes.Parts;
import com.example.rmonitor.classes.SmallAccessory;
import com.example.rmonitor.content.DeliveryPrint;
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
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Component;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Panel;
import com.vaadin.ui.VerticalLayout;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class DeliveryPrint
extends Panel {
    ConnectionManager manager;
    ObjectConstructor constructor;
    Label client;
    Label contact;
    Label delivery_date;
    Label acct_manager;
    Label so;
    Label ard;
    Label due_date;
    Label expanding_gap;
    Label rental_units;
    Label accessories;
    HorizontalLayout people;
    VerticalLayout left_side;
    VerticalLayout so_ard_due;
    HorizontalLayout details;
    HorizontalLayout units;
    HorizontalLayout accs;
    HorizontalLayout peripherals;
    Button print;
    VerticalLayout main;

    public DeliveryPrint(Delivery d) {
        this.manager = new ConnectionManager();
        this.constructor = new ObjectConstructor();
        this.client = new Label();
        this.contact = new Label();
        this.delivery_date = new Label();
        this.acct_manager = new Label();
        this.so = new Label();
        this.ard = new Label();
        this.due_date = new Label();
        this.expanding_gap = new Label();
        this.rental_units = new Label("<b>Rental Units</b>", ContentMode.HTML);
        this.accessories = new Label("<b>Accessories</b>", ContentMode.HTML);
        this.people = new HorizontalLayout(new Component[]{this.client, this.expanding_gap, this.contact});
        this.left_side = new VerticalLayout(new Component[]{this.people, this.delivery_date, this.acct_manager});
        this.so_ard_due = new VerticalLayout(new Component[]{this.so, this.ard, this.due_date});
        this.details = new HorizontalLayout(new Component[]{this.left_side, this.so_ard_due});
        this.units = new HorizontalLayout();
        this.accs = new HorizontalLayout();
        this.peripherals = new HorizontalLayout();
        this.print = new Button("Print this form");
        this.main = new VerticalLayout(new Component[]{this.print, this.details, this.rental_units, this.units, this.accessories, this.accs, this.peripherals});
        this.prepare_labels(d);
        this.prepare_units(d);
        this.expanding_gap.setWidth("100%");
        this.people.setExpandRatio((Component)this.expanding_gap, 1.0f);
        this.main.setComponentAlignment((Component)this.rental_units, Alignment.MIDDLE_CENTER);
        this.main.addComponent((Component)new Label("Rental Form"), 1);
        this.main.setSizeUndefined();
        this.setContent((Component)this.main);
        
        this.print.addClickListener((Button.ClickListener & java.io.Serializable)e -> {});
        this.prepare_html(d.getDeliveryIDStr());
    }

    public DeliveryPrint(String delivery_id) {
        this.manager = new ConnectionManager();
        this.constructor = new ObjectConstructor();
        this.client = new Label();
        this.contact = new Label();
        this.delivery_date = new Label();
        this.acct_manager = new Label();
        this.so = new Label();
        this.ard = new Label();
        this.due_date = new Label();
        this.expanding_gap = new Label();
        this.rental_units = new Label("<b>Rental Units</b>", ContentMode.HTML);
        this.accessories = new Label("<b>Accessories</b>", ContentMode.HTML);
        this.people = new HorizontalLayout(new Component[]{this.client, this.expanding_gap, this.contact});
        this.left_side = new VerticalLayout(new Component[]{this.people, this.delivery_date, this.acct_manager});
        this.so_ard_due = new VerticalLayout(new Component[]{this.so, this.ard, this.due_date});
        this.details = new HorizontalLayout(new Component[]{this.left_side, this.so_ard_due});
        this.units = new HorizontalLayout();
        this.accs = new HorizontalLayout();
        this.peripherals = new HorizontalLayout();
        this.print = new Button("Print this form");
        this.main = new VerticalLayout(new Component[]{this.print, this.details, this.rental_units, this.units, this.accessories, this.accs, this.peripherals});
        this.manager.connect();
        Delivery d = this.constructor.findDelivery(this.manager, delivery_id);
        this.manager.disconnect();
        this.prepare_labels(d);
        this.prepare_pulled_units(d);
        this.expanding_gap.setWidth("100%");
        this.people.setExpandRatio((Component)this.expanding_gap, 1.0f);
        this.main.setComponentAlignment((Component)this.rental_units, Alignment.MIDDLE_CENTER);
        this.main.addComponent((Component)new Label("Pull-out Form"), 1);
        this.main.setSizeUndefined();
        this.setContent((Component)this.main);
        
        this.print.addClickListener((Button.ClickListener & java.io.Serializable)e -> {});
        this.prepare_html(delivery_id);
    }

    private void prepare_html(String delivery_id) {
        String servletPath = VaadinServlet.getCurrent().getServletContext().getContextPath();
        ExternalResource resource = new ExternalResource(String.valueOf(servletPath) + "/form.html");
        BrowserWindowOpener bwo = new BrowserWindowOpener((Resource)resource);
        bwo.setWindowName("Rental Form for Delivery " + delivery_id);
        String param = "delivery_id";
        bwo.setParameter(param, delivery_id);
        bwo.setFeatures("height=600,width=800,resizeable");
        bwo.extend((AbstractComponent)this.print);
        System.out.println("TRACE BrowserWindowOpener URL: " + bwo.getUrl());
        
        VaadinSession.getCurrent().addRequestHandler(
        		new RequestHandler() {
        			
        			@Override
                    public boolean handleRequest ( VaadinSession session ,
                                                   VaadinRequest request ,
                                                   VaadinResponse response )
                            throws IOException {
        				if ("/form.html".equals(request.getPathInfo())) {
        					//String uuidString = request.getParameter( "delivery_id" );  // In real-work, validate the results here.
                            //UUID uuid = UUID.fromString( uuidString ); // Reconstitute a `UUID` object from that hex-string. In real-work, validate the results here.
                            
                            HTMLGenerator generator = new HTMLGenerator();
                            ConnectionManager manager = new ConnectionManager();
                            ObjectConstructor constructor = new ObjectConstructor();
                            
                            manager.connect();
                            Delivery d = constructor.findDelivery(manager, delivery_id);
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

    private void prepare_labels(Delivery d) {
        this.client.setWidth("200px");
        this.client.setValue(String.format("Client: %s", d.getcustomerName()));
        this.contact.setValue(String.format("Contact: %s", this.find_contact(d.getcustomerName())));
        this.delivery_date.setValue(String.format("Delv. Date: %s", d.getReleaseDate()));
        this.acct_manager.setValue(String.format("AM: %s", d.getAccountManager()));
        this.so.setValue(String.format("SO# %s", d.getSO()));
        this.ard.setValue(String.format("ARD# %s", d.getARD()));
        this.due_date.setValue(String.format("Due Date: %s", d.getDueDate()));
        List<Computer> foo = new ArrayList<>();
        this.manager.connect();
        foo = this.constructor.fetchCompleteComputers(this.manager, d.getDeliveryID());
        this.manager.disconnect();
        float test_price = 0.0f;
        for (int i = 0; i < foo.size(); ++i) {
            test_price += ((Computer)foo.get(i)).getPrice();
        }
        System.out.println(String.format("Total Price: Php %s", Float.valueOf(test_price)));
    }

    private void prepare_units(Delivery d) {
        this.units.removeAllComponents();
        this.accs.removeAllComponents();
        this.peripherals.removeAllComponents();
        
        List<Computer> foo = new ArrayList<>();
        this.manager.connect();
        foo = this.constructor.fetchCompleteComputers(this.manager, d.getDeliveryID());
        this.manager.disconnect();
        
        VerticalLayout left = new VerticalLayout();
        VerticalLayout right = new VerticalLayout();
        left.setSizeUndefined();
        right.setSizeUndefined();
        for (int i = 0; i < foo.size(); ++i) {
            if (i % 2 == 0) {
                left.addComponent((Component)new Label(this.generate_parts_label((Computer)foo.get(i)), ContentMode.PREFORMATTED));
                continue;
            }
            right.addComponent((Component)new Label(this.generate_parts_label((Computer)foo.get(i)), ContentMode.PREFORMATTED));
        }
        
        List<Accessory> bar = new ArrayList<>();
        this.manager.connect();
        bar = this.constructor.fetchDeliveryAccessories(this.manager, d.getDeliveryID());
        this.manager.disconnect();
        VerticalLayout left_acc = new VerticalLayout();
        VerticalLayout right_acc = new VerticalLayout();
        for (int i = 0; i < bar.size(); ++i) {
            if (i % 2 == 0) {
                left_acc.addComponent((Component)new Label(String.format("%s - %s", ((Accessory)bar.get(i)).getRentalNumber(), ((Accessory)bar.get(i)).getName())));
                continue;
            }
            right_acc.addComponent((Component)new Label(String.format("%s - %s", ((Accessory)bar.get(i)).getRentalNumber(), ((Accessory)bar.get(i)).getName())));
        }
        
        List<SmallAccessory> small_acc = new ArrayList<>();
        this.manager.connect();
        small_acc = this.constructor.fetchDeliveryPeripherals(this.manager, d.getDeliveryID());
        this.manager.disconnect();
        VerticalLayout left_per = new VerticalLayout();
        VerticalLayout right_per = new VerticalLayout();
        for (int i = 0; i < small_acc.size(); ++i) {
            if (i % 2 == 0) {
                left_per.addComponent((Component)new Label(String.format("%s - %s", ((SmallAccessory)small_acc.get(i)).getName(), ((SmallAccessory)small_acc.get(i)).getQuantity())));
                continue;
            }
            right_per.addComponent((Component)new Label(String.format("%s - %s", ((SmallAccessory)small_acc.get(i)).getName(), ((SmallAccessory)small_acc.get(i)).getQuantity())));
        }
        
        this.units.addComponents(new Component[]{left, right});
        this.accs.addComponents(new Component[]{left_acc, right_acc});
        this.peripherals.addComponents(new Component[]{left_per, right_per});
    }

    private void prepare_pulled_units(Delivery d) {
        this.units.removeAllComponents();
        this.accs.removeAllComponents();
        
        List<Computer> foo = new ArrayList<>();
        this.manager.connect();
        foo = this.constructor.fetchPulledComputers(this.manager, d.getDeliveryID());
        this.manager.disconnect();
        
        VerticalLayout left = new VerticalLayout();
        VerticalLayout right = new VerticalLayout();
        left.setSizeUndefined();
        right.setSizeUndefined();
        for (int i = 0; i < foo.size(); ++i) {
            if (i % 2 == 0) {
                left.addComponent((Component)new Label(this.generate_parts_label((Computer)foo.get(i)), ContentMode.PREFORMATTED));
                continue;
            }
            right.addComponent((Component)new Label(this.generate_parts_label((Computer)foo.get(i)), ContentMode.PREFORMATTED));
        }
        
        List<Accessory> bar = new ArrayList<>();
        this.manager.connect();
        bar = this.constructor.fetchPulledDeliveryAccessories(this.manager, d.getDeliveryID());
        this.manager.disconnect();
        VerticalLayout left_acc = new VerticalLayout();
        VerticalLayout right_acc = new VerticalLayout();
        for (int i = 0; i < bar.size(); ++i) {
            if (i % 2 == 0) {
                left_acc.addComponent((Component)new Label(String.format("%s - %s", ((Accessory)bar.get(i)).getRentalNumber(), ((Accessory)bar.get(i)).getName())));
                continue;
            }
            right_acc.addComponent((Component)new Label(String.format("%s - %s", ((Accessory)bar.get(i)).getRentalNumber(), ((Accessory)bar.get(i)).getName())));
        }
        
        this.units.addComponents(new Component[]{left, right});
        this.accs.addComponents(new Component[]{left_acc, right_acc});
    }

    /***
     * Generates the list of Parts belonging to the input Computer.
     * @param c - any Computer
     * @return a formatted String containing the list of Parts belonging to the Computer
     */
    private String generate_parts_label(Computer c) {
        StringBuilder label = new StringBuilder();
        label.append(c.getRentalNumber());
        label.append("\r\n");
        for (int i = 0; i < c.getPartIDs().size(); ++i) {
            this.manager.connect();
            String result = ((Parts)this.constructor.fetchParts(this.manager, ((Integer)c.getPartIDs().get(i)).intValue()).get(0)).getStringRepresentation();
            this.manager.disconnect();
            label.append(result);
            label.append("\r\n");
        }
        return label.toString();
    }

    /***
     * Finds the name of the Contact Person under the Client name.
     * @param s - the Client's name
     * @return the name of the Client's Contact Person
     */
    private String find_contact(String s) {
        this.manager.connect();
        String query = String.format("FindClientContactPerson\r\n%s", s);
        String foo = this.manager.send(query);
        this.manager.disconnect();
        return foo.trim();
    }
}

