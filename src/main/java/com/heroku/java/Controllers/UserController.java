package com.heroku.java.Controllers;

import com.heroku.java.Exceptions.InvalidGuessException;
import com.heroku.java.Exceptions.InvalidTokenException;
import com.heroku.java.Exceptions.UnknownUserException;
import com.heroku.java.Services.UserService;
import com.heroku.java.Entitites.User.User;
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
    @GetMapping("/{userId}")
    public ResponseEntity<Optional<User>> getSingleUser(@PathVariable String userId) {
        return new ResponseEntity<Optional<User>>(userService.getSingleUser(userId), HttpStatus.OK);
    }

    @PostMapping("/login") //ADD JAVA SECURITY
    public ResponseEntity<String> login(@RequestBody Map<String, String> data) throws InvalidTokenException {
        return new ResponseEntity<>(this.userService.login(data.get("name"),data.get("password")), HttpStatus.OK);
    }

    @GetMapping("/changeName") //ADD JAVA SECURITY
    public ResponseEntity<User> changeName(@RequestBody Map<String,String> data) throws UnknownUserException {
        User updatedUser = userService.changeName(data.get("userId"), data.get("name"));
        if(updatedUser.getName()=="") throw new UnknownUserException();
        return ResponseEntity.ok(updatedUser);
    }

    @GetMapping("/changeHighStreak") //ADD JAVA SECURITY
    public ResponseEntity<User> changeHighStreak(@RequestBody Map<String,String> data) throws UnknownUserException {
        User updatedUser = userService.changeHighStreak(data.get("userId"));
        if(updatedUser.getHighStreak()==-1) throw new UnknownUserException();
        return ResponseEntity.ok(updatedUser);
    }

    @GetMapping("/changeCurrentStreak") //ADD JAVA SECURITY
    public ResponseEntity<User> changeCurrentStreak(@RequestBody Map<String,String> data) throws UnknownUserException {
        User updatedUser = userService.changeCurrentStreak(data.get("userId"),Integer.parseInt(data.get("userId")));
        if(updatedUser.getHighStreak()==-1) throw new UnknownUserException();
        return ResponseEntity.ok(updatedUser);
    }

    @GetMapping("/changeAccuracy") //ADD JAVA SECURITY
    public ResponseEntity<User> changeAccuracy(@RequestBody Map<String,String> data) throws UnknownUserException {
        User updatedUser = userService.changeAccuracy(data.get("userId"), Integer.parseInt(data.get("accuracy")));
        if(updatedUser.getAccuracy()==-1) throw new UnknownUserException();
        return ResponseEntity.ok(updatedUser);
    }

    @GetMapping("/add") //ADD JAVA SECURITY
    public ResponseEntity<User> add(@RequestBody Map<String, String> data) throws UnknownUserException {
        User createdUser = userService.add(data.get("name"), data.get("password"), data.get("email"));
        return ResponseEntity.ok(createdUser);
    }

    @GetMapping("/delete") //ADD JAVA SECURITY
    public ResponseEntity<Integer> delete(@RequestBody Map<String, String> data) throws UnknownUserException {
        int deletedUser = userService.delete(data.get("userId"));
        if(deletedUser==1) throw new UnknownUserException();
        return ResponseEntity.ok(0);
    }

    @GetMapping("/makeGuess") //ADD JAVA SECURITY
    public ResponseEntity<Integer> makeGuess(@RequestBody Map<String, String> data) throws InvalidGuessException {
        userService.makeGuess(data.get("userId"), data.get("city"), data.get("guess"));
        return ResponseEntity.ok(0);
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