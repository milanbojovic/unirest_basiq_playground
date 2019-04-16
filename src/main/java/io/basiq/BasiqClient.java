package io.basiq;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.exceptions.UnirestException;

import io.basiq.http.UnirestHttpClient;
import io.basiq.http.enumerations.HttpMethods;
import io.basiq.http.json.CreateConnection;
import io.basiq.http.json.CreateUser;
import io.basiq.http.json.Transaction;

public class BasiqClient {
	private static final Logger LOGGER = Logger.getLogger(BasiqClient.class.getName());

	String webAddress;
	UnirestHttpClient httpClient;
	JsonParser parser;
	Gson gson;

	Map<HttpMethods, String> httpMap = new HashMap<HttpMethods, String>(){
		private static final long serialVersionUID = 5031329264272758950L;
		{
			put(HttpMethods.GetToken, "/token");
			put(HttpMethods.CreateUser, "/users");
			put(HttpMethods.CreateConnection, "/users/{user.id}/connections");
			put(HttpMethods.GetConnection, "/users/{user.id}/connections");
			put(HttpMethods.GetTransactions, "/users/{user.id}/transactions");
		}
	};

	Map<HttpMethods, String> errMsgMap = new HashMap<HttpMethods, String>() {
		private static final long serialVersionUID = 5031329264272758933L;
		{
			put(HttpMethods.GetToken,
					"Error while invoking HTTP POST request for acquiring new access_token.");
			put(HttpMethods.CreateUser,
					"Error while invoking HTTP POST request for creating new user.");
			put(HttpMethods.CreateConnection,
					"Error while invoking HTTP POST request for connecting user to bank institution.");
			put(HttpMethods.GetConnection,
					"Error while invoking HTTP GET request for listing user's connections.");
			put(HttpMethods.GetTransactions,
					"Error while invoking HTTP GET request for listing user's transactions.");
		}
	};

	public BasiqClient(String webAddress, String apiKey) {
		this.webAddress = webAddress;
		this.httpClient = new UnirestHttpClient(apiKey);
		this.parser = new JsonParser();
		this.gson = new Gson();
	}

	public String getAccessToken() {
		String result = null;
		try {
			LOGGER.fine("Creating accessToken");
			String requestUrl = webAddress + httpMap.get(HttpMethods.GetToken);
			HttpResponse<String> httpResponse = httpClient.httpGetAccessToken(requestUrl);

			checkHttpResponseCode(httpResponse, 200, errMsgMap.get(HttpMethods.GetToken));

			JsonObject jsonResponse = parser.parse(httpResponse.getBody()).getAsJsonObject();
			result = jsonResponse.get("access_token").getAsString();
		} catch (UnirestException e) {
			LOGGER.severe(e.toString());
			e.printStackTrace();
		}
		return result;
	}

	public String createUser(String accessToken, CreateUser user) {
		String result = null;
		try {
			LOGGER.fine("Creating user " + user.toString());
			String requestUrl = webAddress + httpMap.get(HttpMethods.CreateUser);
			String userJson = gson.toJson(user, CreateUser.class);
			HttpResponse<String> httpResponse = httpClient.executeHttpCreateUser(accessToken, requestUrl, userJson);

			checkHttpResponseCode(httpResponse, 201, errMsgMap.get(HttpMethods.CreateUser));

			JsonObject jsonResponse = parser.parse(httpResponse.getBody()).getAsJsonObject();
			result = jsonResponse.get("id").getAsString();
		} catch (UnirestException e) {
			LOGGER.severe(e.toString());
			e.printStackTrace();
		}
		return result;
	}

	public String createConnection(String accessToken, CreateConnection connectionRequest, String userId) {
		String result = null;
		try {
			LOGGER.fine(
					"Creating connection for userid " + userId + " connection request " + connectionRequest.toString());
			String connectionEndpoint = httpMap.get(HttpMethods.CreateConnection).replace("{user.id}", userId);
			String requestUrl = webAddress + connectionEndpoint;
			String connectionJson = gson.toJson(connectionRequest, CreateConnection.class);
			HttpResponse<String> httpResponse = httpClient.httpCreateConnection(accessToken, requestUrl,
					connectionJson);
			checkHttpResponseCode(httpResponse, 202, errMsgMap.get(HttpMethods.CreateConnection));
			result = getUsersConnections(accessToken, userId);
		} catch (UnirestException e) {
			LOGGER.severe(e.toString());
			e.printStackTrace();
		}
		return result;
	}

	public String getUsersConnections(String accessToken, String userId) {
		String result = null;
		try {
			LOGGER.fine("Getting connections for userId: " + userId);
			String connectionEndpoint = httpMap.get(HttpMethods.GetConnection).replace("{user.id}", userId);
			String requestUrl = webAddress + connectionEndpoint;
			HttpResponse<String> httpResponse = httpClient.httpGetUsersConnectins(accessToken, requestUrl);

			checkHttpResponseCode(httpResponse, 200, errMsgMap.get(HttpMethods.GetConnection));
			JsonArray data = getData(httpResponse);

			// Wait for job completion
			int max_retries = 10;
			while (data.size() == 0 && max_retries > 0) {
				Thread.sleep(1000);
				// Repeat HTTP request
				httpResponse = httpClient.httpGetUsersConnectins(accessToken, requestUrl);
				data = getData(httpResponse);
				max_retries--;
			}
			result = data.get(0).getAsJsonObject().get("id").getAsString();

		} catch (Exception e) {
			LOGGER.severe(e.toString());
			e.printStackTrace();
		}
		return result;
	}

	public TransactionsSummary getUsersTransactions(String accessToken, String userId) {
		TransactionsSummary transactionSummary = new TransactionsSummary();

		try {
			LOGGER.fine("Listing all user transactions for userId: " + userId);
			String connectionEndpoint = httpMap.get(HttpMethods.GetTransactions).replace("{user.id}", userId);
			String requestUrl = webAddress + connectionEndpoint;

			// Wait for job completion
			JsonElement jsonResponse = waitForData(accessToken, requestUrl, 300);
			traversePage(jsonResponse, transactionSummary);

			while (hasNext(jsonResponse)) {
				requestUrl = getNextPageUrl(jsonResponse);
				jsonResponse = getTransactionsWaitForConsistentData(accessToken, requestUrl, 30);
				traversePage(jsonResponse, transactionSummary);
			}
		} catch (Exception e) {
			LOGGER.severe(e.toString());
			e.printStackTrace();
		}

		return transactionSummary;
	}

	private JsonElement waitForData(String accessToken, String requestUrl, int maxWaitTime)
			throws InterruptedException, UnirestException {
		int iterationWaitTime = 30;
		int repetitionsCnt = maxWaitTime / iterationWaitTime;
		LOGGER.fine("Waiting for data preparation: maxWaitTime=" + maxWaitTime + "s.");
		JsonElement jsonResponse = executeGetTransactions(accessToken, requestUrl);
		int count = getCount(jsonResponse);

		// Repeat HTTP request until data is ready or for max waiting time exceeded
		while (count == 0 && repetitionsCnt > 0) {
			Thread.sleep(iterationWaitTime * 1000);
			jsonResponse = executeGetTransactions(accessToken, requestUrl);
			count = getCount(jsonResponse);
			repetitionsCnt--;
		}
		return jsonResponse;
	}

	private JsonElement getTransactionsWaitForConsistentData(String accessToken, String requestUrl, int maxWaitTime)
			throws InterruptedException, UnirestException {
		JsonElement jsonResponse = null;
		int count;
		int iterationWaitTime = 5;
		int repetitionsCnt = maxWaitTime / iterationWaitTime;
		LOGGER.fine("Waiting for consistent data: maxWaitTime=" + maxWaitTime + "s.");
		jsonResponse = executeGetTransactions(accessToken, requestUrl);
		count = getCount(jsonResponse);

		while (count != 500 && count == getCount(jsonResponse) && repetitionsCnt > 0) {
			Thread.sleep(iterationWaitTime * 1000);
			jsonResponse = executeGetTransactions(accessToken, requestUrl);
			count = getCount(jsonResponse);
			repetitionsCnt--;
		}
		return jsonResponse;
	}

	private int getCount(JsonElement jsonResponse) {
		return jsonResponse.getAsJsonObject().get("count").getAsInt();
	}

	private JsonElement executeGetTransactions(String accessToken, String requestUrl)
			throws InterruptedException, UnirestException {
		HttpResponse<String> httpResponse;
		httpResponse = httpClient.httpGetUsersTransactions(accessToken, requestUrl);
		checkHttpResponseCode(httpResponse, 200, errMsgMap.get(HttpMethods.GetTransactions));
		return parser.parse(httpResponse.getBody());
	}

	private void traversePage(JsonElement jsonResponse, TransactionsSummary transactionsSummary) {
		JsonArray data = jsonResponse.getAsJsonObject().get("data").getAsJsonArray();
		LOGGER.fine("Traversing page: [" + data.size() + "] entries.");

		for (JsonElement jsonTransaction : data) {
			Transaction transaction = gson.fromJson(jsonTransaction, Transaction.class);
			transactionsSummary.processTransaction(transaction);
		}
	}

	private JsonArray getData(HttpResponse<String> httpResponse) {
		JsonObject jsonResponse = parser.parse(httpResponse.getBody()).getAsJsonObject();
		JsonArray data = jsonResponse.get("data").getAsJsonArray();
		return data;
	}

	private boolean hasNext(JsonElement jsonResponse) {
		JsonObject jsonObject = jsonResponse.getAsJsonObject();
		if (jsonObject.has("links")) {
			JsonObject linksObject = jsonObject.get("links").getAsJsonObject();
			if (linksObject.has("next")) {
				LOGGER.fine("Method hasNext = true");
				return true;
			}
		}
		LOGGER.fine("Method hasNext = false");
		return false;
	}

	private String getNextPageUrl(JsonElement jsonResponse) {
		String result = "";
		JsonObject jsonObject = jsonResponse.getAsJsonObject();

		if (jsonObject.has("links")) {
			JsonObject links = jsonObject.get("links").getAsJsonObject();
			if (links.has("next")) {
				result = links.get("next").getAsString();
			}
		}
		LOGGER.fine("Method getNextPageUrl ==> links.next=" + result);
		return result;
	}

	private void checkHttpResponseCode(HttpResponse<String> httpResponse, int expectedCode, String errorMsg)
			throws UnirestException {
		if (httpResponse.getStatus() != expectedCode) {
			throw new UnirestException(errorMsg);
		}
	}
}
