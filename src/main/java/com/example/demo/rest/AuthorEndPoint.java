package com.example.demo.rest;

import com.example.demo.model.Author;
import com.example.demo.model.Book;
import com.example.demo.repository.AuthorRepository;
import com.example.demo.repository.BookRepository;
import com.example.demo.searchService.AuthorSearch;
import lombok.AllArgsConstructor;
import org.hibernate.search.exception.EmptyQueryException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/authors")
@AllArgsConstructor
public class AuthorEndPoint {

    private AuthorRepository authorRepository;
    private BookRepository bookRepository;
    private AuthorSearch authorSearch;

    @PostMapping
    public ResponseEntity<Void> save(@RequestBody Author author) {
        authorRepository.save(author);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping
    public ResponseEntity<List<Author>> findAll() {
        return ResponseEntity.ok(authorRepository.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Author> findById(@PathVariable long id) {
        Optional<Author> findById = authorRepository.findById(id);
        if (!findById.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        return ResponseEntity.ok(findById.get());
    }

    @GetMapping("/{authorId}/books")
    public ResponseEntity<List<Book>> findBooksByAuthorId(@PathVariable long authorId) {
        return ResponseEntity.ok(bookRepository.findAllByAuthorId(authorId));
    }

    @GetMapping("/search_by_name_with_keyword")
    public List<Author> searchAllByNameWithKeyword(@RequestParam(value = "name", required = false) String name) {
        try {
            return authorSearch.searchAuthorWithKeyword("name", name);
        } catch (EmptyQueryException e) {
            return new ArrayList<>();
        }
    }

    @GetMapping("/search_by_name_with_fuzzy")
    public List<Author> searchAllByNameWithFuzzy(@RequestParam(value = "name", required = false) String name) {
        try {
            return authorSearch.searchAuthorWithFussy("name", name);
        } catch (EmptyQueryException e) {
            return new ArrayList<>();
        }
    }

    @GetMapping("/search_by_name_with_wilcard")
    public List<Author> searchAuthorWilcard(@RequestParam(value = "name", required = false) String name) {
        try {
            return authorSearch.searchAuthorWithWilcard("name", name);
        } catch (EmptyQueryException e) {
            return new ArrayList<>();
        }
    }

    @GetMapping("/search_by_name_with_phrase")
    public List<Author> searchAuthorPhrase(@RequestParam(value = "name", required = false) String name) {
        try {
            return authorSearch.searchAuthorWithPhrase("name", name);
        } catch (EmptyQueryException e) {
            return new ArrayList<>();
        }
    }

    @GetMapping("/search_by_name_with_more_like_this")
    public List<Object[]> searchAuthorPhrase(@RequestParam(value = "id", required = false) Long id) {
        try {
            return authorSearch.searchAuthorWithMoreLikeThis("name", id);
        } catch (EmptyQueryException e) {
            return new ArrayList<>();
        }
    }

    @GetMapping("/search_by_surname")
    public List<Author> searchAllBySurname(@RequestParam(value = "surname", required = false) String surname) {
        try {
            return authorSearch.searchAuthorWithWilcard("surname", surname);
        } catch (EmptyQueryException e) {
            return new ArrayList<>();
        }
    }

    @PutMapping
    public ResponseEntity update(@RequestBody Author author) {
        Optional<Author> repoAuthor = authorRepository.findById(author.getId());
        if (!repoAuthor.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        Optional<Author> mapAuthor = repoAuthor.map(a -> {
            a.setName(author.getName());
            a.setSurname(author.getSurname());
            a.setAge(author.getAge());
            return a;
        });
        authorRepository.save(mapAuthor.get());
        return ResponseEntity.ok().body("author is update");
    }

    @DeleteMapping
    public ResponseEntity delete(@RequestBody Author author) {
        Optional<Author> repoAuthor = authorRepository.findById(author.getId());
        if (!repoAuthor.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        authorRepository.delete(author);
        return ResponseEntity.ok().body("author is delete");
    }
}
