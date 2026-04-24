package com.ecommerce.backend.controller;

import java.sql.SQLException;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import dto.DirectMessageRequest;
import service.AuctionHandler;
import dto.DirectMessageResponse;
import model.Account;
import model.Database;
import service.DirectMessageService;

@RestController
@RequestMapping("/api/directMessage")
public class DirectMessageController {

	@GetMapping("/testCloseAuctions")
	public ResponseEntity<?> testCloseAuctions() {
		AuctionHandler.closeExpiredAuctions();
		return ResponseEntity.ok("done");
	}

	@GetMapping("/conversations")
	public ResponseEntity<?> getConversations(@RequestHeader("Authorization") String authHeader) throws SQLException {
		Account account = Database.getUserByToken(authHeader.substring(7));
		if (account == null)
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid token");

		List<DirectMessageResponse> conversations = DirectMessageService.getAllConversationsForUser(account.getUserID());
		return ResponseEntity.ok(conversations);
	}

	@GetMapping("/messages/{conversationId}")
	public ResponseEntity<?> getMessages(@PathVariable int conversationId,
			@RequestHeader("Authorization") String authHeader) throws SQLException {

		Account account = Database.getUserByToken(authHeader.substring(7));
		if (account == null)
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid token");

		DirectMessageResponse response = DirectMessageService.getConversationMessages(conversationId, account.getUserID());
		if (!response.isSuccess())
			return ResponseEntity.status(HttpStatus.FORBIDDEN).body(response.getMessage());

		return ResponseEntity.ok(response);
	}

	@PostMapping("/send")
	public ResponseEntity<?> sendMessage(@RequestBody DirectMessageRequest request,
			@RequestHeader("Authorization") String authHeader) throws SQLException {

		Account account = Database.getUserByToken(authHeader.substring(7));
		if (account == null)
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid token");

		DirectMessageResponse response = DirectMessageService.sendMessage(
			request.getConversationId(), account.getUserID(), request.getContent()
		);
		if (!response.isSuccess())
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response.getMessage());

		return ResponseEntity.ok(response);
	}

	@PostMapping("/createForItem")
	public ResponseEntity<?> createForItem(@RequestBody DirectMessageRequest request,
			@RequestHeader("Authorization") String authHeader) throws SQLException {

		Account account = Database.getUserByToken(authHeader.substring(7));
		if (account == null)
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid token");

		DirectMessageResponse response = DirectMessageService.startItemConversation(
			account.getUserID(), request.getItemId(), request.getContent()
		);
		if (!response.isSuccess())
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response.getMessage());

		return ResponseEntity.ok(response);
	}

	@PostMapping("/create")
	public ResponseEntity<?> createConversation(@RequestBody DirectMessageRequest request,
			@RequestHeader("Authorization") String authHeader) throws SQLException {

		Account account = Database.getUserByToken(authHeader.substring(7));
		if (account == null)
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid token");

		DirectMessageResponse response = DirectMessageService.createConversationAndSendMessage(
			account.getUserID(), request.getRecipientId(), request.getContent()
		);
		if (!response.isSuccess())
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response.getMessage());

		return ResponseEntity.ok(response);
	}
}
