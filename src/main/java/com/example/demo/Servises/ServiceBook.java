package com.example.demo.Servises;

import com.example.demo.Entity.Book;
import com.example.demo.Entity.UserACCBook;
import com.example.demo.Repos.RepoBook;
import com.example.demo.Repos.UserACCBookRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Работа с книгами
 */
@Service
public class ServiceBook {
    @Autowired
    private RepoBook repoBook;
    @Autowired
    private UserACCBookRepo userACCBookRepo;

    /**
     * Возвращает список всех книг, либо результаты поиска по ключевому слову.
     *
     * @param keyword Ключевое слово для поиска.
     * @return Список книг, соответствующих ключевому слову.
     */

    public List<Book> listAll(String keyword){
        if(keyword!=null){
            return repoBook.search(keyword);
        }
        return repoBook.findAll();
    }
    /**
     * Возвращает список всех книг, отсортированных по названию в порядке возрастания.
     *
     * @return Список книг, отсортированных по названию (по возрастанию).
     */
    public List<Book> findAllSortedByTitleAsc() {
        return repoBook.findAll(Sort.by(Sort.Order.asc("title")));
    }
    /**
     * Возвращает список всех книг, отсортированных по названию в порядке убывания.
     *
     * @return Список книг, отсортированных по названию (по убыванию).
     */
    public List<Book> findAllSortedByTitleDesc() {
        return repoBook.findAll(Sort.by(Sort.Order.desc("title")));
    }
    /**
     * Возвращает список всех книг, отсортированных по автору в порядке возрастания.
     *
     * @return Список книг, отсортированных по автору (по возрастанию).
     */
    public List<Book> findAllSortedByAuthorAsc() {
        return repoBook.findAll(Sort.by(Sort.Order.asc("author")));
    }
    /**
     * Возвращает список всех книг, отсортированных по автору в порядке убывания.
     *
     * @return Список книг, отсортированных по автору (по убыванию).
     */
    public List<Book> findAllSortedByAuthorDesc() {
        return repoBook.findAll(Sort.by(Sort.Order.desc("author")));
    }
    /**
     * Возвращает список всех книг, отсортированных по году издания в порядке возрастания.
     *
     * @return Список книг, отсортированных по году издания (по возрастанию).
     */
    public List<Book> findAllSortedByYearAsc() {
        return repoBook.findAll(Sort.by(Sort.Order.asc("year")));
    }
    /**
     * Возвращает список всех книг, отсортированных по году издания в порядке убывания.
     *
     * @return Список книг, отсортированных по году издания (по убыванию).
     */
    public List<Book> findAllSortedByYearDesc() {
        return repoBook.findAll(Sort.by(Sort.Order.desc("year")));
    }
    /**
     * Возвращает все книги без сортировки.
     *
     * @return Список всех книг.
     */
    public List<Book> findAll() {
        return repoBook.findAll();
    }
    /**
     * Сохраняет книгу в базе данных с установкой статуса "Свободно".
     *
     * @param book Книга, которую нужно сохранить.
     */
    public void saveBook(Book book){
        book.setStatus("Свободно");
        this.repoBook.save(book);
    }
    /**
     * Сохраняет книгу в базе данных без изменения ее статуса.
     *
     * @param book Книга, которую нужно сохранить.
     */
    public void saveBookUs(Book book){
        this.repoBook.save(book);
    }
    /**
     * Удаляет книгу по ID. Если книга связана с пользователем, удаление невозможно.
     *
     * @param id Идентификатор книги, которую нужно удалить.
     * @throws DataIntegrityViolationException если книга связана с пользователем.
     */
    public void deleteByIdBook(Integer id){
        UserACCBook userAccBook = userACCBookRepo.findByBookId(id);

        if (userAccBook != null) {
            if (!userAccBook.getStatus().name().equals("RETURN")) {
                throw new DataIntegrityViolationException("Книга связана с пользователем и не возвращена, удаление невозможно.");
            }
        }
        this.repoBook.deleteById(id);
    }
    /**
     * Находит книгу по ID.
     *
     * @param id Идентификатор книги.
     * @return Книга с заданным ID.
     */
    public Book findById(int id){
        return this.repoBook.findById(id).get();
    }
    /**
     * Выполняет поиск книг по названию или автору, используя переданный запрос.
     *
     * @param query Запрос для поиска.
     * @return Список книг, соответствующих запросу.
     */
    public List<Book> searchBooks(String query){
        System.out.println(repoBook.findByTitleContainingIgnoreCaseOrAuthorContainingIgnoreCase(query,query));
        return repoBook.findByTitleContainingIgnoreCaseOrAuthorContainingIgnoreCase(query,query);


    }
}
