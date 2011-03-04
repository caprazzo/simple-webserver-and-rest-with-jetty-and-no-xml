package net.caprazzi.rest;

import java.io.IOException;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.eclipse.jetty.util.log.Log;
import org.eclipse.jetty.websocket.WebSocket;
import org.eclipse.jetty.websocket.WebSocketServlet;

/**
 * WebSocket publishing servlet. Any string passed to publish() will be broadcasted to all consumers.
 * Any message received from consumers, will be sent to other consumers.
 * @author mcaprari
 */
@SuppressWarnings("serial")
public class PublishServlet extends WebSocketServlet {
	
	private volatile Set<PublishWebSocket> _consumers = new CopyOnWriteArraySet<PublishWebSocket>();

	public void publish(String quote) {
		for (PublishWebSocket member : _consumers) {
			try {
				member._outbound.sendMessage((byte)0, quote);
			} catch (IOException e) {
				Log.warn(e);
			}
		}
	}
	
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
		throws ServletException, IOException {
		
		getServletContext()
			.getNamedDispatcher("default")
			.forward(request, response);
	}

	protected WebSocket doWebSocketConnect(HttpServletRequest request, String protocol) {
		return new PublishWebSocket();
	}

	class PublishWebSocket implements WebSocket {
		Outbound _outbound;

		public void onConnect(Outbound outbound) {
			_outbound = outbound;
			_consumers.add(this);
		}

		public void onMessage(byte frame, byte[] data, int offset, int length) {
		}

		public void onMessage(byte frame, String data) {
			for (PublishWebSocket member : _consumers) {
				try {
					member._outbound.sendMessage(frame, data);
				} catch (IOException e) {
					Log.warn(e);
				}
			}
		}

		public void onDisconnect() {
			_consumers.remove(this);
		}

		@Override
		public void onFragment(boolean more, byte opcode, byte[] data,
				int offset, int length) {
		}		
	}	
}
