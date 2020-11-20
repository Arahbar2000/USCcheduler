package main;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Preferences {
	public List<String> courseList; // unordered list of desired classe NAMES
	public LocalTime startTime;
	public LocalTime endTime;
	public int desiredUnits;
	public List<Map<String, LocalTime>> extraCurriculum = new ArrayList<>(); // Stores time windows that are blocked in users schedule
	
	
	//User will make preferences, push with their ID and all these preferences
	Preferences(List<String> courseList, LocalTime startTime, LocalTime endTime, int desiredUnits ){
		this.courseList = courseList;
		this.startTime = startTime;
		this.endTime = endTime;
		this.desiredUnits = desiredUnits;
	}

	public void setExtraCurriculum(List<Map<String, LocalTime>> extraCurriculum){
		this.extraCurriculum = extraCurriculum;
	}

	@Override
	public String toString() {
		return "Preferences{" +
				"courseList=" + courseList +
				", startTime=" + startTime +
				", endTime=" + endTime +
				", desiredUnits=" + desiredUnits +
				", extraCurriculum=" + extraCurriculum +
				'}';
	}
}
