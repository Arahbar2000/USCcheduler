package main;

import java.util.ArrayList;

public class Schedule {
	public ArrayList<Course> decidedClasses; 
	
	public Schedule() {
		decidedClasses = new ArrayList<Course>();
	}
	
	public void addCourse(Course c) {
		decidedClasses.add(c);
	}
	
	public boolean containsCourse(Course c) {
		
		for(Course in: decidedClasses) {
			
			if(c.equals(in))
				return true;
			
		}
		
		return false;
	}
	
	public boolean typeInSchedule(Course other) {
		
		for(Course c: decidedClasses) {
			if (c.sameType(other))
				return true;
		}
		
		return false;
	}
	
	public boolean equals(Schedule other) {
		
		
		for(Course c: decidedClasses) {
			
			if(!other.containsCourse(c))
				return false;
		}
		
		return true;
	}
}