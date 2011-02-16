package net.caprazzi.rest;

import org.mortbay.jetty.Server;
import org.mortbay.jetty.servlet.Context;
import org.mortbay.jetty.servlet.DefaultServlet;
import org.mortbay.jetty.servlet.ServletHolder;

public class Main {
	public static void main(String[] args) throws Exception {

		Server server = new Server(8080);
		Context root = new Context(server, "/");

		// configure the default servlet to serve static files from "htdocs"
		root.getInitParams().put("org.mortbay.jetty.servlet.Default.resourceBase", "htdocs");
		root.addServlet(new ServletHolder(new DefaultServlet()), "/");

		// use /uuid to get a fresh id
		root.addServlet(new ServletHolder(new UUIDServlet()), "/uuid");

		// the actual key/value store
		root.addServlet(new ServletHolder(new KeyValueServlet()), "/store/*");
		server.start();
	}
}
