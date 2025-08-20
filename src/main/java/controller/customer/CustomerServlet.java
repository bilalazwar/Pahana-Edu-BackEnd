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

@WebServlet("/customers/*")
public class CustomerServlet extends HttpServlet {


    private CustomerService customerService;
    private ObjectMapper objectMapper; // Jackson for JSON

    @Override
    public void init() throws ServletException {
        CustomerDAOImpl customerDAOImpl = new CustomerDAOImpl();
        customerService = new CustomerService(customerDAOImpl);
        objectMapper = new ObjectMapper();
    }

    // POST http://localhost:8080/PahanaEduBackEnd/customers
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        try {
            customerService.addCustomer(objectMapper.readValue(request.getReader(), Customer.class));
            response.setStatus(HttpServletResponse.SC_OK);
            response.getWriter().write("{\"message\": \"Customer details added successfully\"}");
        }
        catch (Exception ex) {
            ex.printStackTrace();
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().write("{\"error\": \"Server error: " + ex.getMessage().replace("\"", "\\\"") + "\"}");
        }
    }

    // GET http://localhost:8080/PahanaEduBackEnd/customers (get all)
    // GET http://localhost:8080/PahanaEduBackEnd/customers/{id} (get by id)
    // GET http://localhost:8080/PahanaEduBackEnd/customers?mobileNumber={} (get by mobile)
    // GET http://localhost:8080/PahanaEduBackEnd/customers/count   (return total registered customers)
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        String pathInfo = request.getPathInfo();
        String mobileNumberParam = request.getParameter("mobileNumber");

        try {
            if (pathInfo != null && !pathInfo.isEmpty()) {
                // Handle GET by ID (e.g., /customers/123)
                String[] pathParts = pathInfo.split("/");

                if (pathParts.length > 1 && "count".equals(pathParts[1])) {
                    // Handle GET customer count (e.g., /customers/count)
                    int count = customerService.getCustomerCount();
                    response.getWriter().write("{\"count\": " + count + "}");
                    return;
                }
                else if (pathParts.length > 1) {
                    int id = Integer.parseInt(pathParts[1]);
                    Customer customer = customerService.getCustomerById(id);
                    if (customer != null) {
                        response.getWriter().write(objectMapper.writeValueAsString(customer));
                    } else {
                        response.sendError(HttpServletResponse.SC_NOT_FOUND, "Customer not found with ID: " + id);
                    }
                }
            }
            else if (mobileNumberParam != null && !mobileNumberParam.isEmpty()) {
                // Handle GET by mobile number (e.g., /customers?mobileNumber=1234567890)
                Customer customer = customerService.getCustomerByMobileNumber(mobileNumberParam);
                if (customer != null) {
                    response.getWriter().write(objectMapper.writeValueAsString(customer));
                } else {
                    response.sendError(HttpServletResponse.SC_NOT_FOUND,
                            "Customer not found with Mobile Number: " + mobileNumberParam);
                }
            }
            else {
                // Handle GET all customers (e.g., /customers)
                List<Customer> customerList = customerService.getAllCustomer();
                response.getWriter().write(objectMapper.writeValueAsString(customerList));
            }
        }
        catch (NumberFormatException e) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid ID format");
        }
        catch (Exception ex) {
            ex.printStackTrace();
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().write("{\"error\": \"Server error: " + ex.getMessage().replace("\"", "\\\"") + "\"}");
        }
    }

    // PUT http://localhost:8080/PahanaEduBackEnd/customers/{id}
    @Override
    protected void doPut(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        String pathInfo = request.getPathInfo();

        try {
            if (pathInfo != null && !pathInfo.isEmpty()) {
                String[] pathParts = pathInfo.split("/");
                if (pathParts.length > 1) {
                    int id = Integer.parseInt(pathParts[1]);
                    customerService.updateCustomer(objectMapper.readValue(request.getReader(), Customer.class), id);
                    response.getWriter().write("{\"message\": \"Customer details updated successfully\"}");
                }
            } else {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Customer ID is required.");
            }
        }
        catch (Exception ex) {
            ex.printStackTrace();
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().write("{\"error\": \"Server error: " + ex.getMessage().replace("\"", "\\\"") + "\"}");
        }
    }

    // DELETE http://localhost:8080/PahanaEduBackEnd/customers/{id}
    @Override
    protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        String pathInfo = request.getPathInfo();

        try {
            if (pathInfo != null && !pathInfo.isEmpty()) {
                String[] pathParts = pathInfo.split("/");
                if (pathParts.length > 1) {
                    int id = Integer.parseInt(pathParts[1]);
                    customerService.deleteCustomer(id);
                    response.getWriter().write("{\"message\": \"Customer deleted successfully\"}");
                }
            } else {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Customer ID is required.");
            }
        }
        catch (Exception ex) {
            ex.printStackTrace();
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().write("{\"error\": \"Server error: " + ex.getMessage().replace("\"", "\\\"") + "\"}");
        }
    }
}