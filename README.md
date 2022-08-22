# ShortUrl Overview

This Short URL Generator service converts long urls into short urls. It also allows us to query the long url that is stored against an id.

For this service, URL's are currently stored in local memory.

## REST API

There are two endpoints currently available for this service:

GET /shortUrl/{id} - The id can be found on the end of the short url

POST /shortUrl/ - { "url" : "someLongUrl.com/" } 

## Building

The service is built using JDK 11.
Maven is required to build the service. The typical build commands to follow are are:
```
cd ShortUrl
mvn clean install
mvn package 
mvn test - this will run the tests independently but they will also be run with above commandes
cd target
java -jar URL.jar