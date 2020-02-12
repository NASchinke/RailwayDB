
import java.io.IOException;
import java.security.*;
import java.security.spec.InvalidKeySpecException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Arrays;
import java.util.InputMismatchException;
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

	public static void main(String[] args) throws Exception {

		MySQLAccess dao = new MySQLAccess();
		dao.readDataBase();

		register();
		
		// Beginning of case/switch switches
		Scanner console = new Scanner(System.in);
		int userCommand = Integer.MIN_VALUE;
		boolean flag = true;
		int passengerID = 0;
		int ticketID = 0;

		// dao.writeResultSet(resultSet);

		while (flag) {

			showMenu();

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

			case 1:

				resultSet = statement.executeQuery("select * from nd5152wx_railway.Train");
				// writeResultSet(resultSet);

				while (resultSet.next()) {
					// It is possible to get the columns via name
					// also possible to get the columns via the column number
					// which starts at 1
					// e.g. resultSet.getSTring(2);

					String trainName = resultSet.getString("trainName");

					System.out.println("Train Name : " + trainName);
				}

				break;

			case 2:

				// get the passenger's destination to be sure it is on the schedule
				// Need to read the whole line but not skip commands
				System.out.println(" What station are you currently at?");
				String currentStation = console.next();

				System.out.println("Where would you like to travel to?");
				String stationName = console.next();

				//create connection for case2
				MySQLAccess case2 = new MySQLAccess();

				// make sure we travel to this destination
				if (case2.readDataBase2(stationName)) {

					// need to do something in here for whether they need
					// to travel to another station to get on the route to
					// get to their destination or not.

					// IF true do this/else break to menu method
					System.out.println("I'd be happy to issue you a ticket to " + stationName
							+ " but first we must register the passenger.\n");
					// get the passenger's first name
					System.out.println("What is the passenger's first name?");
					String firstName = console.next();
					// get the passenger's last name
					System.out.println("What is the passenger's last name?");
					String lastName = console.next();
					System.out.println(
							"What is the passenger's phone number? We'll need this to relay any schedule changes.");
					String pPhoneNo = console.next();

					// Get the last passenger and ticket ID numbers
					int lastPassID = case2.getPassengerID(); 
					int lastTicketID = case2.getTicketID(); 
				
					//increment each ID by 1 for the next passenger registered
					passengerID = lastPassID + 1;
					ticketID = lastTicketID + 1;
				
					//Print out the ID numbers for the passenger
					System.out.println("The passengerID for " + firstName + " " + lastName + " is " + passengerID + ".");
					System.out.println("The ticket ID is " + ticketID);
					System.out.println("\nEnjoy your trip!\n");

					//insert passenger into the database 
					MySQLAccess case2_1 = new MySQLAccess();

					case2_1.readDataBase2_1(ticketID, firstName, lastName, pPhoneNo, passengerID);
					System.out.println("Passenger "+ firstName + " " + lastName + " has been added to the database.");
				} // for boolean
				else {
					System.out.println("I'm sorry, we do not travel to " + stationName + ".");
				}

				break;//end of case 2

			case 3:

				break;

			case 4:

				/*
				  
				 System.out.println("I can help you search for a Passenger.\n");
				 
				 System.out.println("What is the passenger's first name?"); String fName =
				  console.next(); String firstName = fName.toLowerCase(); 
				 
				  System.out.println("What is the passenger's last name?\n"); String lName =
				  console.next(); String lastName = lName.toLowerCase();
				 
				  
				  MySQLAccess case4 = new MySQLAccess();
				 
				  // make sure we travel to this destination
				  if(case4.readDataBase4(firstName, lastName)){
				 
				 System.out.println("Yes, I see that " + fName + " " + lName +
				  " is a passenger with us.");
				 
				  
				  }//for boolean else { System.out.println("I'm sorry, " + fName + " " + lName
				  + " is not a passenger with us."); } // ******insert passenger into the
				  database here************
				  
				  // Run a query to search for this passenger // SELECT * FROM Passenger WHERE
				 firstName LIKE "fName" // AND lastName LIKE "lName" break;
				   break;
				   */
				/*
				  case 5: System.out.println("List Passengers on Train");
				  
				  // SELECT * FROM Passenger break;
				  
				  case 6:
				 
				 // Query database /* Statement stmt = conn.createStatement(); ResultSet rs =
				  stmt.executeQuery("SELECT Lname FROM Customers WHERE Snum = 2001");
				  
				  //print Results while (rs.next()) { String lastName = rs.getString("Lname");
				  System.out.println(lastName + "\n"); }
				  
				
				break;

				*/
			case 7:
				System.out.println("List all trains");

				break;

			case 8:

				break;
			case 9:
				break;
			case 10:
				break;
			case 0:
				System.out.println("Thank you for using the Railway" + " System Database.");
				flag = false;
				System.exit(0);
				break;
			}// end switch

		} // end while
	}// end main

	// print a user menu
	private static void showMenu() {
		System.out.print("\n\n" + "1 - View Train Schedule\n" + "2 - Register Passenger/Purchase Ticket\n"
				+ "3 - Open\n" + "4 - Search for a Passenger\n" + "5 - List Passengers on a Train\n" + "6 -\n"
				+ "7 - List all trains \n" + "8 - \n" + "9 - \n" + "10 - \n" + "0 - Exit\n\n" + "Enter a command: ");
	}// end showMenu
	
	//put at bottom of RailwayClient
	public static void register() throws Exception {
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
		    dao.insertPassengerLogin(login, salt, getHash(password, salt));
		}
		
		private static void login() {
			Scanner console = new Scanner(System.in);
			String login;
			String password;
			
			System.out.print("\nPlease enter your username: ");
			login = console.next();
			System.out.print("\nPlease enter your password: ");
			password = console.next();
		}
		
		public static byte[] getHash(String password, byte[] salt) {
		    return hash(password.toCharArray(), salt);
		}
		
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
