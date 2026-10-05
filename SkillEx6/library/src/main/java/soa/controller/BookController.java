package soa.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import soa.model.Book;
import soa.service.BookService;

@RestController
@RequestMapping("/api/books")
public class BookController {

    BookService BS;

    public BookController(BookService BS) {
        this.BS = BS;
    }

    // 1. ADD BOOK
    @PostMapping
    public ResponseEntity<String> addBook(@RequestBody Book book) {
        String message = BS.addBook(book);
        return new ResponseEntity<>(message, HttpStatus.CREATED);
    }

    // 2. GET ALL BOOKS
    @GetMapping
    public ResponseEntity<List<Book>> getAllBooks() {
        List<Book> books = BS.getAllBooks();
        return ResponseEntity.ok(books);
    }

    // 3. GET BOOK BY ID
    @GetMapping("/{id}")
    public ResponseEntity<Book> getBookById(@PathVariable Long id) {
        Book book = BS.getBookById(id);
        return ResponseEntity.ok(book);
    }

    // 4. UPDATE BOOK
    @PutMapping("/{id}")
    public ResponseEntity<Book> updateBook(
            @PathVariable Long id,
            @RequestBody Book book) {

        Book updatedBook = BS.updateBook(id, book);
        return ResponseEntity.ok(updatedBook);
    }

    // 5. DELETE BOOK
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteBook(
            @PathVariable Long id) {

        BS.deleteBook(id);
        return ResponseEntity.ok("Book deleted successfully");
    }
}