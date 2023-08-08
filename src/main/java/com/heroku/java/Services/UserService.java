package com.heroku.java.Services;

import com.heroku.java.Repositories.UserRepository;
import com.heroku.java.User.User;
import com.heroku.java.Weather.Weather;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private MongoTemplate mongoTemplate;

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public Optional<User> getSingleUser(String userId) {
        return userRepository.findUserByUserId(userId);
    }

    public User changeName(String userId, String newName) {
        User uNew = new User();
        Optional<User> u = this.getSingleUser(userId);
        u.ifPresent((res) -> {
            u.get().setName(newName);

            mongoTemplate.update(User.class)
                    .matching(Criteria.where("userId").is(u.get().getUserId()))
                    .apply(new Update().set("name", u.get().getName()))
                    .first();

            uNew.setUserId(u.get().getUserId());
            uNew.setName(u.get().getName());
        });

        if (!u.isPresent()) {
            uNew.setName("");
        }
        return uNew;
    }

    public User changeHighStreak(String userId) {
        User uNew = new User();
        Optional<User> u = this.getSingleUser(userId);
        u.ifPresent((res) -> {
            u.get().setHighStreak(u.get().getHighStreak()+1);

            mongoTemplate.update(User.class)
                    .matching(Criteria.where("userId").is(u.get().getUserId()))
                    .apply(new Update().set("highStreak", u.get().getHighStreak()))
                    .first();

            uNew.setUserId(u.get().getUserId());
            uNew.setHighStreak(u.get().getHighStreak());
        });

        if (!u.isPresent()) {
            uNew.setHighStreak(-1);
        }
        return uNew;
    }

    public User changeAccuracy(String userId, int newAcc) {
        User uNew = new User();
        Optional<User> u = this.getSingleUser(userId);
        u.ifPresent((res) -> {
            u.get().setAccuracy(newAcc);

            mongoTemplate.update(User.class)
                    .matching(Criteria.where("userId").is(u.get().getUserId()))
                    .apply(new Update().set("accuracy", u.get().getAccuracy()))
                    .first();

            uNew.setUserId(u.get().getUserId());
            uNew.setAccuracy(u.get().getAccuracy());
        });

        if (!u.isPresent()) uNew.setAccuracy(-1);

        return uNew;
    }

    public User add(String name, String password) {
        User u = new User(name, password);
        mongoTemplate.insert(u);

        return u;
    }

    public int delete(String userId) {
        Optional<User> u = this.getSingleUser(userId);

        if (u.isPresent()) {
            mongoTemplate.remove(Query.query(Criteria.where("userId").is(userId)), User.class);
            return 0;
        } else return 1;
    }
}
