package edu.brown.cs32.examples.moshiExample.server.Handlers;

import edu.brown.cs32.examples.moshiExample.server.csvData;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.HashMap;
import spark.Request;
import spark.Response;
import spark.Route;

public class statsHandler extends genericHandler implements Route {
  private int rows;
  private int cols;

  private csvData loader;

  public statsHandler(csvData data) {
    this.rows = 0;
    this.cols = 0;
    this.loader = data;
  }

  public Object handle(Request request, Response response) throws IOException, URISyntaxException {
    hashMap.clear();

    if (this.loader.got) {
      System.out.println("hello");
      if (this.loader.csvContent.size() == 0) {
        rows = 0;
        cols =0;
      } else {
        rows = this.loader.csvContent.size();
        cols = this.loader.csvContent.get(0).size();
      }
      hashMap.put("Stats" , "Rows: " + rows + ", " + "Cols: " + cols);
      System.out.println("hashmap stats is" + hashMap);
      this.loader.got = false;
    } else {
      return DatasourceSerialize(hashMap);
    }
    HashMap<String, Object> hashMapSuccessCopy = new HashMap(hashMap);
    return serialize(hashMapSuccessCopy);
  }
}
