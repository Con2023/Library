package com.example.demo.Entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Setter
@Entity
public class UserACCBook {

    @Id
    @Getter
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @Getter
    @JoinColumn(name = "book_id", referencedColumnName = "id")
    private Book book;

    @ManyToOne
    @Getter
    @JoinColumn(name = "user_account_id", referencedColumnName = "id")
    private UserAccount userAccount;

    @Column
    @Getter
    private Date takeDate;

    @Column
    @Getter
    private Date giveDate;

    @Getter
    @Enumerated(EnumType.STRING)
    private RequestStatus status;

    public enum RequestStatus {
        PENDING, APPROVED, REJECTED, RETURN
    }
}
