package com.shopsphere.order.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class CatalogProductDto {
    private Long id;
    private String name;
    private BigDecimal price;
    private Integer stockQty;
    private String imageUrl;
    private Boolean isActive;
}
