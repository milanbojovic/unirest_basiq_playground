package io.basiq;

import io.basiq.http.UnirestClient;

public class Main {

	public static void main(String[] args) {
		String apiKey = args[0];
		
		UnirestClient client = new UnirestClient("https://au-api.basiq.io", apiKey);
		System.out.println(client.getAccessToken());
	}
}
