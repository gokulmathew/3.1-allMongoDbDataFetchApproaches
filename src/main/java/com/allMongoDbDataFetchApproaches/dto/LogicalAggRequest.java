package com.allMongoDbDataFetchApproaches.dto;

import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
public class LogicalAggRequest {

    private String operator;   // and, or, not, nor
    private List<Map<String, Object>> conditions;
    // Each condition: { "field": "salary", "operator": "gt", "value": 1000000 }

    private String sortField;
    private String sortDirection;
    private Integer limit;
    private Integer skip;

}
