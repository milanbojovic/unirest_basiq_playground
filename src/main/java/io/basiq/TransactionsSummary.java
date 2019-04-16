package io.basiq;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.basiq.http.json.SubClass;
import io.basiq.http.json.Transaction;

public class TransactionsSummary {
	List<Transaction> usersTransactions = new ArrayList<Transaction>();
	Map<String, Double> categoryTotalsMap = new HashMap<>();

	public List<Transaction> getUsersTransactions() {
		return usersTransactions;
	}

	public void setUsersTransactions(List<Transaction> usersTransactions) {
		this.usersTransactions = usersTransactions;
	}

	public Map<String, Double> getCategoryTotalsMap() {
		return categoryTotalsMap;
	}

	public void setCategoryTotalsMap(Map<String, Double> categoryTotalsMap) {
		this.categoryTotalsMap = categoryTotalsMap;
	}

	public void addTransaction(Transaction trans) {
		usersTransactions.add(trans);
		calculateTotals(trans);
	}

	private void calculateTotals(Transaction transaction) {
		// If any value null skip entry
		if (transaction.getSubClass() == null || transaction.getSubClass().getCode() == null) {
			return;
		}

		SubClass subClass = transaction.getSubClass();
		String code = subClass.getCode();

		// Add category if doesn't exist
		if (!categoryTotalsMap.containsKey(code)) {
			categoryTotalsMap.put(code, Double.valueOf(0));
		}
		// recalculate total category sum
		Double totalAmount = categoryTotalsMap.get(code);
		totalAmount = totalAmount + Double.parseDouble(transaction.getAmount());
		categoryTotalsMap.put(code, totalAmount);
	}

	public String printTotals() {
		String lineSeparator = System.getProperty("line.separator");
		StringBuilder sb = new StringBuilder();
		for (Map.Entry<String, Double> entry : categoryTotalsMap.entrySet()) {
			sb.append(entry.getKey() + "=" + entry.getValue());
			sb.append(lineSeparator);
		}
		return sb.toString();
	}
}
