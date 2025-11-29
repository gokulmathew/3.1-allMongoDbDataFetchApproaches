package com.allMongoDbDataFetchApproaches.config;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoDatabase;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MongoDriverConfig {

    @Value("${spring.data.mongodb.uri}")
    private String mongoUri;

    @Value("${spring.data.mongodb.database}")
    private String dbName;

    @Bean
    public MongoClient mongoDriverClient() {
        return MongoClients.create(mongoUri);
    }

    @Bean
    public MongoDatabase mongoDriverDatabase(MongoClient mongoDriverClient) {
        return mongoDriverClient.getDatabase(dbName);
    }
}
