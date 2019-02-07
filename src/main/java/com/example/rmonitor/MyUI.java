package com.example.rmonitor;

import javax.servlet.annotation.WebServlet;

import com.example.rmonitor.access.LoginForm;
import com.example.rmonitor.access.LoginForm.LoginListener;
import com.example.rmonitor.access.AccessControl;
import com.example.rmonitor.access.CurrentUser;
import com.example.rmonitor.access.ServerAccessControl;
import com.example.rmonitor.classes.ConnectionManager;
import com.vaadin.annotations.Theme;
import com.vaadin.annotations.VaadinServletConfiguration;
import com.vaadin.server.Responsive;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinServlet;
import com.vaadin.server.VaadinSession;
import com.vaadin.ui.themes.ValoTheme;
import com.vaadin.ui.UI;

/**
 * This UI is the application entry point. A UI may either represent a browser window 
 * (or tab) or some part of an HTML page where a Vaadin application is embedded.
 * <p>
 * The UI is initialized using {@link #init(VaadinRequest)}. This method is intended to be 
 * overridden to add component to the user interface and initialize non-component functionality.
 */
@Theme("mytheme")
public class MyUI extends UI {
	private AccessControl accessControl = new ServerAccessControl();
	private String userID = "";
	private String userName = "";

    @Override
    protected void init(VaadinRequest vaadinRequest) {
    	// if the client dies/times-out, we want the server to logout the user from database
    	// so they can log in again after
    	addDetachListener(new DetachListener() {
    		@Override
            public void detach(DetachEvent event) {
    			ConnectionManager manager = new ConnectionManager();
    			manager.connect();
            	manager.send(String.format("quit%s%s", userID, userName));
            	manager.disconnect();
                VaadinSession.getCurrent().getSession().invalidate();
    		}
    	});
    	
    	
    	// setup the webpage
    	Responsive.makeResponsive(this);
    	setLocale(vaadinRequest.getLocale());
        getPage().setTitle("Rental Monitoring");
        
        //user must be logged in to access the system
        if (!accessControl.isUserSignedIn()) {
        	//add the login form if not signed in
        	setContent(new LoginForm(accessControl, new LoginListener() {
                @Override
                //let the user in if login was successful
                public void loginSuccessful() {
                	userID = CurrentUser.getId();
                	userName = CurrentUser.getName();
                    getMainPage();
                }
            }));
        } else {
        	//already logged in
        	userID = CurrentUser.getId();
        	userName = CurrentUser.getName();
            getMainPage();
        }
    }
    
    //set the system view
    protected void getMainPage() {
    	addStyleName(ValoTheme.UI_WITH_MENU);
        setContent(new MainScreen(MyUI.this));
        getNavigator().navigateTo(getNavigator().getState());
    }
    
    public static MyUI get() {
        return (MyUI) UI.getCurrent();
    }

    //@VaadinServletConfiguration(ui = MyUI.class, productionMode = false)
    
    @WebServlet(urlPatterns = "/*", name = "MyUIServlet", asyncSupported = true)
    @VaadinServletConfiguration(ui = MyUI.class, productionMode = true)
    public static class MyUIServlet extends VaadinServlet {
    }
}
