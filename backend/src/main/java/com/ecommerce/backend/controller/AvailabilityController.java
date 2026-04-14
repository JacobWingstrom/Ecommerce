package com.ecommerce.backend.controller;

import java.sql.SQLException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import dto.AvailibiltyRequest;
import dto.AvailibiltyResponse;

@RestController
@RequestMapping("/api/availibilty")
public class AvailabilityController {

	@PostMapping("/updateAvailibilty")
	public ResponseEntity<?> updateAvailibilty(@RequestBody AvailibiltyRequest request) throws SQLException {

		// Something something = AvailibiltyService.something(request.something);

		if (false) {
			System.out.println("200");
			return ResponseEntity.ok(new AvailibiltyResponse());
		} else {
			System.out.println("500");
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid User Availibilty Request");
		}
	}

	@PostMapping("/getAvailibility")
	public ResponseEntity<?> getAvailibility(@RequestBody AvailibiltyRequest request) throws SQLException {

		if (false) {
			System.out.println("200");
			return ResponseEntity.ok(new AvailibiltyResponse());
		} else {
			System.out.println("500");
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid User Availibilty Request");
		}
	}

}
