package com.ecommerce.backend.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/test")
public class TestController {

	// Create an AuthController
	// Create an AuthService
	// Wrap Database class
	// Define JSON response format
	// Define JSON request format
	// Add CORS configuration
	// build user‑specific end points

	@GetMapping
	public String hello() {
		return "Backend is running!";
	}
}
