package io.basiq;

import org.apache.commons.lang3.StringUtils;

public class CategoryTotal {
	String categoryTitle;
	double totalSum;
	int cnt;

	public CategoryTotal(String categoryTitle, double sum) {
		super();
		this.categoryTitle = categoryTitle;
		this.totalSum = sum;
		cnt = 0;
	}

	public String getCategoryTitle() {
		return categoryTitle;
	}

	public void setCategoryTitle(String categoryTitle) {
		this.categoryTitle = categoryTitle;
	}

	public double getTotalSum() {
		return totalSum;
	}

	public void setTotalSum(double totalSum) {
		this.totalSum = totalSum;
	}

	public void increaseTotalSum(double totalSum) {
		this.totalSum += totalSum;
		cnt++;
	}

	private double roundDouble(Double value) {
		return (double) Math.round(value * 100d) / 100d;
	}

	private String rightPadding(String line, int padding, String paddingChr) {
		return StringUtils.rightPad(line, padding, paddingChr);
	}

	private String leftPadding(String line, int padding, String paddingChr) {
		return StringUtils.leftPad(line, padding, paddingChr);
	}

	@Override
	public String toString() {
		String paddedTitle = rightPadding("Title: [" + categoryTitle + "]", 75, " ");
		String paddedSum = rightPadding(Double.toString(roundDouble(Math.abs(totalSum))), 20, " ");
		String paddedCnt = leftPadding(Integer.toString(cnt), 3, "0");
		String avgConsumption = Double.toString(roundDouble(Math.abs(totalSum / cnt)));

		return paddedTitle + ", totalAmount=" + paddedSum + ", transactionCount=" + paddedCnt + " avgConsumption="
				+ avgConsumption;
	}
}