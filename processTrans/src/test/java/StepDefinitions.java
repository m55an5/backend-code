import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import app.processTrans.ProcessTransRequest;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When; 




public class StepDefinitions {
	
	ProcessTransRequest ptr;
	List<String[]> csvRecords = new ArrayList<>();
	String csvFileName;
	String bashCmdOutput = "";
	
	File file;
	
	@Given("I create a csv file with name {string}")
	public void i_create_a_csv_file_with_name(String string) {
	    this.csvFileName = string;
	}
	

	@Given("I add hash {string} with time {string} and amount {string}")
	public void i_add_hash_with_time_and_amount(String string, String string2, String string3) {
	    csvRecords.add(new String[] {string, string2, string3});
	}


	@Given("I create the csv file")
	public void i_create_the_csv_file() throws FileNotFoundException {
		File csvOutputFile = new File(this.csvFileName);
	    try (PrintWriter pw = new PrintWriter(csvOutputFile)) {
	    	csvRecords.stream()
	          .map(this::convertToCSV)
	          .forEach(pw::println);
	    }
	    assertEquals(true, csvOutputFile.exists());
	}
	
	
	@When("I run processTrans request with csv file and threshold {string}")
	public void i_run_process_trans_request(String string) throws IOException {
		String [] command = {"bash", "-c", "java -jar ./build/libs/processTrans-1.0.0.jar" +
				" " + string + " " + this.csvFileName };
		
		bashCmdOutput = runBashCmd(command);
		assertNotEquals("", bashCmdOutput);

	}
	
	@Then("I {string} expect {string} in faudulent output")
	public void i_expect_in_faudulent_output(String string, String string2) {
		if(string.equals("do")) {
	    	assertEquals(true, bashCmdOutput.contains(string2));
	    }else {
	    	assertEquals(false, bashCmdOutput.contains(string2));
	    }
	}
	
	public String runBashCmd(String[] command) throws IOException {
		Runtime rt = Runtime.getRuntime();
		Process proc = rt.exec(command);
		
		BufferedReader stdInput = new BufferedReader(new 
			     InputStreamReader(proc.getInputStream()));
		
		String line = null;
		String out = "";
		while ((line = stdInput.readLine()) != null) {
			out += line;
		}
		return out;
	}

	public String convertToCSV(String[] data) {
	    return Stream.of(data)
	      .map(this::escapeSpecialCharacters)
	      .collect(Collectors.joining(","));
	}
	
	public String escapeSpecialCharacters(String data) {
	    String escapedData = data.replaceAll("\\R", " ");
	    if (data.contains(",") || data.contains("\"") || data.contains("'")) {
	        data = data.replace("\"", "\"\"");
	        escapedData = "\"" + data + "\"";
	    }
	    return escapedData;
	}

	
}
