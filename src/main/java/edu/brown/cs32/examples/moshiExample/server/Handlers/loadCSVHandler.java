package edu.brown.cs32.examples.moshiExample.server.Handlers;

import edu.brown.cs32.examples.moshiExample.csv.FactoryFailureException;
import edu.brown.cs32.examples.moshiExample.csv.ListStringCreator;
import edu.brown.cs32.examples.moshiExample.csv.Parse;
import edu.brown.cs32.examples.moshiExample.server.csvData;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import spark.QueryParamsMap;
import spark.Request;
import spark.Response;
import spark.Route;

/**
 * class that handles loading of csv
 */
public class loadCSVHandler extends genericHandler implements Route {

  private csvData loader;

  /**
   * constructor to load the csv data
   * @param loader, csv data content
   */
  public loadCSVHandler(csvData loader) {
    this.loader = loader;
  }

  /**
   * handles loadcsv request and sends a response that indicates if the file was loaded and the filepath
   * @param request, a string of the request
   * @param response
   * @return Json of map that indicates if the file was loaded and the filepath
   * @throws IOException
   */
  public Object handle(Request request, Response response) throws IOException {
//      List<List<String>> csvContent = new ArrayList<>();
    hashMap.clear();
    System.out.println(hashMap);

    QueryParamsMap qpm = request.queryMap();
    String filepath = qpm.value("filepath");

    if ((!(qpm.hasKey("filepath"))) || (filepath.length()==0)) { //bad request
      return BadRequestSerialize(hashMap);
    }

    try {
      // parse file
        Parse csvParse = new Parse(new FileReader(filepath), new ListStringCreator(), false);
        this.loader.setCSVData(csvParse.parser()); // shared state? pop when we get to the gethandler?
//        hashMapSuccess.put("Result", "Success");
        hashMap.put("Filepath", filepath);
        hashMap.put("Result", "Success");
    } catch (FileNotFoundException e) {
      return DatasourceSerialize(hashMap);
    } catch (FactoryFailureException e) {
      return DatasourceSerialize(hashMap);
    }

    HashMap<String, Object> hashMapSuccessCopy = new HashMap(hashMap);
    return serialize(hashMapSuccessCopy);
  }

}







//
//  //dont think we need
//  /**
//   * Response object to send if someone requested soup before any recipes were loaded
//   */
//  public record BadJsonFailureResponse(String response_type) {
//    public BadJsonFailureResponse() { this("error_bad_json"); }
//
//    /**
//     * @return this response, serialized as Json
//     */
//    String serialize() {
//      Moshi moshi = new Moshi.Builder().build();
//      return moshi.adapter(BadJsonFailureResponse.class).toJson(this);
//    }
//  }
//
//  public record DataSourceFailureResponse(String response_type) {
//    public DataSourceFailureResponse() { this("error_data_source"); }
//
//    /**
//     * @return this response, serialized as Json
//     */
//    String serialize() {
//      Moshi moshi = new Moshi.Builder().build();
//      return moshi.adapter(DataSourceFailureResponse.class).toJson(this);
//    }
//  }
