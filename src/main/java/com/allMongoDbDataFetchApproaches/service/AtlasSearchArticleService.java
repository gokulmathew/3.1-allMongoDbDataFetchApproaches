package com.allMongoDbDataFetchApproaches.service;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.AggregateIterable;
import com.mongodb.client.model.search.SearchOperator;
import com.mongodb.client.model.search.SearchPath;
import com.mongodb.client.model.Aggregates;
import com.mongodb.client.model.Projections;
import org.bson.Document;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class AtlasSearchArticleService {

    private final MongoDatabase mongoDatabase;

    public AtlasSearchArticleService(MongoDatabase mongoDatabase) {
        this.mongoDatabase = mongoDatabase;
    }

    public List<Document> basicTextSearch(String textGiven) {

        // 1 — Get the collection
        MongoCollection<Document> collection =
                mongoDatabase.getCollection("AtlasSearch_Articles");

        // 2 — Build individual text operators (one per field)
        SearchOperator tTitle = SearchOperator.text(SearchPath.fieldPath("title"), textGiven);
        SearchOperator tBody  = SearchOperator.text(SearchPath.fieldPath("body"), textGiven);
        SearchOperator tTags  = SearchOperator.text(SearchPath.fieldPath("tags"), textGiven);

        // 3 — Combine them into a compound operator: should(...) takes a List<SearchOperator>
        SearchOperator textSearch = SearchOperator.compound()
                .should(List.of(tTitle, tBody, tTags));

        // 4 — Build aggregation pipeline
        AggregateIterable<Document> result = collection.aggregate(
                List.of(
                        Aggregates.search(textSearch),
                        Aggregates.project(
                                Projections.include("title", "body", "tags", "category", "rating")
                        )
                )
        );

        // Return as list
        return result.into(new ArrayList<>());
    }
}
