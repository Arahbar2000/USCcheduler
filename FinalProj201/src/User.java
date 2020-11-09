import java.util.ArrayList;

public class User {
	public String firstName;
	String lastName;
	int id;
	String email;
	String password;
	ArrayList<Integer> friends ;
	ArrayList<Schedule> currentSchedule;
	Preference prefs;
	
	public User(String firstName,String lastName, int id, String email,	String password)
	{
		this.firstName = firstName;
		this.lastName = lastName;
		this.id = id;
		this.email = email;
		this.password = password;		
		
	}

}
