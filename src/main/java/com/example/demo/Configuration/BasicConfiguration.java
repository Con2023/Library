package com.example.demo.Configuration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.core.session.SessionRegistryImpl;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * Конфигурация безопасности приложения, определяет правила доступа
 */

@Configuration
public class BasicConfiguration {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http, AuthenticationSuccessHandler successHandler) throws Exception {
        http.csrf(csrf -> csrf.ignoringRequestMatchers("/adminPage", "/login", "/userPage", "/userPage**", "/adminPage**","/api/**"));

        http

                .authorizeHttpRequests(authz -> authz
                        .requestMatchers("/images/**", "/uploads/**", "/registration", "/aboutMe", "/saveUser", "/forgot-password", "/reset-password").permitAll() //, , "/saveAdmin"
                        .anyRequest().authenticated())
                .formLogin(form -> form
                        .loginPage("/login")
                        .failureUrl("/login?error=true")
                        .successHandler(successHandler)
                        .permitAll())
                .logout(logout -> logout.permitAll());


        return http.build();
    }

    @Value("${upload.dir}")
    private String uploadDir;

}
