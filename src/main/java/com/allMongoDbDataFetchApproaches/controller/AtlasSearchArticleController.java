package com.allMongoDbDataFetchApproaches.controller;

import com.allMongoDbDataFetchApproaches.dto.AtlasMustSearchRequest;
import com.allMongoDbDataFetchApproaches.dto.AtlasShouldFilterRequest;
import com.allMongoDbDataFetchApproaches.service.AtlasSearchArticleService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/atlas/articles")
public class AtlasSearchArticleController {

    private final AtlasSearchArticleService service;

    public AtlasSearchArticleController(AtlasSearchArticleService service) {
        this.service = service;
    }

    @GetMapping("/search")
    public Object basicTextSearch(@RequestParam String text) {
        return service.basicTextSearch(text);
    }

    @PostMapping("/must")
    public Object mustSearch(@RequestBody AtlasMustSearchRequest request) {
        return service.mustSearch(request);
    }

    @PostMapping("/should-filter")
    public Object shouldFilterSearch(@RequestBody AtlasShouldFilterRequest request) {
        return service.shouldFilterSearch(request);
    }

}
