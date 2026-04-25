package com.shopsphere.order.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/orders/cart")
@Tag(name = "Cart (stub)", description = "Cart endpoints — full business logic planned for Phase 2")
public class CartController {

    @GetMapping
    @Operation(summary = "View cart (stub)")
    public ResponseEntity<?> getCart() {
        return stub("view cart");
    }

    @PostMapping("/items")
    @Operation(summary = "Add item to cart (stub)")
    public ResponseEntity<?> addItem() {
        return stub("add cart item");
    }

    @PutMapping("/items/{itemId}")
    @Operation(summary = "Update cart item quantity (stub)")
    public ResponseEntity<?> updateItem(@PathVariable Long itemId) {
        return stub("update cart item " + itemId);
    }

    @DeleteMapping("/items/{itemId}")
    @Operation(summary = "Remove cart item (stub)")
    public ResponseEntity<?> removeItem(@PathVariable Long itemId) {
        return stub("remove cart item " + itemId);
    }

    @DeleteMapping
    @Operation(summary = "Clear cart (stub)")
    public ResponseEntity<?> clearCart() {
        return stub("clear cart");
    }

    private ResponseEntity<?> stub(String action) {
        return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED)
                .body(Map.of("status", 501, "message", action + " — not yet implemented"));
    }
}
