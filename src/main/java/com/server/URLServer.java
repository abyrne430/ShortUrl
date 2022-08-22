package com.server;

import com.sun.net.httpserver.HttpServer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetSocketAddress;

/**
 * Class which handles server startup.
 */
public class URLServer {

  protected static final Logger mLogger = LoggerFactory.getLogger(URLServer.class);

  /**
   * Main method that will initialize and start a server on the given port.
   * Creates the context for the server by initializing the http handler to be used
   * @param args
   * @throws Exception
   */
  public static void main(String[] args) throws Exception {

    mLogger.info("Starting server");
    // Create the server and set the context to the URLHandler
    HttpServer server = HttpServer.create(new InetSocketAddress(8090), 0);
    server.createContext("/", new URLHandler());
    server.setExecutor(null); // creates a default executor
    server.start();
    mLogger.info("Server is running on port " + 8090);
  }

}
