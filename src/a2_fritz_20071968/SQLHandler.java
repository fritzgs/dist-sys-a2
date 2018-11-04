package a2_fritz_20071968;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Properties;

public class SQLHandler {

	/** The name of the MySQL account to use (or empty for anonymous) */
	private final String userName = "root";

	/** The password for the MySQL account (or empty for anonymous) */
	private final String password = "password";

	/** The name of the computer running MySQL */
	private final String serverName = "localhost";

	/** The port of the MySQL server (default is 3306) */
	private final int portNumber = 3306;

	/**
	 * The name of the database we are testing with (this default is installed with
	 * MySQL)
	 */
	private final String dbName = "Assignment2";

	// ArrayList of all the columns data
	private ArrayList<String> nameArr, deptArr, managerArr, locationArr, employee;
	private ArrayList<Integer> idArr, manIdArr;

	/**
	 * make a connection to the mysql database table
	 **/
	public Connection getConnection() throws SQLException {
		Connection conn = null;
		Properties connectionProps = new Properties();
		connectionProps.put("user", this.userName);
		connectionProps.put("password", this.password);

		conn = DriverManager.getConnection(
				"jdbc:mysql://" + this.serverName + ":" + this.portNumber + "/" + this.dbName, connectionProps);

		return conn;
	}

	public Boolean studentNoExist(Connection con, String stdNo, String password) {
		String query = "SELECT * FROM Students WHERE StudentNo=" + stdNo;
		Statement st = null;
		try {
			st = con.createStatement();
			ResultSet res = st.executeQuery(query);
			res.next();
			if (res.getString(1).equals(stdNo) && res.getString(4).equals(password)) {
				return true;
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	public String getFirstName(Connection con, String stdNo) {
		String query = "SELECT * FROM Students WHERE StudentNo=" + stdNo;
		Statement st = null;
		try {
			st = con.createStatement();
			ResultSet res = st.executeQuery(query);
			res.next();
			return res.getString(2);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public String getLastName(Connection con, String stdNo) {
		String query = "SELECT * FROM Students WHERE StudentNo=" + stdNo;
		Statement st = null;
		try {
			st = con.createStatement();
			ResultSet res = st.executeQuery(query);
			res.next();
			return res.getString(3);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
}
