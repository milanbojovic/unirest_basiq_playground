package io.basiq.http.json;

public class CreateUserRequest {
	String email;
	String mobile;

	public CreateUserRequest(String email, String mobile) {
		this.email = email;
		this.mobile = mobile;
	}
}
