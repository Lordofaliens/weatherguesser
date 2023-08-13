package com.heroku.java;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import java.util.Date;

public class TokenGenerator {

    private static final String SECRET_KEY = "your-secret-key"; //MOVE TO .ENV FILE
    private static final long EXPIRATION_TIME_MS = 604800000; //MOVE TO .ENV FILE // 1 week

    public static String generateBearerToken(String username) {
        Date now = new Date();
        Date expirationDate = new Date(now.getTime() + EXPIRATION_TIME_MS);
        System.out.println("WORKS!!!!!!!");
        return Jwts.builder()
                .setSubject(username)
                .setIssuedAt(now)
                .setExpiration(expirationDate)
                .signWith(SignatureAlgorithm.HS256, SECRET_KEY)
                .compact();
    }
}
