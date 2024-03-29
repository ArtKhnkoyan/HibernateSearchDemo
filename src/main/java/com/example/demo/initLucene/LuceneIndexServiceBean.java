package com.example.demo.initLucene;

import org.hibernate.search.jpa.FullTextEntityManager;
import org.hibernate.search.jpa.Search;

import javax.persistence.EntityManagerFactory;

public class LuceneIndexServiceBean {

    private FullTextEntityManager fullTextEntityManager;

    public LuceneIndexServiceBean(EntityManagerFactory entityManagerFactory) {
        fullTextEntityManager = Search.getFullTextEntityManager(entityManagerFactory.createEntityManager());
    }

    void triggerIndexing() {
        try {
            fullTextEntityManager.createIndexer().startAndWait();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}