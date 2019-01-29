package com.example.rmonitor.layouts;

import com.example.rmonitor.classes.User;
import com.vaadin.navigator.View;
import com.vaadin.ui.Button;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.Grid;
import com.vaadin.ui.Label;
import com.vaadin.ui.Panel;
import com.vaadin.ui.VerticalLayout;

public class UserManagementLayout extends CssLayout implements View {
	protected Label username = new Label();
	protected Label timeKey = new Label();
	protected Label role = new Label();
	protected Label warning = new Label("WARNING: You may need to logout to see the changes reflected in the database.");
	
	protected Grid<User> users = new Grid<>();
	
	protected Button refresh = new Button("Refresh");
	
	protected Panel formFrame = new Panel();
	
	protected VerticalLayout userData = new VerticalLayout(username, role, timeKey, warning);
	protected VerticalLayout layout = new VerticalLayout();
}
