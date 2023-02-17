package com.cydeo.service;

import com.cydeo.model.Account;
import com.cydeo.enums.AccountType;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.UUID;

public interface AccountService {

    Account createNewAccount(BigDecimal balance, Date creationDate, AccountType accountType, Long userId);

    List<Account> listAllAccount();

    void deleteAccount(UUID id);

    Account retrieveById(UUID accountId);
}
