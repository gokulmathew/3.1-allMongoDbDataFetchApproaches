package com.allMongoDbDataFetchApproaches.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "StandardCrudQuery_Users")
public class StandardCrudQueryUser {

    @Id
    private String id;

    private String name;
    private Integer age;
    private String city;
    private List<String> skills;

    private Experience experience;

    private Boolean isActive;
    private Date joinedOn;

    // ---- Nested Object ----
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Experience {
        private Integer years;
        private String domain;
    }
}
