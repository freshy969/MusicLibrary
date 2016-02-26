import java.util.ArrayList;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

public class Song {
	
	/*
	 * Class that stores data from a single song, very similar to Song.java in last homework
	 * 
	 * 
	 */

	private String artist;
	private String trackId;
	private String title;
	private ArrayList<String> similars;
	private ArrayList<String> tags;
	
	
	public Song(String artist, String trackId, String title, ArrayList<String> similars, ArrayList<String> tags) {
		
		this.artist = artist;
		this.trackId = trackId;
		this.title = title;
		this.similars = similars;
		this.tags = tags;
		
		
	}
	
	/**
	 * Constructor that takes as input a JSONObject as illustrated in the example above and
	 * constructs a Song object by extract the relevant data.
	 * @param object
	 * 
	 * Get similars by extracting the JSONArray and iterating through it. At each index, which is going to be its 
	 * own array, I store that as a temporary JSONArray. Using the temporary JSONArray, i extract the first index of 
	 * the array and save that as a string called temp. Then add each temp to the similars ArrayLists I initialized
	 * in the constructor
	 */
	
	public Song(JSONObject object) {
		
		this.artist = (String)object.get("artist");
		this.trackId = (String)object.get("track_id");
		this.title = (String)object.get("title");
		JSONArray simple = (JSONArray)object.get("similars");
		ArrayList<String> similars = new ArrayList<String>();
		for (int i = 0; i < simple.size(); i++) {
			JSONArray temporary = (JSONArray) simple.get(i);
			String temp = (String) temporary.get(0);
			similars.add(temp);
		}
		this.tags = (JSONArray)object.get("tags");
		
	}
	
	// constructor overloading 
	
	
	/**
	 * Return artist.
	 * @return
	 */
	public String getArtist() {
		//TODO: complete method
		return artist;
	}

	/**
	 * Return track ID.
	 * @return
	 */
	public String getTrackId() {
		//TODO: complete method
		return trackId;
	}

	/**
	 * Return title.
	 * @return
	 */
	public String getTitle() {
		//TODO: complete method
		return title;
	}

	/**
	 * Return a list of the track IDs of all similar tracks.
	 * @return
	 */
	public ArrayList<String> getSimilars() {
		//TODO: complete method
		return similars;
	}

	/**
	 * Return a list of all tags for this track.
	 * @return
	 */
	public ArrayList<String> getTags() {
		//TODO: complete method
		return tags;
	}	
}