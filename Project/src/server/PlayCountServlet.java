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

public class PlayCountServlet extends BaseServlet{
	
public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		HttpSession session = request.getSession();
		if(!isLoggedIn(request, response, session)) {
			response.sendRedirect(response.encodeRedirectURL("/login?" + STATUS + "=" + NOT_LOGGED_IN));
			return;
		}
		
		String responseTable = "<html>" +
				"<center>Here's a list of all the Artists in the Library sorted by Play Count! </center>" +
				"<br>" +
				"<table border=\"2px\" width=\"100%\">" +				
				"<tr><td><strong>Artists</strong></td></tr>";
		
		TreeMap <Integer, String> byPlayCount = new TreeMap<Integer, String>();
		byPlayCount = DBHelper.byPlayCount();
		
		for (int eachSong : byPlayCount.navigableKeySet()) {
			
			String responseTemp = "<tr><td>" + "<a href=\"/artistinfo?artist=" + byPlayCount.get(eachSong) + "\">" + byPlayCount.get(eachSong) + "</a>" + "</td></tr>";
			
			responseTable += responseTemp;
		}
		
		String header = header();
		String footer = "<html>" +
				"<center> <a href=\"/logout\" >Logout</a> </center>" +
				"<center> <a href=\"/search\" >Go back to Search</a> </center>" +
				"<center> <a href=\"/favsList\" >Favs List</a> </center>" +
				"</html>";
		
		
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
