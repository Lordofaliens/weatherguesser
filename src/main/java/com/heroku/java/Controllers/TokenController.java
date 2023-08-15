package com.heroku.java.Controllers;

import com.heroku.java.Services.TokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TokenController {

    private final TokenService tokenService;

    @Autowired
    public TokenController(TokenService tokenService) {
        this.tokenService = tokenService;
    }

    @GetMapping("/generateToken")
    public String generateToken() {
        String token = tokenService.generateToken("username");
        return token;
    }
}
