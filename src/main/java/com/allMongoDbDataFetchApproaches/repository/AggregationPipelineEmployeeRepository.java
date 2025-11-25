package com.allMongoDbDataFetchApproaches.repository;

import com.allMongoDbDataFetchApproaches.model.AggregationPipelineEmployee;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface AggregationPipelineEmployeeRepository
        extends MongoRepository<AggregationPipelineEmployee, String> {
}
