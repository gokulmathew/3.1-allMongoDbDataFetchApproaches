package com.allMongoDbDataFetchApproaches.service;

import com.allMongoDbDataFetchApproaches.model.StandardCrudQueryUser;
import com.allMongoDbDataFetchApproaches.repository.StandardCrudQueryUserRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StandardCrudQueryUserService {

    private final StandardCrudQueryUserRepository repo;

    public StandardCrudQueryUserService(StandardCrudQueryUserRepository repo) {
        this.repo = repo;
    }




}
