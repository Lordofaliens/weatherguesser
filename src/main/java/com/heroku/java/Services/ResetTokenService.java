package com.heroku.java.Services;

import com.heroku.java.Entitites.ResetToken.ResetToken;
import com.heroku.java.Entitites.User.User;
import com.heroku.java.Exceptions.InvalidTokenException;
import com.heroku.java.Repositories.ResetTokenRepository;

import io.github.cdimascio.dotenv.Dotenv;
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
public class ResetTokenService {

    @Autowired
    private ResetTokenRepository passwordResetTokenRepository;

    @Autowired
    private MongoTemplate mongoTemplate;

    @Autowired
    private JavaMailSender javaMailSender;

    private final String jwtSecretKey = Dotenv.configure().load().get("SECRET_KEY");

    private int jwtExpiration = 900;

    public ResetToken getTokenByToken(String token) throws InvalidTokenException {
        Optional<ResetToken> prt = passwordResetTokenRepository.findPasswordResetTokenByToken(token);
        if(prt.isPresent()) return prt.get();
        throw new InvalidTokenException("Token is invalid.");
    }

    public void generatePasswordTokenSendEmail(User user) {
        ResetToken prt = generateJwtToken(user);
        mongoTemplate.insert(prt);
        String resetLink = "http:localhost:3000/resetpassword?token=" + prt.getToken();
        String emailBody = "<p style={{textAlign:'center'}}>Click this <a href='"+resetLink+"'>link</a> to reset your WeatherGuesser password.<p><p>P.S Link can be activated once during the next 15 minutes.<p>";

        sendEmail("Password Reset",user.getEmail(), emailBody);
    }

    public void generateEmailTokenSendEmail(User user, String newEmail) {
        ResetToken prt = generateJwtToken(user);
        mongoTemplate.insert(prt);
        String resetLink = "http:localhost:3000/resetemail?email="+newEmail+"&token=" + prt.getToken();
        String emailBody = "<p style={{textAlign:'center'}}>Click this <a href='"+resetLink+"'>link</a> to reset your WeatherGuesser email.<p><p>P.S Link can be activated once during the next 15 minutes.<p>";

        sendEmail("Email Reset",newEmail, emailBody);
    }

    public void verifyEmailSendEmail(User user) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("userId", user.getUserId());
        claims.put("name", user.getName());
        claims.put("password", user.getPassword());
        claims.put("email", user.getEmail());
        claims.put("location", user.getLocation());
        claims.put("registration", user.getRegistration());
        claims.put("rating", user.getRating());

        ResetToken prt = generateJwtTokenFull(claims);
        mongoTemplate.insert(prt);
        String resetLink = "http:localhost:3000/verifyemail?token=" + prt.getToken();
        String emailBody = "<p style={{textAlign:'center'}}>Click this <a href='"+resetLink+"'>link</a> to verify your WeatherGuesser email.<p><p>P.S Link can be activated once during the next 15 minutes.<p>";

        sendEmail("Email Verify",user.getEmail(), emailBody);
    }

    public void sendEmail(String type, String recipientEmail, String emailBody) {
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        MimeMessageHelper messageHelper = new MimeMessageHelper(mimeMessage);

        try {
            messageHelper.setTo(recipientEmail);
            messageHelper.setSubject(type+" Request");
            messageHelper.setText(emailBody, true);
            javaMailSender.send(mimeMessage);
        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }
    }

    public void cleanExpiredTokensByDate(Date date) {
        Query query = new Query(Criteria.where("expiryDate").lt(date));
        mongoTemplate.remove(query, ResetToken.class);
    }

    private ResetToken generateJwtToken(User user) {
        String token = Jwts.builder()
                .setSubject(user.getUserId())
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 15 * 60 * 1000))
                .signWith(SignatureAlgorithm.HS256, jwtSecretKey)
                .compact();

        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.SECOND, jwtExpiration);
        Date futureDate = calendar.getTime();
        return new ResetToken(user.getUserId(),token, futureDate);
    }

    private ResetToken generateJwtTokenFull(Map<String, Object> claims) {
        String token = Jwts.builder()
                .setClaims(claims)
                .signWith(SignatureAlgorithm.HS256, jwtSecretKey)
                .compact();

        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.SECOND, jwtExpiration);
        Date futureDate = calendar.getTime();
        return new ResetToken(claims.get("userId").toString(),token, futureDate);
    }
}
