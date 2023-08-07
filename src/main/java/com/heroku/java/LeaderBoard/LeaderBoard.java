package com.heroku.java.LeaderBoard;

import com.heroku.java.User.User;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.DocumentReference;

import java.util.List;

@Document(collection = "leaderboard")
public class LeaderBoard {
    @Id
    private String id;
    @DocumentReference
    private List<User> users;

    public String getId() {
        return this.id;
    }
    public void setId(String id) {
        this.id = id;
    }
    private List<User> getUsers() {
        return this.users;
    }
    private void setUsers(List<User> users) {
        this.users = users;
    }
}
