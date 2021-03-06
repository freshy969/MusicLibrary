package server;

import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import database.DBHelper;


public class NewAccountServlet extends BaseServlet{
	
	DBHelper dbhelper = new DBHelper();
	
	
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		String header = header();
		PrintWriter writer = prepareResponse(response);
		String status = getParameterValue(request, STATUS);
		boolean statusok = status != null && status.equals(ERROR)?false:true;
		boolean mismatch = status != null && status.equals(MIXMATCH)?true:false;
		boolean unexist = status != null && status.equals(UNEXISTS)?true:false;
		
		String accountInfo = "<html" +
							"<body>" +
							"<form action=\"newUser\" method=\"post\">" +
							"<center> Create New User </center><br/>" + 
							"<center> Full Name: " +
							"<input type=\"text\" name=\"fullname\"></center><br/>" +
							"<center> Username: "  +
							"<input type=\"text\" name=\"username\"></center><br/>" +
							"<center> Password: "  +
							"<input type=\"password\" name=\"password\"></center><br/>" +
							"<center> Verify Password: " +
							"<input type=\"password\" name=\"vpassword\"></center><br/>" +
							"<hr>" +
							"<hr>" +
							"<center><input type=\"submit\" value=\"Create Account\"></center><br/>" +
							"</form>" +
							"</body>" +
							"</html>";
		
		if(!statusok) {
			writer.println("<h3><font color=\"red\">Invalid Request to Create a New User</font></h3>");
		} else if(mismatch) {
			writer.println("<h3><font color=\"red\">Passwords do not Match!</font></h3>");
		} else if(unexist) {
			writer.println("<h3><font color=\"red\">Username Already Exist!</font></h3>");
		}
		
		String responseHtml = header + accountInfo;
		
		writer.println(responseHtml);
	}
	
	
	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		String fullname = request.getParameter(FULLNAME);
		String username = request.getParameter(USERNAME);
		String password = request.getParameter(PASSWORD);
		String verifypass = request.getParameter(VERIFYPASS);
		
		if ((fullname == null || fullname.trim().equals("")) || (username == null || username.trim().equals(""))) {
			response.sendRedirect(response.encodeRedirectURL("/newUser?" + STATUS + "=" + ERROR));
			return;
		}
		if ((password == null || password.trim().equals("")) || (verifypass == null || verifypass.trim().equals(""))) {
			response.sendRedirect(response.encodeRedirectURL("/newUser?" + STATUS + "=" + ERROR));
			return;
		}
		
		if (!password.equals(verifypass)) {
			// passwords don't match
			response.sendRedirect(response.encodeRedirectURL("/newUser?" + STATUS + "=" + MIXMATCH));
			return;
		}
		
		if (DBHelper.verifyUserName(username)) {
			// username already exists
			response.sendRedirect(response.encodeRedirectURL("/newUser?" + STATUS + "=" + UNEXISTS));
			return;
		}
		
		HttpSession session = request.getSession();
		session.setAttribute(FULLNAME, fullname);
		session.setAttribute(USERNAME, username);
		session.setAttribute(PASSWORD, password);
		
		DBHelper.addUser(fullname, username, password);
		
		response.sendRedirect(response.encodeRedirectURL("/search"));	
				
	}
	
	

}
