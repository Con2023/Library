package com.example.demo.Entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Setter
@Entity
public class PartyUser {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Getter
    private int id;

    @ManyToOne
    @Getter
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @Getter
    @JoinColumn(name = "party_id")
    private Party party;

}
