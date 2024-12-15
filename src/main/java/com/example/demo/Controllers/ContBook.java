package com.example.demo.Controllers;

import com.example.demo.Entity.Book;
import com.example.demo.Servises.ServiceBook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

/**
 * Контроллер для работы с книгами.
 * Обрабатывает запросы на добавление, редактирование, удаление книг.
 */

@Controller
public class ContBook {

    @Autowired
    private ServiceBook serviceBook;

    /**
     * Отображение страницы для добавления новой книги.
     * @return Имя шаблона для страницы добавления книги.
     */
    @RequestMapping("/addBook")
    public String newBook() {
        return "addBook";
    }
    /**
     * Сохранение новой книги.
     * Обрабатывает POST-запрос на добавление книги и сохраняет её в базе данных.
     * @param book Книга, которую нужно сохранить.
     * @return Редирект на страницу управления книгами после успешного сохранения.
     */
    @RequestMapping(value = "/saveB", method = RequestMethod.POST)
    public String saveBook(@ModelAttribute("book") Book book) {
        serviceBook.saveBook(book);
        return "redirect:/manageBooks";
    }

    /**
     * Удаление книги по её ID.
     * Обрабатывает запрос на удаление книги и выполняет операцию удаления.
     * В случае ошибки при удалении (например, нарушение целостности данных) перенаправляет на страницу ошибки.
     * @param id Идентификатор книги, которую нужно удалить.
     * @return Редирект на страницу управления книгами или страницу ошибки в случае исключения.
     */
    @RequestMapping("/delete_book/{id}")
    public String deleteBook(@PathVariable Integer id) {
        try {
            serviceBook.deleteByIdBook(id);
            return "redirect:/manageBooks";
        } catch (DataIntegrityViolationException e) {
            return "manage_books_error";
        }
    }
    /**
     * Отображение страницы редактирования книги.
     * Загружает информацию о книге по её ID и передаёт её в модель для отображения на странице.
     * @param id Идентификатор книги, которую нужно отредактировать.
     * @param model Модель для передачи данных на страницу.
     * @return Имя шаблона для страницы редактирования книги.
     */

    @RequestMapping("/edit_book/{id}")
    public String editBook(@PathVariable Integer id, Model model) {
        Book book = serviceBook.findById(id);

        model.addAttribute("book", book);
        return "edit";
    }
}