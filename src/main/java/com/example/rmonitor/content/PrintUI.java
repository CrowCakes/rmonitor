/*
 * Decompiled with CFR 0.139.
 * 
 * Could not load the following classes:
 *  com.example.rmonitor.access.CurrentUser
 *  com.example.rmonitor.classes.ConnectionManager
 *  com.example.rmonitor.classes.ObjectConstructor
 *  com.example.rmonitor.content.PrintUI
 *  com.vaadin.server.Resource
 *  com.vaadin.server.ThemeResource
 *  com.vaadin.server.VaadinRequest
 *  com.vaadin.ui.BrowserFrame
 *  com.vaadin.ui.UI
 */
package com.example.rmonitor.content;

import com.example.rmonitor.access.CurrentUser;
import com.example.rmonitor.classes.ConnectionManager;
import com.example.rmonitor.classes.ObjectConstructor;
import com.vaadin.server.Resource;
import com.vaadin.server.ThemeResource;
import com.vaadin.server.VaadinRequest;
import com.vaadin.ui.BrowserFrame;
import com.vaadin.ui.UI;
import java.io.File;

public class PrintUI
extends UI {
    ConnectionManager manager = new ConnectionManager();
    ObjectConstructor constructor = new ObjectConstructor();

    protected void init(VaadinRequest request) {
        ThemeResource tr = new ThemeResource(String.format("html" + File.separator + "%s_dform.html", CurrentUser.get()));
        BrowserFrame embedded = new BrowserFrame("html", (Resource)tr);
        embedded.setHeight("768px");
        embedded.setWidth("1360px");
    }
}

