/*
 * Decompiled with CFR 0.139.
 * 
 * Could not load the following classes:
 *  com.example.rmonitor.layouts.UserManagementFormLayout
 *  com.vaadin.ui.Button
 *  com.vaadin.ui.Component
 *  com.vaadin.ui.HorizontalLayout
 *  com.vaadin.ui.NativeSelect
 *  com.vaadin.ui.Panel
 *  com.vaadin.ui.PasswordField
 *  com.vaadin.ui.TextField
 *  com.vaadin.ui.VerticalLayout
 */
package com.example.rmonitor.layouts;

import com.vaadin.ui.Button;
import com.vaadin.ui.Component;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.NativeSelect;
import com.vaadin.ui.Panel;
import com.vaadin.ui.PasswordField;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;

public class UserManagementFormLayout
extends VerticalLayout {
    protected Button changePass = new Button("Change Your Password");
    protected Button changeUserPass = new Button("Change Password");
    protected Button changeRole = new Button("Change Role");
    protected Button suspend = new Button("Suspend User");
    protected Button activate = new Button("Activate User");
    protected Button create = new Button("Create User");
    protected HorizontalLayout buttons = new HorizontalLayout();
    protected PasswordField old_password = new PasswordField("Please enter your current password");
    protected PasswordField new_password = new PasswordField("Enter your new password");
    protected PasswordField confirm_new_password = new PasswordField("Confirm your new password");
    protected Button new_password_go = new Button("Go");
    protected VerticalLayout new_pass = new VerticalLayout(new Component[]{this.old_password, this.new_password, this.confirm_new_password, this.new_password_go});
    protected PasswordField new_user_password = new PasswordField("Enter the new password");
    protected PasswordField confirm_user_new_password = new PasswordField("Confirm the new password");
    protected Button new_user_password_go = new Button("Go");
    protected VerticalLayout new_user_pass = new VerticalLayout(new Component[]{this.new_user_password, this.confirm_user_new_password, this.new_user_password_go});
    protected NativeSelect<String> roles = new NativeSelect<>("Role");
    protected Button role_go = new Button("Go");
    protected VerticalLayout change_role = new VerticalLayout(new Component[]{this.roles, this.role_go});
    protected TextField create_username = new TextField("Username");
    protected PasswordField create_password = new PasswordField("Password");
    protected PasswordField create_confirm_password = new PasswordField("Confirm password");
    protected NativeSelect<String> create_role = new NativeSelect<>("Role");
    protected Button create_go = new Button("Go");
    protected VerticalLayout create_user = new VerticalLayout(new Component[]{this.create_username, this.create_password, this.create_confirm_password, this.create_role, this.create_go});
    protected HorizontalLayout forms = new HorizontalLayout();
    protected Panel functions = new Panel();
}

