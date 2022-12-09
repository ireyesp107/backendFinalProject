package edu.brown.cs32.examples.moshiExample.server.Handlers;

import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.Moshi;
import com.squareup.moshi.Moshi.Builder;
import com.squareup.moshi.Types;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

/**
 * facilitates adding a new data source by having common data for different handlers
 */

public class genericHandler {


  public static HashMap<String, Object> hashMap;

  /**
   * initializes the hashmap used in all handlers
   */
  public genericHandler() {
    this.hashMap = new HashMap<>();

  }

  /**
   * serializes a map into a json string
   * @param map, the map of the different responses
   * @return the json of the map
   */

  String serialize(HashMap<String, Object> map) {
    Moshi moshi = new Builder().build();
    Type type = Types.newParameterizedType(Map.class, String.class, Object.class);
    JsonAdapter<Map> jsonAdapter = moshi.adapter(type);
    return jsonAdapter.toJson(map);
  }

  String BadJsonSerialize(HashMap<String, Object> map) {
    map.put("Result", "error_bad_json");
    Moshi moshi = new Builder().build();
    Type type = Types.newParameterizedType(Map.class, String.class, Object.class);
    JsonAdapter<Map> jsonAdapter = moshi.adapter(type);
    return jsonAdapter.toJson(map);
  }

  String DatasourceSerialize(HashMap<String, Object> map) {
    map.put("Result", "error_datasource");
    Moshi moshi = new Builder().build();
    Type type = Types.newParameterizedType(Map.class, String.class, Object.class);
    JsonAdapter<Map> jsonAdapter = moshi.adapter(type);
    return jsonAdapter.toJson(map);
  }


  String BadRequestSerialize(HashMap<String, Object> map) {
      map.put("Result", "error_bad_request");
      Moshi moshi = new Builder().build();
      Type type = Types.newParameterizedType(Map.class, String.class, Object.class);
      JsonAdapter<Map> jsonAdapter = moshi.adapter(type);
      return jsonAdapter.toJson(map);
 }
}