package main.servlets;

import com.google.gson.JsonObject;

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

		String email = request.getParameter("email");
		String password = request.getParameter("password");

		@SuppressWarnings("unchecked")
		HashMap<String, String> userMap = (HashMap<String, String>) request.getSession().getAttribute("user");

		int userID = Integer.parseInt(userMap.get("userId"));

		// parameter is user object
//		CreateSchedule mySch = new CreateSchedule(userID);

//		ArrayList<Schedule> genSchedulesList = mySch.getSchedule(15);

//		JsonObject jsonToReturn = new JsonObject();
//
//		for (Schedule s : genSchedulesList) {
//			for (Course c : s) {
//
//			}
//		}

	}

}
