package com.heroku.java.User;

import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.UUID;
import java.time.LocalDateTime;


import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "users")
public class User {
    @Id
    private ObjectId id;
    private String userId;
    private String name;
    private String password;
    private Date registration;
    private String location;
    private int accuracy;
    private int highStreak;


    public String getUserId() {
        return this.userId;
    }
    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getName() {
        return this.name;
    }
    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() { return this.password; }
    public void setPassword(String password) {
        this.password = password;
    }

    public Date getRegistration() { return this.registration; }
    public void setPassword(Date registration) {
        this.registration = registration;
    }

    public String getLocation() { return this.location; }
    public void setLocation(String location) {
        this.location = location;
    }

    public int getAccuracy() { return this.accuracy; }
    public void setAccuracy(int accuracy) {
        this.accuracy = accuracy;
    }

    public int getHighStreak() { return this.highStreak; }
    public void setHighStreak(int highStreak) {
        this.highStreak = highStreak;
    }

    public User() {
        this.userId = UUID.randomUUID().toString();
        this.name = "John Doe";
        this.password = "1111";
        this.registration = generateRegistration();
        this.location = "Rotterdam"; //change to API output;
        this.accuracy = 0;
        this.highStreak = 0;
    }

    public User(String name, String password) {
        this.userId = UUID.randomUUID().toString();
        this.name = name;
        this.password = password;
        this.registration = generateRegistration();
        this.location = "Rotterdam"; //change to API output;
        this.accuracy = 0;
        this.highStreak = 0;
    }

    public Date generateRegistration() {
        LocalDateTime localDateTime = LocalDateTime.now();
        ZoneId zoneId = ZoneId.systemDefault();
        Instant instant = localDateTime.atZone(zoneId).toInstant();
        Date date = Date.from(instant);
        return date;
    }

    //ADD USER CURRENT STREAK
}
