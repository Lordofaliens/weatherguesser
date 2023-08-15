package com.heroku.java.Services;

import com.heroku.java.Exceptions.InvalidGuessException;
import com.heroku.java.Exceptions.InvalidTokenException;
import com.heroku.java.Exceptions.UnknownUserException;
import com.heroku.java.Repositories.UserRepository;
import com.heroku.java.Entitites.User.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private LeaderBoardService leaderBoardService;
    @Autowired
    private MongoTemplate mongoTemplate;
    @Autowired
    private TokenService tokenService;

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public Optional<User> getSingleUser(String userId) {
        return userRepository.findUserByUserId(userId);
    }

    public Optional<User> getUserByName(String username) {
        return userRepository.findByName(username);
    }

    public User getUserData(String token) throws UnknownUserException {
        Claims claims = Jwts.parser().setSigningKey("your-secret-key").parseClaimsJws(token).getBody(); //CHANGE TO .ENV
        String username = claims.getSubject();
        Optional<User> u = this.getUserByName(username);
        if(u.isPresent()) return u.get();
        throw new UnknownUserException();
    }

    public String login(String name, String password) throws InvalidTokenException{ //ADD JAVA SECURITY
        Optional<User> user = this.userRepository.findByName(name);
        if(user.isPresent() && user.get().getPassword().equals(password)) {
            String token = tokenService.generateToken(user.get().getName()); // Generate JWT token
            mongoTemplate.update(User.class)
                    .matching(Criteria.where("userId").is(user.get().getUserId()))
                    .apply(new Update().set("token", token))
                    .first();
            System.out.println("0");
            return token; // ADD REAL TOKEN
        } else System.out.println("1");
        return "user_not_found";
    }

    public User changeName(String token, String newName) {
        Claims claims = Jwts.parser().setSigningKey("your-secret-key").parseClaimsJws(token).getBody(); //CHANGE TO .ENV
        String username = claims.getSubject();
        Optional<User> u = this.getUserByName(username);
        u.ifPresent((res) -> {
            u.get().setName(newName);

            mongoTemplate.update(User.class)
                    .matching(Criteria.where("userId").is(u.get().getUserId()))
                    .apply(new Update().set("name", u.get().getName()))
                    .first();

            //rerender new leaderboard
        });
        return u.get();
    }

    public User changeHighStreak(String token) {
        Claims claims = Jwts.parser().setSigningKey("your-secret-key").parseClaimsJws(token).getBody(); //CHANGE TO .ENV
        String username = claims.getSubject();
        Optional<User> u = this.getUserByName(username);
        u.ifPresent((res) -> {
            u.get().setHighStreak(u.get().getHighStreak()+1);

            mongoTemplate.update(User.class)
                    .matching(Criteria.where("userId").is(u.get().getUserId()))
                    .apply(new Update().set("highStreak", u.get().getHighStreak()))
                    .first();

        });
        return u.get();
    }

    public User changeCurrentStreak(String token, int newStreak) {
        Claims claims = Jwts.parser().setSigningKey("your-secret-key").parseClaimsJws(token).getBody(); //CHANGE TO .ENV
        String username = claims.getSubject();
        Optional<User> u = this.getUserByName(username);
        u.ifPresent((res) -> {
            mongoTemplate.update(User.class)
                    .matching(Criteria.where("userId").is(u.get().getUserId()))
                    .apply(new Update().set("currentStreak", newStreak))
                    .first();
        });
        return u.get();
    }

    public User changeAccuracy(String token, int newAcc) {
        Claims claims = Jwts.parser().setSigningKey("your-secret-key").parseClaimsJws(token).getBody(); //CHANGE TO .ENV
        String username = claims.getSubject();
        Optional<User> u = this.getUserByName(username);
        u.ifPresent((res) -> {
            u.get().setAccuracy(newAcc);

            mongoTemplate.update(User.class)
                    .matching(Criteria.where("userId").is(u.get().getUserId()))
                    .apply(new Update().set("accuracy", u.get().getAccuracy()))
                    .first();
        });
        return u.get();
    }

    public User add(String name, String password, String email) {
        User u = new User(name, password, email, this.userRepository.findAll().size());
        mongoTemplate.insert(u);
        //rerender new leaderboard
        return u;
    }

    public int delete(String token) {
        Claims claims = Jwts.parser().setSigningKey("your-secret-key").parseClaimsJws(token).getBody(); //CHANGE TO .ENV
        String username = claims.getSubject();
        Optional<User> u = this.getUserByName(username);

        if (u.isPresent()) {
            mongoTemplate.remove(Query.query(Criteria.where("userId").is(u.get().getUserId())), User.class);
            //rerender new leaderboard
            return 0;
        } else return 1;
    }

    public void makeGuess(String token, String city, String guess) throws InvalidGuessException {
        Claims claims = Jwts.parser().setSigningKey("your-secret-key").parseClaimsJws(token).getBody(); //CHANGE TO .ENV
        String username = claims.getSubject();
        Optional<User> u = this.getUserByName(username);
        if(u.isPresent()) {
            List<String> guesses = u.get().getGuess();
            for (int i = 0; i < guesses.size(); i++) {
                if(guesses.get(i).contains(city)) {
                    guesses.set(i, new String(city+";"+guess));
                    return;
                }
            }
            guesses.add(new String(city+";"+guess));
            mongoTemplate.update(User.class)
                    .matching(Criteria.where("userId").is(u.get().getUserId()))
                    .apply(new Update().set("guess", guesses))
                    .first();

        } else throw new InvalidGuessException();
    }

    public int uniqueUsername(String username) {
        List<User> u = getAllUsers();
        for(User user : u) {
            if(user.getName().equals(username)) return 1;
        }
        return 0;
    }

    public int uniqueEmail(String email) {
        List<User> u = getAllUsers();
        for(User user : u) {
            if(user.getEmail().equals(email)) return 1;
        }
        return 0;
    }

//ADD HANDLERS TO AVOID DUPLICATE NAMES / EMAILS


//    public void requestPasswordReset(String email) throws tokenException {
//        Optional<User> user = userRepository.findByEmail(email);
//        if (user.isPresent()) {
//            String token = generateToken();
//
//            // Calculate token expiry date (1 hour from now)
//            Calendar cal = Calendar.getInstance();
//            cal.setTime(new Date());
//            cal.add(Calendar.HOUR_OF_DAY, 1);
//
//            user.get().setResetToken(token);
//            user.get().setResetTokenExpiry(cal.getTime());
//            mongoTemplate.update(User.class)
//                    .matching(Criteria.where("userId").is(user.get().getUserId()))
//                    .apply(new Update().set("resetToken", user.get().getResetToken()).set("resetTokenExpiry", user.get().getResetTokenExpiry()))
//                    .first();
//
//            sendResetEmail(user.get().getEmail(), token);
//        } else throw new tokenException();
//    }
//
//    public void resetPassword(String token, String newPassword) {
//        Optional<User> user = userRepository.findByResetToken(token);
//
//        if (user.isPresent() && isTokenValid(user.get())) {
//            user.get().setPassword(passwordEncoder.encode(newPassword));
//            user.get().setResetToken(null);
//            user.get().setResetTokenExpiry(null);
//            mongoTemplate.update(User.class)
//                    .matching(Criteria.where("userId").is(user.get().getUserId()))
//                    .apply(new Update().set("password", user.get().getPassword()).set("resetToken", null).set("resetTokenExpiry", null))
//                    .first();
//        }
//    }
//
//    private String generateToken() {
//        return UUID.randomUUID().toString();
//    }
//
//    private boolean isTokenValid(User user) {
//        Date tokenExpiry = user.getResetTokenExpiry();
//        return tokenExpiry != null && tokenExpiry.after(new Date());
//    }
//
//    private void sendResetEmail(String email, String token) {
//        SimpleMailMessage message = new SimpleMailMessage();
//        message.setTo(email);
//        message.setSubject("Reset Your Password");
//        message.setText("Click the following link to reset your password: "
//                + "https://yourapp.com/reset-password/" + token);
//        javaMailSender.send(message);
//    }
}
