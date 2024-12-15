package com.example.demo.Configuration;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

/**
 * Конфигурация для создания BCryptPasswordEncoder.
 * Этот класс предоставляет механизм для безопасного кодирования паролей.
 */
@Configuration
public class PasswordEncoderConfiguration {
    /**
     * Создаёт бин BCryptPasswordEncoder, который используется для кодирования паролей.
     *
     * @return объект BCryptPasswordEncoder, который используется для безопасного кодирования паролей.
     */
    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
