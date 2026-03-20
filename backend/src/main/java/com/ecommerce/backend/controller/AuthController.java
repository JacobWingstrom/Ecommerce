package com.ecommerce.backend.controller;

import java.sql.SQLException;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import dto.AccountRequest;
import dto.AccountResponse;
import dto.SignupResponse;
import model.Account;
import service.AuthService;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

	@PostMapping("/hello")
	public void hello(@RequestBody AccountRequest request) throws SQLException {
		System.out.println("hello");

	}	

	@PostMapping("/login")
	public ResponseEntity<?> login(@RequestBody AccountRequest request) throws SQLException {
		System.out.println(request.getUsername() + " " + request.getPassword());
		Account account = AuthService.signInUser(request.getUsername(), request.getPassword());
		if (account != null) {
			System.out.println("200");
            return ResponseEntity.ok(new AccountResponse(account));
        } else {
			System.out.println("500");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                                 .body("Invalid username or password");
        }
	}

	@PostMapping("/signup")
	public ResponseEntity<?> signup(@RequestBody AccountRequest request) throws SQLException {
		boolean success = AuthService.AddUser(request.getUsername(), request.getPassword());
		if (success) {
			System.out.println("200");
            return ResponseEntity.ok(new SignupResponse(success));
        } else {
			System.out.println("500");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                                 .body("Invalid username or password");
        }
	}

	// DONT KNOW WHAT TO DO YET WITH THIS
	@PostMapping("/logout")
	public boolean logout(@RequestBody AccountRequest request) {
		// CALL(MAKE) METHOD IN AuthService
		// return LogOutResponce -> NEED TO KNOW WHAT THEY WANT
		return false;
	}
}
