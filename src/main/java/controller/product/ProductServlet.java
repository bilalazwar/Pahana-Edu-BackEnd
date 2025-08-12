package controller.product;

import com.fasterxml.jackson.databind.ObjectMapper;
import dao.implementations.ProductDAOImpl;
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

@WebServlet("/products")
public class ProductServlet extends HttpServlet {

    ProductDAOImpl productDAOImpl = new ProductDAOImpl();
    private final ProductService productService = new ProductService(productDAOImpl);
    private final ObjectMapper objectMapper = new ObjectMapper(); // Jackson for JSON

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


    //    GET /products?action=getProductById  -- http://localhost:8080/PahanaEduBackEnd/products?action=getProductById&id=1
    //    GET /products?action=getProductByBarcode  -- http://localhost:8080/PahanaEduBackEnd/products?action=getProductByBarcode&barcode=1234567890123
    //    GET /products?action=getProductQuantity   -- http://localhost:8080/PahanaEduBackEnd/products?action=getProductQuantity&id=5
    //    GET /products?action=getAllProducts  -- http://localhost:8080/PahanaEduBackEnd/products?action=getAllProducts
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        String action = request.getParameter("action");     // e.g., "getProductQuantity"
        String idParam = request.getParameter("id");        // for product ID
        String barcode = request.getParameter("barcode");   // for finding by barcode

        try {
            if (action != null && !action.isEmpty()) {
                switch (action) {
                    case "getProductById":
                        if (idParam != null && !idParam.isEmpty()) {
                            int id = Integer.parseInt(idParam);
                            ProductDto product = productService.getProductById(id);
                            response.getWriter().write(objectMapper.writeValueAsString(product));
                        } else {
                            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Product ID is required.");
                        }
                        break;

                    case "getProductByBarcode":
                        if (barcode != null && !barcode.isEmpty()) {
                            ProductDto product = productService.getProductByBarcode(Long.parseLong(barcode));
                            response.getWriter().write(objectMapper.writeValueAsString(product));
                        } else {
                            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Barcode is required.");
                        }
                        break;

                    case "getProductQuantity":
                        if (idParam != null && !idParam.isEmpty()) {
                            int id = Integer.parseInt(idParam);
                            int quantity = productService.getProductQuantity(id);
                            response.getWriter().write("{\"quantity\": " + quantity + "}");
                        } else {
                            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Product ID is required.");
                        }
                        break;

                    case "getAllProducts":
                    default:
                        List<ProductDto> allProducts = productService.getAllProducts();
                        response.getWriter().write(objectMapper.writeValueAsString(allProducts));
                        break;
                }
            } else {
                // Default action if no `action` parameter is provided: return all products
                List<ProductDto> allProducts = productService.getAllProducts();
                response.getWriter().write(objectMapper.writeValueAsString(allProducts));
            }

        } catch (Exception ex) {
            ex.printStackTrace();
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().write("{\"error\": \"Server error: " + ex.getMessage().replace("\"", "\\\"") + "\"}");
        }
    }

//    PUT http://localhost:8080/PahanaEduBackEnd/products?action=updateProduct&id={}
//    PUT http://localhost:8080/PahanaEduBackEnd/products?action=updateProductQuantity&id={}&quantity={}
    @Override
    protected void doPut(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        String action = request.getParameter("action");     //  getProductQuantity,
        String idParam = request.getParameter("id");
        String qtyParam = request.getParameter("quantity");


        try {
            if (action != null && !action.isEmpty() && idParam != null && !idParam.isEmpty()) {
                switch (action) {
                    case "updateProduct":
                        Product product = objectMapper.readValue(request.getReader(), Product.class);
                        int id = Integer.parseInt(idParam);
                        productService.updateProduct(product, id);
                        response.setStatus(HttpServletResponse.SC_OK);
                        response.getWriter().write("{\"message\": \"Product details updated successfully\"}");
                        break;
                    case "updateProductQuantity":
                        productService.updateProductQuantity(Integer.parseInt(idParam), Integer.parseInt(qtyParam));
                        response.setStatus(HttpServletResponse.SC_OK);
                        response.getWriter().write("{\"message\": \"Product quantity updated successfully\"}");
                        break;
                    default:
                        response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                        response.getWriter().write("{\"error\": \"Unknown action: " + action + "\"}");
                        break;
                }
            }
            else {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                response.getWriter().write("{\"error\": \"Missing required parameters: action and id\"}");
            }
        }
        catch (Exception ex) {
            ex.printStackTrace();
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().write("{\"error\": \"Server error: " + ex.getMessage().replace("\"", "\\\"") + "\"}");
        }
    }

//  DELETE /   http://localhost:8080/PahanaEduBackEnd/products?id={}
    @Override
    protected void doDelete(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        String idParam = request.getParameter("id");

        try {
            if (idParam != null && !idParam.isEmpty()) {
                int id = Integer.parseInt(idParam);
                productService.deleteProduct(id);
                response.getWriter().write("{\"message\": \"Product deleted successfully\"}");
            }
        }
        catch (Exception ex) {
            ex.printStackTrace();
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().write("{\"error\": \"Server error: " + ex.getMessage().replace("\"", "\\\"") + "\"}");
        }
    }


}
