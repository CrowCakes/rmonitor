package com.example.rmonitor.content;

import com.example.rmonitor.classes.ConnectionManager;
import com.example.rmonitor.classes.ObjectConstructor;
import com.example.rmonitor.classes.SmallAccessory;
import com.example.rmonitor.content.SmallAccessoryForm;
import com.vaadin.navigator.View;
import com.vaadin.shared.ui.ValueChangeMode;
import com.vaadin.ui.Button;
import com.vaadin.ui.Component;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.Grid;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import java.util.ArrayList;
import java.util.List;

public class SmallAccView
extends CssLayout
implements View {
    public static final String VIEW_NAME = "SmallAcc";
    ConnectionManager manager = new ConnectionManager();
    ObjectConstructor constructor = new ObjectConstructor();
    Grid<SmallAccessory> display_accessories = new Grid<>();
    private SmallAccessoryForm acc_form = new SmallAccessoryForm(this);
    HorizontalLayout button_row;
    VerticalLayout main = new VerticalLayout();
    TextField filter;

    public SmallAccView(String user) {
        this.setSizeFull();
        this.button_row = this.buttonsLayout(user);
        this.prepare_grid(user);
        this.main.addComponents(new Component[]{this.button_row, this.display_accessories, this.acc_form});
        this.addComponent((Component)this.main);
        this.refreshView();
    }

    private HorizontalLayout buttonsLayout(String user) {
        HorizontalLayout row = new HorizontalLayout();
        this.filter = new TextField();
        this.filter.setPlaceholder("Filter by Name");
        this.filter.addValueChangeListener(e -> this.updateList());
        this.filter.setValueChangeMode(ValueChangeMode.LAZY);
        
        Button ViewSmallAccessories = new Button("Refresh");
        ViewSmallAccessories.addClickListener(e -> this.refreshView());
        
        Button addSmallAcc = new Button("Create New Accessory");
        addSmallAcc.addClickListener(e -> {
            this.clearSelection();
            this.acc_form.setAccessory(new SmallAccessory("", "", 0.0f, 0, 0));
        });
        if (!user.equals("Admin")) {
            addSmallAcc.setEnabled(false);
        }
        
        row.addComponents(new Component[]{this.filter, ViewSmallAccessories, addSmallAcc});
        return row;
    }

    private void prepare_grid(String user) {
        this.display_accessories.addColumn(SmallAccessory::getName).setCaption("Name");
        this.display_accessories.addColumn(SmallAccessory::getAccessoryType).setCaption("Type");
        this.display_accessories.addColumn(SmallAccessory::getQuantity).setCaption("Qty");
        this.display_accessories.addColumn(SmallAccessory::getQuantityMinus).setCaption("Qty Taken");
        this.display_accessories.addColumn(SmallAccessory::getQuantityRemaining).setCaption("Qty Left");
        this.display_accessories.setHeight("300px");
        this.display_accessories.setWidth("1000px");
        
        if (user.equals("Admin")) {
            this.display_accessories.asSingleSelect().addValueChangeListener(event -> {
                if (event.getValue() == null) {
                    this.acc_form.setVisible(false);
                } else {
                    this.acc_form.setAccessory((SmallAccessory)event.getValue());
                }
            });
        }
    }

    public void refreshView() {
        int status = this.manager.connect();
        List<SmallAccessory> parts = new ArrayList<>();
        if (status == 1) {
            parts = this.constructor.constructSmallAccessories(this.manager);
            this.display_accessories.setItems(parts);
        } else {
            this.display_accessories.removeAllColumns();
        }
        status = this.manager.disconnect();
    }

    public void clearSelection() {
        this.display_accessories.getSelectionModel().deselectAll();
    }

    private void updateList() {
        List<SmallAccessory> computers = new ArrayList<>();
        
        this.manager.connect();
        computers = this.constructor.constructSmallAccessories(this.manager, this.filter.getValue());
        this.manager.disconnect();
        
        this.display_accessories.setItems(computers);
    }
}

