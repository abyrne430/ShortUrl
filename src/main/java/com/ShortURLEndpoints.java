package com;

import com.repository.URLRepository;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Subclass of endpoints class that overrides http handling methods
 * Implements logic specific to short urls endpoints
 */
public class ShortURLEndpoints extends Endpoints {

  private static final String SHORT_URL = "shortUrl.com";
  private static final String ENDPOINT_PATH = "/shortUrl";
  private static final String URL = "url";
  private static final String DELIMITER = "/";
  private final URLRepository mUrlRepository;

  protected static final Logger mLogger = LoggerFactory.getLogger(ShortURLEndpoints.class);

  /**
   * Constructor which sets the url repository
   * @param pUrlRepository instance of UrlRepository
   */
  public ShortURLEndpoints(URLRepository pUrlRepository) {
    mUrlRepository = pUrlRepository;
  }

  /**
   * Handle http GET request
   * @param pRequest request body
   * @param pPath request path
   * @return response
   */
  @Override
  public Object handleGET(Object pRequest, String pPath) {
    String id = null;
    String longUrl = null;
    JSONObject response = new JSONObject();
    if (pPath.equals(ENDPOINT_PATH)) {
      // TODO - this could be a collection GET so use the request body
    } else {
      // lets check if we are querying an id
      if (pPath.startsWith(ENDPOINT_PATH + DELIMITER)) {
        String[] segments = pPath.split(DELIMITER);
        if (segments.length > 3) {
          // the string should only be split into three segments using the '/' delimiter eg. /shortUrl/{id}
          // id will not contain '/' so any further splits using "/" is an error
          return createErrorResponse("Invalid character '/' in path id.", 400);
        } else {
          // resource id will be in the final segment
          id = segments[2];
          if (id == null || id.equals("")) {
            System.out.println("Id is null");
            // TODO - this could be a collection GET so use the request body
          } else {
            mLogger.info("Querying id: " + id);
            URLConverter urlConverter = new URLConverter();
            longUrl = urlConverter.convertToLongUrl(id);
            if (longUrl != null) {
              return response.put(URL, longUrl);
            }
          }
        }
      }
    }
    mLogger.info("No resource found for given request.");
    return null;
  }

  /**
   * Handle a http POST request
   * @param pRequest input request
   * @return response JSONObject
   */
  public Object handlePOST(Object pRequest) {
    // first check are we dealing with a valid json object
    if(!(pRequest instanceof JSONObject)) {
      return createErrorResponse("Not a valid json object.", 400);
    }

    String urlForConversion = null;
    // check if the request contains a URL to be converted to a short URL
    if (((JSONObject) pRequest).has(URL)) {
      Object urlFromRequest = ((JSONObject) pRequest).get(URL);
      if (urlFromRequest instanceof String) {
        urlForConversion = (String) urlFromRequest;
      } else {
        return createErrorResponse("Property 'url' should be of type 'String'", 400);
      }
    } else {
      return createErrorResponse("Property 'url' is missing", 400);
    }

    // convert the url into a short url
    URLConverter urlConverter = new URLConverter();
    String base62Id = urlConverter.convertToShortUrl(urlForConversion);

    JSONObject response = new JSONObject();
    response.put(URL, SHORT_URL + DELIMITER + base62Id);

    return response;
  }
}
