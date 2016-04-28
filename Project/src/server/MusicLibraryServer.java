package server;
import java.nio.file.Path;
import java.nio.file.Paths;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;

import main.MusicLibraryBuilder;
import threadSafety.ThreadSafeMusicLibrary;

public class MusicLibraryServer {
	
	
	public static final int DEFAULT_PORT = 12051;
	
	public static void main(String[] args) throws Exception {
		
		Server server = new Server(DEFAULT_PORT);
		
		ServletContextHandler servhandler = new ServletContextHandler(ServletContextHandler.SESSIONS);        
		server.setHandler(servhandler);
		
		servhandler.addEventListener(new ServletContextListener() {

			@Override
			public void contextDestroyed(ServletContextEvent sce) {
				//Do nothing when server shut down.
			}

			@Override
			public void contextInitialized(ServletContextEvent sce) {
				
				Path path = Paths.get("input/lastfm_subset");
				MusicLibraryBuilder mlb = new MusicLibraryBuilder(path);
				ThreadSafeMusicLibrary musicLib = mlb.getMusicLibrary();
				mlb.traverseParser();
				
				//if grades file is not valid then create an empty GradeBook.
				if(musicLib == null) {
					musicLib = new ThreadSafeMusicLibrary();
				}
				sce.getServletContext().setAttribute("musicLibrary", musicLib);
			
			}
		});
		// this is where u add each page
		servhandler.addServlet(SearchServlet.class, "/search");

		//servhandler.addServlet(SongsServlet.class, "/search");
		server.setHandler(servhandler);

		server.start();
		server.join();
	}
}