package com.heroku.java.Services;

import com.heroku.java.Entitites.User.User;
import com.heroku.java.Entitites.Weather.Weather;
import com.heroku.java.Repositories.WeatherRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Service
public class WeatherAppService {
    @Autowired
    private UserService userService;
    @Autowired
    private WeatherService weatherService;
    @Autowired
    private LeaderBoardService leaderBoardService;
    @Autowired
    private MongoTemplate mongoTemplate;
    private String randomDailyCity = "London";

    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

    private String getRandomDailyCity() {return this.randomDailyCity;}
    @PostConstruct
    public Integer initializeWeatherUpdates() {

        Calendar calendar = Calendar.getInstance();
        long currentTimeMillis = calendar.getTimeInMillis();

        // Calculate the delay until 00:00 GMT of the next day
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        long nextExecutionTimeMillis = calendar.getTimeInMillis();
        if (nextExecutionTimeMillis <= currentTimeMillis) {
            nextExecutionTimeMillis += TimeUnit.DAYS.toMillis(1);
        }

        long initialDelay = nextExecutionTimeMillis - currentTimeMillis;

        scheduler.scheduleAtFixedRate(() -> {
            try {
                this.weatherService.weatherUpdateForAllLocations();
                List<Weather> weathers = weatherService.getAllWeather();
                List<User> users = userService.getAllUsers();
                this.userService.updateUsers(users, weathers);
                this.leaderBoardService.sortNewLeaderBoard();
                this.randomDailyCity = this.leaderBoardService.updateDailyWeather()[0];
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }, initialDelay, TimeUnit.DAYS.toMillis(1), TimeUnit.MILLISECONDS);
        return 0;
    }
}
//IMPROVE CODE BY USING HASHMAP INSTEAD OF n^3 EFFICIENCY, use sorting to avoid the third cycle