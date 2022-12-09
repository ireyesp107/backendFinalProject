package edu.brown.cs32.examples.moshiExample.server;

import java.util.ArrayList;
import java.util.List;

/**
 * class of the shared csv data
 */

public class csvData {
  public static List<List<String>> csvContent;
  // to be able to get only once
  public boolean loaded;
  public boolean got;

  /**
   * constructor that initialized the array for the csv content and sets bool to false
   */

  public csvData(){
    this.csvContent = new ArrayList<>();
    this.loaded = false;
    this.got = false;
  }

  /**
   * sets the csv content and sets bool to true
   * @param csvContent, content of csv
   */
  public void setCSVData(List<List<String>> csvContent) {
    this.loaded = true;
    this.got = false;
    this.csvContent = csvContent;
  }

  /**
   * gets the csv content
   * @return csv content
   */

  public List<List<String>> getCSVData(){
    this.loaded = false;
    this.got = true;
    return this.csvContent;
  }

  public void clearcsv() {
    this.csvContent.clear();
  }
}

