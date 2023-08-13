package com.heroku.java.Services;

import com.heroku.java.Repositories.WeatherRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Calendar;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Service
public class WeatherAppService {
    @Autowired
    private WeatherService weatherService;

    @Autowired
    private LeaderBoardService leaderBoardService;

    private String randomDailyCity = "London";

    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

    private String getRandomDailyCity() {return this.randomDailyCity;}
    @PostConstruct
    public void initializeWeatherUpdates() {

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
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            List<String> topUsersIds = this.leaderBoardService.sortNewLeaderBoard();
            this.randomDailyCity = this.weatherService.getDailyWeather();
        }, initialDelay, TimeUnit.DAYS.toMillis(1), TimeUnit.MILLISECONDS);
        //CALCULATE PEOPLE's GUESSES, TERMINATE GUESS ARRAY, recalculate high-streaks and accuracy before updating leaderboard
    }
}
