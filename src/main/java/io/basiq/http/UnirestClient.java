package io.basiq.http;

import java.util.HashMap;
import java.util.Map;

import org.apache.http.Consts;
import org.apache.http.entity.ContentType;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;

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
				return result;
			} else {
				throw new UnirestException("Error while invoking HTTP POST request for acquiring new access_token. Response body: [" + httpResponse.getBody() + "]");
			}
		} catch (UnirestException e) {
			e.printStackTrace();
		}
		return result;
	}

	public String createUser(String accessToken, CreateUserRequest user) {
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
				return jsonResponse.get("id").getAsString();
			} else {
				throw new UnirestException("Error while invoking HTTP GET request for creating new user. Response body: [" + httpResponse.getBody() + "]");
			}
		} catch (UnirestException e) {
			e.printStackTrace();
		}
		return "";
	}
}
