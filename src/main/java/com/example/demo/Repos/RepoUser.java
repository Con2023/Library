package com.example.demo.Repos;

import com.example.demo.Entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;
/**
 * Репозиторий для работы с сущностью User.
 * Предоставляет методы для поиска и работы с пользователями.
 */
public interface RepoUser extends JpaRepository <User, Integer>{
    /**
     * Поиск пользователей по ключевому слову.
     * Ищет пользователей по полям: id, name, role, login, password, lastname, userAccount.
     *
     * @param keyword Ключевое слово для поиска.
     * @return Список пользователей, соответствующих запросу.
     */
    @Query("SELECT t FROM User t WHERE concat(t.ID, t.name,t.role, t.login, t.password, t.lastname, t.userAccount) LIKE %?1%")
    public List<User> search(String keyword);
    /**
     * Поиск пользователя по id.
     *
     * @param id Идентификатор пользователя.
     * @return Опциональный пользователь.
     */
    Optional<User> findById(Integer id);
    /**
     * Поиск пользователя по логину.
     *
     * @param login Логин пользователя.
     * @return Найденный пользователь с указанным логином.
     */
    User findUserByLogin(String login);
    Optional<User> findByLogin(String login);


}