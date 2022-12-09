package edu.brown.cs32.examples.moshiExample.csv;

import java.util.List;

/**
 * specifices the object T of as a list of string so that the method can return a list of strings
 */
public class ListStringCreator implements CreatorFromRow<List<String>> {

  /**
   * @param row, a list of string representing the rows of a file
   * @return a list of string representing the rows of a file
   * @throws FactoryFailureException
   */
  @Override
  public List<String> create(List<String> row) throws FactoryFailureException {
    return row;
  }
}
