package controller.sale;

import com.fasterxml.jackson.databind.ObjectMapper;
import dao.implementations.SaleDAOImpl;
import dao.implementations.SaleItemsDAOImpl;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import models.sale.Sale;
import models.sale.SaleItems;
import services.saleService.SaleItemService;
import services.saleService.SaleService;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@WebServlet("/sales/*")
public class SaleServlet extends HttpServlet {

    SaleDAOImpl saleDAOImpl = new SaleDAOImpl();
    SaleItemsDAOImpl saleItemsDAOImpl = new SaleItemsDAOImpl();

    private final SaleService saleService = new SaleService(saleDAOImpl, saleItemsDAOImpl);
    private final SaleItemService saleItemService = new SaleItemService(saleItemsDAOImpl);
    private final ObjectMapper objectMapper = new ObjectMapper();

    // POST http://localhost:8080/PahanaEduBackEnd/sales
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        try {
            Sale sale = objectMapper.readValue(request.getReader(), Sale.class);
            saleService.createSale(sale);

            String json = "{ \"message\": \"Sale created successfully\", \"saleId\": " + sale.getId() + " }";
            response.setStatus(HttpServletResponse.SC_CREATED);
            response.getWriter().write(json);
        } catch (Exception ex) {
            ex.printStackTrace();
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().write("{\"error\": \"Server error: " + ex.getMessage().replace("\"", "\\\"") + "\"}");
        }
    }

    // GET http://localhost:8080/PahanaEduBackEnd/sales (get all)
    // GET http://localhost:8080/PahanaEduBackEnd/sales/{id} (get by id)
    // GET http://localhost:8080/PahanaEduBackEnd/sales/count (get count)
    // GET http://localhost:8080/PahanaEduBackEnd/sales/customer/{customerId} (by customer)
    // GET http://localhost:8080/PahanaEduBackEnd/sales/user/{userId} (by user)
    // GET http://localhost:8080/PahanaEduBackEnd/sales/date-range?start=DATE&end=DATE (by date range)
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        String pathInfo = request.getPathInfo();

        try {
            if (pathInfo == null || pathInfo.equals("/")) {
                // Get all sales
                List<Sale> allSales = saleService.getAllSales();
                response.getWriter().write(objectMapper.writeValueAsString(allSales));
            }
            else if (pathInfo.equals("/count")) {
                // Get sale count
                int saleCount = saleService.getSaleCount();
                response.getWriter().write("{\"count\": " + saleCount + "}");
            }
            else if (pathInfo.startsWith("/customer/")) {
                // Get by customer ID
                int customerId = Integer.parseInt(pathInfo.substring(10));
                List<Sale> sales = saleService.getSalesByCustomerId(customerId);
                response.getWriter().write(objectMapper.writeValueAsString(sales));
            }
            else if (pathInfo.startsWith("/user/")) {
                // Get by user ID
                int userId = Integer.parseInt(pathInfo.substring(6));
                List<Sale> sales = saleService.getSalesByUserId(userId);
                response.getWriter().write(objectMapper.writeValueAsString(sales));
            }
            else if (pathInfo.equals("/date-range")) {
                // Get by date range
                String startDateParam = request.getParameter("start");
                String endDateParam = request.getParameter("end");

                LocalDateTime startDate = LocalDateTime.parse(startDateParam);
                LocalDateTime endDate = LocalDateTime.parse(endDateParam);

                List<Sale> sales = saleService.getSalesBetweenDates(startDate, endDate);
                objectMapper.writeValue(response.getWriter(), sales);
            }
            else {
                // Get by sale ID (default case)
                int id = Integer.parseInt(pathInfo.substring(1));
                Sale sale = saleService.getSaleById(id);
                response.getWriter().write(objectMapper.writeValueAsString(sale));
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().write("{\"error\": \"Server error: " + ex.getMessage().replace("\"", "\\\"") + "\"}");
        }
    }

    // PUT http://localhost:8080/PahanaEduBackEnd/sales/{id}
    protected void doPut(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        try {
            Sale sale = objectMapper.readValue(request.getReader(), Sale.class);
            saleService.updateSale(sale);
            response.getWriter().write("{\"message\": \"Sale updated successfully\"}");
        } catch (Exception ex) {
            ex.printStackTrace();
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().write("{\"error\": \"Server error: " + ex.getMessage().replace("\"", "\\\"") + "\"}");
        }
    }

    // DELETE http://localhost:8080/PahanaEduBackEnd/sales/{id}
    protected void doDelete(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        String pathInfo = request.getPathInfo();

        try {
            if (pathInfo == null || pathInfo.equals("/")) {
                throw new Exception("Sale ID is required for deletion.");
            }

            int id = Integer.parseInt(pathInfo.substring(1));
            saleService.deleteSale(id);

            response.setStatus(HttpServletResponse.SC_OK);
            response.getWriter().write("{\"message\": \"Sale deleted successfully\"}");
        } catch (Exception ex) {
            ex.printStackTrace();
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().write("{\"error\": \"Server error: " + ex.getMessage().replace("\"", "\\\"") + "\"}");
        }
    }
}