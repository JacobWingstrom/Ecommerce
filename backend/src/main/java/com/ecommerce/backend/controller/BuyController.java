package com.ecommerce.backend.controller;

import java.sql.SQLException;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import dto.AccountRequest;
import dto.ItemRequest;
import dto.ItemResponse;
import dto.ListingResponce;
import dto.UserListingRequest;
import model.Item;
import model.ListType;
import model.Listing;
import service.BuyService;
import service.UserService;

@RestController
@RequestMapping("/api/buy")
public class BuyController {

	// UserController -> get user info, update user name, update password, get items
	// bought, get items sold, get availability, update availability

	@PostMapping("/bids/active")
	public ResponseEntity<?> getAuctions(@RequestBody UserListingRequest request) throws SQLException {
		List<Item> items = BuyService.getListingType(request.getUsername(), request.getPageNum());

		if (items != null) {
			System.out.println("200");
			return ResponseEntity.ok(new ListingResponce(items));
		} else {
			System.out.println("500");
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Cannot obtain listings");
		}
	}

	@PostMapping("/bids/itemById")
	public ResponseEntity<?> getItemById(@RequestBody ItemRequest request) throws SQLException {
		Item item = BuyService.getItemById(request.getItemId());

		if (item != null) {
			System.out.println("200");
			return ResponseEntity.ok(new ItemResponse(item));
		} else {
			System.out.println("500");
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Cannot obtain listings");
		}
	}
}
