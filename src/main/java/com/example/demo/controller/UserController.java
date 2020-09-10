package com.example.demo.controller;

import com.example.demo.model.UserModel;
import com.example.demo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Administrator
 */
@RestController
@RequestMapping(value = "/user")
public class UserController {

    @Autowired
    private UserService userService;

    @RequestMapping(value = "/getName")
    public String getUserName(){
        String name = userService.getUserName();
        return name;
    }

    @RequestMapping(value = "/getModel")
    public UserModel getUserModel(){
        UserModel userModel = userService.getUserModel();
        return  userModel ;
    }
}
