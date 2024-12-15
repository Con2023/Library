package com.example.demo.RestControllers;

import com.example.demo.DTO.AdminStatisticsResponse;
import com.example.demo.Servises.ServiceStatistic;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/admin")
public class AdminRestController {

    @Autowired
    private ServiceStatistic statisticsService;

    /**
     * Получение статистики для администратора.
     * Показывает общие данные: количество пользователей, книг, учетных записей и распределение книг по типам аккаунтов.
     *
     * @return Статистические данные в формате JSON.
     */
    @GetMapping("/statistics")
    public ResponseEntity<AdminStatisticsResponse> getStatistics() {
        long totalUsers = statisticsService.getTotalUsers();
        long totalBooks = statisticsService.getTotalBooks();
        long totalAcc = statisticsService.getTotalAcc();
        Map<String, Long> booksByAccountType = statisticsService.getBooksByAccountTypes();

        AdminStatisticsResponse response = new AdminStatisticsResponse(totalUsers, totalBooks, totalAcc, booksByAccountType);
        return ResponseEntity.ok(response);
    }

}