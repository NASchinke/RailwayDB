

import java.io.IOException;
import java.security.*;
import java.security.spec.InvalidKeySpecException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.InputMismatchException;
import java.util.Random;
import java.util.Scanner;
import java.sql.*;
import javax.crypto.*;
import javax.crypto.spec.*;
import javax.swing.JOptionPane;
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author nd5152wx
 */
public class RailwayClient {
	private static Connection connect = null;
	private static Statement statement = null;
	private static ResultSet resultSet = null;
	private static String currentStation = "";
	private static String destinationStation = "";
	private static int TSID = 0;

	public static void main(String[] args) throws Exception {

		MySQLAccess dao = new MySQLAccess();
		dao.readDataBase();

		// Beginning of case/switch blocks
		Scanner console = new Scanner(System.in);
		int passengerID = 0;
		int ticketID = 0;
		boolean permissionFlag = true;
		int permission = Integer.MIN_VALUE;
		boolean passengerFlag = true;

		while (permissionFlag) {

			showPermissionMenu();

			// try/catch to prevent input crash
			try {
				permission = console.nextInt();

			} // end try
			catch (InputMismatchException e) {
				console.next();

			} // end mismatch catch
			catch (Exception e) {
				System.out.println("/n" + e.toString());

			} // end catch

			switch (permission) {

			case 1: // case 1 of permissions
				System.out.println("This is the Ticket Agent menu. This menu is password protected.");

				boolean TAflag = login();

				while (TAflag) {

					int userCommand = Integer.MIN_VALUE;
					showTAMenu();

					// try/catch to prevent input crash
					try {
						userCommand = console.nextInt();

					} // end try
					catch (InputMismatchException e) {
						console.next();

					} // end mismatch catch
					catch (Exception e) {
						System.out.println("/n" + e.toString());

					} // end catch

					switch (userCommand) {

					case 1:// case 1 of Ticket Agent
						System.out.println("Here are all of our routes.\n\n");
						dao.displayRoutes();
						break;

					case 2:// case 2 of Ticket Agent
						//******** WILL NOT WORK FOR A ROUTE THROUGH A HUB ***************
						int arriveID = 0;

						try {
							// get the passenger's current location
							System.out.println("What city are you currently in?");
							currentStation = console.next();// **This does not work for console.NextLine

							// search the city to see if we have a station there
							if (dao.stationSearch(currentStation)) {
								System.out.println("\nGreat! We have a train that services " + currentStation + ".");
							} else {
								System.out.println("\nI'm sorry, we do not have service to " + currentStation + ".");

								// Display cities that we service
								System.out.println("\nHere are the cities that we do service.\n");
								dao.viewStations();

								break;
							}
							console.nextLine();// consume remaining line

							// ask where they want to travel to
							System.out.println("\nWhere would you like to travel to?");
							destinationStation = console.next();

							// make sure we travel to this destination
							if (dao.stationSearch(destinationStation)) {
								System.out.println("Yes, we have service to " + destinationStation + ".\n");

							} else {
								// Display the cities that we service
								System.out.println("I'm sorry, we do not travel to " + destinationStation + ".\n");
								System.out.println("Here are the stations that we have service to.\n\n");
								dao.viewStations();// display our stations

								break;
							}

							// Get the last passenger ID number
							int lastPassID = dao.getMaxPassengerID();

							// increment this just once as the passenger remains the same
							passengerID = lastPassID + 1;

							// to hold ticket number
							int lastTicketID = 0;

							// first need to get current station ID from its name
							int currentStationID = dao.getStationID(currentStation);

							// get destination station ID by its name
							int destStationID = dao.getStationID(destinationStation);

							// Now get arrival station when the departure station is the current station
							arriveID = dao.getArrivalStationID(currentStationID);

							// Ask what type of ticket they would like
							System.out.println("What type of ticket would you like?");
							System.out.println("\nPlease enter 'FC' for First Class or 'C' for Coach");
							String tType = console.next();
							String ticketType = tType.toUpperCase();

							// Register the passenger
							System.out.println("Before we issue you a ticket to " + destinationStation
									+ " the passenger must get registered.\n");
							// get the passenger's first name
							System.out.println("What is the passenger's first name?");
							String firstName = console.next();
							// get the passenger's last name
							System.out.println("What is the passenger's last name?");
							String lastName = console.next();
							System.out.println(
									"What is the passenger's phone number? We'll need this to relay any schedule changes.");
							String pPhoneNo = console.next();

							// if the destination station is the next stop when leaving from
							// the current station, this is the only leg of the journey.
							do {

								if (destStationID == arriveID) {

									// get last issued ticket ID
									lastTicketID = dao.getMaxTicketID();
									// increment ticketID to next available
									ticketID = lastTicketID + 1;

									System.out.println("Great! It looks like " + destinationStation
											+ " is the next stop on this leg.\n");

									// get last issued seat number **Change to hold actual seats taken in an array
									int seatNum = dao.getMaxSeatNo();
									seatNum += 1;

									// Use train's capacity instead of (seatNum + 10) **if we have time

									// ask passenger what seat number they would like
									System.out.println("Here is the layout of the train.");
									System.out.println("\nWhat seat number would you like?");

									// offer last seat # thru capacity of train
									System.out.println("\nWe have seats " + seatNum + " through " + (seatNum + 10)
											+ " available.");

									int seatChoice = console.nextInt();

									// get the TSID Number based on departure and arrival IDs
									int TSID = dao.getTSID(arriveID); // ***Not working ***
									

									dao.issueTicket(ticketID, TSID, ticketType, seatChoice, passengerID);

								} else {

									// get the name of the arrival station on this leg of the trip
									String nextStation = dao.getStationName(arriveID);

									//add comment when train makes more than one stop
									String[] comments = new String[] { "Oh Geez", "I'm sorry", "Oh goodness",
											"Well for Pete's sake", "Oh dear", "Oh man", "Wow" };

									//Randomly select a comment
									Random rand = new Random();
									String quote = (comments[rand.nextInt(comments.length)]);

									//Print out comment
									System.out.println("\n" + quote + ", it looks like we'll have to route you through "
											+ nextStation + " first.");

									// get last issued ticket ID and increment by 1
									lastTicketID = dao.getMaxTicketID();
									ticketID = lastTicketID + 1;

									int lastSeat = dao.getMaxSeatNo();
									int seatNum = lastSeat + 1;

									// Use train's capacity instead of (seatNum + 10) **if we have time

									// ask passenger what seat number they would like
									System.out.println("\nHere is the layout of the train.");
									System.out.println("\nWhat seat number would you like?");
									// offer last highest seat # thru capacity of train
									System.out.println("\nWe have seats " + seatNum + " through " + (seatNum + 10)
											+ " available.");

									int seatChoice = console.nextInt();

									// get the TSID Number based on departure and arrival IDs
									int TSID = dao.getTSID(arriveID); 

									dao.issueTicket(ticketID, TSID, ticketType, seatChoice, passengerID);

									// arrival station of first leg becomes departure station of 2nd leg
									currentStationID = arriveID;

									// get new arrival station given the new departure station
									arriveID = dao.getArrivalStationID(currentStationID);

								}

							} while (destStationID != arriveID);

							// Get the last passenger and ticket ID numbers
							// lastPassID = dao.getMaxPassengerID();
							lastTicketID = dao.getMaxTicketID();
							ticketID = lastTicketID;

							// print out message

							registerUser(firstName, lastName, ticketID, pPhoneNo, passengerID);

							// print out ticket(s) **********Need TSID method to work********
							dao.displayTicket(passengerID);

							System.out.println("\nEnjoy your trip!\n");

						} // end try
						catch (InputMismatchException e) {
							console.next();

						} // end mismatch catch
						catch (Exception e) {
							System.out.println("/n" + e.toString());
						
							} finally {
						} // end catch
							
					
						break;// end of case 2

					case 3:// Display all stations
						System.out.println("\nHere is a list of the stations we service.\n");

						dao.viewStations();

						break;

					case 4:// Search for a passenger by first and last name
						System.out.println("I can help you search for a passenger.\n");

						System.out.println("What is the passenger's first name?");
						String fName = console.next();

						System.out.println("What is the passenger's last name?");
						String lName = console.next();

						if (dao.searchPassenger(fName, lName)) {
							System.out.println("\nYes, I see that " + fName + " " + lName + " is a passenger with us.");

						} else {
							System.out.println("\nI'm sorry, I don't see " + fName + " " + lName
									+ " registered as a passenger with us.");
						}

						break;

					case 5:// Display all passengers
						System.out.println("Here are all the passengers.\n\n");

						dao.displayPassengers();

						break;

					case 6:// Display all routes
						System.out.println("\nHere are all of our routes.\n");

						dao.displayRoutes();

						break;

					case 7:// Display all trains
						System.out.println("\nHere are all of our trains.\n");
						dao.displayTrains();

						break;

					case 8:// case 8 of Ticket Agent ************DON'T WORK****************
						System.out.println("Yes, I can look up your seat number.");
						Thread.sleep(500);
						System.out.println("What is your passenger ID?");
						int passID = console.nextInt();

						int seatNo = dao.getSeatNo(passID);
						if (seatNo == -1) {
							System.out.println(
									"I'm sorry, we do not have a record of a seat number for the passenger ID of "
											+ passID + ".");
						} else {

							System.out.println("Your seat number is " + seatNo + ".");
						}

						break;
						
					case 9:
						System.out.println("This are our arrival/departure times.\n");
						dao.routeSchedule();
						break;
						

					case 0:// case 0 of ticket agent
						System.out.println("Returning to main menu.");
						Thread.sleep(500);
						System.out.println("Thank you for using the Railway System Database.");
						TAflag = false;
						break;

					default:
						System.out.println("Please enter one of the displayed numbers.");
						break;
					}// end ticket agent switch
				} // end ticket agent while loop

				break;// end of case 1 (ticket agent)

			case 2:// Start of Passenger

				int userCommand = Integer.MIN_VALUE;

				System.out.println("This is the passenger menu.");

				while (passengerFlag) {
					// Display passenger menu
					passengerMenu();

					// try/catch to prevent input crash
					try {
						userCommand = console.nextInt();

					} // end try
					catch (InputMismatchException e) {
						console.next();

					} // end mismatch catch
					catch (Exception e) {
						System.out.println("/n" + e.toString());

					} // end catch

					switch (userCommand) {

					case 11:
						System.out.println("Here are all of our routes.\n\n");
						dao.displayRoutes();
						break;

					case 12:
						System.out.println("Here are all of our trains.\n\n");
						dao.displayTrains();
						break;

					case 13:
						System.out.println("Yes, I can look up your seat number.");
						System.out.println("\nWhat is your passenger ID?");
						int passID = console.nextInt();

						int seatNo = dao.getSeatNo(passID);
						if (seatNo == -1) {
							System.out.println(
									"I'm sorry, we do not have a record for a seat number for a passenger ID of "
											+ passID + ".");
						} else {
							System.out.println("Your seat number is " + seatNo + ".");

						}

						break;

					case 14:
						System.out.println("This are our arrival/departure times.\n");
						dao.routeSchedule();

						break;

					case 15:
						System.out.println("Here is a list of the stations we service.");
						Thread.sleep(500);
						dao.viewStations();

						break;

					case 0:// exit the passenger menu
						passengerFlag = false;
						break;
					default:
						System.out.println("Please enter one of the displayed numbers.");

						break;

					}// end passenger switch
				} // end passengerFlag

			case 0:// exit the program
				System.out.println("Thank you for using the Railway" + " System Database.");
				permissionFlag = false;
				System.exit(0);
				break;

			}// End permission switch
		} // end permission flag

	}// end main

	public static void showPermissionMenu() {
		System.out.println("\n\n 1 - Ticket Agent-Password protected.\n 2 - Passenger\n 0 - Exit\n\n Enter a command:");
	}

	public static void passengerMenu() {
		System.out.println("\n\n 11 - List all train routes.\n 12 - List all trains\n 13 - Look up a ticket number\n "
				+ "14 - View arrival/departure times\n 15 - List all stations\n 0 - Exit\n\n Please enter a command:");
	}

	// print a user menu
	private static void showTAMenu() {
		System.out.print("\n\n" + "1 - List all train routes\n" + "2 - Purchase ticket/Register passenger\n"
				+ "3 - List all stations\n" + "4 - Search for a Passenger\n" + "5 - List all passengers\n"
				+ "6 - List all the train routes\n" + "7 - List all trains \n" + "8 - Look up a seat number\n"
				+ "9 - View arrival/departure times\n" + "0 - Exit\n\n" + "Enter a command: ");
	}// end showMenu

	public static void registerUser(String firstN, String lastN, int ticketID, String phoneNo, int passengerID)
			throws Exception {
		Scanner console = new Scanner(System.in);
		String login;
		String password;

		System.out.print("\nEnter the login for your account: ");
		login = console.next();
		System.out.print("\nEnter the password for your account: ");
		password = console.next();

		SecureRandom rand = new SecureRandom();
		byte[] salt = new byte[32];
		rand.nextBytes(salt);

		MySQLAccess dao = new MySQLAccess();
		dao.insertPassenger(firstN, lastN, ticketID, phoneNo, passengerID, login, salt, hash(password.toCharArray(), salt));
	}

	private static boolean login() throws Exception {
		Scanner console = new Scanner(System.in);
		String login;
		String password;

		System.out.print("\nPlease enter your username: ");
		login = console.next();
		System.out.print("\nPlease enter your password: ");
		password = console.next();
		
		MySQLAccess dao = new MySQLAccess();
		
		byte[] salt = dao.getSalt(login);
		byte[] encPass = dao.getPassword(login);
		
		byte[] hashed = hash(password.toCharArray(), salt);
		
		for (int i = 0; i < encPass.length; i++) {
			if (encPass[i] != hashed[i]) {
				System.out.println("Incorrect username / password entered. Please try again.");
				return false;
			}
		}
		return true;
	}//end login

	public static byte[] hash(char[] password, byte[] salt) {
		PBEKeySpec spec = new PBEKeySpec(password, salt, 10000, 256);
		Arrays.fill(password, Character.MIN_VALUE);
		try {
			SecretKeyFactory skf = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
			return skf.generateSecret(spec).getEncoded();
		} catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
			throw new AssertionError("Error while hashing a password: " + e.getMessage(), e);
		} finally {
			spec.clearPassword();
		}
	}
}// end class



