package com.heroku.java.Services;

import io.github.cdimascio.dotenv.Dotenv;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.stereotype.Service;

@Service
public class TokenService {

    public static String generateToken(String userId) {
        Dotenv dotenv = Dotenv.load();
        String secretKey = dotenv.get("SECRET_KEY");

        return Jwts.builder()
                .setSubject(userId)
                .signWith(SignatureAlgorithm.HS256, "your-secret-key") // change to secretKey
                .compact();
    }
}
