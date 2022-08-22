package com.server;

import com.Endpoints;
import com.ShortURLEndpoints;
import com.repository.URLRepository;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;

/**
 * Handles the http requests and responses
 */
public class URLHandler implements HttpHandler {

  // map which stores endpoints for this handler
  private final Map<String, Endpoints> mEndpointMap = new HashMap<String, Endpoints>();

  protected static final Logger mLogger = LoggerFactory.getLogger(URLHandler.class);

  /**
   * Constructor which adds the endpoints appropriate to this server context to a map
   */
  public URLHandler() {
    mEndpointMap.put("/shortUrl", new ShortURLEndpoints(URLRepository.getInstance()));
  }

  /**
   * Handle the httpExchange across the server
   * Get the endpoint path and ensure it matches the endpoint paths for this context.
   * Handle the input request and return the appropriate response.
   * @param pHttpExchange
   * @throws IOException
   */
  @Override
  public void handle(HttpExchange pHttpExchange) throws IOException {
    mLogger.info("Request received. Handling request...");
    String endpointPath = pHttpExchange.getRequestURI().getPath();
    // return the endpoints class for the given path
    Endpoints endpointsInstance = getEndpoints(endpointPath);
    if (endpointsInstance != null) {
      // we have found the endpoints class for the given endpoint path
      String requestBody = getRequestBody(pHttpExchange);
      String resp = endpointsInstance.handleRequest(pHttpExchange.getRequestMethod(), requestBody, endpointPath);
      setResponseBody(pHttpExchange, resp, endpointsInstance.getResponseCode());
    } else {
      JSONObject error = new JSONObject();
      error.put("error", "Invalid URI path");
      String response = error.toString();
      setResponseBody(pHttpExchange, response, 400);
    }
  }

  /**
   * Get the request body from the http exchange.
   * @param pHttpExchange
   * @return request body as a string
   * @throws IOException
   */
  private String getRequestBody(HttpExchange pHttpExchange) throws IOException {
    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(pHttpExchange.getRequestBody()));
    StringBuilder stringBuilder = new StringBuilder();
    String line;
    while((line = bufferedReader.readLine()) != null) {
      stringBuilder.append(line);
      stringBuilder.append("\n");
    }
    bufferedReader.close();
    return stringBuilder.toString();
  }

  /**
   * Get the endpoints that are associated with the path passed in
   * @param pEndpointPath endpoint path to query against endpoints in this context
   * @return instance of Endpoints class
   */
  private Endpoints getEndpoints(String pEndpointPath) {
    // check if the the request path matches the endpoints in this context
    if (mEndpointMap.get(pEndpointPath) != null) {
      // path matches an endpoint for this context
      return mEndpointMap.get(pEndpointPath);
    } else {
      // no matching path but we may be querying a specific resource
      for (Map.Entry<String, Endpoints> endpoints : mEndpointMap.entrySet()) {
         String validPath = endpoints.getKey();
         if (pEndpointPath.startsWith(validPath + "/")) {
           // this will indicate we are still using an endpoint associated for this handler so return the endpoints
           return endpoints.getValue();
         }
      }
      // return null if path does not match endpoints for the handler
      return null;
    }
  }

  /**
   * Set the response on the http exchange
   * @param pHttpExchange http exchange
   * @param pResponseBody response body as a string
   * @param pResponseCode response code
   * @throws IOException
   */
  private void setResponseBody(HttpExchange pHttpExchange, String pResponseBody, int pResponseCode) throws IOException {
    pHttpExchange.getResponseHeaders().add("Content-Type", "application/json");
    pHttpExchange.sendResponseHeaders(pResponseCode, pResponseBody.length());
    OutputStream os = pHttpExchange.getResponseBody();
    os.write(pResponseBody.getBytes());
    os.close();
  }

}
