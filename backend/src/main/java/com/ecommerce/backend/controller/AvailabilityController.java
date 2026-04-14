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
import model.Availability;
import service.AvailibiltyService;

@RestController
@RequestMapping("/api/availibilty")
public class AvailabilityController {

	@PostMapping("/updateAvailibilty")
	public ResponseEntity<?> updateAvailability(@RequestBody AvailibiltyRequest request) throws SQLException {

		boolean updateSuccessful = AvailibiltyService.updateAvailibilty(request.getToken(), request.getAvailibilty());

		Availability availability = AvailibiltyService.getAvailability(request.getToken());

		if (updateSuccessful) {
			System.out.println("200");
			return ResponseEntity.ok(new AvailibiltyResponse(request.getToken(), availability));
		} else {
			System.out.println("500");
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid User Availibilty Request");
		}
	}

	@PostMapping("/getAvailibility")
	public ResponseEntity<?> getAvailability(@RequestBody AvailibiltyRequest request) throws SQLException {

		Availability availability = AvailibiltyService.getAvailability(request.getToken());

		if (false) {
			System.out.println("200");
			return ResponseEntity.ok(new AvailibiltyResponse(request.getToken(), availability));
		} else {
			System.out.println("500");
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid User Availibilty Request");
		}
	}

}
