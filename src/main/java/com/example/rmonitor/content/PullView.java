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
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;
import java.util.ArrayList;
import java.util.List;

public class PullView
extends CssLayout
implements View {
    ConnectionManager manager = new ConnectionManager();
    ObjectConstructor constructor = new ObjectConstructor();
    
    Grid<PullOutForm> pullouts = new Grid<>();
    PullForm pulloutform;
    
    VerticalLayout layout = new VerticalLayout();
    HorizontalLayout main = new HorizontalLayout();
    
    Label display_count = new Label("");
    int MAX_LIMIT = 20;
    int count = 0;
    int offset = 0;
    int limit = MAX_LIMIT;
    
    int latestIncrement;

    public PullView() {
    	limit = MAX_LIMIT;
        this.prepare_grid();
        this.layout.addComponents(new Component[]{this.buttonLayout(), this.pullouts, fetchNextBatch()});
        this.main.addComponent((Component)this.layout);
        this.addComponent((Component)this.main);
        this.refreshView();
    }

    private void prepare_grid() {
        this.pullouts.addColumn(PullOutForm::getDeliveryID).setCaption("DeliveryID");
        this.pullouts.addColumn(PullOutForm::getCustomer).setCaption("Customer");
        this.pullouts.addColumn(PullOutForm::getFormNumber).setCaption("Form Number");
        this.pullouts.addColumn(PullOutForm::getDateCreated).setCaption("Date Created");
        this.pullouts.addColumn(PullOutForm::getStatus).setCaption("Status");
        this.pullouts.setSelectionMode(Grid.SelectionMode.SINGLE);
        this.pullouts.setHeight("500px");
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
    
    private HorizontalLayout fetchNextBatch() {
    	Button previous = new Button(String.format("Previous %d", MAX_LIMIT));
        previous.addClickListener(e -> {
        	offset = (offset - limit < 0) ? 0 : offset - limit;
        	limit = (offset + limit > count) ? count - offset : limit;
        	displayNew(offset, limit);
        	
        	display_count.setValue(String.format("%d-%d of %d", offset, offset+limit, count));
        	limit = MAX_LIMIT;
        });
        Button next = new Button(String.format("Next %d", MAX_LIMIT));
        next.addClickListener(e -> {
        	offset = (offset + limit > count) ? offset : offset + limit;
        	limit = (offset + limit > count) ? count - offset : limit;
        	displayNew(offset, limit);
        	
        	display_count.setValue(String.format("%d-%d of %d", offset, offset+limit, count));
        	limit = MAX_LIMIT;
        });
        return new HorizontalLayout(previous, display_count, next);
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
        offset = 0;
        
        manager.connect();
        count = constructor.getPullOutCount(manager);
        manager.disconnect();

        limit = (offset + limit > count) ? count - offset : limit;
        
        this.manager.connect();
        pullouts = this.constructor.constructPullOuts(this.manager, offset, limit);
        this.manager.disconnect();
        
        this.pullouts.setItems(pullouts);
        display_count.setValue(String.format("%d-%d of %d", offset, offset+limit, count));
        limit = MAX_LIMIT;
        
        this.pullouts.deselectAll();
        this.layout.setVisible(true);
    }
    
    private void displayNew(int offset, int limit) {
    	List<PullOutForm> pullouts = new ArrayList<>();
        
        this.manager.connect();
        pullouts = this.constructor.constructPullOuts(this.manager, offset, limit);
        this.manager.disconnect();
        
        this.pullouts.setItems(pullouts);
        this.pullouts.deselectAll();
        this.layout.setVisible(true);
    }
}

