package com.heroku.java.Services;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.heroku.java.Repositories.WeatherRepository;
import com.heroku.java.Weather.Weather;
import org.asynchttpclient.AsyncHttpClient;
import org.asynchttpclient.DefaultAsyncHttpClient;
import org.asynchttpclient.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;

import javax.management.Query;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
public class WeatherService {
    @Autowired
    private WeatherRepository weatherRepository;
    @Autowired
    private MongoTemplate mongoTemplate;
    public Weather updateWeatherData(double lat, double lon) throws IOException {
        AsyncHttpClient client = new DefaultAsyncHttpClient();
        Weather updatedWeather = new Weather();
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

            int Temperature = jsonNode.get("current").get("temp_c").asInt();
            String Condition = jsonNode.get("current").get("condition").get("text").asText();

            client.close();
            Optional<Weather> weatherToChange = weatherRepository.findWeatherByLatitude(lat);

            weatherToChange.ifPresent(newWeather -> {
                newWeather.setTemperature(Temperature);
                newWeather.setCondition(Condition);

                // Update the weather data in MongoDB
                mongoTemplate.update(Weather.class)
                        .matching(Criteria.where("location").is(newWeather.getLocation()))
                        .apply(new Update().set("temperature", newWeather.getTemperature()).set("condition", newWeather.getCondition()))
                        .first();

                updatedWeather.setTemperature(newWeather.getTemperature());
                updatedWeather.setCondition(newWeather.getCondition());
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
        return updatedWeather;
    }
}
