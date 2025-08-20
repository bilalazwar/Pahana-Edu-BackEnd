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

@WebServlet("/roles/*")
public class RoleServlet extends HttpServlet {

    private RoleService roleService;
    private ObjectMapper mapper;

    @Override
    public void init() throws ServletException {
        this.roleService = new RoleService(new RoleDAOImpl());
        mapper = new ObjectMapper();
    }

    private void writeJson(HttpServletResponse response, Object data) throws IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        mapper.writeValue(response.getWriter(), data);
    }

    // GET http://localhost:8080/PahanaEduBackEnd/roles (get all)
    // GET http://localhost:8080/PahanaEduBackEnd/roles/{id} (get single role)
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Map<String, Object> jsonResponse = new HashMap<>();
        String pathInfo = request.getPathInfo();

        try {
            if (pathInfo == null || pathInfo.equals("/")) {
                // Get all roles
                List<Role> roles = roleService.getAllRoles();
                response.setStatus(HttpServletResponse.SC_OK);
                writeJson(response, roles);
            } else {
                // Get role by ID
                String id = pathInfo.substring(1); // Remove leading slash
                Role role = roleService.getRoleById(Integer.parseInt(id));
                response.setStatus(HttpServletResponse.SC_OK);
                writeJson(response, role);
            }
        } catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            jsonResponse.put("error", e.getMessage());
            writeJson(response, jsonResponse);
        }
    }

    // POST http://localhost:8080/PahanaEduBackEnd/roles (create new role)
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

    // DELETE http://localhost:8080/PahanaEduBackEnd/roles/{id} (delete role)
    @Override
    protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Map<String, Object> jsonResponse = new HashMap<>();
        String pathInfo = request.getPathInfo();

        try {
            if (pathInfo == null || pathInfo.equals("/")) {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                jsonResponse.put("error", "Missing role ID");
                writeJson(response, jsonResponse);
                return;
            }

            String id = pathInfo.substring(1); // Remove leading slash
            String message = roleService.deleteRoleById(Integer.parseInt(id));
            response.setStatus(HttpServletResponse.SC_OK);
            jsonResponse.put("message", message);
            writeJson(response, jsonResponse);

        } catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            jsonResponse.put("error", e.getMessage());
            writeJson(response, jsonResponse);
        }
    }

    // PUT http://localhost:8080/PahanaEduBackEnd/roles/{id} (update role)
    @Override
    protected void doPut(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Map<String, Object> jsonResponse = new HashMap<>();
        String pathInfo = request.getPathInfo();

        try {
            if (pathInfo == null || pathInfo.equals("/")) {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                jsonResponse.put("error", "Missing role ID");
                writeJson(response, jsonResponse);
                return;
            }

            String id = pathInfo.substring(1); // Remove leading slash
            Role role = mapper.readValue(request.getReader(), Role.class);
            role.setId(Integer.parseInt(id));
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