package com.example.demo.Controllers;


import com.example.demo.Entity.UserAccount;
import com.example.demo.Repos.RepoUser;
import com.example.demo.Entity.User;
import com.example.demo.Entity.UserACCBook;
import com.example.demo.Repos.UserACCBookRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;

/**
 * Контроллер для отображения страницы пользователя и статистики.
 * Обрабатывает запросы на отображение страницы пользователя и статистики по книгам.
 */
@Controller
public class ReaderController {
    @Autowired
    private RepoUser repoUser;
    @Autowired
    private UserACCBookRepo userACCBookRepo;

    /**
     * Отображение страницы профиля пользователя.
     *
     * @return Страница профиля пользователя.
     */
    @GetMapping("/userPage")
    public String userPage() {
        return "userPage";
    }

    /**
     * Отображение статистики по книгам пользователя.
     * Показывает количество взятых книг, количество завершенных книг, среднее время на книгу.
     *
     * @param model Модель для передачи данных о статистике.
     * @return Страница с статистикой пользователя.
     */
    @RequestMapping("/userStatistic")
    public String getUserStatistics(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = repoUser.findUserByLogin(authentication.getName());
        UserAccount userAccount = user.getUserAccount();
        List<UserACCBook> listuserACCBook = userACCBookRepo.findBooksByUserAccountId(userAccount.getId());
        int booksTaken = listuserACCBook.size();
        long totalDurationInSeconds = 0;
        int completedBooksCount = 0;

        for (UserACCBook userACCBook : listuserACCBook) {
            if (userACCBook.getTakeDate() != null && userACCBook.getGiveDate() != null) {
                LocalDateTime takeDate = userACCBook.getTakeDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
                LocalDateTime giveDate = userACCBook.getGiveDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
                Duration duration = Duration.between(takeDate, giveDate);
                totalDurationInSeconds += duration.getSeconds();
                completedBooksCount++;
            }
        }
        double avgTime = 0;
        if (completedBooksCount > 0) {
            avgTime = totalDurationInSeconds / (double) completedBooksCount / 3600; // Среднее время в часах
        }
        long totalDuration = totalDurationInSeconds / 3600;
        model.addAttribute("completedBooksCount", completedBooksCount);
        model.addAttribute("booksTaken", booksTaken);
        model.addAttribute("avgTime", avgTime);
        model.addAttribute("totalDuration", totalDuration);

        return "userStatistic";
    }
}
