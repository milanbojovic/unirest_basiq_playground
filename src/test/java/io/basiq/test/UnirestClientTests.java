package io.basiq.test;

import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.util.Base64;

import org.junit.BeforeClass;
import org.junit.Test;

import io.basiq.http.UnirestClient;
import io.basiq.http.json.CreateConnectionRequest;
import io.basiq.http.json.CreateUserRequest;

public class UnirestClientTests {
	static String encodedApiKey = "TVdGaU5qUmtOekV0TjJVNFppMDBabUUwTFRrME1qY3ROekEyTm1VNE1UYzJaVEExT2pNMFlUTTRNV1F3TFdWa1pHRXRORGxtTlMwNE5UTmxMVGxtTUdaa1lXRTNaRE0yWVE9PQ==";
	static String apiKey;
	static UnirestClient httpClient;

	@BeforeClass
	public static void decodeApiKey() {
		byte[] decodedApiKey = Base64.getDecoder().decode(encodedApiKey);
		apiKey = new String(decodedApiKey);
		httpClient = new UnirestClient("https://au-api.basiq.io", apiKey);
	}

	@Test
	public void getAccessTokenTest() throws IOException {
		String accessToken = httpClient.getAccessToken();

		assertNotEquals("", accessToken);
		assertTrue(accessToken.length() > 0);
	}

	@Test
	public void createUserTest() throws IOException {
		String accessToken = httpClient.getAccessToken();

		CreateUserRequest createUserRequest = new CreateUserRequest("apiuser@mbsoftwaresolutions.com", "+381601111111");
		String userId = httpClient.createUser(accessToken, createUserRequest);

		assertNotEquals("", userId);
		assertTrue(userId.length() > 0);
	}

	@Test
	public void createUserBankConnectionTest() throws IOException {
		String accessToken = httpClient.getAccessToken();

		CreateUserRequest createUserRequest = new CreateUserRequest("apiuser@mbsoftwaresolutions.com", "+381601111111");
		String userId = httpClient.createUser(accessToken, createUserRequest);

		CreateConnectionRequest connectionRequest = new CreateConnectionRequest("Wentworth-Smith", "whislter", "AU00000");
		String connection = httpClient.createConnection(accessToken, connectionRequest, userId);
		assertNotEquals("", connection);
		assertTrue(connection.length() > 0);
	}
}
