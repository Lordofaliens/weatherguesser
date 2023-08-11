package com.heroku.java.Repositories;

import com.heroku.java.Entitites.Weather.Weather;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@EnableMongoRepositories
@Repository
public interface WeatherRepository extends MongoRepository<Weather, ObjectId> {
    Optional<Weather> findWeatherByLatitude(double lat);
}
