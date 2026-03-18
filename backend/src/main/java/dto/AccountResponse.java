package dto;

import model.Account;

public class AccountResponse {

	private String username;
	private String token;

	public AccountResponse(Account account) {
		this.username = account.getUsername();
		this.token = "";
	}

	public String getUsername() {
		return this.username;
	}

	public String getToken() {
		return this.token;
	}
}
