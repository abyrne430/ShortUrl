package com;

import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Abstract class that defines the methods to handle different types of http requests.
 */
public abstract class Endpoints {

  protected static final Logger mLogger = LoggerFactory.getLogger(Endpoints.class);

  private int mResponseCode = 200;

  /**
   * Get the response code for the endpoint
   * @return mResponseCode response code
   */
  public int getResponseCode() {
    return mResponseCode;
  }

  /**
   * Set the response code for the endpoint
   * @param pResponseCode
   */
  public void setResponseCode(int pResponseCode) {
    mResponseCode = pResponseCode;
  }

  // Methods to be overridden by subclass
  public abstract Object handleGET(Object pRequest, String pPath);
  public abstract Object handlePOST(Object pRequest);

  /**
   * Handle the http request and return a response
   * @param pRequestMethod http method
   * @param pRequestBody http request body
   * @param pEndpointPath http URI path
   * @return response
   */
  public String handleRequest(String pRequestMethod, String pRequestBody, String pEndpointPath) {
    mLogger.info("Handling " + pRequestMethod + "request for " + pEndpointPath);
    JSONObject requestBodyJson = new JSONObject();

    // if there is no request body we just continue with an empty json object
    if (pRequestBody != null && !pRequestBody.equals("")) {
      // build json object out of request string
      requestBodyJson = new JSONObject(pRequestBody);
    }

    Object responseObject = null;

    // determine the http method we are dealing with
    switch (pRequestMethod) {
      case "GET":
        // we are in a GET request so we should be querying based on an id, else it will be a collection GET with a request body
        // lets pass the endpoint path in for the overriding method to extract any id that exists
        responseObject = handleGET(requestBodyJson, pEndpointPath);
        break;
      case "POST":
        // we are in a POST request so pass the request body into the handlePost method
        responseObject = handlePOST(requestBodyJson);
        break;
      default:
        break;
    }

    // no response received so return an empty response
    if(responseObject == null) {
      return "";
    }
    if(responseObject instanceof String) {
      return (String)responseObject;
    }

    if(responseObject instanceof JSONObject) {
      return (responseObject.toString());
    }

    // may be used in instance of a collection endpoint
    if(responseObject instanceof JSONArray) {
      return (responseObject.toString());
    }
    return "";
  }

  /**
   * Build an error response and set the response's error code
   * @param pErrorMessage error message
   * @param pErrorCode http error code
   * @return error response as JSONObject
   */
  public JSONObject createErrorResponse(String pErrorMessage, int pErrorCode) {
    JSONObject errorResponse = new JSONObject();
    errorResponse.put("error", pErrorMessage);
    setResponseCode(pErrorCode);
    return errorResponse;
  }

}
