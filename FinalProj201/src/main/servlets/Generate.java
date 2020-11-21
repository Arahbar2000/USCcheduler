package main.servlets;

import main.User;
import main.CreateSchedule;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonArray;
import com.google.gson.JsonParser;

import main.Course;
import main.CreateSchedule;
import main.Schedule;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.DataSource;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

@WebServlet(name = "GenerateServlet", urlPatterns = "/api/generate")
public class Generate extends HttpServlet {

	private static final long serialVersionUID = 1L;

	@Resource(name = "jdbc/cs201")
	private DataSource dataSource;

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {

		response.setContentType("application/json"); // Response mime type

		// write to response
		PrintWriter out = response.getWriter();
		User user = (User) request.getSession().getAttribute("user");

		CreateSchedule mySch = new CreateSchedule(user);

		ArrayList<Schedule> genSchedulesList = mySch.getSchedules(15);
		Gson gson = new Gson();

		JsonArray packet = new JsonArray();
		for (Schedule s : genSchedulesList) {
			JsonArray courseList = new JsonArray();
			for (Course c : s.decidedClasses) {
				
				JsonObject course = new JsonParser().parse(gson.toJson(c, Course.class)).getAsJsonObject();
				courseList.add(course);
			}
			packet.add(courseList);
		}
		
		out.println(packet.toString());
		out.close();
		
	}

}
