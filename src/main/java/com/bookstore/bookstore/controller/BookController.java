package com.bookstore.bookstore.controller;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.bookstore.bookstore.model.Book;
import com.bookstore.bookstore.service.BookService;

@RestController
@RequestMapping("/api/books")
public class BookController {

    private final BookService service;

    public BookController(BookService service){
        this.service = service;
    }
    
    // ✅ PAGINATION + SEARCH (NEW)
    @GetMapping
    public ResponseEntity<Page<Book>> getBooks(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String title,
            @RequestParam(required = false) String author) {
        
        Pageable pageable = PageRequest.of(page, size);
        
        if (title != null && !title.isEmpty()) {
            return ResponseEntity.ok(service.searchByTitle(title, pageable));
        }
        if (author != null && !author.isEmpty()) {
            return ResponseEntity.ok(service.searchByAuthor(author, pageable));
        }
        
        return ResponseEntity.ok(service.getAllBooks(pageable));
    }
    
    @PostMapping
    public Book addBook(@RequestBody Book book){
        return service.addBook(book);
    }
    
    @GetMapping("/{id}")
    public Book getBook(@PathVariable Long id){
        return service.getBookById(id);
    }
    
    // ✅ UPDATE (Document req)
    @PutMapping("/{id}")
    public Book updateBook(@PathVariable Long id, @RequestBody Book bookDetails){
        return service.updateBook(id, bookDetails);
    }
    
    @DeleteMapping("/{id}")
    public void deleteBook(@PathVariable Long id){
        service.deleteBook(id);
    }
}