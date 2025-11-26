package com.allMongoDbDataFetchApproaches.service;

import com.allMongoDbDataFetchApproaches.dto.ArithmeticAggRequest;
import com.allMongoDbDataFetchApproaches.dto.ComparisonAggRequest;
import com.allMongoDbDataFetchApproaches.dto.DynamicAggRequest;
import com.allMongoDbDataFetchApproaches.dto.LogicalAggRequest;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.stereotype.Service;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.*;
import org.springframework.data.domain.Sort;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class AggregationPipelineEmployeeService {

    private final MongoTemplate mongoTemplate;

    public AggregationPipelineEmployeeService(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

    // API 1: Full Aggregation Pipeline
    public List<?> runFullPipeline() {

        MatchOperation match = Aggregation.match(
                Criteria.where("experience").gte(3)
        );

        GroupOperation group = Aggregation.group("location.city")
                .avg("salary").as("avgSalary")
                .avg("rating").as("avgRating")
                .count().as("employeeCount");

        ProjectionOperation project = Aggregation.project()
                .and("_id").as("city")
                .and("avgSalary").as("avgSalary")
                .and("avgRating").as("avgRating")
                .and("employeeCount").as("employeeCount")
                .andExclude("_id");

        SortOperation sort = Aggregation.sort(Sort.Direction.DESC, "avgSalary");

        LimitOperation limit = Aggregation.limit(2);

        SkipOperation skip = Aggregation.skip(0);

        Aggregation pipeline = Aggregation.newAggregation(
                match,
                group,
                project,
                sort,
                limit,
                skip
        );

        return mongoTemplate.aggregate(
                pipeline,
                "AggregationPipeline_Employees",
                Object.class
        ).getMappedResults();
    }

    public List<?> runDynamicAggregation(DynamicAggRequest req) {

        // 1. MATCH STAGE
        MatchOperation match = Aggregation.match(
                Criteria.where(req.getMatchField()).is(req.getMatchValue())
        );

        // 2. SORT STAGE
        Sort.Direction direction =
                req.getSortDirection().equalsIgnoreCase("asc") ?
                        Sort.Direction.ASC : Sort.Direction.DESC;

        SortOperation sort = Aggregation.sort(direction, req.getSortField());

        // 3. LIMIT STAGE
        LimitOperation limit = Aggregation.limit(req.getLimit());

        // 4. SKIP STAGE (optional)
        SkipOperation skip = (req.getSkip() != null)
                ? Aggregation.skip(req.getSkip())
                : Aggregation.skip(0);

        // Combine all
        Aggregation pipeline = Aggregation.newAggregation(
                match,
                sort,
                limit,
                skip
        );

        return mongoTemplate.aggregate(
                pipeline,
                "AggregationPipeline_Employees",
                Object.class
        ).getMappedResults();
    }

    // API  2: Dynamic Comparison Filter
    public List<?> runComparisonAggregation(ComparisonAggRequest req) {

        Criteria criteria = null;

        switch (req.getOperator().toLowerCase()) {
            case "gt":
                criteria = Criteria.where(req.getField()).gt(req.getValue());
                break;
            case "gte":
                criteria = Criteria.where(req.getField()).gte(req.getValue());
                break;
            case "lt":
                criteria = Criteria.where(req.getField()).lt(req.getValue());
                break;
            case "lte":
                criteria = Criteria.where(req.getField()).lte(req.getValue());
                break;
            case "eq":
                criteria = Criteria.where(req.getField()).is(req.getValue());
                break;
            case "ne":
                criteria = Criteria.where(req.getField()).ne(req.getValue());
                break;
            case "in":
                criteria = Criteria.where(req.getField()).in(req.getValues());
                break;
            default:
                throw new RuntimeException("Invalid operator");
        }

        MatchOperation match = Aggregation.match(criteria);

        // 2️⃣ SORT
        Sort.Direction direction =
                req.getSortDirection().equalsIgnoreCase("asc") ?
                        Sort.Direction.ASC : Sort.Direction.DESC;

        SortOperation sort = Aggregation.sort(direction, req.getSortField());

        // 3️⃣ LIMIT + SKIP
        LimitOperation limit = Aggregation.limit(req.getLimit());
        SkipOperation skip = Aggregation.skip(
                req.getSkip() == null ? 0 : req.getSkip()
        );

        // 4️⃣ Build Pipeline
        Aggregation pipeline = Aggregation.newAggregation(
                match,
                sort,
                limit,
                skip
        );

        // 5️⃣ Execute
        return mongoTemplate.aggregate(
                pipeline,
                "AggregationPipeline_Employees",
                Object.class
        ).getMappedResults();
    }

    // API  3: Dynamic Logical Operation Aggregation
    public List<?> runLogicalAggregation(LogicalAggRequest req) {

        // Build individual criteria from conditions
        List<Criteria> criteriaList = req.getConditions().stream().map(cond -> {
            String field = cond.get("field").toString();
            String op = cond.get("operator").toString();
            Object value = cond.get("value");

            switch (op.toLowerCase()) {
                case "gt": return Criteria.where(field).gt(value);
                case "gte": return Criteria.where(field).gte(value);
                case "lt": return Criteria.where(field).lt(value);
                case "lte": return Criteria.where(field).lte(value);
                case "eq": return Criteria.where(field).is(value);
                case "ne": return Criteria.where(field).ne(value);
                default: throw new RuntimeException("Invalid comparison operator");
            }
        }).collect(Collectors.toList());

        // 1️⃣ Build Logical Operator
        Criteria logicalCriteria;

        switch (req.getOperator().toLowerCase()) {
            case "and":
                logicalCriteria = new Criteria().andOperator(criteriaList.toArray(new Criteria[0]));
                break;
            case "or":
                logicalCriteria = new Criteria().orOperator(criteriaList.toArray(new Criteria[0]));
                break;
            case "nor":
                logicalCriteria = new Criteria().norOperator(criteriaList.toArray(new Criteria[0]));
                break;
            case "not":
                // NOT applies to only ONE condition
                if (criteriaList.size() != 1) {
                    throw new RuntimeException("NOT operator requires exactly 1 condition");
                }
                logicalCriteria = new Criteria().norOperator(criteriaList.get(0));
                break;
            default:
                throw new RuntimeException("Invalid logical operator");
        }

        MatchOperation match = Aggregation.match(logicalCriteria);

        // 2️⃣ Sort
        Sort.Direction direction =
                req.getSortDirection().equalsIgnoreCase("asc") ?
                        Sort.Direction.ASC :
                        Sort.Direction.DESC;

        SortOperation sort = Aggregation.sort(direction, req.getSortField());

        // 3️⃣ Limit + Skip
        LimitOperation limit = Aggregation.limit(req.getLimit());
        SkipOperation skip = Aggregation.skip(req.getSkip() == null ? 0 : req.getSkip());

        // 4️⃣ Build Pipeline
        Aggregation pipeline = Aggregation.newAggregation(
                match,
                sort,
                limit,
                skip
        );

        return mongoTemplate.aggregate(
                pipeline,
                "AggregationPipeline_Employees",
                Object.class
        ).getMappedResults();
    }

    // API  4: Dynamic Arithmetic Operation Aggregation
    public List<?> runArithmeticAggregation(ArithmeticAggRequest req) {

        ProjectionOperation project;

        switch (req.getOperator().toLowerCase()) {

            case "add":
                project = Aggregation.project()
                        .and(ArithmeticOperators.Add.valueOf(req.getField())
                                .add(req.getValue()))
                        .as(req.getResultField());
                break;

            case "subtract":
                project = Aggregation.project()
                        .and(ArithmeticOperators.Subtract.valueOf(req.getField())
                                .subtract(req.getValue()))
                        .as(req.getResultField());
                break;

            case "multiply":
                project = Aggregation.project()
                        .and(ArithmeticOperators.Multiply.valueOf(req.getField())
                                .multiplyBy(req.getValue()))
                        .as(req.getResultField());
                break;

            case "divide":
                project = Aggregation.project()
                        .and(ArithmeticOperators.Divide.valueOf(req.getField())
                                .divideBy(req.getValue()))
                        .as(req.getResultField());
                break;

            default:
                throw new RuntimeException("Invalid operator");
        }

        SortOperation sort = Aggregation.sort(
                req.getSortDirection().equalsIgnoreCase("asc")
                        ? Sort.Direction.ASC
                        : Sort.Direction.DESC,
                req.getSortField()
        );

        LimitOperation limit = Aggregation.limit(req.getLimit());
        SkipOperation skip = Aggregation.skip(req.getSkip() == null ? 0 : req.getSkip());

        Aggregation pipeline = Aggregation.newAggregation(
                project,
                sort,
                limit,
                skip
        );

        return mongoTemplate.aggregate(
                pipeline,
                "AggregationPipeline_Employees",
                Object.class
        ).getMappedResults();
    }

}
