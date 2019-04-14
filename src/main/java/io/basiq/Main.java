package io.basiq;

import io.basiq.http.UnirestClient;
import io.basiq.http.json.CreateConnectionRequest;
import io.basiq.http.json.CreateUserRequest;

public class Main {

	public static void main(String[] args) {
		String apiKey = args[0];
		
		System.out.println("Program execution started: ");
		UnirestClient client = new UnirestClient("https://au-api.basiq.io", apiKey);

		String accessToken = client.getAccessToken();

		String userId = client.createUser(accessToken, new CreateUserRequest("apiuser@mbsoftwaresolutions.com", "+381601111111"));

		CreateConnectionRequest connectionRequest = new CreateConnectionRequest("Wentworth-Smith", "whislter", "AU00000");
		String connectionId = client.createConnection(accessToken, connectionRequest, userId);

		System.out.println("Acquired access_token: " + accessToken);
		System.out.println("Created new user: " + userId);
		System.out.println("Created new user bank connection: " + connectionId);
	}
}
