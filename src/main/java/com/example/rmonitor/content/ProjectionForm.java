/*
 * Decompiled with CFR 0.139.
 * 
 * Could not load the following classes:
 *  com.example.rmonitor.classes.Computer
 *  com.example.rmonitor.classes.ConnectionManager
 *  com.example.rmonitor.classes.ObjectConstructor
 *  com.example.rmonitor.content.ProjectionForm
 *  com.vaadin.data.ValueProvider
 *  com.vaadin.ui.Component
 *  com.vaadin.ui.Grid
 *  com.vaadin.ui.Grid$Column
 *  com.vaadin.ui.Label
 *  com.vaadin.ui.Panel
 *  com.vaadin.ui.VerticalLayout
 */
package com.example.rmonitor.content;

import com.example.rmonitor.classes.Computer;
import com.example.rmonitor.classes.ConnectionManager;
import com.example.rmonitor.classes.ObjectConstructor;
import com.vaadin.ui.Component;
import com.vaadin.ui.Grid;
import com.vaadin.ui.Label;
import com.vaadin.ui.Panel;
import com.vaadin.ui.VerticalLayout;
import java.time.LocalDate;
import java.util.List;

public class ProjectionForm
extends Panel {
    ConnectionManager manager = new ConnectionManager();
    ObjectConstructor constructor = new ObjectConstructor();
    Label display = new Label();
    Label display2 = new Label();
    Label display3 = new Label();
    Grid<Computer> rental_numbers = new Grid<>();

    public ProjectionForm(LocalDate date) {
        this.get_projection(date);
        this.rental_numbers.setSizeUndefined();
        this.setContent((Component)new VerticalLayout(new Component[]{this.display, this.display2, this.display3, this.rental_numbers}));
        this.setSizeUndefined();
    }

    private void get_projection(LocalDate date) {
        this.manager.connect();
        List<Computer> foo = this.constructor.makeProjection(this.manager, date);
        this.manager.disconnect();
        System.out.println(String.format("Projection: Incoming computers = %s", foo.size()));
        this.manager.connect();
        List<Computer> bar = this.constructor.findAvailableComputers(this.manager);
        this.manager.disconnect();
        System.out.println(String.format("Projection: On-hand computers = %s", bar.size()));
        String text = String.format("There will be %d rental unit(s) available before %s", foo.size() + bar.size(), date.toString());
        String text2 = String.format("ON HAND: %d rental unit(s)", bar.size());
        String text3 = String.format("TO BE RETURNED: %d rental unit(s)", foo.size());
        this.rental_numbers.addColumn(Computer::getRentalNumber).setCaption("Rental Asset#");
        this.rental_numbers.addColumn(Computer::getCpu).setCaption("CPU");
        this.rental_numbers.addColumn(Computer::getStatus).setCaption("Status");
        foo.addAll(bar);
        this.rental_numbers.setItems(foo);
        this.display.setValue(text);
        this.display2.setValue(text2);
        this.display3.setValue(text3);
    }
}

