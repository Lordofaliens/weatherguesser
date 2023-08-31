package com.heroku.java.Repositories;

import com.heroku.java.Entitites.PasswordResetToken.PasswordResetToken;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@EnableMongoRepositories
@Repository
public interface PasswordResetTokenRepository extends MongoRepository<PasswordResetToken, String> {
    Optional<PasswordResetToken> findPasswordResetTokenByUserId(String userId);
    Optional<PasswordResetToken> findPasswordResetTokenByToken(String token);
}
