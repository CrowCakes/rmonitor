package com.example.rmonitor.content;

import com.example.rmonitor.classes.Client;
import com.example.rmonitor.classes.Computer;
import com.example.rmonitor.classes.ConnectionManager;
import com.example.rmonitor.classes.Delivery;
import com.example.rmonitor.classes.ObjectConstructor;
import com.example.rmonitor.content.ClientHistory;
import com.example.rmonitor.content.DeliveryPrint;
import com.example.rmonitor.content.MonthHistory;
import com.example.rmonitor.content.ProjectionForm;
import com.example.rmonitor.content.RentalHistory;
import com.example.rmonitor.layouts.MainPageLayout;
import com.vaadin.ui.Component;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Panel;
import com.vaadin.ui.TabSheet;
import com.vaadin.ui.VerticalLayout;
import java.time.LocalDate;

public class MainPage
extends MainPageLayout {
    ConnectionManager manager = new ConnectionManager();
    ObjectConstructor constructor = new ObjectConstructor();

    public MainPage(String user) {
        this.setSizeFull();
        this.addStyleName("main-page");
        if (!user.equals("Viewer")) {
            TabSheet tabsheet = new TabSheet();
            this.addComponent((Component)new VerticalLayout(new Component[]{tabsheet}));
            tabsheet.addTab((Component)this.urgent, "Deliveries Due");
            tabsheet.addTab((Component)this.prepare_rental_ui(), "Rental Form");
            tabsheet.addTab((Component)this.prepare_client_history_ui(), "Client History");
            tabsheet.addTab((Component)this.prepare_month_ui(), "Month History");
            tabsheet.addTab((Component)this.prepare_rental_history_ui(), "Rental Asset History");
            tabsheet.addTab((Component)this.prepare_projection_ui(), "Projection");
            tabsheet.setSizeUndefined();
            tabsheet.setHeight("650px");
            tabsheet.setWidth("1050px");
        } else {
            this.addComponent((Component)this.layout);
        }
    }

    private Panel prepare_rental_ui() {
        Panel panel = new Panel();
        this.delivery_search.addClickListener(e -> {
            if (!this.delivery_filter.getValue().isEmpty()) {
                this.delivery_search(this.delivery_filter.getValue());
            } else {
                Notification.show((String)"Error", (String)"Please input a valid Delivery ID", (Notification.Type)Notification.Type.ERROR_MESSAGE);
            }
        });
        this.foo.setSizeUndefined();
        this.bar.setSizeUndefined();
        panel.setContent((Component)this.bar);
        panel.setSizeUndefined();
        return panel;
    }

    private Panel prepare_client_history_ui() {
        Panel panel = new Panel();
        this.client_search.addClickListener(e -> {
            if (!this.client_filter.getValue().isEmpty()) {
                this.client_search(this.client_filter.getValue());
            } else {
                Notification.show((String)"Error", (String)"Please input a valid Client name", (Notification.Type)Notification.Type.ERROR_MESSAGE);
            }
        });
        this.client_history_search.setSizeUndefined();
        this.client_history_layout.setSizeUndefined();
        
        manager.connect();
        client_filter.setItems(constructor.constructClientNames(manager));
        manager.disconnect();
        
        panel.setContent((Component)this.client_history_layout);
        panel.setSizeUndefined();
        return panel;
    }

    private Panel prepare_month_ui() {
        Panel panel = new Panel();
        this.date_search.addClickListener(e -> {
            if (!((LocalDate)this.date_select.getValue()).toString().isEmpty() || this.date_select.getValue() == null) {
                this.month_search((LocalDate)this.date_select.getValue());
            } else {
                Notification.show((String)"Error", (String)"Please input a valid date", (Notification.Type)Notification.Type.ERROR_MESSAGE);
            }
        });
        this.month_history_search.setSizeUndefined();
        this.month_history_layout.setSizeUndefined();
        this.date_select.setValue(LocalDate.now());
        panel.setContent((Component)this.month_history_layout);
        panel.setSizeUndefined();
        return panel;
    }

    private Panel prepare_rental_history_ui() {
        Panel panel = new Panel();
        this.rental_search.addClickListener(e -> {
            if (!this.rental_filter.getValue().isEmpty()) {
                this.rental_search(this.rental_filter.getValue());
            } else {
                Notification.show((String)"Error", (String)"Please input a valid Rental#", (Notification.Type)Notification.Type.ERROR_MESSAGE);
            }
        });
        this.rental_history_search.setSizeUndefined();
        this.rental_history_layout.setSizeUndefined();
        panel.setContent((Component)this.rental_history_layout);
        panel.setSizeUndefined();
        return panel;
    }

    private Panel prepare_projection_ui() {
        Panel panel = new Panel();
        this.projection_search.addClickListener(e -> {
            if (!((LocalDate)this.projection_date.getValue()).toString().isEmpty()) {
                this.projection_search((LocalDate)this.projection_date.getValue());
            } else {
                Notification.show((String)"Error", (String)"Please input a valid date", (Notification.Type)Notification.Type.ERROR_MESSAGE);
            }
        });
        this.projection_buttons.setSizeUndefined();
        this.projection_layout.setSizeUndefined();
        this.projection_date.setValue(LocalDate.now());
        panel.setContent((Component)this.projection_layout);
        panel.setSizeUndefined();
        return panel;
    }

    private void delivery_search(String delv_id) {
        try {
            this.bar.removeComponent(this.bar.getComponent(2));
        }
        catch (IndexOutOfBoundsException indexOutOfBoundsException) {
            // empty catch block
        }
        
        this.manager.connect();
        Delivery d = this.constructor.findDelivery(this.manager, delv_id);
        this.manager.disconnect();
        
        try {
            this.rental_form = new DeliveryPrint(d);
            this.rental_form.setHeight("500px");
            this.rental_form.setWidth("1000px");
            this.bar.addComponent((Component)this.rental_form);
        }
        catch (NullPointerException ex) {
            ex.printStackTrace();
            Notification.show((String)"Error", (String)"No such delivery with that ID was found", (Notification.Type)Notification.Type.ERROR_MESSAGE);
        }
    }

    private void client_search(String client_name) {
        try {
            this.client_history_layout.removeComponent(this.client_history_layout.getComponent(2));
        }
        catch (IndexOutOfBoundsException indexOutOfBoundsException) {
            // empty catch block
        }
        
        this.manager.connect();
        Client c = this.constructor.findClient(this.manager, client_name);
        this.manager.disconnect();
        
        try {
            this.client_history_view = new ClientHistory(c);
            this.client_history_view.setHeight("500px");
            this.client_history_view.setWidth("1000px");
            this.client_history_layout.addComponent((Component)this.client_history_view);
        }
        catch (NullPointerException ex) {
            ex.printStackTrace();
            Notification.show((String)"Error", (String)"No such client with that name was found", (Notification.Type)Notification.Type.ERROR_MESSAGE);
        }
    }

    private void month_search(LocalDate date) {
        try {
            this.month_history_layout.removeComponent(this.month_history_layout.getComponent(2));
        }
        catch (IndexOutOfBoundsException indexOutOfBoundsException) {
            // empty catch block
        }
        
        try {
            this.month_history_view = new MonthHistory(date);
            this.month_history_view.setHeight("500px");
            this.month_history_view.setWidth("1000px");
            this.month_history_layout.addComponent((Component)this.month_history_view);
        }
        catch (NullPointerException ex) {
            ex.printStackTrace();
            Notification.show((String)"Error", (String)"I actually don't know what went wrong here. Please call the developer to report this bug.", (Notification.Type)Notification.Type.ERROR_MESSAGE);
        }
    }

    private void rental_search(String rental_number) {
        try {
            this.rental_history_layout.removeComponent(this.rental_history_layout.getComponent(2));
        }
        catch (IndexOutOfBoundsException indexOutOfBoundsException) {
            // empty catch block
        }
        
        //this.manager.connect();
        //Computer c = this.constructor.findComputer(this.manager, rental_number);
        //this.manager.disconnect();
        
        try {
            this.rental_history_view = new RentalHistory(rental_number);
            this.rental_history_view.setHeight("500px");
            this.rental_history_view.setWidth("1000px");
            this.rental_history_layout.addComponent((Component)this.rental_history_view);
        }
        catch (NullPointerException ex) {
            ex.printStackTrace();
            Notification.show((String)"Error", (String)"No such rental unit was found", (Notification.Type)Notification.Type.ERROR_MESSAGE);
        }
    }

    private void projection_search(LocalDate date) {
        try {
            this.projection_layout.removeComponent(this.projection_layout.getComponent(2));
        }
        catch (IndexOutOfBoundsException indexOutOfBoundsException) {
            // empty catch block
        }
        
        try {
            this.projection_view = new ProjectionForm(date);
            this.projection_view.setSizeUndefined();
            this.projection_layout.addComponent((Component)this.projection_view);
        }
        catch (NullPointerException ex) {
            ex.printStackTrace();
            Notification.show((String)"Error", (String)"I actually don't know what went wrong here. Please call the developer to report this bug.", (Notification.Type)Notification.Type.ERROR_MESSAGE);
        }
    }
}

