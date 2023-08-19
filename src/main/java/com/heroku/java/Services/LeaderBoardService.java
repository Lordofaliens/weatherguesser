package com.heroku.java.Services;

import com.heroku.java.Entitites.LeaderBoard.LeaderBoard;
import com.heroku.java.Entitites.User.User;
import com.heroku.java.Entitites.Weather.Weather;
import com.heroku.java.Repositories.LeaderBoardRepository;
import com.heroku.java.Repositories.UserRepository;
import com.heroku.java.Repositories.WeatherRepository;
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
    private WeatherRepository weatherRepository;
    @Autowired
    private MongoTemplate mongoTemplate;

    public List<String> sortNewLeaderBoard() {
        List<User> userList = userRepository.findAll();
        Collections.sort(userList, Comparator.comparingInt(User::getHighStreak).reversed());
        for(int i = 0; i < userList.size(); i++) {
            userList.get(i).setRating(i+1);
            mongoTemplate.update(User.class)
                    .matching(Criteria.where("userId").is(userList.get(i).getUserId()))
                    .apply(new Update().set("rating", userList.get(i).getRating()))
                    .first();
        }
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

    public String[] getDailyWeather() {
        LeaderBoard lb = leaderBoardRepository.findLeaderBoardByLeaderBoardId(0).get();
        String[] res = new String[] {lb.getDailyChallengeCity(), lb.getDailyChallengeDifficulty()};
        return res;
    }

    public String[] updateDailyWeather() {
        List<Weather> weatherList = weatherRepository.findAll();
        int num = weatherList.size();
        Random random = new Random();
        int randomIdx = random.nextInt(num);
        String city = weatherList.get(randomIdx).getLocation();
        String difficulty = weatherList.get(randomIdx).getDifficulty();
        mongoTemplate.update(LeaderBoard.class)
                .matching(Criteria.where("leaderBoardId").is(0))
                .apply(new Update().set("dailyChallengeCity", city).set("dailyChallengeDifficulty", difficulty))
                .first();
        return new String[] {city, difficulty};
    }
}
