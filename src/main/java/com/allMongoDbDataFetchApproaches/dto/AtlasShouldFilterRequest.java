package com.allMongoDbDataFetchApproaches.dto;

import lombok.Data;

@Data
public class AtlasShouldFilterRequest {

    private String shouldText;     // text query for should condition
    private String shouldPath;     // field to run should on

    private String filterField;    // field to filter
    private Object filterValue;    // filter value

}
