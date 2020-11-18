import java.util.ArrayList;
import java.util.Collections;

public class CreateSchedule {

	User user;
	ArrayList<String> courses;
	ArrayList<Course> all_courses;
	Preferences pref;

	public CreateSchedule(User user) {

		this.user = user;
		all_courses = new ArrayList<Course>();

		if(user != null) {
			courses = user.prefs.courseList;
			pref = user.prefs;
		}
	}


	public ArrayList<Schedule> getSchedules(int n) {

		all_courses.removeIf(c -> !meetsPreferences(c));

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


	//Checks if course meets user preferences
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




		return true;
	}

}
