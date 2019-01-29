/*
 * Decompiled with CFR 0.139.
 * 
 * Could not load the following classes:
 *  com.example.rmonitor.access.AccessControl
 *  com.example.rmonitor.access.LoginForm
 *  com.example.rmonitor.access.LoginForm$1
 *  com.example.rmonitor.access.LoginForm$LoginListener
 *  com.example.rmonitor.classes.ConnectionManager
 *  com.example.rmonitor.classes.ObjectConstructor
 *  com.vaadin.server.Sizeable
 *  com.vaadin.server.Sizeable$Unit
 *  com.vaadin.shared.Registration
 *  com.vaadin.ui.Alignment
 *  com.vaadin.ui.Button
 *  com.vaadin.ui.Button$ClickListener
 *  com.vaadin.ui.Component
 *  com.vaadin.ui.CssLayout
 *  com.vaadin.ui.FormLayout
 *  com.vaadin.ui.Notification
 *  com.vaadin.ui.Notification$Type
 *  com.vaadin.ui.PasswordField
 *  com.vaadin.ui.TextField
 *  com.vaadin.ui.VerticalLayout
 */
package com.example.rmonitor.access;

import com.example.rmonitor.access.AccessControl;
import com.example.rmonitor.access.LoginForm;
import com.example.rmonitor.classes.ConnectionManager;
import com.example.rmonitor.classes.ObjectConstructor;
import com.vaadin.event.ShortcutAction;
import com.vaadin.server.Sizeable;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Component;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.themes.ValoTheme;
import com.vaadin.ui.Notification;
import com.vaadin.ui.PasswordField;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import java.io.Serializable;

/*
 * Exception performing whole class analysis.
 */
public class LoginForm
extends CssLayout {
    private TextField username;
    private TextField password;
    private Button login;
    private AccessControl accessControl;
    private LoginListener loginListener;
    ConnectionManager manager;
    ObjectConstructor constructor;

    public LoginForm(AccessControl accessControl, LoginListener loginListener) {
        this.manager = new ConnectionManager();
        this.constructor = new ObjectConstructor();
        this.accessControl = accessControl;
        this.loginListener = loginListener;
        this.prepareLayout();
        this.username.focus();
    }

    private void prepareLayout() {
        this.addStyleName("login-screen");
        FormLayout loginForm = this.buildLoginForm();
        
        VerticalLayout centeringLayout = new VerticalLayout();
        centeringLayout.setMargin(false);
        centeringLayout.setSpacing(false);
        centeringLayout.setStyleName("centering-layout");
        centeringLayout.addComponent((Component)loginForm);
        centeringLayout.setComponentAlignment((Component)loginForm, Alignment.MIDDLE_CENTER);
        
        this.setSizeFull();
        this.addComponent((Component)centeringLayout);
    }

    private FormLayout buildLoginForm() {
        FormLayout loginForm = new FormLayout();
        loginForm.addStyleName("login-form");
        loginForm.setSizeUndefined();
        
        this.username = new TextField("Username");
        loginForm.addComponent((Component)this.username);
        this.username.setWidth(15.0f, Sizeable.Unit.EM);
        
        this.password = new PasswordField("Password");
        loginForm.addComponent((Component)this.password);
        this.password.setWidth(15.0f, Sizeable.Unit.EM);
        
        CssLayout buttons = new CssLayout();
        buttons.setStyleName("buttons");
        loginForm.addComponent((Component)buttons);
        
        this.login = new Button("Login");
        buttons.addComponent((Component)this.login);
        
        this.login.setClickShortcut(ShortcutAction.KeyCode.ENTER);
        this.login.addStyleName(ValoTheme.BUTTON_FRIENDLY);
        this.login.setDisableOnClick(true);
        login.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent event) {
                try {
                    login();
                } finally {
                    login.setEnabled(true);
                }
            }
        });
        return loginForm;
    }

    private void login() {
        this.manager.connect();
        String result = this.accessControl.signIn(this.manager, this.username.getValue(), this.password.getValue());
        this.manager.disconnect();
        if (this.accessControl.isRoleValid(result)) {
            System.out.println(result);
            this.loginListener.loginSuccessful();
        } else {
            this.username.focus();
            Notification.show((String)"Login failed", 
            		(String)"Please check your username and password and try again.", 
            		(Notification.Type)Notification.Type.HUMANIZED_MESSAGE);
            System.out.println(result);
        }
    }
    
    public interface LoginListener extends Serializable {
        void loginSuccessful();
    }

    /*
    static Button access$0(LoginForm loginForm) {
        return loginForm.login;
    }

    static void access$1(LoginForm loginForm) {
        loginForm.login();
    }
    */
}

