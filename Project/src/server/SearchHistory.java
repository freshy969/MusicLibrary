package server;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.TreeMap;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import database.DBHelper;

public class SearchHistory extends BaseServlet {
	
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		HttpSession session = request.getSession();
		String username = (String) session.getAttribute(USERNAME);
		PrintWriter writer = prepareResponse(response);
		if(!isLoggedIn(request, response, session)) {
			response.sendRedirect(response.encodeRedirectURL("/login?" + STATUS + "=" + NOT_LOGGED_IN));
			return;
		}
		
		JSONArray searchHistory = new JSONArray();
		
		searchHistory = DBHelper.displaySearchHistory(username);
		
		String header = header();
		String responseTable = "<html>" +
				"<h3><center>Search History</center></h3>" +
				"<br>" +
				"<table border=\"2px\" width=\"100%\">" +				
				"<tr><td><strong>Search Type</strong></td><td><strong>Search Query</strong></td></tr>";
		if (searchHistory.size() > 0) {
			
			for (int i = 0; i< searchHistory.size(); i++) {
				
				JSONObject eachSearch = (JSONObject) searchHistory.get(i);
				String responseTemp = "<tr><td>" + eachSearch.get("type") + "</td><td>" + 
										eachSearch.get("query") + "</td></tr>";
										responseTable += responseTemp;
				
			}
//			for (String type : searchHistory.navigableKeySet()) {
//				
//				String responseTemp = "<tr><td>" + type + "</td><td>" + 
//										searchHistory.get(type) + "</td></tr>";
//				responseTable += responseTemp;
//			}
		}
		else {
			responseTable = "<center> Your History is Clear!</center>";
		}
		responseTable += "</table>" + "</html>";
		String clearHistory = "<html>" + 
								"<form action=\"searchHistory\" method=\"post\">" +
								"<input name=\"username\" type=\"hidden\" value=" + username + ">" +
								"<center><button onClick='submit();'> Clear History </button></center> " +
								"</form>" +
								"</html>";
		
		String footer = "<html>" + 
				"<center> <a href=\"/logout\" >Logout</a> </center>" +
				"<center> <a href=\"/changepassword\" >Change Password</a> </center>" + 
				"<center> <a href=\"/search\" >Go back to Search</a> </center>" +
				"<center> <a href=\"/favsList\" >Favs List</a> </center>" +
				"<center> <a href=\"/allArtists\" > See all Artist Alphabetically </a> </center>" + 
				"<center> <a href=\"/playcount\" >All Artists sorted by Play Count</a> </center>" +
				"</html>";
		
		String finalResponse = header + responseTable + clearHistory + footer;
		
		writer.println(finalResponse);
		
		
		
		
	}
	
	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		
		HttpSession session = request.getSession();
		String username = (String) session.getAttribute(USERNAME);
		if(!isLoggedIn(request, response, session)) {
			response.sendRedirect(response.encodeRedirectURL("/login?" + STATUS + "=" + NOT_LOGGED_IN));
			return;
		}
		
		DBHelper.clearHistory(username);
		
		response.sendRedirect(response.encodeRedirectURL("/searchHistory"));
		
	}

}
