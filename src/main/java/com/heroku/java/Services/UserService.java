package com.heroku.java.Services;

import com.heroku.java.Exceptions.InvalidGuessException;
import com.heroku.java.Repositories.UserRepository;
import com.heroku.java.Entitites.User.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private LeaderBoardService leaderBoardService;
    @Autowired
    private MongoTemplate mongoTemplate;
//    @Autowired
//    private PasswordEncoder passwordEncoder;
//    @Autowired
//    private JavaMailSender javaMailSender;

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public Optional<User> getSingleUser(String userId) {
        return userRepository.findUserByUserId(userId);
    }

    public User changeName(String userId, String newName) {
        User uNew = new User();
        Optional<User> u = this.getSingleUser(userId);
        u.ifPresent((res) -> {
            u.get().setName(newName);

            mongoTemplate.update(User.class)
                    .matching(Criteria.where("userId").is(u.get().getUserId()))
                    .apply(new Update().set("name", u.get().getName()))
                    .first();

            uNew.setUserId(u.get().getUserId());
            uNew.setName(u.get().getName());
            //rerender new leaderboard
        });

        if (!u.isPresent()) {
            uNew.setName("");
        }
        return uNew;
    }

    public User changeHighStreak(String userId) {
        User uNew = new User();
        Optional<User> u = this.getSingleUser(userId);
        u.ifPresent((res) -> {
            u.get().setHighStreak(u.get().getHighStreak()+1);

            mongoTemplate.update(User.class)
                    .matching(Criteria.where("userId").is(u.get().getUserId()))
                    .apply(new Update().set("highStreak", u.get().getHighStreak()))
                    .first();

            uNew.setUserId(u.get().getUserId());
            uNew.setHighStreak(u.get().getHighStreak());
        });

        if (!u.isPresent()) {
            uNew.setHighStreak(-1);
        }
        return uNew;
    }

    public User changeAccuracy(String userId, int newAcc) {
        User uNew = new User();
        Optional<User> u = this.getSingleUser(userId);
        u.ifPresent((res) -> {
            u.get().setAccuracy(newAcc);

            mongoTemplate.update(User.class)
                    .matching(Criteria.where("userId").is(u.get().getUserId()))
                    .apply(new Update().set("accuracy", u.get().getAccuracy()))
                    .first();

            uNew.setUserId(u.get().getUserId());
            uNew.setAccuracy(u.get().getAccuracy());
        });

        if (!u.isPresent()) uNew.setAccuracy(-1);

        return uNew;
    }

    public User add(String name, String password, String email) {
        User u = new User(name, password, email);
        mongoTemplate.insert(u);
        //rerender new leaderboard
        return u;
    }

    public int delete(String userId) {
        Optional<User> u = this.getSingleUser(userId);

        if (u.isPresent()) {
            mongoTemplate.remove(Query.query(Criteria.where("userId").is(userId)), User.class);
            //rerender new leaderboard
            return 0;
        } else return 1;
    }

    public void makeGuess(String userId, String city, String guess) throws InvalidGuessException {
        Optional<User> u = getSingleUser(userId);
        if(u.isPresent()) {
            List<String> guesses = u.get().getGuess();
            for (int i = 0; i < guesses.size(); i++) {
                if(guesses.get(i).contains(city)) {
                    guesses.set(i, new String(city+";"+guess));
                    return;
                }
            }
            guesses.add(new String(city+";"+guess));
        } else throw new InvalidGuessException();
    }




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
