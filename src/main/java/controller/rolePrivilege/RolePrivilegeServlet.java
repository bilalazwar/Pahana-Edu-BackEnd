package controller.rolePrivilege;

import com.fasterxml.jackson.databind.ObjectMapper;
import dao.implementations.RolePrivilegeImpl;
import dao.interfaces.RolePrivilegeDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import models.rolePrivilege.RolePrivilege;
import services.privilegeRoleService.RolePrivilegeService;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@WebServlet("/rolePrivilege")
public class RolePrivilegeServlet extends HttpServlet {

    private RolePrivilegeService rolePrivilegeService;
    private final ObjectMapper mapper = new ObjectMapper();

    @Override
    public void init() throws ServletException {
        RolePrivilegeDAO rolePrivilegeDAO = new RolePrivilegeImpl();
        this.rolePrivilegeService = new RolePrivilegeService(rolePrivilegeDAO);
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        // Handles:
        // - GET /rolePrivilege → getPrivilegeRole()
        // - GET /rolePrivilege?roleId=x → getPrivilegesByRoleId(x)
        // - GET /rolePrivilege?roleId=x&privilegeId=y&checkExistence=true → privilegeRoleExists(x, y)

        Map<String, Object> jsonResponse = new HashMap<>();

        try {
            String checkExistence = request.getParameter("checkExistence");
            String roleIdParam = request.getParameter("roleId");
            String privilegeIdParam = request.getParameter("privilegeId");

            if ("true".equalsIgnoreCase(checkExistence) && roleIdParam != null && privilegeIdParam != null) {
                // privilegeRoleExists
                int roleId = Integer.parseInt(roleIdParam);
                int privilegeId = Integer.parseInt(privilegeIdParam);

                boolean exists = rolePrivilegeService.privilegeRoleExists(roleId, privilegeId);
                jsonResponse.put("exists", exists);
                response.setStatus(HttpServletResponse.SC_OK);
                writeJson(response, jsonResponse);
                return; // exit the method early once the appropriate response has been generated
            }

            if (roleIdParam != null) {
                // getPrivilegesByRoleId
                int roleId = Integer.parseInt(roleIdParam);
                List<RolePrivilege> result = rolePrivilegeService.getPrivilegesByRoleId(roleId);
                response.setStatus(HttpServletResponse.SC_OK);
                writeJson(response, result);  // consistent use of writeJson
                return;
            }

            // getPrivilegeRole (all role-privilege mappings)
            List<RolePrivilege> result = rolePrivilegeService.getPrivilegeRole();
            response.setStatus(HttpServletResponse.SC_OK);
            writeJson(response, result);  // consistent use of writeJson

        } catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            jsonResponse.put("error", e.getMessage());
            writeJson(response, jsonResponse);  // consistent use of writeJson
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Map<String, Object> jsonResponse = new HashMap<>();

        try {
            String roleIdParam = request.getParameter("roleId");
            String privilegeIdParam = request.getParameter("privilegeId");

            if (roleIdParam == null || privilegeIdParam == null) {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                jsonResponse.put("error", "Missing roleId or privilegeId");
                writeJson(response, jsonResponse);
                return;
            }

            int roleId = Integer.parseInt(roleIdParam);
            int privilegeId = Integer.parseInt(privilegeIdParam);

            // Call service method (now includes internal validation)
            int rowsAffected = rolePrivilegeService.addPrivilegeToRole(roleId, privilegeId);

            jsonResponse.put("message", "Privilege added to role successfully");
            jsonResponse.put("rowsAffected", rowsAffected);
            response.setStatus(HttpServletResponse.SC_CREATED);
            writeJson(response, jsonResponse);
        }
        catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            jsonResponse.put("error", "Internal server error: " + e.getMessage());
            writeJson(response, jsonResponse);
        }
    }

    @Override
    protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Map<String, Object> jsonResponse = new HashMap<>();

        try {
            String roleIdParam = request.getParameter("roleId");
            String privilegeIdParam = request.getParameter("privilegeId");

            if (roleIdParam == null || privilegeIdParam == null) {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                jsonResponse.put("error", "Missing roleId or privilegeId");
                writeJson(response, jsonResponse);
                return;
            }

            int roleId = Integer.parseInt(roleIdParam);
            int privilegeId = Integer.parseInt(privilegeIdParam);

            // Service method includes validation and mapping check
            int rowsAffected = rolePrivilegeService.deletePrivilegeRole(roleId, privilegeId);

            jsonResponse.put("message", "Privilege removed from role successfully");
            jsonResponse.put("rowsAffected", rowsAffected);
            response.setStatus(HttpServletResponse.SC_OK);
            writeJson(response, jsonResponse);
        }
        catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            jsonResponse.put("error", "Internal server error: " + e.getMessage());
            writeJson(response, jsonResponse);
        }
    }
    @Override
    protected void doPut(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }

    private void writeJson(HttpServletResponse response, Object data) throws IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        mapper.writeValue(response.getWriter(), data);
        // mapper is part of jackson library
        // converts java to json.
        // Converts the Java object into JSON and writes it to the response
    }
}



//public RolePrivilegeServlet(RolePrivilegeService rolePrivilegeService) {
//    this.rolePrivilegeService = rolePrivilegeService;
//}
//
//protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
//
//
//}
//protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
//
//    System.out.println("hello");
//
//}
//protected void doPut(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
//
//}
//protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
//
//}