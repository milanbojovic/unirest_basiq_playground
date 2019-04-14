package io.basiq.test;

import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.util.Base64;

import org.junit.Test;

import io.basiq.http.UnirestClient;

public class UnirestClientTests {

	@Test
	public void getAccessTokenTest() throws IOException {
		byte[] decodedApiKey = Base64.getDecoder().decode("TVdGaU5qUmtOekV0TjJVNFppMDBabUUwTFRrME1qY3ROekEyTm1VNE1UYzJaVEExT2pNMFlUTTRNV1F3TFdWa1pHRXRORGxtTlMwNE5UTmxMVGxtTUdaa1lXRTNaRE0yWVE9PQ==");
		String apiKey = new String(decodedApiKey);

		UnirestClient client = new UnirestClient("https://au-api.basiq.io", apiKey);
		String accessToken = client.getAccessToken();

		assertNotEquals("", accessToken);
		assertTrue(accessToken.length() > 0);
	}
}
