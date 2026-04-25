package com.shopsphere.admin.controller;

import com.shopsphere.admin.dto.DashboardDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/admin")
@Tag(name = "Admin (stub)", description = "Admin dashboard, reports, and management endpoints — full aggregation planned for Phase 2")
public class DashboardController {

    @GetMapping("/dashboard")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "[Admin] Aggregated dashboard metrics (sample data)")
    public ResponseEntity<DashboardDto> dashboard() {
        Map<String, Long> byStatus = new LinkedHashMap<>();
        byStatus.put("PAID", 23L);
        byStatus.put("PACKED", 45L);
        byStatus.put("SHIPPED", 120L);
        byStatus.put("DELIVERED", 620L);

        DashboardDto dto = DashboardDto.builder()
                .totalRevenue(new BigDecimal("125000.00"))
                .todayRevenue(new BigDecimal("4500.00"))
                .totalOrders(850L)
                .pendingOrders(23L)
                .totalUsers(1200L)
                .newUsersToday(14L)
                .totalProducts(340L)
                .lowStockProducts(8L)
                .ordersByStatus(byStatus)
                .revenueByMonth(List.of(
                        new DashboardDto.MonthlyRevenue("Jan", new BigDecimal("18000")),
                        new DashboardDto.MonthlyRevenue("Feb", new BigDecimal("22000")),
                        new DashboardDto.MonthlyRevenue("Mar", new BigDecimal("28500")),
                        new DashboardDto.MonthlyRevenue("Apr", new BigDecimal("31000"))))
                .build();
        return ResponseEntity.ok(dto);
    }

    @GetMapping("/reports/sales")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "[Admin] Sales report (stub)")
    public ResponseEntity<Map<String, Object>> salesReport() {
        return ResponseEntity.ok(Map.of("message", "sales report not yet implemented"));
    }

    @GetMapping("/reports/products")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "[Admin] Product performance report (stub)")
    public ResponseEntity<Map<String, Object>> productReport() {
        return ResponseEntity.ok(Map.of("message", "product report not yet implemented"));
    }
}
