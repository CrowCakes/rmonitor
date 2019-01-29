package com.example.rmonitor.access;

import com.example.rmonitor.access.AccessControl;
import com.example.rmonitor.access.CurrentUser;
import com.example.rmonitor.classes.ConnectionManager;
import java.util.ArrayList;
import java.util.Arrays;

public class ServerAccessControl
implements AccessControl {
    public String signIn(ConnectionManager manager, String username, String password) {
        String result = manager.send(String.format("Login\r\n%s\r\n%s", username, password));
        System.out.println("Received login cred");
        if (!result.isEmpty()) {
            ArrayList<String> bar = new ArrayList<String>(Arrays.asList(result.split("\n")));
            if (bar.size() > 1) {
                CurrentUser.set((String)((String)bar.get(0)), (String)((String)bar.get(1)), (String)username);
                result = (String)bar.get(0);
            } else {
                result = "fail";
            }
        } else {
            result = "fail";
        }
        System.out.println("Finished authenticating");
        return result;
    }

    public boolean isUserSignedIn() {
        return !CurrentUser.get().isEmpty();
    }

    public boolean isUserInRole(String role) {
        if ("Admin".equals(role)) {
            return this.getPrincipalName().equals("Admin");
        }
        if ("Reports".equals(role)) {
            return this.getPrincipalName().equals("Reports");
        }
        if ("Viewer".equals(role)) {
            return this.getPrincipalName().equals("Viewer");
        }
        if ("Super".equals(role)) {
            return this.getPrincipalName().equals("Super");
        }
        return this.isRoleValid(role);
    }

    public boolean isRoleValid(String role) {
        return "Admin".equals(role) || "Reports".equals(role) || "Viewer".equals(role) || "Super".equals(role);
    }

    public String getPrincipalName() {
        return CurrentUser.get();
    }
}

