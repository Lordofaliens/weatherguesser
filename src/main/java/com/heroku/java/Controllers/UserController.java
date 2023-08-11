package com.heroku.java.Controllers;

import com.heroku.java.Exceptions.InvalidGuessException;
import com.heroku.java.Exceptions.UnknownUserException;
import com.heroku.java.Services.UserService;
import com.heroku.java.Entitites.User.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
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

    @GetMapping("/changeName")
    public ResponseEntity<User> changeAccuracy(@RequestParam("userId") String userId, @RequestParam("name") String newName) throws UnknownUserException {
        User updatedUser = userService.changeName(userId, newName);
        if(updatedUser.getName()=="") throw new UnknownUserException();
        return ResponseEntity.ok(updatedUser);
    }

    @GetMapping("/changeHighStreak")
    public ResponseEntity<User> changeHighStreak(@RequestParam("userId") String userId) throws UnknownUserException {
        User updatedUser = userService.changeHighStreak(userId);
        if(updatedUser.getHighStreak()==-1) throw new UnknownUserException();
        return ResponseEntity.ok(updatedUser);
    }

    @GetMapping("/changeAccuracy")
    public ResponseEntity<User> changeAccuracy(@RequestParam("userId") String userId, @RequestParam("acc") int newAcc) throws UnknownUserException {
        User updatedUser = userService.changeAccuracy(userId, newAcc);
        if(updatedUser.getAccuracy()==-1) throw new UnknownUserException();
        return ResponseEntity.ok(updatedUser);
    }

    @GetMapping("/add")
    public ResponseEntity<User> add(@RequestParam("name") String name, @RequestParam("password") String password, @RequestParam("email") String email) throws UnknownUserException {
        User createdUser = userService.add(name, password, email);
        return ResponseEntity.ok(createdUser);
    }

    @GetMapping("/delete")
    public ResponseEntity<Integer> delete(@RequestParam("userId") String userId) throws UnknownUserException {
        int deletedUser = userService.delete(userId);
        if(deletedUser==1) throw new UnknownUserException();
        return ResponseEntity.ok(0);
    }

    @GetMapping("/makeGuess")
    public ResponseEntity<Integer> delete(@RequestParam("userId") String userId, @RequestParam("city") String city, @RequestParam("guess") String guess) throws InvalidGuessException {
        userService.makeGuess(userId, city, guess);
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