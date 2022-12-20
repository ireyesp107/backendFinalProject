package edu.brown.cs32.examples.moshiExample.server.Handlers;

import com.squareup.moshi.Moshi;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import spark.QueryParamsMap;
import spark.Request;
import spark.Response;
import spark.Route;

/**
 * Gets the list of songs from the json.
 */

public class getSongNames extends genericHandler implements Route {


  public getSongNames() throws IOException {
    System.out.println("hello");
  }

  /**
   * Deserializes the songs from the easy json of songs.
   */


  // should i ut this in csvData cause the exact same thing as getCategories function
  public static EasySongs getEasySongNames(String path) throws IOException {
    System.out.println("please");
//    System.out.println("hello");
    EasySongs dataContent;
    String data = new String(Files.readAllBytes(Paths.get(path)));
    Moshi moshi = new Moshi.Builder().build();
    dataContent = moshi.adapter(EasySongs.class).fromJson(data);
    System.out.println("Made it");
    return dataContent;

  }

  /**
   * Deserializes the songs from the difficult json of songs.
   */

  public static DiffSongs getDiffSongNames(String path) throws IOException {
    System.out.println("chay");
    DiffSongs dataContent;
    String data = new String(Files.readAllBytes(Paths.get(path)));
    Moshi moshi = new Moshi.Builder().build();
    dataContent = moshi.adapter(DiffSongs.class).fromJson(data);
    System.out.println(dataContent);
    return dataContent;
  }

  // use relative path instead absolute path

  /**
   * handles the requests by creating the map with the easy or difficult songs based ont he request
   * @param request, request from front end
   * @param response response
   * @return map containing the list of songs
   * @throws IOException
   */
  DiffSongs songDiff = getDiffSongNames("/Users/amaris/Desktop/cs32/backendFinalProject/src/"
      + "main/java/edu/brown/cs32/examples/moshiExample/server/Handlers/diffsongNames.json");



  EasySongs songEasy = getEasySongNames("/Users/amaris/Desktop/cs32/backendFinalProject/src/"
      + "main/java/edu/brown/cs32/examples/moshiExample/server/Handlers/easysongNames.json");
  @Override
  public Object handle(Request request, Response response) throws IOException {
    System.out.println("yo");

    System.out.println(songEasy.EasySongs);
    //System.out.println("song easy size" + songEasy);
    //System.out.println("song hard size" + songDiff);
    System.out.println("yooo");
    QueryParamsMap qpm = request.queryMap();
      hashMap.clear();
      System.out.println("loll");

      if (qpm.hasKey("easy"))  {
        try {
          System.out.println("easyy");

//        this.songInList = Arrays.stream(this.songNames).anyMatch(songsWanted::equals);
//        if (this.songInList) {
          hashMap.put("Song", songEasy);
          hashMap.put("Result", "Success");
        } catch (Exception e) {
          return BadRequestSerialize(hashMap);
        }

      } else if (qpm.hasKey("diff")){

        try {
          System.out.println("hi");
          hashMap.put("Song", songDiff);
          hashMap.put("Result", "Success");
        } catch (Exception e) {
          return BadRequestSerialize(hashMap);
        }
      }


    HashMap<String, Object> hashMapSuccessCopy = new HashMap(hashMap);
    return serialize(hashMapSuccessCopy);
  }




}
