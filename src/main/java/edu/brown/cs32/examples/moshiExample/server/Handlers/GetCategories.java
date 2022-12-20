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
  public HashMap<String, HashMap> easysongMap;
  public String songInput;
  public GetCategories(HashMap songMapInput, HashMap easysongMapInput) throws FileNotFoundException {
    songMap = songMapInput;
    easysongMap = easysongMapInput;
  }

  public static PropertyCollection songsHandler(String path) throws IOException {
    PropertyCollection dataContent;
    String data = new String(Files.readAllBytes(Paths.get(path)));
    Moshi moshi = new Moshi.Builder().build();
    dataContent = moshi.adapter(PropertyCollection.class).fromJson(data);
    return dataContent;
  }

  public static HashMap<String, HashMap> createSongMap() throws IOException {
    System.out.println("omg");
    PropertyCollection jsonData = songsHandler(


        "src/main/java/edu/brown/cs32/examples/moshiExample/server/Handlers/songs.json");


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
    System.out.println("priint");
    System.out.println(songMap.size());
    return songMap;


  }

  public static HashMap<String, HashMap> createEasyMap() throws IOException {
    PropertyCollection jsonData = songsHandler(

            "src/main/java/edu/brown/cs32/examples/moshiExample/server/Handlers/songs.json");

    HashMap<String, HashMap> easySongMap = new HashMap<>();
    for(int i = 0; i < jsonData.properties.size(); i ++){
      HashMap categories = new HashMap();
      if(jsonData.properties.get(i).year< 2000){
        continue;
      } else {
      categories.put("year",jsonData.properties.get(i).year);
      categories.put("artist",jsonData.properties.get(i).artist);
      categories.put("pos",jsonData.properties.get(i).pos);
      if(jsonData.properties.get(i).tags.isEmpty()){
        continue;
      } else {
        categories.put("tag",jsonData.properties.get(i).tags);

        easySongMap.put(jsonData.properties.get(i).title.toLowerCase(), categories);
      }}}
    System.out.println(easySongMap.size());
//    System.out.println(easySongMap.size());
    return easySongMap;
  }

  @Override
  public Object handle(Request request, Response response) throws IOException {
    System.out.println("handle");
    QueryParamsMap qpm = request.queryMap();
    if (qpm.hasKey("diffsongs")) {
      this.songInput = qpm.value("diffsongs");
    } else if (qpm.hasKey("easysongs")) {
      this.songInput = qpm.value("easysongs");
    }
    String uncapital = this.songInput.toLowerCase();
    String songSearch = uncapital.replace("_", " ");
    HashMap<String, Object> finalMap = new HashMap();
    getSongNames assist = new getSongNames();
    System.out.println("songsearch is " + songSearch);
   // String[] list = assist.getter();
   // System.out.println(Arrays.toString(list));


    if (!(qpm.hasKey("easysongs")) && !(qpm.hasKey("diffsongs"))){
      System.out.println("a");
      return BadRequestSerialize(hashMap);
    }
    if (((!(qpm.hasKey("easysongs")) && !(qpm.hasKey("diffsongs")))) && songInput.length() == 0){
      hashMap.clear();
      System.out.println("b");
      return BadRequestSerialize(hashMap);
    }
    if (qpm.hasKey("easysongs") && !(this.easysongMap.containsKey(songSearch))) {
      hashMap.clear();
      return DatasourceSerialize(hashMap);
    }
    if (qpm.hasKey("diffsongs") && !(this.songMap.containsKey(songSearch))) {
      hashMap.clear();
      return DatasourceSerialize(hashMap);
    }

    if (!(request.queryParams().size() == 1)){
      hashMap.clear();
      System.out.println("c");
      return BadRequestSerialize(hashMap);
    }
    if (qpm.hasKey("easysongs")) {
      try {

        categories = easysongMap.get(songSearch);
//        System.out.println(easysongMap.get(songSearch));
        finalMap.put("Result", "Success");
        finalMap.put("categories", categories);
      } catch (Exception e) {
        hashMap.clear();
        return BadRequestSerialize(hashMap);
      }
      } else if (qpm.hasKey("diffsongs")){
      try {
        System.out.println("goes trhough");
        categories = songMap.get(songSearch);
//        System.out.println(songMap.get(songSearch));
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


