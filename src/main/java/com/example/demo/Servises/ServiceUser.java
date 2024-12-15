package com.example.demo.Servises;

import com.example.demo.Repos.RepoUser;
import com.example.demo.Entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class ServiceUser {
    @Autowired
    private RepoUser repo;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    /**
     * Сохраняет нового пользователя в базе данных, при этом пароль пользователя хешируется.
     * Роль пользователя по умолчанию устанавливается как "USER".
     *
     * @param user Объект пользователя, которого необходимо сохранить.
     */

    public void saveUser(User user){
        String hashedPassword = passwordEncoder.encode(user.getPassword());
        user.setPassword(hashedPassword);
        user.setRole("USER");
        this.repo.save(user);
    }
    /**
     * Сохраняет нового администратора в базе данных, при этом пароль администратора хешируется.
     * Роль пользователя устанавливается как "ADMIN".
     *
     * @param user Объект администратора, которого необходимо сохранить.
     */
    public void saveAdmin(User user){
        String hashedPassword = passwordEncoder.encode(user.getPassword());
        user.setPassword(hashedPassword);
        user.setRole("ADMIN");
        this.repo.save(user);
    }
    /**
     * Находит пользователя по его логину.
     *
     * @param login Логин пользователя.
     * @return Пользователь, найденный по логину.
     */
    public User findUserByLogin(String login){
        return repo.findUserByLogin(login);
    }


    /**
     * Сбрасывает пароль пользователя по его логину на новый пароль.
     *
     * @param login Логин пользователя, чей пароль нужно сбросить.
     * @param newPassword Новый пароль.
     * @return true, если пароль был успешно сброшен.
     * @throws IllegalArgumentException если пользователь с таким логином не найден.
     */

    public boolean resetPassword(String login, String newPassword) {
        User user = repo.findByLogin(login)
                .orElseThrow(() -> new IllegalArgumentException("Пользователь с таким логином не найден"));
        user.setPassword(passwordEncoder.encode(newPassword));
        repo.save(user);
        return true;
    }

}