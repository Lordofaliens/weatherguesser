package com.heroku.java.Repositories;

import com.heroku.java.Entitites.Photo.Photo;
import com.heroku.java.Entitites.User.User;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@EnableMongoRepositories
@Repository
public interface PhotoRepository extends MongoRepository<Photo, String> {
    Optional<Photo> findPhotoByPhotoId(String photoId);
    Optional<Photo> findPhotoByCity(String city);
}
