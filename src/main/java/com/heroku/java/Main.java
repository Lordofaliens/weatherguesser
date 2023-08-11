package com.heroku.java;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.sql.DataSource;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.Map;

@SpringBootApplication
@RestController
public class Main {
    private final DataSource dataSource;

    @Autowired
    public Main(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @GetMapping("/")
    public String index() {
        return "index";
    }

    @GetMapping("/database")
    String database(Map<String, Object> model) {
        try (Connection connection = dataSource.getConnection()) {
            final var statement = connection.createStatement();
            statement.executeUpdate("CREATE TABLE IF NOT EXISTS ticks (tick timestamp)");
            statement.executeUpdate("INSERT INTO ticks VALUES (now())");

            final var resultSet = statement.executeQuery("SELECT tick FROM ticks");
            final var output = new ArrayList<>();
            while (resultSet.next()) {
                output.add("Read from DB: " + resultSet.getTimestamp("tick"));
            }

            model.put("records", output);
            return "database";

        } catch (Throwable t) {
            model.put("message", t.getMessage());
            return "error";
        }
    }
    @GetMapping("/getHello")
    public String getHello() {
        // Your logic to fetch and return the list of users from a data source
        return "Hello Vlad!";
    }

    @GetMapping("/error")
    public String getError() {
        return "Error!";
    }

    public static void main(String[] args) {
        SpringApplication.run(Main.class, args);
    }
}
