package server;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import database.DBHelper;

public class FavsListServlet extends BaseServlet {
	
	
	
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		
		String header = header();
		String footer = footer();
		HttpSession session = request.getSession();
		if(!isLoggedIn(request, response, session)) {
			response.sendRedirect(response.encodeRedirectURL("/login?" + STATUS + "=" + NOT_LOGGED_IN));
			return;
		}
		String username = (String) session.getAttribute(USERNAME);
		HashMap<String, ArrayList<String>> favMap = new HashMap<String, ArrayList<String>>();
		String allRows = "";
		favMap = DBHelper.getFavList(username);
		if (favMap.isEmpty()) {
			String favoriteTable = "<center> You have no favorite songs YET! </center>";
			String finalResponse = header + favoriteTable + footer;
			PrintWriter writer = prepareResponse(response);
			writer.println(finalResponse);
			
		}
		else {
			String favoriteTable = "<center>Here are your favorites songs!</center>" +
					"<br>" +
					"<table border=\"2px\" width=\"100%\">" +				
					"<tr><td><strong>Artist</strong></td><td><strong>Song Title</strong></td></tr>";
			allRows += favoriteTable;
			for (ArrayList<String> info : favMap.values()) {
				
				String eachFavRow  = "<tr><td>" + info.get(0)+ "</td><td>" + info.get(1) + "</td></tr>";
				allRows += eachFavRow;
			}
			String endOfTable = "</table>";
			allRows += endOfTable;

			
			String finalResponse = header + allRows + footer;
			PrintWriter writer = prepareResponse(response);
			writer.println(finalResponse);
		}
		
	}

}