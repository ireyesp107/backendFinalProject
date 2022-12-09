package edu.brown.cs32.examples.moshiExample.server.Handlers;

import com.squareup.moshi.Moshi;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import spark.QueryParamsMap;
import spark.Request;
import spark.Response;
import spark.Route;



public class GetCategories extends genericHandler implements Route {
  private HashMap<String, ArrayList> categories;
  public HashMap<String, HashMap> songMap;
  public GetCategories(HashMap songMapInput) throws FileNotFoundException {
    songMap = songMapInput;
  }



  public static PropertyCollection songsHandler(String path) throws IOException {
    PropertyCollection dataContent;
    String data = new String(Files.readAllBytes(Paths.get(path)));
    Moshi moshi = new Moshi.Builder().build();
    dataContent = moshi.adapter(PropertyCollection.class).fromJson(data);
    //System.out.println(dataContent);
    return dataContent;
  }

  public static HashMap<String, HashMap> createSongMap() throws IOException {
    PropertyCollection jsonData = songsHandler(
        "/Users/amaris/Desktop/cs32/sprint-2-agrondin-estill/src/main/java/edu/brown/cs32/examples/moshiExample/server/Handlers/songs.json");
    HashMap<String, HashMap> songMap = new HashMap<>();
    for(int i = 0; i < jsonData.properties.size(); i ++){
      HashMap categories = new HashMap();
      categories.put("artist",jsonData.properties.get(i).artist);
      categories.put("pos",jsonData.properties.get(i).pos);
      if(jsonData.properties.get(i).tags.isEmpty()){
        continue;
      } else {
      categories.put("tag",jsonData.properties.get(i).tags);
      categories.put("year",jsonData.properties.get(i).year);
      songMap.put(jsonData.properties.get(i).title.toLowerCase(), categories);
    }}
    System.out.println(songMap.size());
    return songMap;
  }

  @Override
  public Object handle(Request request, Response response) throws IOException {
    QueryParamsMap qpm = request.queryMap();
    String songInput = qpm.value("songs");
    HashMap<String, Object> finalMap = new HashMap();

    if ((!(qpm.hasKey("songs"))) || (songInput.length()==0)) { //bad request
      return BadRequestSerialize(hashMap);
    }
    System.out.println("after");
    if (qpm.hasKey("songs")) {
      String newSong = songInput.replace("_", " ");
      categories = songMap.get(newSong);
      System.out.println(songMap.get(newSong));
      finalMap.put("categories", categories);
      finalMap.put("Result", "Success");
      }


    HashMap<String, Object> hashMapSuccessCopy = new HashMap(finalMap);
    return serialize(hashMapSuccessCopy);

  }

  }


