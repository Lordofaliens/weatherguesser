package com.heroku.java.Services;

import com.heroku.java.Entitites.LeaderBoard.LeaderBoard;
import com.heroku.java.Entitites.User.User;
import com.heroku.java.Entitites.Weather.Weather;
import com.heroku.java.Repositories.LeaderBoardRepository;
import com.heroku.java.Repositories.UserRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class LeaderBoardService {
    @Autowired
    private LeaderBoardRepository leaderBoardRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private MongoTemplate mongoTemplate;

    public List<String> sortNewLeaderBoard() {
        List<User> userList = userRepository.findAll();
        Collections.sort(userList, Comparator.comparingInt(User::getRating));

        List<String> topUsersIds = userList.stream()
                .limit(100)
                .map(User::getUserId)
                .collect(Collectors.toList());

        mongoTemplate.update(LeaderBoard.class)
                .matching(Criteria.where("leaderBoardId").is(0))
                .apply(new Update().set("users", topUsersIds))
                .first();

        return topUsersIds;
    }

    public List<User> getListOfUsersFromIds() {
        List<String> ids = sortNewLeaderBoard();
        List<User> ans = new ArrayList<>();
        for(String id : ids) {
            Optional<User> u = userRepository.findUserByUserId(id);
            if(u.isPresent()) {
                ans.add(u.get());
            }
        }
        return ans;
    }
}
