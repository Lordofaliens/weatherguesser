package com.heroku.java.Controllers;

import com.heroku.java.Entitites.User.User;
import com.heroku.java.Services.LeaderBoardService;
import com.heroku.java.Services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
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

    @GetMapping("/getNames")
    public List<String> getListOfNamesFromIds() {
        return leaderBoardService.getListOfNamesFromIds();
    }
}
