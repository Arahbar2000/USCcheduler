package main.servlets;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import main.JDBCCredential;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

@WebServlet(name = "QueryServlet", urlPatterns="/api/query")
public class Query extends HttpServlet {
    private static final long serialVersionUID = 1L;

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        System.out.println("QUERY");
        response.setContentType("application/json"); // Response mime type
        PrintWriter out = response.getWriter();
        String department = request.getParameter("department");
        String courseNumber = request.getParameter("courseNumber");
        System.out.println(courseNumber);
        JsonArray courses = new JsonArray();
        ResultSet rs = null;
        Connection dbcon = null;
        PreparedStatement statement = null;
        try {
//            Class.forName("com.mysql.cj.jdbc.Driver");
            // Connection dbcon = dataSource.getConnection();
            dbcon = DriverManager.getConnection(JDBCCredential.url, JDBCCredential.username, JDBCCredential.password);
            String query = "select distinct department, courseNumber from Course where department like ?" + 
                            " and courseNumber like ?;";
            // Declare our statement
            statement = dbcon.prepareStatement(query);
            statement.setString(1, department + "%");
            statement.setString(2, courseNumber.equals("NaN") ? "%" : courseNumber + "%");
            // Perform the query
            rs = statement.executeQuery();
            while(rs.next()) {
                JsonObject course = new JsonObject();
                course.addProperty("department", rs.getString("department"));
                course.addProperty("courseNumber", rs.getInt("courseNumber"));
                courses.add(course);
            }
            response.setStatus(200);
            out.println(courses.toString());

        }
        catch(Exception e) {
            System.out.println(e);
            response.setStatus(500);
        }
        finally {
            try {
                statement.close();
                rs.close();
                dbcon.close();
                out.close();
            }
            catch(Exception e) {

            }
        }
    }

}