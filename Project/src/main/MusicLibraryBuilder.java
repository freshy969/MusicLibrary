package main;
import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import threadSafety.JsonParsing;
import threadSafety.ThreadSafeMusicLibrary;
import threadSafety.WorkQueue;

public class MusicLibraryBuilder {

	
	/*
	 * class that traverses the file system (given input path) and 
	 * builds the library (data structure initialization)
	 */
	
	private Path path;
	private ThreadSafeMusicLibrary lib;
	private WorkQueue workQueue;
	private int threadCount = 0;
	
	/*
	 * Constructor is initializing a path as well as a new MusicLibrary, an instance of the MusicLibrary class
	 */
	
	
	public MusicLibraryBuilder(Path path, String stringThreads) {
		
		this.path = path;
		this.lib = new ThreadSafeMusicLibrary();
		
		try {
			this.threadCount = Integer.parseInt(stringThreads);
		}
		catch (NumberFormatException nfe){
			threadCount = 10;
		}
		
		if (threadCount < 1 || threadCount > 1000) {
			threadCount = 10;
		}
		workQueue = new WorkQueue(threadCount);
		
		
	}
	
	public MusicLibraryBuilder(Path path) {
		
		this.path = path;
		this.lib = new ThreadSafeMusicLibrary(); 
		this.threadCount = 10;
		
		workQueue = new WorkQueue(threadCount);
		
	}
	
	/*
	 * Method OverLoading for traverseParser. The first one is just a helper method so that I don't have to pass a
	 * path variable as a parameter when I call it in my driver. The second traverseParser method takes in a path
	 * and recursively traverses the path if it a directory until we reach a valid json file. If it a valid json or 
	 * JSON file I path the file to my jsonGetter method to extract my JSONObject. This is also where I call shutdown
	 * and await termination.
	 */
	
	public void traverseParser() {
		
		traverseParser(path);
		
		workQueue.shutdown();
		
		workQueue.awaitTermination();
	
		
	}
	
	public void traverseParser(Path path) {
		
		if(Files.isDirectory(path)) {
			try (DirectoryStream<Path> dir = Files.newDirectoryStream(path)) {
				
				for (Path entry : dir) {
					traverseParser(entry);
				}
				 
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		else if (path.toString().toLowerCase().endsWith(".json")) {
			
			workQueue.execute(new JsonParsing(path, getMusicLibrary()));
		}
		
	}

	
	/*
	 * I created a getMusicLibrary method so that I can get the MusicLibrary instance I created here in my Driver class
	 */
	
	public ThreadSafeMusicLibrary getMusicLibrary() {
		return lib;
	}
}
