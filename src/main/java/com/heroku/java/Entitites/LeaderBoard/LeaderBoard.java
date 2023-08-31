package com.heroku.java.Entitites.LeaderBoard;

import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.List;

@Document(collection = "leaderBoard")
public class LeaderBoard {
    @Id
    private ObjectId id;
    private int leaderBoardId;
    private List<String> users;
    private String dailyChallengeCity;
    private String dailyChallengeDifficulty;

    public int getLeaderBoardId() {
        return this.leaderBoardId;
    }
    public void setLeaderBoardId(int leaderBoardId) {
        this.leaderBoardId = leaderBoardId;
    }
    public List<String> getUsers() {
        return this.users;
    }
    public void setUsers(List<String> users) {
        this.users = users;
    }
    public String getDailyChallengeCity() {
        return this.dailyChallengeCity;
    }
    public void setDailyChallengeCity(String dailyChallengeCity) {
        this.dailyChallengeCity = dailyChallengeCity;
    }
    public String getDailyChallengeDifficulty() {
        return this.dailyChallengeDifficulty;
    }
    public void setDailyChallengeDifficulty(String dailyChallengeDifficulty) {
        this.dailyChallengeDifficulty = dailyChallengeDifficulty;
    }

    public LeaderBoard() {
        this.users = new ArrayList<>();
    }

    public LeaderBoard(ArrayList<String> users) {
        this.users = users;
    }

}
