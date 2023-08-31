package com.heroku.java.Entitites.ResetToken;

import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

@Document(collection = "passwordTokens")
public class ResetToken {
    @Id
    private ObjectId id;

    private String token;

    private String userId;
    private Date expiryDate;

    public String getToken() {return this.token;}
    public void setToken(String token) {this.token=token;}
    public String getUserId() {return this.userId;}
    public void setUserId(String userId) {this.userId=userId;}
    public Date getExpiryDate() {return this.expiryDate;}
    public void setExpiryDate(Date expiryDate) {this.expiryDate=expiryDate;}

    public ResetToken() {
        this.token="";
        this.userId="";
    }

    public ResetToken(String userId, String token, Date expiryDate) {
        this.userId = userId;
        this.token = token;
        this.expiryDate = expiryDate;
    }
}
