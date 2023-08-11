package com.heroku.java.Entitites.Weather;

import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;


@Document(collection="weather")
public class Weather {
    @Id
    private ObjectId id;
    private String location;
    private int temperature;
    private String condition;
    private double latitude;
    private double longitude;
    private String difficulty;

    public String getLocation() {
        return this.location;
    }
    public void setLocation(String location) { this.location = location; }
    public int getTemperature() {
        return this.temperature;
    }
    public void setTemperature(int temperature) { this.temperature = temperature; }
    public String getCondition() {
        return this.condition;
    }
    public void setCondition(String condition) { this.condition = condition; }
    public double getLatitude() {
        return this.latitude;
    }
    public void setLatitude(double latitude) { this.latitude = latitude; }
    public double getLongitude() {
        return this.longitude;
    }
    public void setLongitude(double longitude) { this.longitude = longitude; }
    public String getDifficulty() {
        return this.difficulty;
    }
    public void setDifficulty(String difficulty) { this.difficulty = difficulty; }

    public Weather() {
        this.location = "unknown";
        this.temperature = 0;
        this.condition = "unknown";
        this.latitude = 0;
        this.longitude = 0;
    }
    public Weather(String location) {
        this.location = location;
        this.temperature = 0;
        this.condition = "unknown";
        this.latitude = 0;
        this.longitude = 0;
    }
}
