package dto;

import model.Account;

public class AccountResponse {

	private String username;
	private String area;
	private String token;

	public AccountResponse(Account account) {
		this.username = account.getUsername();
		this.area = account.getArea();
	}

	public String getUsername() {
		return this.username;
	}

	public String getArea() {
		return this.area;
	}

	public String getToken() {
		return this.token;
	}
}
