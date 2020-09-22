package main.java.app.processTrans;

public class TransactionDetail {
	
	private String cardHash;
	private String initiateTimeStamp;
	private double amount;
	
	private boolean fraudulent; 
	
	public TransactionDetail(final String hash, final String time, final double amount) {
		this.cardHash = hash;
		this.initiateTimeStamp = time;
		this.amount = amount;
		this.fraudulent = false;
	}

	public String getCardHash() {
		return cardHash;
	}

	public void setCardHash(String cardHash) {
		this.cardHash = cardHash;
	}

	public String getInitiateTimeStamp() {
		return initiateTimeStamp;
	}

	public void setInitiateTimeStamp(String timeStamp) {
		this.initiateTimeStamp = timeStamp;
	}

	public double getAmount() {
		return amount;
	}

	public void setAmount(double amount) {
		this.amount = amount;
	}

	public boolean isFraudulent() {
		return fraudulent;
	}

	public void setFraudulent(boolean fraudulent) {
		this.fraudulent = fraudulent;
	}
	
	

}
