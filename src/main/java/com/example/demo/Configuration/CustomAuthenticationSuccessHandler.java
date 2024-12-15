package com.example.demo.Configuration;

import com.example.demo.Repos.RepoUser;
import com.example.demo.Entity.User;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * Пользовательский обработчик успешной аутентификации.
 * Перенаправляет пользователя на страницу в зависимости от его роли (ADMIN или USER).
 */
@Component
public class CustomAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    @Autowired
    private RepoUser repo;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) throws IOException {
        String login = authentication.getName();
        User user = repo.findUserByLogin(login);
        String role = user.getRole();

        if (role != null) {
            String redirectUrl;
            if (role.equals("ADMIN")) {
                redirectUrl = "/adminPage";
            } else {
                redirectUrl = "/userPage";
            }
            response.sendRedirect(redirectUrl);
        }
    }
}