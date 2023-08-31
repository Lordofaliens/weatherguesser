package com.heroku.java.Controllers;

import java.util.List;

import com.heroku.java.Exceptions.InvalidTokenException;
import com.heroku.java.Services.WeatherService;
import com.heroku.java.Entitites.Weather.Weather;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/weather")
public class WeatherController {
    @Autowired
    private WeatherService weatherService;


    @GetMapping("/getCitiesByDiff")
    public ResponseEntity<List<Weather>> login(@RequestParam("difficulty") String diff) {
        return new ResponseEntity<>(this.weatherService.getWeatherByDifficulty(diff), HttpStatus.OK);
    }
}
