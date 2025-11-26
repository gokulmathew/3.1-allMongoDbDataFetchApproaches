package com.allMongoDbDataFetchApproaches.dto;

import lombok.Data;

import java.util.List;

@Data
public class ComparisonAggRequest {

    private String field;          // salary, experience, rating
    private String operator;       // gt, gte, lt, lte, eq, ne, in
    private Object value;          // single value (for gt, eq, etc.)
    private List<Object> values;   // list of values (for IN operator)

    private String sortField;
    private String sortDirection;
    private Integer limit;
    private Integer skip;

}
