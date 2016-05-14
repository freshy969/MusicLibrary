package server;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import database.DBHelper;
import threadSafety.ThreadSafeMusicLibrary;

public class SongInfoServlet extends BaseServlet {
	
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		HttpSession session = request.getSession();
		String username = (String) session.getAttribute(USERNAME);
		HashMap <String, String> favList = DBHelper.getFavList(username);
		ThreadSafeMusicLibrary tsml = (ThreadSafeMusicLibrary) request.getServletContext().getAttribute("musicLibrary");
		JSONArray result = new JSONArray();
		
		String artist = request.getParameter("artist");
		String title = request.getParameter("title");
		System.out.println(artist);
		System.out.println(title);
		
		
		String header = header();
		String songInfo = "<html>" +
						"<h2><center>" + artist + "</center></h2>" +
						"<h2><center>" + title + "</center></h2>" +
						"</html>";
		String responseTable = "<center>Here are some songs that are similar to" + artist + "!</center>" +
				"<br>" +
				"<table border=\"2px\" width=\"100%\">" +				
				"<tr><td><strong>Artist</strong></td><td><strong>Song Title</strong></td><td><strong>Favorites</strong></td></tr>";
		result = tsml.searchByTitle(title);
		for (int i = 0; i<result.size(); i++) {
			JSONObject simSongs = (JSONObject) result.get(i);
			String trackId = (String) simSongs.get("trackId");
			if (favList.containsKey(trackId)) {
				String responseTemp = "<tr><td>" + simSongs.get("artist") + "</td><td>" + 
						"<a href=\"/songInfo?artist=" + simSongs.get("artist") + "&title=" + simSongs.get("title") + "\">" + simSongs.get("title") + "</a>" + "</td><td>" +  
						"Liked" +
						"</td> </tr>";
				responseTable += responseTemp;
			}
			else {
				String responseTemp = "<tr><td>" + simSongs.get("artist") + "</td><td>" +  
						"<a href=\"/songInfo?artist=" + simSongs.get("artist") + "&title=" + simSongs.get("title") + "\">" + simSongs.get("title") + "</a>" + "</td><td>" + 
						"<form action=\"favsList\" method=\"post\">" +
						"<input name=\"trackId\" type=\"hidden\" value=" + trackId + ">" +
						"<button onClick='submit();'> Add </button> " +
						"</form>" +
						"</td> </tr>";
				responseTable += responseTemp;

			}
		}
		
		String footer = 
				"<center> <a href=\"/logout\" >Logout</a> </center>" +
				"<center> <a href=\"/search\" >Go back to Search</a> </center>" +
				"<center> <a href=\"/favsList\" >Favs List</a> </center>" +
				"</html>";
		
		
		String finalResponse = header + songInfo + responseTable + footer;
		PrintWriter writer = prepareResponse(response);
		writer.println(finalResponse);
		
	}

}