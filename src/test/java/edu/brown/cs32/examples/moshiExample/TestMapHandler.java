package edu.brown.cs32.examples.moshiExample;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.squareup.moshi.Moshi;
import edu.brown.cs32.examples.moshiExample.server.Handlers.geoJsonHandler;
import edu.brown.cs32.examples.moshiExample.server.Handlers.geoJsonHandler.FeatureCollection;
import edu.brown.cs32.examples.moshiExample.server.Handlers.geoJsonHandler.Features;
import edu.brown.cs32.examples.moshiExample.server.Handlers.geoJsonHandler.Geometry;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import okio.Buffer;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import spark.Spark;

public class TestMapHandler {
  @BeforeAll
  public static void setup_before_everything() {
    //set up Spark port number
    Spark.port(0);
    //Remove the logging spam during tests
    Logger.getLogger("").setLevel(Level.WARNING);
  }


  @BeforeEach
  public void setup() {
    // Re-initialize state,  etc. for _every_ test method run
    // In fact,  restart the entire Spark server for every test!
    Spark.get("json",  new geoJsonHandler());
    Spark.init();
    Spark.awaitInitialization(); // don't continue until the server is listening
  }

  @AfterEach
  public void teardown() {
    // Gracefully stop Spark listening on both endpoints
    Spark.unmap("json");
    Spark.awaitStop(); // don't proceed until the server is stopped
  }

  /**
   * starts a connection to a specific API endpoint/params
   * @param apiCall the call string including endpoint and request params
   * @return the connection for the given URL
   * @throws IOException if the connection fails
   */
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

  /**
   * successful map response
   * no filtering
   */
  @Test
  public void testMapSuccessResponseNoFiltering() throws Exception {
    HttpURLConnection clientConnection = tryRequest("json?minLat=0&maxLat=1&minLon=0&maxLon=1");
    assertEquals(200,  clientConnection.getResponseCode());

    Moshi moshi = new Moshi.Builder().build();
    Map response = moshi.adapter(Map.class).fromJson
        (new Buffer().readFrom(clientConnection.getInputStream()));
    assertEquals("success",  response.get("result"));
    assertEquals("{type=FeatureCollection, features=[]}",  response.get("data").toString());
    clientConnection.disconnect();
    System.out.println(clientConnection.getResponseCode());
  }

  /**
   * successful map response
   * no param
   */
  @Test
  public void testMapSuccessResponseNoParam() throws Exception {
    HttpURLConnection clientConnection = tryRequest("json");
    assertEquals(200,  clientConnection.getResponseCode());

    Moshi moshi = new Moshi.Builder().build();
    Map response = moshi.adapter(Map.class).fromJson
        (new Buffer().readFrom(clientConnection.getInputStream()));
    assertEquals("Success",  response.get("Result"));
    clientConnection.disconnect();
  }

  /**
   * successful map response
   * all filtering
   */
  @Test
  public void testMapSuccessResponse() throws Exception {
    HttpURLConnection clientConnection = tryRequest("json?minLat=33.0000&maxLat=34.0000&minLon=-87.67&maxLon=-86.9");
    assertEquals(200,  clientConnection.getResponseCode());
    Moshi moshi = new Moshi.Builder().build();
    Map mapResponse = moshi.adapter(Map.class).fromJson
        (new Buffer().readFrom(clientConnection.getInputStream()));
    assertEquals("success",  mapResponse.get("result"));
    assertEquals("{type=FeatureCollection, features=[{type=Feature, geometry={type=MultiPolygon, coordinates=[[[[-86.90933, 33.4975], [-86.90919, 33.494144], [-86.90263, 33.49417], [-86.90229, 33.49389], [-86.902145, 33.493652], [-86.90224, 33.493073], [-86.903305, 33.492744], [-86.903915, 33.49233], [-86.90456, 33.491974], [-86.90488, 33.49108], [-86.90584, 33.491405], [-86.90776, 33.490276], [-86.90855, 33.48989], [-86.90983, 33.48971], [-86.911224, 33.489563], [-86.91472, 33.488316], [-86.914825, 33.49105], [-86.91129, 33.495274], [-86.91097, 33.495777], [-86.90933, 33.4975]]], [[[-86.89308, 33.50963], [-86.89626, 33.505768], [-86.89379, 33.504215], [-86.89509, 33.50252], [-86.896034, 33.50309], [-86.89667, 33.502186], [-86.898735, 33.50339], [-86.899994, 33.501728], [-86.898415, 33.50087], [-86.89444, 33.500854], [-86.89657, 33.499207], [-86.89807, 33.498554], [-86.899254, 33.497826], [-86.90034, 33.49684], [-86.90109, 33.49551], [-86.90202, 33.49662], [-86.902596, 33.497192], [-86.90305, 33.497353], [-86.9036, 33.497353], [-86.9069, 33.499317], [-86.89975, 33.508892], [-86.89919, 33.50856], [-86.89674, 33.511745], [-86.89308, 33.50963]]]]}, properties={state=AL, city=Birmingham, name=Better residential section of Ensley and residential area around school in Fairfield (outside of city limits), holc_id=C16, holc_grade=C, neighborhood_id=224.0, area_description_data={5=Both sales and rental prices in 1929 were off from 15% to 20% of the 1926-28 peak. Location of property within area justifies policy of selling rather than holding for possible price increase. Ensley portion of this area was one of the most stable sections in Ensley during the depression from rental standpoint, and in bad times property in this Ensley portion of the area rented better than any other sections in the western postion of the city. Due to industry influence, property in this areas hould be sold when plants are operating at near capacity., 6=16 C Better residential section of Ensley and residential area around school inFairfield (outside of city limits), 31=95, 32=5, 33=N/A, 4b=Limited on conservative basis, 3m=40-60 40-60 N/A, 3k=Good to fair Good to fair N/A, 3c=1 15-20 15-20, 3f=100 40 40, 2f=A few, 3l=N/A Fair Fair, 1c=Proximity to steel plants. Age of property in areas. Smoke and dirt from nearby industrial plants. , 3e=100 98 98, 2b=900-4000, 3j=1938 58 3000-4500 58 N/A N/A 3000-4500, 3g=None None 2 ($3750), 1d=75, 1e=Slowly downward, 3h=5500-7500 5500-7500 N/A, 2c=1 Italians and Greeks, 2d=None N/A, 3q=Good Good N/A, 2a=Business men, clerical and steel workings, 3p=Good N/A Good, 1a=Level, 3b=Frame Frame Frame, 2g=N/A Yes N/A, 3i=1936 3000-4500 58 3000-4500 N/A 58 N/A, 3d=Fair to poor Fair to poor Good, 4a=Limited on conservative basis, 3o=N/A 1938 25-35 67 25-35 61 N/A, 2e=Italians (very slowly), 3n=N/A 57 22.50-30 22.50-30 53 1936 N/A, 1b=Close to occupants source of employment. Near churches, schools, parks, and community business centers. Zoned for residential, 3a=1 story singles 1 story singles 2 story singles}}}, {type=Feature, geometry={type=MultiPolygon, coordinates=[[[[-86.91843, 33.475086], [-86.91843, 33.470535], [-86.92959, 33.470833], [-86.93027, 33.47467], [-86.91843, 33.48493], [-86.91843, 33.475086]]]]}, properties={state=AL, city=Birmingham, name=Interurban Heights & Forest Hills (outside of both Fairfield and Birmingham city limits), holc_id=C17, holc_grade=C, neighborhood_id=235.0, area_description_data={5=This is low grade C area and is almost a D grace area., 6=Interurban Heights & Forest Hills (outside of both Fairfield and Birmingham city limits) 17 C, 31=94, 32=5, 33=1, 1c=Near industrial plants. Negro property in area. Instability of industrial workers income., 4b=Practically none, 4a=Practically none, 3q=Fair Fair Fair, 3p=Fair Fair Fair, 3o=1938 N/A 20-27.50 N/A 17.50-35 6-12.50 61, 3n=N/a No rentals 1936 61 N/A 6-12.50 N/A, 3m=No rentals 10-20 N/A, 3l=None None None, 3k=Poor Poor None, 3j=500-1500 N/A 1938 No sales N/A Owned by the T. C. I. Co 59, 3i=1936 N/A No sales N/A Owned by the T. C. I. Co 59 500-1500, 3h=No sales 1000-2250 Owned by the T. C. I. Co, 3g=None None 10 ($2000-3000), 3f=0 15 25, 3e=50 90 90, 3d=Fair to dilapidated Fair to dilapidated Good, 3c=1 1-30 20, 3b=Frame Frame Brick veneer, 3a=1 story singles 1 story singles (all negro) 2 story row house, 2g=Yes N/A N/A, 2f=Many, 2e=None, 2d=Yes 90, 2c=None N/A, 2b=250-1500, 2a=Steel workers, miners and laborers, 1e=Slowly downward, 1d=25, 1a=Rolling, 1b=Close to occupants' source of employment}}}, {type=Feature, geometry={type=MultiPolygon, coordinates=[[[[-86.92139, 33.460243], [-86.91112, 33.4653], [-86.90705, 33.460663], [-86.91985, 33.45605], [-86.92139, 33.460243]]]]}, properties={state=AL, city=Birmingham, name=Midfield (outside of city limits of both Fairfield and Birmingham), holc_id=C18, holc_grade=C, neighborhood_id=234.0, area_description_data={5=About 20 houses in this area., 6=C 18 Midfield (outside of city limits of both Fairfield and Birmingham), 31=100, 32=N/A, 33=N/A, 3f=50 N/A N/A, 1d=2, 2c=None N/A, 3k=N/A Poor N/A, 3b=N/A N/A Frame, 3o=1938 N/A N/A N/A N/A N/A 15-20, 3c=12 N/A N/A, 3d=N/A N/A Fair, 3j=N/A N/A N/A N/A N/A 1850-2250 1938, 1e=Slowly downward, 3q=N/A Fair N/A, 3n=N/A N/A N/A 1936 15-20 N/A N/A, 3m=N/A N/A N/A, 3h=N/A N/A N/A, 1c=Distance from everything (schools, churches, transportation, stores, etc.) Unrestricted., 4a=Practically none, 2d=N/A None, 3a=N/A 1 story singles N/A, 2b=300-1200, 2f=A few, 3g=N/A N/A None, 1b=Traversed by Bessemer super-highway., 4b=Practically none, 2a=Industrial workers and miners, 1a=Mostly level, 3i=N/A N/A 1850-2250 N/A N/A N/A 1936, 2g=N/A Yes N/a, 3e=90 N/A N/A, 2e=None, 3l=N/A None N/A, 3p=Fair to poor N/A N/A}}}, {type=Feature, geometry={type=MultiPolygon, coordinates=[[[[-86.90062, 33.523266], [-86.916214, 33.513042], [-86.92377, 33.514153], [-86.90671, 33.535034], [-86.90062, 33.523266]]], [[[-86.91934, 33.525566], [-86.93232, 33.514668], [-86.94663, 33.52073], [-86.92842, 33.537174], [-86.91934, 33.525566]]]]}, properties={state=AL, city=Birmingham, name=Sherman Heights, West Ensley, Katherwood Park, Gary-Ensley and Oakridge Addition (all outside of city limits), holc_id=D20, holc_grade=D, neighborhood_id=237.0, area_description_data={5=About 200 houses in this area, 6=20 Sherman Heights, West Ensley, Katherwood Park, Gary-Ensley and Oakridge Addition (all outside of city limits) D, 31=100, 32=N/A, 33=N/A, 3e=N/A N/A N/A, 3b=N/A N/A N/A, 3j=N/A 750-2500 1938 N/A N/A N/A N/A, 3k=N/A N/A N/A, 2a=Miners and Industrial Workers, 3l=N/A N/A N/A, 1d=Less than 0.5, 2e=None, 3a=1 story singles N/A N/A, 3m=N/A N/A N/A, 3n=N/A N/A N/A N/A N/A N/A N/A, 3d=N/A N/A N/A, 3o=8-18 1938 N/A N/A N/A N/A N/A, 3f=N/A N/A N/A, 2f=Many, 1e=Downward, 3p=N/A N/A N/A, 2g=N/A Yes N/A, 3q=N/A N/A N/A, 2d=Yes 75, 4a=N/A, 1a=N/A, 3c=N/A N/A N/A, 1b=N/A, 3g=N/A N/A N/A, 2c=Italians 5, 3h=N/A N/A N/A, 2b=300-900, 1c=N/A, 4b=N/A, 3i=N/A N/A N/A N/A N/A N/A N/A}}}]}",  mapResponse.get("data").toString());
    clientConnection.disconnect();
  }

  /**
   * unsuccessful map response
   * max lat/long smaller than min lat/long
   */
  @Test
  public void testMapFailureResponseOppLatLon() throws Exception {
    HttpURLConnection clientConnection = tryRequest("json?minLat=1&maxLat=0&minLon=1&maxLon=0");
    assertEquals(200,  clientConnection.getResponseCode());
    Moshi moshi = new Moshi.Builder().build();
    Map mapResponse = moshi.adapter(Map.class).fromJson
        (new Buffer().readFrom(clientConnection.getInputStream()));
    assertEquals("error_bad_request",  mapResponse.get("Result"));
    clientConnection.disconnect();
  }

  /**
   * unsuccessful map response
   * missing params
   */
  @Test
  public void testMapFailureResponseMissingParameters() throws Exception {
    HttpURLConnection clientConnection = tryRequest("json?minLat=0&maxLat=0&minLon=1");
    assertEquals(200,  clientConnection.getResponseCode());
    Moshi moshi = new Moshi.Builder().build();
    Map mapResponse = moshi.adapter(Map.class).fromJson
        (new Buffer().readFrom(clientConnection.getInputStream()));
    assertEquals("error_bad_request",  mapResponse.get("Result"));
    clientConnection.disconnect();
  }

  /**
   * unsuccessful map response
   * not number coords
   */
  @Test
  public void testMapFailureResponseNotNumber() throws Exception {
    HttpURLConnection clientConnection = tryRequest("json?minLat=bdfasdf&maxLat=basdfasdf&minLon=c&maxLon=dadsfasdf");
    assertEquals(200,  clientConnection.getResponseCode());
    Moshi moshi = new Moshi.Builder().build();
    Map mapResponse = moshi.adapter(Map.class).fromJson
        (new Buffer().readFrom(clientConnection.getInputStream()));
    assertEquals("error_bad_request",  mapResponse.get("Result"));
    clientConnection.disconnect();
  }

  /**
   * unsuccessful map response
   * no numbers after keys
   */
  @Test
  public void testMapFailureResponseNoNumberAfterKey() throws Exception {
    HttpURLConnection clientConnection = tryRequest("json?minLat=&maxLat=43&minLon=39&maxLon=");
    assertEquals(200,  clientConnection.getResponseCode());
    Moshi moshi = new Moshi.Builder().build();
    Map mapResponse = moshi.adapter(Map.class).fromJson
        (new Buffer().readFrom(clientConnection.getInputStream()));
    assertEquals("error_bad_request",  mapResponse.get("Result"));
    clientConnection.disconnect();
  }

  /**
   * unsuccessful map response
   * out of range coords
   */
  @Test
  public void testMapFailureResponseOutofRange() throws Exception {
    HttpURLConnection clientConnection = tryRequest("json?minLat=1123412341231&maxLat=1235563&minLon=14565623&maxLon=34563456");
    assertEquals(200,  clientConnection.getResponseCode());
    Moshi moshi = new Moshi.Builder().build();
    Map mapResponse = moshi.adapter(Map.class).fromJson
        (new Buffer().readFrom(clientConnection.getInputStream()));
    assertEquals("error_bad_request",  mapResponse.get("Result"));
    clientConnection.disconnect();
  }

  /**
   * wrong first
   */
  @Test
  public void testMapWrongField() throws Exception {
    HttpURLConnection clientConnection = tryRequest("json?minLat=33.0000&maxLat=34.0000&minLon=-87.67&banana=-86.9");
    assertEquals(200,  clientConnection.getResponseCode());
    Moshi moshi = new Moshi.Builder().build();
    Map mapResponse = moshi.adapter(Map.class).fromJson
        (new Buffer().readFrom(clientConnection.getInputStream()));
    assertEquals("error_bad_request",  mapResponse.get("Result"));
    clientConnection.disconnect();
  }

  /**
   * Test suite that randomly fuzz tests filter() function to ensure no terminations,
   * errors, or exceptions being thrown with random latitude and longitude values.
   */


    /**
     * Helper function -> returns a random number between provided ranges.
     * @param min - the minimum (inclusive)
     * @param max - the maximum (exclusive)
     * @return - random number between range
     */
    private int RandomValue(int min, int max) {
      Random random = new Random();
      return random.nextInt(max - min) + min;
    }
    /**
     * Random fuzz test that tests if no terminations or errors are thrown by filtering
     * random latitude and longitude values
     */
    @Test
    public void testRandomFuzzTesting() throws IOException {
      // preparing the mock
      List<Float> pair1 = List.of((float) -77.7,(float)29.49);
      List<Float> pair2 = List.of((float)-74.7,(float) 39.4);
      List<Float> pair3 = List.of((float)-74, (float)32.4);
      List<Float> pair4 = List.of( (float)-98.7,(float) 33.4);
      Map<String, Object> properties = new HashMap<>();
      properties.put("state", "CA");
      properties.put("city", "San Diego");
      properties.put("name", " Club");
      properties.put("holc_id", "A1");
      properties.put("holc_grade", "A");

      Geometry geo = new Geometry("polygon", List.of(List.of(List.of(pair1, pair2, pair3, pair4))));
      Features feature = new Features("Feature", geo, properties);
      FeatureCollection features = new FeatureCollection("FeatureCollection", List.of(feature));
      geoJsonHandler mapHandler = new geoJsonHandler();

      int NUM_ITERATIONS = 10000;
      for(int i = 0; i < NUM_ITERATIONS; i++) {
        int minLat = RandomValue(-90,91);
        int minLon = RandomValue(-180,181);
        int maxLat = RandomValue(-90,91);
        int maxLon = RandomValue(-180,181);

    //    mapHandler.filter(minLat, minLon, maxLat, maxLon, features);
        HttpURLConnection clientConnection = tryRequest("Geomap?minLon="+ minLon + "&maxLon=" + maxLon + "&minLat=" + minLat + "&maxLat=" + maxLat );
      }

    }
  }
