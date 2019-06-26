package com.example.rmonitor.content;

import com.example.rmonitor.classes.Client;
import com.example.rmonitor.classes.ConnectionManager;
import com.example.rmonitor.classes.Delivery;
import com.example.rmonitor.classes.ObjectConstructor;
import com.example.rmonitor.content.DeliveryPrint;
import com.vaadin.ui.Component;
import com.vaadin.ui.Grid;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Panel;
import com.vaadin.ui.VerticalLayout;
import java.util.ArrayList;
import java.util.List;

public class ClientHistory
extends Panel {
    ConnectionManager manager = new ConnectionManager();
    ObjectConstructor constructor = new ObjectConstructor();
    Grid<Delivery> grid = new Grid<>();
    DeliveryPrint delivery_list;
    HorizontalLayout layout = new HorizontalLayout(new Component[]{this.grid});
    VerticalLayout main;

    /***
     * Prepares a list of Deliveries ordered by the input Client.
     * @param c - any Client
     */
    public ClientHistory(Client c) {
        this.prepare_grid(c);
        this.main = new VerticalLayout(new Component[]{this.prepare_labels(c), this.layout});
        this.grid.setSizeUndefined();
        this.layout.setSizeUndefined();
        this.main.setSizeUndefined();
        this.setContent((Component)this.main);
    }

    /***
     * Formats the grid to be used in this module.
     * @param c
     */
    private void prepare_grid(Client c) {
        this.grid.addColumn(Delivery::getDeliveryID).setCaption("DeliveryID");
        this.grid.addColumn(Delivery::getReleaseDate).setCaption("Release Date");
        this.grid.addColumn(Delivery::getDueDate).setCaption("Due Date");
        this.grid.asSingleSelect().addValueChangeListener(e -> {
            if (e.getValue() == null) {
                if (this.delivery_list != null) {
                    this.delivery_list.setVisible(false);
                }
            } else {
                if (this.delivery_list != null) {
                    this.layout.removeComponent((Component)this.delivery_list);
                }
                this.delivery_list = new DeliveryPrint((Delivery)e.getValue());
                this.layout.addComponent((Component)this.delivery_list);
            }
        });
        List<Delivery> foo = new ArrayList<>();
        this.manager.connect();
        foo = this.constructor.constructClientDeliveries(this.manager, c.getName());
        this.manager.disconnect();
        this.grid.setItems(foo);
    }

    /***
     * Prepares the labels that will display Client info.
     * @param c - the Client entered in the initialization
     * @return a VerticalLayout containing Labels with the Client's information
     */
    private VerticalLayout prepare_labels(Client c) {
        VerticalLayout foo = new VerticalLayout();
        Label name = new Label(String.format("Client: %s", c.getName()));
        Label address = new Label(String.format("Address: %s", c.getAddress()));
        Label contact_num = new Label(String.format("Contact#: %s", c.getContact_number()));
        Label contactp = new Label(String.format("Contact: %s", c.getContact_person()));
        foo.addComponents(new Component[]{new HorizontalLayout(new Component[]{name, address}), new HorizontalLayout(new Component[]{contact_num, contactp})});
        foo.setSizeUndefined();
        return foo;
    }
}

