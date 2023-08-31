package com.heroku.java.Services;

import io.github.cdimascio.dotenv.Dotenv;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class TokenService {
    private static final String jwtSecretKey = Dotenv.configure().load().get("SECRET_KEY");

    public static String generateToken(String userId) {
        return Jwts.builder()
                .setSubject(userId)
                .signWith(SignatureAlgorithm.HS256, jwtSecretKey)
                .compact();
    }
}
