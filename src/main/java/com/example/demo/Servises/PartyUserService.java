package com.example.demo.Servises;

import com.example.demo.Entity.Party;
import com.example.demo.Repos.PartyRepo;
import com.example.demo.Entity.PartyUser;
import com.example.demo.Repos.PartyUserRepo;
import com.example.demo.Entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Сервис для работы с участниками мероприятий (PartyUser).
 * Осуществляет операции с регистрациями пользователей на мероприятия и управлением мероприятиями.
 */

@Service
public class PartyUserService {
    @Autowired
    private PartyUserRepo partyUserRepo;
    @Autowired
    private PartyRepo partyRepo;

    /**
     * Возвращает список всех мероприятий, которые соответствуют заданному ключевому слову для поиска.
     * Если ключевое слово не задано, возвращает все мероприятия.
     *
     * @param keyword Ключевое слово для поиска мероприятий.
     * @return Список всех мероприятий, удовлетворяющих поисковому запросу.
     */

    public List<Party> listAll(String keyword) {
        if (keyword != null) {
            return partyRepo.search(keyword);
        }
        System.out.println(partyRepo.findAll());
        return partyRepo.findAll();

    }
    /**
     * Сохраняет информацию о мероприятии.
     *
     * @param party Мероприятие, которое необходимо сохранить.
     */

    public void saveParty(Party party) {
        this.partyRepo.save(party);
    }

    /**
     * Регистрирует пользователя на мероприятие. Проверяет, зарегистрирован ли уже пользователь на это мероприятие.
     * Если пользователь уже зарегистрирован, выбрасывает исключение.
     *
     * @param partyUser Объект PartyUser, который связывает пользователя с мероприятием.
     * @param user Пользователь, которого нужно зарегистрировать на мероприятие.
     * @param party Мероприятие, на которое пользователь хочет зарегистрироваться.
     * @throws IllegalStateException если пользователь уже зарегистрирован на мероприятие.
     */

    public void registerParty(PartyUser partyUser, User user, Party party){
        PartyUser existingPartyUser = partyUserRepo.findByUserAndParty(user, party);

        if (existingPartyUser != null) {
            throw new IllegalStateException("Вы уже зарегистрированы на это мероприятие!");
        }
        else { partyUser.setUser(user);
            partyUser.setParty(party);
            partyUserRepo.save(partyUser);
            System.out.println("save partyUser success");}

    }
    /**
     * Удаляет мероприятие по его идентификатору.
     *
     * @param id Идентификатор мероприятия.
     */
    public void deleteByIdParty(Integer id){
        this.partyRepo.deleteById(id);
    }
    /**
     * Находит мероприятие по его идентификатору.
     *
     * @param id Идентификатор мероприятия.
     * @return Мероприятие с указанным идентификатором.
     */

    public Party findById(int id){
        return this.partyRepo.findById(id);
    }
    /**
     * Возвращает все мероприятия, отсортированные по имени в порядке возрастания.
     *
     * @return Список всех мероприятий, отсортированных по имени в порядке возрастания.
     */
    public List<Party> findAllSortedByNameAsc() {
        return partyRepo.findAll(Sort.by(Sort.Order.asc("name")));
    }
    /**
     * Возвращает все мероприятия, отсортированные по имени в порядке убывания.
     *
     * @return Список всех мероприятий, отсортированных по имени в порядке убывания.
     */

    public List<Party> findAllSortedByNameDesc() {
        return partyRepo.findAll(Sort.by(Sort.Order.desc("name")));
    }

    /**
     * Возвращает все мероприятия, отсортированные по дате в порядке возрастания.
     *
     * @return Список всех мероприятий, отсортированных по дате в порядке возрастания.
     */

    public List<Party> findAllSortedByDateAsc() {
        return partyRepo.findAll(Sort.by(Sort.Order.asc("date")));
    }
    /**
     * Возвращает все мероприятия, отсортированные по дате в порядке убывания.
     *
     * @return Список всех мероприятий, отсортированных по дате в порядке убывания.
     */
    public List<Party> findAllSortedByDateDesc() {
        return partyRepo.findAll(Sort.by(Sort.Order.desc("date")));
    }
    /**
     * Удаляет запись пользователя о мероприятии по его идентификатору.
     *
     * @param id Идентификатор записи пользователя на мероприятие.
     */
    public void deleteByIdPartyUser(Integer id){
        this.partyUserRepo.deleteById(id);
    }
}