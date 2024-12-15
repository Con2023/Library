package com.example.demo.Repos;


import com.example.demo.Entity.Party;
import com.example.demo.Entity.PartyUser;
import com.example.demo.Entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * Репозиторий для работы с сущностью PartyUser.
 * Предоставляет методы для поиска и работы с участниками мероприятий.
 */
public interface PartyUserRepo extends JpaRepository<PartyUser, Integer> {
    /**
     * Поиск участников мероприятий по ключевому слову.
     * Ищет участников по полям: id, user, party.
     *
     * @param keyword Ключевое слово для поиска.
     * @return Список участников мероприятий, соответствующих запросу.
     */
    @Query("SELECT p FROM PartyUser p WHERE concat(p.id, p.user, p.party) LIKE %?1%")
    public List<PartyUser> search(String keyword);

    /**
     * Поиск всех участников мероприятия по id пользователя.
     *
     * @param id Идентификатор пользователя.
     * @return Список участников мероприятий, связанных с пользователем.
     */
    List<PartyUser> findAllByUser_ID(int id);
    /**
     * Поиск участника мероприятия по пользователю и мероприятию.
     *
     * @param user Пользователь.
     * @param party Мероприятие.
     * @return Участник мероприятия для указанного пользователя и мероприятия.
     */
    PartyUser findByUserAndParty(User user, Party party);



}