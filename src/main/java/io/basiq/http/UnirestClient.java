package io.basiq.http;

import java.util.HashMap;
import java.util.Map;

import org.apache.http.Consts;
import org.apache.http.entity.ContentType;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;

import io.basiq.http.json.CreateConnectionRequest;
import io.basiq.http.json.CreateUserRequest;

public class UnirestClient {
	final String HTTP_HEADER_AUTHORIZATION = "Authorization";
	final String HTTP_HEADER_AUTHORIZATION_BEARER = "Bearer ";
	final String HTTP_HEADER_AUTHORIZATION_BASIC = "Basic ";
	final String HTTP_HEADER_CONTENT_TYPE = "Content-Type";

	String webAddress;
	String apiKey;

	JsonParser parser;
	Gson gson;


	Map<HttpMethodsEnum, String> httpMap = new HashMap<HttpMethodsEnum, String>(){
		/**
		 * 
		 */
		private static final long serialVersionUID = 5031329264272758950L;

		{
			put(HttpMethodsEnum.GetToken, "/token");
			put(HttpMethodsEnum.CreateUser, "/users");
			put(HttpMethodsEnum.CreateGetConnection, "/users/{id}/connections");
		}
	};

	public UnirestClient(String webAddress, String apiKey) {
		this.webAddress = webAddress;
		this.apiKey = apiKey;

		this.parser = new JsonParser();
		this.gson = new Gson();
	}

	public String getAccessToken() {
		String result = null;
		try {
			String url = webAddress + httpMap.get(HttpMethodsEnum.GetToken);
			HttpResponse<String> httpResponse = Unirest.post(url)
					.header(HTTP_HEADER_AUTHORIZATION, HTTP_HEADER_AUTHORIZATION_BASIC + apiKey)
					.header(HTTP_HEADER_CONTENT_TYPE, ContentType.APPLICATION_FORM_URLENCODED.withCharset(Consts.UTF_8).toString())
					.header("basiq-version", "2.0")
					.body("scope=SERVER_ACCESS")
					.asString();

			if (httpResponse.getStatus() == 200) {
				JsonObject jsonResponse = parser.parse(httpResponse.getBody()).getAsJsonObject();
				result = jsonResponse.get("access_token").getAsString();
			} else {
				throw new UnirestException("Error while invoking HTTP POST request for acquiring new access_token. Response body: [" + httpResponse.getBody() + "]");
			}
		} catch (UnirestException e) {
			e.printStackTrace();
		}
		return result;
	}

	public String createUser(String accessToken, CreateUserRequest user) {
		String result = null;
		try {
			String requestUrl = webAddress + httpMap.get(HttpMethodsEnum.CreateUser);
			String userJson = gson.toJson(user, CreateUserRequest.class);

			HttpResponse<String> httpResponse = Unirest.post(requestUrl)
					.header(HTTP_HEADER_AUTHORIZATION, HTTP_HEADER_AUTHORIZATION_BEARER + accessToken)
					.header(HTTP_HEADER_CONTENT_TYPE, ContentType.APPLICATION_JSON.withCharset(Consts.UTF_8).toString())
					.body(userJson)
					.asString();

			if (httpResponse.getStatus() == 201) {
				JsonObject jsonResponse = parser.parse(httpResponse.getBody()).getAsJsonObject();
				result = jsonResponse.get("id").getAsString();
			} else {
				throw new UnirestException("Error while invoking HTTP POST request for creating new user. Response body: [" + httpResponse.getBody() + "]");
			}
		} catch (UnirestException e) {
			e.printStackTrace();
		}
		return result;
	}

	public String createConnection(String accessToken, CreateConnectionRequest connectionRequest, String userId) {
		String result = null;
		try {
			String connectionEndpoint = httpMap.get(HttpMethodsEnum.CreateGetConnection).replace("{id}", userId);
			String requestUrl = webAddress + connectionEndpoint;
			String connectionJson = gson.toJson(connectionRequest, CreateConnectionRequest.class);

			HttpResponse<String> httpResponse = Unirest.post(requestUrl)
					.header(HTTP_HEADER_AUTHORIZATION, HTTP_HEADER_AUTHORIZATION_BEARER + accessToken)
					.header(HTTP_HEADER_CONTENT_TYPE, ContentType.APPLICATION_JSON.withCharset(Consts.UTF_8).toString())
					.body(connectionJson)
					.asString();

				if (httpResponse.getStatus() == 201 || httpResponse.getStatus() == 202) {
					result = getConnectionsForUser(accessToken, userId);
			} else {
				throw new UnirestException("Error while invoking HTTP POST request for connecting user to bank institution. Response body: [" + httpResponse.getBody() + "]");
			}
		} catch (UnirestException e) {
			e.printStackTrace();
		}
		return result;
	}

	private String getConnectionsForUser(String accessToken, String userId) {
		String result = null;
		try {
			String connectionEndpoint = httpMap.get(HttpMethodsEnum.CreateGetConnection).replace("{id}", userId);
			String requestUrl = webAddress + connectionEndpoint;

			HttpResponse<String> httpResponse = Unirest.get(requestUrl)
					.header(HTTP_HEADER_AUTHORIZATION, HTTP_HEADER_AUTHORIZATION_BEARER + accessToken)
					.asString();

				if (httpResponse.getStatus() == 200) {
					JsonObject jsonResponse = parser.parse(httpResponse.getBody()).getAsJsonObject();
					JsonArray data = jsonResponse.get("data").getAsJsonArray();

					//Wait for job completion
					int max_retries = 10;
					while (data.size() == 0 && max_retries > 0) {
						Thread.sleep(1000);
						//Repeat HTTP request
						httpResponse = Unirest.get(requestUrl)
								.header(HTTP_HEADER_AUTHORIZATION, HTTP_HEADER_AUTHORIZATION_BEARER + accessToken)
								.asString();
						jsonResponse = parser.parse(httpResponse.getBody()).getAsJsonObject();
						data = jsonResponse.get("data").getAsJsonArray();
						max_retries--;
					}
					result = data.get(0).getAsJsonObject().get("id").getAsString();
			} else {
				throw new UnirestException("Error while invoking HTTP GET request for listing user's connections. Response body: [" + httpResponse.getBody() + "]");
			}
		} catch (UnirestException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		return result;
	}
}
