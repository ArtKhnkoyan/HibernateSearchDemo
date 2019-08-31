package com.example.demo.searchService;


import com.example.demo.model.Author;
import org.apache.lucene.search.Query;
import org.hibernate.search.engine.ProjectionConstants;
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
public class AuthorSearch {

    @PersistenceContext
    EntityManager entityManager;

    public List<Author> searchAuthorWithKeyword(String field, String authorName) {
        FullTextEntityManager fullTextEntityManager = Search.getFullTextEntityManager(entityManager);
        QueryBuilder queryBuilder = fullTextEntityManager.getSearchFactory().buildQueryBuilder().forEntity(Author.class).get();
        Query query = queryBuilder.keyword()
                .onFields(field)
                .matching(authorName + "*")
                .createQuery();
        return fullTextEntityManager.createFullTextQuery(query, Author.class).getResultList();
    }

    public List<Author> searchAuthorWithWilcard(String field, String authorName) {
        FullTextEntityManager fullTextEntityManager = Search.getFullTextEntityManager(entityManager);
        QueryBuilder queryBuilder = fullTextEntityManager.getSearchFactory().buildQueryBuilder().forEntity(Author.class).get();
        Query query = queryBuilder.keyword()
                .wildcard()
                .onFields(field)
                .matching(authorName)
                .createQuery();
        return fullTextEntityManager.createFullTextQuery(query, Author.class).getResultList();
    }

    public List<Author> searchAuthorWithFussy(String field, String authorName) {
        FullTextEntityManager fullTextEntityManager = Search.getFullTextEntityManager(entityManager);
        QueryBuilder queryBuilder = fullTextEntityManager.getSearchFactory().buildQueryBuilder().forEntity(Author.class).get();
        Query query = queryBuilder.keyword().
                fuzzy()
                .withEditDistanceUpTo(1)
                .withPrefixLength(1)
                .onFields(field)
                .matching(authorName)
                .createQuery();
        return fullTextEntityManager.createFullTextQuery(query, Author.class).getResultList();
    }

    public List<Author> searchAuthorWithPhrase(String field, String authorName) {
        FullTextEntityManager fullTextEntityManager = Search.getFullTextEntityManager(entityManager);
        QueryBuilder queryBuilder = fullTextEntityManager.getSearchFactory().buildQueryBuilder().forEntity(Author.class).get();
        Query query = queryBuilder
                .phrase()
                .withSlop(1)
                .onField(field)
                .sentence(authorName)
                .createQuery();
        return fullTextEntityManager.createFullTextQuery(query, Author.class).getResultList();
    }

    public List<Object[]> searchAuthorWithMoreLikeThis(String field,Long authorId) {
        FullTextEntityManager fullTextEntityManager = Search.getFullTextEntityManager(entityManager);
        QueryBuilder qb = fullTextEntityManager.getSearchFactory()
                .buildQueryBuilder()
                .forEntity(Author.class)
                .get();

        Query mltQuery = qb
                .moreLikeThis()
                .comparingField(field)
                .toEntityWithId(authorId)
                .createQuery();

        return (List<Object[]>) fullTextEntityManager
                .createFullTextQuery(mltQuery, Author.class)
                .setProjection(ProjectionConstants.THIS, ProjectionConstants.SCORE)
                .getResultList();
    }
}
