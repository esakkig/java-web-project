package com.in28minutes.data.impl;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserSessionImpl {

	static Connection in28minutesConn = null;
	static PreparedStatement in28minutesPrepareStat = null;
 
	public static void main(String[] argv) {
 
		try {
			log("-------- Simple in28minutes Tutorial on how to make JDBC connection to MySQL DB locally on macOS ------------");
			makeJDBCConnection();
 
			log("\n---------- Adding user status to DB ----------");
			updateSessionStatus("user15","user15@in28minutes.com","Active");
		
 

			in28minutesPrepareStat.close();
			in28minutesConn.close(); // connection close
 
		} catch (SQLException e) {
 
			e.printStackTrace();
		}
	}
 
	private static void makeJDBCConnection() {
 
		try {
			Class.forName("com.mysql.jdbc.Driver");
			log("Congrats - Seems your MySQL JDBC Driver Registered!");
		} catch (ClassNotFoundException e) {
			log("Sorry, couldn't found JDBC driver. Make sure you have added JDBC Maven Dependency Correctly");
			e.printStackTrace();
			return;
		}
 
		try {
			// DriverManager: The basic service for managing a set of JDBC drivers.
			in28minutesConn = DriverManager.getConnection("jdbc:mysql://cdpdbinstance.c3jhviid3khq.us-east-1.rds.amazonaws.com:3306/cdpdevops", "cdpdevops", "Tata1234");
			if (in28minutesConn != null) {
				log("Connection Successful! Enjoy. Now it's time to push data");
			} else {
				log("Failed to make connection!");
			}
		} catch (SQLException e) {
			log("MySQL Connection Failed!");
			e.printStackTrace();
			return;
		}
 
	}
 
	public static void updateSessionStatus(String userid, String email, String status) {
		
		try {
			
			  
			
			   makeJDBCConnection();
			
				String getQueryStatement = "SELECT * FROM usersession where UserID = ?";
				 
				in28minutesPrepareStat = in28minutesConn.prepareStatement(getQueryStatement);
				in28minutesPrepareStat.setString(1, userid);
		
				// Execute the Query, and get a java ResultSet
				ResultSet resultSet = in28minutesPrepareStat.executeQuery();
				
				if (resultSet.next()) {
					        in28minutesPrepareStat.close();
							String updateQueryStatement = "UPDATE usersession SET InstanceID=?,status=? WHERE UserID=?"; 
							in28minutesPrepareStat = in28minutesConn.prepareStatement(updateQueryStatement);
							in28minutesPrepareStat.setString(1, retrieveInstanceId(status));
							in28minutesPrepareStat.setString(2, status);
							in28minutesPrepareStat.setString(3, userid);
							int rowsUpdated = in28minutesPrepareStat.executeUpdate();
							if (rowsUpdated > 0)
								System.out.println("An existing user was updated successfully!");
				}
				
				else{
					        in28minutesPrepareStat.close();
							String insertQueryStatement = "insert into usersession (UserID,email,InstanceID,status) values (?,?,?,?)";
				 
							in28minutesPrepareStat = in28minutesConn.prepareStatement(insertQueryStatement);
							in28minutesPrepareStat.setString(1, userid);
							in28minutesPrepareStat.setString(2, email);
							in28minutesPrepareStat.setString(3, retrieveInstanceId(status));
							in28minutesPrepareStat.setString(4, status);
				 
							// execute insert SQL statement
							in28minutesPrepareStat.executeUpdate();
							log(userid + " status updated");
						
			   }
				
				in28minutesPrepareStat.close();
				in28minutesConn.close(); // connection close		
		} catch (
				 
				SQLException e) {
					e.printStackTrace();
				}
	}
	
	private static String retrieveInstanceId(String status) {
		
		if(status.equalsIgnoreCase("INActive"))
			return null;
		
	    String EC2Id = null;
	    String inputLine;
	    try {
	    URL EC2MetaData = new URL("http://169.254.169.254/latest/meta-data/instance-id");
	    URLConnection EC2MD = EC2MetaData.openConnection();
	    BufferedReader in = new BufferedReader(new InputStreamReader(EC2MD.getInputStream()));
	    while ((inputLine = in.readLine()) != null) {
	        EC2Id = inputLine;
	    }
	    in.close();
	    } catch(Exception e) {}
	    return EC2Id;
	}

	// Simple log utility
	private static void log(String string) {
		System.out.println(string);
 
	}
}