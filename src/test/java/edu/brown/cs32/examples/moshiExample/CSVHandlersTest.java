package edu.brown.cs32.examples.moshiExample;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.squareup.moshi.Moshi;
import edu.brown.cs32.examples.moshiExample.server.Handlers.getCSVHandler;
import edu.brown.cs32.examples.moshiExample.server.Handlers.loadCSVHandler;
import edu.brown.cs32.examples.moshiExample.server.csvData;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import okio.Buffer;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import spark.Spark;

public class CSVHandlersTest {
  public Map<String, Object> deserializeMap(HttpURLConnection clientConnection) throws IOException {
    Moshi moshi = new Moshi.Builder().build();
    return moshi.adapter(Map.class).fromJson(new Buffer().readFrom(clientConnection.getInputStream()));
  }


  @BeforeAll
  public static void setup_before_everything() {

    // Set the Spark port number. This can only be done once, and has to
    // happen before any route maps are added. Hence using @BeforeClass.
    // Setting port 0 will cause Spark to use an arbitrary available port.
    Spark.port(0);
    // Don't try to remember it. Spark won't actually give Spark.port() back
    // until route mapping has started. Just get the port number later. We're using
    // a random _free_ port to remove the chances that something is already using a
    // specific port on the system used for testing.

    // Remove the logging spam during tests
    //   This is surprisingly difficult. (Notes to self omitted to avoid complicating things.)

    // SLF4J doesn't let us change the logging level directly (which makes sense,
    //   given that different logging frameworks have different level labels etc.)
    // Changing the JDK *ROOT* logger's level (not global) will block messages
    //   (assuming using JDK, not Log4J)
    Logger.getLogger("").setLevel(Level.WARNING); // empty name = root logger
  }

  /**
   * Shared state for all tests. We need to be able to mutate it (adding recipes etc.) but never need to replace
   * the reference itself. We clear this state out after every test runs.
   */

  csvData csvContent = new csvData();



  @BeforeEach
  public void setup() {
    csvContent.clearcsv();
    // Re-initialize state, etc. for _every_ test method run
   // if(csvContent != null) {
   //   csvContent.clear();
  //  }


    // In fact, restart the entire Spark server for every test!
    Spark.get("/loadcsv", new loadCSVHandler(csvContent));
    Spark.get("/getcsv", new getCSVHandler(csvContent));
    Spark.init();
    Spark.awaitInitialization(); // don't continue until the server is listening
  }

  @AfterEach
  public void teardown() {
    // Gracefully stop Spark listening on both endpoints
    Spark.unmap("/loadcsv");
    Spark.unmap("/getcsv");
    Spark.unmap("weather");
    Spark.awaitStop(); // don't proceed until the server is stopped
  }



  static private HttpURLConnection tryRequest(String apiCall) throws IOException {
    // Configure the connection (but don't actually send the request yet)
    URL requestURL = new URL("http://localhost:"+Spark.port()+"/"+apiCall);
    HttpURLConnection clientConnection = (HttpURLConnection) requestURL.openConnection();

    // The default method is "GET", which is what we're using here.
    // If we were using "POST", we'd need to say so.
    //clientConnection.setRequestMethod("GET");

    clientConnection.connect();
    return clientConnection;
  }

/**
 * tests the loading of a filled file and success response and testing you can't getcsv twice in a row
 * without loaded twice
 */
  @Test
  public void testFullCsvAndNoGetTwiceWithoutLoaded() throws IOException{
    HttpURLConnection clientConnection = tryRequest("loadcsv?filepath=/Users/amaris/Desktop/"
        + "cs32/sprint-2-agrondin-estill/src/main/java/edu/brown/cs32/examples/moshiExample/stars/"
        + "fruits.csv");
    // Get an OK response (the *connection* worked, the *API* provides an error response)
    assertEquals(200, clientConnection.getResponseCode());
    Map<String, Object> loadResponse = this.deserializeMap(clientConnection);
    assertEquals("Success", loadResponse.get("Result"));
    assertEquals("/Users/amaris/Desktop/cs32/sprint-2-agrondin-estill/src/main/java/edu/"
        + "brown/cs32/examples/moshiExample/stars/fruits.csv", loadResponse.get("Filepath"));

    HttpURLConnection getConnection = tryRequest("getcsv");
    Map<String, Object> getResponse = this.deserializeMap(getConnection);
    assertEquals("Success", getResponse.get("Result"));
    assertEquals("[[Fruit, Taste, Age, Color, Shape], [banana, good, 8, yellow, curve], "
        + "[2, bad, 9, red, round], [3, ok, 7, orange, round], [4,  , 78 years old, 0, square], "
        + "[5, good food, , 0, square]]", getResponse.get("Data").toString());
    clientConnection.disconnect();

    HttpURLConnection getConnection1 = tryRequest("getcsv");
    Map<String, Object> getResponse2 = this.deserializeMap(getConnection1);
    assertEquals("error_datasource", getResponse2.get("Result"));
    clientConnection.disconnect();
  }

  /**
   * test get twice after loading twice
   */

  @Test
  public void testFullCsvAndNoGetTwiceWithLoaded() throws IOException {
    HttpURLConnection clientConnection = tryRequest("loadcsv?filepath=/Users/amaris/Desktop/"
        + "cs32/sprint-2-agrondin-estill/src/main/java/edu/brown/cs32/examples/moshiExample/stars/"
        + "fruits.csv");
    // Get an OK response (the *connection* worked, the *API* provides an error response)
    assertEquals(200, clientConnection.getResponseCode());
    Map<String, Object> loadResponse = this.deserializeMap(clientConnection);
    HttpURLConnection getConnection = tryRequest("getcsv");
    Map<String, Object> getResponse = this.deserializeMap(getConnection);
    assertEquals("Success", getResponse.get("Result"));
    assertEquals("[[Fruit, Taste, Age, Color, Shape], [banana, good, 8, yellow, curve], "
        + "[2, bad, 9, red, round], [3, ok, 7, orange, round], [4,  , 78 years old, 0, square], "
        + "[5, good food, , 0, square]]", getResponse.get("Data").toString());
    clientConnection.disconnect();


    clientConnection = tryRequest("loadcsv?filepath=/Users/amaris/Desktop/"
        + "cs32/sprint-2-agrondin-estill/src/main/java/edu/brown/cs32/examples/moshiExample/stars/"
        + "onlyHeaderRow.csv");
    // Get an OK response (the *connection* worked, the *API* provides an error response)
    assertEquals(200, clientConnection.getResponseCode());
    Map<String, Object> loadResponse1 = this.deserializeMap(clientConnection);
    assertEquals("Success", loadResponse1.get("Result"));
    assertEquals("/Users/amaris/Desktop/cs32/sprint-2-agrondin-estill/src/main/java/edu/"
        + "brown/cs32/examples/moshiExample/stars/onlyHeaderRow.csv", loadResponse1.get("Filepath"));
    HttpURLConnection getConnection1 = tryRequest("getcsv");
    Map<String, Object> getResponse1 = this.deserializeMap(getConnection1);
    assertEquals("Success", getResponse.get("Result"));
    assertEquals("[[color, shape, taste, good, bad]]", getResponse1.get("Data").toString());
    clientConnection.disconnect();



  }





    /**
     * test update load request
     */

  @Test
  public void testUpdateCsv() throws IOException {
    HttpURLConnection clientConnection = tryRequest("loadcsv?filepath=/Users/amaris/Desktop/"
        + "cs32/sprint-2-agrondin-estill/src/main/java/edu/brown/cs32/examples/moshiExample/stars/"
        + "fruits.csv");
    // Get an OK response (the *connection* worked, the *API* provides an error response)
    assertEquals(200, clientConnection.getResponseCode());
    Map<String, Object> loadResponse = this.deserializeMap(clientConnection);
    assertEquals("Success", loadResponse.get("Result"));
    assertEquals("/Users/amaris/Desktop/cs32/sprint-2-agrondin-estill/src/main/java/edu/"
        + "brown/cs32/examples/moshiExample/stars/fruits.csv", loadResponse.get("Filepath"));



    clientConnection = tryRequest("loadcsv?filepath=/Users/amaris/Desktop/"
        + "cs32/sprint-2-agrondin-estill/src/main/java/edu/brown/cs32/examples/moshiExample/stars/"
        + "onlyHeaderRow.csv");
    // Get an OK response (the *connection* worked, the *API* provides an error response)
    assertEquals(200, clientConnection.getResponseCode());
    Map<String, Object> loadResponse1 = this.deserializeMap(clientConnection);
    assertEquals("Success", loadResponse.get("Result"));
    assertEquals("/Users/amaris/Desktop/cs32/sprint-2-agrondin-estill/src/main/java/edu/"
        + "brown/cs32/examples/moshiExample/stars/onlyHeaderRow.csv", loadResponse1.get("Filepath"));
    clientConnection.disconnect();
  }


  /**
   * tests the loading of an empty file and success response
   */
  @Test
  public void testEmptyCsv() throws IOException{

    HttpURLConnection clientConnection = tryRequest("loadcsv?filepath=/Users/amaris/Desktop/"
        + "cs32/sprint-2-agrondin-estill/src/main/java/edu/brown/cs32/examples/moshiExample/stars/emptyfile.csv");
    // Get an OK response (the *connection* worked, the *API* provides an error response)
    assertEquals(200, clientConnection.getResponseCode());
    Map<String, Object> loadResponse = this.deserializeMap(clientConnection);
    assertEquals("Success", loadResponse.get("Result"));
    assertEquals("/Users/amaris/Desktop/cs32/sprint-2-agrondin-estill/src/main/java/edu/"
        + "brown/cs32/examples/moshiExample/stars/emptyfile.csv", loadResponse.get("Filepath"));

    HttpURLConnection getConnection = tryRequest("getcsv");
    Map<String, Object> getResponse = this.deserializeMap(getConnection);
    assertEquals("Success", getResponse.get("Result"));
    assertEquals("[]", getResponse.get("Data").toString());
    clientConnection.disconnect();




//
//    HttpURLConnection clientConnection = tryRequest("loadcsv?filepath=/Users/amaris/Desktop/"
//        + "cs32/sprint-2-agrondin-estill/src/main/java/edu/brown/cs32/examples/moshiExample/stars/emptyfile.csv");
//    // Get an OK response (the *connection* worked, the *API* provides an error response)
//    assertEquals(200, clientConnection.getResponseCode());
//    System.out.println(clientConnection.getResponseMessage());
//    Map<String, Object> loadResponse = this.deserializeMap(clientConnection);
//    assertEquals("Success", loadResponse.get("Result"));
//    assertEquals("/Users/amaris/Desktop/cs32/sprint-2-agrondin-estill/src/main/java/edu/"
//        + "brown/cs32/examples/moshiExample/stars/emptyfile.csv", loadResponse.get("Filepath"));
//
//    System.out.println(csvContent.loaded);
//
//    HttpURLConnection getConnection = tryRequest("getcsv");
//    Map<String, Object> getResponse = this.deserializeMap(getConnection);
//    assertEquals("Success", getResponse.get("Result"));
//    assertEquals("[]", getResponse.get("Data").toString());
//    clientConnection.disconnect();
  }

  /**
   * tests datasource error in load
   */
  @Test
  public void testLoadErrors() throws IOException {

    HttpURLConnection clientConnection = tryRequest("loadcsv?filepath=nope");
    // Get an OK response (the *connection* worked, the *API* provides an error response)
    assertEquals(200, clientConnection.getResponseCode());
    Map<String, Object> loadResponse = this.deserializeMap(clientConnection);
    assertEquals("error_datasource", loadResponse.get("Result"));
    clientConnection.disconnect();

    HttpURLConnection clientConnection1 = tryRequest("loadcsv?filepath=");
    // Get an OK response (the *connection* worked, the *API* provides an error response)
    assertEquals(200, clientConnection1.getResponseCode());
    Map<String, Object> loadResponse2 = this.deserializeMap(clientConnection1);
    assertEquals("error_bad_request", loadResponse2.get("Result"));
    clientConnection.disconnect();

    HttpURLConnection clientConnection3 = tryRequest("loadcsv?");
    // Get an OK response (the *connection* worked, the *API* provides an error response)
    assertEquals(200, clientConnection3.getResponseCode());
    Map<String, Object> loadResponse4 = this.deserializeMap(clientConnection3);
    assertEquals("error_bad_request", loadResponse4.get("Result"));
    clientConnection.disconnect();
  }

  /**
   * tests errors in get
   */
  @Test
  public void testGetErrors() throws IOException {
    HttpURLConnection clientConnection = tryRequest("loadcsv?filepath=");
    // Get an OK response (the *connection* worked, the *API* provides an error response)
    assertEquals(200, clientConnection.getResponseCode());
    Map<String, Object> loadResponse = this.deserializeMap(clientConnection);

    HttpURLConnection clientConnection1 = tryRequest("getcsv");
    // Get an OK response (the *connection* worked, the *API* provides an error response)
    assertEquals(200, clientConnection1.getResponseCode());
    Map<String, Object> loadResponse2 = this.deserializeMap(clientConnection1);
    assertEquals("error_datasource", loadResponse2.get("Result"));
  }

  @Test
  public void testMockData() throws IOException{
    List<String> listInArray = Arrays.asList("hello", "bye");
    List<List<String>> mockData = new ArrayList<>();
    mockData.add(listInArray);
    this.csvContent.loaded = true;
    this.csvContent.setCSVData(mockData);
    HttpURLConnection clientConnection = tryRequest("getcsv");
    Map<String, Object> loadResponse = this.deserializeMap(clientConnection);
    assertEquals("Success", loadResponse.get("Result"));
    assertEquals("[[hello, bye]]", loadResponse.get("Data").toString());

  }





}
