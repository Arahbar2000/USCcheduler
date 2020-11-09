import java.time.LocalTime;
import java.util.ArrayList;

public class Preferences {
	public ArrayList<String> courseList; // unordered list of desired classe NAMES
	public LocalTime startTime;
	public LocalTime endTime;
	public int desiredUnits;
	public ArrayList<Course> extraCurriculars; // Stores time windows that are blocked in user’s schedule
	
	
	//User will make preferences, push with their ID and all these preferences
	Preferences(ArrayList<String> courseList, LocalTime startTime, LocalTime endTime, int desiredUnits, ArrayList<Course> extraCurriculars){
		this.courseList = courseList;
		this.startTime = startTime;
		this.endTime = endTime;
		this.desiredUnits = desiredUnits;
		this.extraCurriculars = extraCurriculars;
	}
}
