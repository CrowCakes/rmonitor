package com.example.rmonitor.classes;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;


public class ConnectionManager {
	
	private final static String address = "127.0.0.1";
	/*
	private final static String username = "user";
	
	private final static String password = "password";
	private final static String db = "pcrental";
	*/
	
	Socket s = null;
	BufferedReader input = null;
	PrintWriter out = null;
	
	/**
	 * Connects to the server at localhost port 9090. Returns 1 if the connection was
	 * successful, and 0 if it was not.
	 * */
	public int connect() {
		//connect to the server at localhost port 9090
		try {
			s = new Socket(address, 9090);
		} catch (UnknownHostException e) {
			e.printStackTrace();
			return 0;
		} catch (IOException e) {
			e.printStackTrace();
			return 0;
		}
		
		//initialize I/O streams
		try {
			input = new BufferedReader(new InputStreamReader(s.getInputStream()));
			out = new PrintWriter(s.getOutputStream(), true);
		} catch (IOException e) {
			e.printStackTrace();
			return 0;
		}
		return 1;
	}
	
	/**
	 * Disconnects from the server. Returns 1 if successful, 0 if it was not.
	 * */
	public int disconnect() {
		try {
			s.close();
		} catch (IOException e) {
			e.printStackTrace();
			return 0;
		}
		return 1;
	}
	
	/**
	 * Sends the input string to the server. 
	 * Returns the server's response or an error message if the client can't use the connection.
	 * @param query
	 * @return
	 */
	public String send(String query) {
		//out.println(String.valueOf(length));
		out.println(query);
		StringBuilder response = new StringBuilder();
		String foo = new String();
		try {
			while ((foo = input.readLine()) != null) {
				response.append(foo);
				response.append('\n');
			}
		} catch (IOException ex) {
			foo = "Err: " + ex;
			return foo;
		}
		return response.toString();
	}
}
