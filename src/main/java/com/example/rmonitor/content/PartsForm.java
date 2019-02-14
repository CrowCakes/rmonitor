package com.example.rmonitor.content;

import com.example.rmonitor.classes.ConnectionManager;
import com.example.rmonitor.classes.ObjectConstructor;
import com.example.rmonitor.classes.Parts;
import com.example.rmonitor.content.PartsForm;
import com.example.rmonitor.content.PartsView;
import com.example.rmonitor.layouts.PartsLayout;
import com.vaadin.data.Binder;
import com.vaadin.ui.Button;
import com.vaadin.ui.Component;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Notification;
import com.vaadin.ui.TextField;
import com.vaadin.ui.UI;
import org.vaadin.dialogs.ConfirmDialog;

public class PartsForm
extends FormLayout {
    private Parts parts;
    private String old_part;
    private PartsView parts_view;
    private PartsLayout parts_layout;
    private ConnectionManager manager;
    private ObjectConstructor constructor;
    private TextField partid;
    private TextField name;
    private TextField parttype;
    private Button save;
    private Button delete;
    private Button cancel;
    private HorizontalLayout save_cancel;
    private Binder<Parts> binder;

    public PartsForm(PartsView parts_view) {
        this.manager = new ConnectionManager();
        this.constructor = new ObjectConstructor();
        this.partid = new TextField("PartID");
        this.name = new TextField("Name");
        this.parttype = new TextField("Type");
        this.save = new Button("Save");
        this.delete = new Button("Delete");
        this.cancel = new Button("Cancel");
        this.save_cancel = new HorizontalLayout(new Component[]{this.save, this.cancel});
        this.binder = new Binder<>(Parts.class);
        this.parts_view = parts_view;
        this.prepare_fields();
        this.partid.setEnabled(false);
        this.addComponents(new Component[]{this.partid, this.name, this.parttype, this.save_cancel});
        this.setVisible(false);
        this.save.setStyleName("primary");
        this.save.setClickShortcut(13, new int[0]);
        this.prepare_buttons();
    }

    public PartsForm(PartsLayout parts_layout) {
        this.manager = new ConnectionManager();
        this.constructor = new ObjectConstructor();
        this.partid = new TextField("PartID");
        this.name = new TextField("Name");
        this.parttype = new TextField("Type");
        this.save = new Button("Save");
        this.delete = new Button("Delete");
        this.cancel = new Button("Cancel");
        this.save_cancel = new HorizontalLayout(new Component[]{this.save, this.cancel});
        this.binder = new Binder<>(Parts.class);
        this.parts_layout = parts_layout;
        TextField price = new TextField("Price");
        this.binder.bind(price, Parts::getPriceStr, Parts::setPrice);
        this.prepare_fields();
        this.addComponents(new Component[]{this.partid, this.name, this.parttype, price, this.save_cancel});
        this.setVisible(false);
        this.save.setStyleName("primary");
        this.save.setClickShortcut(13, new int[0]);
        this.prepare_buttons();
    }

    public void prepare_fields() {
        this.partid.setRequiredIndicatorVisible(true);
        this.name.setRequiredIndicatorVisible(true);
        this.binder.bind(partid, Parts::getPartIDStr, Parts::setPartID);
        this.binder.bind(name, Parts::getName, Parts::setName);
        this.binder.bind(parttype, Parts::getPartType, Parts::setPartType);
        this.binder.bindInstanceFields(this);
    }

    public void prepare_buttons() {
        this.save.addClickListener(e -> {
            if (this.validate()) {
                this.save();
            } else {
                Notification.show((String)"Error", (String)"Something's wrong with the entered Parts data.\r\nPlease check all the fields are entered.", (Notification.Type)Notification.Type.ERROR_MESSAGE);
            }
        });
        this.delete.addClickListener(e -> {
            this.save_cancel.setVisible(false);
            this.delete.setVisible(false);
            ConfirmDialog.show((UI)this.getUI(), 
            		(String)"Confirmation", 
            		(String)"Are you sure you want to delete this Part?", 
            		(String)"Yes", (String)"No", 
            		new ConfirmDialog.Listener() {
            	public void onClose(ConfirmDialog dialog) {
            		if (dialog.isConfirmed()) {
            			delete();
            			
            			save_cancel.setVisible(true);
            			delete.setVisible(true);
            		}
            		else {
            			save_cancel.setVisible(true);
            			delete.setVisible(true);
            		}
            	}
            });
        });
        this.cancel.addClickListener(e -> this.cancel());
    }

    public void setParts(Parts parts) {
        this.parts = parts;
        this.old_part = String.valueOf(parts.getPartID());
        this.binder.setBean(parts);
        this.delete.setVisible(parts.isPersisted());
        this.setVisible(true);
    }

    private void save() {
        this.manager.connect();
        Boolean test1 = this.constructor.isPartExisting(this.manager, Integer.parseInt(this.old_part));
        this.manager.disconnect();
        this.manager.connect();
        Boolean test2 = this.constructor.isPartExisting(this.manager, this.parts.getPartID());
        this.manager.disconnect();
        
        if (Integer.parseInt(this.old_part) == this.parts.getPartID() || 
        		Integer.parseInt(this.old_part) != 0 && this.parts.getPartID() != 0 && test1.booleanValue() && !test2.booleanValue()) {
            System.out.println("Edit Existing Part");
            this.manager.disconnect();
            
            String query = String.format("EditPart\r\n%s\r\n%s\r\n%s\r\n%s\r\n%s\r\n%s", 
            		this.old_part, 
            		String.valueOf(this.parts.getPartID()), 
            		this.parts.getName(), 
            		this.parts.getPartType(), 
            		this.parts.getStatus(), 
            		Float.valueOf(this.parts.getPrice()));
            
            this.manager.connect();
            String result = this.manager.send(query);
            this.manager.disconnect();
            
            Notification.show((String)"Edit Part", (String)result, (Notification.Type)Notification.Type.HUMANIZED_MESSAGE);
        } 
        else {
            this.manager.disconnect();
            System.out.println("Add New Part");
            
            String query = String.format("InsertNewPart\r\n%d\r\n%s\r\n%s\r\n%s", 
            		this.parts.getPartID(), 
            		this.parts.getName(), 
            		this.parts.getPartType(), 
            		Float.valueOf(this.parts.getPrice()));
            System.out.println(String.valueOf(query) + "\r\nnothing follows");
            
            this.manager.connect();
            String result = this.manager.send(query);
            this.manager.disconnect();
            
            Notification.show((String)"Create New Part", (String)result, (Notification.Type)Notification.Type.HUMANIZED_MESSAGE);
        }
        
        this.setVisible(false);
        if (this.parts_view != null) {
            this.parts_view.clearSelection();
            this.parts_view.refreshView();
        } else {
            this.parts_layout.clearSelection();
            this.parts_layout.refreshView();
        }
    }

    private void cancel() {
        if (this.parts_view != null) {
            this.parts_view.clearSelection();
        } else {
            this.parts_layout.clearSelection();
        }
        this.setVisible(false);
    }

    private void delete() {
        String query = String.format("DeleteParts\r\n%s", this.parts.getPartID());
        this.manager.connect();
        String result = this.manager.send(query);
        this.manager.disconnect();
        
        this.setVisible(false);
        if (this.parts_view != null) {
            this.parts_view.clearSelection();
            this.parts_view.refreshView();
        } else {
            this.parts_layout.clearSelection();
            this.parts_layout.refreshView();
        }
        
        Notification.show((String)"Delete Part", (String)result, (Notification.Type)Notification.Type.HUMANIZED_MESSAGE);
    }

    private boolean validate() {
        if (this.name.getValue().isEmpty()) {
            return Boolean.FALSE;
        }
        return Boolean.TRUE;
    }
}

