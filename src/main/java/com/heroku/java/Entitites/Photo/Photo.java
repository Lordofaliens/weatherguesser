package com.heroku.java.Entitites.Photo;

import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.UUID;

@Document(collection = "photos")
public class Photo {
    @Id
    private ObjectId id;
    private String photoId;
    private String city;
    private byte[] imageData;

    public String getPhotoId() {return this.photoId;}
    public void setPhotoId(String photoId) {this.photoId=photoId;}
    public String getCity() {return this.city;}
    public void setCity(String city) {this.city=city;}
    public byte[] getImageData() {return this.imageData;}
    public void setImageData(byte[] imageData) {this.imageData=imageData;}

    public Photo() {
        this.photoId = UUID.randomUUID().toString();
        this.city = "";
        this.imageData = null;
    }
    public Photo(String city, byte[] imageData) {
        this.photoId = UUID.randomUUID().toString();
        this.city = city;
        this.imageData = imageData;
    }
}