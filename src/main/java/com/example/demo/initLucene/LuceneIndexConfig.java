package com.example.demo.initLucene;

import org.springframework.context.annotation.Bean;

import javax.persistence.EntityManagerFactory;

public class LuceneIndexConfig {
 
    @Bean
    public LuceneIndexServiceBean luceneIndexServiceBean(EntityManagerFactory EntityManagerFactory){
        LuceneIndexServiceBean luceneIndexServiceBean = new LuceneIndexServiceBean(EntityManagerFactory);
        luceneIndexServiceBean.triggerIndexing();
        return luceneIndexServiceBean;
    }
}