package com.bookstore.bookstore.service;

import java.util.List;
import java.util.stream.IntStream;

import org.springframework.stereotype.Service;

import com.bookstore.bookstore.model.Book;
import com.bookstore.bookstore.model.Order;
import com.bookstore.bookstore.model.OrderItem;
import com.bookstore.bookstore.model.User;
import com.bookstore.bookstore.repository.BookRepository;
import com.bookstore.bookstore.repository.OrderRepository;
import com.bookstore.bookstore.repository.UserRepository;

@Service
public class OrderService {
    private final OrderRepository orderRepo;
    private final BookRepository bookRepo;
    private final UserRepository userRepo;

    public OrderService(OrderRepository orderRepo, BookRepository bookRepo, UserRepository userRepo) {
        this.orderRepo = orderRepo;
        this.bookRepo = bookRepo;
        this.userRepo = userRepo;
    }

    public Order createOrder(String userEmail, List<Long> bookIds, List<Integer> quantities) {
        User user = userRepo.findByEmailIgnoreCase(userEmail)
            .orElseThrow(() -> new RuntimeException("User not found"));
        
        List<OrderItem> items = IntStream.range(0, bookIds.size())
            .mapToObj(i -> {
                Book book = bookRepo.findById(bookIds.get(i))
                    .orElseThrow(() -> new RuntimeException("Book not found"));
                return new OrderItem(null, book, quantities.get(i));
            }).toList();
        
        Order order = new Order(user, items);
        order.calculateTotal();
        return orderRepo.save(order);
    }

    public List<Order> getCustomerOrders(String userEmail) {
        return orderRepo.findByUserEmailIgnoreCaseOrderByOrderDateDesc(userEmail);
    }

    public List<Order> getAllOrders() {
        return orderRepo.findAllByOrderByOrderDateDesc();
    }
}