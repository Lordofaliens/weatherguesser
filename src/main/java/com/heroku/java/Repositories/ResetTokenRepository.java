package com.heroku.java.Repositories;

import com.heroku.java.Entitites.ResetToken.ResetToken;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@EnableMongoRepositories
@Repository
public interface ResetTokenRepository extends MongoRepository<ResetToken, String> {
    Optional<ResetToken> findPasswordResetTokenByUserId(String userId);
    Optional<ResetToken> findPasswordResetTokenByToken(String token);
}
