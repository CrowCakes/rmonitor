/*
 * Decompiled with CFR 0.139.
 * 
 * Could not load the following classes:
 *  com.example.rmonitor.classes.ConnectionManager
 *  com.example.rmonitor.classes.ObjectConstructor
 *  com.example.rmonitor.classes.SmallAccessory
 *  com.example.rmonitor.content.SmallAccView
 *  com.example.rmonitor.content.SmallAccessoryForm
 *  com.example.rmonitor.content.SmallAccessoryForm$1
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

import com.example.rmonitor.classes.ConnectionManager;
import com.example.rmonitor.classes.ObjectConstructor;
import com.example.rmonitor.classes.SmallAccessory;
import com.example.rmonitor.content.SmallAccView;
import com.example.rmonitor.content.SmallAccessoryForm;
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
public class SmallAccessoryForm
extends FormLayout {
    ConnectionManager manager;
    ObjectConstructor constructor;
    private SmallAccessory small_acc;
    private String old_acc;
    private String old_acc_type;
    private TextField name;
    private TextField acctype;
    private TextField quantity;
    private Button save;
    private Button delete;
    private Button cancel;
    private HorizontalLayout buttons;
    private Binder<SmallAccessory> binder;
    private SmallAccView view;

    public SmallAccessoryForm(SmallAccView view) {
        this.manager = null;
        this.constructor = null;
        this.name = new TextField("Name");
        this.acctype = new TextField("Type");
        this.quantity = new TextField("Quantity");
        this.save = new Button("Save");
        this.delete = new Button("Delete");
        this.cancel = new Button("Cancel");
        this.buttons = new HorizontalLayout(new Component[]{this.save, this.cancel, this.delete});
        this.binder = new Binder<>(SmallAccessory.class);
        this.manager = new ConnectionManager();
        this.constructor = new ObjectConstructor();
        this.view = view;
        this.prepare_fields();
        this.prepare_buttons();
        this.addComponents(new Component[]{this.name, this.acctype, this.quantity, this.buttons});
        this.save.setStyleName("primary");
        this.save.setClickShortcut(13, new int[0]);
        this.setVisible(false);
    }

    public void setAccessory(SmallAccessory foo) {
        this.small_acc = foo;
        this.old_acc = foo.getName();
        this.old_acc_type = foo.getAccessoryType();
        this.binder.setBean(foo);
        this.delete.setVisible(this.small_acc.isPersisted().booleanValue());
        this.setVisible(true);
    }

    private void prepare_fields() {
        this.name.setRequiredIndicatorVisible(true);
        this.binder.bind(this.name, SmallAccessory::getName, SmallAccessory::setName);
        this.binder.bind(this.acctype, SmallAccessory::getAccessoryType, SmallAccessory::setAccessoryType);
        this.binder.bind(this.quantity, SmallAccessory::getQuantityStr, SmallAccessory::setQuantity);
        this.binder.bindInstanceFields((Object)this);
    }

    private void prepare_buttons() {
        this.save.addClickListener(e -> {
            if (this.validate()) {
                this.save();
            } else {
                Notification.show((String)"Error", (String)"Something's wrong with the entered Accessory data.\r\nPlease check all the fields are entered correctly.", (Notification.Type)Notification.Type.ERROR_MESSAGE);
            }
        });
        
        this.delete.addClickListener(e -> {
            this.buttons.setVisible(false);
            ConfirmDialog.show((UI)this.getUI(), 
            		(String)"Confirmation", (String)"Are you sure you want to delete this Accessory?", (String)"Yes", (String)"No", 
            		new ConfirmDialog.Listener() {
						
						@Override
						public void onClose(ConfirmDialog dialog) {
							if (dialog.isConfirmed()) {
								delete();
								buttons.setVisible(true);
							}
							else {
								buttons.setVisible(true);
							}
							
						}
					});
        });
        
        this.cancel.addClickListener(e -> this.cancel());
    }

    private void save() {
        this.manager.connect();
        if (!this.old_acc.isEmpty() && !this.small_acc.getName().isEmpty()) {
            this.manager.disconnect();
            String query = String.format("EditSmallAccessory\r\n%s\r\n%s\r\n%s\r\n%s\r\n%s\r\n%s\r\n%s", this.old_acc, this.old_acc_type, this.small_acc.getName(), this.small_acc.getAccessoryType(), Float.valueOf(this.small_acc.getPrice()), this.small_acc.getQuantity(), this.small_acc.getQuantityMinus());
            this.manager.connect();
            String result = this.manager.send(query);
            this.manager.disconnect();
            Notification.show((String)"Edit Accessory", (String)result, (Notification.Type)Notification.Type.HUMANIZED_MESSAGE);
        } else {
            this.manager.disconnect();
            String query = String.format("InsertNewSmallAccessory\r\n%s\r\n%s\r\n%s\r\n%s\r\n%s", this.small_acc.getName(), this.small_acc.getAccessoryType(), 0, this.small_acc.getQuantity(), 0);
            this.manager.connect();
            String result = this.manager.send(query);
            this.manager.disconnect();
            Notification.show((String)"Create New Accessory", (String)result, (Notification.Type)Notification.Type.HUMANIZED_MESSAGE);
        }
        this.view.clearSelection();
        this.view.refreshView();
        this.setVisible(false);
    }

    private void delete() {
        this.manager.connect();
        String query = String.format("DeleteSmallAccessory\r\n%s", this.old_acc);
        String result = this.manager.send(query);
        this.manager.disconnect();
        this.view.clearSelection();
        this.view.refreshView();
        this.setVisible(false);
        Notification.show((String)"Delete Accessory", (String)result, (Notification.Type)Notification.Type.HUMANIZED_MESSAGE);
    }

    private void cancel() {
        if (this.view != null) {
            this.view.clearSelection();
        }
        this.setVisible(false);
    }

    private boolean validate() {
        if (this.name.getValue().isEmpty()) {
            return false;
        }
        try {
            int test = Integer.parseInt(this.quantity.getValue());
            return true;
        }
        catch (NumberFormatException ex) {
            return false;
        }
    }
}

