

import java.nio.file.Path;
import java.nio.file.Paths;

import exceptions.InvalidArgumentException;
import main.ArgumentParser;
import main.MusicLibraryBuilder;
import main.SearchQuery;
import threadSafety.ThreadSafeMusicLibrary;

public class Driver {
	
	public static void main(String[] args) {
		

		try {
			ArgumentParser ap = new ArgumentParser(args);   	   // creating an instance of the ArgumentParser class
			Path inputPath = Paths.get(ap.getValue("-input"));	   // creating an inputPath variable to send to the MusicLib Builder
			Path outputPath = Paths.get(ap.getValue("-output"));   // creating an outputPath variable to send to Music Library class 
			String stringThreads = ap.getValue("-threads");
			
			MusicLibraryBuilder mlb = new MusicLibraryBuilder(inputPath, stringThreads);   // creating an instance of the MusicLibraryBuilder class and sending the inputPath
			ThreadSafeMusicLibrary ml = mlb.getMusicLibrary();							// creating an instance of the MusicLibrary class and calling my getMusicLibrary method in order to get the Library already created
			mlb.traverseParser();														// calling the traverseParser method in order to begin parsing the inputPath
			
			if (ap.numFlags() == 6) {
				Path searchInputPath = Paths.get(ap.getValue("-searchInput"));
				String searchOutputPath = ap.getValue("-searchOutput");
				SearchQuery sq = new SearchQuery(searchInputPath, searchOutputPath, ml);
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
