package edu.brown.cs32.examples.moshiExample;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.squareup.moshi.Moshi;
import edu.brown.cs32.examples.moshiExample.server.Handlers.GetCategories;
import edu.brown.cs32.examples.moshiExample.server.Handlers.weatherHandler;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import okio.Buffer;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import spark.Spark;

public class TestCategoriesHandler {

  @BeforeAll
  public static void setup_before_everything() {
    //set up Spark port number
    Spark.port(0);
    //Remove the logging spam during tests
    Logger.getLogger("").setLevel(Level.WARNING);
  }
  @BeforeEach
  public void setup() throws IOException {
    HashMap<String, HashMap> songMapData = new HashMap<>();
    songMapData = GetCategories.createSongMap();
    Spark.get("/songs", new GetCategories(songMapData));
    Spark.init();
    Spark.awaitInitialization();
  }
  @AfterEach
  public void teardown() {
    // Gracefully stop Spark listening on both endpoints
    Spark.unmap("/songs");
    Spark.awaitStop(); // don't proceed until the server is stopped
  }
  static private HttpURLConnection tryRequest(String apiCall) throws IOException {
    // Configure the connection (but don't actually send the request yet)
    URL requestURL = new URL("http://localhost:"+Spark.port()+"/"+apiCall);
    HttpURLConnection clientConnection = (HttpURLConnection) requestURL.openConnection();
    // The default method is "GET",  which is what we're using here.
    // If we were using "POST",  we'd need to say so.
    // clientConnection.setRequestMethod("GET");
    clientConnection.connect();
    return clientConnection;
  }
  @Test
  public void testSuccessfulGettingCategories() throws Exception {
    HttpURLConnection clientConnection = tryRequest("songs?songs=wiggle");
    assertEquals(200,  clientConnection.getResponseCode());

    Moshi moshi = new Moshi.Builder().build();
    Map response = moshi.adapter(Map.class).fromJson
        (new Buffer().readFrom(clientConnection.getInputStream()));
    assertEquals("Success",  response.get("Result"));
    assertEquals("{artist=Jason Derulo, pos=40.0, year=2014.0, tag=[electronic/dance, pop]}",  response.get("categories").toString());
    clientConnection.disconnect();
    System.out.println(clientConnection.getResponseCode());

    //works with spaces in between the query words
    HttpURLConnection clientConnection2 = tryRequest("songs?songs=somebody%20that%20i%20used%20to%20know");
    assertEquals(200,  clientConnection.getResponseCode());

    Moshi moshi2 = new Moshi.Builder().build();
    Map response2 = moshi2.adapter(Map.class).fromJson
        (new Buffer().readFrom(clientConnection2.getInputStream()));
    assertEquals("Success",  response2.get("Result"));
    assertEquals("{artist=Gotye, pos=1.0, year=2012.0, tag=[alternative/indie, electronic/dance, pop, rock]}",  response2.get("categories").toString());
    clientConnection2.disconnect();
    System.out.println(clientConnection2.getResponseCode());

    //works with "_" in between the query words
    HttpURLConnection clientConnection3 = tryRequest("songs?songs=somebody_that_i_used_to_know");
    assertEquals(200,  clientConnection.getResponseCode());

    Moshi moshi3 = new Moshi.Builder().build();
    Map response3 = moshi3.adapter(Map.class).fromJson
        (new Buffer().readFrom(clientConnection3.getInputStream()));
    assertEquals("Success",  response3.get("Result"));
    assertEquals("{artist=Gotye, pos=1.0, year=2012.0, tag=[alternative/indie, electronic/dance, pop, rock]}",  response3.get("categories").toString());
    clientConnection3.disconnect();
    System.out.println(clientConnection3.getResponseCode());

    // works if inputted song is uppercase
    HttpURLConnection clientConnection4 = tryRequest("songs?songs=WIGGLE");
    assertEquals(200,  clientConnection.getResponseCode());
    Moshi moshi4 = new Moshi.Builder().build();
    Map response4 = moshi4.adapter(Map.class).fromJson
        (new Buffer().readFrom(clientConnection4.getInputStream()));
    assertEquals("Success",  response4.get("Result"));
    assertEquals("{artist=Jason Derulo, pos=40.0, year=2014.0, tag=[electronic/dance, pop]}",  response4.get("categories").toString());
    clientConnection4.disconnect();
    System.out.println(clientConnection4.getResponseCode());

    // works if inputted song is uppercase and has spaces in between query words
    HttpURLConnection clientConnection5 = tryRequest("songs?songs=SOMEBODY%20THAT%20I%20USED%20TO%20KNOW");
    assertEquals(200,  clientConnection.getResponseCode());
    Moshi moshi5 = new Moshi.Builder().build();
    Map response5 = moshi5.adapter(Map.class).fromJson
        (new Buffer().readFrom(clientConnection5.getInputStream()));
    assertEquals("Success",  response5.get("Result"));
    assertEquals("{artist=Gotye, pos=1.0, year=2012.0, tag=[alternative/indie, electronic/dance, pop, rock]}",  response5.get("categories").toString());
    clientConnection5.disconnect();
    System.out.println(clientConnection5.getResponseCode());

    // works if inputted songs is uppercase and has "_" in between
    HttpURLConnection clientConnection6 = tryRequest("songs?songs=SOMEBODY_THAT_I_USED_TO_KNOW");
    assertEquals(200,  clientConnection.getResponseCode());
    Moshi moshi6 = new Moshi.Builder().build();
    Map response6 = moshi6.adapter(Map.class).fromJson
        (new Buffer().readFrom(clientConnection6.getInputStream()));
    assertEquals("Success",  response6.get("Result"));
    assertEquals("{artist=Gotye, pos=1.0, year=2012.0, tag=[alternative/indie, electronic/dance, pop, rock]}",  response5.get("categories").toString());
    clientConnection6.disconnect();
    System.out.println(clientConnection6.getResponseCode());
  }
  @Test
  public void testGettingCategoriesErrorsWithNoEquals() throws Exception {
    // without "=" for the query
    HttpURLConnection clientConnection = tryRequest("songs?songs");
    assertEquals(200,  clientConnection.getResponseCode());
    Moshi moshi = new Moshi.Builder().build();
    Map mapResponse = moshi.adapter(Map.class).fromJson
        (new Buffer().readFrom(clientConnection.getInputStream()));
    assertEquals("{errorType=error_bad_request}",  mapResponse.get("Result").toString());
    clientConnection.disconnect();
  }
  @Test
  public void testGettingCategoriesErrorsWithSongNotInData() throws Exception {
    // testing for song name that is not in the data
    HttpURLConnection clientConnection = tryRequest("songs?songs=amk");
    assertEquals(200,  clientConnection.getResponseCode());
    Moshi moshi = new Moshi.Builder().build();
    Map mapResponse = moshi.adapter(Map.class).fromJson
        (new Buffer().readFrom(clientConnection.getInputStream()));
    assertEquals("{errorType=error_datasource}",  mapResponse.get("Result").toString());
    clientConnection.disconnect();
  }
  @Test
  public void testGettingCategoriesErrorsMoreThanOneQueryParam() throws Exception {
    // testing if query has more than one parameter
    HttpURLConnection clientConnection = tryRequest("songs?songs=wiggle&hello=bye");
    assertEquals(200,  clientConnection.getResponseCode());
    Moshi moshi = new Moshi.Builder().build();
    Map mapResponse = moshi.adapter(Map.class).fromJson
        (new Buffer().readFrom(clientConnection.getInputStream()));
    assertEquals("{errorType=error_bad_request}",  mapResponse.get("Result").toString());
    clientConnection.disconnect();
  }
  @Test
  public void testGettingCategoriesErrorsSongInputHasLength0() throws Exception {
    // testing if nothing is inputted for the song
    HttpURLConnection clientConnection = tryRequest("songs?songs=");
    assertEquals(200,  clientConnection.getResponseCode());
    Moshi moshi = new Moshi.Builder().build();
    Map mapResponse = moshi.adapter(Map.class).fromJson
        (new Buffer().readFrom(clientConnection.getInputStream()));
    assertEquals("{errorType=error_bad_request}",  mapResponse.get("Result").toString());
    clientConnection.disconnect();
  }

}
