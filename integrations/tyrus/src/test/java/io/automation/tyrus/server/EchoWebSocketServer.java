package io.automation.tyrus.server;

import java.net.InetSocketAddress;

import org.java_websocket.WebSocket;
import org.java_websocket.handshake.ClientHandshake;
import org.java_websocket.server.WebSocketServer;

public class EchoWebSocketServer extends WebSocketServer {

  public EchoWebSocketServer(int port) {
    super(new InetSocketAddress(port));
    setReuseAddr(true);
  }

  @Override
  public void onOpen(WebSocket conn, ClientHandshake handshake) { }

  @Override
  public void onClose(WebSocket conn, int code, String reason, boolean remote) { }

  @Override
  public void onMessage(WebSocket conn, String message) {
    conn.send(message);
  }

  @Override
  public void onError(WebSocket conn, Exception ex) { }

  @Override
  public void onStart() { }
}
