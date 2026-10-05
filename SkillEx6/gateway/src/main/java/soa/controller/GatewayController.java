package soa.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import soa.service.GatewayService;

@RestController
@RequestMapping("/gateway/books")
public class GatewayController {

    GatewayService GS;

    public GatewayController(GatewayService GS) {
        this.GS = GS;
    }

    @PostMapping
    public ResponseEntity<String> addBook(@RequestBody String book) {
        return GS.addBook(book);
    }

    @GetMapping
    public ResponseEntity<String> getAllBook() {
        return GS.getAllBooks();
    }

    @GetMapping("/{id}")
    public ResponseEntity<String> getBookById(@PathVariable Long id) {
        return GS.getBookById(id);
    }

    @PutMapping("/{id}")
    public ResponseEntity<String> updateBook(
            @PathVariable Long id,
            @RequestBody String book) {
        return GS.updateBook(id, book);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteBook(@PathVariable Long id) {
        return GS.deleteBook(id);
    }
}