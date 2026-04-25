package com.shopsphere.order.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/orders")
@Tag(name = "Order (stub)", description = "Order and checkout endpoints — full flow planned for Phase 2")
public class OrderController {

    @PostMapping("/checkout/start")
    @Operation(summary = "Start checkout (stub)")
    public ResponseEntity<?> startCheckout() {
        return stub("start checkout");
    }

    @PutMapping("/checkout/address")
    @Operation(summary = "Save delivery address (stub)")
    public ResponseEntity<?> saveAddress() {
        return stub("save address");
    }

    @PutMapping("/checkout/delivery")
    @Operation(summary = "Select delivery method (stub)")
    public ResponseEntity<?> selectDelivery() {
        return stub("select delivery");
    }

    @PostMapping("/payment")
    @Operation(summary = "Process payment (stub)")
    public ResponseEntity<?> processPayment() {
        return stub("process payment");
    }

    @PostMapping("/place")
    @Operation(summary = "Place order (stub)")
    public ResponseEntity<?> placeOrder() {
        return stub("place order");
    }

    @GetMapping("/my")
    @Operation(summary = "Customer order history (stub)")
    public ResponseEntity<?> myOrders() {
        return stub("my orders");
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get order detail (stub)")
    public ResponseEntity<?> getOrder(@PathVariable Long id) {
        return stub("order " + id);
    }

    @GetMapping("/admin")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "[Admin] All orders (stub)")
    public ResponseEntity<?> adminAll() {
        return stub("admin all orders");
    }

    @PutMapping("/admin/{id}/status")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "[Admin] Update order status (stub)")
    public ResponseEntity<?> adminUpdateStatus(@PathVariable Long id) {
        return stub("admin update status for order " + id);
    }

    private ResponseEntity<?> stub(String action) {
        return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED)
                .body(Map.of("status", 501, "message", action + " — not yet implemented"));
    }
}
