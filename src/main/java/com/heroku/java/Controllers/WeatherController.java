package com.heroku.java.Controllers;

import java.io.IOException;
import java.util.Map;

import com.heroku.java.Weather.Weather;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin(origins = "http://localhost:3000")
public class WeatherController {
    @GetMapping("/api/weather")
    public String hello(@RequestParam("hello") String hello) {
        return "Hello World " + hello;
    }



    public ResponseEntity<Map<String, String>> getWeatherData(@RequestParam("location") String location) {
        Weather weather = new Weather(location);
        try {
            Map<String, String> weatherData = weather.updateWeatherData();
            return ResponseEntity.ok(weatherData);
        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
