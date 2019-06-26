package com.example.rmonitor.layouts;

import com.example.rmonitor.content.ClientHistory;
import com.example.rmonitor.content.DeliveryPrint;
import com.example.rmonitor.content.MonthHistory;
import com.example.rmonitor.content.ProjectionForm;
import com.example.rmonitor.content.RentalHistory;
import com.vaadin.navigator.View;
import com.vaadin.ui.Button;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.DateField;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.NativeSelect;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;

public class MainPageLayout extends CssLayout implements View {

	protected VerticalLayout layout = new VerticalLayout();
	
	protected TextField delivery_filter = new TextField();
	protected Button delivery_search = new Button("Generate Form");
	protected DeliveryPrint rental_form;
	
	protected NativeSelect<String> client_filter = new NativeSelect<String>();
	protected Button client_search = new Button("Find Client");
	protected ClientHistory client_history_view;
	
	protected DateField date_select = new DateField();
	protected Button date_search = new Button("Go");
	protected MonthHistory month_history_view;
	
	protected TextField rental_filter = new TextField();
	protected Button rental_search = new Button("View Rental Unit History");
	protected RentalHistory rental_history_view;
	
	protected DateField projection_date = new DateField();
	protected Button projection_search = new Button("Find Projected Estimate");
	protected ProjectionForm projection_view;
	
	//protected PullForm pull_form = new PullForm();
	protected DeliveriesLayout urgent = new DeliveriesLayout();
	
//	protected Button generate_delivery_print_form = new Button("Rental Form");
//	protected Button pull_out = new Button("Pull-out");
//	protected Button client_history = new Button("Client History");
//	protected Button month_history = new Button("Monthly History");
//	protected Button rental_history = new Button("Rental Unit History");
//	protected Button projection = new Button("Inventory Projection");
//	protected HorizontalLayout functions = new HorizontalLayout(
//			generate_delivery_print_form, 
//			pull_out,
//			client_history, 
//			month_history, 
//			rental_history,
//			projection);
//	protected HorizontalLayout views = new HorizontalLayout();
	
	protected HorizontalLayout foo = new HorizontalLayout(delivery_filter, delivery_search);
	protected VerticalLayout bar = new VerticalLayout(
			new Label("Rental Form - Enter the Delivery ID# you want to generate a form for"), 
			foo);
	
	protected HorizontalLayout client_history_search = new HorizontalLayout(client_filter, client_search);
	protected VerticalLayout client_history_layout = new VerticalLayout(
			new Label("Client History - Enter the name of a Client to search for their delivery history"),
			client_history_search);
	
	protected HorizontalLayout month_history_search = new HorizontalLayout(date_select, date_search);
	protected VerticalLayout month_history_layout = new VerticalLayout(
			new Label("Month History - Choose a date to see the deliveries for that month and year"),
			month_history_search);
	
	protected HorizontalLayout rental_history_search = new HorizontalLayout(rental_filter, rental_search);
	protected VerticalLayout rental_history_layout = new VerticalLayout(
			new Label("Rental History - Enter the Rental# to see its delivery history"),
			rental_history_search);
	
	protected HorizontalLayout projection_buttons = new HorizontalLayout(projection_date, projection_search);
	protected VerticalLayout projection_layout = new VerticalLayout(
			new Label("Inventory Projection - Choose a date to see how many rental units will be available by then"),
			projection_buttons);
}
