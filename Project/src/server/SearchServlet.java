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
		
		String responseHtml = "<html" + 
									"<head><title>Song Finder</title></head>" + 
									"<body>" +
									"Welcome to Song Finder! Search for an artist, song title, or tag and we will give you a list of similar songs you might like. Enjoy!<br/>" +
									"<form action=\"search\" method=\"post\">" +
									"Search Type: " +
									"<select name =\"type\">" +
									"<option value=\"artist\">artist</option>" +
									"<option value=\"title\">title</option>" +
									"<option value=\"tag\">tag</option>" + 
									"</select>" +
									"Query: " +
									"<input type=\"text\" name=\"student\">" +
									"<input type=\"submit\" value=\"Submit\"><br/>" +
									"</form>" +
									"</body>" +
									"</html>"; 
		
		PrintWriter writer = prepareResponse(response);
		writer.println(responseHtml);
	}
	
	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		String student = request.getParameter("student");
		String type = request.getParameter("type");
		ThreadSafeMusicLibrary tsml = (ThreadSafeMusicLibrary) request.getServletContext().getAttribute("musicLibrary");
		JSONArray result = new JSONArray();
		if (student != null) {
			if (type.equals("artist")) {
				result = tsml.searchByArtist(student);
			}
			else if (type.equals("title")) {
				result = tsml.searchByTitle(student);
			}
			else if (type.equals("tag")) {
				result = tsml.searchByTag(student);
			}
		}
		
		String responseHtmlHead = "<html" + 
				"<head><title>Song Finder</title></head>" +
				"<body>";
		String responseHtmlContent = "";
		if(result.size() > 0) {
			String responseTable = "Here are some songs you might like!" +
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
			responseHtmlContent = student + " not found!";
		}
		
		String responseEnder = 	"Welcome to Song Finder! Search for an artist, song title, or tag and we will give you a list of similar songs you might like. Enjoy!<br/>" +
				
									"<form action=\"search\" method=\"post\">" +	
									"Search Type: " +
									"<select name = \"type\">" +
									"<option value=\"artist\">artist</option>" +
									"<option value=\"title\">title</option>" +
									"<option value=\"tag\">tag</option>" + 
									"</select>" + 
									"Query: " +
									"<input type=\"text\" name=\"student\">" +
									"<input type=\"submit\" value=\"Submit\"><br/>" +
									"</form>" +
									"</body>" +
									"</html>";
		 
		String responseHtml = responseHtmlHead + responseEnder + responseHtmlContent;
		PrintWriter writer = prepareResponse(response);
		writer.println(responseHtml);
	}

}