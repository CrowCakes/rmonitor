package com.example.rmonitor.content;

import com.example.rmonitor.classes.Client;
import com.example.rmonitor.classes.ConnectionManager;
import com.example.rmonitor.classes.ObjectConstructor;
import com.example.rmonitor.content.ClientForm;
import com.example.rmonitor.content.ClientView;
import com.example.rmonitor.layouts.ClientFormLayout;
import com.vaadin.data.Binder;
import com.vaadin.ui.Component;
import com.vaadin.ui.Notification;
import com.vaadin.ui.UI;
import org.vaadin.dialogs.ConfirmDialog;

/*
 * Exception performing whole class analysis.
 */
public class ClientForm
extends ClientFormLayout {
    private Client client;
    private Client old_client;
    private ClientView client_view;
    private ConnectionManager manager;
    private ObjectConstructor constructor;
    private Binder<Client> binder;

    /***
     * Sets up the form to be used in editing or creating Clients.
     * @param client_view - the parent module
     */
    public ClientForm(ClientView client_view) {
        this.manager = new ConnectionManager();
        this.constructor = new ObjectConstructor();
        this.binder = new Binder<>(Client.class);
        this.client_view = client_view;
        this.prepare_buttons();
        this.prepare_fields();
        this.addComponents(new Component[]{this.name, this.address, this.contactperson, this.contactnum, this.buttons});
        this.save.setStyleName("primary");
        this.save.setClickShortcut(13, new int[0]);
        this.binder.bindInstanceFields((Object)this);
        this.setMargin(false);
    }

    /***
     * Attaches the selected Client from the parent module's grid.
     * @param client - the Client selected in the parent module's grid.
     */
    public void setClient(Client client) {
        this.client = client;
        this.old_client = client;
        this.binder.setBean(client);
        this.delete.setVisible(false);
        this.setVisible(true);
    }

    /***
     * Prepares the functions attached to the form's buttons.
     */
    private void prepare_buttons() {
        this.save.addClickListener(e -> this.save());
        this.delete.addClickListener(e -> {
            this.buttons.setVisible(false);
            this.delete.setVisible(false);
            ConfirmDialog.show((UI)this.getUI(), 
            		(String)"Confirmation", (String)"Are you sure you want to delete this Client?", 
            		(String)"Yes", (String)"No", 
            		new ConfirmDialog.Listener() {

                        public void onClose(ConfirmDialog dialog) {
                            if (dialog.isConfirmed()) {
                                // Confirmed to continue
                                delete();
                                
                            	buttons.setVisible(true);
                                delete.setVisible(true);
                            } else {
                                // User did not confirm
                                
                            	buttons.setVisible(true);
                                delete.setVisible(true);
                            }
                        }
            		});
        });
        this.cancel.addClickListener(e -> this.cancel());
    }

    /***
     * Prepares the fields to be used in the form.
     */
    private void prepare_fields() {
        this.binder.bind(this.name, Client::getName, Client::setName);
        this.binder.bind(this.address, Client::getAddress, Client::setAddress);
        this.binder.bind(this.contactperson, Client::getContact_person, Client::setContact_person);
        this.binder.bind(this.contactnum, Client::getContact_numberStr, Client::setContact_number);
        this.name.setRequiredIndicatorVisible(true);
        this.address.setRequiredIndicatorVisible(true);
        this.contactnum.setRequiredIndicatorVisible(true);
    }

    /***
     * Hides the form and deselects any selections in the parent module's grid.
     */
    private void cancel() {
        this.client_view.clearSelection();
        this.client_view.refreshView();
        this.setVisible(false);
    }

    /***
     * Removes the Client from the database.
     */
    private void delete() {
        this.manager.connect();
        String query = String.format("DeleteClient\r\n%s", this.client.getClientid());
        String result = this.manager.send(query);
        this.manager.disconnect();
        this.client_view.refreshView();
        this.setVisible(false);
        Notification.show((String)"Delete Client", (String)result, (Notification.Type)Notification.Type.HUMANIZED_MESSAGE);
    }

    /***
     * Sends field input to the database. An entry is created or edited based on the field input.
     */
    private void save() {
        this.manager.connect();
        if (this.constructor.isClientExisting(this.manager, this.old_client.getName())) {
            this.manager.disconnect();
            System.out.println("Edit Client");
            String query = String.format("EditClient\r\n%s\r\n%s\r\n%s\r\n%s\r\n%s", 
            		this.old_client.getName(), 
            		this.client.getName(), 
            		this.client.getAddress(), 
            		this.client.getContact_person(), 
            		this.client.getContact_numberStr());
            this.manager.connect();
            String result = this.manager.send(query);
            this.manager.disconnect();
            Notification.show((String)"Edit Client", (String)result, (Notification.Type)Notification.Type.HUMANIZED_MESSAGE);
        } else {
            this.manager.disconnect();
            System.out.println("Insert New Client");
            String query = String.format("InsertNewClient\r\n%s\r\n%s\r\n%s\r\n%s", 
            		this.client.getName(), 
            		this.client.getAddress(), 
            		this.client.getContact_person(), 
            		this.client.getContact_numberStr());
            this.manager.connect();
            String result = this.manager.send(query);
            this.manager.disconnect();
            Notification.show((String)"Add New Client", (String)result, (Notification.Type)Notification.Type.HUMANIZED_MESSAGE);
        }
        this.client_view.clearSelection();
        this.client_view.refreshView();
        this.setVisible(false);
    }
}

