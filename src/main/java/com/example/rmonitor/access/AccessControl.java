/*
 * Decompiled with CFR 0.139.
 * 
 * Could not load the following classes:
 *  com.example.rmonitor.access.AccessControl
 *  com.example.rmonitor.classes.ConnectionManager
 */
package com.example.rmonitor.access;

import com.example.rmonitor.classes.ConnectionManager;
import java.io.Serializable;

public interface AccessControl
extends Serializable {
    public String signIn(ConnectionManager var1, String var2, String var3);

    public boolean isUserSignedIn();

    public boolean isUserInRole(String var1);

    public boolean isRoleValid(String var1);

    public String getPrincipalName();
}

