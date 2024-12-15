package com.example.demo.Repos;

import com.example.demo.Entity.UserACCBook;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * Репозиторий для работы с сущностью UserACCBook.
 * Предоставляет методы для работы с запросами на книги для пользователей.
 */
public interface UserACCBookRepo extends JpaRepository<UserACCBook, Integer> {
    /**
     * Поиск записей о книгах пользователей по ключевому слову.
     * Ищет по полям: id, book, userAccount, giveDate, takeDate.
     *
     * @param keyword Ключевое слово для поиска.
     * @return Список записей о книгах, соответствующих запросу.
     */
    @Query("SELECT s FROM UserACCBook s WHERE concat(s.id, s.book,s.userAccount, s.giveDate,s.takeDate ) LIKE %?1%")
    public List<UserACCBook> search(String keyword);

    /**
     * Поиск книг, взятых пользователем по id учетной записи.
     *
     * @param id Идентификатор учетной записи пользователя.
     * @return Список книг, взятых пользователем.
     */

    List<UserACCBook> findBooksByUserAccountId(Integer id);
    /**
     * Поиск записи по id книги.
     *
     * @param bookId Идентификатор книги.
     * @return Запись о книге, связанная с указанным id.
     */

    UserACCBook findByBookId(Integer bookId);






}