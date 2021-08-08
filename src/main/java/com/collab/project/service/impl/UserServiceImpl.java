package com.collab.project.service.impl;

import com.collab.project.controller.UserInput;
import com.collab.project.model.user.User;
import com.collab.project.repositories.UserRepository;
import com.collab.project.service.UserService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    UserRepository userRepository;
    @Override
    public void createUser(UserInput userInput) {
        User user = new User();
        BeanUtils.copyProperties(userInput, user);
        userRepository.save(user);
    }

    @Override
    public void updateUser() {

    }
}
