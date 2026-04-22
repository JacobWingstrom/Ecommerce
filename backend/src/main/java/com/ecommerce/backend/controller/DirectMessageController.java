package com.ecommerce.backend.controller;

import java.sql.SQLException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import dto.DirectMessageRequest;
import dto.DirectMessageResponse;

@RestController
@RequestMapping("/api/directMessage")
public class DirectMessageController {

	/*
		/allMessages : need to return all conversations that the user currently has
		/getMessageById 
	*/
	@PostMapping("/getAvailibility")
	public ResponseEntity<?> getAvailibility(@RequestBody DirectMessageRequest request) throws SQLException {

		// Something something = DirectMessageService.something(request.something);

		if (false) {
			System.out.println("200");
			return ResponseEntity.ok(new DirectMessageResponse());
		} else {
			System.out.println("500");
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid User Direct Message Request");
		}
	}
}
