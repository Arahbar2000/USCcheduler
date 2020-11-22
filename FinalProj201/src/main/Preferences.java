package main;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Preferences {
	public LocalTime startTime;
	public LocalTime endTime;
	public int desiredUnits;
	public List<Map<String, LocalTime>> extraCurriculum = new ArrayList<>(); // Stores time windows that are blocked in users schedule
	// list of courseNames (string), fetched from Schedule table
	public List<String> courseList = new ArrayList<>();

	//User will make preferences, push with their ID and all these preferences
	public Preferences(LocalTime startTime, LocalTime endTime, int desiredUnits ){
		this.startTime = startTime;
		this.endTime = endTime;
		this.desiredUnits = desiredUnits;
	}

	// call on User.java::updatePref
	public void setExtraCurriculum(List<Map<String, LocalTime>> extraCurriculum){
		this.extraCurriculum = extraCurriculum;
	}

	// call on User.java::updatePref
	public void setCourseList(List<String> courseList){
		this.courseList = courseList;
	}

	@Override
	public String toString() {
		return "Preferences{" +
				"startTime=" + startTime +
				", endTime=" + endTime +
				", desiredUnits=" + desiredUnits +
				", extraCurriculum=" + extraCurriculum +
				'}';
	}
}
