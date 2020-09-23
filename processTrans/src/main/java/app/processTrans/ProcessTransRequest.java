package main.java.app.processTrans;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

import main.java.app.processTrans.exception.ProcessTransactionInvalidException;

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
		generateFraudulentCardHash();
		
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
					checkForFraud(lineContent[0]);
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
	
	private void generateFraudulentCardHash() {
		System.out.println("Fraudulent Card Hash");
		System.out.println("---------------------");
		int i = 1;
		for(String cardHash : transactions.keySet()) {
			TransactionDetail record = transactions.get(cardHash);
			if (record.isFraudulent()) {
				System.out.println(i+". "+record.getCardHash());
				i++;
			}
		}
	}
	
	/**
	 * parses each line in csv file and adds valid records to transactions map.
	 * @param hash cardnumber hash 
	 * @param timeStamp timestamp in csv file
	 * @param amnt amount in the csv file
	 */
	private void parseLine(String hash, String timeStamp, String amnt) {
		double amount = Double.parseDouble(amnt);
		TransactionDetail td ;
		
		if(transactions.containsKey(hash)) {
			td = transactions.get(hash);
			
			if(!td.isFraudulent()) {
			
				boolean within24Window = isWithinTimeWindow(td.getInitiateTimeStamp(), timeStamp);
				double updatedAmount;
				if (within24Window) {
					updatedAmount = td.getAmount() + amount;
				} else {
					updatedAmount = amount;
					td.setInitiateTimeStamp(timeStamp);
				}
				td.setAmount(updatedAmount);
			}
			
		} else {
			td = new TransactionDetail(hash, timeStamp, amount);
			transactions.put(hash, td);
		}
		
	}
	
	/**
	 * checks if the record in transactions map is fraudulent.
	 * 
	 * @param hash card hash 
	 */
	public void checkForFraud(String hash) {
		TransactionDetail td = transactions.get(hash);
		
		if(td.getAmount() > getPriceThreshold() ) {
			td.setFraudulent(true);
		} else {
			td.setFraudulent(false);
		}
	}
	
	/**
	 * checks if given time falls within 24-four window starting from initialTime.
	 * 
	 * @param initialTime
	 * @param givenTime
	 * @return True if givenTime is falls within 24 hour window
	 */
	public boolean isWithinTimeWindow(String initialTime, String givenTime) {
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
	
	/**
	 * Adds given number of hours to the passed in time.
	 * 
	 * @param iniTime time to add hours to
	 * @param hoursToAdd amount of time to be added 
	 * @return returns final time after addition
	 */
	public Date addHoursToDate(Date iniTime, int hoursToAdd) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(iniTime);
		calendar.add(Calendar.HOUR_OF_DAY, hoursToAdd);
		return calendar.getTime();
	}
	
	/**
	 * checks for file type ".csv"
	 * @param fileName file to check 
	 * @return returns True if given file has extension .csv
	 */
	public boolean csvFileValidation(String fileName) {
		try {
			return fileName.matches(".+(\\.csv)$");
		}catch (NullPointerException ne) {
			System.out.println(ne);
		}
		return false;
	}
	
	/**
	 * converts and sets the price threshold for the transactions in csv file
	 * @param price
	 * @return
	 */
	public boolean convertPriceArg(String price){
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
