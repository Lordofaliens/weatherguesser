package com.heroku.java.Controllers;

import com.heroku.java.Entitites.Photo.Photo;
import com.heroku.java.Exceptions.UnknownCityExpection;
import com.heroku.java.Services.PhotoService;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Optional;

@RestController
@RequestMapping("/api/photo")
public class PhotoController {
    @Autowired
    private PhotoService photoService;

    @GetMapping("/get")
    public void getPhotoById(@RequestParam("city") String city, HttpServletResponse response) throws UnknownCityExpection, IOException {
        Optional<Photo> photo = photoService.getSinglePhoto(city);
        if (photo.isPresent()) {
            response.setContentType(MediaType.IMAGE_JPEG_VALUE);
            response.getOutputStream().write(photo.get().getImageData());
        } else {
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            throw new UnknownCityExpection();
        }
    }

    @PostMapping("/upload")
    public ResponseEntity<String> uploadPhoto(@RequestBody MultipartFile file, @RequestParam String city) {
        try {
            this.photoService.storePhoto(city, file);
            return ResponseEntity.ok("Photo uploaded successfully");
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error uploading photo");
        }
    }
}
