package com.example.demo.Controllers;

import com.example.demo.Entity.UserAccount;
import com.example.demo.Entity.Book;
import com.example.demo.Servises.ServiceBook;
import com.example.demo.Repos.RepoUser;
import com.example.demo.Entity.User;
import com.example.demo.Servises.ServiceUserACCBook;
import com.example.demo.Entity.UserACCBook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Контроллер для управления действиями пользователя с книгами.
 * Обрабатывает запросы на просмотр, поиск, фильтрацию книг, а также заимствование книг пользователем.
 */
@Controller
public class ControllerUserACCBook {
    @Autowired
    private ServiceUserACCBook serviceUserACCBook;
    @Autowired
    private ServiceBook serviceBook;
    @Autowired
    private RepoUser repoUser;

    /**
     * Отображение списка книг с возможностью сортировки.
     * Сортирует книги по различным полям: названию, автору, году.
     *
     * @param sort  Параметр сортировки (название, автор, год).
     * @param order Направление сортировки (по возрастанию или убыванию).
     * @param model Модель для передачи списка книг в представление.
     * @return Страница с перечнем книг, отсортированных по заданным параметрам.
     */

    @RequestMapping("/take_book")
    public String takeBook(@RequestParam(required = false) String sort,
                           @RequestParam(required = false) String order,
                           Model model) {
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
        model.addAttribute("listBook", listBook);
        return "take_book";
    }

    /**
     * Обработка запроса на заимствование книги пользователем.
     * Устанавливает статус книги как "Занято" и создает запись о заимствовании книги.
     *
     * @param bookId Идентификатор книги, которую пользователь хочет взять.
     * @return Перенаправление на страницу "watch_account".
     */
    @RequestMapping(value = "/takeBookUser")
    public String takeBook(@RequestParam Integer bookId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = repoUser.findUserByLogin(authentication.getName());
        UserAccount userAccount = user.getUserAccount();
        Book book = serviceBook.findById(bookId);
        LocalDate localDate = LocalDate.now();
        Date date = Date.from(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
        UserACCBook userAccBook = new UserACCBook();
        userAccBook.setStatus(UserACCBook.RequestStatus.PENDING);
        userAccBook.setBook(book);
        userAccBook.setUserAccount(userAccount);
        userAccBook.setTakeDate(date);
        serviceUserACCBook.saveBookUser(userAccBook);
        book.setStatus("Занято");
        serviceBook.saveBookUs(book);
        return "redirect:/watch_account";
    }

    /**
     * Поиск книг по ключевому запросу.
     * Позволяет пользователю искать книги по названию, автору или другому критерию.
     *
     * @param query Ключевое слово для поиска книг.
     * @param model Модель для передачи результатов поиска.
     * @return Страница с результатами поиска книг.
     */
    @RequestMapping("/search_book")
    public String searchBooks(@RequestParam(value = "query", required = false) String query, Model model) {
        if (query != null && !query.trim().isEmpty()) {
            List<Book> books = serviceBook.searchBooks(query);
            for (Book book : books) {
                String genresString = String.join(", ", book.getGenres());
                book.setGenresString(genresString);  // добавляем строку жанров в объект книги
            }
            model.addAttribute("listSearchBook", books);
        } else {
            model.addAttribute("listSearchBook", new ArrayList<>());
        }
        return "search_book";
    }

    /**
     * Фильтрация книг по возрастным категориям.
     * Отображает книги в зависимости от типа учетной записи пользователя (например, для детей или взрослых).
     *
     * @param model Модель для передачи списка отфильтрованных книг.
     * @return Страница с фильтрацией книг по возрастным категориям.
     */
    @RequestMapping("/filter_age")
    public String filterAgeSearchBooks(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = repoUser.findUserByLogin(authentication.getName());
        UserAccount userAccount = user.getUserAccount();
        String typeAgeUser = userAccount.getAccount().getTypeAccount();
        List<Book> books = serviceBook.listAll(typeAgeUser);
        for (Book book : books) {
            String genresString = String.join(", ", book.getGenres());
            book.setGenresString(genresString);  // добавляем строку жанров в объект книги
        }
        model.addAttribute("listSearchAgeBook", books);
        return "filter_age";
    }

    /**
     * Просмотр всех книг, заимствованных текущим пользователем.
     *
     * @param model Модель для передачи списка заимствованных книг пользователя.
     * @return Страница с перечнем заимствованных книг пользователя.
     */
    @RequestMapping("/my_book")
    public String my_book(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = repoUser.findUserByLogin(authentication.getName());
        UserAccount userAccount = user.getUserAccount();
        List<Book> listUserSearchBook = serviceUserACCBook.searchUserBooks(userAccount);
        for (Book book : listUserSearchBook) {
            String genresString = String.join(", ", book.getGenres());
            book.setGenresString(genresString);  // добавляем строку жанров в объект книги
        }
        model.addAttribute("listUserSearchBook", listUserSearchBook);
        return "/my_book";
    }

    /**
     * Возврат книги пользователем.
     * Устанавливает статус книги как "Свободно" и обновляет запись о возврате.
     *
     * @param book_id Идентификатор книги, которую нужно вернуть.
     * @return Перенаправление на страницу с перечнем заимствованных книг.
     */
    @RequestMapping("/delete_user_book/{book_id}")
    public String delete_user_book(@PathVariable Integer book_id) {
        UserACCBook userACCBook = serviceUserACCBook.findLine(book_id);
        if (userACCBook != null) {
            LocalDate localDate = LocalDate.now();
            Date date = Date.from(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
            userACCBook.setGiveDate(date);
            Book book = serviceBook.findById(book_id);
            book.setStatus("Свободно");
            serviceBook.saveBookUs(book);
        }

        return "redirect:/my_book";
    }

}
