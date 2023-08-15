package com.heroku.java.Controllers;

import com.heroku.java.Exceptions.InvalidGuessException;
import com.heroku.java.Exceptions.InvalidTokenException;
import com.heroku.java.Exceptions.UnknownUserException;
import com.heroku.java.Services.UserService;
import com.heroku.java.Entitites.User.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.InetAddress;
import java.net.URL;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/user")
public class UserController {
    @Autowired
    private UserService userService;
    @GetMapping("/getAllUsers")
    public ResponseEntity<List<User>> getAllUsers() {
        return new ResponseEntity<List<User>>(userService.getAllUsers(), HttpStatus.OK);
    }
//    @GetMapping("/{userId}")
//    public ResponseEntity<Optional<User>> getSingleUser(@PathVariable String userId) {
//        return new ResponseEntity<Optional<User>>(userService.getSingleUser(userId), HttpStatus.OK);
//    }

    @GetMapping("/getData")
    public ResponseEntity<User> getUserData(@RequestHeader("Authorization") String authorizationHeader) throws UnknownUserException {
        String token = authorizationHeader.replace("Bearer ", "");
        return new ResponseEntity<>(this.userService.getUserData(token), HttpStatus.OK);
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody Map<String, String> data) throws InvalidTokenException {
        return new ResponseEntity<>(this.userService.login(data.get("name"),data.get("password")), HttpStatus.OK);
    }

    @GetMapping("/changeName")
    public ResponseEntity<User> changeName(@RequestHeader("Authorization") String authorizationHeader, @RequestBody Map<String,String> data) throws UnknownUserException {
        String token = authorizationHeader.replace("Bearer ", "");
        User updatedUser = userService.changeName(token, data.get("name"));
        if(updatedUser.getName()=="") throw new UnknownUserException();
        return ResponseEntity.ok(updatedUser);
    }

    @GetMapping("/changeHighStreak")
    public ResponseEntity<User> changeHighStreak(@RequestHeader("Authorization") String authorizationHeader) throws UnknownUserException {
        String token = authorizationHeader.replace("Bearer ", "");
        User updatedUser = userService.changeHighStreak(token);
        if(updatedUser.getHighStreak()==-1) throw new UnknownUserException();
        return ResponseEntity.ok(updatedUser);
    }

    @GetMapping("/changeCurrentStreak")
    public ResponseEntity<User> changeCurrentStreak(@RequestHeader("Authorization") String authorizationHeader, @RequestBody Map<String,String> data) throws UnknownUserException {
        String token = authorizationHeader.replace("Bearer ", "");
        User updatedUser = userService.changeCurrentStreak(token, Integer.parseInt(data.get("currentStreak")));
        if(updatedUser.getHighStreak()==-1) throw new UnknownUserException();
        return ResponseEntity.ok(updatedUser);
    }

    @GetMapping("/changeAccuracy")
    public ResponseEntity<User> changeAccuracy(@RequestHeader("Authorization") String authorizationHeader, @RequestBody Map<String,String> data) throws UnknownUserException {
        String token = authorizationHeader.replace("Bearer ", "");
        User updatedUser = userService.changeAccuracy(token, Integer.parseInt(data.get("accuracy")));
        if(updatedUser.getAccuracy()==-1) throw new UnknownUserException();
        return ResponseEntity.ok(updatedUser);
    }

    @PostMapping("/add")
    public ResponseEntity<User> add(@RequestBody Map<String, String> data) throws UnknownUserException {
        User createdUser = userService.add(data.get("name"), data.get("password"), data.get("email"));
        return ResponseEntity.ok(createdUser);
    }

    @GetMapping("/delete")
    public ResponseEntity<Integer> delete(@RequestHeader("Authorization") String authorizationHeader) throws UnknownUserException {
        String token = authorizationHeader.replace("Bearer ", "");
        int deletedUser = userService.delete(token);
        if(deletedUser==1) throw new UnknownUserException();
        return ResponseEntity.ok(0);
    }

    @GetMapping("/makeGuess")
    public ResponseEntity<Integer> makeGuess(@RequestHeader("Authorization") String authorizationHeader, @RequestBody Map<String, String> data) throws InvalidGuessException {
        String token = authorizationHeader.replace("Bearer ", "");
        userService.makeGuess(token, data.get("city"), data.get("guess"));
        return ResponseEntity.ok(0);
    }

    @GetMapping("/uniqueUsername")
    public ResponseEntity<Integer> uniqueUsername(@RequestParam("name") String name) throws InvalidGuessException {
        return new ResponseEntity<>(this.userService.uniqueUsername(name), HttpStatus.OK);
    }

    @GetMapping("/uniqueEmail")
    public ResponseEntity<Integer> uniqueEmail(@RequestParam("email") String email) throws InvalidGuessException {
        return new ResponseEntity<>(this.userService.uniqueEmail(email), HttpStatus.OK);
    }

//    @PostMapping("/password")
//    public ResponseEntity<String> requestPasswordReset(@RequestParam("email") String email) throws tokenException {
//        userService.requestPasswordReset(email);
//        return ResponseEntity.ok("Password reset link sent to your email!");
//    }
//
//    @PostMapping("/password/{token}")
//    public ResponseEntity<String> resetPassword(@PathVariable String token, @RequestParam("password") String newPassword) {
//        userService.resetPassword(token, newPassword);
//        return ResponseEntity.ok("Password reset successful!");
//    }

}