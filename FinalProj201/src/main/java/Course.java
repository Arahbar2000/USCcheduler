import java.time.LocalTime;

public class Course {
	
	public String department;
	public int courseNumber;
	public String daysOfWeek;
	public LocalTime startTime;
	public LocalTime endTime;
	public String sectionType; //"Lec", "Lab", "Qz", "Dis"
	public String instructor;
	public int units;
	public boolean required;
	
	public Course (String department, int courseNumber, String daysOfWeek, LocalTime startTime, LocalTime endTime,
			String sectionType, String instructor, int units, boolean required) {
		this.department = department;
		this.courseNumber = courseNumber;
		this.daysOfWeek = daysOfWeek;
		this.startTime = startTime;
		this.endTime = endTime;
		this.sectionType = sectionType;
		this.instructor = instructor;
		this.units = units;
		this.required = required;
	}


}
