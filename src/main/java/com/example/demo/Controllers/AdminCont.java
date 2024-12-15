package com.example.demo.Controllers;

import com.example.demo.Servises.ServiceStatistic;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class AdminCont {

    /**
     * Отображение страницы администратора.
     *
     * @return Название представления для страницы администратора.
     */
    @RequestMapping("/adminPage")
    public String adminPage() {
        return "adminPage";
    }

    /**
     * Управление книгами для администратора.
     *
     * @return Название представления для управления книгами.
     */
    @RequestMapping("/manageBooks")
    public String manageBooks() {
        return "manageBooks";
    }

    /**
     * Отображение статистики для администратора.
     * Показывает общие данные: количество пользователей, книг, учетных записей и распределение книг по типам аккаунтов.
     *
     * @return Название представления для отображения статистики.
     */
    @RequestMapping("/adminStatistics")
    public String adminStatistics() {
        return "adminStatis";
    }
}