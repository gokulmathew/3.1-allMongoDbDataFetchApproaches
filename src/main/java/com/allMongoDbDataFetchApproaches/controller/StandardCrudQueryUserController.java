package com.allMongoDbDataFetchApproaches.controller;

import com.allMongoDbDataFetchApproaches.model.StandardCrudQueryUser;
import com.allMongoDbDataFetchApproaches.service.StandardCrudQueryUserService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/standard-crud/users")
public class StandardCrudQueryUserController {

    private final StandardCrudQueryUserService service;

    public StandardCrudQueryUserController(StandardCrudQueryUserService service) {
        this.service = service;
    }

    @PostMapping("/add")
    public StandardCrudQueryUser addUser(@RequestBody StandardCrudQueryUser user) {
        return service.saveUser(user);
    }

    @PostMapping("/add-many")
    public List<StandardCrudQueryUser> addUsers(@RequestBody List<StandardCrudQueryUser> users) {
        return service.saveUsers(users);
    }

    @GetMapping("/all")
    public List<StandardCrudQueryUser> getAll() {
        return service.getAllUsers();
    }
}
