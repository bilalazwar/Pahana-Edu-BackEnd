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

@WebServlet("/users")
public class UserServlet extends HttpServlet {

    private final UserService userService = new UserService();
    private final ObjectMapper objectMapper = new ObjectMapper(); // Jackson for JSON

    //    POST /user?action=register
    //    POST /user?action=login
    //    POST /user?action=updatePassword
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        try {
            String action = request.getParameter("action");


            if (action == null || action.trim().isEmpty()) {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                response.getWriter().write("{\"error\": \"Action parameter is required.\"}");
                return;
            }

            switch (action) {
                case "register":
                    userService.registerUser(request,response);
                    response.getWriter().write("{\"message\": \"Registered Successfully\"}");
                    break;
                case "login":
                    userService.login(request,response);
                    response.getWriter().write("{\"message\": \"Login credentials are correct\"}");
                    break;
//                case "updatePassword":
//                  userService.updateUserPassword(request,response);
//                    response.getWriter().write("{\"message\": \"Password updated.\"}");
//                    break;
                default:
                    response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Unknown action: " + action);
                    response.getWriter().write("{\"error\": \"Unknown action: " + action + "\"}");
            }

        } catch (Exception ex) {
            ex.printStackTrace();
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().write("{\"error\": \"Server error: " + ex.getMessage().replace("\"", "\\\"") + "\"}");
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        String id = request.getParameter("id");

        try {
            if (id != null && !id.isEmpty()) {

                User user = userService.getUserById(Integer.parseInt(id));
                UserDto userDto = UserMapper.toDTO(user);
                response.getWriter().write(objectMapper.writeValueAsString(userDto));
            }
            else{
                List<UserDto> users = userService.getAllUsers();
                response.getWriter().write(objectMapper.writeValueAsString(users));

            }
        } catch (Exception ex) {
            ex.printStackTrace();
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().write("{\"error\": \"Server error: " + ex.getMessage().replace("\"", "\\\"") + "\"}");
        }
    }

    @Override
    protected void doPut(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        String action = request.getParameter("action"); // updatePassword or updateDetails
        String id = request.getParameter("id");

        try {
            if (id == null || id.isEmpty()) {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                response.getWriter().write("{\"error\": \"User ID is required.\"}");
                return;
            }
            if (action == null || action.isEmpty()) {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                response.getWriter().write("{\"error\": \"Action parameter is required.\"}");
                return;
            }

            switch (action) {
                case "updatePassword":
                    userService.updateUserPassword(request, response);
                    response.getWriter().write("{\"message\": \"Password updated successfully.\"}");
                    break;

                case "updateDetails":
                    userService.updateUser(request, response);
                    response.getWriter().write("{\"message\": \"User details updated successfully.\"}");
                    break;

                default:
                    response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                    response.getWriter().write("{\"error\": \"Unknown action: " + action + "\"}");
                    break;
            }
        }
        catch (Exception ex) {
            ex.printStackTrace();
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().write("{\"error\": \"Server error: " + ex.getMessage() + "\"}");
        }
    }

    @Override
    protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        String id = request.getParameter("id");

        try {
            if (id != null && !id.isEmpty()) {
                userService.deleteUser(Integer.parseInt(id));
                response.getWriter().write("{\"message\": \"User Deleted.\"}");
            }
        }
        catch (Exception ex) {
            ex.printStackTrace();
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().write("{\"error\": \"Server error: " + ex.getMessage() + "\"}");
        }
    }
}