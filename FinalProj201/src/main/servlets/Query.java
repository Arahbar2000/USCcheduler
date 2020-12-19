package main.servlets;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;

import main.User;
import main.JDBCCredential;

import javax.sql.DataSource;
import javax.naming.InitialContext;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.ServletException;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.HashMap;
import java.util.Map;
import java.sql.SQLException;

@WebServlet(name = "QueryServlet", urlPatterns="/api/query")
public class Query extends HttpServlet {
    private static final long serialVersionUID = 1L;
    

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("application/json"); // Response mime type
        PrintWriter out = response.getWriter();
        String department = request.getParameter("department");
        String courseNumber = request.getParameter("courseNumber");
        System.out.println(courseNumber);
        JsonArray courses = new JsonArray();
        ResultSet rs = null;
        PreparedStatement statement = null;
        Connection dbcon = null;
        try {
            InitialContext ctx = new InitialContext();
            DataSource ds = (DataSource)ctx.lookup("java:comp/env/jdbc/db");
            dbcon = ds.getConnection();
            String query = "select distinct department, courseNumber, title from Course where department like ?" + 
                            " and courseNumber like ? order by cast(department as unsigned), department asc;";    
            // Declare our statement
            statement = dbcon.prepareStatement(query);
            statement.setString(1, department.equals("null") ? "%" : department + "%");
            statement.setString(2, courseNumber.equals("null") ? "%" : courseNumber + "%");
            // Perform the query
            rs = statement.executeQuery();
            while(rs.next()) {
                JsonObject course = new JsonObject();
                course.addProperty("department", rs.getString("department"));
                course.addProperty("courseNumber", rs.getString("courseNumber"));
                course.addProperty("title", rs.getString("title"));
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
                out.close();
                dbcon.close();

            }
            catch(Exception e) {

            }
        }
    }

}