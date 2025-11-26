package com.allMongoDbDataFetchApproaches.dto;

import lombok.Data;

@Data
public class AtlasMustSearchRequest {

    private String queryText;   // value to search
    private String fieldPath;   // field to search on (title/body/tags)
}
