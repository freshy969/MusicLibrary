package server;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.TreeMap;
import java.util.TreeSet;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import database.DBHelper;
import main.Song;
import threadSafety.ThreadSafeMusicLibrary;

public class ArtistInfoServlet extends BaseServlet {

	

	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		HttpSession session = request.getSession();
		if(!isLoggedIn(request, response, session)) {
			response.sendRedirect(response.encodeRedirectURL("/login?" + STATUS + "=" + NOT_LOGGED_IN));
			return;
		}
		
		String artist = request.getParameter("artist");
		String bio = DBHelper.getBio(artist);
		int listeners = DBHelper.getListenersOrPlayCount(artist,"listeners");
		int playcount = DBHelper.getListenersOrPlayCount(artist, "playcount");
		
		//ThreadSafeMusicLibrary tsml = (ThreadSafeMusicLibrary) request.getServletContext().getAttribute("musicLibrary");
		//TreeMap <String, TreeSet<Song>> byArtistAlphabetical = new TreeMap<String, TreeSet<Song>>();
		//byArtistAlphabetical = tsml.getArtistMap();
		
		String body = "<html>" + 
					  "<h2><center>Artist: " + artist + "</center></h2>" +
					  "<h2><center>Listeners: " + listeners + "</center></h2>" +
					  "<h2><center>Play Count: " + playcount + "</center></h2>" +
					  "<h2><center> Bio: </center></h2>" + 
					  "<h4><center> " + bio + "</center></h4>" +
					  "</html>";
		
		
		
		System.out.println("bio : " + bio);
		System.out.println("listeners : " + listeners);
		System.out.println("playcount : " + playcount);
		
		String header = header();
		String footer =  "<html>" +
				"<center> <a href=\"/logout\" >Logout</a> </center>" +
				"<center> <a href=\"/search\" >Go back to Search</a> </center>" +
				"<center> <a href=\"/favsList\" >Favs List</a> </center>" +
				"<center> <a href=\"/allArtists\" > See all Artist Alphabetically </a> </center>" + 
				"<center> <a href=\"/playcount\" >All Artists sorted by Play Count</a> </center>" +
				"</html>";
		String finalResponse = header + body + footer;
		//String finalResponse = header();
		PrintWriter writer = null;
		try {
			writer = prepareResponse(response);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		writer.println(finalResponse);
		
		
	}
}
