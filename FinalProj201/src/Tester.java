import java.util.Arrays;
import java.time.LocalTime;
import java.util.ArrayList;

public class Tester {

	public static void main(String[] args) {
		// TODO Auto-generated method stub

		Course c1 = new Course("CS", 104, "TTH", LocalTime.of(11, 0, 0), LocalTime.of(12, 20, 0),
				"Lec", "Aaron Cote", 4, true);
		
		Course c2 = new Course("CS", 104, "TTH", LocalTime.of(14, 0, 0), LocalTime.of(15, 20, 0),
				"Lec", "Py", 4, true);
		
		Course c3 = new Course("CS", 104, "W", LocalTime.of(17, 0, 0), LocalTime.of(18, 50, 0),
				"Lab", "TA1", 4, true);
		
		Course c4 = new Course("CS", 104, "F", LocalTime.of(12, 0, 0), LocalTime.of(13, 50, 0),
				"Lab", "TA2", 4, true);
		
		Course c5 = new Course("CS", 104, "TH", LocalTime.of(19, 0, 0), LocalTime.of(20, 50, 0),
				"Qz", "TA+Lec", 4, true);
		
		Course c6 = new Course("PHYS", 316, "TTH", LocalTime.of(12, 0, 0), LocalTime.of(13, 50, 0),
				"Lec", "TA", 4, true);
		
		Course c7 = new Course("PHIl", 120, "MWF", LocalTime.of(9, 0, 0), LocalTime.of(9, 50, 0),
				"Lec", "Zlatan", 4, true);
		
		CreateSchedule cs = new CreateSchedule(null);
		cs.all_courses.add(c1);
		cs.all_courses.add(c2);
		cs.all_courses.add(c3);
		cs.all_courses.add(c4);
		cs.all_courses.add(c5);
		cs.all_courses.add(c6);
		cs.all_courses.add(c7);
		//cs.all_courses.add(c8);
		
		ArrayList<Schedule> s = cs.getSchedules(10);
		for(Schedule t: s) {
			for(Course c: t.decidedClasses) {
				System.out.print(c.department + " " + c.courseNumber + " " + c.instructor + "\t");
			}
			System.out.println();
		}
		
		
		
		
	}

}
