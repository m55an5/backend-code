package main;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

import exception.ProcessTransactionInvalidException;

public class ProcessTransRequest {
	
	private final String priceArg;
	private final String csvTransFile; 
	private double priceThreshold;
	
	private static final int TRANSACTION_TIME_WINDOW = 24;
	private static final int MAX_COLUMNS_IN_FILE = 3;

	private HashMap<String, TransactionDetail> transactions = new HashMap<String, TransactionDetail>();

	public ProcessTransRequest(final String price, final String csvfile) {
		this.priceArg = price;
		this.csvTransFile = csvfile;
		
	}
	
	
	private void processFile() throws ProcessTransactionInvalidException {

		if (!(convertPriceArg(this.priceArg))) {
			throw new ProcessTransactionInvalidException("Error: Invalid Price Threshold");
		}
		
		if (!(csvFileValidation(this.csvTransFile))) {
			throw new ProcessTransactionInvalidException("Error: Invalid file");
		}

		readCSVFile();
		//validateTransactions();
		for(String i : transactions.keySet()) {
			System.out.println(i);
			System.out.println(transactions.get(i).getAmount() + " | " + transactions.get(i).isFraudulent()
					+ " | " + transactions.get(i).getInitiateTimeStamp());
		}
		
		System.out.println("Success");
	}
	
	private void readCSVFile() {
		
		BufferedReader br = null;
		String line = "";
		String splitOn = ",";
		
		try {
			br = new BufferedReader(new FileReader(this.csvTransFile));
			while ( (line = br.readLine()) != null) {
				String[] lineContent = line.split(splitOn);
				if ( lineContent.length == MAX_COLUMNS_IN_FILE) {
					parseLine(lineContent[0],lineContent[1],lineContent[2]);	
				}
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (br != null) {
				try {
					br.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		
	}
	
	private void parseLine(String hash, String timeStamp, String amnt) {
		double amount = Double.parseDouble(amnt);
		TransactionDetail td ;
		
		if(transactions.containsKey(hash)) {
			td = transactions.get(hash);
			boolean valid = isWithinTimeWindow(td.getInitiateTimeStamp(), timeStamp);
			double updatedAmount;
			if (valid) {
				updatedAmount = td.getAmount() + amount;
			} else {
				updatedAmount = amount;
				td.setInitiateTimeStamp(timeStamp);
			}
			td.setAmount(updatedAmount);
			if(td.getAmount() > 150 ) {
				td.setFraudulent(true);
			}
		} else {
			td = new TransactionDetail(hash, timeStamp, amount);
			transactions.put(hash, td);
		}
	}
	
	
	private boolean isWithinTimeWindow(String initialTime, String givenTime) {
		try {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'hh:mm:ss");
			Date iniTime = sdf.parse(initialTime);
			Date timePeriodEnd = addHoursToDate(iniTime, TRANSACTION_TIME_WINDOW);
			Date givenTransTime = sdf.parse(givenTime);
			return timePeriodEnd.after(givenTransTime);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}
	
	private Date addHoursToDate(Date iniTime, int hoursToAdd) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(iniTime);
		calendar.add(Calendar.HOUR_OF_DAY, 12);
		return calendar.getTime();
	}
	
	private boolean csvFileValidation(String fileName) {
		return fileName.matches(".+(\\.csv)$");
	}
	
	private boolean convertPriceArg(String price){
		try {
			setPriceThreshold(Double.parseDouble(price));
			return true;
		} catch (Exception e) {
			System.out.println(e);
		}
		return false;
	}
	
	
	/**
	 * cli usage for the app.
	 */
	private static void usage() {
		System.out.println("Usage: " + ProcessTransRequest.class.getName() + " "
				+ "price_threshold" + " " + "csv_file_name");
		System.out.println("\tExample: " + ProcessTransRequest.class.getName() +
				" " + "150.00" + " " + "monday.csv" + "\n");
		
	}
	
	
	/********** GETTERS AND SETTERS ***********/
	
	public double getPriceThreshold() {
		return priceThreshold;
	}


	public void setPriceThreshold(double priceThreshold) {
		this.priceThreshold = priceThreshold;
	}
	
	
	
	
	
	
	/**
	 * cli entry point.
	 * 
	 * @param args
	 * 				command line arguments
	 */
	public static void main(final String[] args) {
		
		if (args.length != 2) {
			usage();
			return;
		} else {

				ProcessTransRequest ptr = new ProcessTransRequest(args[0], args[1]);
				try {
					ptr.processFile();
				} catch (Exception e) {
					System.out.println(e);
				}
				
		}
		
		
	}

}
