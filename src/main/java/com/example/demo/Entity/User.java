package com.example.demo.Entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;


@Setter
@Entity
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Getter
    private Integer ID;

    @Getter
    private String name;

    @Getter
    private String lastname;

    @Getter
    @Column(unique = true)
    private String login;

    @Getter
    private String role;


    @Getter
    private String password;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "user")
    @Getter
    private UserAccount userAccount;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "user_party")
    @Getter
    private List<PartyUser> partyUsers;
}
