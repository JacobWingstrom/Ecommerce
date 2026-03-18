package com.ecommerce.backend.controller;

import java.sql.SQLException;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import dto.AccountRequest;
import dto.AccountResponse;
import dto.SignupResponse;
import model.Account;
import service.AuthService;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

	@PostMapping("/login")
	public AccountResponse login(@RequestBody AccountRequest request) throws SQLException {
		Account account = AuthService.signInUser(request.getUsername(), request.getPassword());
		return new AccountResponse(account);
	}

	@PostMapping("/signup")
	public SignupResponse signup(@RequestBody AccountRequest request) throws SQLException {
		boolean success = AuthService.AddUser(request.getUsername(), request.getPassword());
		return new SignupResponse(success);
	}

	// DONT KNOW WHAT TO DO YET WITH THIS
	@PostMapping("/logout")
	public boolean logout(@RequestBody AccountRequest request) {
		// CALL(MAKE) METHOD IN AuthService
		// return LogOutResponce -> NEED TO KNOW WHAT THEY WANT
		return false;
	}
}
