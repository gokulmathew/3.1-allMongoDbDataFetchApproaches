package com.allMongoDbDataFetchApproaches.controller;

import com.allMongoDbDataFetchApproaches.model.StandardCrudQueryUser;
import com.allMongoDbDataFetchApproaches.repository.StandardCrudQueryUserRepository;
import com.allMongoDbDataFetchApproaches.service.StandardCrudQueryUserService;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Criteria;

//Note: Here only CRUD operations occurs

@RestController
@RequestMapping("/standard-crud/users")
public class StandardCrudQueryUserController {
    private final StandardCrudQueryUserRepository repo;
    private final StandardCrudQueryUserService service;
    private final MongoTemplate mongoTemplate;

    public StandardCrudQueryUserController(StandardCrudQueryUserService service, StandardCrudQueryUserRepository repo, MongoTemplate mongoTemplate) {
        this.service = service;
        this.repo = repo;
        this.mongoTemplate = mongoTemplate;
    }

    //    Add Data
    @PostMapping("/add")
    public StandardCrudQueryUser addUser(@RequestBody StandardCrudQueryUser user) {
        return repo.save(user);
    }

    @PostMapping("/add-many")
    public List<StandardCrudQueryUser> addUsers(@RequestBody List<StandardCrudQueryUser> users) {
        return repo.saveAll(users);
    }

    // Reading Data
    @GetMapping("/all")
    public List<StandardCrudQueryUser> getAll() {
        return repo.findAll();
    }

    @GetMapping("/{id}")
    public StandardCrudQueryUser getUserById(@PathVariable String id) {
        return repo.findById(id).orElse(null);
    }

    // Data Deletion
    //  Deletion by id
    @DeleteMapping("/{id}")
    public String deleteUserById(@PathVariable String id) {
        repo.deleteById(id);
        return "User deleted with id: " + id;
    }

    //  Deletion by based on a condition
    @DeleteMapping("/delete-by-city")
    public String deleteUsersByCity(@RequestParam String city) {
        Query query = new Query(Criteria.where("city").is(city));
        long deleted = mongoTemplate.remove(query, StandardCrudQueryUser.class).getDeletedCount();
        return deleted + " user(s) deleted from city: " + city;
    }


    //  Data Update
    // Update based on id
    @PatchMapping("/{id}/update-city")
    public StandardCrudQueryUser updateUserCity(@PathVariable String id, @RequestParam String city
    ) {
        Query query = new Query(Criteria.where("_id").is(id));
        Update update = new Update().set("city", city);
        mongoTemplate.updateFirst(query, update, StandardCrudQueryUser.class);
        return repo.findById(id).orElse(null);
    }

    //Updating nested field based on a id
    @PatchMapping("/{id}/update-experience-domain")
    public StandardCrudQueryUser updateExperienceDomain(@PathVariable String id, @RequestParam String domain
    ) {
        Query query = new Query(Criteria.where("_id").is(id));
        Update update = new Update().set("experience.domain", domain);
        mongoTemplate.updateFirst(query, update, StandardCrudQueryUser.class);
        return repo.findById(id).orElse(null);
    }

}

