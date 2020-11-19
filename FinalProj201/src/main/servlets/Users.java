package main.servlets;

import com.google.gson.JsonObject;

import javax.annotation.Resource;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

@WebServlet(name = "UsersServlet", urlPatterns = "/api/users")
public class Users extends HttpServlet{

    private static final long serialVersionUID = 1L;

    @Resource(name = "jdbc/cs201")
    private DataSource dataSource;

    // create new user
    // TODO
    // consider duplicate
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json"); // Response mime type
        PrintWriter out = resp.getWriter();

        String email = req.getParameter("email");
        String password = req.getParameter("password");
        String fname = req.getParameter("fname");
        String lname = req.getParameter("lname");

        JsonObject responseJsonObject = new JsonObject();

        if (email == null || password == null || fname == null || lname == null){
            responseJsonObject.addProperty("message", "some fields are empty");
            out.println(responseJsonObject.toString());
            resp.setStatus(500);
        }
        else{
            try (Connection dbcon = dataSource.getConnection()){
                String query = "Insert into Users(lastName, firstName, email, password)" +
                        "values (?, ?, ?, ?)";
                PreparedStatement stmt = dbcon.prepareStatement(query);

                stmt.setString(1, lname);
                stmt.setString(2, fname);
                stmt.setString(3, email);
                stmt.setString(4, password);
                stmt.executeUpdate();
                System.out.println("updating!");
                System.out.println(stmt);
                responseJsonObject.addProperty("message", "inserted");
                out.println(responseJsonObject.toString());
                stmt.close();
                resp.setStatus(200);
            } catch (SQLException throwables) {
                throwables.printStackTrace();
                resp.setStatus(500);
            }
        }

        out.close();
    }

}
