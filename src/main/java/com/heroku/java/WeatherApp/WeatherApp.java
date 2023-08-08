package com.heroku.java.WeatherApp;

import com.heroku.java.LeaderBoard.LeaderBoard;
import com.heroku.java.Weather.Weather;

import java.util.List;

public class WeatherApp {
    private LeaderBoard leaderBoard;
    private List<Weather> weatherList;

    public LeaderBoard getLeaderBoard() {
        return this.leaderBoard;
    }

    public List<Weather> getWeather() {
        return this.weatherList;
    }

    public Boolean login(String username, String password) {
        return true;
    }

    public Boolean guessWeather(String location, Weather guess) {
        return true;
    }
}
