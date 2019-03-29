/*
 * Decompiled with CFR 0.139.
 * 
 * Could not load the following classes:
 *  com.example.rmonitor.classes.Client
 *  com.example.rmonitor.classes.ConnectionManager
 *  com.example.rmonitor.classes.ObjectConstructor
 *  com.example.rmonitor.content.ClientForm
 *  com.example.rmonitor.content.ClientView
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
 *  com.vaadin.ui.SingleSelect
 *  com.vaadin.ui.TextField
 *  com.vaadin.ui.VerticalLayout
 */
package com.example.rmonitor.content;

import com.example.rmonitor.classes.Client;
import com.example.rmonitor.classes.ConnectionManager;
import com.example.rmonitor.classes.ObjectConstructor;
import com.example.rmonitor.content.ClientForm;
import com.vaadin.navigator.View;
import com.vaadin.shared.ui.ValueChangeMode;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Component;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.Grid;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import java.util.ArrayList;
import java.util.List;

public class ClientView
extends CssLayout
implements View {
    public static final String VIEW_NAME = "Clients";
    ConnectionManager manager = new ConnectionManager();
    ObjectConstructor constructor = new ObjectConstructor();
    ClientForm client_form = new ClientForm(this);
    final HorizontalLayout button_row;
    final VerticalLayout layout = new VerticalLayout();
    final HorizontalLayout main = new HorizontalLayout();
    final Grid<Client> display_clients = new Grid<>();
    TextField filter;

    /***
     * Creates the module that displays the list of Clients.
     * @param user
     */
    public ClientView(String user) {
        this.setSizeFull();
        this.addStyleName("client-view");
        this.button_row = this.prepare_buttons(user);
        this.prepare_grid(user);
        this.display_clients.setHeight("575px");
        this.display_clients.setWidth("750px");
        this.client_form.setVisible(false);
        this.layout.addComponents(new Component[]{this.button_row, this.display_clients});
        this.main.addComponents(new Component[]{this.layout, this.client_form});
        this.main.setComponentAlignment((Component)this.client_form, Alignment.MIDDLE_RIGHT);
        this.addComponent((Component)this.main);
        this.main.setExpandRatio((Component)this.layout, 0.75f);
        this.main.setExpandRatio((Component)this.client_form, 0.25f);
        this.refreshView();
    }

    /***
     * Prepares the Buttons that query the database.
     * @param user - string denoting the user's role
     * @return a HorizontalLayout with buttons ready to use
     */
    private HorizontalLayout prepare_buttons(String user) {
        HorizontalLayout button_row = new HorizontalLayout();
        this.filter = new TextField();
        this.filter.setPlaceholder("Filter by Client Name");
        this.filter.addValueChangeListener(e -> this.updateList());
        this.filter.setValueChangeMode(ValueChangeMode.LAZY);
        
        Button viewclients = new Button("View Clients");
        viewclients.addClickListener(e -> this.refreshView());
        
        Button quit = new Button("Kill Server");
        quit.addClickListener(e -> {
            
        });
        
        Button addclient = new Button("Add New Client");
        addclient.addClickListener((Button.ClickListener & java.io.Serializable)e -> {
            this.clearSelection();
            this.client_form.setClient(new Client(0, "", "", "", 0));
        });
        if (!user.equals("Admin")) {
            addclient.setEnabled(false);
        }
        button_row.addComponents(new Component[]{this.filter, viewclients, addclient});
        return button_row;
    }

    /***
     * Formats the grid that will display the list of Clients.
     * @param user - string denoting the user's role
     */
    private void prepare_grid(String user) {
        this.display_clients.addColumn(Client::getClientid).setCaption("Client ID");
        this.display_clients.addColumn(Client::getName).setCaption("Name");
        this.display_clients.addColumn(Client::getAddress).setCaption("Address");
        this.display_clients.addColumn(Client::getContact_person).setCaption("Contact Person");
        this.display_clients.addColumn(Client::getContact_number).setCaption("Contact Number");
        if (user.equals("Admin")) {
            this.display_clients.asSingleSelect().addValueChangeListener(event -> {
                if (event.getValue() == null) {
                    this.client_form.setVisible(false);
                } else {
                    this.client_form.setClient((Client)event.getValue());
                }
            });
        }
    }

    /***
     * Deselects any grid selections.
     */
    public void clearSelection() {
        this.display_clients.deselectAll();
    }

    /***
     * Fetches the latest entries of Clients from the database based on filter input.
     */
    private void updateList() {
        List<Client> clients = new ArrayList<>();
        this.manager.connect();
        try {
            clients = this.constructor.filterClients(this.manager, this.filter.getValue());
            this.manager.disconnect();
            this.display_clients.setItems(clients);
        }
        catch (NumberFormatException ex) {
            this.manager.disconnect();
        }
    }

    /***
     * Fetches the latest entries of Clients from the database.
     */
    public void refreshView() {
        List<Client> clients = new ArrayList<>();
        
        this.manager.connect();
        clients = this.constructor.constructClients(this.manager);
        this.manager.disconnect();
        
        this.display_clients.setItems(clients);
    }
}

