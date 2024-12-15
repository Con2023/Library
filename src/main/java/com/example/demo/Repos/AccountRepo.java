package com.example.demo.Repos;

import com.example.demo.Entity.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
/**
 * Репозиторий для работы с сущностью Account.
 * Предоставляет методы для поиска и работы с аккаунтами.
 */
public interface AccountRepo extends JpaRepository<Account, Integer> {
    /**
     * Поиск аккаунтов по ключевому слову.
     * Ищет аккаунты по полям: id и typeAccount.
     *
     * @param keyword Ключевое слово для поиска.
     * @return Список аккаунтов, соответствующих запросу.
     */
    @Query("SELECT a FROM Account a WHERE concat(a.id, a.typeAccount) LIKE %?1%")
    public List<Account> search(String keyword);

    /**
     * Поиск аккаунта по типу аккаунта.
     *
     * @param typeAccount Тип аккаунта (например, "Детский", "Подростковый", "Взрослый").
     * @return Найденный аккаунт с указанным типом.
     */
    Account findBytypeAccount(String typeAccount);

}




