package com.example.demo.Entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;
import java.util.Date;
import java.util.List;

@Setter
@Entity
public class Party {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Getter
    private int id;
    @Getter
    private String name;
    @Getter
    private String description;
    @Temporal(TemporalType.DATE)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @Getter
    private Date date;
    @Getter
    private int ageLimit;
    @Getter
    private String imageUrl;


    @OneToMany(mappedBy = "party", cascade = CascadeType.ALL)
    private List<PartyUser> partyUsers;


}
