package com.allMongoDbDataFetchApproaches.controller;

import com.allMongoDbDataFetchApproaches.model.StandardCrudQueryUser;
import com.allMongoDbDataFetchApproaches.repository.StandardCrudQueryUserRepository;
import com.allMongoDbDataFetchApproaches.service.StandardCrudQueryUserService;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

//Note: Here only CRUD operations occurs

@RestController
@RequestMapping("/standard-crud/users")
public class StandardCrudQueryUserControllerPart2 {
    private final StandardCrudQueryUserRepository repo;
    private final StandardCrudQueryUserService service;
    private final MongoTemplate mongoTemplate;

    public StandardCrudQueryUserControllerPart2(StandardCrudQueryUserService service, StandardCrudQueryUserRepository repo, MongoTemplate mongoTemplate) {
        this.service = service;
        this.repo = repo;
        this.mongoTemplate = mongoTemplate;
    }

    //Projection
    @GetMapping("/projection/basic")
    public List<StandardCrudQueryUser> getUsersWithBasicFields() {
        Query query = new Query();
        query.fields().include("name");
        query.fields().include("city");
        query.fields().include("skills");
        // query.fields().exclude("_id");    - To exclude the id
        return mongoTemplate.find(query, StandardCrudQueryUser.class);
    }

    //Date Range
    @GetMapping("/date-range")
    public List<StandardCrudQueryUser> getUsersByDateRange(
            @RequestParam("start") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date start,
            @RequestParam("end") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date end
    ) {
        Query query = new Query(Criteria.where("joinedOn").gte(start).lte(end));
        return mongoTemplate.find(query, StandardCrudQueryUser.class);
    }


    @GetMapping("/paginated")
    public List<StandardCrudQueryUser> getPaginatedUsers(@RequestParam int page,@RequestParam int size,
   @RequestParam(defaultValue = "name") String sortBy,@RequestParam(defaultValue = "asc") String direction
    ) {
        Sort sort = direction.equalsIgnoreCase("asc") ?
                Sort.by(sortBy).ascending() :
                Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(page, size, sort);
        Query query = new Query().with(pageable);
        return mongoTemplate.find(query, StandardCrudQueryUser.class);
    }

    //Array Queries
    //$in
    @GetMapping("/array/in")
    public List<StandardCrudQueryUser> usersWithSkillsIn() {
        Query query = new Query( Criteria.where("skills").in("React", "AWS"));
        return mongoTemplate.find(query, StandardCrudQueryUser.class);
    }
     //$all
    @GetMapping("/array/all")
    public List<StandardCrudQueryUser> usersWithSkillsAll() {
        Query query = new Query(Criteria.where("skills").all("React", "Node"));
        return mongoTemplate.find(query, StandardCrudQueryUser.class);
    }
    // $elemMatch -  Match complex conditions inside array  => this will work only for when you array as objects in it
    @GetMapping("/array/elem-match")
    public List<StandardCrudQueryUser> usersWithElemMatch() {
        Query query = new Query(
                Criteria.where("scores").elemMatch(
                        Criteria.where("subject").is("Math")
                                .and("value").gt(80)));
        return mongoTemplate.find(query, StandardCrudQueryUser.class);
    }



}

