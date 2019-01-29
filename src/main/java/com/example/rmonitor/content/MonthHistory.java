/*
 * Decompiled with CFR 0.139.
 * 
 * Could not load the following classes:
 *  com.example.rmonitor.classes.ConnectionManager
 *  com.example.rmonitor.classes.Delivery
 *  com.example.rmonitor.classes.ObjectConstructor
 *  com.example.rmonitor.content.DeliveryPrint
 *  com.example.rmonitor.content.MonthHistory
 *  com.vaadin.data.HasValue
 *  com.vaadin.data.HasValue$ValueChangeEvent
 *  com.vaadin.data.HasValue$ValueChangeListener
 *  com.vaadin.data.ValueProvider
 *  com.vaadin.shared.Registration
 *  com.vaadin.ui.Component
 *  com.vaadin.ui.Grid
 *  com.vaadin.ui.Grid$Column
 *  com.vaadin.ui.HorizontalLayout
 *  com.vaadin.ui.Panel
 *  com.vaadin.ui.SingleSelect
 */
package com.example.rmonitor.content;

import com.example.rmonitor.classes.ConnectionManager;
import com.example.rmonitor.classes.Delivery;
import com.example.rmonitor.classes.ObjectConstructor;
import com.example.rmonitor.content.DeliveryPrint;
import com.vaadin.ui.Component;
import com.vaadin.ui.Grid;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Panel;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class MonthHistory
extends Panel {
    ConnectionManager manager = new ConnectionManager();
    ObjectConstructor constructor = new ObjectConstructor();
    Grid<Delivery> grid = new Grid<>();
    DeliveryPrint delivery_list;
    HorizontalLayout layout = new HorizontalLayout(new Component[]{this.grid});

    public MonthHistory(LocalDate filter) {
        this.prepare_grid(filter);
        this.grid.setSizeUndefined();
        this.layout.setSizeUndefined();
        this.setContent((Component)this.layout);
    }

    private void prepare_grid(LocalDate filter) {
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
        deliveries = this.constructor.constructMonthDeliveries(this.manager, filter.getMonthValue(), filter.getYear());
        this.manager.disconnect();
        this.grid.setItems(deliveries);
    }
}

