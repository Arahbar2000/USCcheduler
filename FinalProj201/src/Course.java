import java.time.LocalTime;
import java.util.ArrayList;

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
	public boolean []days; //sets true to respective day if section on that day
	public boolean usedFirst;
	
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
		this.usedFirst = false;
		
		days = new boolean[5];
		for(int i = 0; i < days.length; i++)
			days[i] = false;
		
		parseDays();
	}
	
	private void parseDays() {
		
		if(daysOfWeek == null)
			return;
		
		String[] day = {"m", "t", "w",  "th", "f"};
		String dow = daysOfWeek.toLowerCase();
		
		for(int i = day.length-1; i >= 0; i--) {
			
			int index = dow.indexOf(day[i]);
			if(index != -1) {
				days[i] = true;
				dow = dow.replace(day[i], "");
			}
			
		} 
		
	}
	
	public boolean equals(Course other) {
		
		if(this.department.equals(other.department) && this.courseNumber == other.courseNumber &&
				this.daysOfWeek.equals(other.daysOfWeek) && this.startTime.equals(other.startTime) &&
				this.endTime.equals(other.endTime) && this.instructor.equals(other.instructor))
			return true;
		else
			return false;
	}
	
	public boolean sameType(Course other) {
		
		if(this.department.equals(other.department) && this.courseNumber == other.courseNumber
				&& this.sectionType.equals(other.sectionType))
			return true;
		
		return false;
	}


}
