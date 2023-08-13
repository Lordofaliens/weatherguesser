package com.heroku.java.Repositories;

import com.heroku.java.Entitites.User.User;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@EnableMongoRepositories
@Repository
public interface UserRepository extends MongoRepository<User, ObjectId> {
    Optional<User> findUserByUserId(String userId);

    Optional<User> findByName(String name);

    Optional<User> findByEmail(String email);

//    Optional<User> findByResetToken(String resetToken);
}
