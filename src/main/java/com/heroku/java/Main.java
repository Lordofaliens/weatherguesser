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

}
