package io.basiq;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import io.basiq.http.json.SubClass;
import io.basiq.http.json.Transaction;

public class TransactionsSummary {
	Map<String, CategoryTotal> categoryTotalMap = new HashMap<String, CategoryTotal>();

	public Map<String, CategoryTotal> getCategoryTotalsMap() {
		return categoryTotalMap;
	}

	public void setCategoryTotalsMap(Map<String, CategoryTotal> categoryTotalsMap) {
		this.categoryTotalMap = categoryTotalsMap;
	}

	public void processTransaction(Transaction transaction) {
		if ("credit".equals(transaction.getDirection()))
			return;

		if (transaction.getSubClass() == null || transaction.getSubClass().equals(""))
			return;
		SubClass subClass = transaction.getSubClass();

		double dAmount = 0;
		if (transaction.getAmount() == null || transaction.getAmount().equals("")) {
			return;
		} else {
			try {
				dAmount = Double.parseDouble(transaction.getAmount());
			} catch (NumberFormatException e) {
				return;
			}
		}

		if (subClass.getCode() == null || subClass.getCode().equals(""))
			return;
		String code = subClass.getCode();

		if (subClass.getTitle() == null || subClass.getTitle().equals(""))
			return;
		String title = subClass.getTitle();

		if (!categoryTotalMap.containsKey(code)) {
			CategoryTotal ct = new CategoryTotal(title, Double.valueOf(0));
			categoryTotalMap.put(code, ct);
		} else {
			// recalculate total category sum
			Double totalSum = categoryTotalMap.get(code).getTotalSum();
			totalSum = totalSum + dAmount;
			categoryTotalMap.get(code).increaseTotalSum(totalSum);
		}
	}

	public String printTotals() {
		String lineSeparator = System.getProperty("line.separator");
		StringBuilder sb = new StringBuilder();
		sb.append(lineSeparator);
		for (Map.Entry<String, CategoryTotal> entry : categoryTotalMap.entrySet()) {
			sb.append("Code: [" + StringUtils.leftPad(entry.getKey(), 3, "0") + "] " + entry.getValue());
			sb.append(lineSeparator);
		}
		return sb.toString();
	}
}
