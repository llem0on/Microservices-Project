package com.example.public_api.controller;


import com.example.public_api.dto.CreateListingRequest;
import com.example.public_api.dto.CreateUserRequest;
import com.example.public_api.service.PublicApiService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.Map;

@RestController
@RequestMapping("/public-api")
public class PublicApiController {
    @Autowired
    private PublicApiService publicApiService;

    @GetMapping("/listings")
    public Map<String, Object> getListings(
            @RequestParam(defaultValue = "1") Integer pageNum,
            @RequestParam(defaultValue = "10") Integer pageSize,
            @RequestParam(required = false) Integer userId) {
        return publicApiService.getListings(pageNum, pageSize, userId);
    }

    @PostMapping("/users")
    public Map<String, Object> createUser(@RequestBody CreateUserRequest request) {
        return publicApiService.createUser(request);
    }

    @PostMapping("/listings")
    public Map<String, Object> createListing(@RequestBody CreateListingRequest request) {
        return publicApiService.createListing(request);
    }
}