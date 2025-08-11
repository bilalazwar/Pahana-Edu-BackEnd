package controller.loginServlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import services.userService.UserService;

import java.io.IOException;

@WebServlet("/login")
public class LoginServlet extends HttpServlet {

    private final UserService userService = new UserService();

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        try {
            boolean isValid  = userService.login(request,response);

            if (isValid) {
                response.setStatus(HttpServletResponse.SC_OK);
                response.getWriter().write("{\"message\": \"Login successful\"}");

            } else {
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                response.getWriter().write("{\"error\": \"Invalid username or password\"}");
            }
        }
        catch (Exception e) {
            e.printStackTrace(); // Optional: log to file in production
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().write("{\"error\": \"An internal error occurred\"}");
        }

    }
}



//@WebServlet("/logout")
//public class LogoutServlet extends HttpServlet {
//
//    @Override
//    protected void doGet(HttpServletRequest request, HttpServletResponse response)
//            throws ServletException, IOException {
//
//        response.setContentType("application/json");
//        response.setCharacterEncoding("UTF-8");
//
//        // Try to get existing session
//        HttpSession session = request.getSession(false);
//
//        if (session != null) {
//            session.invalidate(); // ✅ Ends the session
//        }
//
//        response.setStatus(HttpServletResponse.SC_OK);
//        response.getWriter().write("{\"message\": \"Logged out successfully\"}");
//    }
//}
