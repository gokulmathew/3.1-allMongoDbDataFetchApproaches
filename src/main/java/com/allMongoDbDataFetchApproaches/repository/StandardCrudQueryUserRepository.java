package com.allMongoDbDataFetchApproaches.repository;

import com.allMongoDbDataFetchApproaches.model.StandardCrudQueryUser;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface StandardCrudQueryUserRepository
        extends MongoRepository<StandardCrudQueryUser, String> {
}
