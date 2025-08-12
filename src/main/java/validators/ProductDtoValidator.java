package validators;


import dtos.ProductDto;

import java.util.ArrayList;
import java.util.List;

public class ProductDtoValidator {

    public static List<String> validate(ProductDto dto) {
        List<String> errors = new ArrayList<>();

        if (dto == null) {
            errors.add("Product is Null.");
        }

        if (isNullOrEmpty(dto.getName())) {
            errors.add("Product name is required.");
        }

        if (isNullOrEmpty(dto.getType())) {
            errors.add("Product type is required.");
        }

        if (dto.getPrice() < 0) {
            errors.add("Price cannot be negative.");
        }

        if (dto.getDiscount() < 0 || dto.getDiscount() > 100) {
            errors.add("Discount must be between 0 and 100.");
        }

        if (dto.getQuantity() < 0) {
            errors.add("Quantity cannot be negative.");
        }

        if (dto.getCostPrice() < 0) {
            errors.add("Cost price cannot be negative.");
        }

        if (dto.getBarcode() != null && dto.getBarcode() <= 0) {
            errors.add("Barcode must be a positive number.");
        }

        if (!isNullOrEmpty(dto.getIsbn()) && !isValidISBN(dto.getIsbn())) {
            errors.add("Invalid ISBN format.");
        }

        // Book-specific validations
        if ("Book".equalsIgnoreCase(dto.getType())) {
            if (isNullOrEmpty(dto.getAuthor())) {
                errors.add("Author is required for books.");
            }
            if (isNullOrEmpty(dto.getPublisher())) {
                errors.add("Publisher is required for books.");
            }
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

