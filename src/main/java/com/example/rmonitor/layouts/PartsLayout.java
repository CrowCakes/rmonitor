package com.example.rmonitor.layouts;

import com.example.rmonitor.classes.ConnectionManager;
import com.example.rmonitor.classes.ObjectConstructor;
import com.example.rmonitor.classes.Parts;
import com.example.rmonitor.content.PartsForm;
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

public class PartsLayout
extends VerticalLayout {
    ConnectionManager manager = new ConnectionManager();
    ObjectConstructor constructor = new ObjectConstructor();
    Grid<Parts> display_parts;
    private PartsForm parts_form = new PartsForm(this);
    final HorizontalLayout button_row;
    final HorizontalLayout main = new HorizontalLayout();
    final VerticalLayout layout = new VerticalLayout();
    TextField filter;

    public PartsLayout() {
        this.setSizeFull();
        this.addStyleName("parts-view");
        this.prepare_grid();
        this.button_row = this.buttonsLayout();
        this.layout.addComponents(new Component[]{this.button_row, this.display_parts});
        this.main.addComponents(new Component[]{this.layout, this.parts_form});
        this.main.setComponentAlignment((Component)this.parts_form, Alignment.MIDDLE_RIGHT);
        this.addComponent((Component)this.main);
        this.main.setExpandRatio((Component)this.layout, 0.75f);
        this.main.setExpandRatio((Component)this.parts_form, 0.25f);
        this.setSizeUndefined();
    }

    private void prepare_grid() {
        this.display_parts = new Grid<>();
        this.display_parts.addColumn(Parts::getPartID).setCaption("PartID");
        this.display_parts.addColumn(Parts::getName).setCaption("Name");
        this.display_parts.addColumn(Parts::getPartType).setCaption("Type");
        this.display_parts.addColumn(Parts::getStatus).setCaption("Status");
        this.display_parts.addColumn(Parts::getPrice).setCaption("Price");
        this.display_parts.setHeight("600px");
        this.display_parts.setWidth("750px");
        this.display_parts.asSingleSelect().addValueChangeListener(event -> {
            if (event.getValue() == null) {
                this.parts_form.setVisible(false);
            } else {
                this.parts_form.setParts((Parts)event.getValue());
            }
        });
    }

    private HorizontalLayout buttonsLayout() {
        HorizontalLayout row = new HorizontalLayout();
        this.filter = new TextField();
        this.filter.setPlaceholder("Filter by PartID");
        this.filter.addValueChangeListener(e -> this.updateList());
        this.filter.setValueChangeMode(ValueChangeMode.LAZY);
        
        Button ViewParts = new Button("ViewParts");
        ViewParts.addClickListener(e -> this.refreshView());
        
        Button addPart = new Button("Create New Part");
        addPart.addClickListener(e -> {
            this.clearSelection();
            this.parts_form.setParts(new Parts(0, "", "", "", 0.0f));
        });
        addPart.setEnabled(false);
        
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
        
        this.display_parts.setItems(parts);
    }
}

