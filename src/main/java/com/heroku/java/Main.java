package com.heroku.java;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.sql.DataSource;

@SpringBootApplication
@RestController
public class Main {

    @Autowired
    public Main(DataSource dataSource) {
    }

    @GetMapping("/")
    public String index() {
        return "Server works!";
    }

    @GetMapping("/error")
    public String getError() {
        return "Error!";
    }

    public static void main(String[] args) {
        SpringApplication.run(Main.class, args);
    }

    //TODO: check other todos
    //TODO: update security code for tokens and store it correctly
    //TODO: optimize reset token code
    //TODO: delete commented code
    //TODO: fix terminal "JSONObject["city"] not found." error\
    //TODO: check front code with reset functions
}
