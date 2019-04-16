package io.basiq.http.json;

import com.google.gson.annotations.SerializedName;

@SuppressWarnings("unused")
public class Transaction {
	private String amount;
	private SubClass subClass;
	private String description;
	private String type;
	private String transactionDate;
	private String institution;
	private String balance;
	private String bankCategory;
	private String postDate;
	private String connection;
	private String id;
	@SerializedName("class")
	private String clazz;
	private String account;
	private String status;
	private String direction;

	private Links links;

	public Transaction(String amount, SubClass subClass, String description, String type, String transactionDate,
			String institution, String balance, String bankCategory, String postDate, String connection, String id,
			String clazz, String account, String status, String direction, Links links) {
		this.amount = amount;
		this.subClass = subClass;
		this.description = description;
		this.type = type;
		this.transactionDate = transactionDate;
		this.institution = institution;
		this.balance = balance;
		this.bankCategory = bankCategory;
		this.postDate = postDate;
		this.connection = connection;
		this.id = id;
		this.clazz = clazz;
		this.account = account;
		this.status = status;
		this.direction = direction;
		this.links = links;
	}

	public String getAmount() {
		return amount;
	}

	public void setAmount(String amount) {
		this.amount = amount;
	}

	public SubClass getSubClass() {
		return subClass;
	}

	public void setSubClass(SubClass subClass) {
		this.subClass = subClass;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getTransactionDate() {
		return transactionDate;
	}

	public void setTransactionDate(String transactionDate) {
		this.transactionDate = transactionDate;
	}

	public String getInstitution() {
		return institution;
	}

	public void setInstitution(String institution) {
		this.institution = institution;
	}

	public String getBalance() {
		return balance;
	}

	public void setBalance(String balance) {
		this.balance = balance;
	}

	public String getBankCategory() {
		return bankCategory;
	}

	public void setBankCategory(String bankCategory) {
		this.bankCategory = bankCategory;
	}

	public String getPostDate() {
		return postDate;
	}

	public void setPostDate(String postDate) {
		this.postDate = postDate;
	}

	public String getConnection() {
		return connection;
	}

	public void setConnection(String connection) {
		this.connection = connection;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getClazz() {
		return clazz;
	}

	public void setClazz(String clazz) {
		this.clazz = clazz;
	}

	public String getAccount() {
		return account;
	}

	public void setAccount(String account) {
		this.account = account;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getDirection() {
		return direction;
	}

	public void setDirection(String direction) {
		this.direction = direction;
	}

	public Links getLinks() {
		return links;
	}

	public void setLinks(Links links) {
		this.links = links;
	}

	@Override
	public String toString() {
		return "Transaction [amount=" + amount + ", subClass=" + subClass + ", description=" + description + ", type="
				+ type + ", transactionDate=" + transactionDate + ", institution=" + institution + ", balance="
				+ balance + ", bankCategory=" + bankCategory + ", postDate=" + postDate + ", connection=" + connection
				+ ", id=" + id + ", clazz=" + clazz + ", account=" + account + ", status=" + status + ", direction="
				+ direction + ", links=" + links + "]";
	}

}