package com.shopsphere.admin.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DashboardDto {
    private BigDecimal totalRevenue;
    private BigDecimal todayRevenue;
    private Long totalOrders;
    private Long pendingOrders;
    private Long totalUsers;
    private Long newUsersToday;
    private Long totalProducts;
    private Long lowStockProducts;
    private Map<String, Long> ordersByStatus;
    private List<MonthlyRevenue> revenueByMonth;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class MonthlyRevenue {
        private String month;
        private BigDecimal revenue;
    }
}
