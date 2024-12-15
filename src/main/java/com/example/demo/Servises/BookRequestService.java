package com.example.demo.Servises;

import com.example.demo.Entity.UserAccount;
import com.example.demo.Entity.Book;
import com.example.demo.Entity.UserACCBook;
import com.example.demo.Repos.UserACCBookRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Сервис для работы с запросом на получение книг.
  */

@Service
public class BookRequestService {

    @Autowired
    private UserACCBookRepo userACCBookRepo;
    @Autowired
    private ServiceUserACCBook serviceUserACCBook;

    /**
     * Создаёт запрос на книгу для указанного пользователя.
     * Статус запроса устанавливается как PENDING.
     *
     * @param userAccount Пользователь, который делает запрос.
     * @param book Книга, на которую создается запрос.
     */
    public void createRequest(UserAccount userAccount, Book book) {
        UserACCBook request = new UserACCBook();
        request.setUserAccount(userAccount);
        request.setBook(book);
        request.setStatus(UserACCBook.RequestStatus.PENDING);
        serviceUserACCBook.saveBookUser(request);
    }

    /**
     * Возвращает список всех ожидающих (PENDING) запросов на книги.
     *
     * @return Список запросов на книги с статусом PENDING.
     */
    public List<UserACCBook> getAllRequests() {
        List<UserACCBook> requests = userACCBookRepo.findAll();
        List<UserACCBook> needRequests = new ArrayList<>();

        for(UserACCBook request:requests) {
            if (request.getStatus() == UserACCBook.RequestStatus.PENDING) {
                needRequests.add(request);
            }
        }
        return needRequests;
    }

    /**
     * Возвращает список всех отклонённых (REJECTED) запросов на книги.
     *
     * @return Список отклонённых запросов на книги.
     */
    public List<UserACCBook> getAllReturnRequests() {
        List<UserACCBook> requests = userACCBookRepo.findAll();
        List<UserACCBook> needRequests = new ArrayList<>();

        for (UserACCBook request : requests) {
            if (request.getStatus() == UserACCBook.RequestStatus.REJECTED) {
                needRequests.add(request);
            }
        }
        return needRequests;
    }

    /**
     * Одобряет запрос на книгу с указанным ID, устанавливает статус запроса как APPROVED
     * и добавляет дату получения книги.
     *
     * @param requestId Идентификатор запроса, который нужно одобрить.
     * @throws IllegalArgumentException если запрос с указанным ID не найден.
     */
    public void approveRequest(Integer requestId) {
        UserACCBook request = userACCBookRepo.findById(requestId).orElseThrow(() -> new IllegalArgumentException("Request not found"));
        request.setStatus(UserACCBook.RequestStatus.APPROVED);
        LocalDate localDate = LocalDate.now();
        Date date = Date.from(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
        request.setTakeDate(date);
        serviceUserACCBook.saveBookUser(request);
    }

    /**
     * Отклоняет запрос на книгу с указанным ID и устанавливает статус как REJECTED.
     *
     * @param requestId Идентификатор запроса, который нужно отклонить.
     */
    public void returnRequest(Integer requestId) {
        UserACCBook request = userACCBookRepo.findById(requestId).get();
        request.setStatus(UserACCBook.RequestStatus.REJECTED);
        LocalDate localDate = LocalDate.now();
        Date date = Date.from(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
        request.setGiveDate(date);
        userACCBookRepo.save(request);
    }
}