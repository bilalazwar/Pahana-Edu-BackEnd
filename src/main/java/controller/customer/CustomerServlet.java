package controller.customer;

import com.fasterxml.jackson.databind.ObjectMapper;
import dao.implementations.CustomerDAOImpl;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import models.customer.Customer;
import services.customerService.CustomerService;

import java.io.IOException;
import java.util.List;

@WebServlet("/customers")
public class CustomerServlet extends HttpServlet {

    CustomerDAOImpl  customerDAOImpl = new CustomerDAOImpl();
    private final CustomerService customerService = new CustomerService(customerDAOImpl);
    private final ObjectMapper objectMapper = new ObjectMapper(); // Jackson for JSON

//  POST/   http://localhost:8080/PahanaEduBackEnd/customers
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        try {
            customerService.addCustomer(objectMapper.readValue(request.getReader(), Customer.class));
            response.getWriter().write("{\"message\": \"Customer details added successfully\"}");
        }
        catch (Exception ex) {
            ex.printStackTrace();
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().write("{\"error\": \"Server error: " + ex.getMessage().replace("\"", "\\\"") + "\"}");
        }
    }


    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        String action = request.getParameter("action");     // Example
        String idParam = request.getParameter("id");

        try {
            if (action != null && !action.isEmpty()) {
                switch (action) {
                    case "getCustomerById":
                        if (idParam != null && !idParam.isEmpty()) {
                            int id = Integer.parseInt(idParam);
                            Customer customer = customerService.getCustomerById(id);
                            if (customer != null) {
                                response.getWriter().write(objectMapper.writeValueAsString(customer));
                            } else {
                                response.sendError(HttpServletResponse.SC_NOT_FOUND, "Customer not found with ID: " + id);
                            }
                        } else {
                            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Customer ID is required.");
                        }
                        break;
                    case "getAllCustomers":
                        List<Customer> customerList = customerService.getAllCustomer();
                        response.getWriter().write(objectMapper.writeValueAsString(customerList));
                        break;

                    default:
                        response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid action parameter.");
                        break;

                }
            }
        }
        catch (Exception ex) {
            ex.printStackTrace();
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().write("{\"error\": \"Server error: " + ex.getMessage().replace("\"", "\\\"") + "\"}");
        }
    }

    @Override
    protected void doPut(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        String idParam = request.getParameter("id");

        try {
            if(idParam != null && !idParam.isEmpty()) {
                int id = Integer.parseInt(idParam);
                customerService.updateCustomer(objectMapper.readValue(request.getReader(), Customer.class),id);
                response.getWriter().write("{\"message\": \"Customer details updated successfully\"}");
            }
        }
        catch (Exception ex) {
            ex.printStackTrace();
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().write("{\"error\": \"Server error: " + ex.getMessage().replace("\"", "\\\"") + "\"}");
        }
    }

    @Override
    protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        String idParam = request.getParameter("id");

        try {
            if(idParam != null && !idParam.isEmpty()) {
                int id = Integer.parseInt(idParam);
                customerService.deleteCustomer(id);
                response.getWriter().write("{\"message\": \"Customer deleted successfully\"}");
            }
        }
        catch (Exception ex) {
            ex.printStackTrace();
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().write("{\"error\": \"Server error: " + ex.getMessage().replace("\"", "\\\"") + "\"}");
        }
    }

}
