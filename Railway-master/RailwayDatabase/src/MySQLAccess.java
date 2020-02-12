
//import java.sql.Connection;
//import java.sql.DriverManager;
//import java.sql.ResultSet;
//import java.sql.SQLException;
//import java.sql.Statement;
import java.util.*;
import java.sql.*;
// java.util.InputMismatchException;
//import java.util.Scanner;

public class MySQLAccess {
	private Connection connect = null;
	private Statement statement = null;
	private ResultSet resultSet = null;
	private ResultSet resultSet2 = null;
	private ResultSet resultSet2_1 = null;
	private ResultSet resultSet2_3 = null;
	private ResultSet resultSet2_4 = null;
	private ResultSet resultSet4 = null;// for passenger search
	private ResultSet resultSet8 = null;// for destination search
	private Statement statement2 = null;
	private Statement statement2_2 = null;

	public void readDataBase() throws Exception {
		try {
			// This will load the MySQL driver, each DB has its own driver
			Class.forName("com.mysql.jdbc.Driver");
			// Setup the connection with the test DataBase - EVERYONE HAS ACCESS, PLEASE BE
			// CAREFUL!!
			// Obviously, if you were distributing this file, you would not include the
			// username and password. There are other ways...
			connect = DriverManager
					.getConnection("jdbc:mysql://mrbartucz.com/nd5152wx_railway?user=nd5152wx&password=TeDDy0620***");
			// connect =
			// DriverManager.getConnection("jdbc:mysql://localhost/CS485?user=CS485&password=WinonaState");

			// Below connects to the Railway database on the web server
			// connect =
			// DriverManager.getConnection("jdbc:mysql://localhost/Railway_System?user=root&password=TeDDy0620***");
			// if you want to connect to your local machine:
			// connect =
			// DriverManager.getConnection("jdbc:mysql://localhost/MyDatabaseName?user=MyUserName&password=MyPassword");

			// Statements allow to issue SQL queries to the database
			statement = connect.createStatement();
			// Result set get the result of the SQL query
			resultSet = statement.executeQuery("select * from nd5152wx_railway.Train");
			writeResultSet(resultSet);

		} catch (Exception e) {
			throw e;
		} finally {
			close();
		}

	}// end readDataBase

	public void writeResultSet(ResultSet resultSet) throws SQLException {
		// ResultSet is initially before the first data set (ResultSet resultSet)
		while (resultSet.next()) {
			// It is possible to get the columns via name
			// also possible to get the columns via the column number
			// which starts at 1
			// e.g. resultSet.getSTring(2);

			String trainName = resultSet.getString("trainName");
			/*
			 * String studentID = resultSet.getString("StudentID"); String firstName =
			 * resultSet.getString("FirstName"); String lastName =
			 * resultSet.getString("LastName"); String age = resultSet.getString("Age");
			 * System.out.println("StudentID: " + studentID);
			 * System.out.println("First Name: " + firstName);
			 * System.out.println("Last Name: " + lastName);
			 */
			System.out.println("Train Name : " + trainName);

		}
	}

	// You need to close the resultSet
	public void close() {
		try {
			if (resultSet != null) {
				resultSet.close();
			}

			if (statement != null) {
				statement.close();
			}

			if (connect != null) {
				connect.close();
			}
		} catch (Exception e) {

		}
	}// end close method
	

	
//The following is for CASE 2; searching for a particular station

	public boolean readDataBase2(String searched) throws Exception {

		try {
			// This will load the MySQL driver, each DB has its own driver
			Class.forName("com.mysql.jdbc.Driver");

			connect = DriverManager
					.getConnection("jdbc:mysql://mrbartucz.com/nd5152wx_railway?user=nd5152wx&password=TeDDy0620***");

			// Statements allow to issue SQL queries to the database
			statement = connect.createStatement();

			// ResultSet get the result of the SQL query.
			resultSet2 = statement.executeQuery("select * from nd5152wx_railway.Station WHERE stationName = '" + searched + "' ");
			if (resultSet2.next()) {
				do {
					// the next line only prints out the 3rd column. We need to print the schedule
					// for this station
					System.out.println(resultSet2.getString(3));

				} while (resultSet2.next());

				return true;
			}

			else {

				return false;

			} // end else

		} catch (Exception e) {
			throw e;
		} finally {
			close2();
		}

	}// end readDataBase2 method

	// You need to close the resultSet
	public void close2() {
		try {
			if (resultSet2 != null) {
				resultSet2.close();
			}

			if (statement != null) {
				statement.close();
			}

			if (connect != null) {
				connect.close();
			}
		} catch (Exception e) {

		}
	}// end close method
	
	

	// The following is for CASE 2_1; add passenger to database

	public void readDataBase2_1(int ticketID, String firstName, String lastName, String pPhoneNo, int passengerID)
			throws Exception {

		try {
			// This will load the MySQL driver, each DB has its own driver
			Class.forName("com.mysql.jdbc.Driver");

			connect = DriverManager
					.getConnection("jdbc:mysql://mrbartucz.com/nd5152wx_railway?user=nd5152wx&password=TeDDy0620***");

			// Statements allow to issue SQL queries to the database
			statement = connect.createStatement();

			// creating object of PreparedStatement class and passing parameter (?)
			PreparedStatement smt = connect.prepareStatement("insert into Passenger values(?,?,?,?,?)");

			// Bind the values
			smt.setInt(1, ticketID);
			smt.setString(2, firstName);
			smt.setString(3, lastName);
			smt.setString(4, pPhoneNo);
			smt.setInt(5, passengerID);

			// to execute update
			smt.executeUpdate();
	
			connect.close();

		} catch (Exception e) {
			throw e;
		} finally {
			close();
		}

		
	}// end readDataBase2_1 method*****************************
	
	
	

	//this method gets the highest registered passengerID and returns it
	@SuppressWarnings("finally")
	public int getPassengerID() throws Exception {
		int passID = 0; 
		try {
			// This will load the MySQL driver, each DB has its own driver
			Class.forName("com.mysql.jdbc.Driver");
		
			connect = DriverManager
					.getConnection("jdbc:mysql://mrbartucz.com/nd5152wx_railway?user=nd5152wx&password=TeDDy0620***");


			// Statements allow to issue SQL queries to the database
			statement = connect.createStatement();
			// Result set get the result of the SQL query
			resultSet2_3 = statement.executeQuery("select MAX(passengerID) AS passID from Passenger");
			while(resultSet2_3.next()) {
			 passID = resultSet2_3.getInt(1);
			}
	
		} catch (Exception e) {
			throw e;
		} finally {
			close();
		return passID;
			
		}

	}// end getPassengerID
	
	
	//This method gets the highest registered ticketID and returns it
	@SuppressWarnings("finally")
	public int getTicketID() throws Exception {
		int tktID = 0; 
		try {
			// This will load the MySQL driver, each DB has its own driver
			Class.forName("com.mysql.jdbc.Driver");
		
			connect = DriverManager
					.getConnection("jdbc:mysql://mrbartucz.com/nd5152wx_railway?user=nd5152wx&password=TeDDy0620***");


			// Statements allow to issue SQL queries to the database
			statement = connect.createStatement();
			// Result set get the result of the SQL query
			resultSet2_4 = statement.executeQuery("select MAX(ticketID) AS tktID from Passenger");
			while(resultSet2_4.next()) {
			tktID = resultSet2_4.getInt(1);
			}
		

		} catch (Exception e) {
			throw e;
		} finally {
			close();
		return tktID;
			
		}

	}//end getTicketID

	
	
	//put at bottom of MySQLAccess
	   public void insertPassengerLogin(String login, byte[] salt, byte[] hash) throws Exception {
			try {
				// This will load the MySQL driver, each DB has its own driver
				Class.forName("com.mysql.jdbc.Driver");

				connect = DriverManager
						.getConnection("jdbc:mysql://mrbartucz.com/nd5152wx_railway?user=vv9924lk&password=vv9924lk");
				
				String sql = "UPDATE Passenger SET login = ?, encPass = ?, salt = ? WHERE Passenger.passengerID = ?";
				
				PreparedStatement pst = connect.prepareStatement(sql);
				
				pst.setString(1, login);
				pst.setBytes(2, salt);
				pst.setBytes(3, hash);
				pst.setInt(4, 3);
				pst.executeUpdate();
				pst.close();
				
			} catch (Exception e) {
				
			}
		} //end insertPassengerLogin method

}
