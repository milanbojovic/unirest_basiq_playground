package io.basiq.http.json;

public class CreateUser {
	String email;
	String mobile;

	public CreateUser(String email, String mobile) {
		this.email = email;
		this.mobile = mobile;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	@Override
	public String toString() {
		return "CreateUser [email=" + email + ", mobile=" + mobile + "]";
	}
}
