package edu.brown.cs32.examples.moshiExample.csv;

import java.util.HashMap;
import java.util.List;

/** counts number of words, characters, rows, and columns */
public class Count {

  int numWords;
  int numChar;
  int numRows;
  int numCols;

  public Count() {
    this.numWords = 0;
    this.numChar = 0;
    this.numRows = 0;
    this.numCols = 0;
  }

  /**
   * counts how many words, characters, rows, and columns in the parsed csv file
   *
   * @param lists, a list of list of strings representing a csv file
   * @return a hashmap that links words to the word count, charcaters to the character count, rows
   *     to the row count, column to column ocunt
   */
  public HashMap<String, Integer> Counter(List<List<String>> lists) {
    HashMap<String, Integer> counter = new HashMap<>();
    // gets the length of the first row of the file to count number of cols
    this.numCols += lists.get(0).size();
    for (int i = 0; i < lists.size(); i++) { // for each row
      this.numRows++; // increase number of rows
      for (int j = 0; j < lists.get(i).size(); j++) // for each group of words between commas
      if (lists.get(i).get(j) != "") {
          this.numChar += lists.get(i).get(j).length(); // gets number of character
          String[] singleWord = lists.get(i).get(j).split(" "); // splits on the spaces
          this.numWords += singleWord.length; // gets number of words
        }
    }
    // creating hashmap
    counter.put("Words: ", this.numWords);
    counter.put("Characters: ", this.numChar);
    counter.put("Rows: ", this.numRows);
    counter.put("Columns: ", this.numCols);
    return counter;
  }
}
