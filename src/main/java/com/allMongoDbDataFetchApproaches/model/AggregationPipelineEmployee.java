package com.allMongoDbDataFetchApproaches.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;
import java.util.List;
import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "AggregationPipeline_Employees")
public class AggregationPipelineEmployee {

    private String _id;          // using string id (emp101...)
    private String name;
    private String role;
    private Integer experience;
    private Integer salary;
    private Integer bonusPct;
    private List<String> skills;
    private List<Project> projects;
    private Date joiningDate;
    private Map<String, String> location;
    private Double rating;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Project {
        private String name;
        private Integer durationMonths;
        private String status;
    }
}
