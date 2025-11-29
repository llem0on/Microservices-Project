package com.example.public_api.dto;


import lombok.Data;
@Data
public class CreateListingRequest {
    private Integer userId;
    private String listingType;
    private Integer price;
}