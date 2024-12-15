package com.example.demo.DTO;

import com.example.demo.Entity.UserAccount;
import lombok.Getter;
import lombok.Setter;
@Getter
@Setter
public class UserAccountDTO {

    private Integer id;
    private String nameAcc;
    private int age;
    private String createdDate;

    public UserAccountDTO(UserAccount userAccount) {
        this.id = userAccount.getId();
        this.nameAcc = userAccount.getNameAcc();
        this.age = userAccount.getAge();
        this.createdDate = userAccount.getCreatedDate().toString();
    }


}
