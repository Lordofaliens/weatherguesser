package com.heroku.java.Controllers;

import java.io.IOException;

import com.heroku.java.Exceptions.UnknownLocationException;
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

    //Improve when database already has this location
//    @GetMapping("/updateByLocation")
//    public ResponseEntity<Weather> updateWeatherData(@RequestParam("lat") String latS, @RequestParam("lon") String lonS) throws UnknownLocationException {
//        try {
//            double lat = Double.parseDouble(latS);
//            double lon = Double.parseDouble(lonS);
//            Weather updatedWeather = weatherService.updateWeatherData(lat,lon);
//            if(updatedWeather.getLatitude()==0) throw new UnknownLocationException();
//            return ResponseEntity.ok(updatedWeather);
//        } catch (IOException e) {
//            e.printStackTrace();
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
//        }
//    }
}
