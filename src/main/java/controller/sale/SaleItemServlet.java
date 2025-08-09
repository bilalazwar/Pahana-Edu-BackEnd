package controller.sale;

import com.fasterxml.jackson.databind.ObjectMapper;
import dao.implementations.SaleItemsDAOImpl;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import models.sale.SaleItems;
import services.saleService.SaleItemService;

import java.io.IOException;
import java.util.List;

@WebServlet("/saleItems")
public class SaleItemServlet extends HttpServlet {

    SaleItemsDAOImpl saleItemsDAOImpl = new SaleItemsDAOImpl();
    private final SaleItemService saleItemService = new SaleItemService(saleItemsDAOImpl);
    private final ObjectMapper objectMapper = new ObjectMapper();

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        try {
            // Convert JSON request body into a SaleItems object
            SaleItems saleItem = objectMapper.readValue(request.getReader(), SaleItems.class);

            saleItemService.addSaleItem(saleItem);

            response.setStatus(HttpServletResponse.SC_CREATED);
            response.getWriter().write("{\"message\": \"Sale added successfully\"}");
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

            response.getWriter().write("{\"message\": \"Customer details added successfully\"}");
        }
        catch (Exception ex) {
            ex.printStackTrace();
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().write("{\"error\": \"Server error: " + ex.getMessage().replace("\"", "\\\"") + "\"}");
        }

    }

    // ONLY Get sale items by saleId
    protected void doPut(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        try {
            String saleIdParam = request.getParameter("saleId");
            String saleItemIdParam = request.getParameter("saleItemId");

            if (saleIdParam != null) {

                int saleId = Integer.parseInt(saleIdParam);
                List<SaleItems> items = saleItemService.getSaleItemsBySaleId(saleId);
                objectMapper.writeValue(response.getWriter(), items);
            }
            if (saleItemIdParam != null) {

                int saleItemId = Integer.parseInt(saleItemIdParam);
                SaleItems item = saleItemService.getSaleItemById(saleItemId);
                objectMapper.writeValue(response.getWriter(), item);
            }
            else {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                response.getWriter().write("{\"error\": \"Missing required parameter: saleId\"}");
            }

            response.getWriter().write("{\"message\": \"Customer details added successfully\"}");
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

            response.getWriter().write("{\"message\": \"Customer details added successfully\"}");
        }
        catch (Exception ex) {
            ex.printStackTrace();
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().write("{\"error\": \"Server error: " + ex.getMessage().replace("\"", "\\\"") + "\"}");
        }

    }
}
