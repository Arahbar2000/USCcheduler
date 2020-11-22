package main.servlets;

 import com.google.gson.JsonArray;
 import com.google.gson.JsonObject;

 import java.time.LocalTime;
 import java.util.List;
 import java.util.HashSet;
 import main.JDBCCredential;
 import main.*;
 import main.User;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.DataSource;
import java.io.IOException;
import java.io.PrintWriter;
 import java.util.Map;

 import main.User;
/**
 * Servlet implementation class Profile
 */
@WebServlet(name = "Profile", urlPatterns = "/api/profile")
public class Profile extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
		// TODO Auto-generated method stub
		response.setContentType("application/json");
		PrintWriter out = response.getWriter();
		
		User user = (User) request.getSession().getAttribute("user");
		System.out.println(user);
		JsonObject profile = getUserProfile(user);
		
		out.println(profile.toString());
	}
	
    private JsonObject getUserProfile(User user) {
    	
    	JsonObject profile = new JsonObject();
    	if(user == null)
    		return profile;
    	
    	profile.addProperty("Fname", user.firstName);
    	profile.addProperty("Lname", user.lastName);
     	profile.addProperty("Email", user.email);
     	profile.addProperty("ID", user.id);

     	CreateSchedule cs = new CreateSchedule(user);
     	List<String> co = cs.req;

     	HashSet<String> unique_courses = new HashSet<>(co);
     	StringBuilder courses = new StringBuilder();


     	for(String s: unique_courses) {
     		courses.append(s).append(",");
     	}

     	profile.addProperty("Courses", courses.toString());

     	if(user.prefs != null) {
 	    	profile.addProperty("StartTime", user.prefs.startTime.toString());
			profile.addProperty("Endtime", user.prefs.endTime.toString());
			courses = new StringBuilder();

 	    	for(int i = 0; i < user.prefs.courseList.size(); i++) {
 	    		String c = user.prefs.courseList.get(i);

 	    		courses.append(c);
 	    		if(i != user.prefs.courseList.size()-1) {
 	    			courses.append(",");
 	    		} 
 	    	}
 	    	profile.addProperty("Courses", courses.toString());

			JsonArray extraCurriculum = new JsonArray();
			for (Map<String, LocalTime> ex: user.prefs.extraCurriculum){
			    JsonObject obj = new JsonObject();
				obj.addProperty("startTime", ex.get("startTime").toString());
				obj.addProperty("endTime", ex.get("endTime").toString());
				extraCurriculum.add(obj);
			}
 	    	profile.add("extraCurriculum", extraCurriculum);
		 }
		 return profile;

     }

     }