package com.repository;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Local memory repository that holds key/value pairs for urls and their base 10 id.
 */
public class URLRepository {

  private static URLRepository mInstance = null;
  private final Map<Integer, String> mUrlRepository = new LinkedHashMap<>();

  /**
   * Create a single instance of the repository
   * @return repository instance
   */
  public static URLRepository getInstance() {
    if(mInstance == null) {
      mInstance = new URLRepository();
    }
    return mInstance;
  }

  /**
   * Get the url based assigned to the id provided
   * @param pId id to query the repository
   * @return url string assigned to the id
   */
  public String getURL(int pId) {
    return mUrlRepository.get(pId);
  }

  /**
   * Adds a URL to the repository and assigns it a unique id
   * @param pLongUrl url to be added to the repository
   * @return id assigned to the url
   */
  public int addUrl(String pLongUrl) {
    // lets get the last id to be added to the map.
    // We can increment this id to add our next id to our storage
    List<Map.Entry<Integer, String>> entryList = new ArrayList<>(mUrlRepository.entrySet());

    int nextId = 10000;

    // if there are entries in the repository lets get the final id in the repository
    // we then can add a new entry with the next number after the existing final id
    if (entryList.size() > 0) {
      Map.Entry<Integer, String> lastEntry = entryList.get(entryList.size() - 1);
      int lastId = lastEntry.getKey();
      nextId = lastId + 10;
    }

    // add the url to the repository with the generated id
    mUrlRepository.put(nextId, pLongUrl);

    // returning the id that has been added to the repository
    return nextId;
  }

}
