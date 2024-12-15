package com.example.demo.Repos;

import com.example.demo.Entity.UserAccount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

/**
 * Репозиторий для работы с сущностью UserAccount.
 * Предоставляет методы для поиска и работы с учетными записями пользователей.
 */


public interface AccountUserRepo extends JpaRepository<UserAccount, Integer> {

    /**
     * Поиск учетных записей пользователей по ключевому слову.
     * Ищет пользователей по всем их полям: id, account, nameAcc, createdDate и age.
     *
     * @param keyword Ключевое слово для поиска.
     * @return Список учетных записей пользователей, соответствующих запросу.
     */

    @Query("SELECT a FROM UserAccount a WHERE concat(a.id, a.account, a.nameAcc,a.createdDate, a.age) LIKE %?1%")
    public List<UserAccount> search(String keyword);

    /**
     * Удаление учетной записи пользователя по id.
     *
     * @param id Идентификатор учетной записи пользователя для удаления.
     */
    void deleteById(Integer id);
    /**
     * Поиск учетной записи пользователя по его id.
     *
     * @param id Идентификатор учетной записи пользователя.
     * @return Опциональная учетная запись пользователя.
     */
    Optional<UserAccount> findById(Integer id);


}