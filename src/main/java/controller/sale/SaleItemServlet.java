package controller.sale;

import com.fasterxml.jackson.databind.ObjectMapper;
import dao.implementations.ProductDAOImpl;
import dao.implementations.SaleItemsDAOImpl;
import dao.interfaces.ProductDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import models.sale.SaleItems;
import services.saleService.SaleItemService;

import java.io.IOException;
import java.util.List;

@WebServlet("/sale-items/*")
public class SaleItemServlet extends HttpServlet {

    private SaleItemService saleItemService;
    private ObjectMapper objectMapper;

    @Override
    public void init() throws ServletException {
        SaleItemsDAOImpl saleItemsDAOImpl = new SaleItemsDAOImpl();
        ProductDAO productDAO = new ProductDAOImpl();
        saleItemService = new SaleItemService(saleItemsDAOImpl, productDAO);
        objectMapper = new ObjectMapper();
    }

    // POST http://localhost:8080/PahanaEduBackEnd/sale-items
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        try {
            SaleItems saleItem = objectMapper.readValue(request.getReader(), SaleItems.class);
            saleItemService.addSaleItem(saleItem);

            response.setStatus(HttpServletResponse.SC_CREATED);
            response.getWriter().write("{\"message\": \"Sale item added successfully\"}");
        } catch (Exception ex) {
            ex.printStackTrace();
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().write("{\"error\": \"Server error: " + ex.getMessage().replace("\"", "\\\"") + "\"}");
        }
    }

    // GET http://localhost:8080/PahanaEduBackEnd/sale-items/sale/{saleId} (by sale)
    // GET http://localhost:8080/PahanaEduBackEnd/sale-items/{id} (single item)
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        String pathInfo = request.getPathInfo();

        try {
            if (pathInfo == null || pathInfo.equals("/")) {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                response.getWriter().write("{\"error\": \"Missing required parameter\"}");
                return;
            }

            if (pathInfo.startsWith("/sale/")) {
                // Get by sale ID
                int saleId = Integer.parseInt(pathInfo.substring(6));
                List<SaleItems> items = saleItemService.getSaleItemsBySaleId(saleId);
                objectMapper.writeValue(response.getWriter(), items);
            } else {
                // Get single item by ID
                int itemId = Integer.parseInt(pathInfo.substring(1));
                SaleItems item = saleItemService.getSaleItemById(itemId);
                objectMapper.writeValue(response.getWriter(), item);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().write("{\"error\": \"Server error: " + ex.getMessage().replace("\"", "\\\"") + "\"}");
        }
    }

    // PUT http://localhost:8080/PahanaEduBackEnd/sale-items/{id}
    protected void doPut(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

//        response.setContentType("application/json");
//        response.setCharacterEncoding("UTF-8");
//        String pathInfo = request.getPathInfo();
//
//        try {
//            if (pathInfo == null || pathInfo.equals("/")) {
//                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
//                response.getWriter().write("{\"error\": \"Missing item ID\"}");
//                return;
//            }
//
//            int itemId = Integer.parseInt(pathInfo.substring(1));
//            SaleItems saleItem = objectMapper.readValue(request.getReader(), SaleItems.class);
//            saleItem.setId(itemId); // Ensure ID matches path
//
//            saleItemService.updateSaleItem(saleItem);
//            response.getWriter().write("{\"message\": \"Sale item updated successfully\"}");
//        } catch (Exception ex) {
//            ex.printStackTrace();
//            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
//            response.getWriter().write("{\"error\": \"Server error: " + ex.getMessage().replace("\"", "\\\"") + "\"}");
//        }
    }

    // DELETE http://localhost:8080/PahanaEduBackEnd/sale-items/{id}
    protected void doDelete(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

//        response.setContentType("application/json");
//        response.setCharacterEncoding("UTF-8");
//        String pathInfo = request.getPathInfo();
//
//        try {
//            if (pathInfo == null || pathInfo.equals("/")) {
//                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
//                response.getWriter().write("{\"error\": \"Missing item ID\"}");
//                return;
//            }
//
//            int itemId = Integer.parseInt(pathInfo.substring(1));
//            saleItemService.deleteSaleItem(itemId);
//            response.getWriter().write("{\"message\": \"Sale item deleted successfully\"}");
//        } catch (Exception ex) {
//            ex.printStackTrace();
//            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
//            response.getWriter().write("{\"error\": \"Server error: " + ex.getMessage().replace("\"", "\\\"") + "\"}");
//        }
    }
}