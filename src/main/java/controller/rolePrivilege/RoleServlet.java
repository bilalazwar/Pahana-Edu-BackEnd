package controller.rolePrivilege;

import com.fasterxml.jackson.databind.ObjectMapper;
import dao.implementations.RoleDAOImpl;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import models.rolePrivilege.Role;
import services.privilegeRoleService.RoleService;
import jakarta.servlet.ServletException;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@WebServlet("/roles")
public class RoleServlet extends HttpServlet {

    private RoleService roleService;
    private final ObjectMapper mapper = new ObjectMapper();

    @Override
    public void init() throws ServletException {
        this.roleService = new RoleService(new RoleDAOImpl());
    }

    private void writeJson(HttpServletResponse response, Object data) throws IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        mapper.writeValue(response.getWriter(), data);
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        Map<String, Object> jsonResponse = new HashMap<>();
        try {
            List<Role> roles = roleService.getAllRoles();
            response.setStatus(HttpServletResponse.SC_OK);
            writeJson(response, roles);
        } catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            jsonResponse.put("error", e.getMessage());
            writeJson(response, jsonResponse);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        Map<String, Object> jsonResponse = new HashMap<>();

        try {
            Role role = mapper.readValue(request.getReader(), Role.class);
            roleService.addRole(role.getName());
            response.setStatus(HttpServletResponse.SC_CREATED);
            jsonResponse.put("message", "Role added successfully");
            writeJson(response, jsonResponse);

        } catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            jsonResponse.put("error", e.getMessage());
            writeJson(response, jsonResponse);
        }
    }

    @Override
    protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        Map<String, Object> jsonResponse = new HashMap<>();

        try {
            String idParam = request.getParameter("id");
            if (idParam == null) {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                jsonResponse.put("error", "Missing role ID");
                writeJson(response, jsonResponse);
                return;
            }

            int id = Integer.parseInt(idParam);
            String message = roleService.deleteRoleById(id);
            response.setStatus(HttpServletResponse.SC_OK);
            jsonResponse.put("message", message);
            writeJson(response, jsonResponse);

        } catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            jsonResponse.put("error", e.getMessage());
            writeJson(response, jsonResponse);
        }
    }

    @Override
    protected void doPut(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        Map<String, Object> jsonResponse = new HashMap<>();

        try {
            Role role = mapper.readValue(request.getReader(), Role.class);
            roleService.updateRole(role);
            response.setStatus(HttpServletResponse.SC_OK);
            jsonResponse.put("message", "Role updated successfully");
            writeJson(response, jsonResponse);

        } catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            jsonResponse.put("error", e.getMessage());
            writeJson(response, jsonResponse);
        }
    }

}
