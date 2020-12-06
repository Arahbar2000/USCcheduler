package main.servlets;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import main.Course;
import main.CreateSchedule;
import main.Preferences;
import main.Schedule;

import javax.annotation.Resource;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalTime;
import java.util.*;
import java.util.stream.Collectors;

@WebServlet(name = "GuestServlet", urlPatterns = "/api/guest")
public class Guest extends HttpServlet {

	private static final long serialVersionUID = 1L;

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
		System.out.println("STILL GENERATING");


		LocalTime startLocalTime = (startTime.equals("null") ? null : LocalTime.parse(startTime));
		LocalTime endLocalTime = (endTime.equals("null") ? null: LocalTime.parse(endTime));

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
		System.out.println(courseString);
		JsonObject resp_json = new JsonObject();
		List<Course> courses = CreateSchedule.getStringCourses(courseString);//splits string of courses into a list of course objects
		// 1 -> valid
		// 0 -> invalid (TBA)
        Map<Boolean, List<Course>> valid_invalid_courses
				= courses.stream().collect(Collectors.partitioningBy(c -> c.startTime != null & c.endTime != null));
		Gson gson = new Gson();

        JsonArray TBAs = new JsonArray();
        for (Course c: valid_invalid_courses.get(false)){
			JsonObject course = new JsonParser().parse(gson.toJson(c, Course.class)).getAsJsonObject();
			TBAs.add(course);
		}
		System.out.println("GETTING TBA COURSES" + TBAs.toString());
        resp_json.add("TBA", TBAs);

		CreateSchedule mySch = new CreateSchedule(valid_invalid_courses.get(true), pref); //uses Guest Constructor which doesnt require a user parameter
		ArrayList<Schedule> genSchedulesList = mySch.getSchedules(15);


		JsonArray packet = new JsonArray();
		for (Schedule s : genSchedulesList) {
			JsonArray courseList = new JsonArray();
			for (Course c : s.decidedClasses) {
				JsonObject course = new JsonParser().parse(gson.toJson(c, Course.class)).getAsJsonObject();
				courseList.add(course);
			}
			packet.add(courseList);
		}

		resp_json.add("schedule", packet);
		out.println(resp_json.toString());
	}

}
