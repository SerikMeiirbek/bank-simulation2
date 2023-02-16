package com.cydeo.repository;

import com.cydeo.entity.Account;
import com.cydeo.exception.RecordNotFoundException;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Component
public class AccountRepository {

    private List<Account> accountList = new ArrayList<>();

    public Account save(Account account){
        accountList.add(account);
        return account;
    }

    public List<Account> findAll() {
        return accountList;
    }

    public Account findById(UUID accountId) {
       return findAll().stream().filter(account -> account.getId().equals(accountId)).findAny().orElseThrow(() ->
               new RecordNotFoundException("This account is not in database"));
    }


}
