//package edu.brown.cs32.examples.moshiExample;
//
//import com.sun.tools.javac.Main;
//import edu.brown.cs32.examples.moshiExample.csv.FactoryFailureException;
//import edu.brown.cs32.examples.moshiExample.csv.Parse;
//import edu.brown.cs32.examples.moshiExample.stars.stars.StarFactory;
//import java.io.FileNotFoundException;
//import java.io.FileReader;
//import java.util.List;
//import org.junit.jupiter.api.Test;
//import spark.utils.Assert;
//
///** tests the exceptions */
//public class ErrorTest {
//
//  /**
//   * tests FactoryFailureException exception
//   *
//   * @throws FileNotFoundException
//   */
//  @Test
//  public void ErrorTestFF() throws FileNotFoundException {
//    StarFactory sf = new StarFactory();
//    FileReader fr =
//        new FileReader(
//            "/Users/amaris/Desktop/CS32/sprint-0-amarisg25-master/data/stars/wrongfile.csv");
//    Parse<List<String>> parser = new Parse(fr, sf, true);
//    Assert.assertThrows(FactoryFailureException.class, () -> parser.parser());
//  }
//
//  /** tests NullPointerException exception */
//  @Test
//  public void ErrorTestNull() {
//    Assert.assertThrows(NullPointerException.class, () -> Main.main(null));
//  }
//}
