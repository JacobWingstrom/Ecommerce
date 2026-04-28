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

		if (updateSuccessful) {
			return ResponseEntity.ok().build();
		} else {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid User Availibilty Request");
		}
	}

	@PostMapping("/getAvailibility")
	public ResponseEntity<?> getAvailability(@RequestBody AvailibiltyRequest request) throws SQLException {

		Availability availability = AvailibiltyService.getAvailability(request.getUserId());

		if (availability == null) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid User Availibilty Request");
		} else {
			return ResponseEntity.ok(new AvailibiltyResponse(request.getUserId(), availability));
		}
	}

}
