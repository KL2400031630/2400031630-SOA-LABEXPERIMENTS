package soa.service;

import java.util.List;

import org.springframework.stereotype.Service;

import soa.model.Book;
import soa.repo.BookRepo;

@Service
public class BookService {

    BookRepo BR;

    public BookService(BookRepo BR) {
        this.BR = BR;
    }

    public String addBook(Book book) {

        BR.save(book);

        return "Book Data Inserted Successfully";
    }

    public List<Book> getAllBooks() {

        return BR.findAll();
    }

    public Book getBookById(Long id) {

        return BR.findById(id).orElse(null);
    }

    public String deleteBook(Long id) {

        if (BR.existsById(id)) {

            BR.deleteById(id);

            return "Book Deleted Successfully";
        }

        return "Book Not Found";
    }

    public Book updateBook(Long id, Book book) {

        Book existingBook = BR.findById(id).orElse(null);

        if (existingBook != null) {

            existingBook.setTitle(book.getTitle());
            existingBook.setAuthor(book.getAuthor());
            existingBook.setIsbn(book.getIsbn());

            return BR.save(existingBook);
        }

        return existingBook;
    }
}