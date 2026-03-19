package dto;

import model.Account;

public class AccountResponse {

	private String username;
	private String token;

	public AccountResponse(Account account) {
		if (account == null) {
			this.username = "";
			this.token = "";
		} else {
			this.username = account.getUsername();
			this.token = account.getToken();
		}
	}

	public String getUsername() {
		return this.username;
	}

	public String getToken() {
		return this.token;
	}
}
