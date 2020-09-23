

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import junit.framework.TestCase;
import app.processTrans.ProcessTransRequest;

public class ProcessTransRequestTest extends TestCase {
	private ProcessTransRequest ptr;
	
	public void setUp() throws Exception {	
		super.setUp();
		ptr = new ProcessTransRequest("150.00", "transactions.csv");
	}
	
	public void testGetPriceThreshold() throws Exception {
		
		// given 
		double givenAmt = 150.00;
		ptr.setPriceThreshold(givenAmt);
		
		// when 
		ptr.getPriceThreshold();
		
		// then 
		assertEquals(givenAmt, ptr.getPriceThreshold());
		
	}
	
	public void testConvertPriceArgValidArg() throws Exception {
		
		// given
		String givenAmt = "150.00";
		
		// when
		boolean actual = ptr.convertPriceArg(givenAmt);
		
		// then 
		assertEquals(true, actual);
		
	}
	
	public void testConvertPriceArgInValidArg() throws Exception {
		
		// given
		String givenAmt = "150.00g";
		
		// when
		try {
			ptr.convertPriceArg(givenAmt);
		}
		
		// then 
		catch (NumberFormatException e) {
			
		}
		
	}
	
	public void testConvertPriceArgNullArg() throws Exception {
		
		// given
		String givenAmt = null;
		
		// when
		try {
			ptr.convertPriceArg(givenAmt);
		}
		
		// then 
		catch (NullPointerException ne) {
			
		}
		
	}
	
	public void testCsvFileValidationValidFileName() throws Exception {
		
		// given 
		String fileName = "abc.csv";
		
		// when
		boolean actual = ptr.csvFileValidation(fileName);
		
		// then 
		assertEquals(true, actual);
		
	}
	
	public void testCsvFileValidationInvalidFileName() throws Exception {
		
		// given 
		String fileName = "abc.pdf";
		
		// when
		boolean actual = ptr.csvFileValidation(fileName);
		
		// then 
		assertEquals(false, actual);
		
	}
	
	public void testCsvFileValidationNullFileName() throws Exception {
		
		// given 
		String fileName = null;
		
		// when
		boolean actual = ptr.csvFileValidation(fileName);
		
		// then 
		assertEquals(false, actual);
		
	}
	
	public void testAddHoursToDateValid() throws Exception {
		
		// given
		String initialTime = "2020-09-18T14:15:15";
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'hh:mm:ss");
		Date iniTime = sdf.parse(initialTime);
	
		int hoursToAdd = 1;
		
		// when
		Date actual = ptr.addHoursToDate(iniTime, hoursToAdd);
		
		 
		long diffInMillies = Math.abs(actual.getTime() - iniTime.getTime());
	    long diff = TimeUnit.HOURS.convert(diffInMillies, TimeUnit.MILLISECONDS);
	    
	    // then
	    assertEquals(hoursToAdd, diff);
		
	}
	
	public void testIsWithinTimeWindowValidBorderCase() throws Exception {
		
		// given
		String initialTime = "2020-09-18T14:15:15";
		String givenTime = "2020-09-19T14:15:14";
		
		// when 
		boolean actual = ptr.isWithinTimeWindow(initialTime, givenTime);
		
		// then
		assertEquals(true, actual);
		
	}
	
	public void testIsWithinTimeWindowInvalidBorderCase() throws Exception {
		
		// given
		String initialTime = "2020-09-18T14:15:15";
		String givenTime = "2020-09-19T14:15:15";
		
		// when 
		boolean actual = ptr.isWithinTimeWindow(initialTime, givenTime);
		
		// then
		assertEquals(false, actual);
		
	}
	
	
}
