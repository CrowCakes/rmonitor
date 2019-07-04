package com.example.rmonitor.classes;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import java.sql.Date;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Constructs a List of Objects to populate the Grids in the MainScreen Views
 * */
public class ObjectConstructor {
	
	/**
	 * The number of Parts per Computer that the webapp is configured to support. If this needs to be changed,
	 * several other modules within the webapp that contain this variable must also be changed.
	 */
	int NUMBER_OF_PARTS = 9;
	
	/***
	 * Returns the number of Computers returned by the server query. Similar in structure to parseComputer.
	 * @param foo The list of Computers as a raw String
	 * @return Number of Computers returned by the server query
	 */
	private int computerCount(String foo) {
		List<String> bar = new ArrayList<String>(Arrays.asList(foo.split("::\n")));
		//Each computer entry occurs at N*(NUMBER_OF_PARTS+1), where N is a whole number
		return bar.size() / (NUMBER_OF_PARTS+1);
	}
	
	/**
	 * Parses a string to construct a List of Computers.
	 * */
	private List<Computer> parseComputer(String foo) {
		List<Computer> parsed_data = new ArrayList<>();
		
		List<String> bar = new ArrayList<String>(Arrays.asList(foo.split("::\n")));
		
		/**
		 * Currently, every Computer entry is separated by NUMBER_OF_PARTS Part entries, so there is a
		 * need to skip over those entries
		 * */
		List<String> foobar;
		List<Integer> parts;
		for (int i=0; i < bar.size(); i = i + NUMBER_OF_PARTS + 1) {
			
			/**
			 * Reset the placeholder list, then
			 * store the PartIDs of the NUMBER_OF_PARTS Parts belonging to the Computer
			 * */
			parts = new ArrayList<>();
			
			for (int j=1; j<NUMBER_OF_PARTS + 1; j=j+1) {
				if (i+j < bar.size()) {
					foobar = new ArrayList<>(Arrays.asList(bar.get(i+j).split("\\s*::,\\s*")));
					parts.add(Integer.parseInt(foobar.get(0)));
				}
				else {
					parts.add(0);
				}
			}
			
			
			/**
			 * Split the Computer string by comma
			 * */
			foobar = new ArrayList<>(Arrays.asList(bar.get(i).split("\\s*::,\\s*")));
			if (foobar.size() == 1) {
				return new ArrayList<>();
			}
			/**
			 * Add the Computer and its Parts to the List of Computers to be returned
			 * Do a manual check for the boolean's value, since it's just an integer and not a Java boolean
			 * */
			if (Integer.parseInt(foobar.get(6)) != 0) {
				parsed_data.add(new Computer(foobar.get(0), 
						foobar.get(2), 
						foobar.get(3), 
						foobar.get(4), 
						Date.valueOf(foobar.get(5)), 
						Boolean.TRUE, 
						parts,
						foobar.get(1),
						foobar.get(7),
						Float.parseFloat(foobar.get(8))
						));
			}
			else {
				parsed_data.add(new Computer(foobar.get(0), 
						foobar.get(2), 
						foobar.get(3), 
						foobar.get(4), 
						Date.valueOf(foobar.get(5)), 
						Boolean.FALSE, 
						parts,
						foobar.get(1),
						foobar.get(7),
						Float.parseFloat(foobar.get(8))
						));
			}
			
		}
		return parsed_data;
	}
	
	/***
	 * Parses a string to construct a List of Computers that start at index offset and end at index offset+limit.
	 * @param foo The list of Computers as a raw String
	 * @param offset The index to begin parsing at (inclusive)
	 * @param limit The index to end parsing at (exclusive)
	 * @return
	 */
	private List<Computer> parseComputer(String foo, int offset, int limit) {
		List<Computer> parsed_data = new ArrayList<>();
		
		List<String> bar = new ArrayList<String>(Arrays.asList(foo.split("::\n")));
		bar = bar.subList(offset*(NUMBER_OF_PARTS + 1), (offset+limit)*(NUMBER_OF_PARTS + 1));
		
		/**
		 * Currently, every Computer entry is separated by NUMBER_OF_PARTS Part entries, so there is a
		 * need to skip over those entries
		 * */
		List<String> foobar;
		List<Integer> parts;
		for (int i=0; i < bar.size(); i = i + NUMBER_OF_PARTS + 1) {
			
			/**
			 * Reset the placeholder list, then
			 * store the PartIDs of the NUMBER_OF_PARTS Parts belonging to the Computer
			 * */
			parts = new ArrayList<>();
			
			for (int j=1; j<NUMBER_OF_PARTS + 1; j=j+1) {
				if (i+j < bar.size()) {
					foobar = new ArrayList<>(Arrays.asList(bar.get(i+j).split("\\s*::,\\s*")));
					parts.add(Integer.parseInt(foobar.get(0)));
				}
				else {
					parts.add(0);
				}
			}
			
			
			/**
			 * Split the Computer string by comma
			 * */
			foobar = new ArrayList<>(Arrays.asList(bar.get(i).split("\\s*::,\\s*")));
			if (foobar.size() == 1) {
				return new ArrayList<>();
			}
			/**
			 * Add the Computer and its Parts to the List of Computers to be returned
			 * Do a manual check for the boolean's value, since it's just an integer and not a Java boolean
			 * */
			if (Integer.parseInt(foobar.get(6)) != 0) {
				parsed_data.add(new Computer(foobar.get(0), 
						foobar.get(2), 
						foobar.get(3), 
						foobar.get(4), 
						Date.valueOf(foobar.get(5)), 
						Boolean.TRUE, 
						parts,
						foobar.get(1),
						foobar.get(7),
						Float.parseFloat(foobar.get(8))
						));
			}
			else {
				parsed_data.add(new Computer(foobar.get(0), 
						foobar.get(2), 
						foobar.get(3), 
						foobar.get(4), 
						Date.valueOf(foobar.get(5)), 
						Boolean.FALSE, 
						parts,
						foobar.get(1),
						foobar.get(7),
						Float.parseFloat(foobar.get(8))
						));
			}
			
		}
		//return parsed_data.subList(offset, offset+limit);
		return parsed_data;
	}
	
	/**
	 * Parses a string to construct a List of Computer. String must not contain Parts information.
	 * */
	private List<Computer> parseComputerNoParts(String foo) {
		List<Computer> parsed_data = new ArrayList<>();
		
		/** 
		 * split the response by newline, making a list of strings containing the information
		 * of a single computer
		 * */
		List<String> bar = new ArrayList<String>(Arrays.asList(foo.split("::\n")));
		
		List<String> foobar;
		List<Integer> parts = new ArrayList<>();
		for (int i=0; i < bar.size(); i = i + 1) {
			/**
			 * Split the Computer string by comma
			 * */
			foobar = new ArrayList<>(Arrays.asList(bar.get(i).split("\\s*::,\\s*")));
			if (i==0 && foobar.size() == 1) {
				//System.out.println("parseComputerNoParts: Empty computer");
				break;
			}
			/**
			 * Add the Computer and its Parts to the List of Computers to be returned
			 * Do a manual check for the boolean's value, since it's just an integer and not a Java boolean
			 * */
			if (Integer.parseInt(foobar.get(6)) != 0) {
				parsed_data.add(new Computer(foobar.get(0), 
						foobar.get(2), 
						foobar.get(3), 
						foobar.get(4), 
						Date.valueOf(foobar.get(5)), 
						Boolean.TRUE, 
						parts,
						foobar.get(1),
						foobar.get(7),
						Float.parseFloat(foobar.get(8))
						)
						);
			}
			else {
				parsed_data.add(new Computer(foobar.get(0), 
						foobar.get(2), 
						foobar.get(3), 
						foobar.get(4), 
						Date.valueOf(foobar.get(5)), 
						Boolean.FALSE, 
						parts,
						foobar.get(1),
						foobar.get(7),
						Float.parseFloat(foobar.get(8))
						)
						);
			}
			
		}
		return parsed_data;
	}
	
	/**
	 * Parses a string to construct a List of Computers. Used for fetchXCompleteComputers.
	 * */
	private List<Computer> parseCompleteComputer(String foo) {
		List<Computer> parsed_data = new ArrayList<>();
		
		/** 
		 * split the response by newline, making a list of strings containing the information
		 * of a single computer's RentalNumber
		 * */
		List<String> bar = new ArrayList<String>(Arrays.asList(foo.split("::\n")));
		//System.out.println("parseCompleteComputer: " + foo);
		
		/**
		 * Add each RentalNumber in.
		 * Currently, every Computer entry is separated by 4 Part entries, so there is a
		 * need to skip over those entries
		 */
		List<String> foobar;
		List<Integer> parts;
		for (int i=0; i < bar.size(); i = i+NUMBER_OF_PARTS+1) {			
			/**
			 * Reset the placeholder list, then
			 * store the PartIDs of the 5 Parts belonging to the Computer
			 * */
			parts = new ArrayList<>();
			for (int j=1; j<NUMBER_OF_PARTS+1; j=j+1) {
				if (i+j < bar.size()) {
					foobar = new ArrayList<>(Arrays.asList(bar.get(i+j).split("\\s*::,\\s*")));
					parts.add(Integer.parseInt(foobar.get(0)));
				}
				else {
					parts.add(0);
				}
			}
			
			foobar = new ArrayList<>(Arrays.asList(bar.get(i).split("\\s*::,\\s*")));
			if (foobar.get(0).isEmpty()) {
				break;
			}
			parsed_data.add(new Computer(foobar.get(0), "", "", "", Date.valueOf("2000-01-01"), Boolean.FALSE, parts,
					"",
					"",
					Float.parseFloat(foobar.get(1))
					)
					);
		}
		return parsed_data;
	}
	
	/***
	 * Returns the number of Parts returned by the server query. Similar in structure to parsePart.
	 * @param foo The list of Parts as a raw String
	 * @return The number of Parts returned by the server query
	 */
	private int partsCount(String foo) {
		/* split the response by newline, making a list of strings containing the information
		 * of a single part
		 */
		List<String> bar = new ArrayList<String>(Arrays.asList(foo.split("::\n")));
		return bar.size();
	}
	
	/**
	 * Construct a List of Parts from a string.
	 * */
	private List<Parts> parsePart(String foo) {
		List<Parts> parsed_data = new ArrayList<>();
		
		/** split the response by newline, making a list of strings containing the information
		 * of a single computer
		 * */
		List<String> bar = new ArrayList<String>(Arrays.asList(foo.split("::\n")));
		
		//Parse the string containing the details of the Part
		List<String> foobar;
		for (int i=0; i < bar.size(); i = i + 1) {
			foobar = new ArrayList<>(Arrays.asList(bar.get(i).split("\\s*::,\\s*")));
			if (foobar.size() == 1) {
				return new ArrayList<>();
			}
			float price;
			try {
				price = Float.parseFloat(foobar.get(5));
			}
			catch (NumberFormatException ex) {
				price = 0;
			}
			parsed_data.add(
					new Parts(
							Integer.parseInt(foobar.get(0)), 
							foobar.get(1), 
							foobar.get(2), 
							foobar.get(3),
							foobar.get(4),
							price
							)
					);
		}
		
		return parsed_data;
	}
	
	/***
	 * Construct a List of Parts from a string, starting at index offset and ending at index offset+limit
	 * @param foo The list of Parts as a raw String
	 * @param offset The index to begin parsing at (inclusive)
	 * @param limit The index to end parsing at (exclusive)
	 * @return
	 */
	private List<Parts> parsePart(String foo, int offset, int limit) {
		List<Parts> parsed_data = new ArrayList<>();
		
		/** split the response by newline, making a list of strings containing the information
		 * of a single computer
		 * */
		List<String> bar = new ArrayList<String>(Arrays.asList(foo.split("::\n")));
		bar = bar.subList(offset, offset+limit);
		
		//Parse the string containing the details of the Part
		List<String> foobar;
		for (int i=0; i < bar.size(); i = i + 1) {
			foobar = new ArrayList<>(Arrays.asList(bar.get(i).split("\\s*::,\\s*")));
			if (foobar.size() == 1) {
				return new ArrayList<>();
			}
			float price;
			try {
				price = Float.parseFloat(foobar.get(5));
			}
			catch (NumberFormatException ex) {
				price = 0;
			}
			parsed_data.add(
					new Parts(
							Integer.parseInt(foobar.get(0)), 
							foobar.get(1), 
							foobar.get(2), 
							foobar.get(3), 
							foobar.get(4),
							price
							)
					);
		}
		
		return parsed_data;
	}
	
	/***
	 * Returns the number of Accessories returned by the server query. Similar in structure to parseAccessory.
	 * @param foo The list of Accessories as a raw String
	 * @return The number of Accessories returned by the server query
	 */
	private int accessoriesCount(String foo) {
		 /* split the response by newline, making a list of strings containing the information
		  * of a single accessory
		  * */
		List<String> bar = new ArrayList<String>(Arrays.asList(foo.split("::\n")));
		return bar.size();
	 }
	 
	/**
	 * Construct a List of Accessories from a string.
	 * */
	private List<Accessory> parseAccessory(String foo) {
		List<Accessory> parsed_data = new ArrayList<>();
		/** split the response by newline, making a list of strings containing the information
		 * of a single computer
		 * */
		List<String> bar = new ArrayList<String>(Arrays.asList(foo.split("::\n")));
		
		//Parse the string containing the details of the Part
		List<String> foobar;
		for (int i=0; i < bar.size(); i = i + 1) {
			foobar = new ArrayList<>(Arrays.asList(bar.get(i).split("\\s*::,\\s*")));
			if (foobar.size() == 1) {
				return new ArrayList<>();
			}
			parsed_data.add(new Accessory(
					foobar.get(2), 
					foobar.get(0), 
					foobar.get(1), 
					foobar.get(3),
					foobar.get(5),
					Float.parseFloat(foobar.get(4))
					)
					);
		}
		return parsed_data;
	}
	
	/***
	 * Construct a List of Accessories from a string, starting at index offset and ending at index offset+limit.
	 * @param foo The list of Accessories as a raw String
	 * @param offset The index to begin parsing at (inclusive)
	 * @param limit The index to end parsing at (exclusive)
	 * @return
	 */
	private List<Accessory> parseAccessory(String foo, int offset, int limit) {
		List<Accessory> parsed_data = new ArrayList<>();
		/** split the response by newline, making a list of strings containing the information
		 * of a single computer
		 * */
		List<String> bar = new ArrayList<String>(Arrays.asList(foo.split("::\n")));
		bar = bar.subList(offset, offset+limit);
		
		//Parse the string containing the details of the Part
		List<String> foobar;
		for (int i=0; i < bar.size(); i = i + 1) {
			foobar = new ArrayList<>(Arrays.asList(bar.get(i).split("\\s*::,\\s*")));
			if (foobar.size() == 1) {
				return new ArrayList<>();
			}
			parsed_data.add(
					new Accessory(
							foobar.get(2), 
							foobar.get(0), 
							foobar.get(1), 
							foobar.get(3),
							foobar.get(5),
							Float.parseFloat(foobar.get(4))
					)
					);
		}
		return parsed_data;
	}
	
	/**
	 * Construct a List of SmallAccessories from a string.
	 * @param foo
	 * @return
	 */
	private List<SmallAccessory> parseSmallAccessory(String foo) {
		List<SmallAccessory> parsed_data = new ArrayList<>();
		/** split the response by newline, making a list of strings containing the information
		 * of a single computer
		 * */
		List<String> bar = new ArrayList<String>(Arrays.asList(foo.split("::\n")));
		
		//Parse the string containing the details of the Part
		List<String> foobar;
		for (int i=0; i < bar.size(); i = i + 1) {
			foobar = new ArrayList<>(Arrays.asList(bar.get(i).split("\\s*::,\\s*")));
			if (foobar.size() == 1) {
				return new ArrayList<>();
			}
			parsed_data.add(new SmallAccessory(foobar.get(0), foobar.get(1), 
					Float.parseFloat(foobar.get(2)), 
					Integer.parseInt(foobar.get(3)),
					Integer.parseInt(foobar.get(4))
					)
					);
		}
		return parsed_data;
	}
	
	/**
	 * Construct a List of SmallAccessories from a string. 
	 * The created objects info are not complete, unlike parseSmallAccessory.
	 * The Quantity field in these objects refer to the amount of peripherals assigned to this Delivery
	 * @param foo
	 * @return
	 */
	private List<SmallAccessory> parsePeripheral(String foo) {
		List<SmallAccessory> parsed_data = new ArrayList<>();
		/** split the response by newline, making a list of strings containing the information
		 * of a single computer
		 * */
		List<String> bar = new ArrayList<String>(Arrays.asList(foo.split("::\n")));
		
		//Parse the string containing the details of the Part
		List<String> foobar;
		for (int i=0; i < bar.size(); i = i + 1) {
			foobar = new ArrayList<>(Arrays.asList(bar.get(i).split("\\s*::,\\s*")));
			if (foobar.size() == 1) {
				return new ArrayList<>();
			}
			parsed_data.add(new SmallAccessory(foobar.get(0), "", 
					0f, 
					Integer.parseInt(foobar.get(1)),
					0
					)
					);
		}
		return parsed_data;
	}
	
	/***
	 * Returns the number of Deliveries returned from server query. Similar in structure to parseDelivery
	 * @param foo The list of Deliveries as a raw String
	 * @return The number of Deliveries returned from server query
	 */
	private int deliveryCount(String foo) {
		/** split the response by newline, making a list of strings containing the information
		 * of a single delivery
		 * */
		List<String> bar = new ArrayList<String>(Arrays.asList(foo.split("::\n")));
		return bar.size();
	}
	
	/**
	 * Construct a List of Deliveries from a string.
	 * @param foo
	 * @return
	 */
	private List<Delivery> parseDelivery(String foo) {
		List<Delivery> parsed_data = new ArrayList<>();
		
		/* split the response by newline */
		List<String> bar = new ArrayList<String>(Arrays.asList(foo.split("::\n")));
		
		//Parse the string containing the details of the Part
		List<String> foobar;
		String so, si, ard, pos; int extension, freq;
		for (int i=0; i < bar.size(); i = i + 1) {
			foobar = new ArrayList<>(Arrays.asList(bar.get(i).split("\\s*::,\\s*")));
			if (foobar.size() == 1) {
				return new ArrayList<>();
			}
			so = foobar.get(2);
			si = foobar.get(3);
			ard = foobar.get(4);
			pos = foobar.get(5);
				
				try {
					extension = Integer.parseInt(foobar.get(10));
				} catch (NumberFormatException ex) {
					extension = 0;
				}
				try {
					freq = Integer.parseInt(foobar.get(11));
				}
				catch (NumberFormatException ex) {
					freq = 0;
				}
				
				parsed_data.add(new Delivery(
						Integer.parseInt(foobar.get(0)), 
						so, 
						si, 
						ard, 
						pos, 
						foobar.get(1), 
						Date.valueOf(foobar.get(6)), 
						Date.valueOf(foobar.get(7)), 
						foobar.get(8), 
						foobar.get(9), 
						extension,
						freq
						)
						);

		}
		
		return parsed_data;
	}
	
	private Delivery parseLastInsertedDelivery(String foo) {
		List<Delivery> parsed_data = new ArrayList<>();
		
		/* split the response by newline */
		List<String> bar = new ArrayList<String>(Arrays.asList(foo.split("::\n")));
		
		//Parse the string containing the details of the Part
		List<String> foobar;
		String so, si, ard, pos; int extension, freq;
		for (int i=bar.size()-1; i < bar.size(); i = i + 1) {
			foobar = new ArrayList<>(Arrays.asList(bar.get(i).split("\\s*::,\\s*")));
			so = foobar.get(2);
			si = foobar.get(3);
			ard = foobar.get(4);
			pos = foobar.get(5);
				
				try {
					extension = Integer.parseInt(foobar.get(10));
				} catch (NumberFormatException ex) {
					extension = 0;
				}
				try {
					freq = Integer.parseInt(foobar.get(11));
				}
				catch (NumberFormatException ex) {
					freq = 0;
				}
				
				parsed_data.add(new Delivery(
						Integer.parseInt(foobar.get(0)), 
						so, 
						si, 
						ard, 
						pos, 
						foobar.get(1), 
						Date.valueOf(foobar.get(6)), 
						Date.valueOf(foobar.get(7)), 
						foobar.get(8), 
						foobar.get(9), 
						extension,
						freq
						)
						);

		}
		
		return parsed_data.get(0);
	}
	
	/***
	 * Construct a List of Deliveries from a string, starting at index offset and ending at index offset+limit.
	 * @param foo The list of Deliveries as a raw String
	 * @param offset The index to begin parsing at (inclusive)
	 * @param limit The index to end parsing at (exclusive)
	 * @return
	 */
	private List<Delivery> parseDelivery(String foo, int offset, int limit) {
		List<Delivery> parsed_data = new ArrayList<>();
		
		/** split the response by newline, making a list of strings containing the information
		 * of a single computer
		 * */
		List<String> bar = new ArrayList<String>(Arrays.asList(foo.split("::\n")));
		bar = bar.subList(offset, offset+limit);
		
		//Parse the string containing the details of the Part
		List<String> foobar;
		String so, si, ard, pos; int extension, freq;
		for (int i=0; i < bar.size(); i = i + 1) {
			foobar = new ArrayList<>(Arrays.asList(bar.get(i).split("\\s*::,\\s*")));
			if (foobar.size() == 1) {
				return new ArrayList<>();
			}
			so = foobar.get(2);
			si = foobar.get(3);
			ard = foobar.get(4);
			pos = foobar.get(5);
				
				try {
					extension = Integer.parseInt(foobar.get(10));
				} catch (NumberFormatException ex) {
					extension = 0;
				}
				try {
					freq = Integer.parseInt(foobar.get(11));
				}
				catch (NumberFormatException ex) {
					freq = 0;
				}
				
				parsed_data.add(new Delivery(
						Integer.parseInt(foobar.get(0)), 
						so, 
						si, 
						ard, 
						pos, 
						foobar.get(1), 
						Date.valueOf(foobar.get(6)), 
						Date.valueOf(foobar.get(7)), 
						foobar.get(8), 
						foobar.get(9), 
						extension,
						freq
						)
						);

		}
		
		return parsed_data;
	}
	
	/**
	 * Construct a List of Deliveries from a string. Will return null if an error is encountered while parsing the string.
	 * @param foo
	 * @return
	 */
	private List<Delivery> parseDeliveryNullOnError(String foo) {
		List<Delivery> parsed_data = new ArrayList<>();
		
		/** split the response by newline, making a list of strings containing the information
		 * of a single computer
		 * */
		List<String> bar = new ArrayList<String>(Arrays.asList(foo.split("::\n")));
		
		//Parse the string containing the details of the Part
		List<String> foobar;
		String so, si, ard, pos; int extension, freq;
		for (int i=0; i < bar.size(); i = i + 1) {
			foobar = new ArrayList<>(Arrays.asList(bar.get(i).split("\\s*::,\\s*")));
			if (foobar.size() == 1) {
				System.out.println("empty set returned from server");
				System.out.println(foobar);
				return null;
			}
			so = foobar.get(2);
			si = foobar.get(3);
			ard = foobar.get(4);
			pos = foobar.get(5);
				
				try {
					extension = Integer.parseInt(foobar.get(10));
				} catch (NumberFormatException ex) {
					extension = 0;
				}
				try {
					freq = Integer.parseInt(foobar.get(11));
				}
				catch (NumberFormatException ex) {
					freq = 0;
				}
				
				parsed_data.add(new Delivery(
						Integer.parseInt(foobar.get(0)), 
						so, 
						si, 
						ard, 
						pos, 
						foobar.get(1), 
						Date.valueOf(foobar.get(6)), 
						Date.valueOf(foobar.get(7)), 
						foobar.get(8), 
						foobar.get(9), 
						extension,
						freq
						)
						);

		}
		
		return parsed_data;
	}
	
	/**
	 * Construct a List of Deliveries from a string. Will return a dummy list if an error is encountered while parsing the string.
	 * @param foo
	 * @return
	 */
	private List<Delivery> parseDeliveryBlankOnError(String foo) {
		List<Delivery> parsed_data = new ArrayList<>();
		
		/** split the response by newline, making a list of strings containing the information
		 * of a single computer
		 * */
		List<String> bar = new ArrayList<String>(Arrays.asList(foo.split("::\n")));
		
		//Parse the string containing the details of the Delivery
		List<String> foobar;
		String so, si, ard, pos; int extension, freq;
		for (int i=0; i < bar.size(); i = i + 1) {
			foobar = new ArrayList<>(Arrays.asList(bar.get(i).split("\\s*::,\\s*")));
			if (foobar.size() == 1) {
				parsed_data.add(new 
						Delivery(0, "", "", "", "", "", null, null, "", "", 0, 0));
				break;
			}
				so = foobar.get(2);
				si = foobar.get(3);
				ard = foobar.get(4);
				pos = foobar.get(5);
				
				try {
					extension = Integer.parseInt(foobar.get(10));
				} catch (NumberFormatException ex) {
					extension = 0;
				}
				
				try {
					freq = Integer.parseInt(foobar.get(11));
				} catch (NumberFormatException ex) {
					freq = 0;
				}
				
				parsed_data.add(new Delivery(
						Integer.parseInt(foobar.get(0)), 
						so, 
						si, 
						ard, 
						pos, 
						foobar.get(1), 
						Date.valueOf(foobar.get(6)), 
						Date.valueOf(foobar.get(7)), 
						foobar.get(8), 
						foobar.get(9), 
						extension,
						freq
						)
						);

		}
		
		return parsed_data;
	}
	
	/**
	 * Construct a List of Integers (ExtensionIDs) that form a chain of extended Deliveries, 
	 * starting from delvID.
	 * @param foo
	 * @param delvID
	 * @return
	 */
	private List<Integer> parseDeliveryExt(String foo, int delvID) {
		int ext = delvID;
		List<Integer> parsed_data = new ArrayList<>();
		
		List<String> bar = new ArrayList<String>(Arrays.asList(foo.split("::\n")));
		
		//Parse the string containing the details of the Part
		List<String> foobar;
		int extension;
		
		//loop over and over until end of chain is reached
		Boolean isBreak = false;
		while (!isBreak) {
			for (int i=0; i < bar.size(); i = i + 1) {
				foobar = new ArrayList<>(Arrays.asList(bar.get(i).split("\\s*::,\\s*")));
				if (foobar.size() == 1) {
					return new ArrayList<>();
				}

				try {
					extension = Integer.parseInt(foobar.get(10));
				} catch (NumberFormatException ex) {
					extension = 0;
				}

				if (extension == ext) {
					parsed_data.add(Integer.parseInt(foobar.get(0)));
					ext = Integer.parseInt(foobar.get(0));
					//start over from the top
					break;
					}
				else {
					//found no more matches
					if (i == bar.size() - 1) isBreak = true;
					continue;
				}
			}
		}
		
		return parsed_data;
	}
	
	/**
	 * Construct a List of PullOutForms from a string.
	 * @param foo
	 * @return
	 */
	private List<PullOutForm> parsePullOutForm(String foo) {
		List<PullOutForm> parsed_data = new ArrayList<>();
		
		List<String> bar = new ArrayList<String>(Arrays.asList(foo.split("::\n")));
		
		List<String> foobar;
		for (int i=0; i < bar.size(); i = i + 1) {
			foobar = new ArrayList<>(Arrays.asList(bar.get(i).split("\\s*::,\\s*")));
			if (foobar.size() == 1) {
				break;
			}
			
			parsed_data.add(new PullOutForm(
					Integer.parseInt(foobar.get(0)),
					foobar.get(1),
					Date.valueOf(foobar.get(2)),
					foobar.get(3)
					));
		}
		
		return parsed_data;
	}
	
	/**
	 * Construct a List of PullOutForms from a string, starting at index offset and ending at index offset+limit.
	 * @param foo The list of PullOutForms as a raw String
	 * @param offset The index to begin parsing at (inclusive)
	 * @param limit The index to end parsing at (exclusive)
	 * @return
	 */
	private List<PullOutForm> parsePullOutForm(String foo, int offset, int limit) {
		List<PullOutForm> parsed_data = new ArrayList<>();
		
		List<String> bar = new ArrayList<String>(Arrays.asList(foo.split("::\n")));
		bar = bar.subList(offset, offset+limit);
		
		List<String> foobar;
		for (int i=0; i < bar.size(); i = i + 1) {
			foobar = new ArrayList<>(Arrays.asList(bar.get(i).split("\\s*::,\\s*")));
			if (foobar.size() == 1) {
				break;
			}
			
			parsed_data.add(new PullOutForm(
					Integer.parseInt(foobar.get(0)),
					foobar.get(1),
					Date.valueOf(foobar.get(2)),
					foobar.get(3)
					));
		}
		
		return parsed_data;
	}
	
	/***
	 * Returns the number of Pull-outs returned from server query. Similar in structure to parsePullOutForm.
	 * @param foo The list of Pull-outs as a raw String
	 * @return The number of Pull-outs returned from server query
	 */
	private int pullOutCount(String foo) {
		List<String> bar = new ArrayList<String>(Arrays.asList(foo.split("::\n")));
		return bar.size();
	}
	
	/**
	 * Construct a List of Users from a string.
	 * @param foo
	 * @return
	 */
	private List<User> parseUserData(String foo) {
		List<User> parsed_data = new ArrayList<>();
		
		List<String> bar = new ArrayList<String>(Arrays.asList(foo.split("::\n")));
		
		List<String> foobar;
		for (int i=0; i < bar.size(); i = i + 1) {
			foobar = new ArrayList<>(Arrays.asList(bar.get(i).split("\\s*::,\\s*")));
			if (foobar.size() == 1) {
				break;
			}
			
			parsed_data.add(new User(foobar.get(0), foobar.get(1)));
		}
		
		return parsed_data;
	}

	/**
	 * Generates a hashed password from a given string and a user-input salt. SHA-512 is used as the hashing technique.
	 * @param passwordToHash
	 * @param salt
	 * @return
	 */
	private String get_SHA_512_SecurePassword(String passwordToHash, String salt){
		String generatedPassword = null;
		    try {
		         MessageDigest md = MessageDigest.getInstance("SHA-512");
		         md.update(salt.getBytes(StandardCharsets.UTF_8));
		         byte[] bytes = md.digest(passwordToHash.getBytes(StandardCharsets.UTF_8));
		         StringBuilder sb = new StringBuilder();
		         for(int i=0; i< bytes.length ;i++){
		            sb.append(Integer.toString((bytes[i] & 0xff) + 0x100, 16).substring(1));
		         }
		         generatedPassword = sb.toString();
		        } 
		       catch (NoSuchAlgorithmException e){
		        e.printStackTrace();
		       }
		    return generatedPassword;
		}
	
//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////	
	
	/**
	 * Create the list of Computers by first requesting for the Computers 
	 * from the database controller, then converting the raw string output into
	 * a list of Computer objects.
	 * 
	 * The function assumes that the connection passed to it is active
	 * */
 	public List<Computer> constructComputers(ConnectionManager manager) {
		List<Computer> parsed_data = new ArrayList<>();
		
		//query the information from database
		String foo = new String(manager.send("ViewComputers"));
		
		parsed_data = parseComputer(foo);
		return parsed_data;
	}
	
	/**
	 * Create the list of Computers by first requesting for the Computers that match the user-input
	 * from the database controller, then converting the raw string output into
	 * a list of Computer objects.
	 * 
	 * The function assumes that the connection passed to it is active
	 * */
	public List<Computer> constructComputers(ConnectionManager manager, String rentalNumber) {
		List<Computer> parsed_data = new ArrayList<>();
		String query = String.format("ViewComputerRentalNumber\r\n%s", rentalNumber);
		
		//query the information from database
		String foo = new String(manager.send(query));
		
		parsed_data = parseComputer(foo);
		return parsed_data;
	}
	
	/**
	 * Create the list of Computers by first requesting for the Computers 
	 * from the database controller, then converting the raw string output into
	 * a list of Computer objects. Returns up to a specified number of entries at a time.
	 * 
	 * The function assumes that the connection passed to it is active
	 * */
	public List<Computer> constructComputers(ConnectionManager manager, int offset, int limit) {
		List<Computer> parsed_data = new ArrayList<>();
		
		//query the information from database
		String foo = new String(manager.send("ViewComputers"));
		
		parsed_data = parseComputer(foo, offset, limit);
		return parsed_data;
	}
	
	/***
	 * Returns the total number of Computers currently in database.
	 */
	public int getComputerCount(ConnectionManager manager) {
		String foo = new String(manager.send("ViewComputers"));
		return computerCount(foo);
	}
	
	/**
	 * Find the Computer with matching user-input rental number. 
	 * Calls constructComputers(manager, rentalNumber)
	 * then searches through its output for a Computer with the matching rental number.
	 * 
	 * Functions assumes that the connection manager has an active connection.
	 * */
	public Computer findComputer(ConnectionManager manager, String rentalNumber) {
		List<Computer> foo = this.constructComputers(manager, rentalNumber);
		for (int i = 0 ; i<foo.size(); i=i+1) {
			if (foo.get(i).getRentalNumber().equals(rentalNumber)) {
				return foo.get(i);
			}
		}
		return new Computer("", "", "", "", Date.valueOf(LocalDate.now()), false, Collections.emptyList(), "", "", 0);
	}
	
	/**
	 * Fetch a Delivery's associated RentalNumbers.
	 * 
	 * Function assumes connection in the parameter is active
	 * */
	/**
	public List<Computer> fetchComputers(ConnectionManager manager, int deliveryID) {
		List<Computer> parsed_data = new ArrayList<>();
		String query = String.format("FetchComputers\r\n%s", deliveryID);
		
		//query the information from database
		String foo = new String(manager.send(query));
		
		/** 
		 * split the response by newline, making a list of strings containing the information
		 * of a single computer's RentalNumber
		 * 
		List<String> bar = new ArrayList<String>(Arrays.asList(foo.split("::\n")));
		
		/**
		 * Add each RentalNumber in
		 * 
		List<String> foobar;
		for (int i=0; i < bar.size(); i = i+1) {
			foobar = new ArrayList<>(Arrays.asList(bar.get(i).split("\\s*::,\\s*")));
			if (foobar.size() == 1) {
				return new ArrayList<>();
			}
			parsed_data.add(new Computer(
					foobar.get(0), "", "", "", 
					Date.valueOf("2000-01-01"), Boolean.FALSE, Collections.emptyList(),
					"", "", 0));
		}
		return parsed_data;
	}
	*/
	
	/**
	 * Fetch a Delivery's associated RentalNumbers and these rental units' parts.
	 * 
	 * Function assumes connection in the parameter is active
	 * */
	public List<Computer> fetchCompleteComputers(ConnectionManager manager, int deliveryID) {
		List<Computer> parsed_data = new ArrayList<>();
		String query = String.format("FetchCompleteComputers\r\n%s", deliveryID);
		
		//query the information from database
		String foo = new String(manager.send(query));
		
		parsed_data = parseCompleteComputer(foo);
		return parsed_data;
	}
	
	 /** Fetch a Delivery's associated active RentalNumbers and these rental units' parts.
	 * 
	 * Function assumes connection in the parameter is active
	 * */
	public List<Computer> fetchActiveCompleteComputers(ConnectionManager manager, int deliveryID) {
		List<Computer> parsed_data = new ArrayList<>();
		String query = String.format("FetchActiveCompleteComputers\r\n%s", deliveryID);
		
		//query the information from database
		String foo = new String(manager.send(query));
		
		parsed_data = parseCompleteComputer(foo);
		return parsed_data;
	}
	
	/** Fetch a Delivery's associated pulled-out RentalNumbers and these rental units' parts.
	 * 
	 * Function assumes connection in the parameter is active
	 * */
	public List<Computer> fetchPulledComputers(ConnectionManager manager, int deliveryID) {
		List<Computer> parsed_data = new ArrayList<>();
		String query = String.format("FetchPulledComputers\r\n%s", deliveryID);
		
		//query the information from database
		String foo = new String(manager.send(query));
		
		parsed_data = parseCompleteComputer(foo);
		return parsed_data;
	}
	
	/** Fetch a Delivery's associated pending RentalNumbers and these rental units' parts.
	 * 
	 * Function assumes connection in the parameter is active
	 * */
	public List<Computer> fetchPendingComputers(ConnectionManager manager, int deliveryID, String formNumber) {
		List<Computer> parsed_data = new ArrayList<>();
		String query = String.format("ViewPullOutListUnits\r\n%s\r\n%s", deliveryID, formNumber);
		
		//query the information from database
		String foo = new String(manager.send(query));
		
		parsed_data = parseCompleteComputer(foo);
		return parsed_data;
	}
	
	/**
	 * Create the list of Computers that whose status will be On Hand during the specified date.
	 * 
	 * Function assumes manager is already connected to the server.
	 * */
	public List<Computer> makeProjection(ConnectionManager manager, LocalDate date) {
		List<Computer> parsed_data = new ArrayList<>();
		
		String query = String.format("Projection\r\n%s", date.toString());
		
		//query the information from database
		String foo = new String(manager.send(query));
				
		parsed_data = parseComputerNoParts(foo);
		return parsed_data;
	}
	
	/**
	 * Create the list of Parts by first requesting for the Parts 
	 * from the database controller, then converting the raw string output into
	 * a list of Parts objects.
	 * 
	 * The function assumes that the connection passed to it is active
	 * */
	public List<Parts> constructParts(ConnectionManager manager) {
		List<Parts> parsed_data = new ArrayList<>();
		
		//query the information from database
		String foo = new String(manager.send("ViewParts"));
		
		parsed_data = parsePart(foo);
		return parsed_data;
	}
	
	/**
	 * Create the list of Parts by first requesting for the Parts 
	 * from the database controller, then converting the raw string output into
	 * a list of Parts objects. Returns up to a specified number of entries at a time.
	 * 
	 * The function assumes that the connection passed to it is active
	 * */
	public List<Parts> constructParts(ConnectionManager manager, int offset, int limit) {
		List<Parts> parsed_data = new ArrayList<>();
		
		//query the information from database
		String foo = new String(manager.send("ViewParts"));
		
		parsed_data = parsePart(foo, offset, limit);
		return parsed_data;
	}
	
	/***
	 * Returns the total number of Parts currently in database.
	 */
	public int getPartsCount(ConnectionManager manager) {
		String foo = new String(manager.send("ViewParts"));
		return partsCount(foo);
	}
	
	/**
	 * Create the list of Accessories by first requesting for the Accessories 
	 * from the database controller, then converting the raw string output into
	 * a list of Accessories objects.
	 * 
	 * The function assumes that the connection passed to it is active
	 * */
	public List<Accessory> constructAccessories(ConnectionManager manager) {
		List<Accessory> parsed_data = new ArrayList<>();
		
		//query the information from database
		String foo = new String(manager.send("ViewAccessoryRentalNumbersByTypes"));
		
		parsed_data = parseAccessory(foo);
		
		return parsed_data;
	}
	
	/**
	 * Create the list of Accessories by first requesting for the Accessories matching the user-input filter
	 * from the database controller, then converting the raw string output into
	 * a list of Accessories objects.
	 * 
	 * The function assumes that the connection passed to it is active
	 * */
	public List<Accessory> constructAccessories(ConnectionManager manager, String rentalNumber) {
		List<Accessory> parsed_data = new ArrayList<>();
		String query = String.format("FilterAccessories\r\n%s", rentalNumber);
		
		//query the information from database
		String foo = new String(manager.send(query));
		
		parsed_data = parseAccessory(foo);
		
		return parsed_data;
	}
	
	/**
	 * Create the list of Accessories by first requesting for the Accessories matching the user-input filter
	 * from the database controller, then converting the raw string output into
	 * a list of Accessories objects. Returns up to a specified number of entries at a time.
	 * 
	 * The function assumes that the connection passed to it is active
	 * */
	public List<Accessory> constructAccessories(ConnectionManager manager, int offset, int limit) {
		List<Accessory> parsed_data = new ArrayList<>();
		
		//query the information from database
		String foo = new String(manager.send("ViewAccessoryRentalNumbersByTypes"));
		
		parsed_data = parseAccessory(foo, offset, limit);
		
		return parsed_data;
	}
	
	/***
	 * Returns the total number of Accessories currently in database.
	 */
	public int getAccessoriesCount(ConnectionManager manager) {
		String foo = new String(manager.send("ViewAccessoryRentalNumbersByTypes"));
		return accessoriesCount(foo);
	}
	
	/**
	 * Create the list of Accessories belonging to a user-input DeliveryID.
	 * 
	 * Manager must have an active connection before being passed to the function.
	 * */
	public List<Accessory> fetchDeliveryAccessories(ConnectionManager manager, int deliveryID) {
		List<Accessory> parsed_data = new ArrayList<>();
		String query = String.format("FetchDeliveryAccessories\r\n%s", deliveryID);
		
		//query the information from database
		String foo = new String(manager.send(query));

		parsed_data = parseAccessory(foo);
		
		return parsed_data;
	}
	
	/**
	 * Create the list of active Accessories belonging to a user-input DeliveryID.
	 * 
	 * Manager must have an active connection before being passed to the function.
	 * */
	public List<Accessory> fetchActiveDeliveryAccessories(ConnectionManager manager, int deliveryID) {
		List<Accessory> parsed_data = new ArrayList<>();
		String query = String.format("FetchActiveDeliveryAccessories\r\n%s", deliveryID);
		
		//query the information from database
		String foo = new String(manager.send(query));

		parsed_data = parseAccessory(foo);
		return parsed_data;
	}
	
	/**
	 * Create the list of returned Accessories belonging to a user-input DeliveryID.
	 * 
	 * Manager must have an active connection before being passed to the function.
	 * */
	public List<Accessory> fetchPulledDeliveryAccessories(ConnectionManager manager, int deliveryID) {
		List<Accessory> parsed_data = new ArrayList<>();
		String query = String.format("FetchPulledDeliveryAccessories\r\n%s", deliveryID);
		
		//query the information from database
		String foo = new String(manager.send(query));

		parsed_data = parseAccessory(foo);
		
		return parsed_data;
	}
	
	/**
	 * Create the list of returned Accessories belonging to a user-input DeliveryID.
	 * 
	 * Manager must have an active connection before being passed to the function.
	 * */
	public List<Accessory> fetchPendingDeliveryAccessories(ConnectionManager manager, int deliveryID, String formNumber) {
		List<Accessory> parsed_data = new ArrayList<>();
		String query = String.format("ViewPullOutListAcc\r\n%s\r\n%s", deliveryID, formNumber);
		
		//query the information from database
		String foo = new String(manager.send(query));

		parsed_data = parseAccessory(foo);
		
		return parsed_data;
	}
	
	/**
	 * Create the list of SmallAccessories.
	 * @param manager
	 * @return
	 */
	public List<SmallAccessory> constructSmallAccessories(ConnectionManager manager) {
		List<SmallAccessory> parsed_data = new ArrayList<>();
		
		//query the information from database
		String foo = new String(manager.send("ViewSmallAccessories"));
		
		parsed_data = parseSmallAccessory(foo);
		
		return parsed_data;
	}
	
	/**
	 * Create the list of SmallAccessories whose name contains the substring name.
	 * @param manager
	 * @param name
	 * @return
	 */
	public List<SmallAccessory> constructSmallAccessories(ConnectionManager manager, String name) {
		List<SmallAccessory> parsed_data = new ArrayList<>();
		String query = String.format("FilterSmallAccessories\r\n%s", name);
		
		//query the information from database
		String foo = new String(manager.send(query));
		
		parsed_data = parseSmallAccessory(foo);
		
		return parsed_data;
	}
	
	/**
	 * Create the list of SmallAccessories belonging to a Delivery. See the function in ObjectConstructor for more details.
	 * @param manager
	 * @param delvID
	 * @return
	 */
	public List<SmallAccessory> fetchDeliveryPeripherals(ConnectionManager manager, int delvID) {
		List<SmallAccessory> parsed_data = new ArrayList<>();
		String query = String.format("FetchDeliveryPeripherals\r\n%s", delvID);
		
		//query the information from database
		String foo = new String(manager.send(query));
		
		parsed_data = parsePeripheral(foo);
		
		return parsed_data;
	}
	
	/**
	 * Create the list of pending to return SmallAccessories belonging to a Pull-out form of a Delivery
	 * @param manager
	 * @param delvID
	 * @return
	 */
	public List<SmallAccessory> fetchPendingPeripherals(ConnectionManager manager, int delvID, String formNumber) {
		List<SmallAccessory> parsed_data = new ArrayList<>();
		String query = String.format("FetchPendingPeripherals\r\n%s\r\n%s", delvID, formNumber);
		
		//query the information from database
		String foo = new String(manager.send(query));
		
		parsed_data = parsePeripheral(foo);
		
		return parsed_data;
	}
	
	/**
	 * Create the list of Deliveries by first requesting for the Deliveries 
	 * from the database controller, then converting the raw string output into
	 * a list of Deliveries objects.
	 * 
	 * The function assumes that the connection passed to it is active
	 * */
	public List<Delivery> constructDeliveries(ConnectionManager manager) {
		List<Delivery> parsed_data = new ArrayList<>();
		
		//query the information from database
		String foo = new String(manager.send("ViewDeliveries"));
		
		parsed_data = parseDelivery(foo);
		
		return parsed_data;
	}
	
	public Delivery constructLastInsertedDelivery(ConnectionManager manager) {
		//query the information from database
		String foo = new String(manager.send("ViewDeliveries"));
		
		Delivery parsed_data = parseLastInsertedDelivery(foo);
		
		return parsed_data;
	}
	
	/**
	 * Create the list of Deliveries by first requesting for the Deliveries 
	 * that match the user-input Delivery ID 
	 * from the database controller, 
	 * then converting the raw string output into a list of Deliveries.
	 * 
	 * The function assumes that the connection passed to it is active
	 * */
	public List<Delivery> constructDeliveries(ConnectionManager manager, int delvID) {
		List<Delivery> parsed_data = new ArrayList<>();
		String query = String.format("FilterDeliveries\r\n%s", delvID);
		
		//query the information from database
		String foo = new String(manager.send(query));
		
		parsed_data = parseDelivery(foo);
		
		return parsed_data;
	}
	
	/**
	 * Create the list of Deliveries by first requesting for the Deliveries 
	 * that match the user-input name
	 * from the database controller, then converting the raw string output into
	 * a list of Deliveries.
	 * 
	 * The function assumes that the connection passed to it is active
	 * */
	public List<Delivery> constructDeliveries(ConnectionManager manager, String delvID) {
		List<Delivery> parsed_data = new ArrayList<>();
		String query = String.format("FilterDeliveriesName\r\n%s", delvID);
		
		//query the information from database
		String foo = new String(manager.send(query));
		
		parsed_data = parseDelivery(foo);
		
		return parsed_data;
	}

	/**
	 * Create the list of Deliveries by first requesting for the Deliveries 
	 * that match the user-input name
	 * from the database controller, then converting the raw string output into
	 * a list of Deliveries. Returns up to a specified number of entries a time.
	 * 
	 * The function assumes that the connection passed to it is active
	 * */
	public List<Delivery> constructDeliveries(ConnectionManager manager, int offset, int limit) {
		List<Delivery> parsed_data = new ArrayList<>();
		
		//query the information from database
		String foo = new String(manager.send("ViewDeliveries"));
		
		parsed_data = parseDelivery(foo, offset, limit);
		
		return parsed_data;
	}
	
	/***
	 * Returns the total number of Deliveries currently in database.
	 */
	public int getDeliveryCount(ConnectionManager manager) {
		//query the information from database
		String foo = new String(manager.send("ViewDeliveries"));
		
		return deliveryCount(foo);
	}
	
	/**
	 * Find the Delivery that corresponds exactly to the user-input deliveryID.
	 * 
	 * Function assumes manager's connection is active
	 * */
	public Delivery findDelivery(ConnectionManager manager, String delvID) {
		List<Delivery> parsed_data = new ArrayList<>();
		String query = String.format("FindDelivery\r\n%s", delvID);
		
		//query the information from database
		String foo = new String(manager.send(query));
		
		parsed_data = parseDeliveryNullOnError(foo);
		
		return parsed_data.get(0);
	}
	
	/**
	 * Create the list of Deliveries by first requesting for the Deliveries that match the user-input 
	 * client name from the database controller, then converting the raw string output into
	 * a list of Deliveries objects.
	 * 
	 * The function assumes that the connection passed to it is active
	 * */
	public List<Delivery> constructClientDeliveries(ConnectionManager manager, String clientName) {
		List<Delivery> parsed_data = new ArrayList<>();
		String query = String.format("ViewClientHistory\r\n%s", clientName);
		
		//query the information from database
		String foo = new String(manager.send(query));
		
		parsed_data = parseDelivery(foo);
		
		return parsed_data;
	}
	
	/**
	 * Create the list of Deliveries by first requesting for the Deliveries that match the user-input 
	 * year and month from the database controller, then converting the raw string output into
	 * a list of Deliveries objects.
	 * 
	 * The function assumes that the connection passed to it is active
	 * */
	public List<Delivery> constructMonthDeliveries(ConnectionManager manager, int month, int year) {
		List<Delivery> parsed_data = new ArrayList<>();
		String query = String.format("ViewMonthHistory\r\n%s\r\n%s", month, year);
		
		//query the information from database
		String foo = new String(manager.send(query));
		
		parsed_data = parseDelivery(foo);
		
		return parsed_data;
	}
	
	/**
	 * Create the list of active Deliveries by first requesting for the Deliveries 
	 * from the database controller, then converting the raw string output into
	 * a list of Deliveries objects.
	 * 
	 * The function assumes that the connection passed to it is active
	 * */
	public List<Delivery> constructActiveDeliveries(ConnectionManager manager) {
		List<Delivery> parsed_data = new ArrayList<>();
		
		//query the information from database
		String foo = new String(manager.send("ViewActiveDeliveries"));

		parsed_data = parseDelivery(foo);
		
		return parsed_data;
	}
	
	/**
	 * Create the list of extended Deliveries by first requesting for the Deliveries
	 * from the database controller, then converting the raw string output into
	 * a list of Deliveries objects.
	 * 
	 * The function assumes that the connection passed to it is active
	 * */
	public List<Delivery> constructExtendedDeliveries(ConnectionManager manager) {
		List<Delivery> parsed_data = new ArrayList<>();
		
		//query the information from database
		String foo = new String(manager.send("ViewAllExtended"));
		
		parsed_data = parseDelivery(foo);
		
		return parsed_data;
	}
	
	/**
	 * Create the list of Deliveries that are about to expire within a week.
	 * */
	public List<Delivery> constructUrgentDeliveries(ConnectionManager manager) {
		List<Delivery> parsed_data = new ArrayList<>();
		
		//query the information from database
		String foo = new String(manager.send("ViewUrgentPull"));
		
		parsed_data = parseDelivery(foo);
		
		return parsed_data;
	}
	
	/**
	 * Create the list of Deliveries that contain the rental number indicated by the user-input.
	 * 
	 * Function assumes connection manager is active.
	 * */
	public List<Delivery> constructRentalUnitHistory(ConnectionManager manager, String rental_number) {
		List<Delivery> parsed_data = new ArrayList<>();
		String query = String.format("ViewRentalUnitHistory\r\n%s", rental_number);
		
		//query the information from database
		String foo = new String(manager.send(query));
		
		parsed_data = parseDelivery(foo);
		
		return parsed_data;
	}
	
	 /** Finds the latest Delivery that contains the rental number indicated by the user-input.
	 * 
	 * Function assumes connection manager is active.
	 * */
	public Delivery findParentDelivery(ConnectionManager manager, String rental_number) {
		List<Delivery> parsed_data = new ArrayList<>();
		String query = String.format("ViewComputerParentDelivery\r\n%s", rental_number);
		
		//query the information from database
		String foo = new String(manager.send(query));
		
		parsed_data = parseDeliveryBlankOnError(foo);
		
		return parsed_data.get(0);
	}
	
	/**
	 * Attempts to find any parent Deliveries whose extended Delivery has the specified DeliveryID.
	 * @param manager
	 * @param delvID
	 * @return A list of DeliveryIDs whose ExtensionID matches delvID
	 */
	public List<Integer> findExtendedParentDelivery(ConnectionManager manager, int delvID) {
		List<Integer> parsed_data = new ArrayList<>();
		
		//query the information from database
		String foo = new String(manager.send("ViewDeliveries"));
		
		parsed_data = parseDeliveryExt(foo, delvID);
		
		return parsed_data;
	}

	/**
	 * Constructs the list of Clients by querying the database, then parsing
	 * the result of the query.
	 * 
	 * Assumes the ConnectionManager passed to the function is active.
	 * */
	public List<Client> constructClients(ConnectionManager manager) {
		List<Client> parsed_data = new ArrayList<>();
		
		//send query, retrieve query results
		String foo = new String(manager.send("ViewClients"));
		
		//split query results into client strings
		List<String> bar = new ArrayList<String>(Arrays.asList(foo.split("::\n")));
		
		List<String> foobar;
		//parse client strings into Clients
		for (int i=0; i < bar.size(); i = i + 1) {
			foobar = new ArrayList<>(Arrays.asList(bar.get(i).split("\\s*::,\\s*")));
			System.out.println("-- ConstructClients --");
			System.out.println(foobar);
			if (foobar.size() == 1) {
				return new ArrayList<>();
			}
			parsed_data.add(new Client(
					Integer.parseInt(foobar.get(0)),
					foobar.get(1),
					foobar.get(2),
					foobar.get(3),
					foobar.get(4)
					));
		}
		
		return parsed_data;
	}
	
	/**
	 * Constructs the list of Clients whose name matches the user-input string.
	 * 
	 * Assumes the ConnectionManager passed to the function is active.
	 * */
	public List<Client> filterClients(ConnectionManager manager, String name) {
		List<Client> parsed_data = new ArrayList<>();
		
		String query = String.format("FilterClients\r\n%s", name);
		
		//send query, retrieve query results
		String foo = new String(manager.send(query));
		
		//split query results into client strings
		List<String> bar = new ArrayList<String>(Arrays.asList(foo.split("::\n")));
		
		List<String> foobar;
		//parse client strings into Clients
		for (int i=0; i < bar.size(); i = i + 1) {
			foobar = new ArrayList<>(Arrays.asList(bar.get(i).split("\\s*::,\\s*")));
			if (foobar.size() == 1) {
				return new ArrayList<>();
			}
			parsed_data.add(new Client(
					Integer.parseInt(foobar.get(0)),
					foobar.get(1),
					foobar.get(2),
					foobar.get(3),
					foobar.get(4)
					));
		}
		
		return parsed_data;
	}

	/**
	 * Find the client whose name matches the user's input.
	 * 
	 * Assumes the ConnectionManager passed to the function is active.
	 * */
	public Client findClient(ConnectionManager manager, String client_name) {
		List<Client> parsed_data = new ArrayList<>();
		
		//send query, retrieve query results
		String foo = new String(manager.send("ViewClients"));
		
		//split query results into client strings
		List<String> bar = new ArrayList<String>(Arrays.asList(foo.split("::\n")));
		
		List<String> foobar;
		//parse client strings into Clients
		for (int i=0; i < bar.size(); i = i + 1) {
			foobar = new ArrayList<>(Arrays.asList(bar.get(i).split("\\s*::,\\s*")));
			if (foobar.size() == 1) {
				break;
			}
			parsed_data.add(new Client(
					Integer.parseInt(foobar.get(0)),
					foobar.get(1),
					foobar.get(2),
					foobar.get(3),
					foobar.get(4)
					));
		
		}
		//didn't find the client we're looking for
		for (int i = 0; i<parsed_data.size(); i=i+1) {
			if (parsed_data.get(i).getName().equals(client_name)) {
				return parsed_data.get(i);
			}
		}
		return (new Client(0, "", "", "", ""));
	}
	
	/**
	 * Constructs the list of Clients' names by querying the database, then parsing
	 * the result of the query, taking only the field that corresponds to the name.
	 * 
	 * Assumes the ConnectionManager passed to the function is active.
	 * */
	public List<String> constructClientNames(ConnectionManager manager) {
		List<String> parsed_data = new ArrayList<>();
		
		//send query, retrieve query results
		String foo = new String(manager.send("ViewClients"));
		
		//split query results into client strings
		List<String> bar = new ArrayList<String>(Arrays.asList(foo.split("::\n")));
		
		List<String> foobar;
		//parse client strings into Clients
		for (int i=0; i < bar.size(); i = i + 1) {
			foobar = new ArrayList<>(Arrays.asList(bar.get(i).split("\\s*::,\\s*")));
			if (foobar.size() == 1) {
				return new ArrayList<>();
			}
			parsed_data.add(foobar.get(1));
		}
		
		return parsed_data;
	}
	
	/**
	 * Gets the list of PullOutForms from the server.
	 * 
	 * ConnectionManager must be actively connected already when passed to the function.
	 * */
	public List<PullOutForm> constructPullOuts(ConnectionManager manager) {
		List<PullOutForm> parsed_data = new ArrayList<>();
		
		String foo = manager.send("ViewPullOuts");
		
		parsed_data = parsePullOutForm(foo);
		
		return parsed_data;
	}
	
	/**
	 * Gets the list of PullOutForms corresponding to a DeliveryID.
	 * 
	 * ConnectionManager must be actively connected already when passed to the function.
	 * */
	public List<PullOutForm> constructPullOuts(ConnectionManager manager, int deliveryID) {
		List<PullOutForm> parsed_data = new ArrayList<>();
		
		String query = String.format("FetchPullOut\r\n%s", deliveryID);
		String foo = manager.send(query);
		
		parsed_data = parsePullOutForm(foo);
		
		return parsed_data;
	}
	
	/**
	 * Gets the list of PullOutForms from the server. Returns up to a specified number of entries.
	 * 
	 * ConnectionManager must be actively connected already when passed to the function.
	 * */
	public List<PullOutForm> constructPullOuts(ConnectionManager manager, int offset, int limit) {
		List<PullOutForm> parsed_data = new ArrayList<>();
		
		String foo = manager.send("ViewPullOuts");
		
		parsed_data = parsePullOutForm(foo, offset, limit);
		
		return parsed_data;
	}
	
	/***
	 * Returns the total number of Pull-outs currently in the database.
	 */
	public int getPullOutCount(ConnectionManager manager) {
		String foo = manager.send("ViewPullOuts");
		return pullOutCount(foo);
	}
	
	/**
	 * Constructs the list of usernames and their roles.
	 * 
	 * ConnectionManager must already be active when passed to the function.
	 * */
	public List<User> constructUsers(ConnectionManager manager) {
		List<User> parsed_data = new ArrayList<>();
		
		String query = "UserManagement\r\nViewAllLogin";
		String foo = new String(manager.send(query));
		
		parsed_data = parseUserData(foo);
		
		return parsed_data;
	}
	
	/**
	 * Attempts to authenticate username and password. Returns false if it fails, and true if it succeeds.
	 * 
	 * ConnectionManager must already be active when passed to the function.
	 * */
	public Boolean findUser(ConnectionManager manager, String username, String password) {
		//List<User> parsed_data = new ArrayList<>();
		
		String query = new String();
		query = String.format("UserManagement\r\nViewLogin\r\n%s", username);
		
		String foo = new String(manager.send(query));
		
		List<String> bar = new ArrayList<String>(Arrays.asList(foo.split("::\n")));
		
		List<String> foobar;
		
		for (int i=0; i < bar.size(); i = i + 1) {
			foobar = new ArrayList<>(Arrays.asList(bar.get(i).split("\\s*::,\\s*")));
			if (foobar.size() == 1) {
				break;
			}
			
			else if (foobar.get(0).equals(username) && 
					get_SHA_512_SecurePassword(password, foobar.get(2)).equals(foobar.get(1))) {
				return true;
			}
			
		}
		
		return false;
	}
	
	/**
	 * Finds the Part that corresponds exactly to the user-input partID.
	 * 
	 * The function assumes that the connection passed to it is active
	 * */
	public List<Parts> fetchParts(ConnectionManager manager, int parts) {
		List<Parts> parsed_data = new ArrayList<>();
		
		String query = new String();
		query = String.format("FetchParts\r\n%s", parts);
		
		//query the information from database
		String foo = new String(manager.send(query));
			

		parsed_data = parsePart(foo);
		return parsed_data;
	}
	
	/**
	 * Finds the original Parts of a Computer.
	 * 
	 * Function assumes connection manager is already connected to the server.
	 * */
	public List<Parts> fetchOriginalParts(ConnectionManager manager, String rentalNumber) {
		List<Parts> parsed_data = new ArrayList<>();
		
		String query = new String();
		query = String.format("ViewOriginalSpecs\r\n%s", rentalNumber);
		
		//query the information from database
		String foo = new String(manager.send(query));
			

		parsed_data = parsePart(foo);
		return parsed_data;
	}
	
	/**
	 * Create the List of Parts that match the user-input filter.
	 * 
	 * The function assumes that the connection passed to it is active
	 * */
	public List<Parts> filterParts(ConnectionManager manager, String parts) {
		List<Parts> parsed_data = new ArrayList<>();
		
		//Do this for as many times as there are parts to process
		
		String query = new String();
		query = String.format("FilterParts\r\n%s", parts);
		
		//query the information from database
		String foo = new String(manager.send(query));
			

		parsed_data = parsePart(foo);	
		return parsed_data;
	}
	
	/**
	 * Create the List of Parts that match the user-input filter and whose status is "On Hand."
	 * 
	 * The function assumes that the connection passed to it is active
	 * */
	public List<Parts> filterAvailableParts(ConnectionManager manager, String parts) {
		List<Parts> parsed_data = new ArrayList<>();
		
		//Do this for as many times as there are parts to process
		
		String query = new String();
		query = String.format("FilterAvailableParts\r\n%s", parts);
		
		//query the information from database
		String foo = new String(manager.send(query));
			

		parsed_data = parsePart(foo);
		return parsed_data;
	}

	/**
	 * Creates the list of on-hand Computers.
	 * 
	 * The function assumes that the connection passed to it is active
	 * */
	public List<Computer> findAvailableComputers(ConnectionManager manager) {
		List<Computer> parsed_data = new ArrayList<>();
		
		//query the information from database
		String foo = new String(manager.send("FindOnHandComputers"));
		
		parsed_data = parseComputerNoParts(foo);
		return parsed_data;
	}
	
	/**
	 * Finds the Computer to which the input partID originally belonged
	 * @param manager
	 * @param x
	 * @return
	 */
	public Computer findOriginalComputer(ConnectionManager manager, int partID) {
		Computer parsed_data = new Computer("", "", "", "", null, false, null, "", "", 0f);
		
		//query the information from database
		String foo = new String(manager.send(String.format("FindOriginalComputer\r\n%d", partID)));
		
		try {
			parsed_data = parseComputerNoParts(foo).get(0);
		}
		catch (IndexOutOfBoundsException ex) {
			System.out.println("-- findOriginalComputer -- error");
			System.out.println(String.format("Input: %s", partID));
			System.out.println(String.format("Response: %s", foo));
			parsed_data = new Computer("error", "", "", "", null, null, null, foo, foo, partID);
		}
		return parsed_data;
	}
	
	/**
	 * Create the list of Parts whose status is "On Hand."
	 * 
	 * The function assumes that the connection passed to it is active
	 * */
	public List<Parts> findAvailableParts(ConnectionManager manager) {
		List<Parts> available_parts = new ArrayList<>();
		
		String foo = new String(manager.send("FindOnHandParts"));
		
		available_parts = parsePart(foo);
		
		return available_parts;
	}
	
	/***
	 * Create the list of Accessories whose status is "On Hand."
	 * 
	 * Connection manager must have an active connection before being passed to the function.
	 * */
	public List<Accessory> findAvailableAccessories(ConnectionManager manager) {
		List<Accessory> parsed_data = new ArrayList<>();
		
		String foo = new String(manager.send("FindOnHandAccessories"));
		
		parsed_data = parseAccessory(foo);
		
		return parsed_data;
	}
	
	/***
	 * Find the ListIDs corresponding to the Parts of a user-input rental number.
	 * 
	 * The manager passed to this function is assumed to be active
	 * */
	public List<Integer> findListIDs(ConnectionManager manager, String rentalNumber) {
		List<Integer> parsed_data = new ArrayList<>();
		
		String query = String.format("FindListIDs\r\n%s", rentalNumber);
		String foo = manager.send(query);
		
		/** 
		 * split the response by newline, making a list of strings containing the information
		 * of a single computer's RentalNumber
		 * */
		List<String> bar = new ArrayList<String>(Arrays.asList(foo.split("::\n")));
		/**
		 * Add each RentalNumber in
		 * */
		List<String> foobar;
		for (int i=0; i < bar.size(); i = i+1) {
			foobar = new ArrayList<>(Arrays.asList(bar.get(i).split("\\s*::,\\s*")));
			try {
				System.out.println(foobar.get(0));
				parsed_data.add(Integer.parseInt(foobar.get(0)));
			} catch (NumberFormatException ex) {
				break;
			}
		}
		System.out.println(parsed_data);
		return parsed_data;
	}
	
	/***
	 * Returns whether or not a rental unit of number specified exists.
	 * 
	 * ConnectionManager is assumed to be active already
	 * */
	public Boolean isComputerExisting(ConnectionManager manager, String rentalNumber) {
		List<String> existing_numbers = new ArrayList<>();
		
		//query the information from database
		String foo = new String(manager.send("ViewComputers"));
					
		/** 
		* split the response by newline, making a list of strings containing the information
		* of a single computer
		* */
		List<String> bar = new ArrayList<String>(Arrays.asList(foo.split("::\n")));
					
		/**
		* Currently, every Computer entry is separated by 4 Part entries, so there is a
		* need to skip over those entries
		* */
		List<String> foobar;
		//List<Integer> parts;
		for (int i=0; i < bar.size(); i = i + NUMBER_OF_PARTS+1) {
			/**
			 * Split the Computer string by comma
			* */
			foobar = new ArrayList<>(Arrays.asList(bar.get(i).split("\\s*::,\\s*")));
						
			existing_numbers.add(foobar.get(0));
		}
		
		if (existing_numbers.contains(rentalNumber)) {
			return Boolean.TRUE;
		}
		else {
			return Boolean.FALSE;
		}
	}
	
	/***
	 * Check if the Part with user-input partID exists.
	 * 
	 * The function assumes that the connection passed to it is active
	 * */
	public Boolean isPartExisting(ConnectionManager manager, int partID) {
		List<Integer> parsed_data = new ArrayList<>();
		
		//query the information from database
		String foo = new String(manager.send("ViewParts"));
		
		/** split the response by newline, making a list of strings containing the information
		 * of a single computer
		 * */
		List<String> bar = new ArrayList<String>(Arrays.asList(foo.split("::\n")));
		
		//Parse the string containing the details of the Part
		List<String> foobar;
		for (int i=0; i < bar.size(); i = i + 1) {
			foobar = new ArrayList<>(Arrays.asList(bar.get(i).split("\\s*::,\\s*")));
			parsed_data.add(Integer.parseInt(foobar.get(0)));
		}
		
		if (parsed_data.contains(partID)) {
			return Boolean.TRUE;
		}
		else {
			return Boolean.FALSE;
		}
	}
	
	/***
	 * Check if the Part with user-input rentalNumber exists.
	 * 
	 * The function assumes that the connection passed to it is active
	 * */
	public Boolean isAccessoryExisting(ConnectionManager manager, String rentalNumber) {
		List<String> parsed_data = new ArrayList<>();
		
		//query the information from database
		String foo = new String(manager.send("ViewAccessoryRentalNumbersByTypes"));
		
		/** split the response by newline, making a list of strings containing the information
		 * of a single computer
		 * */
		List<String> bar = new ArrayList<String>(Arrays.asList(foo.split("::\n")));
		
		//Parse the string containing the details of the Part
		List<String> foobar;
		for (int i=0; i < bar.size(); i = i + 1) {
			foobar = new ArrayList<>(Arrays.asList(bar.get(i).split("\\s*::,\\s*")));
			parsed_data.add(foobar.get(2));
		}
		
		if (parsed_data.contains(rentalNumber)) {
			return Boolean.TRUE;
		}
		else {
			return Boolean.FALSE;
		}
	}
	
	/**
	 * Check if the SmallAccessory with user-input name exists.
	 * @param manager
	 * @param name
	 * @return
	 */
	public Boolean isSmallAccessoryExisting(ConnectionManager manager, String name) {
		List<SmallAccessory> parsed_data = new ArrayList<>();
		
		//query the information from database
		String foo = new String(manager.send("ViewSmallAccessories"));
		
		parsed_data = parseSmallAccessory(foo);
		
		for (SmallAccessory i: parsed_data) {
			if (i.getName().equals(name)) {
				return true;
			}
		}
		
		return false;
	}
	
	/***
	 * Check if the Delivery with user-input DeliveryID exists.
	 * 
	 * The function assumes that the connection passed to it is active
	 * */
	public boolean isDeliveryExisting(ConnectionManager manager, String deliveryIDStr) {
		List<String> parsed_data = new ArrayList<>();
		
		String foo = new String(manager.send("ViewDeliveries"));
		
		List<String> bar = new ArrayList<String>(Arrays.asList(foo.split("::\n")));
		
		List<String> foobar;
		for (int i=0; i<bar.size(); i=i+1) {
			foobar = new ArrayList<>(Arrays.asList(bar.get(i).split("\\s*::,\\s*")));
			parsed_data.add(foobar.get(0));
		}
		
		if (parsed_data.contains(deliveryIDStr)) {
			return Boolean.TRUE;
		}
		else {return Boolean.FALSE;}
	}
	
	/***
	 * Check if the Client with user-input name exists.
	 * 
	 * Function assumes that manager's connection is active
	 * */
	public boolean isClientExisting(ConnectionManager manager, String name) {
		String clientid = manager.send(String.format("FindClient\r\n%s", name));
		
		if (clientid.isEmpty()) {
			return Boolean.FALSE;
		}
		else {
			return Boolean.TRUE;
		}
	}

	/***
	 * Check if the FormNumber of DeliveryID exists.
	 * 
	 * Function assumes that manager's connection is already active
	 * */
	public boolean isPOFormExisting(ConnectionManager manager, int deliveryID, String formNumber) {
		List<String> forms = new ArrayList<>();
		List<PullOutForm> parsed_data = new ArrayList<>();
		
		String foo = manager.send("ViewPullOuts");
		
		parsed_data = parsePullOutForm(foo);
		for (PullOutForm item : parsed_data) {
			forms.add(item.getFormNumber());
		}
		
		if (forms.contains(formNumber)) {
			return true;
		}
		else return false;
	}

	/**
	 * Parses a raw string separated by newlines into a list of Strings.
	 * @param manager
	 * @return
	 */
	public List<String> parseRawString(String foo) {
		List<String> bar = new ArrayList<String>(Arrays.asList(foo.split("\n")));
		return bar;
	}

	/**
	 * Assembles a server-readable message containing the desired query to be run and a list of parameters
	 * to insert into the query. An empty list may be passed if parameters are not needed.
	 * @param query
	 * @param parameters
	 * @return
	 */
	public String constructMessage(String query, List<String> parameters) {
		String result = "";
		
		result = result.concat(query.concat("\r\n"));

		if (!parameters.isEmpty()) {
			
			for (String input : parameters) {
				result = result.concat(input.concat("\r\n"));
			}
			
		}
		
		//System.out.println("-- constructMessage --");
		//System.out.println(result);
		//System.out.println("-- nothing follows --");
		return result;
	}
}
