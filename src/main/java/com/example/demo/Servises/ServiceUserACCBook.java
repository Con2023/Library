package com.example.demo.Servises;

import com.example.demo.Entity.UserAccount;
import com.example.demo.Entity.Book;
import com.example.demo.Entity.UserACCBook;
import com.example.demo.Repos.UserACCBookRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Класс для работы с абонементом пользователя и книгами.
 */
@Service
public class ServiceUserACCBook {
    @Autowired
    private UserACCBookRepo userACCBookRepo;

    /**
     * Сохраняет связь между пользователем и книгой в базе данных.
     *
     * @param userAccBook Объект связи между пользователем и книгой.
     */
    public void saveBookUser(UserACCBook userAccBook) {
        this.userACCBookRepo.save(userAccBook);
    }
    /**
     * Находит все книги, которые принадлежат указанному пользователю и находятся в статусе "Одобрено" и без даты возврата.
     *
     * @param userAccount Учетная запись пользователя.
     * @return Список книг, одобренных для пользователя и еще не возвращенных.
     */

    public List<Book> searchUserBooks(UserAccount userAccount){

        int idUserAccount=userAccount.getId();
        List<UserACCBook> listAccUserBook =  userACCBookRepo.findBooksByUserAccountId(idUserAccount);

        List<Book> books = new ArrayList<>();
        for(UserACCBook book:listAccUserBook){
            if(book.getGiveDate()==null && book.getStatus() == UserACCBook.RequestStatus.APPROVED ){
                books.add(book.getBook());
            }
    }
    return books;

}
    /**
     * Находит запись о книге по ее идентификатору.
     *
     * @param book_id Идентификатор книги.
     * @return Запись о книге в контексте пользователя.
     */
    public UserACCBook findLine(Integer book_id) {
        return userACCBookRepo.findByBookId(book_id);
    }
}
