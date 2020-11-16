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
import java.util.HashMap;
import java.util.Map;

@WebServlet(name = "LoginServlet", urlPatterns = "/api/login")
public class Login extends HttpServlet {

    private static final long serialVersionUID = 1L;

    @Resource(name = "jdbc/cs201")
    private DataSource dataSource;

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {

        // allow cors
        response.addHeader("Access-Control-Allow-Origin", "*");
        response.setContentType("application/json"); // Response mime type

        // write to response
        PrintWriter out = response.getWriter();

        String email = request.getParameter("email");
        String password = request.getParameter("password");

        JsonObject responseJsonObject = new JsonObject();

        if (email == null && password == null){
            if (request.getSession().getAttribute("user") != null){
                System.out.println("already login in");
                responseJsonObject.addProperty("status", "success");
                responseJsonObject.addProperty("message", "already login in");
            }
            else{
                responseJsonObject.addProperty("status", "error");
                responseJsonObject.addProperty("message", "not logined in");
            }
            out.println(responseJsonObject.toString());
        }
        else{
            try {
                Connection dbcon = dataSource.getConnection();

                String query = "select *\n" +
                        "from Users\n" +
                        "where email = ?" +
                        "and password = ?";

                // Declare our statement
                PreparedStatement statement = dbcon.prepareStatement(query);

                statement.setString(1, email);
                statement.setString(2, password);

                System.out.println(statement);

                // Perform the query
                ResultSet rs = statement.executeQuery();

                System.out.println("getting login");

                if (rs.next()) {
                    // login success
                    System.out.println("\tlogin success!!");
                    Map<String, String> userInfo = new HashMap<>();
                    userInfo.put("id", rs.getString("userId"));
                    userInfo.put("firstName", rs.getString("firstName"));
                    userInfo.put("lastName", rs.getString("lastName"));
                    userInfo.put("email", rs.getString("email"));

                    System.out.println("hello there! \n");
                    for (String k: userInfo.keySet()){
                        System.out.println("\t " + k + userInfo.get(k));
                    }

                    request.getSession().setAttribute("user", userInfo);

                    responseJsonObject.addProperty("status", "success");
                    responseJsonObject.addProperty("message", "success!");
                }
                else{
                    System.out.println("login fail");
                    responseJsonObject.addProperty("status", "fail");
                    responseJsonObject.addProperty("message", "Invalid email or password. Please try again.");
                }
                response.setStatus(200);
                out.println(responseJsonObject.toString());
                rs.close();
                statement.close();
                dbcon.close();
            }
            catch (Exception e) {
                System.out.println("error");
                e.printStackTrace();

                // write error message JSON object to output
                responseJsonObject.addProperty("status", "fail");
                responseJsonObject.addProperty("message", e.getMessage());

                // set response status to 500 (Internal Server Error)
                response.setStatus(500);
            }
        }


        out.close();
    }
}

