package com.heroku.java.Services;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.stereotype.Service;

@Service
public class TokenService {

    private final static String secretKey = "your-secret-key"; // CHANGE TO .ENV

    public static String generateToken(String username) {
        return Jwts.builder()
                .setSubject(username)
                .signWith(SignatureAlgorithm.HS256, secretKey)
                .compact();
    }
}
