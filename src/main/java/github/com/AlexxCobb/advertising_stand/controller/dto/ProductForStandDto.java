package github.com.AlexxCobb.advertising_stand.controller.dto;


import github.com.AlexxCobb.advertising_stand.controller.dto.enums.ProductType;

import java.math.BigDecimal;

public record ProductForStandDto(
        String publicProductId,
        String name,
        BigDecimal price,
        Integer purchaseCount,
        String brand,
        String color,
        String imagePath,
        Integer stockQuantity,
        ProductType type
) {
    public ProductForStandDto withUpdatedFields(ProductForStandDto dtoToUpdate) {
        return new ProductForStandDto(
                this.publicProductId(),
                dtoToUpdate.name(),
                dtoToUpdate.price(),
                this.purchaseCount(),
                dtoToUpdate.brand(),
                dtoToUpdate.color(),
                dtoToUpdate.imagePath(),
                dtoToUpdate.stockQuantity(),
                this.type()
        );
    }
}