package com.example.demo.searchService;


import com.example.demo.model.Author;
import com.example.demo.model.Book;
import org.apache.lucene.search.Query;
import org.hibernate.search.jpa.FullTextEntityManager;
import org.hibernate.search.jpa.Search;
import org.hibernate.search.query.dsl.QueryBuilder;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import java.util.List;


@Repository
@Transactional
public class BookSearch {

    @PersistenceContext
    EntityManager entityManager;

    public List<Book> searchBook(String field, String title) {
        FullTextEntityManager fullTextEntityManager = Search.getFullTextEntityManager(entityManager);
        QueryBuilder queryBuilder = fullTextEntityManager.getSearchFactory().buildQueryBuilder().forEntity(Book.class).get();
        Query query = queryBuilder.
                keyword()
                .onFields(field)
                .matching(title)
                .createQuery();
        return fullTextEntityManager.createFullTextQuery(query, Book.class).getResultList();
    }

    public List<Book> searchBooksByAuthorName(String authorName) {
        FullTextEntityManager fullTextEntityManager = Search.getFullTextEntityManager(entityManager);
        QueryBuilder queryBuilder = fullTextEntityManager.getSearchFactory().buildQueryBuilder().forEntity(Book.class).get();
        Query query = queryBuilder.keyword().onFields("author.name").matching(authorName).createQuery();
        return fullTextEntityManager.createFullTextQuery(query, Book.class).getResultList();
    }
}
