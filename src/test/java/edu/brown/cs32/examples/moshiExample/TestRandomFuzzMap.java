package edu.brown.cs32.examples.moshiExample;

import edu.brown.cs32.examples.moshiExample.server.Handlers.geoJsonHandler;
import edu.brown.cs32.examples.moshiExample.server.Handlers.geoJsonHandler.FeatureCollection;
import edu.brown.cs32.examples.moshiExample.server.Handlers.geoJsonHandler.Features;
import edu.brown.cs32.examples.moshiExample.server.Handlers.geoJsonHandler.Geometry;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import org.junit.jupiter.api.Test;

/**
 * Test suite that randomly fuzz tests filter() function to ensure no terminations,
 * errors, or exceptions being thrown with random latitude and longitude values.
 */
public class TestRandomFuzzMap {

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
  public void testRandomFuzzTesting() {
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

      mapHandler.filter(minLat, minLon, maxLat, maxLon, features);
   //   HttpURLConnection clientConnection = tryRequest("Geomap?minLon="+ minLon + "&maxLon=" + maxLon + "&minLat=" + minLat + "&maxLat=" + maxLat );
    }

  }
}