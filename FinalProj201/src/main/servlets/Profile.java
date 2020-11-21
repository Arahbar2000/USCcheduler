package main.servlets;

import com.google.gson.JsonObject;
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
    	
    	if(user.prefs != null) {
	    	profile.addProperty("StartTime", user.prefs.startTime.toString());
	    	profile.addProperty("Endtime", user.prefs.endTime.toString());
	    	String courses = "";
	    	for(int i = 0; i < user.prefs.courseList.size(); i++) {
	    		String c = user.prefs.courseList.get(i);
	    		
	    		courses += c;
	    		if(i != user.prefs.courseList.size()-1) {
	    			courses += ",";
	    		} 
	    		
	    	}
	    	profile.addProperty("Courses", courses);
    	}
    	return profile;
    	
    }



}
