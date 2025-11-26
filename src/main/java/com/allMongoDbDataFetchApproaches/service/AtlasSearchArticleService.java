package com.allMongoDbDataFetchApproaches.service;

import com.allMongoDbDataFetchApproaches.dto.AtlasMustSearchRequest;
import com.allMongoDbDataFetchApproaches.dto.AtlasShouldFilterRequest;
import com.allMongoDbDataFetchApproaches.model.AtlasSearchArticle;
import org.bson.Document;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationOperation;
import org.springframework.data.mongodb.core.aggregation.ProjectionOperation;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

@Service
public class AtlasSearchArticleService {

    private final MongoTemplate mongoTemplate;

    public AtlasSearchArticleService(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

    public List<?> basicTextSearch(String searchText) {

        // Build dynamic Atlas $search stage
        Document searchStage = new Document("$search",
                new Document("index", "default")
                        .append("text",
                                new Document("query", searchText)
                                        .append("path", Arrays.asList("title", "body", "tags"))
                        )
        );

        // Wrap into AggregationOperation
        AggregationOperation searchOp = context -> searchStage;

        // Projection for clean output
        ProjectionOperation project = Aggregation.project("title", "body", "tags", "category", "rating");

        Aggregation agg = Aggregation.newAggregation(
                searchOp,
                project
        );

        return mongoTemplate.aggregate(
                agg,
                "AtlasSearch_Articles",
                Object.class
        ).getMappedResults();
    }


    public List<?> mustSearch(AtlasMustSearchRequest req) {

        // Build dynamic MUST query
        Document mustStage = new Document("$search",
                new Document("index", "default")
                        .append("compound",
                                new Document("must",
                                        Arrays.asList(
                                                new Document("text",
                                                        new Document("query", req.getQueryText())
                                                                .append("path", req.getFieldPath())
                                                )
                                        )
                                )
                        )
        );

        AggregationOperation searchOp = context -> mustStage;

        ProjectionOperation project = Aggregation.project("title", "body", "tags", "category", "rating");

        Aggregation agg = Aggregation.newAggregation(
                searchOp,
                project
        );

        return mongoTemplate.aggregate(
                agg,
                "AtlasSearch_Articles",
                Object.class
        ).getMappedResults();
    }

    public List<?> shouldFilterSearch(AtlasShouldFilterRequest req) {

        // Build dynamic should + filter Atlas Search query
        Document searchStage = new Document("$search",
                new Document("index", "default")
                        .append("compound",
                                new Document("should",
                                        Arrays.asList(
                                                new Document("text",
                                                        new Document("query", req.getShouldText())
                                                                .append("path", req.getShouldPath())
                                                )
                                        )
                                )
                                        .append("filter",
                                                Arrays.asList(
                                                        new Document("text",
                                                                new Document("query", req.getFilterValue())
                                                                        .append("path", req.getFilterField())
                                                        )
                                                )
                                        )
                        )
        );

        AggregationOperation searchOp = context -> searchStage;

        ProjectionOperation project = Aggregation.project("title", "body", "tags", "category", "rating");

        Aggregation agg = Aggregation.newAggregation(
                searchOp,
                project
        );

        return mongoTemplate.aggregate(
                agg,
                "AtlasSearch_Articles",
                Object.class
        ).getMappedResults();
    }

}
