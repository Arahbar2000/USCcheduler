package main;

import main.servlets.Pref;

import java.time.LocalTime;
import java.util.*;

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

	public User(int id, String firstName, String lastName, String email, String password) {
		this.id = id;
		this.firstName = firstName;
		this.lastName = lastName;
		this.email = email;
		this.password = password;
		this.prefs = getUserPref();
	}

	private Preferences getUserPref(){
		Preferences pref = null;
		try (Connection dbcon = DriverManager.getConnection(
				JDBCCredential.url, JDBCCredential.username, JDBCCredential.password)){
			String query = "select * " +
					"from Preferences " +
					"where userId = ?;";

			// Declare our statement
			PreparedStatement statement = dbcon.prepareStatement(query);

			statement.setInt(1, this.id);

			// Perform the query
			ResultSet rs = statement.executeQuery();

			while (rs.next()) {
				String courses_str = rs.getString("courseName");

				String startTime = rs.getString("startTime");
				String endTime = rs.getString("endTime");
				LocalTime startLocalTime = startTime.equals("TBA") ? null : LocalTime.parse(startTime);
				LocalTime endLocalTime = endTime.equals("TBA") ? null: LocalTime.parse(endTime);

				pref = new Preferences(
						Arrays.asList( courses_str.split(",")),
						startLocalTime,
						endLocalTime,
						rs.getInt("desiredUnits")
				);

				String extraCurriculumStr = rs.getString("extraCurriculum");
				// [{"startTime":08:00, "endTime":10:00}, ... ]
				List<Map<String, LocalTime>> extraCurriculum = new ArrayList<>();
				for (String times: Arrays.asList(extraCurriculumStr.split(","))){
					int splitPos = times.indexOf(' ');
					Map<String, LocalTime> m = new HashMap<>();
					m.put("startTime", LocalTime.parse(
							times.substring(1, splitPos))
					);
					m.put("endTime", LocalTime.parse(
							times.substring(splitPos + 1, times.length() -1))
					);
					extraCurriculum.add(m);
				}
				pref.setExtraCurriculum(extraCurriculum);
			}
			rs.close();
			statement.close();
		}
		catch (Exception e) {
			System.out.println("error");
			e.printStackTrace();
		}
		return pref;
	}


	public static void main(String args[]) {
		testAdd();
	}
	public static void testAdd() {
//		User test = new User("Joe", "Mo", "test@gmail.com", "supersecret");

	}

	// make preferences, push to table
	// @receive Post request from react somehow

	public void addUserToDB(User a) {

		Connection conn = null;
		Statement st = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		try {
			conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/cs201", "chris", "1313");
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
