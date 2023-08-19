package com.heroku.java.Entitites.User;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.*;
import java.time.Instant;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.time.LocalDateTime;


import org.bson.types.ObjectId;
import org.json.JSONObject;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "users")
public class User {
    @Id
    private ObjectId id;
    private String userId;
    private String name;
    private String email;
    private String password;
    private Date registration;
    private String location;
    private int accuracy;
    private int totalGuesses;
    private int successfulGuesses;
    private int highStreak;
    private int currentStreak;
    private int rating;
    private String friends;
    private List<String> guess;
    private String token;
    private Date tokenExpiry;



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

    public String getEmail() {return this.email;}
    public void setEmail(String email) {this.email = email;}
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

    public int getTotalGuesses() { return this.totalGuesses; }
    public void setTotalGuesses(int totalGuesses) {
        this.totalGuesses = totalGuesses;
    }

    public int getSuccessfulGuesses() { return this.successfulGuesses; }
    public void setSuccessfulGuesses(int successfulGuesses) {
        this.successfulGuesses = successfulGuesses;
    }

    public int getHighStreak() { return this.highStreak; }
    public void setHighStreak(int highStreak) {
        this.highStreak = highStreak;
    }

    public int getCurrentStreak() { return this.currentStreak; }
    public void setCurrentStreak(int currentStreak) {
        this.currentStreak = currentStreak;
    }

    public int getRating() { return this.rating; }
    public void setRating(int rating) {
        this.rating = rating;
    }

    public String getFriends() { return this.friends; }
    public void setFriends(String friends) {
        this.friends = friends;
    }

    public List<String> getGuess() {return this.guess; }
    public void setGuess(List<String> guess) {this.guess = guess;}

    public String getToken() { return this.token; }
    public void setToken(String token) {
        this.token = token;
    }

    public Date getTokenExpiry() { return this.tokenExpiry; }
    public void setTokenExpiry(Date tokenExpiry) {
        this.tokenExpiry = tokenExpiry;
    }

    public User() {
        this.userId = UUID.randomUUID().toString();
        this.name = "John Doe";
        this.password = "1111";
        this.email = "example@gmail.com";
        this.registration = generateRegistration();
        this.location = generateLocation();
        this.accuracy = 0;
        this.totalGuesses = 0;
        this.successfulGuesses = 0;
        this.highStreak = 0;
        this.currentStreak = 0;
        this.rating = 0;
        this.friends = "";
        this.guess = new ArrayList<String>();
    }

    public User(int userDBLength) {
        this.userId = UUID.randomUUID().toString();
        this.name = "John Doe";
        this.password = "1111";
        this.email = "example@gmail.com";
        this.registration = generateRegistration();
        this.location = generateLocation();
        this.accuracy = 0;
        this.totalGuesses = 0;
        this.successfulGuesses = 0;
        this.highStreak = 0;
        this.currentStreak = 0;
        this.rating = userDBLength+1;
        this.friends = "";
        this.guess = new ArrayList<String>();
    }

    public User(String name, String password, String email, int userDBLength) {
        this.userId = UUID.randomUUID().toString();
        this.name = name;
        this.password = password;
        this.email = email;
        this.registration = generateRegistration();
        this.location = generateLocation();
        this.accuracy = 0;
        this.highStreak = 0;
        this.currentStreak = 0;
        this.rating = userDBLength+1;
        this.friends = "";
        this.guess = new ArrayList<String>();
    }

    private Date generateRegistration() {
        LocalDateTime localDateTime = LocalDateTime.now();
        ZoneId zoneId = ZoneId.systemDefault();
        Instant instant = localDateTime.atZone(zoneId).toInstant();
        Date date = Date.from(instant);
        return date;
    }

    private String generateLocation() {
        try {
            String apiKey = "d31cf59595d8a49b7f7aa6c844d5057d";
            String apiUrl = "http://api.ipstack.com/check?access_key=" + apiKey;
            URL url = new URL(apiUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");

            int responseCode = connection.getResponseCode();

            if (responseCode == HttpURLConnection.HTTP_OK) {
                BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                String line;
                StringBuilder response = new StringBuilder();
                while ((line = reader.readLine()) != null) {
                    response.append(line);
                }
                reader.close();
                JSONObject jsonResponse = new JSONObject(response.toString());
                String city = jsonResponse.getString("city");

                return city;
            } else {
                System.out.println("Request failed with response code: " + responseCode);
            }
        } catch(Exception e) {
            System.out.println(e.getMessage());
        }
        return "Unknown";
    }
}