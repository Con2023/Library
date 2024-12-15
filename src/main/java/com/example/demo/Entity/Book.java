package com.example.demo.Entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Entity
public class Book {
    @Getter
    private Integer id;
    @Getter
    private String title;
    @Getter
    private String author;
    @Getter
    private String status;
    @Getter
    private String typeAgeAccount;
    @Getter
    private int year;
    @Getter
    private String genresString;
    @Getter
    @ElementCollection
    private List<String> genres;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Integer getId() {
        return id;
    }

    @OneToMany(mappedBy = "book")
    private List<UserACCBook> userACCBooks;
}
