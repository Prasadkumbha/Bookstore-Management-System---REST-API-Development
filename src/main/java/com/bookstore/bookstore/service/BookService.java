package com.bookstore.bookstore.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.bookstore.bookstore.model.Book;
import com.bookstore.bookstore.repository.BookRepository;

@Service
public class BookService {
    private final BookRepository repo;

    public BookService(BookRepository repo){
        this.repo = repo;
    }
    
    // OLD (keep for compatibility)
    public List<Book> getAllBooks(){
        return repo.findAll();
    }
    
    // ✅ NEW PAGINATED
    public Page<Book> getAllBooks(Pageable pageable){
        return repo.findAll(pageable);
    }
    
    public Book addBook(Book book){
        return repo.save(book);
    }
    
    public Book getBookById(Long id){
        return repo.findById(id).orElseThrow();
    }
    
    public void deleteBook(Long id){
        repo.deleteById(id);
    }
    
    // ✅ NEW SEARCH METHODS
    public Page<Book> searchByTitle(String title, Pageable pageable){
        return repo.findByTitleContainingIgnoreCase(title, pageable);
    }
    
    public Page<Book> searchByAuthor(String author, Pageable pageable){
        return repo.findByAuthorContainingIgnoreCase(author, pageable);
    }
    
    // ✅ UPDATE (Document req #1)
    public Book updateBook(Long id, Book bookDetails){
    Book book = getBookById(id);
    book.setTitle(bookDetails.getTitle());
    book.setAuthor(bookDetails.getAuthor());
    book.setPrice(bookDetails.getPrice());
    book.setStockQuantity(bookDetails.getStockQuantity());  // ✅ Changed from setStock
    book.setIsbn(bookDetails.getIsbn());
    book.setDescription(bookDetails.getDescription());
    return repo.save(book);
}
}