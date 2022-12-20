package edu.brown.cs32.examples.moshiExample.server.Handlers;

import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.Moshi;
import com.squareup.moshi.Moshi.Builder;
import com.squareup.moshi.Types;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

/**
 * Facilitates adding a new data source by having common data for different handlers.
 */

public class genericHandler {


  public static HashMap<String, Object> hashMap;

  /**
   * Initializes the hashmap used in all handlers.
   */
  public genericHandler() {
    this.hashMap = new HashMap<>();

  }

  /**
   * Serializes a map into a json string.
   * @param map, the map of the different responses
   * @return the json of the map
   */

  String serialize(HashMap<String, Object> map) {
    Moshi moshi = new Builder().build();
    Type type = Types.newParameterizedType(Map.class, String.class, Object.class);
    JsonAdapter<Map> jsonAdapter = moshi.adapter(type);
    return jsonAdapter.toJson(map);
  }

  /**
   * Serializes bad json error
   * @param map that will contain the error
   * @return json of the error map
   */

  String BadJsonSerialize(HashMap<String, Object> map) {
    Map badjson = new HashMap();
    badjson.put("errorType", "error_bad_json");
    map.put("Result", badjson);
    Moshi moshi = new Builder().build();
    Type type = Types.newParameterizedType(Map.class, String.class, Object.class);
    JsonAdapter<Map> jsonAdapter = moshi.adapter(type);
    return jsonAdapter.toJson(map);
  }

  /**
   * Serializes datasource error
   * @param map that will contain the error
   * @return json of the error map
   */

  String DatasourceSerialize(HashMap<String, Object> map) {
    Map datasource = new HashMap();
    datasource.put("errorType", "error_datasource");
    map.put("Result", datasource);
    Moshi moshi = new Builder().build();
    Type type = Types.newParameterizedType(Map.class, String.class, Object.class);
    JsonAdapter<Map> jsonAdapter = moshi.adapter(type);
    return jsonAdapter.toJson(map);
  }

  /**
   * Serializes bad json error
   * @param map that will contain the error
   * @return json of the error map
   */

  String BadRequestSerialize(HashMap<String, Object> map) {
    Map badrequest = new HashMap();
    badrequest.put("errorType", "error_bad_request");
    map.put("Result", badrequest);
      Moshi moshi = new Builder().build();
      Type type = Types.newParameterizedType(Map.class, String.class, Object.class);
      JsonAdapter<Map> jsonAdapter = moshi.adapter(type);
      return jsonAdapter.toJson(map);
 }
}
