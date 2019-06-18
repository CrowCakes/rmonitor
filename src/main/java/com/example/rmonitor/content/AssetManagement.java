package com.example.rmonitor.content;

import com.example.rmonitor.access.CurrentUser;
import com.example.rmonitor.layouts.AccessoryLayout;
import com.example.rmonitor.layouts.ComputerLayout;
import com.example.rmonitor.layouts.PartsLayout;
import com.vaadin.navigator.View;
import com.vaadin.ui.Component;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.TabSheet;
import com.vaadin.ui.VerticalLayout;

public class AssetManagement
extends CssLayout
implements View {
    public static final String VIEW_NAME = "Assets";
    TabSheet tabsheet = new TabSheet();
    VerticalLayout layout = new VerticalLayout(new Component[]{this.tabsheet});
    VerticalLayout parts = new PartsLayout();
    VerticalLayout acc = new AccessoryLayout();
    VerticalLayout comp = new ComputerLayout();

    public AssetManagement() {
        if (CurrentUser.get().equals("Super")) {
        	this.addComponent((Component)this.layout);
            this.tabsheet.addTab((Component)this.parts, "Parts");
            this.tabsheet.addTab((Component)this.acc, "Accessories");
            this.tabsheet.addTab((Component)this.comp, "Computers");
            this.tabsheet.setSizeUndefined();
            this.tabsheet.setHeight("650px");
            this.tabsheet.setWidth("1050px");
            this.layout.addComponent((Component)this.tabsheet);
            this.layout.setSizeUndefined();
            this.layout.setHeight("100%");
            this.layout.setWidth("100%");
        }
    }
}

