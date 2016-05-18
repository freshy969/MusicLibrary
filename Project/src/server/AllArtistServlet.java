package server;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.TreeMap;
import java.util.TreeSet;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import main.Song;
import threadSafety.ThreadSafeMusicLibrary;

public class AllArtistServlet extends BaseServlet {
	
	

	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		HttpSession session = request.getSession();
		if(!isLoggedIn(request, response, session)) {
			response.sendRedirect(response.encodeRedirectURL("/login?" + STATUS + "=" + NOT_LOGGED_IN));
			return;
		}
		
		ThreadSafeMusicLibrary tsml = (ThreadSafeMusicLibrary) request.getServletContext().getAttribute("musicLibrary");
		TreeMap <String, TreeSet<Song>> byArtistAlphabetical = new TreeMap<String, TreeSet<Song>>();
		byArtistAlphabetical = tsml.getArtistMap();
		
		String header = header();
		String footer = "<html>" +
				"<center> <a href=\"/logout\" >Logout</a> </center>" +
				"<center> <a href=\"/changepassword\" >Change Password</a> </center>" + 
				"<center> <a href=\"/search\" >Go back to Search</a> </center>" +
				"<center> <a href=\"/favsList\" >Favs List</a> </center>" +
				"<center> <a href=\"/playcount\" >All Artists sorted by Play Count</a> </center>" +
				"</html>";
		
		String responseTable = "<html>" +
				"<center>Here's a list of all the Artists in the Library sorted Alphabetically! </center>" +
				"<br>" +
				"<table border=\"2px\" width=\"100%\">" +				
				"<tr><td><strong>Artists</strong></td></tr>";
		
		
		for (String eachArtist : byArtistAlphabetical.navigableKeySet()) {
			
			String responseTemp = "<tr><td>" + "<a href=\"/artistinfo?artist=" + eachArtist + "\">" + eachArtist + "</a>" + "</td></tr>";  
					
			responseTable += responseTemp;
		}
		
		responseTable += "</table>" + "</html>";
		

		String finalResponse = header  + footer + responseTable;
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
