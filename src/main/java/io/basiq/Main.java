package io.basiq;

import io.basiq.http.UnirestClient;
import io.basiq.http.json.CreateUserRequest;

public class Main {

	public static void main(String[] args) {
		String apiKey = args[0];
		
		System.out.println("Program execution started: ");
		UnirestClient client = new UnirestClient("https://au-api.basiq.io", apiKey);

		String accessToken = client.getAccessToken();

		String userId = client.createUser(accessToken, new CreateUserRequest("apiuser@mbsoftwaresolutions.com", "+381601111111"));

		System.out.println(accessToken);
		System.out.println(userId);
	}
}
