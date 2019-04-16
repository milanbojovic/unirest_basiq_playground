package io.basiq.http;

import java.util.logging.Logger;

import org.apache.http.Consts;
import org.apache.http.entity.ContentType;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;

public class UnirestHttpClient {
	private static final Logger LOGGER = Logger.getLogger(UnirestHttpClient.class.getName());

	final String HTTP_HEADER_AUTHORIZATION = "Authorization";
	final String HTTP_HEADER_AUTHORIZATION_BEARER = "Bearer ";
	final String HTTP_HEADER_AUTHORIZATION_BASIC = "Basic ";
	final String HTTP_HEADER_CONTENT_TYPE = "Content-Type";

	String apiKey;

	public UnirestHttpClient(String apiKey) {
		this.apiKey = apiKey;
	}

	public HttpResponse<String> httpGetAccessToken(String requestUrl) throws UnirestException {
		LOGGER.fine("Executing http POST request: " + requestUrl);
		HttpResponse<String> httpResponse = Unirest.post(requestUrl)
				.header(HTTP_HEADER_AUTHORIZATION, HTTP_HEADER_AUTHORIZATION_BASIC + apiKey)
				.header(HTTP_HEADER_CONTENT_TYPE, ContentType.APPLICATION_FORM_URLENCODED.withCharset(Consts.UTF_8).toString())
				.header("basiq-version", "2.0")
				.body("scope=SERVER_ACCESS").asString();

		return httpResponse;
	}

	public HttpResponse<String> executeHttpCreateUser(String accessToken, String requestUrl, String userJson)
			throws UnirestException {
		LOGGER.fine("Executing http POST request: " + requestUrl);
		HttpResponse<String> httpResponse = Unirest.post(requestUrl)
				.header(HTTP_HEADER_AUTHORIZATION, HTTP_HEADER_AUTHORIZATION_BEARER + accessToken)
				.header(HTTP_HEADER_CONTENT_TYPE, ContentType.APPLICATION_JSON.withCharset(Consts.UTF_8).toString())
				.body(userJson)
				.asString();
		return httpResponse;
	}

	public HttpResponse<String> httpCreateConnection(String accessToken, String requestUrl, String connectionJson)
			throws UnirestException {
		LOGGER.fine("Executing http POST request: " + requestUrl);
		HttpResponse<String> httpResponse = Unirest.post(requestUrl)
				.header(HTTP_HEADER_AUTHORIZATION, HTTP_HEADER_AUTHORIZATION_BEARER + accessToken)
				.header(HTTP_HEADER_CONTENT_TYPE, ContentType.APPLICATION_JSON.withCharset(Consts.UTF_8).toString())
				.body(connectionJson).asString();
		return httpResponse;
	}

	public HttpResponse<String> httpGetUsersConnectins(String accessToken, String requestUrl) throws UnirestException {
		LOGGER.fine("Executing http GET request: " + requestUrl);
		HttpResponse<String> httpResponse = Unirest.get(requestUrl)
				.header(HTTP_HEADER_AUTHORIZATION, HTTP_HEADER_AUTHORIZATION_BEARER + accessToken)
				.asString();
		return httpResponse;
	}

	public HttpResponse<String> httpGetUsersTransactions(String accessToken, String requestUrl)
			throws UnirestException {
		LOGGER.fine("Executing http GET request: " + requestUrl);
		HttpResponse<String> httpResponse = Unirest.get(requestUrl)
				.header(HTTP_HEADER_AUTHORIZATION, HTTP_HEADER_AUTHORIZATION_BEARER + accessToken)
				.header(HTTP_HEADER_CONTENT_TYPE, ContentType.APPLICATION_JSON.withCharset(Consts.UTF_8).toString())
				.asString();
		return httpResponse;
	}

}
