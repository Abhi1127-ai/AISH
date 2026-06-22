package com.memory.repository;

import com.memory.model.UserFact;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserFactRepository extends MongoRepository<UserFact, String> {
    List<UserFact> findByUserId(String userId);
}
