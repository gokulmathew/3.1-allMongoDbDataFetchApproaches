package com.allMongoDbDataFetchApproaches.repository;

import com.allMongoDbDataFetchApproaches.model.AtlasSearchArticle;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface AtlasSearchArticleRepository
        extends MongoRepository<AtlasSearchArticle, String> {
}
