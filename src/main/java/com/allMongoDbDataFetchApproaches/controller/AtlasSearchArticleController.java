package com.allMongoDbDataFetchApproaches.controller;

import com.allMongoDbDataFetchApproaches.dto.AtlasMustSearchRequest;
import com.allMongoDbDataFetchApproaches.dto.AtlasShouldFilterRequest;
import com.allMongoDbDataFetchApproaches.service.AtlasSearchArticleService;
import com.allMongoDbDataFetchApproaches.service.AtlasSearchArticleServiceusignRawJSON;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/atlas/articles")
public class AtlasSearchArticleController {

    private final AtlasSearchArticleServiceusignRawJSON service;

    private final AtlasSearchArticleService service2;

    public AtlasSearchArticleController(AtlasSearchArticleServiceusignRawJSON service, AtlasSearchArticleService service2) {
        this.service = service;
        this.service2 = service2;
    }


// usign Search API

    @GetMapping("/search_searchAPI")
    public Object basicTextSearchApi(@RequestParam String text) {
        return service2.basicTextSearch(text);
    }




    /// Below Queyr made using Raw JSON
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
