package net.caprazzi.rest;

import java.io.InputStream;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;

public class Main<T> {
	public static void main(String[] args) throws Exception {

		if (args.length != 1) {
			System.err.println("Please specify the port number");
			return;
		}
		Integer port;
        try {
            port = Integer.parseInt(args[0]);
        }
        catch(NumberFormatException ex) {
            System.err.println("port_number must be a number");
            return;
        
        }
                	
		Server server = new Server(port);
		 
        ServletContextHandler context = new ServletContextHandler(ServletContextHandler.SESSIONS);
        context.setContextPath("/");
        server.setHandler(context);
                
        // configure the default servlet to serve static files from "htdocs" in the classpath   
        context.addServlet(new ServletHolder(new ClasspathFilesServlet("/htdocs")),"/");
        
        // use /uuid to get a fresh id
        context.addServlet(new ServletHolder(new UUIDServlet()), "/uuid");
        
        // the actual key/value store
        context.addServlet(new ServletHolder(new KeyValueServlet()), "/store/*");
        
        // bind a publishServlet to /quotes
        final PublishServlet publishServlet = new PublishServlet();
        context.addServlet(new ServletHolder(publishServlet), "/quotes");
        
        // setup the quote service
        InputStream stream = Main.class.getClassLoader().getResourceAsStream("hitchhiker_guide_to_the_galaxy_quotes.txt");        
        final QuoteService guideQuoteService = QuoteService.fromInputStream(stream);
        
        // send out a new quote every 3 to 10 seconds
        new RandomTimer(3, 10) {			
			@Override
			public void tick() {
				publishServlet.publish(guideQuoteService.getRandomQuote());				
			}
		}; 		
        
		// start the server
        server.start();
        server.join();		
	}
	
	public T get() {
		return null;
	}
}
