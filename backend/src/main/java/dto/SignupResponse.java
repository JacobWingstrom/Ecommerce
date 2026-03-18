package dto;

public class SignupResponse {

	private boolean success;

	public SignupResponse(boolean success) {
		this.success = success;
	}

	public boolean isSuccess() {
		return success;
	}
}
