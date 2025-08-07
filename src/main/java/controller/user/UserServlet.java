package controller.user;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import models.parent.User;
import services.userService.UserService;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;

@WebServlet("/user")
public class UserServlet extends HttpServlet {

    private final UserService userService = new UserService();
    private final ObjectMapper objectMapper = new ObjectMapper(); // Jackson for JSON

    //    POST /user?action=register
    //    POST /user?action=login
    //    POST /user?action=updatePassword
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String action = request.getParameter("action");

        if (action == null) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Action is required.");
            return;
        }

        try {
            switch (action) {
                case "register":
                    handleRegister(request, response);
                    break;
                case "login":
                    handleLogin(request, response);
                    break;
                case "updatePassword":
                    handleUpdatePassword(request, response);
                    break;
                default:
                    response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Unknown action: " + action);
            }
        } catch (Exception e) {
            e.printStackTrace();
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Error: " + e.getMessage());
        }
    }
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        String idParam = request.getParameter("id");

        try {
            if (idParam != null) {
                int id = Integer.parseInt(idParam);
                User user = userService.getUser(id);
                writeJson(response, user);
            } else {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "User ID is required.");
            }
        } catch (Exception e) {
            handleException(response, e);
        }
    }

    @Override
    protected void doPut(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");

        try {
            switch (action) {
                case "updateUser":
                    handleUpdateUser(request, response);
                    break;
                case "updatePassword":
                    handleUpdatePassword(request, response);
                    break;
                default:
                    response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid action for PUT.");
            }
        } catch (Exception e) {
            handleException(response, e);
        }
    }

    @Override
    protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String idParam = request.getParameter("id");

        try {
            if (idParam != null) {
                int id = Integer.parseInt(idParam);
                userService.deleteUser(id);
                response.setStatus(HttpServletResponse.SC_NO_CONTENT);
            } else {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "User ID is required for deletion.");
            }
        } catch (Exception e) {
            handleException(response, e);
        }
    }

    // ============================
    // === Handler Methods Below ==
    // ============================

    private void handleRegister(HttpServletRequest request, HttpServletResponse response) throws Exception {
        User user = readRequestBody(request, User.class);
        userService.registerUser(user);
        response.setStatus(HttpServletResponse.SC_CREATED);
        writeJson(response, "User registered successfully.");
    }

    private void handleLogin(HttpServletRequest request, HttpServletResponse response) throws Exception {
        String username = request.getParameter("username");
        String password = request.getParameter("password");

        boolean isAuthenticated = userService.login(username, password);
        writeJson(response, isAuthenticated ? "Login successful." : "Invalid credentials.");
    }

    private void handleUpdateUser(HttpServletRequest request, HttpServletResponse response) throws Exception {
        User user = readRequestBody(request, User.class);
        userService.updateUser(user);
        writeJson(response, "User updated successfully.");
    }

    private void handleUpdatePassword(HttpServletRequest request, HttpServletResponse response) throws Exception {
        int userId = Integer.parseInt(request.getParameter("userId"));
        String newPassword = request.getParameter("newPassword");

        userService.updateUserPassword(userId, newPassword);
        writeJson(response, "Password updated successfully.");
    }

    // ============================
    // === Utility Methods Below ==
    // ============================



    private <T> T readRequestBody(HttpServletRequest request, Class<T> clazz) throws IOException {
        try (BufferedReader reader = request.getReader()) {
            return objectMapper.readValue(reader, clazz);
        }
    }

    private void writeJson(HttpServletResponse response, Object data) throws IOException {
        response.setContentType("application/json");
        PrintWriter out = response.getWriter();
        objectMapper.writeValue(out, data);
    }

    private void handleException(HttpServletResponse response, Exception e) throws IOException {
        response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        writeJson(response, "Error: " + e.getMessage());
    }



}

