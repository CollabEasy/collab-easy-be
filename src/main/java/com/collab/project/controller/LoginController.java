package com.collab.project.controller;


import com.collab.project.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class LoginController {

    @Autowired
    UserService userService;

    @RequestMapping("/login")
    public void login(UserInput input) {
        userService.createUser(input);
        return;
    }
}
