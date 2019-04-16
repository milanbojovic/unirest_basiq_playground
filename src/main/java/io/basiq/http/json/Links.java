package io.basiq.http.json;

@SuppressWarnings("unused")
public class Links {
	private String institution;
	private String self;
	private String connection;
	private String account;

	public Links(String institution, String self, String connection, String account) {
		this.institution = institution;
		this.self = self;
		this.connection = connection;
		this.account = account;
	}

	public String getInstitution() {
		return institution;
	}

	public void setInstitution(String institution) {
		this.institution = institution;
	}

	public String getSelf() {
		return self;
	}

	public void setSelf(String self) {
		this.self = self;
	}

	public String getConnection() {
		return connection;
	}

	public void setConnection(String connection) {
		this.connection = connection;
	}

	public String getAccount() {
		return account;
	}

	public void setAccount(String account) {
		this.account = account;
	}
}
