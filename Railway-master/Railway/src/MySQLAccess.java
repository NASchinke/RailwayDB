
import java.util.*;
import java.sql.*;

public class MySQLAccess {
	private Connection connect = null;
	private Statement statement = null;
	private ResultSet resultSet = null;
	private ResultSet resultSet2_3 = null;
	private ResultSet resultSet2_4 = null;
	private ResultSet resultSet12 = null;;
	private ResultSet resultSet13 = null;

	public void readDataBase() throws Exception {
		try {
			// This will load the MySQL driver, each DB has its own driver
			Class.forName("com.mysql.jdbc.Driver");

			connect = DriverManager
					.getConnection("jdbc:mysql://mrbartucz.com/nd5152wx_railway?user=nd5152wx&password=TeDDy0620***");
		} catch (Exception e) {
			throw e;
		} finally {
			close();
		}

	}// end readDataBase

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

//searching for a particular station method
	public boolean stationSearch(String name) throws Exception {

		try {
			// This will load the MySQL driver, each DB has its own driver
			Class.forName("com.mysql.jdbc.Driver");

			connect = DriverManager
					.getConnection("jdbc:mysql://mrbartucz.com/nd5152wx_railway?user=nd5152wx&password=TeDDy0620***");

			String sql = "select * from nd5152wx_railway.Station WHERE Station.stationName = ?";
			PreparedStatement pst = connect.prepareStatement(sql);
			pst.setString(1, name);

			ResultSet resultSet2 = pst.executeQuery();
			if (resultSet2.next()) {
				return true;
			} else {
				return false;
			}

		} catch (Exception e) {
			throw e;
		} finally {
			close();
		}

	}// end stationSearch method

	// display all routes method
	public void displayRoutes() throws Exception {

		try {
			// This will load the MySQL driver, each DB has its own driver
			Class.forName("com.mysql.jdbc.Driver");

			connect = DriverManager
					.getConnection("jdbc:mysql://mrbartucz.com/nd5152wx_railway?user=nd5152wx&password=TeDDy0620***");
			String sql = "SELECT * FROM Route";
			PreparedStatement pst = connect.prepareStatement(sql);
			// Result set get the result of the SQL query
			resultSet = pst.executeQuery("select * from nd5152wx_railway.Route");
			writeResultSet(resultSet);

		} catch (Exception e) {
			throw e;
		} finally {
			close();
		}

	}// end displayRoutes

	public void writeResultSet(ResultSet resultSet) throws SQLException {
		// ResultSet is initially before the first data set (ResultSet resultSet)
		while (resultSet.next()) {

			int routeID = resultSet.getInt("routeID");
			String routeName = resultSet.getString("routeName");
			int departureStationID = resultSet.getInt("departureStationID");
			int arrivalStationID = resultSet.getInt("arrivalStationID");

			System.out.printf(
					"Route ID: %-5d Route Name: %-25s Departure Station ID: %-10s" + "Arrival Station ID: %-2d \n",
					routeID, routeName, departureStationID, arrivalStationID);
		}
		close();
	}

	// display all trains method
	public void displayTrains() throws Exception {

		try {
			// This will load the MySQL driver, each DB has its own driver
			Class.forName("com.mysql.jdbc.Driver");

			connect = DriverManager
					.getConnection("jdbc:mysql://mrbartucz.com/nd5152wx_railway?user=nd5152wx&password=TeDDy0620***");
			String sql = "select trainName,trainID,capacity from nd5152wx_railway.Train";
			PreparedStatement pst = connect.prepareStatement(sql);
			// Result set get the result of the SQL query
			resultSet = pst.executeQuery(sql);
			writeResultSet7(resultSet);

		} catch (Exception e) {
			throw e;
		} finally {
			close();
		}

	}// end displayTrains

	public void writeResultSet7(ResultSet resultSet) throws SQLException {
		// ResultSet is initially before the first data set (ResultSet resultSet)
		while (resultSet.next()) {

			String trainName = resultSet.getString("trainName");
			int routeID = resultSet.getInt("trainID");
			int capacity = resultSet.getInt("capacity");

			System.out.printf("Train: %-20s Route ID: %-7d Capacity: %-5d\n", trainName, routeID, capacity);
		}
		close();
	}

	// search for a passenger method
	public boolean searchPassenger(String firstName, String lastName) throws Exception {

		try {
			// This will load the MySQL driver, each DB has its own driver
			Class.forName("com.mysql.jdbc.Driver");

			connect = DriverManager
					.getConnection("jdbc:mysql://mrbartucz.com/nd5152wx_railway?user=nd5152wx&password=TeDDy0620***");
			String sql = "select * from nd5152wx_railway.Passenger WHERE Passenger.firstName = ? AND Passenger.lastName = ?";
			PreparedStatement pst = connect.prepareStatement(sql);
			pst.setString(1, firstName);
			pst.setString(2, lastName);
			ResultSet resultSet4 = pst.executeQuery();
			if (resultSet4.next()) {
				return true;
			} else {
				return false;
			}

		} catch (Exception e) {
			throw e;
		} finally {
			close();
		}

	}// end searchPassenger

	// search for a seat number method
	public int getSeatNo(int passengerID) throws Exception {
		int seatNo = 0;

		try {
			// This will load the MySQL driver, each DB has its own driver
			Class.forName("com.mysql.jdbc.Driver");

			connect = DriverManager
					.getConnection("jdbc:mysql://mrbartucz.com/nd5152wx_railway?user=nd5152wx&password=TeDDy0620***");
			String sql = "select seatNo from nd5152wx_railway.Ticket WHERE Ticket.passengerID = ? ";
			PreparedStatement pst = connect.prepareStatement(sql);
			pst.setInt(1, passengerID);

			ResultSet resultSet4 = pst.executeQuery();
			if (resultSet4.next()) {
				return seatNo = resultSet4.getInt(1);
			} else {
				return -1;
			}

		} catch (Exception e) {
			throw e;
		} finally {
			close();
		}

	}// end getSeatNo method

	// get a station's name from its ID number
	public String getStationName(int stationID) throws Exception {
		String statName = "";
		try {
			// This will load the MySQL driver, each DB has its own driver
			Class.forName("com.mysql.jdbc.Driver");

			connect = DriverManager
					.getConnection("jdbc:mysql://mrbartucz.com/nd5152wx_railway?user=nd5152wx&password=TeDDy0620***");
			String sql = "SELECT stationName FROM nd5152wx_railway.Station WHERE stationID = ? ";
			PreparedStatement pst = connect.prepareStatement(sql);
			pst.setInt(1, stationID);

			ResultSet resultSet4 = pst.executeQuery();
			if (resultSet4.next()) {
				return statName = resultSet4.getString(1);
			} else {
				return "No such station";
			}

		} catch (Exception e) {
			throw e;
		} finally {
			close();
		}

	}// end getStationName method

	// display route schedule method
	public void routeSchedule() throws Exception {

		try {
			// This will load the MySQL driver, each DB has its own driver
			Class.forName("com.mysql.jdbc.Driver");

			connect = DriverManager
					.getConnection("jdbc:mysql://mrbartucz.com/nd5152wx_railway?user=nd5152wx&password=TeDDy0620***");

			String sql = "SELECT Route.routeID,Route.routeName,Route.departureStationID, TrainSchedule.departureTime,"
					+ "Route.arrivalStationID, TrainSchedule.arrivalTime FROM TrainSchedule INNER JOIN Route ON "
					+ " TrainSchedule.routeID=Route.routeID";
			PreparedStatement pst = connect.prepareStatement(sql);

			resultSet = pst.executeQuery(sql);
			writeResultSet9(resultSet);

		} catch (Exception e) {
			throw e;
		} finally {
			close();
		}

	}// end routeSchedule

	public void writeResultSet9(ResultSet resultSet) throws SQLException {
		// ResultSet is initially before the first data set (ResultSet resultSet)
		while (resultSet.next()) {

			int routeID = resultSet.getInt("routeID");
			String routeName = resultSet.getString("routeName");
			int departureStationID = resultSet.getInt("departureStationID");
			Time departureTime = resultSet.getTime("departureTime");
			int arrivalStationID = resultSet.getInt("arrivalStationID");
			Time arrivalTime = resultSet.getTime("arrivalTime");

			System.out.printf(
					"Route ID: %-5d %-25s Departure Station ID: %-5d"
							+ "Departure Time: %-13s Arrival Station ID: %-10d Arrival Time: %-5s\n",
					routeID, routeName, departureStationID, departureTime, arrivalStationID, arrivalTime);
		}
		close();
	}

	// display tickets method 
	public void displayTicket(int passengerID) throws Exception {

		try {
			// This will load the MySQL driver, each DB has its own driver
			Class.forName("com.mysql.jdbc.Driver");

			connect = DriverManager
					.getConnection("jdbc:mysql://mrbartucz.com/nd5152wx_railway?user=nd5152wx&password=TeDDy0620***");
			String sql = "SELECT  Ticket.ticketID, Ticket.ticketType, " + "Ticket.seatNo, Route.routeName, "
					+ "TrainSchedule.departureTime, TrainSchedule.arrivalTime " // leave space before quotes
					+ "FROM  TrainSchedule " + "JOIN Ticket ON TrainSchedule.TSID = Ticket.TSID "
					+ "JOIN Route ON TrainSchedule.routeID = Route.routeID " + "WHERE Ticket.passengerID = ? ";
			PreparedStatement pst = connect.prepareStatement(sql);
			pst.setInt(1, passengerID);
			// Result set get the result of the SQL query
			resultSet = pst.executeQuery();
			writeResultSet19(resultSet);

		} catch (Exception e) {
			throw e;
		} finally {
			close();
		}

	}// end displayTicket

	public void writeResultSet19(ResultSet resultSet) throws SQLException {
		// ResultSet is initially before the first data set (ResultSet resultSet)
		while (resultSet.next()) {

			int ticketID = resultSet.getInt("ticketID");
			String ticketType = resultSet.getString("ticketType");
			int seatNo = resultSet.getInt("seatNo");
			String routeName = resultSet.getString("routeName");
			String departureTime = resultSet.getString("departureTime");
			String arrivalTime = resultSet.getString("arrivalTime");

			System.out.printf(
					"Ticket ID: %-2d Type: %-3s Seat Num: %-5d Route: %-20s Departure Time: %-12s Arrival Time %-10s\n",
					ticketID, ticketType, seatNo, routeName, departureTime, arrivalTime);
		}
		close();
	}

	// display all passengers method
	public void displayPassengers() throws Exception {

		try {
			// This will load the MySQL driver, each DB has its own driver
			Class.forName("com.mysql.jdbc.Driver");

			connect = DriverManager
					.getConnection("jdbc:mysql://mrbartucz.com/nd5152wx_railway?user=nd5152wx&password=TeDDy0620***");
			String sql = "select * from nd5152wx_railway.Passenger";
			PreparedStatement pst = connect.prepareStatement(sql);
			// Result set get the result of the SQL query
			resultSet = pst.executeQuery(sql);
			writeResultSet5(resultSet);

		} catch (Exception e) {
			throw e;
		} finally {
			close();
		}

	}// end displayPassengers

	public void writeResultSet5(ResultSet resultSet) throws SQLException {
		// ResultSet is initially before the first data set (ResultSet resultSet)
		while (resultSet.next()) {

			int ticketID = resultSet.getInt("ticketID");
			String firstName = resultSet.getString("firstName");
			String lastName = resultSet.getString("lastName");
			String phoneNo = resultSet.getString("phoneNo");
			int passengerID = resultSet.getInt("passengerID");

			System.out.printf("Ticket ID: %-5d fName: %-15s lName: %-15s Phone No: %-15s Passenger ID: %-5d\n",
					ticketID, firstName, lastName, phoneNo, passengerID);
		}
		close();
	}

	// get Train Station ID number.
	int getTSID(int arriveID) throws Exception { // removed , int departID

		int TSIDNo;

		try {
			// This will load the MySQL driver, each DB has its own driver
			Class.forName("com.mysql.jdbc.Driver");

			connect = DriverManager
					.getConnection("jdbc:mysql://mrbartucz.com/nd5152wx_railway?user=nd5152wx&password=TeDDy0620***");

			String sql = "SELECT TrainSchedule.TSID FROM TrainSchedule INNER JOIN Route ON "
					+ "TrainSchedule.routeID = Route.routeID " + "WHERE Route.arrivalStationID = ? "; // removed AND
																										// Route.departureStationID
																										// = ?";

			PreparedStatement pst = connect.prepareStatement(sql);
			// pst.setInt(2, departID);
			pst.setInt(1, arriveID);

			ResultSet resultSet9 = pst.executeQuery();
			if (resultSet9.next()) {
				return TSIDNo = resultSet9.getInt(1);

			} else {
				return -1;
			}

		} catch (Exception e) {
			throw e;
		} finally {
			close();
		}

	}// end getTSID_No method

	// view all stations
	public void viewStations() throws Exception {

		try {
			// This will load the MySQL driver, each DB has its own driver
			Class.forName("com.mysql.jdbc.Driver");

			connect = DriverManager
					.getConnection("jdbc:mysql://mrbartucz.com/nd5152wx_railway?user=nd5152wx&password=TeDDy0620***");
			String sql = "select * from nd5152wx_railway.Station";
			PreparedStatement pst = connect.prepareStatement(sql);
			// Result set get the result of the SQL query
			resultSet = pst.executeQuery();
			writeResultSet3(resultSet);

		} catch (Exception e) {
			throw e;
		} finally {
			close();
		}

	}// end displayRoutes

	public void writeResultSet3(ResultSet resultSet) throws SQLException {
		// ResultSet is initially before the first data set (ResultSet resultSet)
		while (resultSet.next()) {

			String stationPhoneNo = resultSet.getString("stationPhoneNo");
			int stationID = resultSet.getInt("stationID");
			String stationName = resultSet.getString("stationName");

			// Display stations
			System.out.printf("Station Phone No: %-15s 	" + "Station ID: %-15d Station Name: %-15s\n", stationPhoneNo,
					stationID, stationName);
		}
		close();

	}

	// issue ticket method
	public void issueTicket(int ticketID, int TSID, String ticketType, int seatNo, int passengerID) throws Exception {

		try {
			// This will load the MySQL driver, each DB has its own driver
			Class.forName("com.mysql.jdbc.Driver");

			connect = DriverManager
					.getConnection("jdbc:mysql://mrbartucz.com/nd5152wx_railway?user=vv9924lk&password=vv9924lk");

			String sql = "INSERT INTO Ticket VALUES (?,?,?,?,?)";

			PreparedStatement pst = connect.prepareStatement(sql);

			pst.setInt(1, ticketID);
			pst.setInt(2, TSID);
			pst.setString(3, ticketType);
			pst.setInt(4, seatNo);
			pst.setInt(5, passengerID);

			pst.executeUpdate();
			pst.close();

		} catch (Exception e) {
			throw e;
		} finally {
			close();
		}
	} // end insertPassengerLogin method

	// get stationID from the station name method
	@SuppressWarnings("finally")
	public int getStationID(String name) throws Exception {

		int statID = 0;
		try {
			// This will load the MySQL driver, each DB has its own driver
			Class.forName("com.mysql.jdbc.Driver");

			connect = DriverManager
					.getConnection("jdbc:mysql://mrbartucz.com/nd5152wx_railway?user=nd5152wx&password=TeDDy0620***");
			String sql = "SELECT Station.stationID from Station WHERE Station.stationName =  ?"; // added Station. on
																									// this line

			// Statements allow to issue SQL queries to the database
			PreparedStatement pst = connect.prepareStatement(sql);
			pst.setString(1, name);

			resultSet13 = pst.executeQuery();// ********Was passing in sql statement again with the ?

			// while (resultSet13.next()) {
			if (resultSet13.next()) {
				statID = resultSet13.getInt(1);

			} else {
				return statID;
			}

		} catch (Exception e) {

		} finally {

			close();// call closing method

			return statID;

		}

	}// end getStationID method

	// get the arrivalStationID when given a certain departureStationID. 
	// WON'T WORK for a HUB.
	@SuppressWarnings("finally")
	public int getArrivalStationID(int departID) throws Exception {

		int arriveID = 0;
		try {
			// This will load the MySQL driver, each DB has its own driver
			Class.forName("com.mysql.jdbc.Driver");

			connect = DriverManager
					.getConnection("jdbc:mysql://mrbartucz.com/nd5152wx_railway?user=nd5152wx&password=TeDDy0620***");
			String sql = "select arrivalStationID from Route" + " WHERE departureStationID =  ?";

			// Statements allow to issue SQL queries to the database
			PreparedStatement pst = connect.prepareStatement(sql);

			pst.setInt(1, departID);

			// Result set get the result of the SQL query
			resultSet12 = pst.executeQuery();

			while (resultSet12.next()) {
				arriveID = resultSet12.getInt(1);
			}

		} catch (Exception e) {
			throw e;
		} finally {
			close();// call closing method

			return arriveID;

		}

	}// end getArrivalStationID

	// get a train's capacity **NOT USED ********
	public int getTrainCapacity(int trainID) {

		int capacity = 0;
		try {
			// This will load the MySQL driver, each DB has its own driver
			Class.forName("com.mysql.jdbc.Driver");

			connect = DriverManager
					.getConnection("jdbc:mysql://mrbartucz.com/nd5152wx_railway?user=nd5152wx&password=TeDDy0620***");
			String sql = "SELECT capacity from Train" + " WHERE departureStationID =  ?";

			// Statements allow to issue SQL queries to the database
			PreparedStatement pst = connect.prepareStatement(sql);

			pst.setInt(1, trainID);

			// Result set get the result of the SQL query
			resultSet = pst.executeQuery();

			while (resultSet.next()) {
				capacity = resultSet.getInt(1);
			}

		} catch (Exception e) {
			throw e;
		} finally {
			close();// call closing method

			return capacity;

		}

	}// end getArrivalStationID

	// this method gets the highest registered passengerID and returns it
	@SuppressWarnings("finally")
	public int getMaxPassengerID() throws Exception {
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
			while (resultSet2_3.next()) {
				passID = resultSet2_3.getInt(1);
			}

		} catch (Exception e) {
			throw e;
		} finally {
			close();
			return passID;

		}

	}// end getMaxPassengerID

	// this method gets the highest registered seat number and returns it
	@SuppressWarnings("finally")
	public int getMaxSeatNo() throws Exception {
		int maxSeatNo = 0;
		try {
			// This will load the MySQL driver, each DB has its own driver
			Class.forName("com.mysql.jdbc.Driver");

			connect = DriverManager
					.getConnection("jdbc:mysql://mrbartucz.com/nd5152wx_railway?user=nd5152wx&password=TeDDy0620***");

			// Statements allow to issue SQL queries to the database
			statement = connect.createStatement();
			// Result set get the result of the SQL query
			ResultSet resultSet17 = statement.executeQuery("select MAX(seatNo) AS maxSeatNo from Ticket");
			while (resultSet17.next()) {
				maxSeatNo = resultSet17.getInt(1);
			}

		} catch (Exception e) {
			throw e;
		} finally {
			close();
			return maxSeatNo;

		}

	}// end getMaxPassengerID

	// This method gets the highest registered ticketID and returns it
	@SuppressWarnings("finally")
	public int getMaxTicketID() throws Exception {
		int tktID = 0;
		try {
			// This will load the MySQL driver, each DB has its own driver
			Class.forName("com.mysql.jdbc.Driver");

			connect = DriverManager
					.getConnection("jdbc:mysql://mrbartucz.com/nd5152wx_railway?user=nd5152wx&password=TeDDy0620***");

			// Statements allow to issue SQL queries to the database
			statement = connect.createStatement();
			// Result set get the result of the SQL query
			resultSet2_4 = statement.executeQuery("select MAX(ticketID) AS tktID from Ticket");
			while (resultSet2_4.next()) {
				tktID = resultSet2_4.getInt(1);
			}

		} catch (Exception e) {
			throw e;
		} finally {
			close();
			return tktID;

		}

	}

	// getTSIDnum method. This will retrieve the TSID num

	// insertPassenger method to put a passenger into the database
	public void insertPassenger(String firstN, String lastN, int ticketID, String phoneNo, int passengerID,
			String login, byte[] salt, byte[] hash) throws Exception {
		try {
			// This will load the MySQL driver, each DB has its own driver
			Class.forName("com.mysql.jdbc.Driver");

			connect = DriverManager
					.getConnection("jdbc:mysql://mrbartucz.com/nd5152wx_railway?user=vv9924lk&password=vv9924lk");

			String sql = "INSERT INTO Passenger VALUES (?,?,?,?,?,?,?,?)";

			PreparedStatement pst = connect.prepareStatement(sql);

			pst.setInt(1, ticketID);
			pst.setString(2, firstN);
			pst.setString(3, lastN);
			pst.setString(4, phoneNo);
			pst.setInt(5, passengerID);
			pst.setString(6, login);
			pst.setBytes(7, hash);
			pst.setBytes(8, salt);

			pst.executeUpdate();
			pst.close();

		} catch (Exception e) {
		}
	} // end insertPassengerLogin method

	public byte[] getSalt(String login) throws Exception {
		byte[] salt = new byte[32];
		Class.forName("com.mysql.jdbc.Driver");

		connect = DriverManager
				.getConnection("jdbc:mysql://mrbartucz.com/nd5152wx_railway?user=vv9924lk&password=vv9924lk");

		String sql = "SELECT salt FROM TicketAgent WHERE login = ?";

		PreparedStatement pst = connect.prepareStatement(sql);

		pst.setString(1, login);

		ResultSet set = pst.executeQuery();
		while (set.next()) {
			salt = set.getBytes(1);
		}

		return salt;
	}// end getSalt()

	public byte[] getPassword(String login) throws Exception {
		byte[] password = new byte[32];
		Class.forName("com.mysql.jdbc.Driver");

		connect = DriverManager
				.getConnection("jdbc:mysql://mrbartucz.com/nd5152wx_railway?user=vv9924lk&password=vv9924lk");

		String sql = "SELECT encPass FROM TicketAgent WHERE login = ?";

		PreparedStatement pst = connect.prepareStatement(sql);

		pst.setString(1, login);

		ResultSet set = pst.executeQuery();
		while (set.next()) {
			password = set.getBytes(1);
		}
		return password;
	}// end getHash()
}

/*
 * 
 * //information to save for future projects
 * 
 * // connect = // DriverManager.getConnection(
 * "jdbc:mysql://localhost/CS485?user=CS485&password=WinonaState");
 * 
 * // Below connects to the Railway database on the web server // connect = //
 * DriverManager.getConnection(
 * "jdbc:mysql://localhost/Railway_System?user=root&password=TeDDy0620***"); //
 * if you want to connect to your local machine: // connect = //
 * DriverManager.getConnection(
 * "jdbc:mysql://localhost/MyDatabaseName?user=MyUserName&password=MyPassword");
 */
