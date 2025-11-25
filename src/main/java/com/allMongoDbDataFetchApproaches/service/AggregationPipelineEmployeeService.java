package com.allMongoDbDataFetchApproaches.service;

import com.allMongoDbDataFetchApproaches.dto.DynamicAggRequest;
import com.allMongoDbDataFetchApproaches.model.AggregationPipelineEmployee;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.stereotype.Service;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.*;
import org.springframework.data.domain.Sort;

import java.util.List;

@Service
public class AggregationPipelineEmployeeService {

    private final MongoTemplate mongoTemplate;

    public AggregationPipelineEmployeeService(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

    // -----------------------------------------
    // API 1: Full Aggregation Pipeline
    // -----------------------------------------
    public List<?> runFullPipeline() {

        MatchOperation match = Aggregation.match(
                Criteria.where("experience").gte(3)
        );

        GroupOperation group = Aggregation.group("location.city")
                .avg("salary").as("avgSalary")
                .avg("rating").as("avgRating")
                .count().as("employeeCount");

        ProjectionOperation project = Aggregation.project()
                .and("_id").as("city")
                .and("avgSalary").as("avgSalary")
                .and("avgRating").as("avgRating")
                .and("employeeCount").as("employeeCount")
                .andExclude("_id");

        SortOperation sort = Aggregation.sort(Sort.Direction.DESC, "avgSalary");

        LimitOperation limit = Aggregation.limit(2);

        SkipOperation skip = Aggregation.skip(0);

        Aggregation pipeline = Aggregation.newAggregation(
                match,
                group,
                project,
                sort,
                limit,
                skip
        );

        return mongoTemplate.aggregate(
                pipeline,
                "AggregationPipeline_Employees",
                Object.class
        ).getMappedResults();
    }


    public List<?> runDynamicAggregation(DynamicAggRequest req) {

        // 1. MATCH STAGE
        MatchOperation match = Aggregation.match(
                Criteria.where(req.getMatchField()).is(req.getMatchValue())
        );

        // 2. SORT STAGE
        Sort.Direction direction =
                req.getSortDirection().equalsIgnoreCase("asc") ?
                        Sort.Direction.ASC : Sort.Direction.DESC;

        SortOperation sort = Aggregation.sort(direction, req.getSortField());

        // 3. LIMIT STAGE
        LimitOperation limit = Aggregation.limit(req.getLimit());

        // 4. SKIP STAGE (optional)
        SkipOperation skip = (req.getSkip() != null)
                ? Aggregation.skip(req.getSkip())
                : Aggregation.skip(0);

        // Combine all
        Aggregation pipeline = Aggregation.newAggregation(
                match,
                sort,
                limit,
                skip
        );

        return mongoTemplate.aggregate(
                pipeline,
                "AggregationPipeline_Employees",
                Object.class
        ).getMappedResults();
    }

}
