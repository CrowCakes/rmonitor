/*
 * Decompiled with CFR 0.139.
 * 
 * Could not load the following classes:
 *  com.example.rmonitor.classes.ConnectionManager
 *  com.example.rmonitor.classes.ObjectConstructor
 *  com.example.rmonitor.classes.Parts
 *  com.example.rmonitor.content.PartsForm
 *  com.example.rmonitor.content.PartsView
 *  com.example.rmonitor.content.PartsView$1
 *  com.vaadin.data.HasValue
 *  com.vaadin.data.HasValue$ValueChangeEvent
 *  com.vaadin.data.HasValue$ValueChangeListener
 *  com.vaadin.data.ValueProvider
 *  com.vaadin.navigator.View
 *  com.vaadin.shared.Registration
 *  com.vaadin.shared.ui.ValueChangeMode
 *  com.vaadin.ui.Alignment
 *  com.vaadin.ui.Button
 *  com.vaadin.ui.Button$ClickEvent
 *  com.vaadin.ui.Button$ClickListener
 *  com.vaadin.ui.Component
 *  com.vaadin.ui.CssLayout
 *  com.vaadin.ui.Grid
 *  com.vaadin.ui.Grid$Column
 *  com.vaadin.ui.HorizontalLayout
 *  com.vaadin.ui.Panel
 *  com.vaadin.ui.SingleSelect
 *  com.vaadin.ui.TextField
 *  com.vaadin.ui.VerticalLayout
 *  com.vaadin.ui.components.grid.GridSelectionModel
 */
package com.example.rmonitor.content;

import com.example.rmonitor.classes.ConnectionManager;
import com.example.rmonitor.classes.ObjectConstructor;
import com.example.rmonitor.classes.Parts;
import com.example.rmonitor.content.PartsForm;
import com.example.rmonitor.content.PartsView;
import com.vaadin.navigator.View;
import com.vaadin.shared.ui.ValueChangeMode;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Component;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.Grid;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Panel;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import java.util.ArrayList;
import java.util.List;

/*
 * Exception performing whole class analysis.
 */
public class PartsView
extends CssLayout
implements View {
    public static final String VIEW_NAME = "Parts";
    ConnectionManager manager;
    ObjectConstructor constructor;
    Grid<Parts> display_parts;
    private PartsForm parts_form;
    final HorizontalLayout button_row;
    final HorizontalLayout main;
    final VerticalLayout layout;
    final Panel panel;
    TextField filter;
    int latestIncrement;

    public PartsView(String user) {
        this.manager = null;
        this.constructor = null;
        this.parts_form = new PartsForm(this);
        this.manager = new ConnectionManager();
        this.constructor = new ObjectConstructor();
        this.layout = new VerticalLayout();
        this.main = new HorizontalLayout();
        this.panel = new Panel();
        this.setSizeFull();
        this.addStyleName("parts-view");
        this.prepare_grid(user);
        this.button_row = this.buttonsLayout(user);
        this.layout.addComponents(new Component[]{this.button_row, this.display_parts});
        this.main.addComponents(new Component[]{this.layout, this.parts_form});
        this.main.setComponentAlignment((Component)this.parts_form, Alignment.MIDDLE_RIGHT);
        this.panel.setContent((Component)this.main);
        this.panel.setHeight("700px");
        this.panel.setWidth("1075px");
        this.addComponent((Component)this.panel);
        this.main.setExpandRatio((Component)this.layout, 0.75f);
        this.main.setExpandRatio((Component)this.parts_form, 0.25f);
        this.refreshView();
    }

    private void prepare_grid(String user) {
        this.display_parts = new Grid<>();
        this.display_parts.addColumn(Parts::getPartID).setCaption("PartID");
        this.display_parts.addColumn(Parts::getName).setCaption("Name");
        this.display_parts.addColumn(Parts::getPartType).setCaption("Type");
        this.display_parts.addColumn(Parts::getStatus).setCaption("Status");
        this.display_parts.addColumn(Parts::getParent).setCaption("Current Unit");
        this.display_parts.setHeight("600px");
        this.display_parts.setWidth("750px");
        
        if (user.equals("Admin")) {
            this.display_parts.asSingleSelect().addValueChangeListener(event -> {
                if (event.getValue() == null) {
                    this.parts_form.setVisible(false);
                } else {
                    this.parts_form.setParts((Parts)event.getValue());
                }
            });
        }
    }

    private HorizontalLayout buttonsLayout(String user) {
        HorizontalLayout row = new HorizontalLayout();
        this.filter = new TextField();
        this.filter.setPlaceholder("Filter by PartID");
        this.filter.addValueChangeListener(e -> this.updateList());
        this.filter.setValueChangeMode(ValueChangeMode.LAZY);
        
        Button ViewParts = new Button("Refresh");
        ViewParts.addClickListener(e -> this.refreshView());
        
        Button addPart = new Button("Create New Part");
        addPart.addClickListener(e -> {
            this.clearSelection();
            this.parts_form.setParts(new Parts(0, "", "", "", 0.0f));
        });
        if (!user.equals("Admin")) {
            addPart.setEnabled(false);
        }
        
        Button quit = new Button("Kill Server");
        quit.addClickListener(e -> {
            
        });
        
        row.addComponents(new Component[]{this.filter, ViewParts, addPart});
        return row;
    }

    private void updateList() {
    	List<Parts> computers = new ArrayList<>();
        this.manager.connect();
        try {
            computers = this.constructor.filterParts(this.manager, this.filter.getValue());
            this.manager.disconnect();
            this.display_parts.setItems(computers);
        }
        catch (NumberFormatException ex) {
            this.manager.disconnect();
        }
    }

    public void clearSelection() {
        this.display_parts.getSelectionModel().deselectAll();
    }

    public void refreshView() {
        List<Parts> parts = new ArrayList<>();
        
        this.manager.connect();
        parts = this.constructor.constructParts(this.manager);
        this.manager.disconnect();
        
        for (Parts x : parts) {
            String query = String.format("TraceComputer\r\n%s", x.getPartID());
            
            this.manager.connect();
            String parent = this.manager.send(query).trim();
            this.manager.disconnect();
            
            x.setParent(parent);
        }
        this.display_parts.setItems(parts);
    }

    public int returnLatestIncrement() {
        return this.latestIncrement;
    }
}

