package com.ecommerce.backend.controller;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import dto.AccountRequest;
import dto.BidRequest;
import dto.ItemRequest;
import dto.ItemResponse;
import dto.ListingResponce;
import dto.UserListingRequest;
import model.Account;
import model.Bid;
import model.Item;
import model.ListType;
import model.Listing;
import service.AuthService;
import service.BuyService;
import service.UserService;

@RestController
@RequestMapping("/api/buy")
public class BuyController {

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

	@PostMapping("/placeBid")
	public ResponseEntity<?> setBid(@RequestBody BidRequest request, @RequestHeader("Authorization") String authHeader)
			throws SQLException {

		try {
			String token = authHeader.substring(7);

			Account account = AuthService.getUserFromToken(token);
			if (account == null) {
                System.out.println("acct is null");
				return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid token");
			}

			int itemId = Integer.parseInt(request.getItemId());
			BigDecimal bidAmount = new BigDecimal(request.getBid());

			Item item = BuyService.getItemById(itemId);
			if (item == null) {
                System.out.println("Item not found");
				return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Item not found");
			}

			Bid bid = new Bid(account.getUserID(), itemId, bidAmount, LocalDateTime.now());

			Item updatedItem = BuyService.setBid(bid);

			if (updatedItem != null) {
                System.out.println("Item Updated");
                System.out.println(updatedItem.getItemId() + " " + updatedItem.getHighestBid().toString());
			    return ResponseEntity.ok(new ItemResponse(updatedItem));
			} else {
                System.out.println("Bid was not high enough");
				return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Bid was not high enough");
			}

		} catch (NumberFormatException e) {
            System.out.println("Invalid bid or itemId format");
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid bid or itemId format");
		} catch (Exception e) {
            System.out.println("Error placing bid");
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error placing bid");
		}
	}
}
