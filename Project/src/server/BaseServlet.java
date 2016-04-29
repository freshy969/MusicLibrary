package server;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletResponse;

/**
 * A Servlet superclass with methods common to all servlets for this application.
 * @author srollins
 *
 */
public class BaseServlet extends HttpServlet {

	protected PrintWriter prepareResponse(HttpServletResponse response) throws IOException {
		response.setContentType("text/html");
		response.setStatus(HttpServletResponse.SC_OK);
		return response.getWriter();
	}
	
	protected String responseFormat() {
		String responseFormat = "<html" + 
										"<head><title>Song Finder</title></head>" + 
										"<h1><center> Song Finder</center></h1>" +
										"<hr>" +
										"<body>" +
										"<center> Welcome to Song Finder! Search for an artist, song title, or tag and we will give you a list of similar songs you might like. Enjoy!</center><br/>" +
										"<form action=\"search\" method=\"post\">" +
										"<center>Search Type: " +
										"<select name =\"type\">" +
										"<option value=\"artist\">artist</option>" +
										"<option value=\"title\">title</option>" +
										"<option value=\"tag\">tag</option>" + 
										"</select>" +
										"Query: " +
										"<input type=\"text\" name=\"query\">" +
										"<input type=\"submit\" value=\"Submit\"></center><br/>" +
										"</form>" +
										"</body>" +
										"</html>";
		
		return responseFormat;
	}

}
