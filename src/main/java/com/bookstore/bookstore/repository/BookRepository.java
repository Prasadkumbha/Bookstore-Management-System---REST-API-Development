package com.bookstore.bookstore.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.bookstore.bookstore.model.Book;

public interface BookRepository extends JpaRepository<Book, Long> {
    
    // ADD THESE 2 LINES:
    Page<Book> findByTitleContainingIgnoreCase(String title, Pageable pageable);
    Page<Book> findByAuthorContainingIgnoreCase(String author, Pageable pageable);
}