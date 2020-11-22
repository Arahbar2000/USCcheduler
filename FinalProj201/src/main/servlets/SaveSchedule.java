package main.servlets;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import com.google.gson.JsonObject;
import com.google.gson.JsonArray;
import com.google.gson.JsonParser;
import main.*;
import java.util.ArrayList;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.DriverManager;
import java.io.PrintWriter;

/**
 * Servlet implementation class SaveSchedule
 */
@WebServlet(name = "SaveScheduleServlet", urlPatterns = "/api/saveschedule")
public class SaveSchedule extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		try {
            Class.forName("com.mysql.jdbc.Driver");
        }
        catch(ClassNotFoundException e) {

        }
		// TODO Auto-generated method stub
		JsonArray schedule = new JsonArray();
		User user = (User) request.getSession().getAttribute("user");
		response.setContentType("application/json");
		
		PrintWriter out = response.getWriter();
		
		try (Connection dbcon = DriverManager.getConnection(JDBCCredential.url, 
				JDBCCredential.username, JDBCCredential.password)) {
			
		    String query = "select courseId, department, courseNumber, title, " +
					"startTime, endTime, section, instructor, units, daysOfWeek, spots\n" +
					"from Takes t natural join Course c\n" +
					"where t.userId = ?;";
		    
			PreparedStatement statement = dbcon.prepareStatement(query);
			statement.setInt(1, user.id);
			ResultSet rs = statement.executeQuery();
			
			while(rs.next()) {
				
				JsonObject course = new JsonObject();
				course.addProperty("courseId", rs.getInt("courseId"));
				course.addProperty("department", rs.getString("department"));
				course.addProperty("courseNumber", rs.getInt("courseNumber"));
				course.addProperty("title", rs.getString("title"));
				course.addProperty("daysOfWeek", rs.getString("daysOfWeek"));
				course.addProperty("startTime", rs.getString("startTime"));
				course.addProperty("endTime", rs.getString("endTime"));
				course.addProperty("section", rs.getString("section"));
				course.addProperty("instructor", rs.getString("instructor"));
				course.addProperty("units", rs.getInt("units"));
				course.addProperty("spots", rs.getString("spots"));
				
				schedule.add(course);
			}
			
			
			out.println(schedule.toString());
			
		} catch (SQLException sqlE) {
			System.out.println(sqlE);
			System.out.println("SQL Exception");
		}
		
		
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		try {
            Class.forName("com.mysql.jdbc.Driver");
        }
        catch(ClassNotFoundException e) {

        }
		// TODO Auto-generated method stub
		
		User user = (User) request.getSession().getAttribute("user");
		
		String ids = request.getParameter("id");
		
		String[] c_ids = ids.split(",");
		
		try (Connection dbcon = DriverManager.getConnection(JDBCCredential.url, 
				JDBCCredential.username, JDBCCredential.password)) {
			
			//Delete previously saved schedule
			String query = "DELETE FROM Takes WHERE userId = ?;";
			PreparedStatement statement = dbcon.prepareStatement(query);
			statement.setInt(1, user.id);
			statement.executeUpdate();
			
			query = "INSERT INTO Takes (userId, courseId) VALUES(?, ?);";
			
			statement = dbcon.prepareStatement(query);
			statement.setInt(1,  user.id);
			
			//Save new schedule
			for(int i = 0; i < c_ids.length; i++) {
				statement.setInt(2, Integer.parseInt(c_ids[i]));
				statement.executeUpdate();
			}
			
			
					
		} catch (SQLException sqlE) {
			System.out.println(sqlE);
			System.out.println("SQL Exception");
		}
		
	}

}
