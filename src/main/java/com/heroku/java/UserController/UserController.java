package com.heroku.java.UserController;

import com.heroku.java.User.User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api")
public class UserController {
    @RequestMapping("/getHello")
    public String getHello() {
        // Your logic to fetch and return the list of users from a data source
        return "Hello Vlad!";
    }
}