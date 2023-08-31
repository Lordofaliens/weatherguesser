package com.heroku.java.Repositories;

import com.heroku.java.Entitites.LeaderBoard.LeaderBoard;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@EnableMongoRepositories
@Repository
public interface LeaderBoardRepository extends MongoRepository<LeaderBoard, ObjectId> {
    Optional<LeaderBoard> findLeaderBoardByLeaderBoardId(int leaderBoardId);
}