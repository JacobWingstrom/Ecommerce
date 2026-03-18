package com.ecommerce.backend.controller;

import java.sql.SQLException;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import model.Account;
import service.AuthService;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

	// DO I NEED TO RETURN AN OBJECT?
	@PostMapping("/login")
	public boolean login(@RequestBody AccountRequest account) {
		return false;
	}

	@PostMapping("/signup")
	public String signup(@RequestBody AccountRequest request) throws SQLException {

		if (AuthService.AddUser(request.getUsername(), request.getPassword(), request.getArea())) {
			return "Signup Successful";

		}
		return "Signup Unsuccessful";
	}

	@PostMapping("/logout")
	public boolean logout(@RequestBody Account account) {
		return false;
	}
}

class AccountRequest {

	private String username;
	private String password;
	private String area;

	public AccountRequest() {
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getArea() {
		return area;
	}

	public void setArea(String area) {
		this.area = area;
	}
}
