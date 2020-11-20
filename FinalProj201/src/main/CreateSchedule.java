package main;


import jdk.nashorn.internal.scripts.JD;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.time.LocalTime;
import java.util.*;

public class CreateSchedule {

	User user;
	// user's classes in table `Schedule`
	public List<Course> all_courses = new ArrayList<>();
	// user's classes in table `Schedule`, but with TBA startime/endtime
	public List<Course> TBA_courses = new ArrayList<>();
	Preferences pref;

	public CreateSchedule(User user) {
		this.user = user;
		// assign all_courses & TBA_courses
		getUserSchedule();

		if (user != null)
			pref = user.prefs;
	}

	private void getUserSchedule() {
		try (Connection dbcon = DriverManager.getConnection(
				JDBCCredential.url, JDBCCredential.username, JDBCCredential.password)){
		    String query = "select userId, department, courseNumber, title, startTime, endTime, section, instructor, " +
					"units, daysOfWeek, spots\n" +
					"from Schedule s natural join Course c\n" +
					"where s.userId = ?;";

			// Declare our statement
			PreparedStatement statement = dbcon.prepareStatement(query);

			statement.setInt(1, this.user.id);

			// Perform the query
			ResultSet rs = statement.executeQuery();


			while (rs.next()) {
			    String startTime = rs.getString("startTime");
				String endTime = rs.getString("endTime");
				LocalTime startLocalTime = startTime.equals("TBA") ? null : LocalTime.parse(startTime);
				LocalTime endLocalTime = endTime.equals("TBA") ? null: LocalTime.parse(endTime);


				// login success
                Course c = new Course(rs.getString("department"),
						rs.getInt("courseNumber"),
						rs.getString("title"),
						rs.getString("daysOfWeek"),
                        startLocalTime,
                        endLocalTime,
                        rs.getString("section"),
						rs.getString("instructor"),
                        rs.getInt("units"),
						rs.getString("spots")
						);
                if (startLocalTime == null || endLocalTime == null)
                	TBA_courses.add(c);
                else
                	all_courses.add(c);
			}
			rs.close();
			statement.close();
		}
		catch (Exception e) {
			System.out.println("error");
			e.printStackTrace();
		}
	}


	// Checks if course meets user preferences
	private boolean meetsPreferences(Course c) {

		if(pref == null)
			return true;

		if(c.startTime.isBefore(pref.startTime))
			return false;

		if(c.endTime.isAfter(pref.endTime))
			return false;

		for(Map<String, LocalTime> other: pref.extraCurriculum) {
			// c is in interval of [s, e]
			LocalTime s = other.get("startTime");
			LocalTime e = other.get("endTime");

			if ((s.isBefore(c.endTime) && e.isAfter(c.endTime))
				|| (s.isBefore(c.startTime) && e.isAfter(c.startTime)))
				return false;
		}

		return true;
	}

	// return T if already exists a valid course of same type and meet user's pref
	private boolean multipleSections(Course c) {
		for(Course co: all_courses) {
			if(co.sameType(c) && meetsPreferences(co))
			    return true;
			//EDIT: only remove if other section does not violate, otherwise keep all
		}

		return false;

	}

	private boolean validSchedule(Schedule s) {

		if(pref == null)
			return true;

		String delim = "";
		ArrayList<String> coInSchedule = new ArrayList<String>();

		for(Course c: s.decidedClasses) {
			String name = c.department + delim + c.courseNumber;
			coInSchedule.add(name);
		}


		for(String name: pref.courseList) {
			if(!coInSchedule.contains(name))
				return false;
		}

		return true;
	}


	// return at most n schedules
    // 		each schedule contains valid courses combination (eg. lec + dis + qz)
	//		also includes TBA sections
	public ArrayList<Schedule> getSchedules(int n) {

		all_courses.removeIf(c -> (!meetsPreferences(c) && multipleSections(c)));
		// now every interchangeable courses are removed

		ArrayList<Schedule> schedules = new ArrayList<Schedule>();
		int i = 0;
		while(i++ < n || schedules.isEmpty()) {

			boolean add = true;
			Schedule sched = makeSchedule();
			for(Schedule s: schedules) {
				if(s.equals(sched))
					add = false;
			}

			if(add && validSchedule(sched)) {
				sched.decidedClasses.addAll(TBA_courses);
				schedules.add(sched);
			}
		}
		return schedules;
	}

	//creates 1 schedule
	private Schedule makeSchedule() {

		Schedule schedule = new Schedule();
		// introduce randomness
		Collections.shuffle(all_courses);

		for(Course c: all_courses) {

			if(!conflict(c, schedule) && !schedule.typeInSchedule(c))
				schedule.addCourse(c);
		}

		// cleanup, remove classes that are in schedule but have no lec section
		schedule.decidedClasses.removeIf(c -> (!c.sectionType.equals("Lec")
				&& !verifyLecture(c, schedule)));
		return schedule;
	}


	//Checks if course has conflict with already given schedule
	public boolean conflict(Course c, Schedule s) {

		ArrayList<Course> decided = s.decidedClasses;
		for(Course other: decided) {
			if(sameTime(c, other))
				return true;
		}

		return false;

	}


	//Checks if classes are on same time if they are on same day
	private boolean sameTime(Course c, Course other) {

		if(sameDay(c, other)) {

			if(c.startTime.equals(other.startTime))
				return true;

			return c.startTime.isBefore(other.endTime) && other.startTime.isBefore(c.endTime);

		}
		return false;
	}


	//Checks if classes are on same day
	private boolean sameDay(Course c, Course other) {

		for(int i = 0; i < 5; i++) {
			if(c.days[i] && other.days[i])
				return true;
		}

		return false;
	}

	private boolean verifyLecture(Course co, Schedule s) {

		for(Course c: s.decidedClasses) {
			if(c.department.equals(co.department) && c.courseNumber == co.courseNumber
					&& c.sectionType.equals("Lec"))
				return true;
		}

		return false;

	}


}
