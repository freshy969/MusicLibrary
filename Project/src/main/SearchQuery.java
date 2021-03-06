package main;

import java.io.BufferedReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import threadSafety.ThreadSafeMusicLibrary;

public class SearchQuery {
	
	private Path searchInputPath;
	private String searchOutputPath;
	private JSONObject obj;
	private ThreadSafeMusicLibrary ml;
	
	public SearchQuery(Path searchInputPath, String searchOutputPath, ThreadSafeMusicLibrary ml)  {
		
		this.searchInputPath = searchInputPath;
		this.searchOutputPath = searchOutputPath;
		this.ml = ml;
		this.obj = new JSONObject();
		inputQuery(searchInputPath);
		outputQuery(searchOutputPath);
		
	}
	
	/*
	 * inputQuery method deals with searchInput command line argument. If the 
	 * input file is a json file, then it parses the json file into a JSONObject
	 * and calls the JSONOutput method.
	 */
	
	public void inputQuery(Path inputPath){
		
		if (inputPath.toString().toLowerCase().endsWith(".json")) {
			JSONParser parser = new JSONParser();
			try (BufferedReader reader = Files.newBufferedReader(inputPath, Charset.forName("UTF-8"))) {
				
				Object wholeFile = parser.parse(reader);
				JSONObject contents = (JSONObject) wholeFile;
				
				obj = jSonOutput(contents);
				
			}  catch (NoSuchFileException nsfe) {
				//nsfe.printStackTrace();
			}  catch (IOException e) {
				e.printStackTrace();
			} catch (ParseException e) {
				e.printStackTrace();
			}
		}
		
	}
	
	/*
	 * outputQuery method deals with the searchOutput command line argument. 
	 * If the output file is a json file, then we write to that file using
	 * File writer.
	 */
	
	public void outputQuery(String outputPath) {
		
		if (outputPath.toString().toLowerCase().endsWith(".json")) {
			
			try {
				FileWriter file = new FileWriter(outputPath);
				file.write(obj.toJSONString());
				file.flush();
				file.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
	}
	
	/*
	 * jSonOutput method takes in JSONObject that was extracted from the searchInput. 
	 * It return the JSONObject with the content and in the format you want for your output file.
	 */
	
	public JSONObject jSonOutput(JSONObject input) {
		
		JSONObject obj = new JSONObject();
		
		if (input.containsKey("searchByArtist")) {
			JSONArray valueAllArtists = new JSONArray();
			JSONArray allArtist = (JSONArray) input.get("searchByArtist");
			for (int i = 0; i < allArtist.size(); i++) {
				if (ml.byArtist.containsKey(allArtist.get(i))) {
					JSONObject eachArtist = new JSONObject();
					eachArtist.put("artist", allArtist.get(i));
					eachArtist.put("similars", ml.searchByArtist((String) allArtist.get(i)));
					valueAllArtists.add(eachArtist);
				}
				else {
					JSONObject eachArtist = new JSONObject();
					eachArtist.put("artist", allArtist.get(i));
					eachArtist.put("similars", new JSONArray());
					valueAllArtists.add(eachArtist);
				}
			}
			
			obj.put("searchByArtist", valueAllArtists);
		}
		
		if (input.containsKey("searchByTitle")) {
			JSONArray valueAllTitle = new JSONArray();
			JSONArray allTitle = (JSONArray) input.get("searchByTitle");
			for (int i = 0; i < allTitle.size(); i++) {
				if (ml.byTitle.containsKey(allTitle.get(i))) {
					JSONObject eachTitle = new JSONObject();
					eachTitle.put("similars", ml.searchByTitle((String) allTitle.get(i)));
					eachTitle.put("title", allTitle.get(i));
					valueAllTitle.add(eachTitle);
				}
				else {
					JSONObject eachTitle = new JSONObject();
					eachTitle.put("similars", new JSONArray());
					eachTitle.put("title", allTitle.get(i));
					valueAllTitle.add(eachTitle);
				}
			}
			
			obj.put("searchByTitle", valueAllTitle);
		}
		
		if (input.containsKey("searchByTag")) {
			JSONArray valueAllTag = new JSONArray();
			JSONArray allTag = (JSONArray) input.get("searchByTag");
			for (int i = 0; i < allTag.size(); i++) {
				if (ml.byTag.containsKey(allTag.get(i))) {
					JSONObject eachTag = new JSONObject();
					eachTag.put("similars", ml.searchByTag((String) allTag.get(i)));
					eachTag.put("tag", allTag.get(i));
					valueAllTag.add(eachTag);
				}
				else {
					JSONObject eachTag = new JSONObject();
					eachTag.put("similars", new JSONArray());
					eachTag.put("tag", allTag.get(i));
					valueAllTag.add(eachTag);
				}
			}
			
			obj.put("searchByTag", valueAllTag);
		}
		return obj;
	}

}
