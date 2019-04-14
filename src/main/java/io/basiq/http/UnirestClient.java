package io.basiq.http;

import java.util.HashMap;
import java.util.Map;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;

public class UnirestClient {
	String webAddress;
	JsonParser parser;
	String apiKey;

	Map<HttpMethods, String> httpMap = new HashMap<HttpMethods, String>(){
		/**
		 * 
		 */
		private static final long serialVersionUID = 5031329264272758950L;

		{
			put(HttpMethods.TokenPOST, "/token");
		}
	};

	public UnirestClient(String webAddress, String apiKey) {
		super();
		this.parser = new JsonParser();
		this.apiKey = apiKey;
		this.webAddress = webAddress;
	}

	public String getAccessToken() {
		try {
			String url = webAddress + httpMap.get(HttpMethods.TokenPOST);
			HttpResponse<String> httpResponse = Unirest.post(url)
					.header("Authorization", "Basic " + apiKey)
					.header("Content-Type", "application/x-www-form-urlencoded")
					.header("basiq-version", "2.0")
					.body("scope=SERVER_ACCESS")
					.asString();

			if (httpResponse.getStatus() == 200) {
				JsonObject jsonResponse = parser.parse(httpResponse.getBody()).getAsJsonObject();
				return jsonResponse.get("access_token").getAsString();
			}
		} catch (UnirestException e) {
			e.printStackTrace();
		}
		return "";
	}

}
