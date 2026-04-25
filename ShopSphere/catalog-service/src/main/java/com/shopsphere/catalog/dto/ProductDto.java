package com.shopsphere.catalog.dto;

import com.shopsphere.catalog.entity.Product;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductDto {

    private Long id;

    @NotBlank
    @Size(max = 200)
    private String name;

    private String description;

    @NotNull
    @DecimalMin(value = "0.0", inclusive = false)
    private BigDecimal price;

    @DecimalMin("0.0")
    @DecimalMax("100.0")
    private BigDecimal discountPct;

    @NotNull
    @Min(0)
    private Integer stockQty;

    private String imageUrl;

    private Long categoryId;

    private String categoryName;

    private Boolean isFeatured;

    private Boolean isActive;

    public static ProductDto from(Product p) {
        return ProductDto.builder()
                .id(p.getId())
                .name(p.getName())
                .description(p.getDescription())
                .price(p.getPrice())
                .discountPct(p.getDiscountPct())
                .stockQty(p.getStockQty())
                .imageUrl(p.getImageUrl())
                .categoryId(p.getCategory() == null ? null : p.getCategory().getId())
                .categoryName(p.getCategory() == null ? null : p.getCategory().getName())
                .isFeatured(p.getIsFeatured())
                .isActive(p.getIsActive())
                .build();
    }
}
