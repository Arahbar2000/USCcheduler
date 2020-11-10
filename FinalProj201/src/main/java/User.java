import java.util.ArrayList;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class User {
	public String firstName;
	public String lastName;
	public int id; //should be auto generated ? or static counter
	public String email;
	public String password;
	public ArrayList<Integer> friends;
	public ArrayList<Schedule> currentSchedule;
	public Preferences prefs;

	public User(String firstName, String lastName, String email, String password) {
		this.firstName = firstName;
		this.lastName = lastName;
		this.email = email;
		this.password = password;

		addUserToDB(this);
	}

	public static void main(String args[]) {
		testAdd();
	}
	public static void testAdd() {
		User test = new User("Joe", "Mo", "test@gmail.com", "supersecret");

	}

	// make preferences, push to table
	// @receive Post request from react somehow

	public void addUserToDB(User a) {

		Connection conn = null;
		Statement st = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		try {
			conn = DriverManager.getConnection("jdbc:mysql://localhost:33061/cs201?user=root&password=secret");
			st = conn.createStatement();


			String insertString = "INSERT INTO Users(firstName, lastName, email, password) values ('" +
					a.firstName + "','" + a.lastName + "','" + a.email + "','" + a.password+ "')";

			st.execute(insertString);

			System.out.println("Done, added " + a.firstName + a.lastName + "to db");

		} catch (SQLException sqle) {
			System.out.println("Database Error: " + sqle.getMessage());
		} finally {
			try {
				if (rs != null) {
					rs.close();
				}
				if (st != null) {
					st.close();
				}
				if (ps != null) {
					ps.close();
				}
				if (conn != null) {
					conn.close();
				}
			} catch (SQLException sqle) {
				System.out.println("sqle: " + sqle.getMessage());
			}

		}

	}

}