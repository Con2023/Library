package com.example.demo.Controllers;


import com.example.demo.Servises.AccountUserService;
import com.example.demo.Entity.UserAccount;
import com.example.demo.Entity.Account;
import com.example.demo.Repos.AccountUserRepo;
import com.example.demo.Repos.RepoUser;
import com.example.demo.Entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

/**
 * Контроллер для управления учетными записями пользователей.
 * Обрабатывает запросы, связанные с отображением, созданием, редактированием и управлением учетными записями.
 */
@Controller
public class AccountUserController {
    @Autowired
    private AccountUserService accountUserService;
    @Autowired
    private RepoUser repoUser;

    /**
     * Отображение информации о текущей учетной записи пользователя.
     * Если у пользователя есть учетная запись, отображается ее информация, иначе — страница ошибки регистрации.
     *
     * @param model Модель для передачи данных в представление.
     * @return Название представления для отображения (страница с учетной записью или ошибка).
     */
    @RequestMapping("/watch_account")
    public String watchAccount(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = repoUser.findUserByLogin(authentication.getName());
        if (user.getUserAccount() != null) {
            UserAccount userAccount = user.getUserAccount();
            Account account = userAccount.getAccount();
            model.addAttribute("user", user);
            model.addAttribute("userAccount", userAccount);
            model.addAttribute("account", account);
            return "/watch_account";
        } else {
            return "/error_account_registration";
        }
    }

    /**
     * Отображение формы для создания новой учетной записи.
     *
     * @param model Модель для передачи данных в представление.
     * @return Название представления для отображения (форма создания учетной записи).
     */
    @RequestMapping("/create_account")
    public String newAccount(Model model) {
        model.addAttribute("userAccount", new UserAccount());
        return "/create_account";
    }

    /**
     * Сохранение новой учетной записи пользователя.
     * Если у пользователя уже есть учетная запись, перенаправляет на страницу с сообщением об ошибке.
     *
     * @param userAccount        Объект UserAccount для сохранения.
     * @param redirectAttributes Атрибуты для перенаправления с сообщением.
     * @return Перенаправление на страницу с пользователем.
     */
    @RequestMapping(value = "/saveAccount")
    public String saveAccount(@ModelAttribute("userAccount") UserAccount userAccount, RedirectAttributes redirectAttributes) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = repoUser.findUserByLogin(authentication.getName());
        if (user.getUserAccount() != null) {
            redirectAttributes.addFlashAttribute("message", "У вас уже есть активный абонемент.");
            return "redirect:/userPage";
        }
        accountUserService.saveAccount(userAccount, user);
        return "redirect:/userPage";
    }
}
