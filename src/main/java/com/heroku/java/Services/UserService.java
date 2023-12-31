package com.heroku.java.Services;

import com.heroku.java.Entitites.Weather.Weather;
import com.heroku.java.Exceptions.InvalidGuessException;
import com.heroku.java.Exceptions.InvalidTokenException;
import com.heroku.java.Exceptions.InvalidWeatherDBException;
import com.heroku.java.Exceptions.UnknownUserException;
import com.heroku.java.Repositories.UserRepository;
import com.heroku.java.Entitites.User.User;

import io.github.cdimascio.dotenv.Dotenv;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;

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
    @Autowired
    private TokenService tokenService;
    @Autowired
    private ResetTokenService passwordResetTokenService;
    private final String jwtSecretKey = Dotenv.configure().load().get("SECRET_KEY");

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public Optional<User> getSingleUser(String userId) {
        return userRepository.findUserByUserId(userId);
    }

    public Optional<User> getUserByName(String username) {
        return userRepository.findByName(username);
    }

    public Optional<User> getUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public User getUserData(String token) throws UnknownUserException {
        Claims claims = Jwts.parser().setSigningKey(jwtSecretKey).parseClaimsJws(token).getBody(); //CHANGE TO .ENV
        String userId = claims.getSubject();
        Optional<User> u = this.getSingleUser(userId);
        if (u.isPresent()) return u.get();
        throw new UnknownUserException();
    }

    public String login(String name, String password) throws InvalidTokenException{
        Optional<User> user = this.userRepository.findByName(name);
        if(user.isPresent() && user.get().getPassword().equals(password)) {
            String token = tokenService.generateToken(user.get().getUserId());
            mongoTemplate.update(User.class)
                    .matching(Criteria.where("userId").is(user.get().getUserId()))
                    .apply(new Update().set("token", token))
                    .first();
            return token;
        } else System.out.println("1");
        return "user_not_found";
    }

    public User changeName(String token, String newName) {
        Claims claims = Jwts.parser().setSigningKey(jwtSecretKey).parseClaimsJws(token).getBody(); //CHANGE TO .ENV
        String userId = claims.getSubject();
        Optional<User> u = this.getSingleUser(userId);
        u.ifPresent((res) -> {
            u.get().setName(newName);
            mongoTemplate.update(User.class)
                    .matching(Criteria.where("userId").is(u.get().getUserId()))
                    .apply(new Update().set("name", u.get().getName()))
                    .first();
        });
        return u.get();
    }

    public User changeEmail(String userId, String newEmail) {
        Optional<User> u = this.getSingleUser(userId);
        u.ifPresent((res) -> mongoTemplate.update(User.class)
                .matching(Criteria.where("userId").is(userId))
                .apply(new Update().set("email", newEmail))
                .first());
        return u.get();
    }

    public User changeHighStreak(String token) {
        Claims claims = Jwts.parser().setSigningKey(jwtSecretKey).parseClaimsJws(token).getBody(); //CHANGE TO .ENV
        String userId = claims.getSubject();
        Optional<User> u = this.getSingleUser(userId);
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
        Claims claims = Jwts.parser().setSigningKey(jwtSecretKey).parseClaimsJws(token).getBody(); //CHANGE TO .ENV
        String userId = claims.getSubject();
        Optional<User> u = this.getSingleUser(userId);
        u.ifPresent((res) -> mongoTemplate.update(User.class)
                .matching(Criteria.where("userId").is(u.get().getUserId()))
                .apply(new Update().set("currentStreak", newStreak))
                .first());
        return u.get();
    }

    public User changeAccuracy(String token, int newAcc) {
        Claims claims = Jwts.parser().setSigningKey(jwtSecretKey).parseClaimsJws(token).getBody(); //CHANGE TO .ENV
        String userId = claims.getSubject();
        Optional<User> u = this.getSingleUser(userId);
        u.ifPresent((res) -> {
            u.get().setAccuracy(newAcc);

            mongoTemplate.update(User.class)
                    .matching(Criteria.where("userId").is(u.get().getUserId()))
                    .apply(new Update().set("accuracy", u.get().getAccuracy()))
                    .first();
        });
        return u.get();
    }

    public User changeTotalGuesses(String token, int newTotalGuesses) {
        Claims claims = Jwts.parser().setSigningKey(jwtSecretKey).parseClaimsJws(token).getBody(); //CHANGE TO .ENV
        String userId = claims.getSubject();
        Optional<User> u = this.getSingleUser(userId);
        u.ifPresent((res) -> {
            u.get().setTotalGuesses(newTotalGuesses);

            mongoTemplate.update(User.class)
                    .matching(Criteria.where("userId").is(u.get().getUserId()))
                    .apply(new Update().set("totalGuesses", u.get().getTotalGuesses()))
                    .first();
        });
        return u.get();
    }

    public void addPreEmail(String name, String password, String email) {
        User u = new User(name, password, email, this.userRepository.findAll().size());
        passwordResetTokenService.verifyEmailSendEmail(u);
    }

    public String addPostEmail(String initToken) {
        Claims claims = decodeToken(initToken);
        User u = new User(
                claims.get("userId", String.class),
                claims.get("name", String.class),
                claims.get("password", String.class),
                claims.get("email", String.class),
                claims.get("location", String.class),
                claims.get("registration", Date.class),
                claims.get("rating", Integer.class)
                );

        String token = TokenService.generateToken(u.getUserId());
        u.setToken(token);
        mongoTemplate.insert(u);
        return token;
    }

    public int delete(String token) {
        Claims claims = Jwts.parser().setSigningKey(jwtSecretKey).parseClaimsJws(token).getBody(); //CHANGE TO .ENV
        String userId = claims.getSubject();
        Optional<User> u = this.getSingleUser(userId);
        if (u.isPresent()) {
            mongoTemplate.remove(Query.query(Criteria.where("userId").is(u.get().getUserId())), User.class);
            //rerender new leaderboard
            return 0;
        } else return 1;
    }

    public List<String> makeGuess(String token, String city, String guess) throws InvalidGuessException {
        Claims claims = Jwts.parser().setSigningKey(jwtSecretKey).parseClaimsJws(token).getBody(); //CHANGE TO .ENV
        String userId = claims.getSubject();
        Optional<User> u = this.getSingleUser(userId);
        if(u.isPresent()) {
            List<String> guesses = u.get().getGuess();
            for (int i = 0; i < guesses.size(); i++) {
                if(guesses.get(i).contains(city)) {
                    guesses.remove(i);
                    break;
                }
            }
            guesses.add(city + ";" + guess);
            mongoTemplate.update(User.class)
                    .matching(Criteria.where("userId").is(u.get().getUserId()))
                    .apply(new Update().set("guess", guesses))
                    .first();

        } else throw new InvalidGuessException();
        return u.get().getGuess();
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

    public void updateUsers(List<User> users, List<Weather> weathers) throws InvalidWeatherDBException {
        for (User u : users) {
            List<String> guesses = u.getGuess();
            for(String guess : guesses) {
                String[] gArr = guess.split(";");
                boolean isDaily = this.leaderBoardService.getDailyWeather()[0].equals(gArr[0]);
                for (Weather weather : weathers) {
                    if (weather.getLocation().equals(gArr[0])) {
                        if(weather.getCondition().equals(gArr[1])) {
                            u.setSuccessfulGuesses(u.getSuccessfulGuesses()+1);
                            if(isDaily) u.setCurrentStreak(u.getCurrentStreak()+1);
                        } else {
                            if(isDaily) u.setCurrentStreak(0);
                        }
                        u.setTotalGuesses(u.getTotalGuesses()+1);
                        break;
                    }
                }
            }
            if(u.getHighStreak()<u.getCurrentStreak()) u.setHighStreak(u.getCurrentStreak());
            u.setAccuracy(u.getTotalGuesses()==0?0:100*u.getSuccessfulGuesses()/u.getTotalGuesses());
            u.setGuess(new ArrayList<>());
            mongoTemplate.update(User.class)
                    .matching(Criteria.where("userId").is(u.getUserId()))
                    .apply(new Update()
                            .set("currentStreak", u.getCurrentStreak())
                            .set("highStreak", u.getHighStreak())
                            .set("totalGuesses", u.getTotalGuesses())
                            .set("successfulGuesses", u.getSuccessfulGuesses())
                            .set("accuracy", u.getAccuracy())
                            .set("guess", u.getGuess())
                    )
                    .first();
        }
    }

    public void changePassword(String userId, String newPassword) {
            mongoTemplate.update(User.class)
                    .matching(Criteria.where("userId").is(userId))
                    .apply(new Update().set("password", newPassword))
                    .first();
    }

    public Claims decodeToken(String token) {
        return Jwts.parser()
                .setSigningKey(jwtSecretKey)
                .parseClaimsJws(token)
                .getBody();
    }
}
