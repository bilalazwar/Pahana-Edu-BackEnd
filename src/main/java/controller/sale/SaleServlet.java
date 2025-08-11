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

@WebServlet("/sales")
public class SaleServlet extends HttpServlet {

    SaleDAOImpl saleDAOImpl = new SaleDAOImpl();
    SaleItemsDAOImpl saleItemsDAOImpl = new SaleItemsDAOImpl();

    private final SaleService saleService = new SaleService(saleDAOImpl, saleItemsDAOImpl);
    private final SaleItemService saleItemService = new SaleItemService(saleItemsDAOImpl);

    private final ObjectMapper objectMapper = new ObjectMapper(); // Jackson for JSON


    // please add the calculate total sales part here
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        try {
            Sale sale = objectMapper.readValue(request.getReader(), Sale.class);

            // 1. Have to save the Sale with total_amount calculation
            BigDecimal totalAmount = BigDecimal.ZERO;
            List<SaleItems> saleItems= sale.getItems();

            for (SaleItems saleItems1 : saleItems) {
                totalAmount = saleItems1.getTotalPrice();
            }

            sale.setTotalAmount(totalAmount);
            saleService.createSale(sale);

            // 2. Save sale items
            if (sale.getItems() != null && !sale.getItems().isEmpty()) {
                for (SaleItems item : sale.getItems()) {
                    item.setSaleId(sale.getId()); //  Very important where giving the Sale ID
                    saleItemService.addSaleItem(item); // delegate to the other service
                }
            }

            String json = "{ \"message\": \"Sale created successfully\", \"saleId\": " + sale.getId() + " }";
            response.setStatus(HttpServletResponse.SC_CREATED);
            response.getWriter().write(json);
        }
        catch (Exception ex) {
            ex.printStackTrace();
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().write("{\"error\": \"Server error: " + ex.getMessage().replace("\"", "\\\"") + "\"}");
        }
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        try {
            String idParam = request.getParameter("id");
            String customerIdParam = request.getParameter("customerId");
            String userIdParam = request.getParameter("userId");
            String startDateParam = request.getParameter("startDate");
            String endDateParam = request.getParameter("endDate");
            String countParam = request.getParameter("count");


            if (countParam != null && countParam.equalsIgnoreCase("true")) {
                int saleCount = saleService.getSaleCount();
                response.getWriter().write("{\"count\": " + saleCount + "}");
            }
            else if (idParam != null) {

                int id = Integer.parseInt(idParam);
                Sale sale = saleService.getSaleById(id);
                response.getWriter().write(objectMapper.writeValueAsString(sale));
//                objectMapper.writeValue(response.getWriter(), sales);
            }
            else if (customerIdParam != null) {

                int customerId = Integer.parseInt(customerIdParam);
                List<Sale> sales = saleService.getSalesByCustomerId(customerId);
                response.getWriter().write(objectMapper.writeValueAsString(sales));
//                objectMapper.writeValue(response.getWriter(), sales);
            }
            else if (userIdParam != null) {

                int userId = Integer.parseInt(userIdParam);
                List<Sale> sales = saleService.getSalesByUserId(userId);
                response.getWriter().write(objectMapper.writeValueAsString(sales));
//                objectMapper.writeValue(response.getWriter(), sales);
            }
            else if (startDateParam != null && endDateParam != null) {
                // Example: 2025-08-01T00:00:00
                LocalDateTime startDate = LocalDateTime.parse(startDateParam);
                LocalDateTime endDate = LocalDateTime.parse(endDateParam);

                List<Sale> sales = saleService.getSalesBetweenDates(startDate, endDate);
                objectMapper.writeValue(response.getWriter(), sales);
            }
            else {

                List<Sale> allSales = saleService.getAllSales();
                response.getWriter().write(objectMapper.writeValueAsString(allSales));
//                objectMapper.writeValue(response.getWriter(), sales);
            }
        }
        catch (Exception ex) {
            ex.printStackTrace();
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().write("{\"error\": \"Server error: " + ex.getMessage().replace("\"", "\\\"") + "\"}");
        }


    }
    protected void doPut(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        try {

            Sale sale = objectMapper.readValue(request.getReader(), Sale.class);
            saleService.updateSale(sale);
            response.getWriter().write("{\"message\": \"Sale details added successfully\"}");
        }
        catch (Exception ex) {
            ex.printStackTrace();
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().write("{\"error\": \"Server error: " + ex.getMessage().replace("\"", "\\\"") + "\"}");
        }


    }
    protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        try {
            String idParam = request.getParameter("id");
            if (idParam == null) {
                throw new Exception("Sale ID is required for deletion.");
            }
//      Have to delete the SaleItems first before doing that
            int id = Integer.parseInt(idParam);
            saleService.deleteSale(id);

            response.setStatus(HttpServletResponse.SC_OK);
            response.getWriter().write("{\"message\": \"Customer details added successfully\"}");
        }
        catch (Exception ex) {
            ex.printStackTrace();
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().write("{\"error\": \"Server error: " + ex.getMessage().replace("\"", "\\\"") + "\"}");
        }


    }
}
