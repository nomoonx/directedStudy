package thesis_network_growth;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

public class OutputTrialResults {

	ArrayList<String> fileContents;
	
	public OutputTrialResults () {
		fileContents = new ArrayList<String>();
		
	} // end OutputTrialResults()
	
	public void appendFileContents (String line) {
		fileContents.add(line);
	} // end appendFileContents
	
	
	public void writeToFile (String filename) {
		
		FileWriter writer;
		try {
			writer = new FileWriter(filename);
			PrintWriter printer = new PrintWriter(writer);
			
			
			int i;
			for (i = 0; i < fileContents.size(); i++) {
				// Write each line to file.
				printer.println(fileContents.get(i));
				
			} // end for i (loop through fileContents array to write out to file)
			
			// Close file.
			printer.close();
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		// First print out a header with column descriptions. (?)
		
		

		
	} // end writeToFile()
	
	
} // end OutputTrialResults class
