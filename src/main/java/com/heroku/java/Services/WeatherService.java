package com.heroku.java.Services;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.heroku.java.Repositories.WeatherRepository;
import com.heroku.java.Entitites.Weather.Weather;

import org.asynchttpclient.AsyncHttpClient;
import org.asynchttpclient.DefaultAsyncHttpClient;
import org.asynchttpclient.Response;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;

import java.io.IOException;

import java.util.*;

@Service
public class WeatherService {
    @Autowired
    private WeatherRepository weatherRepository;
    @Autowired
    private MongoTemplate mongoTemplate;

    public List<Weather> getAllWeather() {
        return this.weatherRepository.findAll();
    }

    public String convertCondition(String condition){
        if(condition.contains("Clear")||condition.contains("clear")) return "Clear";
        if(condition.contains("Sunny")||condition.contains("sunny")||condition.contains("Hot")||condition.contains("hot")) return "Sunny";
        if(condition.contains("Cloudy")||condition.contains("cloudy")) return "Cloudy";
        if(condition.contains("Mist")||condition.contains("mist")||condition.contains("Humid")||condition.contains("humid")||condition.contains("Cast")||condition.contains("cast")) return "Humid";
        if(condition.contains("Rain")||condition.contains("rain")) return "Rainy";
        if(condition.contains("Thunder")||condition.contains("thunder")) return "Thunder";
        if(condition.contains("Snow")||condition.contains("snow")) return "Snowy";
        return "Unknown";
    }

    public void updateWeatherData(double lat, double lon) {
        AsyncHttpClient client = new DefaultAsyncHttpClient();
        try {
            String responseBody = client.prepare("GET", "https://weatherapi-com.p.rapidapi.com/current.json?q="+lat+"%2C"+lon)
                    .setHeader("X-RapidAPI-Key", "2b386cf600msh1dfb6e048cf656dp180a91jsn7a58fcbbed6f")
                    .setHeader("X-RapidAPI-Host", "weatherapi-com.p.rapidapi.com")
                    .execute()
                    .toCompletableFuture()
                    .thenApply(Response::getResponseBody)
                    .join();
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode jsonNode = objectMapper.readTree(responseBody);

            int Temperature = (int)jsonNode.get("current").get("temp_c").asDouble();
            String Condition = jsonNode.get("current").get("condition").get("text").asText();
            client.close();
            Optional<Weather> weatherToChange = weatherRepository.findWeatherByLatitude(lat);
            if(weatherToChange.isPresent()) {
                mongoTemplate.update(Weather.class)
                        .matching(Criteria.where("latitude").is(lat))
                        .apply(new Update().set("temperature", Temperature).set("condition", convertCondition(Condition)))
                        .first();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void weatherUpdateForAllLocations() throws IOException {
        List<Weather> weatherList = weatherRepository.findAll();

        for (Weather weather : weatherList) {
            double lat = weather.getLatitude();
            double lon = weather.getLongitude();
            updateWeatherData(lat, lon);
        }
    }

    public List<Weather> getWeatherByDifficulty(String diff) {
        List<Weather> weatherList = weatherRepository.findAll();
        List<Weather> ans = new ArrayList<>();
        for(Weather w : weatherList) {
            if(w.getDifficulty().equals(diff)) {
                ans.add(w);
            }
        }
        return ans;
    }
}