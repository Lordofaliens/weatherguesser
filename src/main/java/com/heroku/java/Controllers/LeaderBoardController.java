package com.heroku.java.Controllers;

import com.heroku.java.Entitites.User.User;
import com.heroku.java.Exceptions.UnknownUserException;
import com.heroku.java.Services.LeaderBoardService;
import com.heroku.java.Services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


@RestController
@RequestMapping("/api/leaderboard")
public class LeaderBoardController {
    @Autowired
    private LeaderBoardService leaderBoardService;

    @GetMapping("/getLeaderBoard")
    public ResponseEntity<List<User>> getLeaderBoard() {
        return new ResponseEntity<>(this.leaderBoardService.getListOfUsersFromIds(), HttpStatus.OK);
    }

    @GetMapping("/getDailyChallenge")
    public ResponseEntity<String[]> getDailyChallenge() {
        return new ResponseEntity<>(this.leaderBoardService.getDailyWeather(), HttpStatus.OK);
    }

    @GetMapping("/updateDailyChallenge")
    public ResponseEntity<String[]> updateDailyChallenge() {
        return new ResponseEntity<>(this.leaderBoardService.updateDailyWeather(), HttpStatus.OK);
    }
}