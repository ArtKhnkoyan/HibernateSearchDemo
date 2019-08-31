package com.example.demo.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.search.annotations.*;
import org.hibernate.search.annotations.Index;

import javax.persistence.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Indexed
@Table(name = "author")
public class Author {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private long id;
    @Column

    @ContainedIn
    @Field(termVector = TermVector.YES)
    private String name;
    @Column
    @Field(termVector = TermVector.YES)
    private String surname;
    @Column
    @Field(termVector = TermVector.YES)
    private int age;
}
