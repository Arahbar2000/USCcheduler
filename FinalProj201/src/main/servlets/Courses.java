package main.servlets;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;

@WebServlet(name = "CourseServlet", urlPatterns = "/api/courses")
public class Courses extends HttpServlet {

    private static final long serialVersionUID = 1L;
    @Resource(name = "jdbc/cs201")
    private DataSource dataSource;

    // get courses that satisfy the requirement (return json)
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {

        response.setContentType("application/json"); // Response mime type

        // write to response
        PrintWriter out = response.getWriter();

        // assume there would always be department and courseNumber
        String department = request.getParameter("department");
        int courseNumber = Integer.parseInt(request.getParameter("courseNumber"));
        // like 'TTh'
//        String days = request.getParameter("days");
//        String startTime = request.getParameter("startTime");
//        String endTime = request.getParameter("endTime");

        JsonObject respJson = new JsonObject();
        try {
            Connection dbcon = dataSource.getConnection();

            String query = "select *\n" +
                    "from Course\n" +
                    "where department = ?" +
                    "and courseNumber = ?";

            // Declare our statement
            PreparedStatement statement = dbcon.prepareStatement(query);

            statement.setString(1, department);
            statement.setInt(2, courseNumber);

            // Perform the query
            ResultSet rs = statement.executeQuery();

            while (rs.next()) {
                // login success
                JsonObject course = new JsonObject();
                course.addProperty("department", rs.getString("department"));
                course.addProperty("courseNumber", rs.getString("courseNumber"));
                course.addProperty("title", rs.getString("title"));
                course.addProperty("startTime", rs.getString("startTime"));
                course.addProperty("endTime", rs.getString("endTime"));
                course.addProperty("instructor", rs.getString("instructor"));
                course.addProperty("units", rs.getString("units"));
                course.addProperty("daysOfWeek", rs.getString("daysOfWeek"));
                course.addProperty("spots", rs.getString("spots"));

                String section = rs.getString("section");
                if (respJson.has(section)){
                    JsonArray courses_list = (JsonArray) respJson.get(section);
                    courses_list.add(course);
                }
                else{
                    JsonArray courses_list = new JsonArray();
                    courses_list.add(course);
                    respJson.add(section, courses_list);
                }
            }
            rs.close();
            statement.close();
            dbcon.close();
            response.setStatus(200);
        }
        catch (Exception e) {
            System.out.println("error");
            e.printStackTrace();

            // write error message JSON object to output
            respJson.addProperty("status", "fail");
            respJson.addProperty("message", e.getMessage());

            // set response status to 500 (Internal Server Error)
            response.setStatus(500);
        }
        out.println(respJson.toString());
        out.close();
    }
    
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		resp.setContentType("application/json"); // Response mime type
		PrintWriter out = resp.getWriter();

		
		//assumed get this data from request
        String department = req.getParameter("department");
        int courseNumber = Integer.parseInt(req.getParameter("courseNumber"));
        

		try {
			Connection dbcon = dataSource.getConnection();


			String updateQuery = "SET SQL_SAFE_UPDATES = 0;" + "DELETE FROM COURSES WHERE department = '" + department + "' and courseNumber = '"
					+ courseNumber + "'";
			
			Statement st =  dbcon.createStatement();
			st.execute(updateQuery);

			

		} catch (Exception e) {
			System.out.println("error");
			e.printStackTrace();
		}
	}

}
