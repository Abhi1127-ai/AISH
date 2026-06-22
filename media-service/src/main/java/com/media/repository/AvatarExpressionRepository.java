package com.media.repository;

import com.media.model.AvatarExpression;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AvatarExpressionRepository extends MongoRepository<AvatarExpression, String> {
    Optional<AvatarExpression> findFirstByEmotionOrderByCreatedAtDesc(String emotion);
}
