package com.heroku.java.Controllers;

import com.heroku.java.Exceptions.InvalidGuessException;
import com.heroku.java.Exceptions.InvalidTokenException;
import com.heroku.java.Exceptions.UnknownUserException;
import com.heroku.java.Services.UserService;
import com.heroku.java.Entitites.User.User;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/user")
public class UserController {
    @Autowired
    private UserService userService;

    @GetMapping("/getAllUsers")
    public ResponseEntity<List<User>> getAllUsers() {
        return new ResponseEntity<>(userService.getAllUsers(), HttpStatus.OK);
    }

    @GetMapping("/getData")
    public ResponseEntity<User> getUserData(@RequestHeader("Authorization") String authorizationHeader) throws UnknownUserException {
        String token = authorizationHeader.replace("Bearer ", "");
        return new ResponseEntity<>(this.userService.getUserData(token), HttpStatus.OK);
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody Map<String, String> data) throws InvalidTokenException {
        return new ResponseEntity<>(this.userService.login(data.get("name"),data.get("password")), HttpStatus.OK);
    }

    @PostMapping("/changeName")
    public ResponseEntity<User> changeName(@RequestHeader("Authorization") String authorizationHeader, @RequestBody Map<String,String> data) throws UnknownUserException {
        String token = authorizationHeader.replace("Bearer ", "");
        User updatedUser = userService.changeName(token, data.get("name"));
        if(updatedUser.getName().equals("")) throw new UnknownUserException();
        return ResponseEntity.ok(updatedUser);
    }

    @PostMapping("/changeEmail")
    public ResponseEntity<User> changeEmail(@RequestHeader("Authorization") String authorizationHeader, @RequestBody Map<String,String> data) throws UnknownUserException {
        String token = authorizationHeader.replace("Bearer ", "");
        User updatedUser = userService.changeEmail(token, data.get("email"));
        if(updatedUser.getName().equals("")) throw new UnknownUserException();
        return ResponseEntity.ok(updatedUser);
    }

    @PostMapping("/changeHighStreak")
    public ResponseEntity<User> changeHighStreak(@RequestHeader("Authorization") String authorizationHeader) throws UnknownUserException {
        String token = authorizationHeader.replace("Bearer ", "");
        User updatedUser = userService.changeHighStreak(token);
        if(updatedUser.getHighStreak()==-1) throw new UnknownUserException();
        return ResponseEntity.ok(updatedUser);
    }

    @PostMapping("/changeCurrentStreak")
    public ResponseEntity<User> changeCurrentStreak(@RequestHeader("Authorization") String authorizationHeader, @RequestBody Map<String,String> data) throws UnknownUserException {
        String token = authorizationHeader.replace("Bearer ", "");
        User updatedUser = userService.changeCurrentStreak(token, Integer.parseInt(data.get("currentStreak")));
        if(updatedUser.getHighStreak()==-1) throw new UnknownUserException();
        return ResponseEntity.ok(updatedUser);
    }

    @PostMapping("/changeAccuracy")
    public ResponseEntity<User> changeAccuracy(@RequestHeader("Authorization") String authorizationHeader, @RequestBody Map<String,String> data) throws UnknownUserException {
        String token = authorizationHeader.replace("Bearer ", "");
        User updatedUser = userService.changeAccuracy(token, Integer.parseInt(data.get("accuracy")));
        if(updatedUser.getAccuracy()==-1) throw new UnknownUserException();
        return ResponseEntity.ok(updatedUser);
    }

    @PostMapping("/add")
    public ResponseEntity<Integer> add(@RequestBody Map<String, String> data) throws UnknownUserException {
        userService.addPreEmail(data.get("name"), data.get("password"), data.get("email"));
        return ResponseEntity.ok(0);
    }

    @PostMapping("/register")
    public ResponseEntity<String> register(@Param("token") String token) throws UnknownUserException {
        String res = userService.addPostEmail(token);
        return ResponseEntity.ok(res);
    }

    @PostMapping("/delete")
    public ResponseEntity<Integer> delete(@RequestHeader("Authorization") String authorizationHeader) throws UnknownUserException {
        String token = authorizationHeader.replace("Bearer ", "");
        int deletedUser = userService.delete(token);
        if(deletedUser==1) throw new UnknownUserException();
        return ResponseEntity.ok(0);
    }

    @PostMapping("/makeGuess")
    public ResponseEntity<List<String>> makeGuess(@RequestHeader("Authorization") String authorizationHeader, @RequestBody Map<String, String> requestBody) throws InvalidGuessException {
        String token = authorizationHeader.replace("Bearer ", "");
        return new ResponseEntity<>(userService.makeGuess(token, requestBody.get("city"), requestBody.get("guess")), HttpStatus.OK);
    }

    @GetMapping("/uniqueUsername")
    public ResponseEntity<Integer> uniqueUsername(@RequestParam("name") String name) {
        return new ResponseEntity<>(this.userService.uniqueUsername(name), HttpStatus.OK);
    }

    @GetMapping("/uniqueEmail")
    public ResponseEntity<Integer> uniqueEmail(@RequestParam("email") String email) {
        return new ResponseEntity<>(this.userService.uniqueEmail(email), HttpStatus.OK);
    }
}