package com.example.rmonitor.content;

import com.example.rmonitor.classes.ConnectionManager;
import com.example.rmonitor.classes.ObjectConstructor;
import com.example.rmonitor.classes.PullOutForm;
import com.example.rmonitor.content.PullForm;
import com.vaadin.navigator.View;
import com.vaadin.ui.Button;
import com.vaadin.ui.Component;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.Grid;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.VerticalLayout;
import java.util.ArrayList;
import java.util.List;

public class PullView
extends CssLayout
implements View {
    ConnectionManager manager = new ConnectionManager();
    ObjectConstructor constructor = new ObjectConstructor();
    Grid<PullOutForm> pullouts = new Grid<>();
    int latestIncrement;
    VerticalLayout layout = new VerticalLayout();
    HorizontalLayout main = new HorizontalLayout();
    PullForm pulloutform;

    public PullView() {
        this.prepare_grid();
        this.layout.addComponents(new Component[]{this.buttonLayout(), this.pullouts});
        this.main.addComponent((Component)this.layout);
        this.addComponent((Component)this.main);
        this.refreshView();
    }

    private void prepare_grid() {
        this.pullouts.addColumn(PullOutForm::getDeliveryID).setCaption("DeliveryID");
        this.pullouts.addColumn(PullOutForm::getFormNumber).setCaption("Form Number");
        this.pullouts.addColumn(PullOutForm::getDateCreated).setCaption("Date Created");
        this.pullouts.addColumn(PullOutForm::getStatus).setCaption("Status");
        this.pullouts.setSelectionMode(Grid.SelectionMode.SINGLE);
        this.pullouts.setHeight("600px");
        this.pullouts.setWidth("1000px");
        this.pullouts.asSingleSelect().addValueChangeListener(event -> {
            if (event.getValue() == null) {
                if (this.pulloutform != null) {
                    this.pulloutform.setVisible(false);
                }
                this.layout.setVisible(true);
            } 
            else if (!((PullOutForm)event.getValue()).getStatus().equals("Returned")) {
                if (this.pulloutform != null) {
                    this.main.removeComponent((Component)this.pulloutform);
                }
                this.pulloutform = new PullForm(this);
                this.pulloutform.setPullOut((PullOutForm)event.getValue());
                this.main.addComponent((Component)this.pulloutform);
                this.layout.setVisible(false);
            }
        });
    }

    private HorizontalLayout buttonLayout() {
        HorizontalLayout button_row = new HorizontalLayout();
        
        Button ViewPullOuts = new Button("Refresh");
        ViewPullOuts.addClickListener(e -> this.refreshView());
        
        Button NewPullOut = new Button("New");
        NewPullOut.addClickListener(e -> {
            if (this.pulloutform != null) {
                this.main.removeComponent((Component)this.pulloutform);
            }
            this.pulloutform = new PullForm(this);
            this.main.addComponent((Component)this.pulloutform);
            this.layout.setVisible(false);
        });
        
        button_row.addComponents(new Component[]{ViewPullOuts, NewPullOut});
        return button_row;
    }

    public void refreshView() {
        List<PullOutForm> pullouts = new ArrayList<>();
        
        this.manager.connect();
        pullouts = this.constructor.constructPullOuts(this.manager);
        this.manager.disconnect();
        
        this.pullouts.setItems(pullouts);
        this.pullouts.deselectAll();
        this.layout.setVisible(true);
    }
}

