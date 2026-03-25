package com.bookstore.bookstore.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bookstore.bookstore.model.Order;
import com.bookstore.bookstore.service.OrderService;

@RestController
@RequestMapping("/api/orders")
public class OrderController {
    private final OrderService service;

    public OrderController(OrderService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<?> createOrder(
            Authentication auth, 
            @RequestBody Map<String, Object> request) {
        
        String userEmail = auth.getName();
        @SuppressWarnings("unchecked")
        List<Long> bookIds = (List<Long>) request.get("bookIds");
        @SuppressWarnings("unchecked")
        List<Integer> quantities = (List<Integer>) request.get("quantities");
        
        Order order = service.createOrder(userEmail, bookIds, quantities);
        Double total = order.getTotalAmount() != null ? order.getTotalAmount() : 0.0;
        
        Map<String, Object> response = new HashMap<>();  // ✅ NO ERROR
        response.put("message", "Order created!");
        response.put("orderId", order.getId());
        response.put("total", total);
        response.put("status", order.getStatus());
        
        return ResponseEntity.ok(response);
    }

    @GetMapping("/my-orders")
    public ResponseEntity<List<Order>> getMyOrders(Authentication auth) {
        String userEmail = auth.getName();
        return ResponseEntity.ok(service.getCustomerOrders(userEmail));
    }

    @GetMapping
    public ResponseEntity<List<Order>> getAllOrders() {
        return ResponseEntity.ok(service.getAllOrders());
    }
}