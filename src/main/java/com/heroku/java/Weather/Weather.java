package com.heroku.java.Weather;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.asynchttpclient.AsyncHttpClient;
import org.asynchttpclient.DefaultAsyncHttpClient;
import org.asynchttpclient.ListenableFuture;
import org.asynchttpclient.Response;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;



public class Weather {
    private String location;
    private int temperature;

    public String getLocation() {
        return this.location;
    }

    public int getTemperature() {
        return this.temperature;
    }
    public Weather(String location) {
        this.location = location;
        this.temperature = 0;
    }
    public Map<String, String> updateWeatherData() throws IOException {
        String[] coordinates = getLocation().split(";");
        int lat = Integer.parseInt(coordinates[0]);
        int lon = Integer.parseInt(coordinates[1]);
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

                        String Name = jsonNode.get("location").get("name").asText();
                        int Temperature = jsonNode.get("current").get("temp_c").asInt();
                        String Condition = jsonNode.get("current").get("condition").get("text").asText();

                        // Create a map to hold the weather data
                        Map<String, String> weatherData = new HashMap<>();
                        weatherData.put("Name", Name);
                        weatherData.put("Temperature", String.valueOf(Temperature));
                        weatherData.put("Condition", Condition);
            client.close();
            return weatherData;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new HashMap<>();
    }
}
