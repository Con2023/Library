package com.example.demo.Controllers;


import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;


@Controller
public class AdminContccount {

    /**
     * Страница управления учетными записями с возможностью поиска.
     * @return Страница управления учетными записями.
     */
    @RequestMapping("/manageAccount")
    public String manageAccount() {
        return "manageAccount"; }

    /**
     * Отображение формы редактирования учетной записи.
     * @return Страница редактирования учетной записи.
     */
    @RequestMapping("/edit_account/{id}")
    public String editAccount() {
        return "edit_account";
    }
}
