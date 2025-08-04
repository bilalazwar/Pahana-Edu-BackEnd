package controller.rolePrivilegeController;

import com.fasterxml.jackson.databind.ObjectMapper;
import dao.implementations.PrivilegeDAOImpl;
import dao.interfaces.PrivilegeDAO;
import dtos.PrivilegeDTO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import mapper.PrivilegeMapper;
import models.rolePrivilege.Privilege;
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


        } catch (Exception e) {

        }

    }


}

// app name: Tomcat: PahanaEduBackEnd
