package com.allMongoDbDataFetchApproaches.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Data
@Document(collection = "AtlasSearch_Articles")
public class AtlasSearchArticle {

    @Id
    private String id;
    private String title;
    private String body;
    private List<String> tags;
    private String category;
    private String author;
    private double rating;
    private String publishedOn;

}
