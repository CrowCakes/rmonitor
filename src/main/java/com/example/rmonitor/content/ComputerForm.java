package com.example.rmonitor.content;

import com.example.rmonitor.classes.Computer;
import com.example.rmonitor.classes.ConnectionManager;
import com.example.rmonitor.classes.Delivery;
import com.example.rmonitor.classes.ObjectConstructor;
import com.example.rmonitor.classes.Parts;
import com.example.rmonitor.content.ComputerForm;
import com.example.rmonitor.content.ComputerView;
import com.example.rmonitor.layouts.ComputerFormLayout;
import com.example.rmonitor.layouts.ComputerLayout;
import com.vaadin.data.Binder;
import com.vaadin.shared.ui.ValueChangeMode;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Component;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Panel;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.vaadin.dialogs.ConfirmDialog;

public class ComputerForm
extends ComputerFormLayout {
    private int NUMBER_OF_PARTS;
    private Computer comp;
    private ComputerView comp_view;
    private ComputerLayout comp_layout;
    private ConnectionManager manager;
    private ObjectConstructor constructor;
    private List<Parts> parts;
    private List<Parts> old_parts;
    private List<Parts> foo;
    private String old_rental;
    private Binder<Computer> binder;
    private String function;
    private Boolean reset_flag;

    /***
     * Sets up the form used to create and edit Computers.
     * @param comp_view - the parent module which this form is attached to
     */
    public ComputerForm(ComputerView comp_view) {
        this.NUMBER_OF_PARTS = 9;
        this.manager = new ConnectionManager();
        this.constructor = new ObjectConstructor();
        this.parts = new ArrayList<>();
        this.old_parts = new ArrayList<>();
        this.foo = new ArrayList<>();
        this.binder = new Binder<>(Computer.class);
        this.reset_flag = false;
        this.comp_view = comp_view;
        
        this.setup_buttons();
        this.setup_grid();
        
        VerticalLayout main_column = new VerticalLayout();
        main_column.addComponents(
        		new Component[]{
        				new HorizontalLayout(
        						new Component[]{
        								new FormLayout(
        										new Component[]{this.rn, this.cpu, this.pctype, this.os, this.pur}), 
        								new FormLayout(new Component[]{this.status, this.history_count, this.parent_delivery})}), 
        				this.parts_menu, 
        				this.save_cancel});
        HorizontalLayout final_form = new HorizontalLayout(new Component[]{main_column, new VerticalLayout(new Component[]{this.description, this.select_menu})});
        Panel panel = new Panel((Component)final_form);
        panel.setHeight("650px");
        panel.setWidth("1500px");
        this.addComponents(new Component[]{panel});
        this.setVisible(false);
        this.setSizeUndefined();
        this.setMargin(false);
        this.save.setStyleName("primary");
    }

    /***
     * Sets up the form used to create and edit Computers, to be used in the Asset Management module.
     * @param comp_layout - the parent module to which this form is attached
     */
    public ComputerForm(ComputerLayout comp_layout) {
        this.NUMBER_OF_PARTS = 9;
        this.manager = new ConnectionManager();
        this.constructor = new ObjectConstructor();
        this.parts = new ArrayList<>();
        this.old_parts = new ArrayList<>();
        this.foo = new ArrayList<>();
        this.binder = new Binder<>(Computer.class);
        this.reset_flag = false;
        this.comp_layout = comp_layout;
        this.setup_buttons();
        this.setup_grid();
        VerticalLayout main_column = new VerticalLayout();
        main_column.addComponents(
        		new Component[]{
        				new HorizontalLayout(
        						new Component[]{
        								new FormLayout(new Component[]{this.rn, this.cpu, this.pctype, this.os, this.pur}), 
        								new FormLayout(new Component[]{this.status, this.history_count, this.parent_delivery})}), 
        				this.parts_menu, 
        				this.save_cancel});
        HorizontalLayout final_form = new HorizontalLayout(new Component[]{main_column, new VerticalLayout(new Component[]{this.description, this.select_menu})});
        Panel panel = new Panel((Component)final_form);
        panel.setHeight("650px");
        panel.setWidth("1050px");
        this.addComponents(new Component[]{panel});
        this.setComponentAlignment((Component)panel, Alignment.TOP_LEFT);
        this.setVisible(false);
        this.setSizeUndefined();
        this.save.setStyleName("primary");
    }

    /***
     * Formats the grid that will display the list of Computers.
     */
    private void setup_grid() {
        this.display_computers_parts.addColumn(Parts::getPartID).setCaption("Part ID");
        this.display_computers_parts.addColumn(Parts::getName).setCaption("Name");
        this.display_computers_parts.addColumn(Parts::getPartType).setCaption("Part Type");
        this.display_computers_parts.addColumn(Parts::getStatus).setCaption("Status");
        this.display_computers_parts.setHeightByRows(1.0);
        
        this.available_parts.addColumn(Parts::getPartID).setCaption("Part ID");
        this.available_parts.addColumn(Parts::getName).setCaption("Name");
        this.available_parts.addColumn(Parts::getPartType).setCaption("Part Type");
        this.available_parts.setWidth("300px");
        this.available_parts.setHeightByRows(5.0);
        
        this.select_menu.setVisible(false);
        this.filter.setPlaceholder("Filter by PartID");
        this.filter.addValueChangeListener(e -> this.updateList());
        this.filter.setValueChangeMode(ValueChangeMode.LAZY);
        
        this.addpart.addClickListener(e -> {
            this.description.setVisible(false);
            this.revealPartsList("Add");
        });
        this.editpart.addClickListener(e -> {
            this.description.setVisible(false);
            this.editPart();
        });
        this.delpart.addClickListener(e -> {
            this.description.setVisible(false);
            this.deletePart();
        });
        this.choosepart.addClickListener(e -> {
            this.addParts(this.function);
            this.description.setVisible(true);
        });
        this.cancelpart.addClickListener(e -> {
            this.cancelParts();
            this.description.setVisible(true);
        });
    }

    /***
     * Prepares the functions that will be attached to the form buttons.
     */
    private void setup_buttons() {
        this.prepare_fields();
        this.save.addClickListener(e -> {
            if (this.validate()) {
                this.save();
            } else {
                Notification.show((String)"Error", 
                		(String)"Something's wrong with the entered Computer data.\r\nPlease check all the fields are entered.", 
                		(Notification.Type)Notification.Type.ERROR_MESSAGE);
            }
        });
        this.delete.addClickListener(e -> {
            this.save_cancel.setVisible(false);
            this.part_buttons.setVisible(false);
            ConfirmDialog.show((UI)this.getUI(), 
            		(String)"Confirmation", 
            		(String)"Are you sure you want to delete this Computer?", 
            		(String)"Yes", (String)"No", 
            		new ConfirmDialog.Listener() {
            	public void onClose(ConfirmDialog dialog) {
            		if (dialog.isConfirmed()) {
                        // Confirmed to continue
                        delete();
                        
                    	part_buttons.setVisible(true);
                        save_cancel.setVisible(true);
                    } else {
                        // User did not confirm
                        
                    	part_buttons.setVisible(true);
                        save_cancel.setVisible(true);
                    }
            	}
            });
        });
        this.cancel.addClickListener(e -> this.cancel());
        this.resetpart.addClickListener(e -> {
            this.save_cancel.setVisible(false);
            this.part_buttons.setVisible(false);
            ConfirmDialog.show((UI)this.getUI(), (String)"Confirmation", 
            		(String)"This will set current Parts to the original Parts of this Computer. Are you sure?", 
            		(String)"Yes", (String)"No", 
            		new ConfirmDialog.Listener() {
            	public void onClose(ConfirmDialog dialog) {
            		if (dialog.isConfirmed()) {
            			reset_parts();
            			
            			part_buttons.setVisible(true);
                        save_cancel.setVisible(true);
            		}
            		else {
            			part_buttons.setVisible(true);
                        save_cancel.setVisible(true);
            		}
            	}
            });
        });
    }

    /***
     * Prepares the fields to be filled out in the form.
     */
    private void prepare_fields() {
        this.binder.bind(this.rn, Computer::getRentalNumber, Computer::setRentalNumber);
        this.binder.bind(this.cpu, Computer::getCpu, Computer::setCpu);
        this.binder.bind(this.pctype, Computer::getPcType, Computer::setPcType);
        this.binder.bind(this.os, Computer::getOs, Computer::setOs);
        this.binder.bind(this.pur, Computer::getPurchaseLocalDate, Computer::setPurchaseDateFromLocal);
        this.binder.bind(this.description, Computer::getDescription, Computer::setDescription);
        this.binder.bind(this.price, Computer::getPriceStr, Computer::setPrice);
        this.binder.bind(this.status, Computer::getStatus, Computer::setStatus);
        
        this.price.setReadOnly(true);
        this.status.setReadOnly(true);
        
        this.description.setWordWrap(true);
        this.description.setHeight("500px");
        this.description.setWidth("300px");
        
        this.binder.bindInstanceFields((Object)this);
        this.rn.setRequiredIndicatorVisible(true);
        this.cpu.setRequiredIndicatorVisible(true);
        this.pctype.setRequiredIndicatorVisible(true);
        this.os.setRequiredIndicatorVisible(true);
        this.pur.setRequiredIndicatorVisible(true);
        
        this.pctype.addValueChangeListener(e -> {
            if (this.pctype.getValue().equals("Laptop")) {
                this.price.setReadOnly(false);
            } else {
                this.price.setValue(this.parts_price_sum());
                this.price.setReadOnly(true);
            }
        });
        this.status.addValueChangeListener(e -> {
            if (this.status.getValue().equals("Unavailable")) {
                this.manager.connect();
                Delivery d = this.constructor.findParentDelivery(this.manager, this.old_rental);
                this.manager.disconnect();
                if (d != null && d.getDeliveryID() > 0) {
                    this.parent_delivery.setValue(String.format("Currently in DeliveryID# %d", d.getDeliveryID()));
                    this.parent_delivery.setVisible(true);
                } else {
                    this.parent_delivery.setVisible(false);
                }
            } else {
                this.parent_delivery.setVisible(false);
            }
        });
    }

    /***
     * Removes the selected Part from the Computer.
     * This is a soft operation, so it does not actually affect the server database.
     */
    private void deletePart() {
        try {
            System.out.println("DELETE: ".concat(((Parts)this.display_computers_parts.asSingleSelect().getValue()).getStringRepresentation()));
            for (int i = 0; i < this.parts.size(); ++i) {
                if (((Parts)this.parts.get(i)).getPartID() != ((Parts)this.display_computers_parts.asSingleSelect().getValue()).getPartID()) continue;
                this.parts.remove(i);
                break;
            }
            this.refreshRentalParts();
            this.reset_flag = false;
            this.price.setValue(this.parts_price_sum());
            this.price.setReadOnly(true);
            Notification.show((String)"Delete Part", (String)"Successfully removed the Part!", (Notification.Type)Notification.Type.HUMANIZED_MESSAGE);
        }
        catch (NullPointerException ex) {
            Notification.show((String)"Error", (String)"Please select a Part to remove", (Notification.Type)Notification.Type.ERROR_MESSAGE);
        }
    }

    /***
     * Tells the form that the next operation to be done is editing a selected Part. Does not actually modify the selected Part.
     */
    private void editPart() {
        try {
            System.out.println("SELECTED: ".concat(((Parts)this.display_computers_parts.asSingleSelect().getValue()).getStringRepresentation()));
            this.revealPartsList("Edit");
        }
        catch (NullPointerException ex) {
            Notification.show((String)"Error", (String)"Please select a Part to edit", (Notification.Type)Notification.Type.ERROR_MESSAGE);
        }
    }

    /***
     * Adds or changes a Part belonging to the Computer.
     * This is a soft operation, so it does not actually affect the server database.
     * @param check - string denoting which operation to perform
     */
    private void addParts(String check) {
        if (check == "Add") {
            try {
                System.out.println("ADD: ".concat(((Parts)this.available_parts.asSingleSelect().getValue()).getStringRepresentation()));
                this.parts.add((Parts)this.available_parts.asSingleSelect().getValue());
                
                this.refreshRentalParts();
                this.available_parts.deselectAll();
                this.select_menu.setVisible(false);
                this.part_buttons.setVisible(true);
                this.save_cancel.setVisible(true);
                this.reset_flag = false;
                
                this.price.setValue(this.parts_price_sum());
                this.price.setReadOnly(true);
                Notification.show((String)"Add Part", (String)"Successfully added the Part!", (Notification.Type)Notification.Type.HUMANIZED_MESSAGE);
            }
            catch (NullPointerException ex) {
                Notification.show((String)"Error", (String)"Please select at least 1 Part to add", (Notification.Type)Notification.Type.ERROR_MESSAGE);
            }
        } else {
            try {
                System.out.println("CHANGE TO: ".concat(((Parts)this.available_parts.asSingleSelect().getValue()).getStringRepresentation()));
                for (int i = 0; i < this.parts.size(); ++i) {
                    if (((Parts)this.parts.get(i)).getPartID() != ((Parts)this.display_computers_parts.asSingleSelect().getValue()).getPartID()) continue;
                    this.parts.remove(this.parts.get(i));
                    break;
                }
                this.parts.add((Parts)this.available_parts.asSingleSelect().getValue());
                this.refreshRentalParts();
                
                this.available_parts.deselectAll();
                this.select_menu.setVisible(false);
                this.part_buttons.setVisible(true);
                this.save_cancel.setVisible(true);
                
                this.reset_flag = false;
                this.price.setValue(this.parts_price_sum());
                this.price.setReadOnly(true);
                Notification.show((String)"Edit Part", (String)"Successfully edited the Part!", (Notification.Type)Notification.Type.HUMANIZED_MESSAGE);
            }
            catch (NullPointerException ex) {
                Notification.show((String)"Error", (String)"Please select at least 1 Part to edit", (Notification.Type)Notification.Type.ERROR_MESSAGE);
            }
        }
    }

    /***
     * Hides the Part selection menus.
     */
    private void cancelParts() {
        this.select_menu.setVisible(false);
        this.part_buttons.setVisible(true);
        this.save_cancel.setVisible(true);
    }

    /***
     * Fetches the list of available Parts, then reveals the Part selection menu for adding or editing Parts.
     * @param check - the next operation to perform ("Add" or "Edit")
     */
    private void revealPartsList(String check) {
    	this.foo.clear();
    	
        this.manager.connect();
        this.foo = this.constructor.findAvailableParts(this.manager);
        this.manager.disconnect();
        
        this.available_parts.setItems(this.foo);
        
        this.function = check == "Add" ? "Add" : "Edit";
        this.select_menu.setVisible(true);
        this.part_buttons.setVisible(false);
        this.save_cancel.setVisible(false);
    }

    /***
     * Attaches a selected Computer to the form, then fetches its Parts data from the database.
     * @param comp - the selected Computer from the parent module
     */
    public void setComputer(Computer comp) {
        this.comp = comp;
        this.old_rental = comp.getRentalNumber();
        this.parts.clear();
        this.old_parts.clear();
        
        //get the full Parts data belonging to the Computer
        for (int i = 0; i < comp.getPartIDs().size(); ++i) {
            this.manager.connect();
            List<Parts> foo = this.constructor.fetchParts(this.manager, ((Integer)comp.getPartIDs().get(i)).intValue());
            
            //make sure not to include 1
            for (Parts x : parts) {
                if (x.getPartID() > 1) {
                	String query = String.format("TraceComputer\r\n%s", x.getPartID());
                    
                    this.manager.connect();
                    String parent = this.manager.send(query).trim();
                    parent = parent.replace(":", "");
                    this.manager.disconnect();
                    
                    x.setParent(parent);
                    
                    manager.connect();
                    parent = constructor.findOriginalComputer(manager, x.getPartID()).getRentalNumber();
                    manager.disconnect();
                    
                    x.setOriginalParent(parent);
                }
            }
            
            this.parts.addAll(foo);
            this.old_parts.addAll(foo);
            this.manager.disconnect();
        }
        if (comp.getPartIDs().size() < 1) {
            this.display_computers_parts.setHeightByRows(1.0);
            this.display_computers_parts.setItems(Collections.emptyList());
        } else {
            this.display_computers_parts.setHeightByRows((double)this.parts.size());
            this.display_computers_parts.setItems(Collections.emptyList());
            this.display_computers_parts.setItems(this.parts);
        }
        
        manager.connect();
        this.history_count.setValue(
        		String.format("Number of times released: %d times", 
        				constructor.constructRentalUnitHistory(manager, comp.getRentalNumber()).size())
        		);
        manager.disconnect();
        
        this.binder.setBean(comp);
        this.delete.setVisible(false);
        this.setVisible(true);
    }

    /***
     * Fetches the Computer's original set of Parts, then sets the form's currently tracked Parts to that.
     */
    private void reset_parts() {
        this.manager.connect();
        List<Parts> foo = this.constructor.fetchOriginalParts(this.manager, this.old_rental);
        this.manager.disconnect();
        
        this.reset_flag = true;
        this.parts = foo;

        this.refreshRentalParts();
        this.display_computers_parts.setItems(this.parts);
        this.price.setValue(this.parts_price_sum());
    }

    /***
     * Hides the form and refreshes the parent module.
     */
    private void cancel() {
        if (this.comp_view != null) {
            this.comp_view.clearSelection();
            this.setVisible(false);
            this.comp_view.resetView();
        } else {
            this.comp_layout.clearSelection();
            this.setVisible(false);
            this.comp_layout.resetView();
        }
    }

    /***
     * Deletes the Computer from the database then returns its Parts to the inventory. The form is then hidden.
     */
    private void delete() {
        this.manager.connect();
        String query = String.format("DeleteComputer\r\n%s", this.old_rental);
        String result = this.manager.send(query);
        this.manager.disconnect();
        Notification.show((String)"Delete Computer", (String)result, (Notification.Type)Notification.Type.HUMANIZED_MESSAGE);
        this.setVisible(false);
        if (this.comp_view != null) {
            this.comp_view.resetView();
        } else {
            this.comp_layout.resetView();
        }
    }

    /***
     * Sends the field inputs to the database. A new entry is created or an existing entry is modified in the database 
     * depending on the field inputs.
     */
    private void save() {
        this.manager.connect();
        Boolean test1 = this.constructor.isComputerExisting(this.manager, this.old_rental);
        this.manager.disconnect();
        this.manager.connect();
        Boolean test2 = this.constructor.isComputerExisting(this.manager, this.comp.getRentalNumber());
        this.manager.disconnect();
        
        if (this.old_rental == this.comp.getRentalNumber() || test1.booleanValue() && !test2.booleanValue() || this.isPartsEdited() == "True") {
            int x;
            ArrayList<String> foo = new ArrayList<String>();
            List<Integer> bar = new ArrayList<>();
            
            this.manager.connect();
            bar = this.constructor.findListIDs(this.manager, this.old_rental);
            this.manager.disconnect();
            
            String desc = this.comp.getDescription().replace("\n", "").replace("\r", "").isEmpty() ? 
            		"n/a" : this.comp.getDescription().replace("\n", "").replace("\r", "");
            /*
            String base = String.format("EditComputer\r\n%s\r\n%s\r\n%s\r\n%s\r\n%s\r\n%tF\r\n%s\r\n%s\r\n%s\r\n", 
            		this.old_rental, 
            		this.comp.getRentalNumber(), 
            		this.comp.getCpu(), 
            		this.comp.getPcType(), 
            		this.comp.getOs(), 
            		this.comp.getPurchaseDate(), 
            		this.isPartsEdited(), 
            		Float.valueOf(this.comp.getPrice()), 
            		desc);*/
            
            List<String> parameters = new ArrayList<>();
            parameters.add(this.old_rental);
            parameters.add(this.comp.getRentalNumber());
            parameters.add(this.comp.getCpu());
            parameters.add(this.comp.getPcType());
            parameters.add(this.comp.getOs());
            parameters.add(String.valueOf(this.comp.getPurchaseDate()));
            parameters.add(this.isPartsEdited());
            parameters.add(String.valueOf(Float.valueOf(this.comp.getPrice())));
            parameters.add(desc);
            
            String base = constructor.constructMessage("EditComputer", parameters);
            
            StringBuilder query = new StringBuilder(base);
            
            ArrayList<Integer> unique_parts = new ArrayList<Integer>();
            for (x = 0; x < this.parts.size(); ++x) {
                if (unique_parts.contains(((Parts)this.parts.get(x)).getPartID())) continue;
                unique_parts.add(((Parts)this.parts.get(x)).getPartID());
                foo.add(String.valueOf(((Parts)this.parts.get(x)).getPartID()));
            }
            
            Collections.sort(foo);
            Collections.sort(bar);
            
            for (x = 0; x < foo.size(); ++x) {
                query.append((String)foo.get(x));
                query.append("\r\n");
            }
            for (x = 0; x < this.NUMBER_OF_PARTS - foo.size(); ++x) {
                query.append("1\r\n");
            }
            
            for (x = 0; x < this.old_parts.size(); ++x) {
                query.append(String.valueOf(bar.get(x)));
                query.append("\r\n");
            }
            for (x = 0; x < this.NUMBER_OF_PARTS - this.old_parts.size(); ++x) {
                query.append("0\r\n");
            }
            
            this.manager.connect();
            String result = this.manager.send(query.toString());
            this.manager.disconnect();
            
            Notification.show((String)"Edit Computer", (String)result, (Notification.Type)Notification.Type.HUMANIZED_MESSAGE);
        } 
        else {
            String desc = this.comp.getDescription().replace("\n", "").replace("\r", "").isEmpty() ? 
            		"None" : this.comp.getDescription().replace("\n", "").replace("\r", "");
            
            /*String base = String.format("InsertNewRentalComputer\r\n%s\r\n%s\r\n%s\r\n%s\r\n%tF\r\n%s\r\n%s\r\n%s\r\n", 
            		this.comp.getRentalNumber(), 
            		this.comp.getCpu(), 
            		this.comp.getPcType(), 
            		this.comp.getOs(), 
            		this.comp.getPurchaseDate(), 
            		this.isPartsEdited(), 
            		Float.valueOf(this.comp.getPrice()), 
            		desc);*/
            
            List<String> parameters = new ArrayList<>();
            parameters.add(this.comp.getRentalNumber());
            parameters.add(this.comp.getCpu());
            parameters.add(this.comp.getPcType());
            parameters.add(this.comp.getOs());
            parameters.add(String.valueOf(this.comp.getPurchaseDate()));
            parameters.add(this.isPartsEdited());
            parameters.add(String.valueOf(Float.valueOf(this.comp.getPrice())));
            parameters.add(desc);
            
            String base = constructor.constructMessage("InsertNewRentalComputer", parameters);
            
            StringBuilder query = new StringBuilder(base);
            
            ArrayList<Integer> unique_parts = new ArrayList<Integer>();
            for (int x = 0; x < this.parts.size(); ++x) {
                if (unique_parts.contains(((Parts)this.parts.get(x)).getPartID())) continue;
                unique_parts.add(((Parts)this.parts.get(x)).getPartID());
                query.append(String.valueOf(((Parts)this.parts.get(x)).getPartID()));
                query.append("\r\n");
            }
            for (int y = 0; y < this.NUMBER_OF_PARTS - unique_parts.size(); ++y) {
                query.append("1\r\n");
            }
            
            this.manager.connect();
            String result = this.manager.send(query.toString());
            this.manager.disconnect();
            
            Notification.show((String)"Create New Computer", (String)result, (Notification.Type)Notification.Type.HUMANIZED_MESSAGE);
        }
        this.setVisible(false);
        if (this.comp_view != null) {
            this.comp_view.resetView();
        } else {
            this.comp_layout.resetView();
        }
    }

    /***
     * Fetches the latest list of available Parts from the database, then displays them in the Part selection menu.
     */
    private void updateList() {
        List<Parts> parts = new ArrayList<>();
        this.manager.connect();
        try {
            parts = this.constructor.filterAvailableParts(this.manager, this.filter.getValue());
            this.manager.disconnect();
            
            for (Parts x: parts) {
            	if (x.getPartID() > 1) {
            		String query = String.format("TraceComputer\r\n%s", x.getPartID());
                    
                    this.manager.connect();
                    String parent = this.manager.send(query).trim();
                    parent = parent.replace(":", "");
                    this.manager.disconnect();
                    
                    x.setParent(parent);
                    
                    manager.connect();
                    parent = constructor.findOriginalComputer(manager, x.getPartID()).getRentalNumber();
                    manager.disconnect();
                    
                    x.setOriginalParent(parent);
            	}
            }
            
            parts.removeAll(this.parts);
            this.available_parts.setItems(parts);
        }
        catch (NumberFormatException ex) {
            this.manager.disconnect();
        }
    }

    /***
     * Refreshes the Computer's list of parts.
     */
    private void refreshRentalParts() {
        ArrayList<Parts> foo = new ArrayList<>();
        foo.addAll(this.parts);
        if (foo.size() > 0) {
            this.display_computers_parts.setHeightByRows((double)foo.size());
        } else {
            this.display_computers_parts.setHeightByRows(1.0);
        }
        this.display_computers_parts.setItems(foo);
    }

    /***
     * Checks whether or not the list of Parts changed from the previously taken list. Called when the Save button is pressed.
     * @return "True" if the list of Parts changed, "False" otherwise
     */
    private String isPartsEdited() {
        int x;
        if (this.old_parts.size() == 0) {
            System.out.println("false: new unit");
            return "False";
        }
        if (this.reset_flag.booleanValue()) {
            System.out.println("false: original parts loaded");
            return "False";
        }
        if (this.parts.size() != this.old_parts.size()) {
            System.out.println("true: unequal size");
            return "True";
        }
        ArrayList<Integer> parts_id = new ArrayList<Integer>();
        ArrayList<Integer> old_parts_id = new ArrayList<Integer>();
        for (x = 0; x < this.parts.size(); ++x) {
            parts_id.add(((Parts)this.parts.get(x)).getPartID());
        }
        Collections.sort(parts_id);
        for (x = 0; x < this.old_parts.size(); ++x) {
            old_parts_id.add(((Parts)this.old_parts.get(x)).getPartID());
        }
        Collections.sort(old_parts_id);
        if (parts_id.equals(old_parts_id) & this.comp.getIsUpgraded() == false) {
            System.out.println("false: no parts were changed");
            return "False";
        }
        System.out.println("true: different parts detected");
        return "True";
    }

    /***
     * Gets the sum of the Computer's Parts' prices.
     * @return the sum of the Computer's Parts' prices
     */
    private String parts_price_sum() {
        int sum = 0;
        for (int i = 0; i < this.parts.size(); ++i) {
            sum = (int)((float)sum + ((Parts)this.parts.get(i)).getPrice());
        }
        return String.valueOf(sum);
    }

    /***
     * Checks if the Rental Number, CPU, PC Type, or the OS fields are empty.
     * @return True if they are all not empty, False otherwise
     */
    private boolean validate() {
        if (this.rn.getValue().isEmpty() || 
        		this.cpu.getValue().isEmpty() || 
        		this.pctype.getValue().isEmpty() || 
        		this.os.getValue().isEmpty()) {
            return Boolean.FALSE;
        }
        return Boolean.TRUE;
    }
}

