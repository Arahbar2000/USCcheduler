package main.servlets;

import main.*;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonArray;
import com.google.gson.JsonParser;

import main.CreateSchedule;
import main.Preferences;

import javax.servlet.ServletException;
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
import java.time.LocalTime;
import java.util.*;

@WebServlet(name = "GuestServlet", urlPatterns = "/api/guest")
public class Guest extends HttpServlet {

	private static final long serialVersionUID = 1L;

	@Resource(name = "jdbc/cs201")
	private DataSource dataSource;

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
		response.setContentType("application/json"); // Response mime type
		// write to response
		System.out.println("GENERATING SCHEDULES");
		PrintWriter out = response.getWriter();

		String courseString = request.getParameter("courses");//ie. "CSCI270,CSCI201"

		
		String startTime = request.getParameter("startTime");
		String endTime = request.getParameter("endTime");
		// eg. [08:00 09:00], [10:00,11:00]
		String extraCurriculumStr = request.getParameter("extraCurriculum");
		String desiredUnits = request.getParameter("desiredUnits");
		System.out.println("STILL GENERATING");

		LocalTime startLocalTime;
		LocalTime endLocalTime;

		if (startTime .equals("null")) {
			System.out.println("HELLO");
			startLocalTime = null;
		}
		else {
			startLocalTime = LocalTime.parse(startTime);
		}
		if(endTime.equals("null")) {
			System.out.println("HELLO");
			endLocalTime = null;
		}
		else {
			endLocalTime = LocalTime.parse(endTime);
		}
		// LocalTime startLocalTime = (startTime == null ? null : LocalTime.parse(startTime));
		// LocalTime endLocalTime = (endTime == null ? null: LocalTime.parse(endTime));

		Preferences pref = new Preferences(//create preferences object to feed into guest CreateSchedule constructor
				// empty list if course_str is null
				startLocalTime,
				endLocalTime,
				18
		);

		//split extra curricullars string and set to pref.extraCurriculum
		List<Map<String, LocalTime>> extraCurriculum = new ArrayList<>();
		if (!extraCurriculumStr.equals("null")){
			// [{"startTime":08:00, "endTime":10:00}, ... ]
			for (String times: Arrays.asList(extraCurriculumStr.split(",\\s*"))){
				int splitPos = times.indexOf(' ');
				Map<String, LocalTime> m = new HashMap<>();
				System.out.println(times.substring(2, splitPos));
				m.put("startTime", LocalTime.parse(
						times.substring(2, splitPos))
				);
				System.out.println(times.substring(splitPos + 1, times.length() -2));
				m.put("endTime", LocalTime.parse(
						times.substring(splitPos + 1, times.length() -2))
				);
				extraCurriculum.add(m);
			}
		}
		pref.setExtraCurriculum(extraCurriculum);
		System.out.println("HELLO");
		System.out.println(courseString);
		List<Course> courses = CreateSchedule.getStringCourses(courseString);//splits string of courses into a list of course objects
		CreateSchedule mySch = new CreateSchedule(courses, pref); //uses Guest Constructor which doesnt require a user parameter
		ArrayList<Schedule> genSchedulesList = mySch.getSchedules(25);
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

	}

}
