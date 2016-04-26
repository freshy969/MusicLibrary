
import java.io.BufferedReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class Driver {
	
	public static void main(String[] args) {
		
		int threadCount = 0;
		
		
		try {
			ArgumentParser ap = new ArgumentParser(args);   	   // creating an instance of the ArgumentParser class
			Path inputPath = Paths.get(ap.getValue("-input"));	   // creating an inputPath variable to send to the MusicLib Builder
			Path outputPath = Paths.get(ap.getValue("-output"));   // creating an outputPath variable to send to Music Library class 
			String stringThreads = ap.getValue("-threads");
			try {
				threadCount = Integer.parseInt(stringThreads);
			}
			catch (NumberFormatException nfe){
				threadCount = 10;
			}
			
			if (threadCount < 1 || threadCount > 1000) {
				threadCount = 10;
			}
			
			MusicLibraryBuilder mlb = new MusicLibraryBuilder(inputPath, threadCount);   // creating an instance of the MusicLibraryBuilder class and sending the inputPath
			ThreadSafeMusicLibrary ml = mlb.getMusicLibrary();							// creating an instance of the MusicLibrary class and calling my getMusicLibrary method in order to get the Library already created
			
			mlb.traverseParser();														// calling the traverseParser method in order to begin parsing the inputPath
			JSONObject obj = new JSONObject();
			
			if (ap.numFlags() == 6) {
				Path searchInputPath = Paths.get(ap.getValue("-searchInput"));
				String searchOutputPath = ap.getValue("-searchOutput");
			
				if (searchInputPath.toString().toLowerCase().endsWith(".json")) {
					JSONParser parser = new JSONParser();
					try (BufferedReader reader = Files.newBufferedReader(searchInputPath, Charset.forName("UTF-8"))) {
						
						Object wholeFile = parser.parse(reader);
						JSONObject contents = (JSONObject) wholeFile;
						
						obj = ml.jSonOutput(contents);
						
					} catch (IOException e) {
						e.printStackTrace();
					} catch (ParseException e) {
						e.printStackTrace();
					}
				}
				
				if (searchOutputPath.toString().toLowerCase().endsWith(".json")) {
					
					
					try {
						FileWriter file = new FileWriter(searchOutputPath);
						file.write(obj.toJSONString());
						file.flush();
						file.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}	
				
			if (ap.getValue("-order").equals("artist")) {				// if the order flag is artist,
				ml.outputByArtist(outputPath);							// call my outPutbyArtist method and sending the outPutPath variable 
					
			}
			else if (ap.getValue("-order").equals("title")) {			// if the order flag is title,
				ml.outputByTitle(outputPath);							// call my outPutbyArtist method and sending the outPutPath variable
					
			}
			else if (ap.getValue("-order").equals("tag")) {				// if the order flag is tag,
				ml.outputByTag(outputPath);								// call my outPutbyArtist method and sending the outPutPath variable
					
			}
				
				
		} catch (InvalidArgumentException e) {
			
			System.out.println("Incorrect Arguments");
		}
		
	}

}
