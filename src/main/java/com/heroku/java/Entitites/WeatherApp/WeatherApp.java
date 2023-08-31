package com.heroku.java.Entitites.WeatherApp;

import com.heroku.java.Entitites.LeaderBoard.LeaderBoard;
import com.heroku.java.Entitites.Weather.Weather;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DocumentReference;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class WeatherApp {
    @Id
    private String id;
    private LeaderBoard leaderBoard;
    @DocumentReference
    private List<Weather> weatherList;

    public String getId() {return this.id;}
    public LeaderBoard getLeaderBoard() {
        return this.leaderBoard;
    }

    public List<Weather> getWeather() {
        return this.weatherList;
    }

    public WeatherApp() {
        this.id = UUID.randomUUID().toString();
        this.leaderBoard = new LeaderBoard();
        this.weatherList = new ArrayList<>();
    }

    public WeatherApp(LeaderBoard leaderBoard, List<Weather> weatherList) {
        this.id = UUID.randomUUID().toString();
        this.leaderBoard = leaderBoard;
        this.weatherList = weatherList;
    }

    public Boolean login(String username, String password) {
        return true;
    }

    public Boolean guessWeather(String location, Weather guess) {
        return true;
    }

}
