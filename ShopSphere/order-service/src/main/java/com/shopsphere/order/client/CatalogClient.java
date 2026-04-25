package com.shopsphere.order.client;

import com.shopsphere.order.dto.CatalogProductDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "CATALOG-SERVICE", path = "/catalog")
public interface CatalogClient {

    @GetMapping("/products/{id}")
    CatalogProductDto getProduct(@PathVariable("id") Long id);

    @PutMapping("/products/{id}/stock")
    void updateStock(@PathVariable("id") Long id, @RequestParam("stockQty") Integer stockQty);
}
