package servlets;

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
import java.util.HashMap;

@WebServlet(name = "PrefServlet", urlPatterns = "/api/pref")
public class Pref extends HttpServlet {

    private static final long serialVersionUID = 1L;

    @Resource(name = "jdbc/cs201")
    private DataSource dataSource;

    // user input preferences
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json"); // Response mime type
        PrintWriter out = resp.getWriter();

        // preference: eg. CS201
        String course = req.getParameter("pref");
        JsonObject respJson = new JsonObject();

        try (Connection dbcon = dataSource.getConnection()){
            String query = "INSERT INTO Preferences(userId, courseName) " +
                    "VALUES (?, ?)";
            PreparedStatement stmt = dbcon.prepareStatement(query);


            HashMap<String, String> user = (HashMap<String, String>) req.getSession().getAttribute("user");
            stmt.setString(1, user.get("id"));
            stmt.setString(2, course);
            stmt.executeUpdate();
            System.out.println("updating!");
            System.out.println(stmt);
            respJson.addProperty("message", "inserted");
            out.println(respJson.toString());
            stmt.close();
            resp.setStatus(200);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            resp.setStatus(500);
        }

        out.close();
    }

}
