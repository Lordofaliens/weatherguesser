package com.heroku.java.Controllers;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.heroku.java.Exceptions.InvalidTokenException;
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


    @GetMapping("/getCitiesByDiff")
    public ResponseEntity<List<Weather>> login(@RequestParam("difficulty") String diff) throws InvalidTokenException {
        return new ResponseEntity<>(this.weatherService.getWeatherByDifficulty(diff), HttpStatus.OK);
    }

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
