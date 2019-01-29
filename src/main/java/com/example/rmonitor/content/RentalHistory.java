package com.example.rmonitor.content;

import com.example.rmonitor.classes.Computer;
import com.example.rmonitor.classes.ConnectionManager;
import com.example.rmonitor.classes.Delivery;
import com.example.rmonitor.classes.ObjectConstructor;
import com.example.rmonitor.content.DeliveryPrint;
import com.vaadin.ui.Component;
import com.vaadin.ui.Grid;
import com.vaadin.ui.Label;
import com.vaadin.ui.Panel;
import com.vaadin.ui.VerticalLayout;
import java.util.ArrayList;
import java.util.List;

public class RentalHistory
extends Panel {
    ConnectionManager manager = new ConnectionManager();
    ObjectConstructor constructor = new ObjectConstructor();
    Grid<Delivery> grid = new Grid<>();
    DeliveryPrint delivery_list;
    Label count = new Label("test");
    VerticalLayout layout = new VerticalLayout(new Component[]{this.count, this.grid});

    public RentalHistory(Computer c) {
        this.prepare_grid(c);
        this.grid.setSizeUndefined();
        this.layout.setSizeUndefined();
        this.setContent((Component)this.layout);
    }

    private void prepare_grid(Computer c) {
        this.grid.addColumn(Delivery::getDeliveryID).setCaption("DeliveryID");
        this.grid.addColumn(Delivery::getcustomerName).setCaption("Client");
        this.grid.addColumn(Delivery::getReleaseDate).setCaption("Release Date");
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
        List<Delivery> deliveries = new ArrayList<>();
        
        this.manager.connect();
        deliveries = this.constructor.constructRentalUnitHistory(this.manager, c.getRentalNumber());
        this.manager.disconnect();
        
        this.grid.setItems(deliveries);
        System.out.println(deliveries.size());
        
        this.count.setValue(String.format("This unit has been delivered %d times", deliveries.size()));
    }
}

