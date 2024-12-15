package com.example.demo.RestControllers;

import com.example.demo.Entity.Book;
import com.example.demo.Servises.ServiceBook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/books")
public class BookRestController {

    @Autowired
    private ServiceBook serviceBook;

    /**
     * Получение списка всех книг с фильтрацией и сортировкой.
     * @param keyword Ключевое слово для поиска книг.
     * @param sort    Параметр для сортировки.
     * @param order   Направление сортировки.
     * @return Список книг в формате JSON.
     */
    @GetMapping
    public List<Book> getBooks(@RequestParam(required = false) String keyword,
                               @RequestParam(required = false) String sort,
                               @RequestParam(required = false) String order) {
        List<Book> listBook;
        if ("title".equals(sort)) {
            listBook = "desc".equals(order) ? serviceBook.findAllSortedByTitleDesc() : serviceBook.findAllSortedByTitleAsc();
        } else if ("author".equals(sort)) {
            listBook = "desc".equals(order) ? serviceBook.findAllSortedByAuthorDesc() : serviceBook.findAllSortedByAuthorAsc();
        } else if ("year".equals(sort)) {
            listBook = "desc".equals(order) ? serviceBook.findAllSortedByYearDesc() : serviceBook.findAllSortedByYearAsc();
        } else {
            listBook = serviceBook.findAll();
        }

        for (Book book : listBook) {
            String genresString = String.join(", ", book.getGenres());
            book.setGenresString(genresString);
        }

        return listBook;
    }

    /**
     * Получение информации о книге по ID.
     * @param id Идентификатор книги.
     * @return Книга в формате JSON.
     */
    @GetMapping("/{id}")
    public ResponseEntity<Book> getBook(@PathVariable Integer id) {
        Book book = serviceBook.findById(id);
        return ResponseEntity.ok(book);
    }

    /**
     * Добавление новой книги.
     * @param book Книга для добавления.
     * @return Ответ с добавленной книгой.
     */
    @PostMapping
    public ResponseEntity<Book> addBook(@RequestBody Book book) {
        serviceBook.saveBook(book);
        return ResponseEntity.status(HttpStatus.CREATED).body(book);
    }
}
