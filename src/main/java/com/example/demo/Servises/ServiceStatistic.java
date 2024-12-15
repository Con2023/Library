package com.example.demo.Servises;

import com.example.demo.Repos.AccountUserRepo;
import com.example.demo.Entity.Book;
import com.example.demo.Repos.RepoBook;
import com.example.demo.Repos.RepoUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Работает со статистикой пользоватетелей
 */

@Service
public class ServiceStatistic {

    @Autowired
    private RepoUser userRepository;

    @Autowired
    private RepoBook bookRepository;

    @Autowired
    private AccountUserRepo accountUserRepository;

    /**
     * Возвращает общее количество пользователей в базе данных.
     *
     * @return Общее количество пользователей.
     */

    public long getTotalUsers() {
        return userRepository.count();
    }
    /**
     * Возвращает общее количество книг в базе данных.
     *
     * @return Общее количество книг.
     */
    public long getTotalBooks() {
        return bookRepository.count();
    }
    /**
     * Возвращает общее количество аккаунтов пользователей.
     *
     * @return Общее количество аккаунтов.
     */
    public long getTotalAcc() {
        return accountUserRepository.count();
    }
    /**
     * Возвращает статистику по количеству книг для каждого типа аккаунта.
     * Например, количество книг для "Детского", "Подросткового" и "Взрослого" аккаунтов.
     *
     * @return Карта, где ключ — тип аккаунта, а значение — количество книг для данного типа.
     */
    public Map<String, Long> getBooksByAccountTypes() {
        List<Book> books = bookRepository.findAll();
        Map<String, Long> accountCounts = new HashMap<>();

        for (Book book : books) {
            String typeAgeAccount = book.getTypeAgeAccount();
            accountCounts.put(typeAgeAccount, accountCounts.getOrDefault(typeAgeAccount, 0L) + 1);
        }
        return accountCounts;
    }

}