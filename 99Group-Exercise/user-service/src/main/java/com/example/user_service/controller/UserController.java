package com.example.user_service.controller;


import com.example.user_service.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.Map;

@RestController
public class UserController {
    @Autowired
    private UserService userService;

    @GetMapping("/users")
    public Map<String, Object> getUsers(@RequestParam(defaultValue = "1") Integer pageNum, @RequestParam(defaultValue = "10") Integer pageSize) {
        return userService.getUsers(pageNum - 1, pageSize);
    }

    @GetMapping("/users/{id}")
    public Map<String, Object> getUser(@PathVariable Integer id) {
        return userService.getUserById(id);
    }

    @PostMapping("/users")
    public Map<String, Object> createUser(@RequestParam String name) {
        return userService.createUser(name);
    }
}