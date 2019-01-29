package com.example.rmonitor.content;

import com.example.rmonitor.access.CurrentUser;
import com.example.rmonitor.classes.ConnectionManager;
import com.example.rmonitor.classes.ObjectConstructor;
import com.example.rmonitor.classes.User;
import com.example.rmonitor.content.UserManagementForm;
import com.example.rmonitor.layouts.UserManagementLayout;
import com.vaadin.ui.Component;
import com.vaadin.ui.Grid;
import com.vaadin.ui.VerticalLayout;
import java.util.ArrayList;
import java.util.List;

public class UserManagement
extends UserManagementLayout {
    ConnectionManager manager = new ConnectionManager();
    ObjectConstructor constructor = new ObjectConstructor();
    String currentUser;
    String currentId;
    String currentRole;
    UserManagementForm usrButtons = new UserManagementForm(this);
    VerticalLayout userFrame = new VerticalLayout();

    public UserManagement(String role) {
        this.update_user();
        if (role.equals("Admin") || role.equals("Super")) {
        	
            this.userFrame.addComponent((Component)this.refresh);
            this.prepare_refresh();
            
            this.userFrame.addComponent((Component)this.users);
            this.prepare_grid();
            
            this.users.asSingleSelect().addValueChangeListener(e -> {
                this.usrButtons.resetForms();
                if (e.getValue() != null) {
                    this.usrButtons.showButtons();
                } else {
                    this.usrButtons.hideButtons();
                }
            });
            
        }
        this.userFrame.addComponent((Component)this.usrButtons);
        this.userFrame.setSizeUndefined();
        
        this.formFrame.setContent((Component)this.userFrame);
        this.formFrame.setHeight("525px");
        this.formFrame.setWidth("1000px");
        
        this.userData.setMargin(false);
        
        this.layout.addComponents(new Component[]{this.userData, this.formFrame});
        
        this.addComponent((Component)this.layout);
        this.populate_grid();
    }

    public String getCurrentUser() {
        return this.currentUser;
    }

    public String getCurrentId() {
        return this.currentId;
    }

    public String getCurrentRole() {
        return this.currentRole;
    }

    public void populate_grid() {
        List<User> userList = new ArrayList<>();
        
        this.manager.connect();
        userList = this.constructor.constructUsers(this.manager);
        this.manager.disconnect();
        
        this.users.setItems(userList);
    }

    private void update_user() {
        this.currentUser = CurrentUser.getName();
        this.currentId = CurrentUser.getId();
        this.currentRole = CurrentUser.get();
        this.username.setValue(String.format("User: %s", this.currentUser));
        this.timeKey.setValue(String.format("TimeKey: %s", this.currentId));
        this.role.setValue(String.format("Role: %s", this.currentRole));
    }

    public User getSelectedUser() {
        return (User)this.users.asSingleSelect().getValue();
    }

    private void prepare_grid() {
        this.users.setHeight("250px");
        this.users.setWidth("600px");
        this.users.addColumn(User::getUsername).setCaption("User");
        this.users.addColumn(User::getRole).setCaption("Role");
        this.users.setSelectionMode(Grid.SelectionMode.SINGLE);
        this.users.deselectAll();
    }

    private void prepare_refresh() {
        this.refresh.addClickListener(e -> this.populate_grid());
    }
}

