package main;

import java.time.LocalTime;

public class Course {
	
	public String department;
	public int sectionId;
	public int courseNumber;
	public String title;
	public String daysOfWeek;
	public LocalTime startTime;
	public LocalTime endTime;
	public String sectionType; //"Lec", "Lab", "Qz", "Dis"
	public String instructor;
	public int units;
	public String spots;
	public boolean []days; //sets true to respective day if section on that day

	@Override
	public String toString() {
		return "Course{" +
				"department='" + department + '\'' +
				", courseNumber=" + courseNumber +
				", title='" + title + '\'' +
				", daysOfWeek='" + daysOfWeek + '\'' +
				", startTime=" + startTime +
				", endTime=" + endTime +
				", sectionType='" + sectionType + '\'' +
				", instructor='" + instructor + '\'' +
				", units=" + units +
				", spots='" + spots + '\'' +
				'}';
	}

	public Course (int sectionId, String department, int courseNumber,
				   String title,
				   String daysOfWeek,
				   LocalTime startTime, LocalTime endTime,
				   String sectionType, String instructor, int units, String spots) {
		this.sectionId = sectionId;
		this.department = department;
		this.courseNumber = courseNumber;
		this.title = title;
		this.daysOfWeek = daysOfWeek;
		this.startTime = startTime;
		this.endTime = endTime;
		this.sectionType = sectionType;
		this.instructor = instructor;
		this.units = units;
		this.spots = spots;

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

		return this.department.equals(other.department) && this.courseNumber == other.courseNumber &&
				this.daysOfWeek.equals(other.daysOfWeek) && this.startTime.equals(other.startTime) &&
				this.endTime.equals(other.endTime) && this.instructor.equals(other.instructor);
	}

	// same department & courseNumber & sectionType
	public boolean sameType(Course other) {

		return this.department.equals(other.department) && this.courseNumber == other.courseNumber
				&& this.sectionType.equals(other.sectionType);
	}


}
