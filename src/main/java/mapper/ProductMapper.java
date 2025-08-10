package mapper;

import dtos.ProductDto;
import models.product.Product;

import java.util.ArrayList;
import java.util.List;

public class ProductMapper {

    public static ProductDto toDto(Product product) {
        if (product == null) return null;

        ProductDto dto = new ProductDto();
        dto.setId(product.getId());
        dto.setName(product.getName());
        dto.setDescription(product.getDescription());
        dto.setType(product.getType());
        dto.setAuthor(product.getAuthor());
        dto.setPublisher(product.getPublisher());
        dto.setBarcode(product.getBarcode());
        dto.setIsbn(product.getIsbn());
        dto.setLanguage(product.getLanguage());
        dto.setPrice(product.getPrice());
        dto.setDiscount(product.getDiscount());
        dto.setQuantity(product.getQuantity());

        // ❌ Do NOT expose: costPrice, internalNotes, createdBy, isDeleted, etc.
        return dto;
    }

    public static List<ProductDto> toDTO(List<Product> products) {
        if (products == null) return null;

        List<ProductDto> dtoList = new ArrayList<>();

        for (Product product : products) {
            dtoList.add(toDto(product));
        }
        return dtoList;
    }

    public static Product toDomain(ProductDto productDto) {
        if (productDto == null) return null;

        Product product = new Product();
        product.setId(productDto.getId());
        product.setName(productDto.getName());
        product.setDescription(productDto.getDescription());
        product.setType(productDto.getType());
        product.setAuthor(productDto.getAuthor());
        product.setPublisher(productDto.getPublisher());
        product.setBarcode(productDto.getBarcode());
        product.setIsbn(productDto.getIsbn());
        product.setLanguage(productDto.getLanguage());
        product.setPrice(productDto.getPrice());
        product.setDiscount(productDto.getDiscount());
        product.setQuantity(productDto.getQuantity());

        // ✅ Set defaults or leave blank for internal fields
//        product.setActive(true);                     // Default to active
//        product.setDeleted(false);                   // Not deleted
//        product.setCostPrice(0.0);                   // Unknown at this stage
//        product.setInternalNotes(null);
//        product.setCreatedBy(null);
//        product.setCreatedAt(java.time.LocalDateTime.now());
//        product.setUpdatedAt(java.time.LocalDateTime.now());

        return product;
    }
}
