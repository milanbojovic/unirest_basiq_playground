package io.basiq.http.json;

public class CreateConnectionRequest {
	String loginId;
	String password;
	Institution institution;

	public CreateConnectionRequest(String loginId, String password, String institutionId) {
		this.loginId = loginId;
		this.password = password;
		this.institution = new Institution(institutionId);
	}
}
