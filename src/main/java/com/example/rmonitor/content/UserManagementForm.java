package com.example.rmonitor.content;

import com.example.rmonitor.access.CurrentUser;
import com.example.rmonitor.classes.ConnectionManager;
import com.example.rmonitor.classes.ObjectConstructor;
import com.example.rmonitor.content.UserManagement;
import com.example.rmonitor.content.UserManagementForm;
import com.example.rmonitor.layouts.UserManagementFormLayout;
import com.vaadin.ui.Button;
import com.vaadin.ui.Component;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Panel;
import com.vaadin.ui.TextField;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import java.util.ArrayList;
import java.util.List;
import org.vaadin.dialogs.ConfirmDialog;

public class UserManagementForm
extends UserManagementFormLayout {
    ConnectionManager manager;
    ObjectConstructor constructor;
    List<Button> go;
    String function;
    UserManagement usr;
    List<VerticalLayout> layouts;

    public UserManagementForm(UserManagement usr) {
        this.manager = new ConnectionManager();
        this.constructor = new ObjectConstructor();
        this.go = new ArrayList<>();
        this.function = "";
        this.layouts = new ArrayList<>();
        this.usr = usr;
        this.roles.setItems(new String[]{"Viewer", "Reports", "Admin"});
        this.create_role.setItems(new String[]{"Viewer", "Reports", "Admin"});
        this.addForm(this.new_pass);
        this.prepare_buttons();
        this.buttons.addComponent((Component)new Panel((Component)this.changePass));
        if (CurrentUser.get().equals("Admin") || CurrentUser.get().equals("Super")) {
            this.functions.setContent((Component)new HorizontalLayout(new Component[]{this.changeUserPass, this.changeRole, this.suspend, this.activate}));
            this.buttons.addComponent((Component)this.create);
            this.buttons.addComponent((Component)this.functions);
            this.addForm(this.new_user_pass);
            this.addForm(this.change_role);
            this.addForm(this.create_user);
            this.hideButtons();
        }
        this.addComponents(new Component[]{this.buttons, this.forms});
        this.setSizeUndefined();
        this.setMargin(false);
    }

    private void prepare_buttons() {
        this.go.add(this.new_password_go);
        this.go.add(this.new_user_password_go);
        this.go.add(this.role_go);
        this.go.add(this.create_go);
        this.changePass.addClickListener(e -> {
            this.resetForms();
            this.resetEntryField((TextField)this.old_password);
            this.resetEntryField((TextField)this.new_password);
            this.resetEntryField((TextField)this.confirm_new_password);
            this.function = "ChangePass";
            this.new_pass.setVisible(true);
        });
        
        this.changeUserPass.addClickListener(e -> {
            this.resetForms();
            this.resetEntryField((TextField)this.new_user_password);
            this.resetEntryField((TextField)this.confirm_user_new_password);
            this.function = "ChangeUserPass";
            if (this.usr.getSelectedUser() == null) {
                Notification.show((String)"Error", (String)"Please select a User from the grid.", (Notification.Type)Notification.Type.ERROR_MESSAGE);
            } else {
                this.new_user_pass.setVisible(true);
            }
        });
        
        this.changeRole.addClickListener(e -> {
            this.resetForms();
            this.roles.setSelectedItem(null);
            this.function = "ChangeRole";
            if (this.usr.getSelectedUser() == null) {
                Notification.show((String)"Error", (String)"Please select a User from the grid.", (Notification.Type)Notification.Type.ERROR_MESSAGE);
            } else {
                this.change_role.setVisible(true);
            }
        });
        
        this.suspend.addClickListener(e -> {
            this.resetForms();
            this.function = "Suspend";
            if (this.usr.getSelectedUser() == null) {
                Notification.show((String)"Error", (String)"Please select a User from the grid.", (Notification.Type)Notification.Type.ERROR_MESSAGE);
            } else {
                ConfirmDialog.show((UI)this.getUI(), (String)"Confirmation", (String)"Are you sure?", (String)"Yes", (String)"Cancel", 
                		new ConfirmDialog.Listener() {
							
							@Override
							public void onClose(ConfirmDialog dialog) {
								if (dialog.isConfirmed()) {
									suspendUser();
								}
								else {
									
								}
							}
						});
            }
        });
        
        this.activate.addClickListener(e -> {
            this.resetForms();
            this.function = "Activate";
            if (this.usr.getSelectedUser() == null) {
                Notification.show((String)"Error", (String)"Please select a User from the grid.", (Notification.Type)Notification.Type.ERROR_MESSAGE);
            } else {
                ConfirmDialog.show((UI)this.getUI(), (String)"Confirmation", (String)"Are you sure?", (String)"Yes", (String)"Cancel", 
                		new ConfirmDialog.Listener() {
                	@Override
					public void onClose(ConfirmDialog dialog) {
                		if (dialog.isConfirmed()) {
							activateUser();
						}
						else {
							
						}
					}
                });
            }
        });
        
        this.create.addClickListener(e -> {
            this.resetForms();
            this.resetEntryField(this.create_username);
            this.resetEntryField((TextField)this.create_password);
            this.resetEntryField((TextField)this.create_confirm_password);
            this.create_role.setSelectedItem(null);
            this.function = "Create";
            this.create_user.setVisible(true);
        });
        
        for (Button button : this.go) {
        	
            button.addClickListener(e -> ConfirmDialog.show((UI)this.getUI(), 
            		(String)"Confirmation", (String)"Are you sure?", (String)"Yes", (String)"Cancel", 
            		new ConfirmDialog.Listener() {
						
						@Override
						public void onClose(ConfirmDialog dialog) {
							if (dialog.isConfirmed()) {
								switch (function) {
								case "ChangePass":
									NewPassword();
									break;
								case "ChangeUserPass":
									NewUserPassword();
									break;
								case "ChangeRole":
									ChangeRole();
									break;
								case "Create":
									CreateUser();
									break;
								}
							}
							else {
								
							}
						}
					}
            	)
            );
            
        }
    }

    private void NewPassword() {
        this.manager.connect();
        if (this.old_password.getValue().isEmpty() || this.new_password.getValue().isEmpty() || this.confirm_new_password.getValue().isEmpty()) {
            Notification.show((String)"Error", (String)"One of your fields was empty. Please fill out the fields", (Notification.Type)Notification.Type.ERROR_MESSAGE);
        } else if (!this.new_password.getValue().equals(this.confirm_new_password.getValue())) {
            Notification.show((String)"Error", (String)"Please enter the same passwords in the Enter and Confirm New Password", (Notification.Type)Notification.Type.ERROR_MESSAGE);
        } else {
            this.manager.disconnect();
            String query = String.format("UserManagement\r\nChangePassword\r\n%s\r\n%s\r\n%s", this.new_password.getValue(), CurrentUser.getName(), this.old_password.getValue());
            this.manager.connect();
            String result = this.manager.send(query);
            this.usr.populate_grid();
            Notification.show((String)"", (String)result, (Notification.Type)Notification.Type.HUMANIZED_MESSAGE);
        }
        this.manager.disconnect();
    }

    private void NewUserPassword() {
        if (this.new_user_password.getValue().isEmpty() || this.confirm_user_new_password.getValue().isEmpty()) {
            Notification.show((String)"Error", (String)"One of your fields was empty. Please fill out the fields", (Notification.Type)Notification.Type.ERROR_MESSAGE);
        } else if (!this.new_user_password.getValue().equals(this.confirm_user_new_password.getValue())) {
            Notification.show((String)"Error", (String)"Please enter the same passwords in the Enter and Confirm New Password", (Notification.Type)Notification.Type.ERROR_MESSAGE);
        } else {
            String query = String.format("UserManagement\r\nChangeUserPassword\r\n%s\r\n%s", this.new_user_password.getValue(), this.usr.getSelectedUser().getUsername());
            this.manager.connect();
            String result = this.manager.send(query);
            this.manager.disconnect();
            this.usr.populate_grid();
            Notification.show((String)"", (String)result, (Notification.Type)Notification.Type.HUMANIZED_MESSAGE);
        }
    }

    private void ChangeRole() {
        if (((String)this.roles.getValue()).isEmpty()) {
            Notification.show((String)"Error", (String)"Please select a role", (Notification.Type)Notification.Type.ERROR_MESSAGE);
        } else {
            String query = String.format("UserManagement\r\nChangeRole\r\n%s\r\n%s", this.roles.getValue(), this.usr.getSelectedUser().getUsername());
            this.manager.connect();
            String result = this.manager.send(query);
            this.manager.disconnect();
            this.usr.populate_grid();
            Notification.show((String)"", (String)result, (Notification.Type)Notification.Type.HUMANIZED_MESSAGE);
        }
    }

    private void CreateUser() {
        if (((String)this.create_role.getValue()).isEmpty()) {
            Notification.show((String)"Error", (String)"Please select a role", (Notification.Type)Notification.Type.ERROR_MESSAGE);
        } else if (this.create_password.getValue().isEmpty() || this.create_confirm_password.getValue().isEmpty() || this.create_username.getValue().isEmpty()) {
            Notification.show((String)"Error", (String)"One of your fields was empty. Please fill out the fields", (Notification.Type)Notification.Type.ERROR_MESSAGE);
        } else if (!this.create_password.getValue().equals(this.create_confirm_password.getValue())) {
            Notification.show((String)"Error", (String)"Please enter the same passwords in both Password fields", (Notification.Type)Notification.Type.ERROR_MESSAGE);
        } else {
            String query = String.format("UserManagement\r\nCreate\r\n%s\r\n%s\r\n%s", this.create_username.getValue(), this.create_password.getValue(), this.create_role.getValue());
            this.manager.connect();
            String result = this.manager.send(query);
            this.manager.disconnect();
            this.usr.populate_grid();
            Notification.show((String)"", (String)result, (Notification.Type)Notification.Type.HUMANIZED_MESSAGE);
        }
    }
    
    private void suspendUser() {
    	if (usr.getSelectedUser() == null) {
    		Notification.show("Error", "You didn't select anything.", Notification.Type.ERROR_MESSAGE);
    		return;
    	}
    	
    	String result;
    	
    	String query = String.format("UserManagement\r\nSuspend\r\n%s", usr.getSelectedUser().getUsername());
    	manager.connect();
    	result = manager.send(query);
    	manager.disconnect();
    	
    	this.usr.populate_grid();
    	Notification.show("Result", result, Notification.Type.HUMANIZED_MESSAGE);
    }
    
    private void activateUser() {
    	if (usr.getSelectedUser() == null) {
    		Notification.show("Error", "You didn't select anything.", Notification.Type.ERROR_MESSAGE);
    		return;
    	}
    	
    	String result;
    	
    	String query = String.format("UserManagement\r\nActivate\r\n%s", usr.getSelectedUser().getUsername());
    	manager.connect();
    	result = manager.send(query);
    	manager.disconnect();
    	
    	this.usr.populate_grid();
    	Notification.show("Result", result, Notification.Type.HUMANIZED_MESSAGE);
    }

    private void addForm(VerticalLayout c) {
        this.layouts.add(c);
        c.setVisible(false);
        this.forms.addComponent((Component)c);
    }

    private void resetEntryField(TextField field) {
        field.setValue("");
    }

    public void resetForms() {
        for (VerticalLayout layout : this.layouts) {
            layout.setVisible(false);
        }
        this.function = "";
    }

    public void hideButtons() {
        this.functions.setVisible(false);
    }

    public void showButtons() {
        this.functions.setVisible(true);
    }
}

