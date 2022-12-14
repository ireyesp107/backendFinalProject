package edu.brown.cs32.examples.moshiExample.server.Handlers;

import static java.lang.String.valueOf;

import com.squareup.moshi.Moshi;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
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
    return dataContent;
  }

  public static HashMap<String, HashMap> createSongMap() throws IOException {
    PropertyCollection jsonData = songsHandler(
        "/Users/student/Documents/GitHub/backendFinalProject/src/main/java/edu/brown/cs32/examples/moshiExample/server/Handlers/songs.json");
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
    String uncapital = songInput.toLowerCase();
    String songSearch = uncapital.replace("_", " ");
    HashMap<String, Object> finalMap = new HashMap();
    getSongNames assist = new getSongNames();
    String[] list = assist.getter();
    System.out.println(Arrays.toString(list));

    if (!(qpm.hasKey("songs"))){
      return BadRequestSerialize(hashMap);
    }
    if (qpm.hasKey("songs") && songInput.length() == 0){
      hashMap.clear();
      return BadRequestSerialize(hashMap);
    }
    if (qpm.hasKey("songs") && !Arrays.asList(list).contains(songSearch)) {
      hashMap.clear();
      return DatasourceSerialize(hashMap);
    }
    if (!(request.queryParams().size() == 1)){
      hashMap.clear();
      return BadRequestSerialize(hashMap);
    }
    if (qpm.hasKey("songs")) {
      try {
        categories = songMap.get(songSearch);
        System.out.println(songMap.get(songSearch));
        finalMap.put("Result", "Success");
        finalMap.put("categories", categories);
      } catch (Exception e) {
        hashMap.clear();
        return BadRequestSerialize(hashMap);
      }
      }
      HashMap<String, Object> hashMapSuccessCopy = new HashMap(finalMap); //defensive programming
      return serialize(hashMapSuccessCopy);
    }
  }


