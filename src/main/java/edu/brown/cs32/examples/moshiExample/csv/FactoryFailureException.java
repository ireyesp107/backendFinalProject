package edu.brown.cs32.examples.moshiExample.csv;

import java.util.List;

/** Exception thrown when a Factory class fails. */
public class FactoryFailureException extends Exception {
  final List<String> row;

  public FactoryFailureException(List<String> row) {
    this.row = row;
  }
}
