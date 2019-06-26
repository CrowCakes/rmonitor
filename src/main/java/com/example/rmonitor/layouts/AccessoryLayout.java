/*
 * Decompiled with CFR 0.139.
 * 
 * Could not load the following classes:
 *  com.example.rmonitor.classes.Accessory
 *  com.example.rmonitor.classes.ConnectionManager
 *  com.example.rmonitor.classes.ObjectConstructor
 *  com.example.rmonitor.content.AccessoryForm
 *  com.example.rmonitor.layouts.AccessoryLayout
 *  com.vaadin.data.HasValue
 *  com.vaadin.data.HasValue$ValueChangeEvent
 *  com.vaadin.data.HasValue$ValueChangeListener
 *  com.vaadin.data.ValueProvider
 *  com.vaadin.shared.Registration
 *  com.vaadin.shared.ui.ValueChangeMode
 *  com.vaadin.ui.Alignment
 *  com.vaadin.ui.Button
 *  com.vaadin.ui.Button$ClickEvent
 *  com.vaadin.ui.Button$ClickListener
 *  com.vaadin.ui.Component
 *  com.vaadin.ui.Grid
 *  com.vaadin.ui.Grid$Column
 *  com.vaadin.ui.HorizontalLayout
 *  com.vaadin.ui.SingleSelect
 *  com.vaadin.ui.TextField
 *  com.vaadin.ui.VerticalLayout
 *  com.vaadin.ui.components.grid.GridSelectionModel
 */
package com.example.rmonitor.layouts;

import com.example.rmonitor.classes.Accessory;
import com.example.rmonitor.classes.ConnectionManager;
import com.example.rmonitor.classes.ObjectConstructor;
import com.example.rmonitor.content.AccessoryForm;
import com.vaadin.shared.ui.ValueChangeMode;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Component;
import com.vaadin.ui.Grid;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import java.util.ArrayList;
import java.util.List;

public class AccessoryLayout
extends VerticalLayout {
    ConnectionManager manager = new ConnectionManager();
    ObjectConstructor constructor = new ObjectConstructor();
    final HorizontalLayout button_row;
    final VerticalLayout layout = new VerticalLayout();
    final HorizontalLayout main = new HorizontalLayout();
    Grid<Accessory> display_accessories;
    private AccessoryForm acc_form = new AccessoryForm(this);
    TextField filter;

    public AccessoryLayout() {
        this.setSizeFull();
        this.addStyleName("acc-view");
        this.prepare_grid();
        this.button_row = this.buttonsLayout();
        this.layout.addComponents(new Component[]{this.button_row, this.display_accessories});
        this.main.addComponents(new Component[]{this.layout, this.acc_form});
        this.main.setComponentAlignment((Component)this.acc_form, Alignment.MIDDLE_RIGHT);
        this.addComponents(new Component[]{this.main});
        this.setSizeUndefined();
    }

    private void prepare_grid() {
        this.display_accessories = new Grid<>();
        this.display_accessories.addColumn(Accessory::getRentalNumber).setCaption("Rental Asset#");
        this.display_accessories.addColumn(Accessory::getName).setCaption("Name");
        this.display_accessories.addColumn(Accessory::getAccessoryType).setCaption("Type");
        this.display_accessories.addColumn(Accessory::getStatus).setCaption("Status");
        this.display_accessories.addColumn(Accessory::getPrice).setCaption("Price");
        this.display_accessories.setHeight("600px");
        this.display_accessories.setWidth("750px");
        this.display_accessories.asSingleSelect().addValueChangeListener(event -> {
            if (event.getValue() == null) {
                this.acc_form.setVisible(false);
            } else {
                this.acc_form.setAccessory((Accessory)event.getValue());
            }
        });
    }

    private HorizontalLayout buttonsLayout() {
        HorizontalLayout row = new HorizontalLayout();
        this.filter = new TextField();
        this.filter.setPlaceholder("Filter by Rental Number");
        this.filter.addValueChangeListener(e -> this.updateList());
        this.filter.setValueChangeMode(ValueChangeMode.LAZY);
        
        Button ViewAccessories = new Button("View Accessories");
        ViewAccessories.addClickListener(e -> this.refreshView());
        
        Button addAcc = new Button("Create New Accessory");
        addAcc.addClickListener(e -> {
            this.clearSelection();
            this.acc_form.setAccessory(new Accessory("", "", "", "", "n/a", 0.0f));
        });
        
        Button quit = new Button("Kill Server");
        quit.addClickListener(e -> {
            
        });
        
        row.addComponents(new Component[]{this.filter, ViewAccessories, addAcc});
        return row;
    }

    private void updateList() {
        List<Accessory> computers = new ArrayList<>();
        this.manager.connect();
        computers = this.constructor.constructAccessories(this.manager, this.filter.getValue());
        this.manager.disconnect();
        this.display_accessories.setItems(computers);
    }

    public void clearSelection() {
        this.display_accessories.getSelectionModel().deselectAll();
    }

    public void refreshView() {
        int status = this.manager.connect();
        List<Accessory> parts = new ArrayList<>();
        if (status == 1) {
            parts = this.constructor.constructAccessories(this.manager);
            this.display_accessories.setItems(parts);
        } else {
            this.display_accessories.removeAllColumns();
        }
        status = this.manager.disconnect();
    }
}

