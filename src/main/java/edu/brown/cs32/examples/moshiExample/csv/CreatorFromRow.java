package edu.brown.cs32.examples.moshiExample.csv;

import java.util.List;

/**
 * Creates an object of type T from a List of Strings.
 *
 * @param <T> object to be created
 */
public interface CreatorFromRow<T> {

  T create(List<String> row) throws FactoryFailureException;

  /**
   * specifices the object T of as a list of string so that the method can return a list of strings
   */
}

