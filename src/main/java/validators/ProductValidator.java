package validators;

import models.product.Product;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class ProductValidator {

    public static List<String> validate(Product product) {

        List<String> errors = new ArrayList<>();

        if(product == null){
            errors.add("Product name is Empty.");
        }

        if (isNullOrEmpty(product.getName())) {
            errors.add("Product name is required.");
        }

        if (isNullOrEmpty(product.getType())) {
            errors.add("Product type is required.");
        }

        if (product.getPrice() < 0) {
            errors.add("Price cannot be negative.");
        }

        if (product.getDiscount() < 0 || product.getDiscount() > 100) {
            errors.add("Discount must be between 0 and 100.");
        }

        if (product.getQuantity() < 0) {
            errors.add("Quantity cannot be negative.");
        }

        if (product.getCostPrice() < 0) {
            errors.add("Cost price cannot be negative.");
        }

        // Barcode (optional but if present must be positive)
        if (product.getBarcode() != null && product.getBarcode() <= 0) {
            errors.add("Barcode must be a positive number.");
        }

        // ISBN validation (if provided)
        if (!isNullOrEmpty(product.getIsbn()) && !isValidISBN(product.getIsbn())) {
            errors.add("Invalid ISBN format.");
        }

        // Author/publisher checks for Books
        if ("Book".equalsIgnoreCase(product.getType())) {
            if (isNullOrEmpty(product.getAuthor())) {
                errors.add("Author is required for books.");
            }
            if (isNullOrEmpty(product.getPublisher())) {
                errors.add("Publisher is required for books.");
            }
        }

        // Date validations
        LocalDateTime now = LocalDateTime.now();

        if (product.getCreatedAt() != null && product.getCreatedAt().isAfter(now)) {
            errors.add("Created date cannot be in the future.");
        }

        if (product.getCreatedAt() != null && product.getUpdatedAt() != null) {
            if (product.getUpdatedAt().isBefore(product.getCreatedAt())) {
                errors.add("Updated date cannot be before created date.");
            }
        }
        if (isNullOrEmpty(product.getCreatedBy())) {
            errors.add("Created by is required.");
        }
        if (product.getName().length() > 255) {
            errors.add("Product name cannot exceed 255 characters.");
        }

        if (product.getDiscount() > 0 && product.getPrice() == 0) {
            errors.add("Cannot apply discount to a free product.");
        }

        if (product.getCostPrice() > product.getPrice()) {
            errors.add("Cost price cannot be greater than selling price.");
        }

        return errors;
    }
    private static boolean isNullOrEmpty(String s) {
        return s == null || s.trim().isEmpty();
    }

    private static boolean isValidISBN(String isbn) {
        isbn = isbn.replaceAll("-", "").trim();
        return isbn.matches("\\d{10}|\\d{13}");
    }
}
