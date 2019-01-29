/*
 * Decompiled with CFR 0.139.
 * 
 * Could not load the following classes:
 *  com.example.rmonitor.classes.Accessory
 *  com.example.rmonitor.classes.ConnectionManager
 *  com.example.rmonitor.classes.ObjectConstructor
 *  com.example.rmonitor.content.AccessoriesView
 *  com.example.rmonitor.content.AccessoryForm
 *  com.example.rmonitor.content.AccessoryForm$1
 *  com.example.rmonitor.layouts.AccessoryLayout
 *  com.vaadin.data.Binder
 *  com.vaadin.data.Binder$Binding
 *  com.vaadin.data.HasValue
 *  com.vaadin.data.ValueProvider
 *  com.vaadin.server.Setter
 *  com.vaadin.shared.Registration
 *  com.vaadin.ui.Button
 *  com.vaadin.ui.Button$ClickEvent
 *  com.vaadin.ui.Button$ClickListener
 *  com.vaadin.ui.Component
 *  com.vaadin.ui.FormLayout
 *  com.vaadin.ui.HorizontalLayout
 *  com.vaadin.ui.Notification
 *  com.vaadin.ui.Notification$Type
 *  com.vaadin.ui.TextField
 *  com.vaadin.ui.UI
 *  org.vaadin.dialogs.ConfirmDialog
 *  org.vaadin.dialogs.ConfirmDialog$Listener
 */
package com.example.rmonitor.content;

import com.example.rmonitor.classes.Accessory;
import com.example.rmonitor.classes.ConnectionManager;
import com.example.rmonitor.classes.ObjectConstructor;
import com.example.rmonitor.content.AccessoriesView;
import com.example.rmonitor.content.AccessoryForm;
import com.example.rmonitor.layouts.AccessoryLayout;
import com.vaadin.data.Binder;
import com.vaadin.ui.Button;
import com.vaadin.ui.Component;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Notification;
import com.vaadin.ui.TextField;
import com.vaadin.ui.UI;
import org.vaadin.dialogs.ConfirmDialog;

/*
 * Exception performing whole class analysis.
 */
public class AccessoryForm
extends FormLayout {
    private Accessory acc;
    private String old_acc;
    private AccessoriesView acc_view;
    private AccessoryLayout acc_layout;
    private ConnectionManager manager;
    private ObjectConstructor constructor;
    private TextField rn;
    private TextField name;
    private TextField acctype;
    private TextField price;
    private Button save;
    private Button delete;
    private Button cancel;
    private HorizontalLayout save_cancel;
    private Binder<Accessory> binder;

    /***
     * Constructs the Accessory form for editing and creating Accessories.
     * @param acc_view - the module this form is attached to
     */
    public AccessoryForm(AccessoriesView acc_view) {
        this.manager = new ConnectionManager();
        this.constructor = new ObjectConstructor();
        this.rn = new TextField("Rental Asset#");
        this.name = new TextField("Name");
        this.acctype = new TextField("Type");
        this.price = new TextField("Price");
        this.save = new Button("Save");
        this.delete = new Button("Delete");
        this.cancel = new Button("Cancel");
        this.save_cancel = new HorizontalLayout(new Component[]{this.save, this.cancel});
        this.binder = new Binder<>(Accessory.class);
        this.acc_view = acc_view;
        this.prepare_fields();
        this.addComponents(new Component[]{this.rn, this.name, this.acctype, this.save_cancel});
        this.setVisible(false);
        this.save.setStyleName("primary");
        this.save.setClickShortcut(13, new int[0]);
        this.prepare_buttons();
        this.setMargin(false);
    }

    /***
     * Constructs the Accessory form for editing and creating Accessories.
     * @param acc_layout - the parent standalone layout, for use in Asset Management
     */
    public AccessoryForm(AccessoryLayout acc_layout) {
        this.manager = new ConnectionManager();
        this.constructor = new ObjectConstructor();
        this.rn = new TextField("Rental Asset#");
        this.name = new TextField("Name");
        this.acctype = new TextField("Type");
        this.price = new TextField("Price");
        this.save = new Button("Save");
        this.delete = new Button("Delete");
        this.cancel = new Button("Cancel");
        this.save_cancel = new HorizontalLayout(new Component[]{this.save, this.cancel});
        this.binder = new Binder<>(Accessory.class);
        this.acc_layout = acc_layout;
        this.prepare_fields(1);
        this.rn.setReadOnly(true);
        this.name.setReadOnly(true);
        this.acctype.setReadOnly(true);
        this.addComponents(new Component[]{this.rn, this.name, this.acctype, this.price, this.save_cancel, this.delete});
        this.setVisible(false);
        this.save.setStyleName("primary");
        this.save.setClickShortcut(13, new int[0]);
        this.prepare_buttons();
    }

    /***
     * Prepares the fields that should be filled out by the user.
     */
    public void prepare_fields() {
        this.rn.setRequiredIndicatorVisible(true);
        this.name.setRequiredIndicatorVisible(true);
        this.acctype.setRequiredIndicatorVisible(true);
        this.binder.bind(this.rn, Accessory::getRentalNumber, Accessory::setRentalNumber);
        this.binder.bind(this.name, Accessory::getName, Accessory::setName);
        this.binder.bind(this.acctype, Accessory::getAccessoryType, Accessory::setAccessoryType);
        this.binder.bind(this.price, Accessory::getPriceStr, Accessory::setPrice);
        this.binder.bindInstanceFields((Object)this);
    }

    /***
     * Prepares the fields that should be filled out by the user. This function includes the price of the Accessory in the fields.
     * @param x - any integer
     */
    public void prepare_fields(int x) {
        this.rn.setRequiredIndicatorVisible(true);
        this.name.setRequiredIndicatorVisible(true);
        this.acctype.setRequiredIndicatorVisible(true);
        this.binder.bind(this.rn, Accessory::getRentalNumber, Accessory::setRentalNumber);
        this.binder.bind(this.name, Accessory::getName, Accessory::setName);
        this.binder.bind(this.acctype, Accessory::getAccessoryType, Accessory::setAccessoryType);
        this.binder.bind(this.price, Accessory::getPriceStr, Accessory::setPrice);
        this.binder.bindInstanceFields((Object)this);
    }

    /***
     * Prepares the functions attached to each button in the form.
     */
    public void prepare_buttons() {
        this.save.addClickListener(e -> {
            if (this.validate()) {
                this.save();
            } else {
                Notification.show((String)"Error", (String)"Something's wrong with the entered Accessory data.\r\nPlease check all the fields are entered.", (Notification.Type)Notification.Type.ERROR_MESSAGE);
            }
        });
        this.delete.addClickListener(e -> {
            this.save_cancel.setVisible(false);
            this.delete.setVisible(false);
            ConfirmDialog.show((UI)this.getUI(), 
            		(String)"Confirmation", 
            		(String)"Are you sure you want to delete this Accessory?", 
            		(String)"Yes", (String)"No", 
            		new ConfirmDialog.Listener() {

                public void onClose(ConfirmDialog dialog) {
                    if (dialog.isConfirmed()) {
                        // Confirmed to continue
                        delete();
                        
                    	save_cancel.setVisible(true);
                        delete.setVisible(true);
                    } else {
                        // User did not confirm
                        
                    	save_cancel.setVisible(true);
                        delete.setVisible(true);
                    }
                }
    		});
        });
        this.cancel.addClickListener(e -> this.cancel());
    }

    /***
     * Attaches the Accessory to the data in the fields.
     * @param acc - an Accessory, usually the one selected in the parent module
     */
    public void setAccessory(Accessory acc) {
        this.acc = acc;
        this.old_acc = acc.getRentalNumber();
        this.binder.setBean(acc);
        this.delete.setVisible(acc.isPersisted());
        this.setVisible(true);
    }

    /***
     * Sends field input to database. An entry is either created or edited in the database based on the field input.
     */
    private void save() {
        this.manager.connect();
        Boolean test1 = this.constructor.isAccessoryExisting(this.manager, this.acc.getRentalNumber());
        this.manager.disconnect();
        this.manager.connect();
        Boolean test2 = this.constructor.isAccessoryExisting(this.manager, this.old_acc);
        this.manager.disconnect();
        if (this.acc.getRentalNumber() == this.old_acc || !this.old_acc.isEmpty() && !this.acc.getRentalNumber().isEmpty() && !test1.booleanValue() && test2.booleanValue()) {
            String query = String.format("EditAccessory\r\n%s\r\n%s\r\n%s\r\n%s\r\n%s", this.old_acc, this.acc.getRentalNumber(), this.acc.getName(), this.acc.getAccessoryType(), Float.valueOf(this.acc.getPrice()));
            this.manager.connect();
            String result = this.manager.send(query);
            this.manager.disconnect();
            Notification.show((String)"Edit Accessory", (String)result, (Notification.Type)Notification.Type.HUMANIZED_MESSAGE);
        } else {
            String query = String.format("InsertNewRentalAccessory\r\n%s\r\n%s\r\n%s\r\n%s", this.acc.getRentalNumber(), this.acc.getName(), this.acc.getAccessoryType(), Float.valueOf(this.acc.getPrice()));
            this.manager.connect();
            String result = this.manager.send(query);
            this.manager.disconnect();
            Notification.show((String)"Create New Accessory", (String)result, (Notification.Type)Notification.Type.HUMANIZED_MESSAGE);
        }
        this.setVisible(false);
        if (this.acc_view != null) {
            this.acc_view.clearSelection();
            this.acc_view.refreshView();
        } else {
            this.acc_layout.clearSelection();
            this.acc_layout.refreshView();
        }
    }

    /***
     * Hides the form and deselects any selection in the parent module's grid.
     */
    private void cancel() {
        if (this.acc_view != null) {
            this.acc_view.clearSelection();
        } else {
            this.acc_layout.clearSelection();
        }
        this.setVisible(false);
    }

    /***
     * Removes the attached Accessory from the database.
     */
    private void delete() {
        this.manager.connect();
        String query = String.format("DeleteAccessory\r\n%s", this.acc.getRentalNumber());
        String result = this.manager.send(query);
        this.manager.disconnect();
        this.setVisible(false);
        if (this.acc_view != null) {
            this.acc_view.clearSelection();
            this.acc_view.refreshView();
        } else {
            this.acc_layout.clearSelection();
            this.acc_layout.refreshView();
        }
        Notification.show((String)"Delete Accessory", (String)result, (Notification.Type)Notification.Type.HUMANIZED_MESSAGE);
    }

    /***
     * Checks whether or not all fields are filled in.
     * @return True if all fields were filled in, False otherwise
     */
    private boolean validate() {
        if (this.rn.getValue().isEmpty() || this.name.getValue().isEmpty() || this.acctype.getValue().isEmpty()) {
            return Boolean.FALSE;
        }
        return Boolean.TRUE;
    }

    /*
    static HorizontalLayout access$0(AccessoryForm accessoryForm) {
        return accessoryForm.save_cancel;
    }

    static Button access$1(AccessoryForm accessoryForm) {
        return accessoryForm.delete;
    }

    static void access$2(AccessoryForm accessoryForm) {
        accessoryForm.delete();
    }

    private static Object $deserializeLambda$(SerializedLambda serializedLambda) {
        switch (serializedLambda.getImplMethodName()) {
            case "getRentalNumber": 
            case "getRentalNumber": {
                if (serializedLambda.getImplMethodKind() == 5 && serializedLambda.getFunctionalInterfaceClass().equals("com/vaadin/data/ValueProvider") && serializedLambda.getFunctionalInterfaceMethodName().equals("apply") && serializedLambda.getFunctionalInterfaceMethodSignature().equals("(Ljava/lang/Object;)Ljava/lang/Object;") && serializedLambda.getImplClass().equals("com/example/rmonitor/classes/Accessory") && serializedLambda.getImplMethodSignature().equals("()Ljava/lang/String;")) {
                    return Accessory::getRentalNumber;
                }
                if (serializedLambda.getImplMethodKind() != 5 || !serializedLambda.getFunctionalInterfaceClass().equals("com/vaadin/data/ValueProvider") || !serializedLambda.getFunctionalInterfaceMethodName().equals("apply") || !serializedLambda.getFunctionalInterfaceMethodSignature().equals("(Ljava/lang/Object;)Ljava/lang/Object;") || !serializedLambda.getImplClass().equals("com/example/rmonitor/classes/Accessory") || !serializedLambda.getImplMethodSignature().equals("()Ljava/lang/String;")) break;
                return Accessory::getRentalNumber;
            }
            case "setRentalNumber": 
            case "setRentalNumber": {
                if (serializedLambda.getImplMethodKind() == 5 && serializedLambda.getFunctionalInterfaceClass().equals("com/vaadin/server/Setter") && serializedLambda.getFunctionalInterfaceMethodName().equals("accept") && serializedLambda.getFunctionalInterfaceMethodSignature().equals("(Ljava/lang/Object;Ljava/lang/Object;)V") && serializedLambda.getImplClass().equals("com/example/rmonitor/classes/Accessory") && serializedLambda.getImplMethodSignature().equals("(Ljava/lang/String;)V")) {
                    return Accessory::setRentalNumber;
                }
                if (serializedLambda.getImplMethodKind() != 5 || !serializedLambda.getFunctionalInterfaceClass().equals("com/vaadin/server/Setter") || !serializedLambda.getFunctionalInterfaceMethodName().equals("accept") || !serializedLambda.getFunctionalInterfaceMethodSignature().equals("(Ljava/lang/Object;Ljava/lang/Object;)V") || !serializedLambda.getImplClass().equals("com/example/rmonitor/classes/Accessory") || !serializedLambda.getImplMethodSignature().equals("(Ljava/lang/String;)V")) break;
                return Accessory::setRentalNumber;
            }
            case "getName": 
            case "getName": {
                if (serializedLambda.getImplMethodKind() == 5 && serializedLambda.getFunctionalInterfaceClass().equals("com/vaadin/data/ValueProvider") && serializedLambda.getFunctionalInterfaceMethodName().equals("apply") && serializedLambda.getFunctionalInterfaceMethodSignature().equals("(Ljava/lang/Object;)Ljava/lang/Object;") && serializedLambda.getImplClass().equals("com/example/rmonitor/classes/Accessory") && serializedLambda.getImplMethodSignature().equals("()Ljava/lang/String;")) {
                    return Accessory::getName;
                }
                if (serializedLambda.getImplMethodKind() != 5 || !serializedLambda.getFunctionalInterfaceClass().equals("com/vaadin/data/ValueProvider") || !serializedLambda.getFunctionalInterfaceMethodName().equals("apply") || !serializedLambda.getFunctionalInterfaceMethodSignature().equals("(Ljava/lang/Object;)Ljava/lang/Object;") || !serializedLambda.getImplClass().equals("com/example/rmonitor/classes/Accessory") || !serializedLambda.getImplMethodSignature().equals("()Ljava/lang/String;")) break;
                return Accessory::getName;
            }
            case "setName": 
            case "setName": {
                if (serializedLambda.getImplMethodKind() == 5 && serializedLambda.getFunctionalInterfaceClass().equals("com/vaadin/server/Setter") && serializedLambda.getFunctionalInterfaceMethodName().equals("accept") && serializedLambda.getFunctionalInterfaceMethodSignature().equals("(Ljava/lang/Object;Ljava/lang/Object;)V") && serializedLambda.getImplClass().equals("com/example/rmonitor/classes/Accessory") && serializedLambda.getImplMethodSignature().equals("(Ljava/lang/String;)V")) {
                    return Accessory::setName;
                }
                if (serializedLambda.getImplMethodKind() != 5 || !serializedLambda.getFunctionalInterfaceClass().equals("com/vaadin/server/Setter") || !serializedLambda.getFunctionalInterfaceMethodName().equals("accept") || !serializedLambda.getFunctionalInterfaceMethodSignature().equals("(Ljava/lang/Object;Ljava/lang/Object;)V") || !serializedLambda.getImplClass().equals("com/example/rmonitor/classes/Accessory") || !serializedLambda.getImplMethodSignature().equals("(Ljava/lang/String;)V")) break;
                return Accessory::setName;
            }
            case "getAccessoryType": 
            case "getAccessoryType": {
                if (serializedLambda.getImplMethodKind() == 5 && serializedLambda.getFunctionalInterfaceClass().equals("com/vaadin/data/ValueProvider") && serializedLambda.getFunctionalInterfaceMethodName().equals("apply") && serializedLambda.getFunctionalInterfaceMethodSignature().equals("(Ljava/lang/Object;)Ljava/lang/Object;") && serializedLambda.getImplClass().equals("com/example/rmonitor/classes/Accessory") && serializedLambda.getImplMethodSignature().equals("()Ljava/lang/String;")) {
                    return Accessory::getAccessoryType;
                }
                if (serializedLambda.getImplMethodKind() != 5 || !serializedLambda.getFunctionalInterfaceClass().equals("com/vaadin/data/ValueProvider") || !serializedLambda.getFunctionalInterfaceMethodName().equals("apply") || !serializedLambda.getFunctionalInterfaceMethodSignature().equals("(Ljava/lang/Object;)Ljava/lang/Object;") || !serializedLambda.getImplClass().equals("com/example/rmonitor/classes/Accessory") || !serializedLambda.getImplMethodSignature().equals("()Ljava/lang/String;")) break;
                return Accessory::getAccessoryType;
            }
            case "setAccessoryType": 
            case "setAccessoryType": {
                if (serializedLambda.getImplMethodKind() == 5 && serializedLambda.getFunctionalInterfaceClass().equals("com/vaadin/server/Setter") && serializedLambda.getFunctionalInterfaceMethodName().equals("accept") && serializedLambda.getFunctionalInterfaceMethodSignature().equals("(Ljava/lang/Object;Ljava/lang/Object;)V") && serializedLambda.getImplClass().equals("com/example/rmonitor/classes/Accessory") && serializedLambda.getImplMethodSignature().equals("(Ljava/lang/String;)V")) {
                    return Accessory::setAccessoryType;
                }
                if (serializedLambda.getImplMethodKind() != 5 || !serializedLambda.getFunctionalInterfaceClass().equals("com/vaadin/server/Setter") || !serializedLambda.getFunctionalInterfaceMethodName().equals("accept") || !serializedLambda.getFunctionalInterfaceMethodSignature().equals("(Ljava/lang/Object;Ljava/lang/Object;)V") || !serializedLambda.getImplClass().equals("com/example/rmonitor/classes/Accessory") || !serializedLambda.getImplMethodSignature().equals("(Ljava/lang/String;)V")) break;
                return Accessory::setAccessoryType;
            }
            case "getPriceStr": 
            case "getPriceStr": {
                if (serializedLambda.getImplMethodKind() == 5 && serializedLambda.getFunctionalInterfaceClass().equals("com/vaadin/data/ValueProvider") && serializedLambda.getFunctionalInterfaceMethodName().equals("apply") && serializedLambda.getFunctionalInterfaceMethodSignature().equals("(Ljava/lang/Object;)Ljava/lang/Object;") && serializedLambda.getImplClass().equals("com/example/rmonitor/classes/Accessory") && serializedLambda.getImplMethodSignature().equals("()Ljava/lang/String;")) {
                    return Accessory::getPriceStr;
                }
                if (serializedLambda.getImplMethodKind() != 5 || !serializedLambda.getFunctionalInterfaceClass().equals("com/vaadin/data/ValueProvider") || !serializedLambda.getFunctionalInterfaceMethodName().equals("apply") || !serializedLambda.getFunctionalInterfaceMethodSignature().equals("(Ljava/lang/Object;)Ljava/lang/Object;") || !serializedLambda.getImplClass().equals("com/example/rmonitor/classes/Accessory") || !serializedLambda.getImplMethodSignature().equals("()Ljava/lang/String;")) break;
                return Accessory::getPriceStr;
            }
            case "setPrice": 
            case "setPrice": {
                if (serializedLambda.getImplMethodKind() == 5 && serializedLambda.getFunctionalInterfaceClass().equals("com/vaadin/server/Setter") && serializedLambda.getFunctionalInterfaceMethodName().equals("accept") && serializedLambda.getFunctionalInterfaceMethodSignature().equals("(Ljava/lang/Object;Ljava/lang/Object;)V") && serializedLambda.getImplClass().equals("com/example/rmonitor/classes/Accessory") && serializedLambda.getImplMethodSignature().equals("(Ljava/lang/String;)V")) {
                    return Accessory::setPrice;
                }
                if (serializedLambda.getImplMethodKind() != 5 || !serializedLambda.getFunctionalInterfaceClass().equals("com/vaadin/server/Setter") || !serializedLambda.getFunctionalInterfaceMethodName().equals("accept") || !serializedLambda.getFunctionalInterfaceMethodSignature().equals("(Ljava/lang/Object;Ljava/lang/Object;)V") || !serializedLambda.getImplClass().equals("com/example/rmonitor/classes/Accessory") || !serializedLambda.getImplMethodSignature().equals("(Ljava/lang/String;)V")) break;
                return Accessory::setPrice;
            }
            case "lambda$16": {
                if (serializedLambda.getImplMethodKind() != 7 || !serializedLambda.getFunctionalInterfaceClass().equals("com/vaadin/ui/Button$ClickListener") || !serializedLambda.getFunctionalInterfaceMethodName().equals("buttonClick") || !serializedLambda.getFunctionalInterfaceMethodSignature().equals("(Lcom/vaadin/ui/Button$ClickEvent;)V") || !serializedLambda.getImplClass().equals("com/example/rmonitor/content/AccessoryForm") || !serializedLambda.getImplMethodSignature().equals("(Lcom/vaadin/ui/Button$ClickEvent;)V")) break;
                return (Button.ClickListener & java.io.Serializable)e -> {
                    if (this.validate()) {
                        this.save();
                    } else {
                        Notification.show((String)"Error", (String)"Something's wrong with the entered Accessory data.\r\nPlease check all the fields are entered.", (Notification.Type)Notification.Type.ERROR_MESSAGE);
                    }
                };
            }
            case "lambda$17": {
                if (serializedLambda.getImplMethodKind() != 7 || !serializedLambda.getFunctionalInterfaceClass().equals("com/vaadin/ui/Button$ClickListener") || !serializedLambda.getFunctionalInterfaceMethodName().equals("buttonClick") || !serializedLambda.getFunctionalInterfaceMethodSignature().equals("(Lcom/vaadin/ui/Button$ClickEvent;)V") || !serializedLambda.getImplClass().equals("com/example/rmonitor/content/AccessoryForm") || !serializedLambda.getImplMethodSignature().equals("(Lcom/vaadin/ui/Button$ClickEvent;)V")) break;
                return (Button.ClickListener & java.io.Serializable)e -> {
                    this.save_cancel.setVisible(false);
                    this.delete.setVisible(false);
                    ConfirmDialog.show((UI)this.getUI(), (String)"Confirmation", (String)"Are you sure you want to delete this Accessory?", (String)"Yes", (String)"No", (ConfirmDialog.Listener)new );
                };
            }
            case "lambda$18": {
                if (serializedLambda.getImplMethodKind() != 7 || !serializedLambda.getFunctionalInterfaceClass().equals("com/vaadin/ui/Button$ClickListener") || !serializedLambda.getFunctionalInterfaceMethodName().equals("buttonClick") || !serializedLambda.getFunctionalInterfaceMethodSignature().equals("(Lcom/vaadin/ui/Button$ClickEvent;)V") || !serializedLambda.getImplClass().equals("com/example/rmonitor/content/AccessoryForm") || !serializedLambda.getImplMethodSignature().equals("(Lcom/vaadin/ui/Button$ClickEvent;)V")) break;
                return (Button.ClickListener & java.io.Serializable)e -> this.cancel();
            }
        }
        throw new IllegalArgumentException("Invalid lambda deserialization");
    }
    */
}

