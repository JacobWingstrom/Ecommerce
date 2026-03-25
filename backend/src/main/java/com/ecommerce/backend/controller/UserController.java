package com.ecommerce.backend.controller;

import java.sql.SQLException;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import dto.AccountRequest;
import dto.ListingResponce;
import model.ListType;
import model.Listing;
import service.UserService;

public class UserController {

	// UserController -> get user info, update user name, update password, get items
	// bought, get items sold, get availability, update availability

	@PostMapping("/Bids/Current")
	public ResponseEntity<?> currentBids(@RequestBody AccountRequest request) throws SQLException {
		Listing items = UserService.getListingType(request.getUsername(), ListType.CurrentBidItems);

		if (items != null) {
			System.out.println("200");
			return ResponseEntity.ok(new ListingResponce(items));
		} else {
			System.out.println("500");
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid username or password");
		}
	}

	@PostMapping("/Selling/Current")
	public ResponseEntity<?> currentSelling(@RequestBody AccountRequest request) throws SQLException {
		Listing items = UserService.getListingType(request.getUsername(), ListType.CurrentItemsSold);

		if (items != null) {
			System.out.println("200");
			return ResponseEntity.ok(new ListingResponce(items));
		} else {
			System.out.println("500");
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid username or password");
		}
	}

}
