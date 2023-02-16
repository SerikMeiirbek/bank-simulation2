package com.cydeo.service.impl;

import com.cydeo.entity.Account;
import com.cydeo.entity.Transaction;
import com.cydeo.enums.AccountType;
import com.cydeo.exception.AccountOwnershipException;
import com.cydeo.exception.BadRequestException;
import com.cydeo.exception.InsufficientBalanceExption;
import com.cydeo.exception.UnderConstructionException;
import com.cydeo.repository.AccountRepository;
import com.cydeo.repository.TransactionRepository;
import com.cydeo.service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Component
public class TransactionServiceImpl implements TransactionService {

    @Value("${under_construction}")
    private boolean underConstruction;
    AccountRepository accountRepository;
    TransactionRepository transactionRepository;

    @Autowired
    public TransactionServiceImpl(AccountRepository accountRepository, TransactionRepository transactionRepository) {
        this.accountRepository = accountRepository;
        this.transactionRepository = transactionRepository;
    }

    @Override
    public Transaction makeTransfer(BigDecimal amount, Date creationDate, Account sender, Account receiver, String message) {

        if (underConstruction) throw new UnderConstructionException("Transaction is under construction");

        checkAccountOwnerShip(sender, receiver);
        validateAccounts(sender,receiver);
        executeBalanceAndUpdateIfRequired(amount, sender, receiver);
        return transactionRepository.save(Transaction.builder()
                                            .amount(amount)
                                            .creationDate(creationDate)
                                            .sender(sender.getId())
                                            .receiver(receiver.getId())
                                            .message(message)
                                            .build());


    }

    private void executeBalanceAndUpdateIfRequired(BigDecimal amount, Account sender, Account receiver) {
        if(checkSenderBalance(sender, amount)){
            sender.setBalance(sender.getBalance().subtract(amount));
            receiver.setBalance(receiver.getBalance().add(amount));
        }else{
            throw new InsufficientBalanceExption("Balance is not enough for this transaction");
        }
    }

    private boolean checkSenderBalance(Account sender, BigDecimal amount) {
        return  sender.getBalance().subtract(amount).compareTo(BigDecimal.ZERO) > 0;
    }

    private void validateAccounts(Account sender, Account receiver) {

        if(sender == null || receiver == null){
            throw new BadRequestException("Sender or receiver can not be null");
        }

        if(sender.getId().equals(receiver.getId())){
            throw new BadRequestException("Sender and receiver can not be the same");
        }

        findAccountById(sender.getId());
        findAccountById(receiver.getId());
    }

    private Account findAccountById(UUID accountId) {
        return accountRepository.findById(accountId);
    }

    private void checkAccountOwnerShip(Account sender, Account receiver){
        if(sender.getAccountType().equals(AccountType.SAVING) || receiver.getAccountType().equals(AccountType.SAVING) && !sender.getUserId().equals(receiver.getUserId())){
            throw new AccountOwnershipException("When one of the account type is SAVING, sender and receiver has to be the same person");
        }
    }

    @Override
    public List<Transaction> findAll() {
        return transactionRepository.findAll();
    }
}
