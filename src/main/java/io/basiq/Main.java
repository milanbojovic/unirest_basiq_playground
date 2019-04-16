package io.basiq;

import java.text.SimpleDateFormat;
import java.time.temporal.ChronoUnit;
import java.util.Calendar;
import java.util.Date;
import java.util.logging.Logger;

import io.basiq.http.json.CreateConnection;
import io.basiq.http.json.CreateUser;

public class Main {
	private static final Logger LOGGER = Logger.getLogger(Main.class.getName());

	public static String DATE_FORMAT = "dd.MM.yyyy_HH:mm:ss";

	public static void main(String[] args) {
		String apiKey = args[0];
		Date startTime = Calendar.getInstance().getTime();
		LOGGER.info("Program execution started: [" + new SimpleDateFormat(DATE_FORMAT).format(startTime) + "]");

		BasiqClient client = new BasiqClient("https://au-api.basiq.io", apiKey);

		String accessToken = client.getAccessToken();
		String userId = client.createUser(accessToken, new CreateUser("milanbojovic@mailinator.com", "+381601111111"));

		CreateConnection connectionRequest = new CreateConnection("Wentworth-Smith", "whislter", "AU00000");
		String connectionId = client.createConnection(accessToken, connectionRequest, userId);

		TransactionsSummary transactionSummary = client.getUsersTransactions(accessToken, userId);

		LOGGER.info("Acquired access_token: " + accessToken);
		LOGGER.info("Created new user: " + userId);
		LOGGER.info("Created new user bank connection: " + connectionId);
		LOGGER.info("Printing categories: ");
		LOGGER.info(transactionSummary.printTotals());

		Date endTime = Calendar.getInstance().getTime();
		LOGGER.info("Program execution finished: [" + new SimpleDateFormat(DATE_FORMAT).format(endTime) + "]");
		long programDuration = ChronoUnit.MILLIS.between(startTime.toInstant(), endTime.toInstant());
		LOGGER.info("Program execution time: " + programDuration / 1000 + " sec.");
	}
}
