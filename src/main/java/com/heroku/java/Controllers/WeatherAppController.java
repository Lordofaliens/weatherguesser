package com.heroku.java.Controllers;

import com.heroku.java.Services.WeatherAppService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/weatherapp")
public class WeatherAppController {
    @Autowired
    private WeatherAppService weatherAppService;

    @PostMapping("/update")
    public ResponseEntity<Integer> update() {
        return new ResponseEntity<>(this.weatherAppService.initializeWeatherUpdates(), HttpStatus.OK);
    }
}