package dto;

public class AvailibiltyRequest {

	private String token;
	private boolean[][] availibilty;

	public AvailibiltyRequest() {

	}

	public String getToken() {
		return this.token;
	}

	public boolean[][] getAvailibilty() {
		return this.availibilty;
	}

}