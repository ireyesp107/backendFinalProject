package edu.brown.cs32.examples.moshiExample.server.Handlers;

import com.squareup.moshi.Moshi;
import edu.brown.cs32.examples.moshiExample.server.csvData;
import java.util.HashMap;
import spark.Request;
import spark.Response;
import spark.Route;

/**
 * class that handles getting content of csv
 */

public class getCSVHandler extends genericHandler implements Route{
  private csvData loader;


//  private edu.brown.cs32.examples.moshiExample.server.csvData csvData;

  /**
   * gets the content of the csv file inputted and converts it to json
   * @param data
   */

  public getCSVHandler(csvData data) {
    this.loader = data;
  }

  /**
   * handles getcsv request and sends response with the content of the CSV
   * @param request, string of the request
   * @param response
   * @return the serialized map that contains the content of the CSV
   */

  public Object handle(Request request, Response response) {
    hashMap.clear();
    System.out.println(hashMap);
    System.out.println(this.loader);
    // checks that a file was just loaded
    if (this.loader.loaded) {
      hashMap.put("Result", "Success");
      hashMap.put("Data", this.loader.getCSVData());
    } else {
      return DatasourceSerialize(hashMap);
      }
    // defensive copy
    HashMap<String, Object>  hashMapSuccessCopy = new HashMap(hashMap);
    return serialize(hashMapSuccessCopy);
  }





















  //dont think we need below

  public record BadRequestFailureResponse(String response_type) {
    public BadRequestFailureResponse() { this("error_bad_request"); }

    /**
     * @return this response, serialized as Json
     */
    String serialize() {
      Moshi moshi = new Moshi.Builder().build();
      return moshi.adapter(getCSVHandler.BadRequestFailureResponse.class).toJson(this);
    }
  }

}
