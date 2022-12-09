//package edu.brown.cs32.examples.moshiExample.server.Handlers;
//
//import com.squareup.moshi.Moshi;
//import java.io.IOException;
//import java.nio.file.Files;
//import java.nio.file.Paths;
//import java.util.ArrayList;
//import java.util.HashMap;
//import java.util.HashSet;
//import java.util.List;
//import java.util.Map;
//import spark.QueryParamsMap;
//import spark.Request;
//import spark.Response;
//import spark.Route;
//
//
//public class geoJsonHandler extends genericHandler implements Route {
//  float minLat = 0;
//  float maxLat = 0;
//  float minLon = 0;
//  float maxLon = 0;
//
//  /**
//   Serializing the json file.
//   */
//
//  private FeatureCollection dataContent;
//
//  public FeatureCollection geoJsonHandler(String path) throws IOException {
//    String data = new String(Files.readAllBytes(Paths.get(path)));
//    Moshi moshi = new Moshi.Builder().build();
//    this.dataContent = moshi.adapter(FeatureCollection.class).fromJson(data);
//    return dataContent;
//
//  }
//
//
//  /**
//   Handles the request and response.
//   */
//  public Object handle(Request request, Response response) throws IOException {
//    hashMap.clear();
//    QueryParamsMap qpm = request.queryMap();
//
//// if there are no params, get the whole data
//    if (request.queryParams().equals(new HashSet<>())) {
//      FeatureCollection jsonData = geoJsonHandler("/Users/amaris/Desktop/cs32/sprint-2-agrondin-estill/src/main/java/edu/brown/cs32/examples/moshiExample/stars/fullDownload.geojson");
//      hashMap.put("Result", "Success");
//      hashMap.put("Data", jsonData);
//      HashMap<String, Object> hashMapSuccessCopy = new HashMap(hashMap);
//      return serialize(hashMapSuccessCopy);
//    }
//
//// if there is the request is valid, filter for given bounds
//    try {
//    if ((request.queryParams().size() == 4) && (qpm.hasKey("minLat") && qpm.hasKey("maxLat") && qpm.hasKey("minLon") && qpm.hasKey(
//     "maxLon"))) {
//   this.minLat = Float.parseFloat(request.queryParams("minLat"));
//   this.maxLat = Float.parseFloat(request.queryParams("maxLat"));
//   this.minLon = Float.parseFloat(request.queryParams("minLon"));
//   this.maxLon = Float.parseFloat(request.queryParams("maxLon"));
//   System.out.println(maxLon);
//   if ((this.minLat < -90 || this.minLat > 90) || (this.maxLat < -90 || this.maxLat > 90) ||
//       (this.minLon < -180 || this.minLon > 180) || (this.maxLon < -180 || this.maxLon > 180) ||
//       (this.minLat > this.maxLat) || (this.minLon > this.maxLon)) {
//     return BadRequestSerialize(hashMap);
//   } else {
//     this.dataContent = geoJsonHandler("/Users/amaris/Desktop/cs32/sprint-2-agrondin-estill/src/main/java/edu/brown/cs32/examples/moshiExample/stars/fullDownload.geojson");
//     List<Features> filtered = filter(minLat, maxLat, minLon, maxLon);
//     //success response
//     hashMap.put("result","success");
//     hashMap.put("data", new FeatureCollection(this.dataContent.type(), filtered));
//     HashMap<String, Object> hashMapSuccessCopy = new HashMap(hashMap);
//     return serialize(hashMapSuccessCopy);
////     System.out.println("4");
////     System.out.println("5");
////     List<Features> filtered = filter(minLat, maxLat, minLon, maxLon);
////     System.out.println("7");
////     hashMap.put("Result", "Success");
////     hashMap.put("Data", new FeatureCollection(this.dataContent.type(), filtered));
////     System.out.println(hashMap);
////     HashMap<String, Object> hashMapSuccessCopy = new HashMap(hashMap);
////     return serialize(hashMapSuccessCopy);
//   }
//   } else {
//   return BadRequestSerialize(hashMap); }
//
//  } catch (NullPointerException | NumberFormatException e) {
//    return BadRequestSerialize(hashMap);
//  }
//
//  }
//
//
//  /**
//   adds the features in the list of properties that are within the bounds inputted
//   */
//  public List<Features> filter(float minLat, float maxLat, float minLon, float maxLon){
//    List<Features> filtered = new ArrayList<>();
//    big : for (Features features: this.dataContent.features()) {
//      if (features.geometry == null)
//        continue;
//      // check if geo is non existent
//      List<List<Float>> boundBox = features.geometry().coordinates().get(0).get(0);
//      for (List<Float> coordinatePair : boundBox) {
//        float lon = coordinatePair.get(0);
//        float lat = coordinatePair.get(1);
//        if (lat < minLat || lat > maxLat || lon < minLon || lon > maxLon) {
//          continue big;
//        }
//      }
//      filtered.add(features);
//    }
//    System.out.println("yellow");
//    return filtered;
//  }
//
//
//  public record FeatureCollection(String type, List<Features> features) {}
//  public record Features(String type, Geometry geometry, Map<String, Object> properties) {}
//  public record Geometry(String type, List<List<List<List<Float>>>> coordinates) {}
//
//
//}
//

package edu.brown.cs32.examples.moshiExample.server.Handlers;

import com.squareup.moshi.Moshi;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import spark.QueryParamsMap;
import spark.Request;
import spark.Response;
import spark.Route;



public class geoJsonHandler extends genericHandler implements Route {
  float minLat = 0;
  float maxLat = 0;
  float minLon = 0;
  float maxLon = 0;

  /**
   Serializing the json file.
   */

  private FeatureCollection dataContent;

  public FeatureCollection geoJsonHandler(String path) throws IOException {
    String data = new String(Files.readAllBytes(Paths.get(path)));
    Moshi moshi = new Moshi.Builder().build();
    this.dataContent = moshi.adapter(FeatureCollection.class).fromJson(data);
    return dataContent;
  }

  /**
   Handles the request and response.
   */
  public Object handle(Request request, Response response) throws IOException {
    hashMap.clear();
    QueryParamsMap qpm = request.queryMap();

// if there are no params, get the whole data
    if (request.queryParams().equals(new HashSet<>())) {
      FeatureCollection jsonData = geoJsonHandler("/Users/amaris/Desktop/cs32/sprint-2-agrondin-estill/src/main/java/edu/brown/cs32/examples/moshiExample/stars/fullDownload.geojson");
      hashMap.put("Result", "Success");
      hashMap.put("Data", jsonData.toString());
      HashMap<String, Object> hashMapSuccessCopy = new HashMap(hashMap);
      return serialize(hashMapSuccessCopy);
    }

// if there is the request is valid, filter for given bounds
    try {
      if ((request.queryParams().size() == 4) && (qpm.hasKey("minLat") && qpm.hasKey("maxLat") && qpm.hasKey("minLon") && qpm.hasKey(
          "maxLon"))) {
        this.minLat = Float.parseFloat(request.queryParams("minLat"));
        this.maxLat = Float.parseFloat(request.queryParams("maxLat"));
        this.minLon = Float.parseFloat(request.queryParams("minLon"));
        this.maxLon = Float.parseFloat(request.queryParams("maxLon"));
        System.out.println(maxLon);
        if (this.minLat > this.maxLat || this.minLon > this.maxLon) { // min max
          return BadRequestSerialize(hashMap);
        }
        if ((this.minLat < -90 || this.minLat > 90) || (this.maxLat < -90 || this.maxLat > 90) ||
            (this.minLon < -180 || this.minLon > 180) || (this.maxLon < -180 || this.maxLon > 180)) {
          return BadRequestSerialize(hashMap);
        } else {
          FeatureCollection json = geoJsonHandler("/Users/william/Desktop/integration-agrondin-ctulpar-wpark14/src/fullDownload.geojson");
          this.dataContent = json;
          List<Features> filtered = filter(minLat, maxLat, minLon, maxLon, json);
          //success response
          hashMap.put("result","success");
          hashMap.put("type", new FeatureCollection(this.dataContent.type(), filtered));
          hashMap.put("features", filtered);
          System.out.println(hashMap);
          HashMap<String, Object> hashMapSuccessCopy = new HashMap(hashMap);
          return serialize(hashMapSuccessCopy);
        }
      } else {
        return BadRequestSerialize(hashMap); }

    } catch (NullPointerException | NumberFormatException e) {
      return BadRequestSerialize(hashMap);
    }
  }


  /**
   adds the features in the list of properties that are within the bounds inputted
   */
  public List<Features> filter(float minLat, float maxLat, float minLon, float maxLon, FeatureCollection json){
    List<Features> filtered = new ArrayList<>();
    big : for (Features features: json.features()) {
      if (features.geometry() == null)
        continue;
      // check if geo is non existent
      List<List<Float>> boundBox = features.geometry().coordinates().get(0).get(0);
      for (List<Float> coordinatePair : boundBox) {
        float lon = coordinatePair.get(0);
        float lat = coordinatePair.get(1);
        if (lat < minLat || lat > maxLat || lon < minLon || lon > maxLon) {
          continue big;
        }
      }
      filtered.add(features);
    }
    return filtered;
  }


  public record FeatureCollection(String type, List<Features> features) {}
  public record Features(String type, Geometry geometry, Map<String, Object> properties) {}
  public record Geometry(String type, List<List<List<List<Float>>>> coordinates) {}


}
