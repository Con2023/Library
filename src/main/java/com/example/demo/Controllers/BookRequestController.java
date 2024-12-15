package com.example.demo.Controllers;

import com.example.demo.Entity.UserAccount;
import com.example.demo.Entity.Book;
import com.example.demo.Servises.ServiceBook;
import com.example.demo.Servises.BookRequestService;
import com.example.demo.Repos.RepoUser;
import com.example.demo.Entity.User;
import com.example.demo.Entity.UserACCBook;
import com.example.demo.Repos.UserACCBookRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * Контроллер для обработки запросов пользователей на книги.
 * Обрабатывает запросы на заимствование книг, их возврат и подтверждение этих запросов.
 */
@Controller
public class BookRequestController {

    @Autowired
    private BookRequestService bookRequestService;
    @Autowired
    private ServiceBook serviceBook;
    @Autowired
    private RepoUser repoUser;

    @Autowired
    private UserACCBookRepo userACCBookRepo;

    /**
     * Отправка запроса на заимствование книги.
     * Изменяет статус книги на "В ожидании подтверждения" и создает запрос на заимствование книги.
     *
     * @param bookId Идентификатор книги для запроса.
     * @return Страница с продолжением запроса на книгу.
     */

    @RequestMapping(value = "/requestBook")
    public String requestBook(@RequestParam Integer bookId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = repoUser.findUserByLogin(authentication.getName());
        UserAccount userAccount = user.getUserAccount();
        Book book = serviceBook.findById(bookId);
        book.setStatus("В ожидании подтверждения");
        serviceBook.saveBookUs(book);
        bookRequestService.createRequest(userAccount, book);

        return "requestBookContinuePage";
    }

    /**
     * Редирект на страницу с запросами на книги.
     *
     * @return Перенаправление на страницу запросов на книги.
     */
    @RequestMapping(value = "/bookRequests")
    public String bookRequests() {
        return "redirect:/bookRequests";
    }

    /**
     * Просмотр всех запросов на книги.
     * Отображает список всех запросов на заимствование книг.
     *
     * @param model Модель для передачи списка запросов в представление.
     * @return Страница со списком всех запросов на книги.
     */
    @RequestMapping(value = "/requestList")
    public String viewBookRequests(Model model) {
        List<UserACCBook> requests = bookRequestService.getAllRequests();
        model.addAttribute("requests", requests);
        return "requestList";
    }

    /**
     * Подтверждение запроса на книгу.
     * Изменяет статус запроса на "одобрено" и меняет статус книги на "Занято".
     *
     * @param request_id Идентификатор запроса на книгу.
     * @return Перенаправление на страницу списка запросов.
     */
    @RequestMapping(value = "/approveRequest/{request_id}")
    public String approveRequest(@PathVariable Integer request_id) {
        bookRequestService.approveRequest(request_id);
        UserACCBook request = userACCBookRepo.findById(request_id).get();
        Book book = request.getBook();
        book.setStatus("Занято");
        serviceBook.saveBookUs(book);
        return "redirect:/requestList";
    }

    /**
     * Возврат книги пользователем.
     * Изменяет статус книги на "В ожидании подтверждения на возврат" и создает запрос на возврат книги.
     *
     * @param request_id Идентификатор запроса на возврат.
     * @return Страница с продолжением запроса на возврат книги.
     */
    @RequestMapping(value = "/returnBook/{request_id}")
    public String returnRequest(@PathVariable Integer request_id) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = repoUser.findUserByLogin(authentication.getName());
        UserAccount userAccount = user.getUserAccount();
        Book book = serviceBook.findById(request_id);
        book.setStatus("В ожидании подтверждения на возврат");
        serviceBook.saveBookUs(book);
        List<UserACCBook> requests = userACCBookRepo.findBooksByUserAccountId(userAccount.getId());
        if (!requests.isEmpty()) {
            UserACCBook request = requests.get(requests.size() - 1);
            System.out.println(request.getId());
            bookRequestService.returnRequest(request.getId());
        } else {
            System.out.println("Нет записей для этой книги");
        }

        return "returnBookContinuePage";
    }

    /**
     * Просмотр всех запросов на возврат книг.
     * Отображает список всех запросов на возврат книг.
     *
     * @param model Модель для передачи списка запросов на возврат в представление.
     * @return Страница со списком всех запросов на возврат.
     */
    @RequestMapping(value = "/returnRequests")
    public String viewBookReturnRequests(Model model) {
        List<UserACCBook> returnRequests = bookRequestService.getAllReturnRequests();
        model.addAttribute("returnRequests", returnRequests);
        return "returnRequests";
    }

    /**
     * Подтверждение возврата книги.
     * Изменяет статус запроса на возврат на "Возвращено" и меняет статус книги на "Свободно".
     *
     * @param request_id Идентификатор запроса на возврат.
     * @return Перенаправление на страницу списка запросов на возврат.
     */
    @RequestMapping(value = "/approveReturn/{request_id}")
    public String approveReturn(@PathVariable Integer request_id) {
        UserACCBook request = userACCBookRepo.findById(request_id).get();
        request.setStatus(UserACCBook.RequestStatus.RETURN);
        userACCBookRepo.save(request);
        Book book = request.getBook();
        book.setStatus("Свободно");
        serviceBook.saveBookUs(book);
        return "redirect:/returnRequests";
    }
}
