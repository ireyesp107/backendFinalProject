package edu.brown.cs32.examples.moshiExample.csv;

import static java.util.Collections.unmodifiableList;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;

/**
 * parses the object
 *
 * @param <T>
 */
public class Parse<T> {

  // declare a field of a class before assigning t in the constructor
  BufferedReader bReader; // instance variable
  CreatorFromRow<T> creator;
  ArrayList<T> listT;
  Boolean hasHeaderRow;

  // parser should return List <T> : read each line, make it a string, then seperate them as
  // seperate objects
  // use create method which takes in list of strings and convert into T then add the Ts into a List
  // <T>
  public Parse(Reader read, CreatorFromRow<T> creator, boolean hasHeaderRow) {
    this.listT = new ArrayList<T>();
    this.creator = creator;
    this.bReader = new BufferedReader(read);
    this.hasHeaderRow = hasHeaderRow;
  }

  public List<T> parser() throws IOException, FactoryFailureException {

    try {
      // skips the first line of file (because it describes the content
      String line = this.bReader.readLine();
      if (hasHeaderRow) {
        line = this.bReader.readLine();
      }
      while (line != null) {
        // split the line at the comma to create a string array of the file
        String[] seperatedHeaderRow = line.split(",");
        // convert list of strings into T
        T TObject = this.creator.create(List.of(seperatedHeaderRow));
        // add t into list of T
        listT.add(TObject);
        // reads the next line
        line = this.bReader.readLine();
      }
      return unmodifiableList(listT);
    } catch (FileNotFoundException e) {
      System.out.println("File not found");
    } catch (IOException e) {
      System.err.println("Input output error - IOException");
    } catch (FactoryFailureException e){
      System.out.println("Null row was given to create");

    }
    return null;
  }
}
