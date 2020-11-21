package main.servlets;

import com.google.gson.JsonObject;
import main.User;

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

@WebServlet(name = "PrefServlet", urlPatterns = "/api/pref")
public class Pref extends HttpServlet {

    private static final long serialVersionUID = 1L;

    @Resource(name = "jdbc/cs201")
    private DataSource dataSource;

    // user input preferences
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        resp.setContentType("application/json"); // Response mime type
        PrintWriter out = resp.getWriter();

        JsonObject respJson = new JsonObject();
        User user = (User) req.getSession().getAttribute("user");
        if (user == null) {
            respJson.addProperty("status", "error");
            respJson.addProperty("message", "user if not logged in");
            resp.setStatus(500);
        }
        // if logged in
        else {
            // Null if not provided
            // eg. CSCI201,CSCI270
            String courseName = req.getParameter("courseName");
            // eg. 08:00
            String startTime = req.getParameter("startTime");
            String endTime = req.getParameter("endTime");
            // eg. [08:00 09:00], [10:00,11:00]
            String extraCurriculum = req.getParameter("extraCurriculum");
            String desiredUnits = req.getParameter("desiredUnits");
            try (Connection dbcon = dataSource.getConnection()) {
                String update = req.getParameter("update");
                PreparedStatement stmt = null;
                if (update == null) {
                    resp.sendError(HttpServletResponse.SC_BAD_REQUEST);
                    return;
                }
                // insert db
                else if (update.equals("0")) {
                    String query =
                            "INSERT INTO Preferences(userId, courseName, startTime, endTime, extraCurriculum, desiredUnits)\n" +
                                    "VALUES (?,?,?,?,?,?);\n";
                    stmt = dbcon.prepareStatement(query);

                    stmt.setInt(1, user.id);
                    // use setObject to insert NULL if object == null
                    stmt.setObject(2, courseName);
                    stmt.setObject(3, startTime);
                    stmt.setObject(4, endTime);
                    stmt.setObject(5, extraCurriculum);
                    stmt.setObject(6, desiredUnits);
                    respJson.addProperty("message", "inserted");
                }
                // update db
                else if (update.equals("1")) {
                    String query =
                            "UPDATE Preferences\n" +
                                    "   SET courseName = ?, startTime=?, endTime=?, extraCurriculum = ?, desiredUnits=?\n" +
                                    "WHERE userId = ?";
                    stmt = dbcon.prepareStatement(query);

                    // use setObject to insert NULL if object == null
                    stmt.setObject(1, courseName);
                    stmt.setObject(2, startTime);
                    stmt.setObject(3, endTime);
                    stmt.setObject(4, extraCurriculum);
                    stmt.setObject(5, desiredUnits);

                    stmt.setInt(6, user.id);
                    respJson.addProperty("message", "updated");
                }
                // update = 0/1
                else{
                    resp.sendError(HttpServletResponse.SC_BAD_REQUEST);
                    return;
                }
                stmt.executeUpdate();

                respJson.addProperty("status", "success");
                stmt.close();
                resp.setStatus(200);
            } catch (SQLException throwables) {
                throwables.printStackTrace();
                respJson.addProperty("status", "error");
                respJson.addProperty("message", "SQL database error");
                resp.setStatus(500);
            }
            user.updatePref();
            System.out.println(user);
        }
        out.println(respJson.toString());
        out.close();
    }
}
