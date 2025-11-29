package com.example.public_api.service;

import com.example.public_api.dto.CreateListingRequest;
import com.example.public_api.dto.CreateUserRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class PublicApiService {
    @Autowired
    private RestTemplate restTemplate;

    private final String LISTING_URL = "http://localhost:8081/listings";
    private final String USER_URL = "http://localhost:8082/users";

    public Map<String, Object> getListings(Integer pageNum, Integer pageSize, Integer userId) {
        String url = UriComponentsBuilder.fromHttpUrl(LISTING_URL)
                .queryParam("pageNum", pageNum)
                .queryParam("pageSize", pageSize)
                .queryParam("userId", userId).toUriString();

        Map<String, Object> listingResponse = restTemplate.getForObject(url, Map.class);

        if (listingResponse != null && Boolean.TRUE.equals(listingResponse.get("result"))) {
            List<Map<String, Object>> listings = (List<Map<String, Object>>) listingResponse.get("listings");
            List<Map<String, Object>> enhancedListings = listings.stream().map(listing -> {
                Integer uId = (Integer) listing.get("userId");
                try {
                    Map<String, Object> userRes = restTemplate.getForObject(USER_URL + "/" + uId, Map.class);
                    if (userRes != null && Boolean.TRUE.equals(userRes.get("result"))) {
                        listing.put("user", userRes.get("user"));
                    }
                } catch (Exception e) {}
                listing.remove("userId");
                return listing;
            }).collect(Collectors.toList());
            listingResponse.put("listings", enhancedListings);
        }
        return listingResponse;
    }

    public Map<String, Object> createUser(CreateUserRequest request) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        map.add("name", request.getName());
        return restTemplate.postForObject(USER_URL, new HttpEntity<>(map, headers), Map.class);
    }

    public Map<String, Object> createListing(CreateListingRequest request) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        map.add("userId", String.valueOf(request.getUserId()));
        map.add("listingType", request.getListingType());
        map.add("price", String.valueOf(request.getPrice()));
        return restTemplate.postForObject(LISTING_URL, new HttpEntity<>(map, headers), Map.class);
    }
}