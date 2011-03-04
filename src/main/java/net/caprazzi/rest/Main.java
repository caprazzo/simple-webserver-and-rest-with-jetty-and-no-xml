package net.caprazzi.rest;

import java.net.URL;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.DefaultServlet;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;

public class Main<T> {
	public static void main(String[] args) throws Exception {

		Server server = new Server(8080);
		 
        ServletContextHandler context = new ServletContextHandler(ServletContextHandler.SESSIONS);
        context.setContextPath("/");
        server.setHandler(context);
 
        // configure the default servlet to serve static files from "htdocs"
        context.getInitParams().put("org.eclipse.jetty.servlet.Default.resourceBase", "htdocs");
        context.addServlet(new ServletHolder(new DefaultServlet()),"/");
        
        // use /uuid to get a fresh id
        context.addServlet(new ServletHolder(new UUIDServlet()), "/uuid");
        
        // the actual key/value store
        context.addServlet(new ServletHolder(new KeyValueServlet()), "/store/*");
        
        // bind a publishServlet to /quotes
        final PublishServlet publishServlet = new PublishServlet();
        context.addServlet(new ServletHolder(publishServlet), "/quotes");
        
        // setup the quote service
        URL guideDataURL = Main.class.getClassLoader().getResource("hitchhiker_guide_to_the_galaxy_quotes.txt");
        final QuoteService guideQuoteService = QuoteService.fromFile(guideDataURL.getFile());
        
        // send out a new quote every 3 to 10 seconds
        new RandomTimer(3, 10) {			
			@Override
			public void tick() {
				publishServlet.publish(guideQuoteService.getRandomQuote());				
			}
		}; 		
        
		// start theserver
        server.start();
        server.join();		
	}
	
	public T get() {
		return null;
	}
}
