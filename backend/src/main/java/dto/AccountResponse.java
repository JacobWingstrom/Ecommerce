package dto;

import model.Account;

public class AccountResponse {

	private String username;
	private String token;
	private int userId;

	public AccountResponse(Account account) {
		if (account == null) {
			this.username = "";
			this.token = "";
			this.userId = -1;
		} else {
			this.username = account.getUsername();
			this.token = account.getToken();
			this.userId = account.getUserID();
		}
	}

	public String getUsername() {
		return this.username;
	}

	public String getToken() {
		return this.token;
	}

	public int getUserId() {
		return this.userId;
	}
}
