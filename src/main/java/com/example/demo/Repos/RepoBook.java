package com.example.demo.Repos;

import com.example.demo.Entity.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * Репозиторий для работы с сущностью Book.
 * Предоставляет методы для поиска и работы с книгами.
 */

public interface RepoBook extends JpaRepository<Book, Integer> {
    /**
     * Поиск книг по ключевому слову.
     * Ищет книги по полям: id, author, genres, title, year, status, typeAgeAccount.
     *
     * @param keyword Ключевое слово для поиска.
     * @return Список книг, соответствующих запросу.
     */
    @Query("SELECT t FROM Book t WHERE concat(t.id, t.author, t.genres, t.title, t.year, t.status, t.typeAgeAccount) LIKE %?1%")
    public List<Book> search(String keyword);
    /**
     * Поиск всех книг.
     *
     * @return Список всех книг.
     */
     List<Book> findAll();
    /**
     * Поиск книг по названию или автору.
     *
     * @param title Название книги.
     * @param author Автор книги.
     * @return Список книг, содержащих указанные подстроки в названии или авторе.
     */
     List<Book> findByTitleContainingIgnoreCaseOrAuthorContainingIgnoreCase(String title, String author);

}