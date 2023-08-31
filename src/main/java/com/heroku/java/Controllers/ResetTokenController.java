package com.heroku.java.Controllers;

import com.heroku.java.Entitites.ResetToken.ResetToken;
import com.heroku.java.Entitites.User.User;
import com.heroku.java.Exceptions.InvalidTokenException;
import com.heroku.java.Exceptions.UnknownUserException;
import com.heroku.java.Services.ResetTokenService;
import com.heroku.java.Services.UserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.Optional;

@RestController
@RequestMapping("/data-reset")
public class ResetTokenController {
    @Autowired
    private MongoTemplate mongoTemplate;

    @Autowired
    private UserService userService;

    @Autowired
    private ResetTokenService resetTokenService;

    @PostMapping("/requestPassword")
    public ResponseEntity<String> requestPasswordReset(@RequestParam("email") String email) throws UnknownUserException {
        Optional<User> user = userService.getUserByEmail(email);
        if(user.isPresent()) {
            resetTokenService.generatePasswordTokenSendEmail(user.get());
        } else throw new UnknownUserException();

        return ResponseEntity.ok("Password reset link sent to "+email+".");
    }

    @PostMapping("/requestEmail")
    public ResponseEntity<String> requestEmailReset(@RequestParam("newEmail") String newEmail, @RequestParam("oldEmail") String oldEmail) throws UnknownUserException {
        Optional<User> user = userService.getUserByEmail(oldEmail);
        if(user.isPresent()) {
            resetTokenService.generateEmailTokenSendEmail(user.get(), newEmail);
        } else throw new UnknownUserException();

        return ResponseEntity.ok("Email reset link sent to "+newEmail+".");
    }

    @PostMapping("/receivePassword")
    public ResponseEntity<String> receivePasswordReset(@RequestParam("password") String password, @RequestParam("token") String token) throws InvalidTokenException {
        ResetToken prt = resetTokenService.getTokenByToken(token);
        if (prt.getExpiryDate().after(new Date())) {
            String userId = prt.getUserId();
            userService.changePassword(userId,password);
            mongoTemplate.remove(Query.query(Criteria.where("token").is(token)), ResetToken.class);
            return ResponseEntity.ok("Password is changed.");
        } else throw new InvalidTokenException("Token is expired.");
    }

    @PostMapping("/receiveEmail")
    public ResponseEntity<String> receiveEmailReset(@RequestParam("email") String email, @RequestParam("token") String token) throws InvalidTokenException {
        ResetToken prt = resetTokenService.getTokenByToken(token);
        if (prt.getExpiryDate().after(new Date())) {
            String userId = prt.getUserId();
            userService.changeEmail(userId,email);
            mongoTemplate.remove(Query.query(Criteria.where("token").is(token)), ResetToken.class);
            return ResponseEntity.ok("Email is changed.");
        } else throw new InvalidTokenException("Token is expired.");
    }
}
