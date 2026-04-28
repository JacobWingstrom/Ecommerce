package dto;

public class AvailibiltyRequest {

	private String token;
	private String userId;
	private boolean[][] availibilty;

	public AvailibiltyRequest() {

	}

	public String getUserId() {
		return this.userId;
	}

	public String getToken() {
		return this.token;
	}

	public boolean[][] getAvailibilty() {
		return this.availibilty;
	}

}