package com.example.rmonitor.classes;

import java.io.File;
import java.util.List;

import com.example.rmonitor.access.CurrentUser;
import com.vaadin.server.VaadinService;

public class HTMLGenerator {
	ConnectionManager manager = new ConnectionManager();
	ObjectConstructor constructor = new ObjectConstructor();
	
	int NUMBER_OF_PARTS = 9;
	
	public HTMLGenerator() {
		
	}
	
	public String generate_url() {
		String path = VaadinService.getCurrent()
                .getBaseDirectory().getAbsolutePath();
		String fname = String.format("VAADIN\\themes\\mytheme\\html\\%s_dform.html", CurrentUser.get());
		
		return path + File.separator + fname;
	}
	
	/**
	 * Generates an html form from a Delivery input, containing the Delivery data.
	 * @param d
	 * @return
	 */
	public String generate_delivery_print_html(Delivery d) {
		StringBuilder html = new StringBuilder();
		html.append("<!DOCTYPE html>\r\n" + 
				"<html lang=\"en\">\r\n" + 
				"<head>\r\n" + 
				"	<meta charset=\"UTF-8\">\r\n" + 
				String.format("	<title>Delivery#%s - Rental Requirement Form</title>\r\n", d.getDeliveryID()) +
				"<style>\r\n" + 
				".column {\r\n" + 
				"	float: left;\r\n" + 
				"	width: 50%;\r\n" + 
				"}\r\n" + 
				"\r\n" + 
				".row:after {\r\n" + 
				"	content: \"\";\r\n" + 
				"	display: table;\r\n" + 
				"	clear: both;\r\n" + 
				"}\r\n" + 
				"\r\n" + 
				".container {\r\n" + 
				"	border-style: solid;\r\n" + 
				"	border-width: 1px;\r\n" + 
				"	border-top-style: hidden;\r\n" + 
				"	border-right-style: hidden;\r\n" + 
				"	border-bottom-style: solid;\r\n" + 
				"	border-left-style: hidden;\r\n" + 
				"	margin: 5px;\r\n" + 
				"}\r\n" +
				"</style> " +
				"</head>");
		
		/*
		 * This parts adds the header that contains the Delivery data
		 * */
		html.append("<body>\r\n" +
				"<h3 style=\"text-align:center\">" + 
				String.format("	Delivery#%s - Rental Requirement Form\r\n", d.getDeliveryID()) +
				"</h3>\r\n" + 
				"	<div id=\"main-container\"\r\n" + 
				"		<div id=\"rr-form\">\r\n" + 
				"				<table border=\"1px\" width=\"100%\">\r\n" + 
				"					<tr>\r\n" + 
				"						<td width=\"20%\"><strong>CLIENT</strong></td>\r\n" + 
				"						<td width=\"50%\">");
				
		html.append(d.getcustomerName());
		
		html.append("</td>\r\n" + 
				"						<td>\r\n" + 
				"							<strong>SO#</strong>\r\n" + 
				"						</td>\r\n" + 
				"						<td>");
		
		html.append(d.getSO());
		
		html.append("</td>\r\n" + 
				"					</tr>\r\n" + 
				"					<tr>\r\n" + 
				"						<td width=\"20%\">\r\n" + 
				"							<strong>DEL. DATE</strong>\r\n" + 
				"						</td>\r\n" + 
				"						<td width=\"50%\">");
				
		html.append(d.getReleaseDate());
		
		html.append("</td>\r\n" + 
				"						<td>\r\n" + 
				"							<strong>ARD#</strong>\r\n" + 
				"						</td>\r\n" + 
				"						<td>");
				
		html.append(d.getARD());
		
		
		html.append("</td>\r\n" + 
				"					</tr>\r\n" + 
				"					<tr>\r\n" + 
				"						<td width=\"20%\">\r\n" + 
				"							<strong>ACCOUNT MANAGER</strong>\r\n" + 
				"						</td>\r\n" + 
				"						<td width=\"50%\">");
				
		html.append(d.getAccountManager());
		
		html.append("</td>\r\n" + 
				"						<td>\r\n" + 
				"							<strong>Due Date</strong>\r\n" + 
				"						</td>\r\n" + 
				"						<td>");
		
		html.append(String.format("%s", d.getDueDate()));
		
		html.append("</td>\r\n" + 
				"					</tr>\r\n" + 
				"				</table>");
		
		
		/*
		 * This part adds the Rental units table
		 * */
		html.append("<h3 style=\"text-align:center\">Rental Units</h3>\r\n" + 
				"<div id=\"ClientForm\"\r\n" + 
				"				<div id=\"itemList\" class=\"row\">\r\n");
		
		//find the computers belonging to this delivery
		manager.connect();
		List<Computer> foo = constructor.fetchCompleteComputers(manager, d.getDeliveryID());
		manager.disconnect();
		
		//two columns to divide the computers between
		//for each computer make a cell
		int blank = 0;
		html.append("<div class=\"column\">");
		for (int y = 0; y<foo.size(); y+=2) {
			html.append("<div class=\"container\">");
			
			//rental number
			html.append("<span class=\"RentalNumber\"><strong>" + foo.get(y).getRentalNumber() + "</strong></span><br>");
			
			//fill each cell with the Parts of the Computer
			for (int x = 0; x < NUMBER_OF_PARTS; x+=1) {
				manager.connect();
				Parts result = constructor.fetchParts(manager, foo.get(y).getPartIDs().get(x)).get(0);
				manager.disconnect();
				
				if (!result.getName().equals("N/A")) html.append("<span>" + String.format("%s: %s", result.getPartType(), result.getName()) + "</span><br>");
				else blank += 1;
			}
			
			for (int x = 0; x < blank; x+=1) {
				html.append("<span></span><br>");
			}
			html.append("</div>");
			
			blank = 0;
		}
		html.append("</div>");
		
		blank = 0;
		html.append("<div class=\"column\">");
		for (int y = 1; y<foo.size(); y+=2) {
			html.append("<div class=\"container\">");
			
			//rental number
			html.append("<span class=\"RentalNumber\"><strong>" + foo.get(y).getRentalNumber() + "</strong></span><br>");
			
			//fill each cell with the Parts of the Computer
			for (int x = 0; x < NUMBER_OF_PARTS; x+=1) {
				manager.connect();
				Parts result = constructor.fetchParts(manager, foo.get(y).getPartIDs().get(x)).get(0);
				manager.disconnect();
				
				if (!result.getName().equals("N/A")) html.append("<span>" + String.format("%s: %s", result.getPartType(), result.getName()) + "</span><br>");
				else blank += 1;
			}
			
			for (int x = 0; x < blank; x+=1) {
				html.append("<span></span><br>");
			}
			html.append("</div>");
			
			blank = 0;
		}
		html.append("</div>");
		
		html.append("				</div>\r\n");
		
		/*
		 * This part makes the table for the Delivery's Accessories
		 * */
		html.append("<h3 style=\"text-align:center\">Accessories</h3>\r\n" + 
				"			<div id=\"partsList\"\r\n" + 
				"				<div id=\"itemList\" class=\"row\">");
		
		//find the accessories belonging the delivery
		manager.connect();
		List<Accessory> bar = constructor.fetchDeliveryAccessories(manager, d.getDeliveryID());
		manager.disconnect();

		//for every 4 accessories make a cell
		for (int y = 0; y<bar.size(); y+=4) {
			html.append("<div class=\"container\">");
			
			//fill each cell with 4 accessories
			for (int x = 0; x < 4; x+=1) {
				if (y+x < bar.size()) {
					html.append("<span class=\"rentalNumber\"><strong>" + 
							bar.get(y+x).getRentalNumber() + 
							"</strong>" +
							" " +
							bar.get(y+x).getName() +
							"</span><br><br>");
				}
			}
			
			html.append("</div>");
		}
		
		manager.connect();
		List<SmallAccessory> foobar = constructor.fetchDeliveryPeripherals(manager, d.getDeliveryID());
		manager.disconnect();
		
		//for every 4 accessories make a cell
		for (int y = 0; y<foobar.size(); y+=4) {
			html.append("<div class=\"container\">");

			//fill each cell with 4 accessories
			for (int x = 0; x < 4; x+=1) {
				if (y+x < foobar.size()) {
					html.append("<span class=\"rentalNumber\"><strong>" + 
							foobar.get(y+x).getName() + 
							"</strong>" +
							" x " +
							foobar.get(y+x).getQuantity() +
							"</span><br><br>");
				}
			}

			html.append("</div>");
		}
		
		html.append("</div></div>");
		
		html.append("			</div>\r\n" + 
				"		</div>\r\n" + 
				"	</div>\r\n" + 
				"</body>\r\n" + 
				"</html>\r\n" + 
				"\r\n" + 
				"<!-- 20%  50%-->");
		
		String s = html.toString();
		return s;
	}
	
	/**
	 * Generates an html form from a Delivery input,
	 * three lists containing rental units to be pulled out,
	 * and the PullOut form number.
	 * @param d
	 * @param prepared_comp
	 * @param prepared_acc
	 * @param prepared_per
	 * @param PO
	 * @return
	 */
	public String generate_pull_print_html(Delivery d, 
			List<Computer> prepared_comp, 
			List<Accessory> prepared_acc, 
			List<SmallAccessory> prepared_per,
			String PO) {		
		StringBuilder html = new StringBuilder();
		html.append("<!DOCTYPE html>\r\n" + 
				"<html lang=\"en\">\r\n" + 
				"<head>\r\n" + 
				"	<meta charset=\"UTF-8\">\r\n" + 
				String.format("	<title>Delivery#%s - Pull-out Form</title>\r\n", d.getDeliveryID()) +
				"<style>\r\n" + 
				".column {\r\n" + 
				"	float: left;\r\n" + 
				"	width: 50%;\r\n" + 
				"}\r\n" + 
				"\r\n" + 
				".row:after {\r\n" + 
				"	content: \"\";\r\n" + 
				"	display: table;\r\n" + 
				"	clear: both;\r\n" + 
				"}\r\n" + 
				"\r\n" + 
				".container {\r\n" + 
				"	border-style: solid;\r\n" + 
				"	border-width: 1px;\r\n" + 
				"	border-top-style: hidden;\r\n" + 
				"	border-right-style: hidden;\r\n" + 
				"	border-bottom-style: solid;\r\n" + 
				"	border-left-style: hidden;\r\n" + 
				"	margin: 5px;\r\n" + 
				"}\r\n" +
				"</style>" +
				"</head>");
		
		/*
		 * This parts adds the header that contains the Delivery data
		 * */
		html.append("<body>\r\n" +
				"<h3 style=\"text-align:center\">" + 
				String.format("	Delivery#%s - Pull-out Form\r\n", d.getDeliveryID()) +
				"</h3>\r\n" + 
				"	<div id=\"main-container\"\r\n" + 
				"		<div id=\"rr-form\">\r\n" + 
				"				<table border=\"1px\" width=\"100%\">\r\n" + 
				"					<tr>\r\n" + 
				"						<td width=\"20%\"><strong>CLIENT</strong></td>\r\n" + 
				"						<td width=\"50%\">");
				
		html.append(d.getcustomerName());
		
		html.append("</td>\r\n" + 
				"						<td>\r\n" + 
				"							<strong>SO#</strong>\r\n" + 
				"						</td>\r\n" + 
				"						<td>");
		
		html.append(d.getSO());
		
		html.append("</td>\r\n" + 
				"					</tr>\r\n" + 
				"					<tr>\r\n" + 
				"						<td width=\"20%\">\r\n" + 
				"							<strong>DEL. DATE</strong>\r\n" + 
				"						</td>\r\n" + 
				"						<td width=\"50%\">");
				
		html.append(d.getReleaseDate());
		
		html.append("</td>\r\n" + 
				"						<td>\r\n" + 
				"							<strong>PO#</strong>\r\n" + 
				"						</td>\r\n" + 
				"						<td>");
				
		html.append(PO);
		
		
		html.append("</td>\r\n" + 
				"					</tr>\r\n" + 
				"					<tr>\r\n" + 
				"						<td width=\"20%\">\r\n" + 
				"							<strong>ACCOUNT MANAGER</strong>\r\n" + 
				"						</td>\r\n" + 
				"						<td width=\"50%\">");
				
		html.append(d.getAccountManager());
		
		html.append("</td>\r\n" + 
				"						<td>\r\n" + 
				"							<strong>Due Date</strong>\r\n" + 
				"						</td>\r\n" + 
				"						<td>");
		
		html.append(String.format("%s", d.getDueDate()));
		
		html.append("</td>\r\n" + 
				"					</tr>\r\n" + 
				"				</table>");
		
		
		/*
		 * This part adds the Rental units table
		 * */
		html.append("<h3 style=\"text-align:center\">Rental Units</h3>\r\n" + 
				"<div id=\"ClientForm\"\r\n" + 
				"				<div id=\"itemList\" class=\"row\">\r\n");
		
		//find the computers belonging to this delivery
		List<Computer> foo = prepared_comp;
		
		//two columns to divide the computers between
		//for each computer make a cell
		int blank = 0;
		html.append("<div class=\"column\">");
		for (int y = 0; y<foo.size(); y+=2) {
			html.append("<div class=\"container\">");
			
			//rental number
			html.append("<span class=\"rentalNumber\"><strong>" + foo.get(y).getRentalNumber() + "</strong></span><br>");
			
			//fill each cell with the Parts of the Computer
			for (int x = 0; x < NUMBER_OF_PARTS; x+=1) {
				manager.connect();
				Parts result = constructor.fetchParts(manager, foo.get(y).getPartIDs().get(x)).get(0);
				manager.disconnect();
				
				if (!result.getName().equals("N/A")) html.append("<span>" + String.format("%s: %s", result.getPartType(), result.getName()) + "</span><br>");
				else blank += 1;
			}
			
			for (int x = 0; x < blank; x+=1) {
				html.append("<span></span><br>");
			}
			html.append("</div>");
			
			blank = 0;
		}
		html.append("</div>");
		
		blank = 0;
		html.append("<div class=\"column\">");
		for (int y = 1; y<foo.size(); y+=2) {
			html.append("<div class=\"container\">");
			
			//rental number
			html.append("<span class=\"rentalNumber\"><strong>" + foo.get(y).getRentalNumber() + "</strong></span><br>");
			
			//fill each cell with the Parts of the Computer
			for (int x = 0; x < NUMBER_OF_PARTS; x+=1) {
				manager.connect();
				Parts result = constructor.fetchParts(manager, foo.get(y).getPartIDs().get(x)).get(0);
				manager.disconnect();
				
				if (!result.getName().equals("N/A")) html.append("<span>" + String.format("%s: %s", result.getPartType(), result.getName()) + "</span><br>");
				else blank += 1;
			}
			
			for (int x = 0; x < blank; x+=1) {
				html.append("<span></span><br>");
			}
			html.append("</div>");
			
			blank = 0;
		}
		html.append("</div>");
		
		html.append("				</div>\r\n");
		
		/*
		 * This part makes the table for the Delivery's Accessories
		 * */
		html.append("<h3 style=\"text-align:center\">Accessories</h3>\r\n" + 
				"			<div id=\"partsList\"\r\n" + 
				"				<div id=\"itemList\" class=\"row\">");
		
		//find the accessories belonging the delivery
		List<Accessory> bar = prepared_acc;

		//for every 4 accessories make a cell
		for (int y = 0; y<bar.size(); y+=4) {
			html.append("<div class=\"container\">");
			
			//fill each cell with 4 accessories
			for (int x = 0; x < 4; x+=1) {
				if (y+x < bar.size()) {
					html.append("<span class=\"rentalNumber\"><strong>" + 
							bar.get(y+x).getRentalNumber() + 
							"</strong>" +
							" " +
							bar.get(y+x).getName() +
							"</span><br><br>");
				}
			}
			
			html.append("</div>");
		}
		
		List<SmallAccessory> foobar = prepared_per;
		
		//for every 4 accessories make a cell
		for (int y = 0; y<foobar.size(); y+=4) {
			html.append("<div class=\"container\">");

			//fill each cell with 4 accessories
			for (int x = 0; x < 4; x+=1) {
				if (y+x < foobar.size()) {
					html.append("<span class=\"rentalNumber\"><strong>" + 
							foobar.get(y+x).getName() + 
							"</strong>" +
							" x " +
							foobar.get(y+x).getQuantity() +
							"</span><br><br>");
				}
			}

			html.append("</div>");
		}
		
		html.append("</div></div>");
		
		html.append("			</div>\r\n" + 
				"		</div>\r\n" + 
				"	</div>\r\n" + 
				"</body>\r\n" + 
				"</html>\r\n" + 
				"\r\n" + 
				"<!-- 20%  50%-->");
		
		String s = html.toString();
		return s;
		
	}
}
