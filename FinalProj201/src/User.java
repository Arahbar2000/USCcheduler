import java.util.ArrayList;

public class User {
	public String firstName;
	public String lastName;
	public int id;
	public String email;
	public String password;
	public ArrayList<Integer> friends ;
	public ArrayList<Schedule> currentSchedule;
	public Preferences prefs;
	
	public User(String firstName,String lastName, int id, String email,	String password)
	{
		this.firstName = firstName;
		this.lastName = lastName;
		this.id = id;
		this.email = email;
		this.password = password;		
		
	}
	
	//make preferences, push to table
	
	
	
	

}
