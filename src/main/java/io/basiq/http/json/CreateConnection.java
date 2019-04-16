package io.basiq.http.json;

public class CreateConnection {
	String loginId;
	String password;
	Institution institution;

	public CreateConnection(String loginId, String password, String institutionId) {
		this.loginId = loginId;
		this.password = password;
		this.institution = new Institution(institutionId);
	}

	public String getLoginId() {
		return loginId;
	}

	public void setLoginId(String loginId) {
		this.loginId = loginId;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public Institution getInstitution() {
		return institution;
	}

	public void setInstitution(Institution institution) {
		this.institution = institution;
	}

	@Override
	public String toString() {
		return "CreateConnection [loginId=" + loginId + ", password=" + password + ", institution=" + institution + "]";
	}
}
