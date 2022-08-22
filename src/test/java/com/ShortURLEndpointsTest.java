package com;

import com.repository.URLRepository;
import org.json.JSONObject;
import org.junit.Test;

/**
 * Test class for ShortURLEndpoints
 */
public class ShortURLEndpointsTest {

  @Test
  public void testPostShortUrl() {
    JSONObject requestPayload = new JSONObject();
    requestPayload.put("url", "longUrlExample.com/test");
    URLRepository urlRepository = URLRepository.getInstance();
    ShortURLEndpoints shortURLEndpoints = new ShortURLEndpoints(urlRepository);
    JSONObject response = (JSONObject) shortURLEndpoints.handlePOST(requestPayload);
    // confirm a new url has been returned
    assert response.has("url");
  }

  @Test
  public void testPostShortUrlWithNonStringUrl() {
    JSONObject requestPayload = new JSONObject();
    requestPayload.put("url", 1234);
    URLRepository urlRepository = URLRepository.getInstance();
    ShortURLEndpoints shortURLEndpoints = new ShortURLEndpoints(urlRepository);
    JSONObject response = (JSONObject) shortURLEndpoints.handlePOST(requestPayload);
    // confirm a new url has been returned
    assert response.has("error");
    assert response.getString("error").equals("Property 'url' should be of type 'String'");
  }

  @Test
  public void testPostShortUrlWithNoUrlProperty() {
    JSONObject requestPayload = new JSONObject();
    requestPayload.put("wrongProperty", "longUrlExample.com/test");
    URLRepository urlRepository = URLRepository.getInstance();
    ShortURLEndpoints shortURLEndpoints = new ShortURLEndpoints(urlRepository);
    JSONObject response = (JSONObject) shortURLEndpoints.handlePOST(requestPayload);
    // confirm a new url has been returned
    assert response.has("error");
    assert response.getString("error").equals("Property 'url' is missing");
  }

  @Test
  public void testGetUrlFromShortUrlId() {
    String longUrlExample = "longUrlExample.com/test";
    JSONObject requestPayload = new JSONObject();
    requestPayload.put("url", longUrlExample);
    URLRepository urlRepository = URLRepository.getInstance();
    ShortURLEndpoints shortURLEndpoints = new ShortURLEndpoints(urlRepository);
    JSONObject response = (JSONObject) shortURLEndpoints.handlePOST(requestPayload);
    String shortUrl = response.getString("url");
    String[] segments = shortUrl.split("/");
    String id = segments[1];
    JSONObject responseGET = (JSONObject) shortURLEndpoints.handleGET(null, "/shortUrl/" + id);

    assert responseGET.getString("url").equals(longUrlExample);
  }

  @Test
  public void testGetUrlNotInRepository() {
    URLRepository urlRepository = URLRepository.getInstance();
    ShortURLEndpoints shortURLEndpoints = new ShortURLEndpoints(urlRepository);
    JSONObject responseGET = (JSONObject) shortURLEndpoints.handleGET(null, "/shortUrl/someRandomId");

    assert responseGET == null;
  }

  @Test
  public void testGetUrlInvalidCharacter() {
    URLRepository urlRepository = URLRepository.getInstance();
    ShortURLEndpoints shortURLEndpoints = new ShortURLEndpoints(urlRepository);
    JSONObject responseGET = (JSONObject) shortURLEndpoints.handleGET(null, "/shortUrl/invalid/id");

    assert responseGET.has("error");
    assert responseGET.getString("error").equals("Invalid character '/' in path id.");
  }


}
