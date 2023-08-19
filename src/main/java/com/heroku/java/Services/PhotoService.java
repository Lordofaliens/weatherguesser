package com.heroku.java.Services;

import com.heroku.java.Entitites.Photo.Photo;
import com.heroku.java.Repositories.PhotoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Optional;

@Service
public class PhotoService {
    @Autowired
    private PhotoRepository photoRepository;
    @Autowired
    private MongoTemplate mongoTemplate;

    public Optional<Photo> getSinglePhoto(String city) {
        return this.photoRepository.findPhotoByCity(city);
    }

    public void storePhoto(String city, MultipartFile file) throws IOException {
        Photo photo = new Photo(city, file.getBytes());
        mongoTemplate.insert(photo);
    }
}
