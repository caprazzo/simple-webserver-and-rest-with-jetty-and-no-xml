package net.caprazzi.rest;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.concurrent.ConcurrentHashMap;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Simple in-memory REST key-value store
 * 
 * GET path/<key>: get the value associated with the key
 * PUT path/<key>: put the request busy as value of <key>
 * DELETE path/<key>: delete the key and the value
 * POST: not supported
 *
 */
@SuppressWarnings("serial")
public class KeyValueServlet extends HttpServlet {
	
	private final ConcurrentHashMap<String, byte[]> kv
		= new ConcurrentHashMap<String,byte[]>();
	
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String[] parts = req.getRequestURI().split("/");
		
		if (parts.length == 0) {
			resp.sendError(400);
			return;
		}
				
		String key = parts[parts.length - 1];
		
		byte[] value = kv.get(key);
		if (value == null) {
			resp.sendError(404);
			return;
		}
		
		OutputStream out = resp.getOutputStream();
		out.write(value);
		out.close();		
	}
	
	@Override
	protected void doPut(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		
		String[] parts = req.getRequestURI().split("/");
		byte[] body = readInputStream(req.getInputStream());
		
		if (parts.length == 0 || body.length == 0) {
			resp.sendError(400);
			return;
		}
		
		String key = parts[parts.length - 1];
		kv.put(key, body);
		// 204 No Content
		resp.setStatus(204);
	}
	
	@Override
	protected void doDelete(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		
		String[] parts = req.getRequestURI().split("/");		
		if (parts.length == 0) {
			resp.sendError(400);
			return;
		}
		
		String key = parts[parts.length - 1];
		if (kv.remove(key) == null) {
			// 404 not found
			resp.sendError(404);
			return;
		}
				
		// 204 No Content: delete confirmed
		resp.setStatus(204);
	}
	
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		
		// 405 Method Not Allowed
		resp.sendError(405);
	}
	
	private byte[] readInputStream(InputStream is) throws IOException {
		ByteArrayOutputStream buffer = new ByteArrayOutputStream();

		int nRead;
		byte[] data = new byte[16384];

		while ((nRead = is.read(data, 0, data.length)) != -1) {
		  buffer.write(data, 0, nRead);
		}
		buffer.flush();
		return buffer.toByteArray();
	}	
	
}