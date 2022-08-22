package com;

import com.math.Base62;
import com.repository.URLRepository;

/**
 * Class that will use conversion techniques to determine a url.
 */
public class URLConverter {

  /**
   * Convert a long url to a short url.
   * Adds the long url to the repository and uses the resulting repository id to create a base 62 id to use in the url.
   * @param pLongUrl long url to be converted
   * @return generated base62 string
   */
  public String convertToShortUrl(String pLongUrl) {
    // here we will add the long url to the repository and retrieve the id from the repository
    int idForConversion = URLRepository.getInstance().addUrl(pLongUrl);
    return Base62.fromBase10(idForConversion);
  }

  /**
   * Convert a id to its given long url
   * Takes the id passed in and converts it to its base 10 value
   * Queries the repository for the base 10 id and returns the long url value.
   * @param pUrlId base 62 url id
   * @return long url stored in repository
   */
  public String convertToLongUrl(String pUrlId) {
    int id = Base62.toBase10(pUrlId);
    String longUrl = URLRepository.getInstance().getURL(id);
    return longUrl;
  }

}
