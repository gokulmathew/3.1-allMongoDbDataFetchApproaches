package com.allMongoDbDataFetchApproaches.dto;

import lombok.Data;

@Data
public class DynamicAggRequest {

    private String matchField;
    private Object matchValue;
    private String sortField;
    private String sortDirection;
    private Integer limit;
    private Integer skip;

}
