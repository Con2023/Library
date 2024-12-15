package com.example.demo.DTO;

import lombok.Getter;
import lombok.Setter;

import java.util.Map;

@Setter
public class AdminStatisticsResponse {
    @Getter
    private long totalUsers;
    @Getter
    private long totalBooks;
    @Getter
    private long totalAcc;
    @Getter
    private Map<String, Long> booksByAccountType;


    public AdminStatisticsResponse(long totalUsers, long totalBooks, long totalAcc, Map<String, Long> booksByAccountType) {
        this.totalUsers = totalUsers;
        this.totalBooks = totalBooks;
        this.totalAcc = totalAcc;
        this.booksByAccountType = booksByAccountType;
    }

}