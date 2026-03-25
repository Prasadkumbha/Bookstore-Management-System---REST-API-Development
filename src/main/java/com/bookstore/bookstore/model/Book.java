package com.bookstore.bookstore.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Book {
    
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;

    private String title;
    private String author;
    private String genre;
    private String isbn;
    private double price;
    private String description;
    private int stockQuantity;  // ✅ @Data = getStockQuantity()/setStockQuantity()
    private String imageUrl;
    
    // DELETE manual setters - @Data creates them!
}