package controller.rolePrivilege;

import com.fasterxml.jackson.databind.ObjectMapper;
import dao.implementations.PrivilegeDAOImpl;
import dao.interfaces.PrivilegeDAO;
import dtos.PrivilegeDTO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import models.rolePrivilege.Privilege;
import services.privilegeRoleService.PrivilegeService;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@WebServlet("/privileges/*")
public class PrivilegeServlet extends HttpServlet {

    private PrivilegeService privilegeService;

    // Manual dependency injection using init()
    @Override
    public void init() throws ServletException {
        PrivilegeDAO privilegeDAO = new PrivilegeDAOImpl();
        this.privilegeService = new PrivilegeService(privilegeDAO);
    }

    // POST http://localhost:8080/PahanaEduBackEnd/privileges
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        ObjectMapper mapper = new ObjectMapper();
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        try {
            // Parse JSON request body to PrivilegeDTO
            PrivilegeDTO privilegeDTO = mapper.readValue(request.getReader(), PrivilegeDTO.class);
            String message = privilegeService.addPrivilege(privilegeDTO);

            Map<String, String> jsonResponse = new HashMap<>();
            jsonResponse.put("message", message);
            mapper.writeValue(response.getWriter(), jsonResponse);

        } catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("error", e.getMessage());
            mapper.writeValue(response.getWriter(), errorResponse);
        }
    }

    // GET http://localhost:8080/PahanaEduBackEnd/privileges (get all)
    // GET http://localhost:8080/PahanaEduBackEnd/privileges/{id} (get by id)
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        ObjectMapper mapper = new ObjectMapper();
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        try {
            String pathInfo = request.getPathInfo();

            if (pathInfo == null || pathInfo.equals("/")) {
                // Get all privileges
                List<PrivilegeDTO> privilegeDTOs = privilegeService.getAllPrivileges();
                mapper.writeValue(response.getWriter(), privilegeDTOs);
            } else {
                // Get privilege by ID
                String id = pathInfo.substring(1);
                Privilege privilege = privilegeService.getPrivilegeById(Integer.parseInt(id));
                mapper.writeValue(response.getWriter(), privilege);
            }
        } catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("error", e.getMessage());
            mapper.writeValue(response.getWriter(), errorResponse);
        }
    }

    // PUT http://localhost:8080/PahanaEduBackEnd/privileges/{id}
    @Override
    protected void doPut(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        ObjectMapper mapper = new ObjectMapper();
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        try {
            String pathInfo = request.getPathInfo();
            if (pathInfo == null || pathInfo.equals("/")) {
                throw new IllegalArgumentException("Missing privilege ID in path");
            }

            String id = pathInfo.substring(1);
            PrivilegeDTO privilegeDTO = mapper.readValue(request.getReader(), PrivilegeDTO.class);
            privilegeDTO.setId(Integer.parseInt(id)); // Ensure ID matches path

            String result = privilegeService.updatePrivilege(privilegeDTO);

            // Send proper JSON success response
            Map<String, String> jsonResponse = new HashMap<>();
            jsonResponse.put("message", result);
            mapper.writeValue(response.getWriter(), jsonResponse);

        } catch (Exception e) {
            e.printStackTrace();
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);

            // Send proper JSON error response
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("error", "Failed to update privilege: " + e.getMessage());
            mapper.writeValue(response.getWriter(), errorResponse);
        }
    }


    // DELETE http://localhost:8080/PahanaEduBackEnd/privileges/{id}
    @Override
    protected void doDelete(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        ObjectMapper mapper = new ObjectMapper();
        response.setContentType("application/json");
        PrintWriter out = response.getWriter();

        try {
            String pathInfo = request.getPathInfo();
            if (pathInfo == null || pathInfo.equals("/")) {
                throw new IllegalArgumentException("Missing privilege ID in path");
            }

            String id = pathInfo.substring(1);
            String resultMessage = privilegeService.deletePrivilegeById(Integer.parseInt(id));

            Map<String, String> responseMap = new HashMap<>();
            responseMap.put("message", resultMessage);

            if ("Successfully deleted".equals(resultMessage)) {
                response.setStatus(HttpServletResponse.SC_OK);
            } else {
                response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            }

            mapper.writeValue(out, responseMap);

        } catch (Exception e) {
            e.printStackTrace();
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            out.write("{\"error\": \"Failed to delete privilege: " + e.getMessage() + "\"}");
        }
    }
}