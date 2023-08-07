package com.heroku.java.WeatherApp;

import com.heroku.java.LeaderBoard.LeaderBoard;
import com.heroku.java.Weather.Weather;

public class WeatherApp {
    private LeaderBoard leaderBoard;
    private Weather weather;

    public LeaderBoard getLeaderBoard() {
        return this.leaderBoard;
    }

    public Weather getWeather() {
        return this.weather;
    }

    public Boolean login(String username, String password) {
        return true;
    }

    public Boolean guessWeather(String location, Weather guess) {
        return true;
    }
}
