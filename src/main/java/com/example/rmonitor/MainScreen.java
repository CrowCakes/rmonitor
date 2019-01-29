package com.example.rmonitor;

import com.example.rmonitor.access.CurrentUser;
import com.example.rmonitor.content.AccessoriesView;
import com.example.rmonitor.content.AssetManagement;
import com.example.rmonitor.content.ClientView;
import com.example.rmonitor.content.ComputerView;
import com.example.rmonitor.content.DeliveriesView;
import com.example.rmonitor.content.MainPage;
import com.example.rmonitor.content.PartsView;
import com.example.rmonitor.content.PullView;
import com.example.rmonitor.content.SmallAccView;
import com.example.rmonitor.content.UserManagement;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.navigator.Navigator;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.HorizontalLayout;

public class MainScreen extends HorizontalLayout {
	private Menu menu;
	
	public MainScreen(MyUI ui) {
		setSpacing(false);
		setStyleName("main-screen");
		
		CssLayout viewContainer = new CssLayout();
        viewContainer.addStyleName("valo-content");
        viewContainer.setSizeFull();
        
        //set up system navigation
        final Navigator navigator = new Navigator(ui, viewContainer);
        navigator.setErrorView(Error.class);
        
        //set up the menu for system navigation
        menu = new Menu(navigator);
        //System.out.println("CurrentUser.get() is ".concat(CurrentUser.get()));
        //System.out.println("CurrentUser.getId() is ".concat(CurrentUser.getId()));
        menu.addView(new DeliveriesView(CurrentUser.get()), DeliveriesView.VIEW_NAME, DeliveriesView.VIEW_NAME, VaadinIcons.PACKAGE);
        menu.addView(new MainPage(CurrentUser.get()), "", "Main Page", VaadinIcons.CHART);
        menu.addView(new PullView(), "Pull-out", "Pull-out", VaadinIcons.CHECK_SQUARE);
        menu.addView(new ComputerView(CurrentUser.get()), ComputerView.VIEW_NAME, ComputerView.VIEW_NAME, VaadinIcons.DESKTOP);
        menu.addView(new PartsView(CurrentUser.get()), PartsView.VIEW_NAME, PartsView.VIEW_NAME, VaadinIcons.OPTIONS);
        menu.addView(new AccessoriesView(CurrentUser.get()), AccessoriesView.VIEW_NAME, AccessoriesView.VIEW_NAME, VaadinIcons.KEYBOARD);
        menu.addView(new SmallAccView(CurrentUser.get()), "Small Accessories", "Small Accessories", VaadinIcons.KEYBOARD);
        menu.addView(new ClientView(CurrentUser.get()), ClientView.VIEW_NAME, ClientView.VIEW_NAME, VaadinIcons.USERS);
        if (CurrentUser.get().equals("Super")) {
        	menu.addView(new AssetManagement(), AssetManagement.VIEW_NAME, AssetManagement.VIEW_NAME, VaadinIcons.CHECK);
        }
        menu.addView(new UserManagement(CurrentUser.get()), "User Management", "User Management", VaadinIcons.DOCTOR);
        
        navigator.addViewChangeListener(viewChangeListener);
        
        //add all the things to menu bar
        addComponent(menu);
        addComponent(viewContainer);
        setExpandRatio(viewContainer, 1);
        setSizeFull();
	}
	
	ViewChangeListener viewChangeListener = new ViewChangeListener() {

        @Override
        public boolean beforeViewChange(ViewChangeEvent event) {
            return true;
        }

        @Override
        public void afterViewChange(ViewChangeEvent event) {
            menu.setActiveView(event.getViewName());
        }

    };
}
