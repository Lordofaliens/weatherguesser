package com.heroku.java.Services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class ExpiredTokenCollectorService {

    @Autowired
    private ResetTokenService passwordResetTokenService;

    @Scheduled(fixedRate = 3600000)
    public void cleanupExpiredTokens() {
        Date now = new Date();
        passwordResetTokenService.cleanExpiredTokensByDate(now);
    }
}
