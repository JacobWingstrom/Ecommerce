package com.ecommerce.backend.controller;

import java.sql.SQLException;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import dto.AccountRequest;
import dto.ListingResponce;
import dto.NewListingResponse;
import model.Item;
import service.ItemService;
import service.UserService;

@RestController
@RequestMapping("/api/item")
public class ItemController {

	// ItemController -> get store items, get newly listed items, get items on
	// market, add item, update item, bid on item, mark item sold
	@PostMapping("/createListing")
	public ResponseEntity<?> createListing(
		@RequestPart("image") MultipartFile image,
    	@RequestPart("title") String title,
    	@RequestPart("minimumPrice") String price,
    	@RequestPart("description") String description,
    	@RequestPart("endDate") String endDate,
		@RequestHeader("Authorization") String authHeader
	) throws SQLException {

		if (ItemService.AddListing(title, description, price, endDate, image, authHeader.substring(7))) {
			System.out.println("add listing 200");
			return ResponseEntity.ok(new NewListingResponse(true));
		} else {
			System.out.println("500");
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error with creating listing");
		}
	}
}
