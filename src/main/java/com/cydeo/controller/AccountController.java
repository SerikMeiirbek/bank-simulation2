package com.cydeo.controller;

import com.cydeo.enums.AccountType;
import com.cydeo.model.Account;
import com.cydeo.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Date;
import java.util.UUID;

@Controller
@RequestMapping("")
public class AccountController {

    private final AccountService accountService;

    @Autowired
    public AccountController(AccountService accountService) {
        this.accountService = accountService;
    }

    @RequestMapping(value={"", "/", "/index"})
    public String accountList(Model model){
        model.addAttribute("accountList", accountService.listAllAccount());
        return "account/index";
    }

    @GetMapping("/create-form")
    public String createForm(Model model){
        model.addAttribute("account", Account.builder().build());
        model.addAttribute("accountTypes", AccountType.values());
        return "account/create-account";
    }

    @PostMapping("/create")
    public String create(@Valid Model model, @ModelAttribute("account") Account account, BindingResult bindingResult){

        if(bindingResult.hasErrors()){
            model.addAttribute("accountTypes", AccountType.values());
            return "account/create-account";
        }

        accountService.createNewAccount(account.getBalance()
                                            ,new Date(),
                                            account.getAccountType(),
                                            account.getUserId());


        model.addAttribute("accountList", accountService.listAllAccount());


        return "redirect:/index";
    }

    @GetMapping("/delete/{id}")
    public String deleteUser(@PathVariable("id") UUID id){
        accountService.deleteAccount(id);
        return "redirect:/index";
    }
}
