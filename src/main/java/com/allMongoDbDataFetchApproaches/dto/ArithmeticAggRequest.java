package com.allMongoDbDataFetchApproaches.dto;

import lombok.Data;

@Data
public class ArithmeticAggRequest {

    private String operator;     // add, subtract, multiply, divide
    private String field;        // salary, bonusPct, experience
    private Number value;        // constant to apply (e.g., + 20000)
    private String resultField;  // name of new computed field

    private String sortField;
    private String sortDirection;
    private Integer limit;
    private Integer skip;

}
