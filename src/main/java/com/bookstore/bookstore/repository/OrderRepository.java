package com.bookstore.bookstore.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.bookstore.bookstore.model.Order;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
    
    // Customer orders
    List<Order> findByUserEmailIgnoreCaseOrderByOrderDateDesc(String email);
    
    // Admin - all orders
    List<Order> findAllByOrderByOrderDateDesc();
}