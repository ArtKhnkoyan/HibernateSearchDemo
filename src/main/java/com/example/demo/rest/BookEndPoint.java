package com.example.demo.rest;

import com.example.demo.model.Book;
import com.example.demo.repository.BookRepository;
import com.example.demo.searchService.BookSearch;
import lombok.AllArgsConstructor;
import org.hibernate.search.exception.EmptyQueryException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/books")
@AllArgsConstructor
public class BookEndPoint {

    private BookRepository bookRepository;
    private BookSearch bookSearch;

    @PostMapping
    public ResponseEntity<Void> save(@RequestBody Book book) {
        bookRepository.save(book);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping
    public ResponseEntity<List<Book>> findAll() {
        return ResponseEntity.ok(bookRepository.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Book> findById(@PathVariable long id) {
        Optional<Book> findById = bookRepository.findById(id);
        if (!findById.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        return ResponseEntity.ok(findById.get());
    }

    @GetMapping("/search")
    public List<Book> searchAllByNameWithKeyword(@RequestParam(value = "title", required = false) String title) {
        try {
            return bookSearch.searchBook("title", title);
        } catch (EmptyQueryException e) {
            return new ArrayList<>();
        }
    }

    @GetMapping("/search_by_author_name")
    public List<Book> searchBooksByAuthorName(@RequestParam(value = "name", required = false) String name) {
        try {
            return bookSearch.searchBooksByAuthorName(name);
        } catch (EmptyQueryException e) {
            return new ArrayList<>();
        }
    }

    @PutMapping
    public ResponseEntity update(@RequestBody Book book) {
        Optional<Book> repoABook = bookRepository.findById(book.getId());
        if (!repoABook.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        Optional<Book> mapBook = repoABook.map(b -> {
            b.setTitle(book.getTitle());
            b.setDescription(book.getDescription());
            b.setPrice(book.getPrice());
            b.setAuthor(book.getAuthor());
            return b;
        });
        bookRepository.save(mapBook.get());
        return ResponseEntity.ok().body("author is update");
    }

    @DeleteMapping
    public ResponseEntity delete(@RequestBody Book book) {
        Optional<Book> repoBook = bookRepository.findById(book.getId());
        if (!repoBook.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        bookRepository.delete(book);
        return ResponseEntity.ok().body("author is delete");
    }
}
