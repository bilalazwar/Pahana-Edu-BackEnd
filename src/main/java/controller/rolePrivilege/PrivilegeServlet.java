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
import services.privilegeRoleService.PrivilegeService;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@WebServlet("/privilege")
public class PrivilegeServlet extends HttpServlet {

    private PrivilegeService privilegeService;

//     Manual dependency injection using init()
    @Override
    public void init() throws ServletException {

        PrivilegeDAO privilegeDAO = new PrivilegeDAOImpl();
        this.privilegeService = new PrivilegeService(privilegeDAO);
    }


    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        PrivilegeDTO privilegeDTO = new PrivilegeDTO();
        ObjectMapper mapper = new ObjectMapper();

        try {
            privilegeDTO.setName(request.getParameter("privilegeName"));
            String message = privilegeService.addPrivilege(privilegeDTO);

            // Prepare JSON response
            Map<String, String> jsonResponse = new HashMap<>();
            jsonResponse.put("message", message);

            // Set content type and write JSON response
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            mapper.writeValue(response.getWriter(), jsonResponse);


        } catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("error", e.getMessage());
            mapper.writeValue(response.getWriter(), errorResponse);
        }
    }


    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        try {
            // Fetch all privileges as DTOs
            List<PrivilegeDTO> privilegeDTOs = privilegeService.getAllPrivileges();

            // Set response content type and encoding
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");

            // Convert list to JSON and write to response
            ObjectMapper mapper = new ObjectMapper();
            mapper.writeValue(response.getWriter(), privilegeDTOs);

        } catch (Exception e) {

            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");

            ObjectMapper mapper = new ObjectMapper();
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("error", e.getMessage());

            mapper.writeValue(response.getWriter(), errorResponse);

        }

    }

    @Override
    protected void doPut(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        try {
            // part of Jackson library where it maps json obj with java obj
            ObjectMapper mapper = new ObjectMapper();
            PrivilegeDTO privilegeDTO = mapper.readValue(request.getReader(), PrivilegeDTO.class);

            String result = privilegeService.updatePrivilege(privilegeDTO);

            response.setStatus(HttpServletResponse.SC_OK);
            response.getWriter().write(result);

        } catch (Exception e) {
            e.printStackTrace();
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().write("Failed to update privilege");
        }

    }


    @Override
    protected void doDelete(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        try {
            // Set response content type
            response.setContentType("application/json");
            PrintWriter out = response.getWriter();

            // Retrieve 'id' parameter from query string (?id=5)
            String idParam = request.getParameter("id");

            if (idParam == null || idParam.isEmpty()) {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                out.write("{\"message\": \"Missing or invalid ID parameter.\"}");
                return;
            }

            int id = Integer.parseInt(idParam);

            String resultMessage = privilegeService.deletePrivilegeById(id);

            if ("Successfully deleted".equals(resultMessage)) {
                response.setStatus(HttpServletResponse.SC_OK);
            } else {
                response.setStatus(HttpServletResponse.SC_NOT_FOUND); // Or another relevant code
            }

            out.write("{\"message\": \"" + resultMessage + "\"}");


        } catch (Exception e) {
            e.printStackTrace();
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().write("Failed to delete privilege");
        }
    }
}

// app name: Tomcat: PahanaEduBackEnd
