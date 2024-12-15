package com.example.demo.Entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;
import java.util.List;

@Setter
@Entity
public class UserAccount {

    @Id
    @Getter
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @OneToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @Getter
    @JoinColumn(name = "user_id", referencedColumnName = "ID", nullable = true, foreignKey = @ForeignKey(name = "FK_user_account_user"))
    private User user;

    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinColumn(name = "account_id", referencedColumnName = "id")
    @Getter
    private Account account;

    @Column
    @Getter
    private Date createdDate;

    @Column
    @Getter
    private String nameAcc;

    @Column
    @Getter
    private int age;

    @OneToMany(mappedBy = "userAccount", cascade = CascadeType.REMOVE)
    @Getter
    private List<UserACCBook> userACCBooks;
}
