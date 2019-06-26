package com.example.rmonitor.content;

import com.example.rmonitor.classes.Accessory;
import com.example.rmonitor.classes.Computer;
import com.example.rmonitor.classes.ConnectionManager;
import com.example.rmonitor.classes.Delivery;
import com.example.rmonitor.classes.ObjectConstructor;
import com.example.rmonitor.classes.Parts;
import com.example.rmonitor.classes.SmallAccessory;
import com.example.rmonitor.content.DeliveriesView;
import com.example.rmonitor.content.DeliveryForm;
import com.example.rmonitor.layouts.DeliveryFormLayout;
import com.vaadin.data.Binder;
import com.vaadin.shared.ui.ContentMode;
import com.vaadin.shared.ui.ValueChangeMode;
import com.vaadin.ui.Component;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Panel;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.renderers.ComponentRenderer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import org.vaadin.dialogs.ConfirmDialog;

public class DeliveryForm
extends DeliveryFormLayout {
    private DeliveriesView delv_view;
    private ConnectionManager manager;
    private ObjectConstructor constructor;
    private Delivery delivery;
    private Delivery old_delivery;
    private List<Computer> computers;
    private List<Accessory> accessories;
    private List<SmallAccessory> small_accessories;
    private String function;
    private String acc_function;
    private String small_acc_function;
    private String old_delv_id;
    private Binder<Delivery> binder;

    /***
     * Constructs the form used to create and edit Deliveries.
     * @param delv_view - the parent module
     */
    public DeliveryForm(DeliveriesView delv_view) {
        this.manager = new ConnectionManager();
        this.constructor = new ObjectConstructor();
        this.computers = new ArrayList<>();
        this.accessories = new ArrayList<>();
        this.small_accessories = new ArrayList<>();
        this.binder = new Binder<>(Delivery.class);
        this.delv_view = delv_view;
        this.ext.setVisible(false);
        this.choose_grid.setVisible(false);
        VerticalLayout main = this.main_column();
        this.setup_grid();
        this.setup_buttons();
        HorizontalLayout grids = new HorizontalLayout(new Component[]{this.main_grid, this.choose_grid});
        VerticalLayout layout = new VerticalLayout(new Component[]{main, grids, this.buttons});
        layout.setHeightUndefined();
        layout.setWidthUndefined();
        Panel panel = new Panel();
        panel.setContent((Component)layout);
        panel.setHeight("650px");
        panel.setWidth("1500px");
        this.addComponents(new Component[]{panel});
    }

    /***
     * Prepares the fields to be used in the form.
     * @return a VerticalLayout containing the fields
     */
    private VerticalLayout main_column() {
        this.delv_number.setRequiredIndicatorVisible(true);
        this.release.setRequiredIndicatorVisible(true);
        this.due.setRequiredIndicatorVisible(true);
        this.client.setRequiredIndicatorVisible(true);
        this.acct_manager.setRequiredIndicatorVisible(true);
        this.status.setRequiredIndicatorVisible(true);
        this.freq.setRequiredIndicatorVisible(true);
        this.delv_number.setReadOnly(true);
        this.price.setReadOnly(true);
        
        VerticalLayout main = new VerticalLayout();
        HorizontalLayout docs = new HorizontalLayout(new Component[]{this.so, this.si, this.ard, this.pos});
        HorizontalLayout dates = new HorizontalLayout(new Component[]{this.release, this.due, this.freq});
        VerticalLayout extension = new VerticalLayout(new Component[]{this.status, new HorizontalLayout(new Component[]{this.ext, this.extend})});
        HorizontalLayout people = new HorizontalLayout(new Component[]{this.client, this.acct_manager});
        
        this.status.setItems(new String[]{"Active", "Damaged", "Returned", "Partial Return", "Extended"});
        this.status.setEnabled(false);
        this.freq.setItems(new String[]{"Daily", "Weekly", "Monthly", "Quarterly", "Yearly", "Other"});
        
        this.manager.connect();
        this.client.setItems(this.constructor.constructClientNames(this.manager));
        this.manager.disconnect();
        
        main.addComponents(new Component[]{new HorizontalLayout(new Component[]{this.delv_number, this.price}), docs, people, dates, extension});
        
        this.status.addValueChangeListener(e -> this.extended_info());
        
        this.binder.bind(this.delv_number, Delivery::getDeliveryIDStr, Delivery::setDeliveryID);
        this.binder.bind(this.so, Delivery::getSO, Delivery::setSO);
        this.binder.bind(this.si, Delivery::getSI, Delivery::setSI);
        this.binder.bind(this.ard, Delivery::getARD, Delivery::setARD);
        this.binder.bind(this.pos, Delivery::getPOS, Delivery::setPOS);
        this.binder.bind(this.client, Delivery::getcustomerName, Delivery::setcustomerName);
        this.binder.bind(this.release, Delivery::getReleaseLocalDate, Delivery::setReleaseLocalDate);
        this.binder.bind(this.due, Delivery::getDueDateLocal, Delivery::setDueDateLocal);
        this.binder.bind(this.acct_manager, Delivery::getAccountManager, Delivery::setAccountManager);
        this.binder.bind(this.status, Delivery::getStatus, Delivery::setStatus);
        this.binder.bind(this.ext, Delivery::getExtensionIDStr, Delivery::setExtensionID);
        this.binder.bind(this.freq, Delivery::getFrequencyStr, Delivery::setFrequency);
        this.price.setValue(this.computer_price_sum());
        
        return main;
    }

    /***
     * Attaches the selected Delivery to the form.
     * @param delivery - the selected Delivery from the parent module
     */
    public void setDelivery(Delivery delivery) {
        this.delivery = delivery;
        this.old_delv_id = delivery.getDeliveryIDStr();
        this.old_delivery = delivery;
        this.binder.setBean(delivery);
        
        if (delivery.isPersisted()) {
            this.manager.connect();
            this.computers = this.constructor.fetchCompleteComputers(this.manager, delivery.getDeliveryID());
            this.manager.disconnect();
            this.manager.connect();
            this.accessories = this.constructor.fetchDeliveryAccessories(this.manager, delivery.getDeliveryID());
            this.manager.disconnect();
            this.manager.connect();
            this.small_accessories = this.constructor.fetchDeliveryPeripherals(this.manager, delivery.getDeliveryID());
            this.manager.disconnect();
        } else {
            delivery.setDeliveryID(this.delv_view.returnLatestIncrement());
            this.computers = new ArrayList<>();
            this.accessories = new ArrayList<>();
            this.small_accessories = new ArrayList<>();
        }
        
        if (this.computers.size() > 0) {
            this.display_computers.setItems(this.computers);
            this.display_computers.setHeight("400px");
            this.display_computers.setRowHeight(300.0);
            this.display_computers.setHeaderRowHeight(-1.0);
        } else {
            this.display_computers.setItems(Collections.emptyList());
            this.display_computers.setHeightByRows(1.0);
        }
        this.number_of_computers.setValue(String.format("Number of Computers: %d", this.computers.size()));
        
        if (this.accessories.size() > 0) {
            this.display_acc.setItems(this.accessories);
            this.display_acc.setHeight("400px");
        } else {
            this.display_acc.setItems(Collections.emptyList());
            this.display_acc.setHeightByRows(1.0);
        }
        
        if (this.small_accessories.size() > 0) {
            this.display_small_acc.setItems(this.small_accessories);
            this.display_small_acc.setHeight("400px");
        } else {
            this.display_small_acc.setItems(Collections.emptyList());
            this.display_small_acc.setHeightByRows(1.0);
        }
        
        this.delete.setVisible(delivery.isPersisted());
        this.setVisible(true);
    }

    /***
     * Formats the grids in the form.
     */
    private void setup_grid() {
        this.filter.setPlaceholder("Filter by Asset#");
        this.filter.addValueChangeListener(e -> this.updateList());
        this.filter.setValueChangeMode(ValueChangeMode.LAZY);
        
        this.acc_filter.setPlaceholder("Filter by Asset#");
        this.acc_filter.addValueChangeListener(e -> this.updateAccList());
        this.acc_filter.setValueChangeMode(ValueChangeMode.LAZY);
        
        this.available_computers.setRowHeight(150.0);
        this.available_computers.setHeaderRowHeight(-1.0);
        
        this.display_computers.addColumn(Computer::getRentalNumber).setCaption("Rental#");
        this.display_computers.addColumn(parts -> {
            Label label = new Label(this.generate_parts_label(parts), ContentMode.PREFORMATTED);
            return label;
        }, new ComponentRenderer()).setCaption("Parts");
        this.display_computers.addColumn(Computer::getPrice).setCaption("Price");
        
        this.available_computers.addColumn(Computer::getRentalNumber).setCaption("Rental Asset#");
        this.available_computers.addColumn(parts -> {
            Label label = new Label(this.generate_parts_label(parts), ContentMode.PREFORMATTED);
            return label;
        }, new ComponentRenderer()).setCaption("Parts");
        this.available_computers.addColumn(Computer::getPrice).setCaption("Price");
        this.available_computers.setWidth("400px");
        
        this.display_acc.addColumn(Accessory::getRentalNumber).setCaption("Rental Asset#");
        this.display_acc.addColumn(Accessory::getName).setCaption("Name");
        this.display_acc.addColumn(Accessory::getAccessoryType).setCaption("Type");
        
        this.available_acc.addColumn(Accessory::getRentalNumber).setCaption("Rental Asset#");
        this.available_acc.addColumn(Accessory::getName).setCaption("Name");
        this.available_acc.addColumn(Accessory::getAccessoryType).setCaption("Type");
        
        this.display_small_acc.addColumn(SmallAccessory::getName).setCaption("Name");
        this.display_small_acc.addColumn(SmallAccessory::getQuantity).setCaption("Amount");
        
        this.small_accs.setItemCaptionGenerator(smallAccessory -> smallAccessory.getName());
        this.small_accs.addValueChangeListener(e -> {
            if (e.getValue() == null) {
                this.small_acc_amount.setVisible(false);
                this.small_acc_available_amount.setVisible(false);
            } else {
                this.small_acc_amount.setValue("");
                this.small_acc_available_amount.setValue(String.format("%s units available", ((SmallAccessory)e.getValue()).getQuantity()));
                this.small_acc_amount.setVisible(true);
                this.small_acc_available_amount.setVisible(true);
            }
        });
    }

    /***
     * Prepares the functions that the buttons will call.
     */
    private void setup_buttons() {
        this.save.addClickListener(e -> {
            if (this.validate()) {
                this.save();
            } else {
                Notification.show((String)"Error", (String)"Something's wrong with the entered Delivery data.\r\nPlease check all the fields are entered.", (Notification.Type)Notification.Type.ERROR_MESSAGE);
            }
        });
        
        this.save.setStyleName("primary");
        this.cancel.addClickListener(e -> this.cancel());
        this.delete.addClickListener(e -> {
            this.buttons.setVisible(false);
            this.comp_buttons.setVisible(false);
            this.choose_buttons.setVisible(false);
            ConfirmDialog.show((UI)this.getUI(), (String)"Confirmation", 
            		(String)"Are you sure you want to return this Delivery?", 
            		(String)"Yes", (String)"No", 
            		new ConfirmDialog.Listener() {
            	public void onClose(ConfirmDialog dialog) {
            		if (dialog.isConfirmed()) {
            			returnDelivery(delivery.getDeliveryIDStr());
            			
            			buttons.setVisible(true);
                        comp_buttons.setVisible(true);
                        choose_buttons.setVisible(true);
            		}
            		else {
            			buttons.setVisible(true);
                        comp_buttons.setVisible(true);
                        choose_buttons.setVisible(true);
            		}
            	}
            });
        });
        
        this.extend.addClickListener(e -> this.status.setValue("Extended"));
        this.add_comp.addClickListener(e -> this.revealComputerList("Add"));
        this.edit_comp.addClickListener(e -> this.editComputer());
        this.delete_comp.addClickListener(e -> this.deleteComputer());
        this.select_comp.addClickListener(e -> this.addComputer(this.function));
        this.cancel_comp.addClickListener(e -> this.cancelComputer());
        this.add_acc.addClickListener(e -> this.revealAccList("Add"));
        this.edit_acc.addClickListener(e -> this.editAcc());
        this.delete_acc.addClickListener(e -> this.deleteAcc());
        this.select_acc.addClickListener(e -> this.addAcc(this.acc_function));
        this.cancel_acc.addClickListener(e -> this.cancelAcc());
        this.add_small_acc.addClickListener(e -> this.revealSmallAccList("Add"));
        this.edit_small_acc.addClickListener(e -> this.editSmallAcc());
        this.delete_small_acc.addClickListener(e -> this.deleteSmallAcc());
        this.select_small_acc.addClickListener(e -> this.addSmallAcc(this.small_acc_function));
        this.cancel_small_acc.addClickListener(e -> this.cancelSmallAcc());
    }

    /***
     * Hides the Computer selection menu.
     */
    private void cancelComputer() {
        this.available_computers.deselectAll();
        
        this.comp_buttons.setVisible(true);
        this.acc_buttons.setVisible(true);
        this.display_acc.setVisible(true);
        this.small_acc_buttons.setVisible(true);
        this.display_small_acc.setVisible(true);
        this.buttons.setVisible(true);
        
        this.choose_grid.setVisible(false);
        this.showAccSelection(Boolean.valueOf(true));
        this.showSmallAccSelection(Boolean.valueOf(true));
    }

    /***
     * Hides the Accessory selection menu.
     */
    private void cancelAcc() {
        this.available_acc.deselectAll();

        this.comp_buttons.setVisible(true);
        this.display_computers.setVisible(true);
        this.acc_buttons.setVisible(true);
        this.small_acc_buttons.setVisible(true);
        this.display_small_acc.setVisible(true);
        this.buttons.setVisible(true);
        
        this.choose_grid.setVisible(false);
        this.showCompSelection(Boolean.valueOf(true));
        this.showSmallAccSelection(Boolean.valueOf(true));
    }

    /***
     * Hides the Small Accessory selection menu.
     */
    private void cancelSmallAcc() {
        this.small_accs.setValue(null);
        this.small_acc_amount.setValue("");
        
        this.comp_buttons.setVisible(true);
        this.display_computers.setVisible(true);
        this.acc_buttons.setVisible(true);
        this.display_acc.setVisible(true);
        this.small_acc_buttons.setVisible(true);
        this.buttons.setVisible(true);
        
        this.choose_grid.setVisible(false);
        this.showCompSelection(Boolean.valueOf(true));
        this.showAccSelection(Boolean.valueOf(true));
    }

    /***
     * Adds or modifies the selected Computer to the Delivery's list of Computers.
     * This is a soft operation, so it does not actually affect the server database.
     * @param check - the operation to perform
     */
    private void addComputer(String check) {
        if (check.equals("Add")) {
            try {
                System.out.println("ADD: ".concat(((Computer)this.available_computers.asSingleSelect().getValue()).getRentalNumber()));
                this.computers.add((Computer)this.available_computers.asSingleSelect().getValue());
                this.refreshRentalUnits();
                Notification.show((String)"Add Computer", (String)"Successfully added the unit!", (Notification.Type)Notification.Type.HUMANIZED_MESSAGE);
            }
            catch (NullPointerException ex) {
                Notification.show((String)"Error", (String)"Please select a unit to add", (Notification.Type)Notification.Type.ERROR_MESSAGE);
            }
        } else {
            try {
                System.out.println("CHANGE TO: ".concat(((Computer)this.available_computers.asSingleSelect().getValue()).getRentalNumber()));
                for (Computer i : this.computers) {
                    if (!i.getRentalNumber().equals(((Computer)this.display_computers.asSingleSelect().getValue()).getRentalNumber())) continue;
                    this.computers.remove(i);
                    break;
                }
                this.computers.add((Computer)this.available_computers.asSingleSelect().getValue());
                this.refreshRentalUnits();
                Notification.show((String)"Edit Computer", (String)"Successfully changed the unit!", (Notification.Type)Notification.Type.HUMANIZED_MESSAGE);
            }
            catch (NullPointerException ex) {
                Notification.show((String)"Error", (String)"Please select a unit to change", (Notification.Type)Notification.Type.ERROR_MESSAGE);
            }
        }
        this.price.setValue(this.computer_price_sum());
        this.available_computers.deselectAll();
        this.comp_buttons.setVisible(true);
        this.buttons.setVisible(true);
        this.choose_grid.setVisible(false);
        this.showAccSelection(Boolean.valueOf(true));
        this.showSmallAccSelection(Boolean.valueOf(true));
    }

    /***
     * Adds or modifies the selected Accessory to the Delivery's list of Accessories.
     * This is a soft operation, so it does not actually affect the server database.
     * @param check - the operation to perform
     */
    private void addAcc(String check) {
        if (check.equals("Add")) {
            try {
                System.out.println("ADD: ".concat(((Accessory)this.available_acc.asSingleSelect().getValue()).getRentalNumber()));
                this.accessories.add((Accessory)this.available_acc.asSingleSelect().getValue());
                this.refreshRentalAcc();
                Notification.show((String)"Add Accessory", (String)"Successfully added the Accessory!", (Notification.Type)Notification.Type.HUMANIZED_MESSAGE);
            }
            catch (NullPointerException ex) {
                Notification.show((String)"Error", (String)"Please select an Accessory to Add", (Notification.Type)Notification.Type.ERROR_MESSAGE);
            }
        } else {
            try {
                System.out.println("EDIT: ".concat(((Accessory)this.available_acc.asSingleSelect().getValue()).getRentalNumber()));
                for (Accessory i : this.accessories) {
                    if (!i.getRentalNumber().equals(((Accessory)this.display_acc.asSingleSelect().getValue()).getRentalNumber())) continue;
                    this.accessories.remove(i);
                    break;
                }
                this.accessories.add((Accessory)this.available_acc.asSingleSelect().getValue());
                this.refreshRentalAcc();
                Notification.show((String)"Edit Accessory", (String)"Successfully changed the Accessory!", (Notification.Type)Notification.Type.HUMANIZED_MESSAGE);
            }
            catch (NullPointerException ex) {
                Notification.show((String)"Error", (String)"Please select an Accessory to change", (Notification.Type)Notification.Type.ERROR_MESSAGE);
            }
        }
        this.available_acc.deselectAll();
        this.acc_buttons.setVisible(true);
        this.buttons.setVisible(true);
        this.choose_grid.setVisible(false);
        this.showCompSelection(Boolean.valueOf(true));
        this.showSmallAccSelection(Boolean.valueOf(true));
    }

    /***
     * Adds or modifies the selected Small Accessory to the Delivery's list of Small Accessories.
     * This is a soft operation, so it does not actually affect the server database.
     * @param check - the operation to perform
     */
    private void addSmallAcc(String check) {
        if (check.equals("Add")) {
            if (this.small_accs.getValue() != null && !this.small_acc_amount.getValue().isEmpty() && this.small_acc_amount.getValue().chars().allMatch(Character::isDigit)) {
                System.out.println("ADD: ".concat(((SmallAccessory)this.small_accs.getValue()).getName()));
                this.small_accessories.add(new SmallAccessory(((SmallAccessory)this.small_accs.getValue()).getName(), "", 0.0f, Integer.parseInt(this.small_acc_amount.getValue()), 0));
                this.refreshSmallAcc();
                Notification.show((String)"Add Accessory", (String)"Successfully added the Accessory!", (Notification.Type)Notification.Type.HUMANIZED_MESSAGE);
            } else {
                Notification.show((String)"Error", (String)"Please select a peripheral to add and specify an amount", (Notification.Type)Notification.Type.ERROR_MESSAGE);
            }
        } else if (this.small_accs.getValue() != null && !this.small_acc_amount.getValue().isEmpty() && this.small_acc_amount.getValue().chars().allMatch(Character::isDigit)) {
            System.out.println("EDIT: ".concat(((SmallAccessory)this.small_accs.getValue()).getName()));
            for (SmallAccessory i : this.small_accessories) {
                if (!i.getName().equals(((SmallAccessory)this.display_small_acc.asSingleSelect().getValue()).getName())) continue;
                this.small_accessories.remove(i);
                break;
            }
            this.small_accessories.add(new SmallAccessory(((SmallAccessory)this.small_accs.getValue()).getName(), "", 0.0f, Integer.parseInt(this.small_acc_amount.getValue()), 0));
            this.refreshSmallAcc();
            Notification.show((String)"Edit Accessory", (String)"Successfully changed the Accessory!", (Notification.Type)Notification.Type.HUMANIZED_MESSAGE);
        } else {
            Notification.show((String)"Error", (String)"Please select a peripheral to change", (Notification.Type)Notification.Type.ERROR_MESSAGE);
        }
        this.small_accs.setValue(null);
        this.small_acc_amount.setValue("");
        this.small_acc_buttons.setVisible(true);
        this.buttons.setVisible(true);
        this.choose_grid.setVisible(false);
        this.showCompSelection(Boolean.valueOf(true));
        this.showAccSelection(Boolean.valueOf(true));
    }

    /***
     * Removes the selected Computer from the form's list of Computers.
     * This is a soft operation, so it does not actually affect the server database.
     */
    private void deleteComputer() {
        try {
            System.out.println("DELETE: ".concat(((Computer)this.display_computers.asSingleSelect().getValue()).getRentalNumber()));
            for (Computer i : this.computers) {
                if (!i.getRentalNumber().equals(((Computer)this.display_computers.asSingleSelect().getValue()).getRentalNumber())) continue;
                this.computers.remove(i);
                break;
            }
            this.refreshRentalUnits();
            this.price.setValue(this.computer_price_sum());
            Notification.show((String)"Delete Part", (String)"Successfully removed the unit!", (Notification.Type)Notification.Type.HUMANIZED_MESSAGE);
        }
        catch (NullPointerException ex) {
            Notification.show((String)"Error", (String)"Please select a unit to remove", (Notification.Type)Notification.Type.ERROR_MESSAGE);
        }
    }

    /***
     * Removes the selected Accessory from the form's list of Accessories.
     * This is a soft operation, so it does not actually affect the server database.
     */
    private void deleteAcc() {
        try {
            System.out.println("DELETE: ".concat(((Accessory)this.display_acc.asSingleSelect().getValue()).getRentalNumber()));
            for (Accessory i : this.accessories) {
                if (!i.getRentalNumber().equals(((Accessory)this.display_acc.asSingleSelect().getValue()).getRentalNumber())) continue;
                this.accessories.remove(i);
                break;
            }
            this.refreshRentalAcc();
            Notification.show((String)"Delete Accessory", (String)"Successfully removed the Accessory!", (Notification.Type)Notification.Type.HUMANIZED_MESSAGE);
        }
        catch (NullPointerException ex) {
            Notification.show((String)"Error", (String)"Please select an Accessory to remove", (Notification.Type)Notification.Type.ERROR_MESSAGE);
        }
    }

    /***
     * Removes the selected Small Accessory from the form's list of Small Accessories.
     * This is a soft operation, so it does not actually affect the server database.
     */
    private void deleteSmallAcc() {
        try {
            System.out.println("DELETE: ".concat(((SmallAccessory)this.display_small_acc.asSingleSelect().getValue()).getName()));
            for (SmallAccessory i : this.small_accessories) {
                if (!i.getName().equals(((SmallAccessory)this.display_small_acc.asSingleSelect().getValue()).getName())) continue;
                this.small_accessories.remove((Object)i);
                break;
            }
            this.refreshSmallAcc();
            Notification.show((String)"Delete Accessory", (String)"Successfully removed the Accessory!", (Notification.Type)Notification.Type.HUMANIZED_MESSAGE);
        }
        catch (NullPointerException ex) {
            Notification.show((String)"Error", (String)"Please select a peripheral to remove", (Notification.Type)Notification.Type.ERROR_MESSAGE);
        }
    }

    /***
     * Tells the form the next operation to perform, then reveals the Computer selection menu. 
     * Does not actually edit the form's list of Computers.
     */
    private void editComputer() {
        try {
            System.out.println("SELECTED: ".concat(((Computer)this.display_computers.asSingleSelect().getValue()).getRentalNumber()));
            this.revealComputerList("Edit");
        }
        catch (NullPointerException ex) {
            Notification.show((String)"Error", (String)"Please select a unit to edit", (Notification.Type)Notification.Type.ERROR_MESSAGE);
        }
    }

    /***
     * Tells the form the next operation to perform, then reveals the Accessory selection menu. 
     * Does not actually edit the form's list of Accessories.
     */
    private void editAcc() {
        try {
            System.out.println("SELECTED: ".concat(((Accessory)this.display_acc.asSingleSelect().getValue()).getRentalNumber()));
            this.revealAccList("Edit");
        }
        catch (NullPointerException ex) {
            Notification.show((String)"Error", (String)"Please select an accessory to edit", (Notification.Type)Notification.Type.ERROR_MESSAGE);
        }
    }

    /***
     * Tells the form the next operation to perform, then reveals the Small Accessory selection menu. 
     * Does not actually edit the form's list of Small Accessories.
     */
    private void editSmallAcc() {
        try {
            System.out.println("SELECTED: ".concat(((SmallAccessory)this.display_small_acc.asSingleSelect().getValue()).getName()));
            this.revealSmallAccList("Edit");
        }
        catch (NullPointerException ex) {
            Notification.show((String)"Error", (String)"Please select a peripheral to edit", (Notification.Type)Notification.Type.ERROR_MESSAGE);
        }
    }

    /***
     * Fetches the list of available Computers from the database, then reveals the Computer selection menu.
     * @param check - the next operation to perform
     */
    private void revealComputerList(String check) {
        this.function = check;
        this.manager.connect();
        this.available_computers.setItems(this.constructor.findAvailableComputers(this.manager));
        this.manager.disconnect();
        this.choose_grid.setVisible(true);
        this.showAccSelection(Boolean.valueOf(false));
        this.showSmallAccSelection(Boolean.valueOf(false));
        
        this.buttons.setVisible(false);
        this.acc_buttons.setVisible(false);
        this.display_acc.setVisible(false);
        this.small_acc_buttons.setVisible(false);
        this.display_small_acc.setVisible(false);
        this.comp_buttons.setVisible(false);
    }

    /***
     * Fetches the list of available Accessories from the database, then reveals the Accessory selection menu.
     * @param check - the next operation to perform
     */
    private void revealAccList(String check) {
        this.acc_function = check;
        this.manager.connect();
        this.available_acc.setItems(this.constructor.findAvailableAccessories(this.manager));
        this.manager.disconnect();
        this.choose_grid.setVisible(true);
        this.showCompSelection(Boolean.valueOf(false));
        this.showSmallAccSelection(Boolean.valueOf(false));
        
        this.buttons.setVisible(false);
        this.acc_buttons.setVisible(false);
        this.small_acc_buttons.setVisible(false);
        this.display_small_acc.setVisible(false);
        this.comp_buttons.setVisible(false);
        this.display_computers.setVisible(false);
    }

    /***
     * Fetches the list of available Small Accessories from the database, then reveals the Small Accessory selection menu.
     * @param check - the next operation to perform
     */
    private void revealSmallAccList(String check) {
        this.small_acc_function = check;
        this.manager.connect();
        this.small_accs.setItems(this.constructor.constructSmallAccessories(this.manager));
        this.manager.disconnect();
        this.choose_grid.setVisible(true);
        this.showCompSelection(Boolean.valueOf(false));
        this.showAccSelection(Boolean.valueOf(false));
        
        this.buttons.setVisible(false);
        this.acc_buttons.setVisible(false);
        this.display_acc.setVisible(false);
        this.small_acc_buttons.setVisible(false);
        this.comp_buttons.setVisible(false);
        this.display_computers.setVisible(false);
    }

    /***
     * Hides the form, then refreshes the parent module.
     */
    private void cancel() {
        this.setVisible(false);
        this.delv_view.resetView();
    }

    /***
     * Sends the field inputs to the database. A new entry is created, or an existing one is modified based on the field inputs.
     * This function already checks for duplicate Computers, Accessories, and SmallAccessories.
     */
    private void save() {
        this.manager.connect();
        String clientid = this.manager.send(String.format("FindClient\r\n%s", this.delivery.getcustomerName()));
        this.manager.disconnect();
        
        clientid = clientid.replace(":", "");
        
        if (clientid.isEmpty()) {
            Notification.show((String)"Error", (String)"The client doesn't exist.", (Notification.Type)Notification.Type.ERROR_MESSAGE);
            return;
        }
        
        this.manager.connect();
        //edit the delivery if the deliveryID already exists
        if (this.constructor.isDeliveryExisting(this.manager, this.old_delv_id)) {
            this.manager.disconnect();
            System.out.println("Edit Delivery");
            
            List<String> parameters = new ArrayList<>();
            parameters.add(this.old_delv_id);
            parameters.add(this.delivery.getDeliveryIDStr());
            parameters.add(this.delivery.getSO());
            parameters.add(this.delivery.getSI());
            parameters.add(this.delivery.getARD());
            parameters.add(this.delivery.getPOS());
            parameters.add(clientid.trim());
            parameters.add(String.valueOf(this.delivery.getReleaseDate()));
            parameters.add(String.valueOf(this.delivery.getDueDate()));
            parameters.add(this.delivery.getAccountManager());
            parameters.add(this.delivery.getStatus());
            parameters.add(this.delivery.getExtensionIDStr());
            parameters.add(String.valueOf(this.delivery.getFrequency()));
            
            String base = constructor.constructMessage("EditDelivery", parameters);
            
            this.manager.connect();
            String result = this.manager.send(base);
            this.manager.disconnect();
            
            parameters.clear();
            ArrayList<String> unique_computers = new ArrayList<String>();
            for (int i = 0; i < this.computers.size(); ++i) {
                if (unique_computers.contains(((Computer)this.computers.get(i)).getRentalNumber())) continue;
                unique_computers.add(((Computer)this.computers.get(i)).getRentalNumber());
                
                parameters.add(this.delivery.getDeliveryIDStr());
                parameters.add(((Computer)this.computers.get(i)).getRentalNumber());
                
                this.manager.connect();
                result = this.manager.send(constructor.constructMessage("InsertDeliverySpecs", parameters));
                /*String.format(
                		"InsertDeliverySpecs\r\n%s\r\n%s", 
                		this.delivery.getDeliveryIDStr(), 
                		((Computer)this.computers.get(i)).getRentalNumber())*/
                this.manager.disconnect();
                
                if (delivery.getExtensionID() != 0) {
                	parameters.set(0, delivery.getExtensionIDStr());
                	
                	System.out.println("-- EditDelivery --");
                	System.out.println("The delivery was extended, adding Computers");
                	System.out.println(parameters);
                	
                	manager.connect();
                	result = this.manager.send(constructor.constructMessage("InsertDeliverySpecs", parameters));
                    manager.disconnect();
                }
                parameters.clear();
            }
            
            parameters.clear();
            ArrayList<String> unique_acc = new ArrayList<String>();
            for (int i = 0; i < this.accessories.size(); ++i) {
                if (unique_acc.contains(((Accessory)this.accessories.get(i)).getRentalNumber())) continue;
                unique_acc.add(((Accessory)this.accessories.get(i)).getRentalNumber());
                
                parameters.add(this.delivery.getDeliveryIDStr());
                parameters.add(((Accessory)this.accessories.get(i)).getRentalNumber());
                
                this.manager.connect();
                result = this.manager.send(constructor.constructMessage("InsertDeliveryAccessories", parameters));
                /*String.format(
                		"InsertDeliveryAccessories\r\n%s\r\n%s", 
                		this.delivery.getDeliveryIDStr(), 
                		((Accessory)this.accessories.get(i)).getRentalNumber())*/
                this.manager.disconnect();
                
                if (delivery.getExtensionID() != 0) {
                	parameters.set(0, delivery.getExtensionIDStr());
                	
                	System.out.println("-- EditDelivery --");
                	System.out.println("The delivery was extended, adding accessories");
                	System.out.println(parameters);
                	
                	manager.connect();
                	result = this.manager.send(constructor.constructMessage("InsertDeliveryAccessories", parameters));
                    manager.disconnect();
                }
                parameters.clear();
            }
            
            parameters.clear();
            ArrayList<String> unique_per = new ArrayList<String>();
            for (SmallAccessory i : this.small_accessories) {
                if (unique_per.contains(i.getName())) continue;
                unique_per.add(i.getName());
                
                parameters.add(this.delivery.getDeliveryIDStr());
                parameters.add(i.getName());
                parameters.add(i.getQuantityStr());
                
                this.manager.connect();
                result = this.manager.send(constructor.constructMessage("InsertDeliveryPeripherals", parameters));
                /*String.format(
                		"InsertDeliveryPeripherals\r\n%s\r\n%s\r\n%s", 
                		this.delivery.getDeliveryIDStr(), 
                		i.getName(), 
                		i.getQuantity())*/
                this.manager.disconnect();
                
                if (delivery.getExtensionID() != 0) {
                	parameters.set(0, delivery.getExtensionIDStr());
                	
                	System.out.println("-- EditDelivery --");
                	System.out.println("The delivery was extended, adding peripherals");
                	System.out.println(parameters);
                	
                	manager.connect();
                	result = this.manager.send(constructor.constructMessage("InsertDeliveryPeripherals", parameters));
                    manager.disconnect();
                }
                parameters.clear();
            }
            
            Notification.show((String)"Edit Delivery", (String)result, (Notification.Type)Notification.Type.HUMANIZED_MESSAGE);
        }
        //if the deliveryID does not exist, create a new Delivery
        else {
            this.manager.disconnect();
            System.out.println("Create New Delivery");
            
            List<String> parameters = new ArrayList<>();
            parameters.add(this.delivery.getDeliveryIDStr());
            parameters.add(this.delivery.getSO());
            parameters.add(this.delivery.getSI());
            parameters.add(this.delivery.getARD());
            parameters.add(this.delivery.getPOS());
            parameters.add(clientid.trim());
            parameters.add(String.valueOf(this.delivery.getReleaseDate()));
            parameters.add(String.valueOf(this.delivery.getDueDate()));
            parameters.add(this.delivery.getAccountManager());
            parameters.add(this.delivery.getStatus());
            parameters.add(this.delivery.getExtensionIDStr());
            parameters.add(String.valueOf(this.delivery.getFrequency()));
            
            String base = constructor.constructMessage("InsertNewDelivery", parameters);
            
            String result = null;
            this.manager.connect();
            //get the deliveryID that was generated for this delivery
            String next_increment = this.manager.send(base).trim();
            this.manager.disconnect();
            
            int last_insert_id;
            try {
            	last_insert_id = Integer.parseInt(next_increment.trim());
            }
            catch (NumberFormatException ex) {
            	//in case a connection reset occurs
            	manager.connect();
            	last_insert_id = constructor.constructLastInsertedDelivery(manager).getDeliveryID();
            	manager.disconnect();
            }
            
            //insert the units
            parameters.clear();
            ArrayList<String> unique_computers = new ArrayList<String>();
            for (int i = 0; i < this.computers.size(); ++i) {
                if (unique_computers.contains(((Computer)this.computers.get(i)).getRentalNumber())) continue;
                unique_computers.add(((Computer)this.computers.get(i)).getRentalNumber());
                
                /*
                this.manager.connect();
                result = this.manager.send(String.format(
                		"InsertDeliverySpecs\r\n%s\r\n%s", 
                		last_insert_id, 
                		((Computer)this.computers.get(i)).getRentalNumber()));
                this.manager.disconnect();*/
                
                parameters.add(String.valueOf(last_insert_id));
                parameters.add(((Computer)this.computers.get(i)).getRentalNumber());
                
                this.manager.connect();
                result = this.manager.send(constructor.constructMessage("InsertDeliverySpecs", parameters));
                this.manager.disconnect();
                
                if (delivery.getExtensionID() != 0) {
                	parameters.set(0, delivery.getExtensionIDStr());
                	manager.connect();
                	result = this.manager.send(constructor.constructMessage("InsertDeliverySpecs", parameters));
                    manager.disconnect();
                }
                parameters.clear();
            }
            
            parameters.clear();
            ArrayList<String> unique_acc = new ArrayList<String>();
            for (int i = 0; i < this.accessories.size(); ++i) {
                if (unique_acc.contains(((Accessory)this.accessories.get(i)).getRentalNumber())) continue;
                unique_acc.add(((Accessory)this.accessories.get(i)).getRentalNumber());
                
                /*this.manager.connect();
                result = this.manager.send(String.format(
                		"InsertDeliveryAccessories\r\n%s\r\n%s", 
                		last_insert_id, 
                		((Accessory)this.accessories.get(i)).getRentalNumber()));
                this.manager.disconnect();*/
                
                parameters.add(String.valueOf(last_insert_id));
                parameters.add(((Accessory)this.accessories.get(i)).getRentalNumber());
                
                this.manager.connect();
                result = this.manager.send(constructor.constructMessage("InsertDeliveryAccessories", parameters));
                this.manager.disconnect();
                
                if (delivery.getExtensionID() != 0) {
                	parameters.set(0, delivery.getExtensionIDStr());
                	manager.connect();
                	result = this.manager.send(constructor.constructMessage("InsertDeliveryAccessories", parameters));
                    manager.disconnect();
                }
                parameters.clear();
            }
            
            parameters.clear();
            ArrayList<String> unique_per = new ArrayList<String>();
            for (SmallAccessory i : this.small_accessories) {
                if (unique_per.contains(i.getName())) continue;
                unique_per.add(i.getName());
                
                /*this.manager.connect();
                result = this.manager.send(String.format(
                		"InsertDeliveryPeripherals\r\n%s\r\n%s\r\n%s", 
                		last_insert_id, 
                		i.getName(), 
                		i.getQuantity()));
                this.manager.disconnect();*/
                
                parameters.add(String.valueOf(last_insert_id));
                parameters.add(i.getName());
                parameters.add(i.getQuantityStr());
                
                this.manager.connect();
                result = this.manager.send(constructor.constructMessage("InsertDeliveryPeripherals", parameters));
                this.manager.disconnect();
                
                if (delivery.getExtensionID() != 0) {
                	parameters.set(0, delivery.getExtensionIDStr());
                	manager.connect();
                	result = this.manager.send(constructor.constructMessage("InsertDeliveryPeripherals", parameters));
                    manager.disconnect();
                }
                parameters.clear();
            }
            
            Notification.show((String)"Create New Delivery", (String)result, (Notification.Type)Notification.Type.HUMANIZED_MESSAGE);
        }
        this.setVisible(false);
        this.delv_view.resetView();
    }

    /***
     * Sets the Delivery status as "Returned," then hides the form and refreshes the parent module.
     * @param delv - the DeliveryID of the attached Delivery
     */
    private void returnDelivery(String delv) {
        this.manager.connect();
        String query = String.format("ReturnDelivery\r\n%s", delv);
        String computers = this.manager.send(query);
        this.manager.disconnect();
        
        Notification.show((String)"Return Delivery", (String)computers, (Notification.Type)Notification.Type.HUMANIZED_MESSAGE);
        this.setVisible(false);
        this.delv_view.resetView();
    }

    /***
     * Refreshes the form's list of Computers.
     */
    private void refreshRentalUnits() {
        ArrayList<Computer> foo = new ArrayList<>();
        foo.addAll(this.computers);
        if (foo.size() > 0) {
            this.display_computers.setHeight("400px");
        } else {
            this.display_computers.setHeightByRows(1.0);
        }
        this.number_of_computers.setValue(String.format("Number of Computers: %d", foo));
        this.display_computers.setItems(foo);
    }

    /***
     * Refreshes the form's list of Accessories.
     */
    private void refreshRentalAcc() {
        ArrayList<Accessory> foo = new ArrayList<>();
        foo.addAll(this.accessories);
        
        if (foo.size() > 0) {
            this.display_acc.setHeight("400px");
        } else {
            this.display_acc.setHeightByRows(1.0);
        }
        this.display_acc.setItems(foo);
    }

    /***
     * Refreshes the form's list of Small Accessories.
     */
    private void refreshSmallAcc() {
        ArrayList<SmallAccessory> foo = new ArrayList<>();
        foo.addAll(this.small_accessories);
        
        if (foo.size() > 0) {
            this.display_small_acc.setHeight("400px");
        } else {
            this.display_small_acc.setHeightByRows(1.0);
        }
        this.display_small_acc.setItems(foo);
    }

    /***
     * Displays the Extension label if the Delivery's status is "Extended."
     * Hides it otherwise.
     */
    private void extended_info() {
        if (this.status.getValue() == "Extended") {
            this.ext.setRequiredIndicatorVisible(true);
            this.ext.setVisible(true);
        } else {
            this.ext.setRequiredIndicatorVisible(false);
            this.ext.setVisible(false);
        }
    }

    /***
     * Checks for the validity of the field inputs.
     * @return If the DeliveryID, Client, Account Manager, or Status fields are empty, it returns False.
     * If the Delivery status is "Extended" but the Extension field contains 
     * a non-existent, zero, or non-integer DeliveryID, it returns False.
     * It returns True if none of the above conditions apply.
     */
    private boolean validate() {
        if (this.delv_number.getValue().isEmpty() || 
        		((String)this.client.getValue()).isEmpty() || 
        		this.acct_manager.getValue().isEmpty() || 
        		((String)this.status.getValue()).isEmpty()) {
            return Boolean.FALSE;
        }
        if (this.status.getValue() == "Extended") {
            try {
                this.manager.connect();
                if (!this.constructor.isDeliveryExisting(this.manager, this.ext.getValue())) {
                    this.manager.disconnect();
                    return Boolean.FALSE;
                }
                if (Integer.parseInt(this.ext.getValue()) == 0) {
                    this.manager.disconnect();
                    return Boolean.FALSE;
                }
                this.manager.disconnect();
            }
            catch (NumberFormatException ex) {
                this.manager.disconnect();
                return Boolean.FALSE;
            }
        }
        return Boolean.TRUE;
    }

    /***
     * Generates the list of Parts belonging to a Computer.
     * @param c - a Computer
     * @return a String containing the formatted list of Parts belonging to the Computer
     */
    private String generate_parts_label(Computer c) {
        StringBuilder label = new StringBuilder();
        for (int i = 0; i < c.getPartIDs().size(); ++i) {
            this.manager.connect();
            String result = ((Parts)this.constructor.fetchParts(this.manager, 
            		((Integer)c.getPartIDs().get(i)).intValue()).get(0)).getStringRepresentation();
            this.manager.disconnect();
            label.append(result);
            label.append("\r\n");
        }
        return label.toString();
    }

    /***
     * Gets the sum of the Delivery's Computers.
     * @return the sum of the Computers currently attached to the Delivery.
     */
    private String computer_price_sum() {
        try {
            return String.valueOf(this.delivery.getTotalPrice());
        }
        catch (Exception ex) {
            return "0";
        }
    }

    /***
     * Refreshes the list of available Computers based on filter input.
     */
    private void updateList() {
    	List<String> foo = Arrays.asList(filter.getValue().split("\\s*/\\s*"));
		foo.replaceAll(string -> {
			String temp = String.format(".*%s.*", string);
			return temp;
		});
		String parameter = String.join("|", foo);
    	
        List<Computer> computers = new ArrayList<>();
        this.manager.connect();
        try {
            computers = this.constructor.constructComputers(this.manager, parameter);
            this.manager.disconnect();
            computers.removeAll(this.computers);
            this.available_computers.setItems(computers);
        }
        catch (Exception ex) {
            this.manager.disconnect();
        }
    }

    /***
     * Refreshes the list of available Accessories based on filter input.
     */
    private void updateAccList() {
    	List<String> foo = Arrays.asList(acc_filter.getValue().split("\\s*/\\s*"));
		foo.replaceAll(string -> {
			String temp = String.format(".*%s.*", string);
			return temp;
		});
		String parameter = String.join("|", foo);
    	
        List<Accessory> accessories = new ArrayList<>();
        this.manager.connect();
        try {
            accessories = this.constructor.constructAccessories(this.manager, parameter);
            this.manager.disconnect();
            accessories.removeAll(this.accessories);
            this.available_acc.setItems(accessories);
        }
        catch (Exception ex) {
            this.manager.disconnect();
        }
    }

    /***
     * Sets the visibility of the Computer selection menu.
     * @param foo - a Boolean value
     */
    private void showCompSelection(Boolean foo) {
        this.filter.setVisible(foo.booleanValue());
        this.available_computers.setVisible(foo.booleanValue());
        this.choose_buttons.setVisible(foo.booleanValue());
    }

    /***
     * Sets the visibility of the Accessory selection menu.
     * @param foo - a Boolean value
     */
    private void showAccSelection(Boolean foo) {
        this.acc_filter.setVisible(foo.booleanValue());
        this.available_acc.setVisible(foo.booleanValue());
        this.choose_acc.setVisible(foo.booleanValue());
    }

    /***
     * Sets the visibility of the Small Accessory selection menu.
     * @param foo - a Boolean value
     */
    private void showSmallAccSelection(Boolean foo) {
        this.available_small_acc.setVisible(foo.booleanValue());
        this.choose_small_acc.setVisible(foo.booleanValue());
    }
}

