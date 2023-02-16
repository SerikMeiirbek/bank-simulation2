package com.cydeo.service.impl;

import com.cydeo.entity.Account;
import com.cydeo.enums.AccountType;
import com.cydeo.repository.AccountRepository;
import com.cydeo.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Component
public class AccountServiceImpl implements AccountService {

    AccountRepository accountRepository;

    @Autowired
    public AccountServiceImpl(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    @Override
    public Account createNewAccount(BigDecimal balance, Date creationDate, AccountType accountType, Long userId) {
        Account account = Account.builder().id(UUID.randomUUID()).userId(userId).accountType(accountType).balance(balance)
                .creationDate(creationDate).build();
        return accountRepository.save(account);
    }

    @Override
    public List<Account> listAllAccount() {
        return AccountRepository.accountList;
    }
}
