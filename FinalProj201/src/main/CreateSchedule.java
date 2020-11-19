package main;

import javax.annotation.Resource;
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public class CreateSchedule {

	User user;
	// user's preference courses
	List<String> courses;
	// user's classes in table `Schedule`
	public List<Course> all_courses;
	Preferences pref;

	public CreateSchedule(User user) {
		this.user = user;
		all_courses = getUserSchedule(user);

		if(user != null) {
			courses = user.prefs.courseList;
			pref = user.prefs;
		}
	}

	private List<Course> getUserSchedule(User user) {
		List<Course> courses = new LinkedList<>();
		try (Connection dbcon = DriverManager.getConnection("jdbc:mysql://localhost:3306/cs201", "chris", "1313")){
		    String query = "select userId, department, courseNumber, title, startTime, endTime, section, instructor, " +
					"units, daysOfWeek, spots\n" +
					"from Schedule s natural join Course c\n" +
					"where s.userId = ?;";

			// Declare our statement
			PreparedStatement statement = dbcon.prepareStatement(query);

			statement.setInt(1, user.id);

			// Perform the query
			ResultSet rs = statement.executeQuery();

			System.out.println("getting courses");

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
				System.out.println(c);

                courses.add(c);
			}
			rs.close();
			statement.close();
		}
		catch (Exception e) {
			System.out.println("error");
			e.printStackTrace();
		}
		return courses;
	}


	public ArrayList<Schedule> getSchedules(int n) {

		// remove courses that are 1. not in user's preference
		// 					   AND 2. already exist a section of same type that meet user's pref
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
				schedules.add(sched);

			}

		}
		return schedules;
	}

	//creates 1 schedule
	private Schedule makeSchedule() {

		Schedule schedule = new Schedule();
		Collections.shuffle(all_courses);

		for(Course c: all_courses) {

			if(!conflict(c, schedule) && !schedule.typeInSchedule(c))
				schedule.addCourse(c);
		}

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

			if(c.startTime.isBefore(other.endTime) && other.startTime.isBefore(c.endTime))
				return true;

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


	// Checks if course meets user preferences
	private boolean meetsPreferences(Course c) {

		if(pref == null)
			return true;

		if(c.startTime.isBefore(pref.startTime))
			return false;

		if(c.endTime.isAfter(pref.endTime))
			return false;

		for(Course other: pref.extraCurriculars) {
			if(sameTime(c, other))
				return false;
		}

		return true;
	}

	private boolean verifyLecture(Course co, Schedule s) {

		for(Course c: s.decidedClasses) {

			if(c.department.equals(co.department) && c.courseNumber == co.courseNumber
					&& c.sectionType.equals("Lec"))
				return true;

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

	// return T if already exists a course of same type and meet user's pref
	private boolean multipleSections(Course c) {

		ArrayList<Course> sameType = new ArrayList<Course>();
		for(Course co: all_courses) {
			// same department & courseNumber & sectionType
			if(co.sameType(c))
				sameType.add(co);
			//EDIT: only remove if other section does not violate, otherwise keep all
		}

		for(Course co: sameType) {
			if(meetsPreferences(co))
				return true;
		}

		return false;

	}

}
