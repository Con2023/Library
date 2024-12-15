package com.example.demo.Repos;

import com.example.demo.Entity.Party;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
/**
 * Репозиторий для работы с сущностью Party.
 * Предоставляет методы для поиска и работы с мероприятиями.
 */
public interface PartyRepo extends JpaRepository<Party, Integer> {
    /**
     * Поиск мероприятий по ключевому слову.
     * Ищет мероприятия по полям: id, name, description, partyUsers, date, ageLimit.
     *
     * @param keyword Ключевое слово для поиска.
     * @return Список мероприятий, соответствующих запросу.
     */
    @Query("SELECT p FROM Party p WHERE concat(p.id, p.name, p.description,p.date, p.ageLimit) LIKE %?1%")
    public List<Party> search(String keyword);
    /**
     * Поиск мероприятия по id.
     *
     * @param id Идентификатор мероприятия.
     * @return Мероприятие с указанным id.
     */
    Party findById(int id);


}