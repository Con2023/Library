package com.example.demo.RestControllers;

import com.example.demo.DTO.UserAccountDTO;
import com.example.demo.Entity.UserAccount;
import com.example.demo.Repos.AccountUserRepo;
import com.example.demo.Servises.AccountUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/accounts")
public class AccountRestController {

    @Autowired
    private AccountUserService accountUserService;


    /**
     * Получение списка всех учетных записей с возможностью поиска.
     * @return Список учетных записей.
     */
    @GetMapping
    public List<UserAccountDTO> getAccounts() {
        List<UserAccount> userAccounts = accountUserService.findAll();
        return userAccounts.stream()
                .map(UserAccountDTO::new)
                .collect(Collectors.toList());
    }

    /**
     * Получение информации об учетной записи по ID.
     * @param id Идентификатор учетной записи.
     * @return Данные учетной записи.
     */
    @GetMapping("/{id}")
    public ResponseEntity<UserAccountDTO> getAccount(@PathVariable("id") Integer id) {
        UserAccount userAccount = accountUserService.findById(id);
        if (userAccount == null) {
            return ResponseEntity.notFound().build();
        }
        UserAccountDTO userAccountDTO = new UserAccountDTO(userAccount);
        return ResponseEntity.ok(userAccountDTO);
    }

    /**
     * Обновление данных учетной записи.
     * @param id Идентификатор учетной записи.
     * @param userAccount Обновленные данные учетной записи.
     * @return Ответ с кодом 200 OK при успешном обновлении.
     */
    @PutMapping("/{id}")
    public ResponseEntity<UserAccountDTO> updateAccount(@PathVariable("id") Integer id,
                                                        @RequestBody UserAccount userAccount) {
        UserAccount existingUserAccount = accountUserService.findById(id);
        if (existingUserAccount == null) {
            return ResponseEntity.notFound().build();
        }
        LocalDate localDate = LocalDate.now();
        Date date = Date.from(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
        existingUserAccount.setCreatedDate(date);
        existingUserAccount.setNameAcc(userAccount.getNameAcc());
        existingUserAccount.setAge(userAccount.getAge());

        accountUserService.saveAcc(existingUserAccount);
        UserAccountDTO updatedDTO = new UserAccountDTO(existingUserAccount);
        return ResponseEntity.ok(updatedDTO);
    }

}
