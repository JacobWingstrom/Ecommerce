package com.ecommerce.backend.controller;

import java.sql.SQLException;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import dto.ListingResponce;
import dto.UserListingRequest;
import model.Item;
import model.ListType;
import service.UserService;

@RestController
@RequestMapping("/api/user")
public class UserController {

	// UserController -> get user info, update user name, update password, get items
	// bought, get items sold, get availability, update availability

	@PostMapping("/Bids/Current")
	public ResponseEntity<?> currentBids(@RequestBody UserListingRequest request) throws SQLException {
		List<Item> items = UserService.getListingType(request.getUsername(), ListType.CurrentBidItems,
				request.getPageNum());

		if (items != null) {
			System.out.println("200");
			return ResponseEntity.ok(new ListingResponce(items));
		} else {
			System.out.println("500");
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid username or password");
		}
	}

	@PostMapping("/Selling/Current")
	public ResponseEntity<?> currentSelling(@RequestBody UserListingRequest request) throws SQLException {
		List<Item> items = UserService.getListingType(request.getUsername(), ListType.CurrentItemsOnMarket,
				request.getPageNum());

		if (items != null) {
			System.out.println("200");
			return ResponseEntity.ok(new ListingResponce(items));
		} else {
			System.out.println("500");
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid username or password");
		}
	}

	@PostMapping("/Bids/Won")
	public ResponseEntity<?> itemsWon(@RequestBody UserListingRequest request) throws SQLException {
		List<Item> items = UserService.getListingType(request.getUsername(), ListType.AllItemsWon,
				request.getPageNum());

		if (items != null) {
			System.out.println("200");
			return ResponseEntity.ok(new ListingResponce(items));
		} else {
			System.out.println("500");
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid username or password");
		}
	}
}
