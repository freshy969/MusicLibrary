package server;
import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import threadSafety.ThreadSafeMusicLibrary;

public class SearchServlet extends BaseServlet {
	
	
	private static final long serialVersionUID = 1L;

	/**
	 * GET /search returns a web page containing a search box where a student's name may be entered.
	 */
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		String responseHtml = responseFormat();
		PrintWriter writer = prepareResponse(response);
		writer.println(responseHtml);
	}
	
	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
			
		String query = request.getParameter("query");
		String type = request.getParameter("type");
		ThreadSafeMusicLibrary tsml = (ThreadSafeMusicLibrary) request.getServletContext().getAttribute("musicLibrary");
		JSONArray result = new JSONArray();
		if (query != null) {
			if (type.equals("artist")) {
				result = tsml.searchByArtist(query);
			}
			else if (type.equals("title")) {
				result = tsml.searchByTitle(query);
			}
			else if (type.equals("tag")) {				
				result = tsml.searchByTag(query);
			}
		}
		
		String responseHtmlHead = "<html" + 
				"<head><title>Song Finder</title></head>" +
				"<body>";
		String responseHtmlContent = "";
		if(result.size() > 0) {
			String responseTable = "<center>Here are some songs you might like!</center>" +
										"<br>" +
										"<table border=\"2px\" width=\"100%\">" +				
										"<tr><td><strong>Artist</strong></td><td><strong>Song Title</strong></td></tr>";
			responseHtmlContent += responseTable;
			for (int i = 0; i<result.size(); i++) {
				JSONObject simSongs = (JSONObject) result.get(i);
				String responseTemp = "<tr><td>" + simSongs.get("artist") + "</td><td>" + simSongs.get("title") + "</td></tr>";
				responseHtmlContent += responseTemp;
				
			}
			
			String endTable = "</table>";
			responseHtmlContent += endTable;
		
		} else {
			responseHtmlContent = "<center>" + query + " not found!</center>";
		}
		
		String responseEnder = 	responseFormat();
		 
		String responseHtml = responseHtmlHead + responseEnder + responseHtmlContent;
		PrintWriter writer = prepareResponse(response);
		writer.println(responseHtml);
	}

}
