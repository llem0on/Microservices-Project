package com.example.user_service.service;


import com.example.user_service.entity.User;
import com.example.user_service.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.Map;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    public Map<String, Object> getUsers(Integer pageNum, Integer pageSize) {
        PageRequest pageRequest = PageRequest.of(pageNum, pageSize, Sort.by("createdAt").descending());
        Map<String, Object> response = new HashMap<>();
        response.put("result", true);
        response.put("users", userRepository.findAll(pageRequest).getContent());
        return response;
    }

    public Map<String, Object> getUserById(Integer id) {
        Map<String, Object> response = new HashMap<>();
        userRepository.findById(id).ifPresentOrElse(
                user -> {
                    response.put("result", true);
                    response.put("user", user);
                },
                () -> response.put("result", false)
        );
        return response;
    }

    public Map<String, Object> createUser(String name) {
        User user = new User();
        user.setName(name);
        long now = ChronoUnit.MICROS.between(Instant.EPOCH, Instant.now());
        user.setCreatedAt(now);
        user.setUpdatedAt(now);
        userRepository.save(user);

        Map<String, Object> response = new HashMap<>();
        response.put("result", true);
        response.put("user", user);
        return response;
    }
}