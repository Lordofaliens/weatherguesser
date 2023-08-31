package com.heroku.java.Entitites.PasswordResetToken;

import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

@Document(collection = "passwordTokens")
public class PasswordResetToken {
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

    public PasswordResetToken() {
        this.token="";
        this.userId="";
    }

    public PasswordResetToken(String userId, String token, Date expiryDate) {
        this.userId = userId;
        this.token = token;
        this.expiryDate = expiryDate;
    }
}
