package com.heroku.java.Entitites.LeaderBoard;

import com.heroku.java.Entitites.User.User;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.DocumentReference;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Document(collection = "leaderBoard")
public class LeaderBoard {
    @Id
    private ObjectId id;
    private int leaderBoardId;

    private List<String> users;

    public int getLeaderBoardId() {
        return this.leaderBoardId;
    }
    public void setLeaderBoardId(int leaderBoardId) {
        this.leaderBoardId = leaderBoardId;
    }
    private List<String> getUsers() {
        return this.users;
    }
    private void setUsers(List<String> users) {
        this.users = users;
    }

    public LeaderBoard() {
        this.users = new ArrayList<String>();
    }

    public LeaderBoard(ArrayList<String> users) {
        this.users = users;
    }

    public void updateLeaderBoard() {}; //UPDATE EVERYDAY 00:00 GMT
}
