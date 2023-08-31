package com.heroku.java.Services;

import com.heroku.java.Entitites.PasswordResetToken.PasswordResetToken;
import com.heroku.java.Entitites.User.User;
import com.heroku.java.Exceptions.InvalidTokenException;
import com.heroku.java.Repositories.PasswordResetTokenRepository;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class PasswordResetTokenService {

    @Autowired
    private PasswordResetTokenRepository passwordResetTokenRepository;

    @Autowired
    private MongoTemplate mongoTemplate;

    @Autowired
    private JavaMailSender javaMailSender;

    private String jwtSecret = "your-secret-key";

    private int jwtExpiration = 900;

    public PasswordResetToken getTokenByToken(String token) throws InvalidTokenException {
        Optional<PasswordResetToken> prt = passwordResetTokenRepository.findPasswordResetTokenByToken(token);
        if(prt.isPresent()) {
            return prt.get();
        }
        throw new InvalidTokenException("Token is invalid.");
    }

    public void generateTokenSendEmail(User user) {
        String token = generateJwtToken(user);

        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.SECOND, jwtExpiration);
        Date futureDate = calendar.getTime();
        PasswordResetToken prt = new PasswordResetToken(user.getUserId(),token, futureDate);
        mongoTemplate.insert(prt);
        String resetLink = "http:localhost:3000/resetpassword?token=" + token;
        String emailBody = "<p style={{textAlign:'center'}}>Click this <a href='"+resetLink+"'>link</a> to reset your WeatherGuesser password.<p><p>P.S Link can be activated once during the next 15 minutes.<p>";

        sendPasswordResetEmail(user.getEmail(), emailBody);
    }

    public void sendPasswordResetEmail(String recipientEmail, String emailBody) {
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        MimeMessageHelper messageHelper = new MimeMessageHelper(mimeMessage);

        try {
            messageHelper.setTo(recipientEmail);
            messageHelper.setSubject("Password Reset Request");
            messageHelper.setText(emailBody, true); // true indicates HTML content
            javaMailSender.send(mimeMessage);
        } catch (MessagingException e) {
            // Handle exception: log, throw, etc.
        }
    }

    public void generateEmailTokenSendEmail(User user, String newEmail) {
        String token = generateJwtToken(user);

        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.SECOND, jwtExpiration);
        Date futureDate = calendar.getTime();
        PasswordResetToken prt = new PasswordResetToken(user.getUserId(),token, futureDate);
        mongoTemplate.insert(prt);
        String resetLink = "http:localhost:3000/resetemail?email="+newEmail+"&token=" + token;
        String emailBody = "<p style={{textAlign:'center'}}>Click this <a href='"+resetLink+"'>link</a> to reset your WeatherGuesser email.<p><p>P.S Link can be activated once during the next 15 minutes.<p>";

        sendEmailResetEmail(newEmail, emailBody);
    }

    public void sendEmailResetEmail(String recipientEmail, String emailBody) {
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        MimeMessageHelper messageHelper = new MimeMessageHelper(mimeMessage);

        try {
            messageHelper.setTo(recipientEmail);
            messageHelper.setSubject("Email Reset Request");
            messageHelper.setText(emailBody, true); // true indicates HTML content
            javaMailSender.send(mimeMessage);
        } catch (MessagingException e) {
            // Handle exception: log, throw, etc.
        }
    }

    public void verifyEmail(User user) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("userId", user.getUserId());
        claims.put("name", user.getName());
        claims.put("password", user.getPassword());
        claims.put("email", user.getEmail());
        claims.put("location", user.getLocation());
        claims.put("registration", user.getRegistration());
        claims.put("rating", user.getRating());

        String token = generateJwtTokenFull(claims);

        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.SECOND, jwtExpiration);
        Date futureDate = calendar.getTime();
        PasswordResetToken prt = new PasswordResetToken(user.getUserId(),token, futureDate);
        mongoTemplate.insert(prt);
        String resetLink = "http:localhost:3000/verifyemail?token=" + token;
        String emailBody = "<p style={{textAlign:'center'}}>Click this <a href='"+resetLink+"'>link</a> to verify your WeatherGuesser email.<p><p>P.S Link can be activated once during the next 15 minutes.<p>";

        sendEmailVerifyEmail(user.getEmail(), emailBody);
    }

    public void sendEmailVerifyEmail(String recipientEmail, String emailBody) {
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        MimeMessageHelper messageHelper = new MimeMessageHelper(mimeMessage);

        try {
            messageHelper.setTo(recipientEmail);
            messageHelper.setSubject("Email Verify Request");
            messageHelper.setText(emailBody, true); // true indicates HTML content
            javaMailSender.send(mimeMessage);
        } catch (MessagingException e) {
            // Handle exception: log, throw, etc.
        }
    }

    public void cleanExpiredTokensByDate(Date date) {
        Query query = new Query(Criteria.where("expiryDate").lt(date));
        mongoTemplate.remove(query, PasswordResetToken.class);
    }

    private String generateJwtToken(User user) {
        return Jwts.builder()
                .setSubject(user.getUserId())
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 15 * 60 * 1000))
                .signWith(SignatureAlgorithm.HS256, "your-secret-key")
                .compact();
    }

    private String generateJwtTokenFull(Map<String, Object> claims) {

        return Jwts.builder()
                .setClaims(claims)
                .signWith(SignatureAlgorithm.HS256, "your-secret-key")
                .compact();
    }
}
