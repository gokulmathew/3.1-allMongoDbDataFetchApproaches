package com.allMongoDbDataFetchApproaches.controller;

import com.allMongoDbDataFetchApproaches.dto.DynamicAggRequest;
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

}
