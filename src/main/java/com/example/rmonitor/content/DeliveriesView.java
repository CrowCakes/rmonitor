package com.example.rmonitor.content;

import com.example.rmonitor.classes.Accessory;
import com.example.rmonitor.classes.Computer;
import com.example.rmonitor.classes.ConnectionManager;
import com.example.rmonitor.classes.Delivery;
import com.example.rmonitor.classes.ObjectConstructor;
import com.example.rmonitor.content.DeliveriesView;
import com.example.rmonitor.content.DeliveryForm;
import com.vaadin.navigator.View;
import com.vaadin.shared.ui.ValueChangeMode;
import com.vaadin.ui.Button;
import com.vaadin.ui.Component;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.Grid;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import java.sql.Date;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class DeliveriesView
extends CssLayout
implements View {
    public static final String VIEW_NAME = "Deliveries";
    ConnectionManager manager;
    ObjectConstructor constructor;
    private DeliveryForm delv_form;
    final HorizontalLayout button_row;
    final HorizontalLayout grid_row;
    final VerticalLayout layout;
    final HorizontalLayout main;
    Grid<Delivery> display_deliveries;
    TextField filter;
    
    Label text = new Label("");
    int latestIncrement;
    int MAX_LIMIT = 20;
    int offset = 0;
    int limit = MAX_LIMIT;
    int count = 0;

    /***
     * Constructs the module used to view Deliveries.
     * @param user - string denoting the user's role
     */
    public DeliveriesView(String user) {
    	limit = MAX_LIMIT;
        this.manager = null;
        this.constructor = null;
        this.manager = new ConnectionManager();
        this.constructor = new ObjectConstructor();
        this.layout = new VerticalLayout();
        this.main = new HorizontalLayout();
        this.delv_form = new DeliveryForm(this);
        this.setSizeFull();
        this.addStyleName("delv-view");
        this.grid_row = this.gridLayout(user);
        this.button_row = this.buttonsLayout(user);
        this.layout.addComponents(new Component[]{this.button_row, this.grid_row, fetchNextBatch()});
        this.main.addComponents(new Component[]{this.layout});
        if (!user.equals("Viewer")) {
            this.main.addComponents(new Component[]{this.delv_form});
        }
        this.delv_form.setVisible(false);
        this.addComponents(new Component[]{this.main});
        this.resetView();
    }

    /***
     * Constructs the grid that will display the list of Deliveries.
     * @param user - string denoting the user's role
     * @return a HorizontalLayout containing the grid
     */
    private HorizontalLayout gridLayout(String user) {
        HorizontalLayout grid_row = new HorizontalLayout();
        this.display_deliveries = new Grid<>();
        this.display_deliveries.addColumn(Delivery::getDeliveryID).setCaption("DelvID");
        this.display_deliveries.addColumn(Delivery::getcustomerName).setCaption("Client");
        this.display_deliveries.addColumn(Delivery::getReleaseDate).setCaption("Released");
        this.display_deliveries.addColumn(Delivery::getDueDate).setCaption("Due");
        this.display_deliveries.addColumn(Delivery::getFrequencyStr).setCaption("MOF");
        this.display_deliveries.addColumn(Delivery::getStatus).setCaption("Status");
        this.display_deliveries.addColumn(Delivery::getExtensionID).setCaption("Extended to?");
        this.display_deliveries.addColumn(Delivery::getTotalPrice).setCaption("Total Price");
        this.display_deliveries.addColumn(Delivery::getNumberOfUnits).setCaption("# of Units");
        
        display_deliveries.setFrozenColumnCount(1);
        this.display_deliveries.setHeight("500px");
        this.display_deliveries.setWidth("1000px");
        this.display_deliveries.setSelectionMode(Grid.SelectionMode.SINGLE);
        
        if (user.equals("Admin")) {
            this.display_deliveries.asSingleSelect().addValueChangeListener(event -> {
                if (event.getValue() == null) {
                    this.delv_form.setVisible(false);
                    this.layout.setVisible(true);
                } else {
                    this.layout.setVisible(false);
                    this.delv_form.setDelivery((Delivery)event.getValue());
                }
            });
        }
        grid_row.addComponents(new Component[]{this.display_deliveries});
        return grid_row;
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
     * Constructs the buttons that will query the database.
     * @param user - string denoting the user's role
     * @return a HorizontalLayout containing the buttons ready to use
     */
    private HorizontalLayout buttonsLayout(String user) {
        HorizontalLayout row = new HorizontalLayout();
        this.filter = new TextField();
        this.filter.setPlaceholder("Filter by DeliveryID");
        this.filter.addValueChangeListener(e -> {
        	if (!filter.getValue().isEmpty()) this.updateList(); 
        	else this.resetView();
        });
        this.filter.setValueChangeMode(ValueChangeMode.LAZY);
        
        Button ViewDeliveries = new Button("Refresh");
        ViewDeliveries.addClickListener(e -> this.resetView());
        
        Button quit = new Button("Kill Server");
        quit.addClickListener(e -> {
            
        });
        
        Button addDelivery = new Button("Add New");
        addDelivery.addClickListener(e -> {
            this.clearSelection();
            this.layout.setVisible(false);
            this.delv_form.setDelivery(new Delivery(0, "", "", "", "", "", 
            		Date.valueOf(LocalDate.now()), 
            		Date.valueOf(LocalDate.now()), "", "Active", 0, 1));
        });
        if (!user.equals("Admin")) {
            addDelivery.setEnabled(false);
        }
        
        Button viewExtended = new Button("Extended");
        viewExtended.addClickListener(e -> {
            List<Delivery> parts = new ArrayList<>();
            this.manager.connect();
            parts = this.constructor.constructExtendedDeliveries(this.manager);
            this.manager.disconnect();
            this.display_deliveries.setItems(parts);
        });
        
        Button viewActive = new Button("Active");
        viewActive.addClickListener(e -> {
            List<Delivery> parts = new ArrayList<>();
            this.manager.connect();
            parts = this.constructor.constructActiveDeliveries(this.manager);
            this.manager.disconnect();
            this.display_deliveries.setItems(parts);
        });
        
        Button viewUrgent = new Button("Expiring");
        viewUrgent.addClickListener(e -> {
            List<Delivery> parts = new ArrayList<>();
            this.manager.connect();
            parts = this.constructor.constructUrgentDeliveries(this.manager);
            this.manager.disconnect();
            this.display_deliveries.setItems(parts);
        });
        
        row.addComponents(new Component[]{this.filter, ViewDeliveries, addDelivery, viewExtended, viewActive, viewUrgent});
        return row;
    }

    /***
     * Deselects the module's grid selections.
     */
    private void clearSelection() {
        this.display_deliveries.getSelectionModel().deselectAll();
    }

    /***
     * Fetches the latest list of Deliveries from the database based on the filter input.
     */
    private void updateList() {
        List<Delivery> parts = new ArrayList<>();
        this.manager.connect();
        parts = DeliveriesView.isInteger((String)this.filter.getValue()) ? this.constructor.constructDeliveries(this.manager, Integer.parseInt(this.filter.getValue())) : this.constructor.constructDeliveries(this.manager, this.filter.getValue());
        this.manager.disconnect();
        
        // get the total price of the Delivery order
        for (Delivery x : parts) {
            List<Computer> foo = new ArrayList<>();
            List<Accessory> bar = new ArrayList<>();
            float comp_total = 0.0f;
            float acc_total = 0.0f;
            if (x.isPersisted()) {
                this.manager.connect();
                foo = this.constructor.fetchCompleteComputers(this.manager, x.getDeliveryID());
                this.manager.disconnect();
                this.manager.connect();
                bar = this.constructor.fetchDeliveryAccessories(this.manager, x.getDeliveryID());
                this.manager.disconnect();
            }
            for (Computer y : foo) {
                comp_total += y.getPrice();
            }
            for (Accessory z : bar) {
                acc_total += z.getPrice();
            }
            System.out.println(String.format("DeliveiesView: D#%s value = %s", x.getDeliveryIDStr(), Float.valueOf(comp_total + acc_total)));
            x.updateTotalPrice(comp_total + acc_total);
            x.setNumberOfUnits(foo.size());
        }
        
        this.display_deliveries.setItems(parts);
    }

    /***
     * Refreshes the list of Deliveries.
     */
    public void resetView() {
        List<Delivery> parts = new ArrayList<>();
        offset = 0;

        manager.connect();
        count = constructor.getDeliveryCount(manager);
        manager.disconnect();

        limit = (offset + limit > count) ? count - offset : limit;
        
        this.manager.connect();
        parts = this.constructor.constructDeliveries(this.manager, offset, limit);
        this.manager.disconnect();
        
        // get the total price of the Delivery order
        for (Delivery x : parts) {
            List<Computer> foo = new ArrayList<>();
            List<Accessory> bar = new ArrayList<>();
            float comp_total = 0.0f;
            float acc_total = 0.0f;
            if (x.isPersisted()) {
                this.manager.connect();
                foo = this.constructor.fetchCompleteComputers(this.manager, x.getDeliveryID());
                this.manager.disconnect();
                this.manager.connect();
                bar = this.constructor.fetchDeliveryAccessories(this.manager, x.getDeliveryID());
                this.manager.disconnect();
            }
            for (Computer y : foo) {
                comp_total += y.getPrice();
            }
            for (Accessory z : bar) {
                acc_total += z.getPrice();
            }
            //System.out.println(String.format("DeliveiesView: D#%s value = %s", x.getDeliveryIDStr(), Float.valueOf(comp_total + acc_total)));
            x.updateTotalPrice(comp_total + acc_total);
            x.setNumberOfUnits(foo.size());
        }
        this.display_deliveries.setItems(parts);
        text.setValue(String.format("%d-%d of %d", offset, offset+limit, count));
        limit = MAX_LIMIT;
        
        this.layout.setVisible(true);
    }
    
    private void displayNew(int offset, int limit) {
    	List<Delivery> parts = new ArrayList<>();
        
        this.manager.connect();
        parts = this.constructor.constructDeliveries(this.manager, offset, limit);
        this.manager.disconnect();
        
        // get the total price of the Delivery order
        for (Delivery x : parts) {
            List<Computer> foo = new ArrayList<>();
            List<Accessory> bar = new ArrayList<>();
            float comp_total = 0.0f;
            float acc_total = 0.0f;
            if (x.isPersisted()) {
                this.manager.connect();
                foo = this.constructor.fetchCompleteComputers(this.manager, x.getDeliveryID());
                this.manager.disconnect();
                this.manager.connect();
                bar = this.constructor.fetchDeliveryAccessories(this.manager, x.getDeliveryID());
                this.manager.disconnect();
            }
            for (Computer y : foo) {
                comp_total += y.getPrice();
            }
            for (Accessory z : bar) {
                acc_total += z.getPrice();
            }
            //System.out.println(String.format("DeliveiesView: D#%s value = %s", x.getDeliveryIDStr(), Float.valueOf(comp_total + acc_total)));
            x.updateTotalPrice(comp_total + acc_total);
            x.setNumberOfUnits(foo.size());
        }
        this.display_deliveries.setItems(parts);
        text.setValue(String.format("%d-%d of %d", offset, offset+limit, count));
    }

    public int returnLatestIncrement() {
        return this.latestIncrement;
    }

    /***
     * Check if the input string is a representation of an integer.
     * @param x - any string
     * @return True if the string is a representation of an integer, False otherwise
     */
    private static boolean isInteger(String x) {
        if (x == null) {
            return false;
        }
        if (x.isEmpty()) {
            return false;
        }
        int neg = 0;
        if (x.charAt(0) == '-') {
            if (x.length() == 1) {
                return false;
            }
            neg = 1;
        }
        while (neg < x.length()) {
            char c = x.charAt(neg);
            if (c < '0' || c > '9') {
                return false;
            }
            ++neg;
        }
        return true;
    }
}

