package controller.user;

import com.fasterxml.jackson.databind.ObjectMapper;
import dtos.UserDto;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import mapper.UserMapper;
import models.parent.User;
import services.userService.UserService;
import java.io.IOException;
import java.util.List;

@WebServlet("/users/*")
public class UserServlet extends HttpServlet {

    private final UserService userService = new UserService();
    private final ObjectMapper objectMapper = new ObjectMapper();

    // POST http://localhost:8080/PahanaEduBackEnd/users/register
    // POST http://localhost:8080/PahanaEduBackEnd/users/login
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        try {
            String pathInfo = request.getPathInfo();

            if (pathInfo == null || pathInfo.equals("/")) {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                response.getWriter().write("{\"error\": \"Endpoint required (register/login)\"}");
                return;
            }

            switch (pathInfo.substring(1)) {
                case "register":
                    userService.registerUser(request);
                    response.getWriter().write("{\"message\": \"Registered Successfully\"}");
                    break;
                case "login":
                    int loginSuccessWithUserId = userService.login(request, response);
                    response.getWriter().write("{\"message\": \"Login successful\", \"userId\": " + loginSuccessWithUserId + "}");
                    //userService.login(request, response);
//                    response.getWriter().write("{\"message\": \"Login successful\"}");
                    break;
                default:
                    response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                    response.getWriter().write("{\"error\": \"Invalid endpoint\"}");
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().write("{\"error\": \"Server error: " + ex.getMessage() + "\"}");
        }
    }

    // GET http://localhost:8080/PahanaEduBackEnd/users (get all)
    // GET http://localhost:8080/PahanaEduBackEnd/users/{id} (get by id)
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        String pathInfo = request.getPathInfo();

        try {
            if (pathInfo != null && pathInfo.equals("/login")) {
                // Handle login
                try {
                    int loginSuccessWithUserId = userService.login(request, response);

                    if (loginSuccessWithUserId>0) {
                        response.setStatus(HttpServletResponse.SC_OK);
//                        response.getWriter().write("{\"message\": \"Login successful\"}");
                        response.getWriter().write("{\"message\": \"Login successful\", \"userId\": " + loginSuccessWithUserId + "}");
                    } else {
                        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                        response.getWriter().write("{\"error\": \"Login failed\"}");
                    }
                } catch (IllegalArgumentException e) {
                    response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                    response.getWriter().write("{\"error\": \"" + e.getMessage().replace("\"", "\\\"") + "\"}");
                }
            } else if (pathInfo == null || pathInfo.equals("/")) {
                // Get all users
                List<UserDto> users = userService.getAllUsers();

                response.getWriter().write(objectMapper.writeValueAsString(users));
            } else {
                // Get user by ID
                String id = pathInfo.substring(1);
                User user = userService.getUserById(Integer.parseInt(id));
                UserDto userDto = UserMapper.toDTO(user);
                response.getWriter().write(objectMapper.writeValueAsString(userDto));
            }

        } catch (Exception ex) {
            ex.printStackTrace();
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().write("{\"error\": \"Server error: " + ex.getMessage().replace("\"", "\\\"") + "\"}");
        }
    }

    // PUT http://localhost:8080/PahanaEduBackEnd/users/{id} (update details)
    // PUT http://localhost:8080/PahanaEduBackEnd/users/{id}/password (update password)
    @Override
    protected void doPut(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        String pathInfo = request.getPathInfo();

        try {
            if (pathInfo == null || pathInfo.equals("/")) {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                response.getWriter().write("{\"error\": \"User ID required\"}");
                return;
            }

            String[] parts = pathInfo.split("/");
            if (parts.length < 2) {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                response.getWriter().write("{\"error\": \"Invalid path\"}");
                return;
            }

            String id = parts[1];

            if (parts.length > 2 && parts[2].equals("password")) {
                // Update password
                userService.updateUserPassword(request, response);
                response.getWriter().write("{\"message\": \"Password updated\"}");
            } else {
                // Update user details
                userService.updateUser(request, response, id);
                response.getWriter().write("{\"message\": \"User updated\"}");
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().write("{\"error\": \"Update failed: " + ex.getMessage() + "\"}");
        }
    }

    // DELETE http://localhost:8080/PahanaEduBackEnd/users/{id}
    @Override
    protected void doDelete(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        String pathInfo = request.getPathInfo();

        try {
            if (pathInfo == null || pathInfo.equals("/")) {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                response.getWriter().write("{\"error\": \"User ID required\"}");
                return;
            }

            String id = pathInfo.substring(1);
            userService.deleteUser(Integer.parseInt(id));
            response.getWriter().write("{\"message\": \"User deleted\"}");
        } catch (Exception ex) {
            ex.printStackTrace();
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().write("{\"error\": \"Delete failed: " + ex.getMessage() + "\"}");
        }
    }
}