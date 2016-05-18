package database;

import java.text.ParseException;
import java.util.TreeMap;
import java.util.TreeSet;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import main.Song;

public class LastFmClient {
	
	public static final String apiKey = "c8dd34d569c783b66b38083a1c078596";
	
	public static void fetchAndStoreArtists(TreeMap<String, TreeSet<Song>> byArtist, DBConfig dbconfig) {
		
		JSONParser parser = new JSONParser();
		int counter = 0;
		
		for (String artist : byArtist.navigableKeySet()) {
			
			if (counter > 0 && counter % 5 == 0) {
				try {
					Thread.currentThread().sleep(1000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			
			String request = Fetcher.download("ws.audioscrobbler.com", "/2.0?artist=" + artist + "&api_key=" + apiKey + "&method=artist.getInfo&format=json");
			
			JSONObject obj = null;
			try {
				obj = (JSONObject) parser.parse(request);
			} catch (org.json.simple.parser.ParseException e) {
				e.printStackTrace();
			}
			DBHelper.addToLastFmTable(obj, dbconfig, artist);
			
			counter++;
			
		}
	
	}

}
