package controller.product;

import com.fasterxml.jackson.databind.ObjectMapper;
import dao.implementations.ProductDAOImpl;
import dao.interfaces.ProductDAO;
import dtos.ProductDto;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import models.product.Product;
import services.productService.ProductService;

import java.io.IOException;
import java.util.List;

@WebServlet("/products/*")
public class ProductServlet extends HttpServlet {

    ProductService productService;
    private ObjectMapper objectMapper; // Jackson for JSON

    @Override
    public void init() throws ServletException {
        ProductDAO productDAO = new ProductDAOImpl();
        productService = new ProductService(productDAO);
        objectMapper = new ObjectMapper();
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        try {
            productService.addProduct(objectMapper.readValue(request.getReader(), Product.class));
            response.getWriter().write("{\"message\": \"Product details added successfully\"}");
        }
        catch (Exception ex) {
            ex.printStackTrace();
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().write("{\"error\": \"Server error: " + ex.getMessage().replace("\"", "\\\"") + "\"}");
        }
    }

    // GET http://localhost:8080/PahanaEduBackEnd/products (get all)
    // GET http://localhost:8080/PahanaEduBackEnd/products/{id} (get by id)
    // GET http://localhost:8080/PahanaEduBackEnd/products/barcode/{barcode} (get by barcode)
    // GET http://localhost:8080/PahanaEduBackEnd/products/{id}/quantity (get quantity)
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        String pathInfo = request.getPathInfo();

        try {
            if (pathInfo == null || pathInfo.equals("/")) {
                // Handle GET all products
                List<ProductDto> allProducts = productService.getAllProducts();
                response.getWriter().write(objectMapper.writeValueAsString(allProducts));
            }
            else if (pathInfo.startsWith("/barcode/")) {
                // Handle GET by barcode
                String barcode = pathInfo.substring(9); // Extract barcode after "/barcode/"
                ProductDto product = productService.getProductByBarcode(Long.parseLong(barcode));
                response.getWriter().write(objectMapper.writeValueAsString(product));
            }
            else if (pathInfo.endsWith("/quantity")) {
                // Handle GET quantity
                String idStr = pathInfo.substring(1, pathInfo.indexOf("/quantity"));
                int quantity = productService.getProductQuantity(Integer.parseInt(idStr));
                response.getWriter().write("{\"quantity\": " + quantity + "}");
            }
            else {
                // Handle GET by ID (default case)
                String idStr = pathInfo.substring(1); // Remove leading slash
                ProductDto product = productService.getProductById(Integer.parseInt(idStr));
                response.getWriter().write(objectMapper.writeValueAsString(product));
            }

        } catch (NumberFormatException e) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid ID or barcode format");
        } catch (Exception ex) {
            ex.printStackTrace();
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().write("{\"error\": \"Server error: " + ex.getMessage().replace("\"", "\\\"") + "\"}");
        }
    }

    // PUT http://localhost:8080/PahanaEduBackEnd/products/{id} (full update)
    // PUT http://localhost:8080/PahanaEduBackEnd/products/{id}/quantity?value={} (quantity update)
    @Override
    protected void doPut(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        String pathInfo = request.getPathInfo();

        try {
            if (pathInfo != null && !pathInfo.isEmpty()) {
                if (pathInfo.endsWith("/quantity")) {
                    // Handle quantity update
                    String idStr = pathInfo.substring(1, pathInfo.indexOf("/quantity"));
                    String qtyParam = request.getParameter("value");
                    productService.updateProductQuantity(Integer.parseInt(idStr), Integer.parseInt(qtyParam));
                    response.getWriter().write("{\"message\": \"Product quantity updated successfully\"}");
                } else {
                    // Handle full product update
                    String idStr = pathInfo.substring(1);
                    Product product = objectMapper.readValue(request.getReader(), Product.class);
                    productService.updateProduct(product, Integer.parseInt(idStr));
                    response.getWriter().write("{\"message\": \"Product details updated successfully\"}");
                }
            }
            else {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                response.getWriter().write("{\"error\": \"Product ID is required\"}");
            }
        }
        catch (Exception ex) {
            ex.printStackTrace();
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().write("{\"error\": \"Server error: " + ex.getMessage().replace("\"", "\\\"") + "\"}");
        }
    }

    // DELETE http://localhost:8080/PahanaEduBackEnd/products/{id}
    @Override
    protected void doDelete(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        String pathInfo = request.getPathInfo();

        try {
            if (pathInfo != null && !pathInfo.isEmpty()) {
                String idStr = pathInfo.substring(1);
                productService.deleteProduct(Integer.parseInt(idStr));
                response.getWriter().write("{\"message\": \"Product deleted successfully\"}");
            }
            else {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                response.getWriter().write("{\"error\": \"Product ID is required\"}");
            }
        }
        catch (Exception ex) {
            ex.printStackTrace();
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().write("{\"error\": \"Server error: " + ex.getMessage().replace("\"", "\\\"") + "\"}");
        }
    }
}