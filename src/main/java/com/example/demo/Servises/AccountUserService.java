package com.example.demo.Servises;


import com.example.demo.Entity.Book;
import com.example.demo.Entity.UserAccount;
import com.example.demo.Entity.Account;
import com.example.demo.Repos.AccountRepo;
import com.example.demo.Repos.AccountUserRepo;
import com.example.demo.Repos.RepoUser;
import com.example.demo.Entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;

/**
 * Работает с абонементами пользователей.
 */
@Service
public class AccountUserService {
    @Autowired
    private AccountUserRepo accountUserRepo;
    @Autowired
    private AccountRepo accountRepo;
    @Autowired
    private RepoUser repoUser;

    /**
     * Возвращает список всех учетных записей пользователей, либо результаты поиска по ключевому слову.
     *
     * @param keyword Ключевое слово для поиска.
     * @return Список учетных записей пользователей.
     */

    public List<UserAccount> listAll(String keyword){
        if(keyword!=null){
            return accountUserRepo.search(keyword);
        }
        return accountUserRepo.findAll();
    }

    /**
     * Сохраняет учетную запись пользователя и назначает тип счета в зависимости от возраста пользователя.
     * Если возраст меньше 14 лет, назначается "Детский" аккаунт, от 14 до 17 - "Подростковый",
     * старше 17 лет - "Взрослый".
     *
     * @param userAccount Учетная запись пользователя.
     * @param user Пользователь.
     */
    public void saveAccount(UserAccount userAccount, User user){
        Account account = new Account();
        if (userAccount.getAge()<14){
                account = accountRepo.findBytypeAccount("Детский");
                userAccount.setAccount(account); }
        else if (userAccount.getAge() >= 14 && userAccount.getAge() <= 17) {
                account = accountRepo.findBytypeAccount("Подростковый");
                userAccount.setAccount(account);}
        else {
                account = accountRepo.findBytypeAccount("Взрослый");
                userAccount.setAccount(account);
            }
            LocalDate localDate = LocalDate.now();
            Date date = Date.from(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
            userAccount.setCreatedDate(date);
            userAccount.setUser(user);
            accountUserRepo.save(userAccount);
            user.setUserAccount(userAccount);
            repoUser.save(user);
        }

    /**
     * Находит учетную запись пользователя по ID.
     *
     * @param id Идентификатор учетной записи.
     * @return Учетная запись пользователя с заданным ID.
     */
    public UserAccount findById(int id){
        return this.accountUserRepo.findById(id).get();
    }

    /**
     * Возвращает все абонементы без сортировки.
     *
     * @return Список всех книг.
     */
    public List<UserAccount> findAll() {
        return accountUserRepo.findAll();
    }

    public void saveAcc(UserAccount userAccount) {
        if (userAccount.getAge() < 14) {
            Account account = accountRepo.findBytypeAccount("Детский");
            userAccount.setAccount(account);
        } else if (userAccount.getAge() >= 14 && userAccount.getAge() <= 17) {
            Account account = accountRepo.findBytypeAccount("Подростковый");
            userAccount.setAccount(account);
        } else {
            Account account = accountRepo.findBytypeAccount("Взрослый");
            userAccount.setAccount(account);
        }

        accountUserRepo.save(userAccount);
    }

}

