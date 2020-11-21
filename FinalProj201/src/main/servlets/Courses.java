package main.servlets;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;

import main.User;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
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

        JsonObject respJson = new JsonObject();
        User user = (User) request.getSession().getAttribute("user");
        try {
            Connection dbcon = dataSource.getConnection();

            String query = "SELECT *\n" +
                    "FROM Course\n" +
                    "WHERE department = ?" +
                    "AND courseNumber = ?";

            // Declare our statement
            PreparedStatement statement = dbcon.prepareStatement(query);

            statement.setString(1, department);
            statement.setInt(2, courseNumber);

            // Perform the query
            ResultSet rs = statement.executeQuery();

            if(rs.next()){
                query = "INSERT INTO Schedule (userId, department, courseNumber)\n" +
                            "VALUES (?, ?, ?)\n";

                statement = dbcon.prepareStatement(query);

                statement.setInt(1, user.id);
                statement.setString(2, department);
                statement.setInt(3, courseNumber);

                // Perform the query
                statement.executeUpdate();

                //status OK
                respJson.addProperty("message", "inserted");
                response.setStatus(200);
            }
            else response.setStatus(404);//course does not exist

            rs.close();
            statement.close();
            dbcon.close();
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
        user.updatePref();
        out.println(respJson.toString());
        out.close();
    }



    protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        resp.setContentType("application/json"); // Response mime type
        PrintWriter out = response.getWriter();

        JsonObject respJson = new JsonObject();
        User user = (User) request.getSession().getAttribute("user");
        if (user == null) {//not logged in
            respJson.addProperty("status", "error");
            respJson.addProperty("message", "user if not logged in");
            response.setStatus(500);
        }
        else{//user is logged in
            try (Connection dbcon = dataSource.getConnection()) {
                String department = request.getParameter("department");
                int courseNumber = Integer.parseInt(request.getParameter("courseNumber"));

                String query = "SELECT * FROM Schedule WHERE userId = ? AND department = ? AND courseNumber = ?";

                // Declare our statement
                PreparedStatement statement = dbcon.prepareStatement(query);

                statement.setInt(1, user.id);
                statement.setString(2, department);
                statement.setInt(3, courseNumber);

                // Perform the query
                ResultSet rs = statement.executeQuery();

                if(!rs.next()) response.setStatus(404); //no record of user matches that to be removed
                else{
                    query =
                        "DELETE FROM Schedule WHERE userId = ? AND department = ? AND courseNumber = ?";

                    statement = dbcon.prepareStatement(query);
                    statement.setInt(1, user.id);
                    statement.setString(2, department);
                    statement.setInt(2, courseNumber);
                    statement.executeUpdate();
                    respJson.addProperty("message", "deleted");
                    user.updatePref();
                }
                respJson.addProperty("status", "success");
                statement.close();
                response.setStatus(200);
            } catch (SQLException throwables){
                throwables.printStackTrace();
                respJson.addProperty("status", "error");
                respJson.addProperty("message", "SQL database error");
                response.setStatus(500);
            }
        }
        out.println(respJson.toString());
        out.close();
    }
}
