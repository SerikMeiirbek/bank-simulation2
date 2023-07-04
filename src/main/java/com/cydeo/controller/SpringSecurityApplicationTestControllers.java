package com.cydeo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Controller
@RequestMapping("")
public class SpringSecurityApplicationTestControllers {

    @GetMapping("/")
    public String home(){
        return "Welcome";
    }

    @GetMapping("/user")
    public String user(){
        return "<h1>Welcome User</h1>";
    }

    @GetMapping("/admin")
    public String admin(){
        return "<h1>Welcome Admin</h1>";
    }
}
