package main;


import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.time.LocalTime;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.annotation.Resource;
import javax.sql.DataSource;
import javax.naming.InitialContext;

public class CreateSchedule {

	User user;
	// user's classes in table `Schedule`
	public List<Course> all_courses = new ArrayList<>();
	Preferences pref;
	public List<String> req = new ArrayList<>();
	@Resource(name="jdbc/db")
	private DataSource ds;

	public CreateSchedule(User user) {
		this.user = user;
		all_courses = getUserCourses(this.user);
		pref = user.prefs;

	}
	
	//Guest Constructor
	public CreateSchedule(List<Course> courses, Preferences prefs_) {
		this.user = null;
		all_courses = courses;
		pref = prefs_;
		for(Course c : courses) {
			this.req.add(c.department + c.courseNumber);
		}
	}

	// fetch courses
	private List<Course> getUserCourses(User user) {
		List<Course> courses = new ArrayList<>();
		try (Connection dbcon = ds.getConnection()){

			String query = "select userId, courseId, department, courseNumber, title, " +
				"startTime, endTime, section, instructor, units, daysOfWeek, spots\n" +
				"from Schedule s natural join Course c\n" +
				"where s.userId = ?\n" +
				"and startTime != 'TBA'\n" +
				"and endTime != 'TBA';";

			// Declare our statement
			PreparedStatement statement = dbcon.prepareStatement(query);

			System.out.println(user.id);
			statement.setInt(1, user.id);

			// Perform the query
			ResultSet rs = statement.executeQuery();

			System.out.println("TRYING TO FIND COURSES");

			while (rs.next()) {
				LocalTime startLocalTime = LocalTime.parse(rs.getString("startTime"));
				LocalTime endLocalTime = LocalTime.parse(rs.getString("endTime"));
				System.out.println("GETTING A COURSE");
                Course c = new Course(
                		rs.getInt("courseId"),
                		rs.getString("department"),
						rs.getString("courseNumber"),
						rs.getString("title"),
						rs.getString("daysOfWeek"),
                        startLocalTime,
                        endLocalTime,
                        rs.getString("section"),
						rs.getString("instructor"),
                        rs.getInt("units"),
						rs.getString("spots")
						);

                courses.add(c);
                this.req.add(c.department+ "" + c.courseNumber);
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

	// assume courses_str ='CSCI201,CSCI270‘
	static public List<Course> getStringCourses(String courses_str){
		List<Course> courses = new ArrayList<>();
		ResultSet rs = null;
		PreparedStatement statement = null;
		Connection dbcon = null;
		try {
			InitialContext ctx = new InitialContext();
			DataSource ds = (DataSource)ctx.lookup("java:comp/env/jdbc/db");
			dbcon = ds.getConnection();
			String query = "select courseId, department, courseNumber, title, startTime," +
					" endTime, section, instructor, units, daysOfWeek, spots\n" +
					"from Course c\n" +
					"where department=? and courseNumber=?";
					// "and startTime != 'TBA'\n" +
					// "and endTime != 'TBA';";

			statement = dbcon.prepareStatement(query);
			Pattern pat = Pattern.compile("\\d+\\D*|\\D+");

			for (String course: courses_str.split(",\\s*")){
				Matcher match = pat.matcher(course);
				match.find();
				String department = match.group();
				match.find();
				String courseNumber = match.group();
				System.out.println(department + courseNumber);

				statement.setString(1, department);
				statement.setString(2, courseNumber);

				System.out.println(statement);

				// Perform the query
				rs = statement.executeQuery();

				while (rs.next()) {
					LocalTime startLocalTime = LocalTime.parse(rs.getString("startTime"));
					LocalTime endLocalTime = LocalTime.parse(rs.getString("endTime"));

					Course c = new Course(
							rs.getInt("courseId"),
							rs.getString("department"),
							rs.getString("courseNumber"),
							rs.getString("title"),
							rs.getString("daysOfWeek"),
							startLocalTime,
							endLocalTime,
							rs.getString("section"),
							rs.getString("instructor"),
							rs.getInt("units"),
							rs.getString("spots")
					);
					courses.add(c);
				}
				rs.close();
			}
			//statement.close();
		}
		catch (Exception e) {
			System.out.println("error");
			e.printStackTrace();
		}
		finally {
			try {
				rs.close();
				statement.close();
				dbcon.close();
			} catch(Exception e) {

			}
		}
		return courses;
	}


	// Checks if course meets user preferences
	private boolean meetsPreferences(Course c) {

		if(pref == null)
			return true;

		if(pref.startTime == null && pref.endTime == null) return true;
		System.out.println(pref.startTime);
		if (pref.startTime != null) {
			if(c.startTime.isBefore(pref.startTime))
			return false;
		}
		if (pref.endTime != null) {
			if(c.endTime.isAfter(pref.endTime))
			return false;
		}

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

		String delim = "";
		ArrayList<String> coInSchedule = new ArrayList<String>();

		for(Course c: s.decidedClasses) {
			String name = c.department + delim + c.courseNumber;
			coInSchedule.add(name);
		}


		for(String name: req) {
			if(!coInSchedule.contains(name))
				return false;
		}

		return containsAllSections(s);
	}
	
	private boolean containsAllSections(Schedule s) {
		
		ArrayList<String> checked = new ArrayList<String>();
		
		for(Course orig: s.decidedClasses) {
			
			String c_name = orig.department + orig.courseNumber;
			if(!checked.contains(c_name)) {
				
				checked.add(c_name);
				
				HashSet<String> sections = new HashSet<String>();
				for(Course c: all_courses) {
					
					if(c.department == orig.department && c.courseNumber == orig.courseNumber)
						sections.add(c.sectionType);
					
				}
				
				boolean contains = false;
				for(String section: sections) {
					
					for(Course c: s.decidedClasses) {
						
						if(c.department == orig.department && c.courseNumber == orig.courseNumber
								&& c.sectionType.equals(section)) 
							contains = true;
							
					}
					
					if(!contains)
						return false;
					
				}
			}
		}
		
		return true;
	}


	// return at most n schedules
    // 		each schedule contains valid courses combination (eg. lec + dis + qz)
	//		also includes TBA sections
	public ArrayList<Schedule> getSchedules(int n) {
		
		System.out.println("GETTING SCHEDULES");
		// PROBLEM LINE ****
		all_courses.removeIf(c -> (!meetsPreferences(c) && multipleSections(c)));
		System.out.println("HELLO SCHEDULE");
		// now every interchangeable courses are removed

		ArrayList<Schedule> schedules = new ArrayList<Schedule>();
		int i = 0;
		while(i++ < n) {
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
		
		for(Schedule s: schedules) {
			
			for(Course c: s.decidedClasses) {
				System.out.println(c.toString());
			}
			System.out.println();
			
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
			
		
			if(c.startTime.equals(other.startTime) || c.endTime.equals(other.endTime))
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
		
		if(co.sectionType.equals("Lec-Lab") || co.sectionType.equals("Lec-Dis"))
            return true;

		for(Course c: s.decidedClasses) {
			if(c.department.equals(co.department) && c.courseNumber.equalsIgnoreCase(co.courseNumber)
					&& c.sectionType.equals("Lec"))
				return true;
		}

		return false;

	}


}
