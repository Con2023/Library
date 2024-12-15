package com.example.demo.Controllers;

import com.example.demo.Repos.RepoUser;
import com.example.demo.Servises.ServiceUser;
import com.example.demo.Entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

/**
 * Контроллер для управления пользователями.
 * Обрабатывает запросы на регистрацию, вход, изменение пароля, а также создание администратора.
 */
@Controller
public class ControllerUser {
    @Autowired
    private ServiceUser serviceUser;

    /**
     * Отображение страницы входа.
     * Отображает форму для ввода имени пользователя и пароля.
     * В случае ошибки входа отображает сообщение об ошибке.
     *
     * @param model Модель для передачи атрибутов на страницу.
     * @param error Сообщение об ошибке, если оно есть.
     * @return Страница входа.
     */
    @RequestMapping("/login")
    public String login(Model model, String error) {
        model.addAttribute("user", new User());
        if (error != null) {
            model.addAttribute("errorMessage", "Неправильное имя пользователя или пароль");
        }
        return "login";
    }

    /**
     * Отображение страницы регистрации нового пользователя.
     *
     * @param model Модель для передачи нового объекта пользователя на страницу.
     * @return Страница регистрации пользователя.
     */
    @RequestMapping("/registration")
    public String newUser(Model model) {
        model.addAttribute("user", new User());
        return "registration";
    }

    /**
     * Отображение страницы "Обо мне" для пользователя.
     *
     * @return Страница с информацией о пользователе.
     */
    @RequestMapping("/aboutMe")
    public String aboutMe() {
        return "aboutMe";
    }

    /**
     * Обработка регистрации нового пользователя.
     * Проверяет уникальность логина и сохраняет нового пользователя, если логин свободен.
     *
     * @param user  Объект пользователя, который будет зарегистрирован.
     * @param model Модель для передачи сообщений об ошибках.
     * @return Перенаправление на страницу входа, если регистрация успешна, или возвращение на страницу регистрации с ошибкой.
     */
    @PostMapping("/saveUser")
    public String registerUser(@ModelAttribute("user") User user, Model model) {

        if (serviceUser.findUserByLogin(user.getLogin()) != null) {
            model.addAttribute("loginError", "Этот логин уже занят!");
            return "registration";
        } else {
            serviceUser.saveUser(user);
            return "redirect:/login";
        }


    }

    /**
     * Отображение страницы добавления нового администратора.
     *
     * @param model Модель для передачи нового объекта администратора.
     * @return Страница добавления администратора.
     */
    @RequestMapping("/add_admin")
    public String newAdmin(Model model) {
        model.addAttribute("admin", new User());
        return "add_admin";
    }

    /**
     * Сохранение нового администратора.
     * Создает администратора и сохраняет его в системе.
     *
     * @param user Объект администратора, который будет сохранен.
     * @return Перенаправление на страницу администрирования.
     */
    @RequestMapping(value = "/saveAdmin")
    public String saveAdmin(@ModelAttribute("admin") User user) {
        serviceUser.saveAdmin(user);
        return "redirect:/adminPage";
    }

    /**
     * Отображение страницы для восстановления пароля.
     *
     * @return Страница восстановления пароля.
     */
    @GetMapping("/forgot-password")
    public String showForgotPasswordPage() {
        return "forgot-password";
    }

    /**
     * Обработка запроса на восстановление пароля.
     * Если пользователь с указанным логином найден, перенаправляет на страницу сброса пароля.
     *
     * @param login Логин пользователя для восстановления пароля.
     * @param model Модель для передачи сообщений об ошибках.
     * @return Страница восстановления пароля или перенаправление на страницу сброса пароля.
     */
    @PostMapping("/forgot-password")
    public String processForgotPassword(@RequestParam("login") String login, Model model) {
        if (serviceUser.findUserByLogin(login) != null) {
            return "redirect:/reset-password?login=" + login;
        } else {
            model.addAttribute("error", "Пользователь с таким логином не найден");
            return "forgot-password";
        }
    }

    /**
     * Отображение страницы сброса пароля.
     * Позволяет пользователю ввести новый пароль для восстановления доступа.
     *
     * @param login Логин пользователя для сброса пароля.
     * @param model Модель для передачи логина в представление.
     * @return Страница для сброса пароля.
     */
    @GetMapping("/reset-password")
    public String showResetPasswordPage(@RequestParam("login") String login, Model model) {
        model.addAttribute("login", login);
        return "reset-password";
    }

    /**
     * Обработка сброса пароля пользователя.
     * Обновляет пароль пользователя в базе данных.
     *
     * @param login       Логин пользователя, чей пароль будет сброшен.
     * @param newPassword Новый пароль пользователя.
     * @return Перенаправление на страницу входа после успешного сброса пароля.
     */
    @PostMapping("/reset-password")
    public String resetPassword(@RequestParam("login") String login, @RequestParam("newPassword") String newPassword) {
        serviceUser.resetPassword(login, newPassword);
        return "redirect:/login";
    }
}

