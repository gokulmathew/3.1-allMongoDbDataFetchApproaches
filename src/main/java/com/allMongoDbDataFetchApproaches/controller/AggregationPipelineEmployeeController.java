package com.allMongoDbDataFetchApproaches.controller;

import com.allMongoDbDataFetchApproaches.dto.ArithmeticAggRequest;
import com.allMongoDbDataFetchApproaches.dto.ComparisonAggRequest;
import com.allMongoDbDataFetchApproaches.dto.DynamicAggRequest;
import com.allMongoDbDataFetchApproaches.dto.LogicalAggRequest;
import com.allMongoDbDataFetchApproaches.service.AggregationPipelineEmployeeService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/aggregation/emp")
public class AggregationPipelineEmployeeController {

    private final AggregationPipelineEmployeeService service;

    public AggregationPipelineEmployeeController(AggregationPipelineEmployeeService service) {
        this.service = service;
    }

    @GetMapping("/complete-aggreagtion-pipeline")
    public Object runPipeline1() {
        return service.runFullPipeline();
    }

    @PostMapping("/dynamic-query")
    public Object runDynamicQuery(@RequestBody DynamicAggRequest request) {
        return service.runDynamicAggregation(request);
    }

    @PostMapping("/comparison-query")
    public Object runComparisonQuery(@RequestBody ComparisonAggRequest request) {
        return service.runComparisonAggregation(request);
    }

    @PostMapping("/logical-query")
    public Object runLogicalQuery(@RequestBody LogicalAggRequest request) {
        return service.runLogicalAggregation(request);
    }

    @PostMapping("/arithmetic-query")
    public Object runArithmeticQuery(@RequestBody ArithmeticAggRequest request) {
        return service.runArithmeticAggregation(request);
    }


}
