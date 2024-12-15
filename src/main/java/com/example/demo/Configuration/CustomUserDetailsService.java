package com.example.demo.Configuration;

import com.example.demo.Repos.RepoUser;
import com.example.demo.Entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Сервис для загрузки данных пользователя из базы данных.
 * Используется Spring Security для аутентификации пользователя.
 */
@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private RepoUser repoUser;

    /**
     * Загружает пользователя по логину.
     * Проверяет существование пользователя в базе данных и возвращает объект UserDetails для аутентификации.
     *
     * @param login Логин пользователя.
     * @return объект UserDetails, содержащий информацию о пользователе и его роли.
     * @throws UsernameNotFoundException если пользователь с таким логином не найден.
     */
    @Override
    public UserDetails loadUserByUsername(String login) throws UsernameNotFoundException {
        User user = repoUser.findUserByLogin(login);
        if (user == null) {
            throw new UsernameNotFoundException("Пользователь не найден");
        }
        List<GrantedAuthority> grantedAuthorities = new ArrayList<>();
        grantedAuthorities.add(new SimpleGrantedAuthority(user.getRole()));

        return new org.springframework.security.core.userdetails.User(
                user.getLogin(),
                user.getPassword(),
                grantedAuthorities
        );
    }

}