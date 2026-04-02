package dto;

public class NewListingResponse {

	private boolean success;

	public NewListingResponse(boolean success) {
		this.success = success;
	}

	public boolean isSuccess() {
		return success;
	}
}
