//package edu.brown.cs32.examples.moshiExample;
//
//import static org.junit.jupiter.api.Assertions.assertEquals;
//
//import edu.brown.cs32.examples.moshiExample.csv.CreatorFromRow;
//import edu.brown.cs32.examples.moshiExample.csv.FactoryFailureException;
//import edu.brown.cs32.examples.moshiExample.csv.ListStringCreator;
//import edu.brown.cs32.examples.moshiExample.csv.Parse;
//import edu.brown.cs32.examples.moshiExample.stars.stars.GalaxyGenerator;
//import edu.brown.cs32.examples.moshiExample.stars.stars.Star;
//import edu.brown.cs32.examples.moshiExample.stars.stars.StarFactory;
//import java.io.FileReader;
//import java.io.IOException;
//import java.util.ArrayList;
//import java.util.Collections;
//import java.util.List;
//import org.junit.jupiter.api.Test;
//
///** Tests the parser */
//public class ParserTest {
//
//  /**
//   * tests parser for ten-star.csv
//   *
//   * @throws IOException
//   * @throws FactoryFailureException
//   */
//  @Test
//  public void parseTenStar() throws IOException, FactoryFailureException {
//    CreatorFromRow<Star> starFactory = new StarFactory();
//    GalaxyGenerator galaxyGen = new GalaxyGenerator();
//
//    FileReader tenStar =
//        new FileReader(
//            "/Users/amaris/Desktop/CS32/sprint-0-amarisg25-master/data/stars/ten-star.csv");
//    Parse<Star> parseTS = new Parse<>(tenStar, starFactory, true);
//    Star starObj1 = new Star(0, "Sol", 0.0, 0.0, 0.0);
//    Star starObj2 = new Star(3, "", 277.11358, 0.02422, 223.27753);
//    Star starObj3 = new Star(118721, "", -2.28262, 0.64697, 0.29354);
//
//    List<Star> lstSt = galaxyGen.generate(7);
//    starObj1.getDimension();
//
//    List<Star> tStar = parseTS.parser();
//
//    Star star1 = tStar.get(0);
//    Star star2 = tStar.get(3);
//    Star star3 = tStar.get(tStar.size() - 1);
//    assertEquals(star1, starObj1);
//    assertEquals(star2, starObj2);
//    assertEquals(star3, starObj3);
//  }
//
//  /**
//   * tests parser for parseStarData.csv
//   *
//   * @throws IOException
//   * @throws FactoryFailureException
//   */
//  @Test
//  public void parseStarData() throws IOException, FactoryFailureException {
//    CreatorFromRow<Star> starFactory = new StarFactory();
//    FileReader starData =
//        new FileReader(
//            "/Users/amaris/Desktop/CS32/sprint-0-amarisg25-master/data/stars/stardata.csv");
//    Star starObj1 = new Star(0, "Sol", 0.0, 0.0, 0.0);
//    Star starObj2 = new Star(50, "Leilani", 35.55067, 0.08864, -47.34518);
//    Star starObj3 = new Star(119616, "Van_17", 5.68997, 0.05287, -5.33537);
//    Parse<Star> parseTS = new Parse<>(starData, starFactory, true);
//    List<Star> starD = parseTS.parser();
//    Star star1 = starD.get(0);
//    Star star2 = starD.get(50);
//    Star star3 = starD.get(starD.size() - 1);
//    System.out.println(star1);
//    System.out.println(star2);
//    System.out.println(star3);
//    assertEquals(star1, starObj1);
//    assertEquals(star2, starObj2);
//    assertEquals(star3, starObj3);
//  }
//
//  /**
//   * tests parser for fruits.csv
//   *
//   * @throws IOException
//   * @throws FactoryFailureException
//   */
//  @Test
//  public void parseFruit() throws IOException, FactoryFailureException {
//    ListStringCreator fruit = new ListStringCreator();
//    FileReader fruitData =
//        new FileReader(
//            "/Users/amaris/Desktop/CS32/sprint-0-amarisg25-master/data/stars/fruits.csv");
//    Parse<List<String>> parseF = new Parse<>(fruitData, fruit, true);
//    List<List<String>> fruitList = parseF.parser();
//    List<String> ls1 = new ArrayList<>();
//    ls1.add("banana");
//    ls1.add("good");
//    ls1.add("8");
//    ls1.add("yellow");
//    ls1.add("curve");
//    List<String> ls2 = new ArrayList<>();
//    ls2.add("4");
//    ls2.add(" ");
//    ls2.add("78 years old");
//    ls2.add("0");
//    ls2.add("square");
//    List<String> ls3 = new ArrayList<>();
//    ls3.add("5");
//    ls3.add("good food");
//    ls3.add("");
//    ls3.add("0");
//    ls3.add("square");
//
//    List<String> fruit1 = fruitList.get(0);
//    List<String> fruit2 = fruitList.get(3);
//    List<String> fruit3 = fruitList.get(4);
//    assertEquals(fruit1, ls1);
//    assertEquals(fruit2, ls2);
//    assertEquals(fruit3, ls3);
//    assertEquals(fruitList.size(), 5);
//  }
//
//  /**
//   * test for empty.csv, the empty file
//   *
//   * @throws IOException
//   * @throws FactoryFailureException
//   */
//  @Test
//  public void parseEmpty() throws IOException, FactoryFailureException {
//    ListStringCreator empty = new ListStringCreator();
//    FileReader emptyData =
//        new FileReader(
//            "/Users/amaris/Desktop/CS32/sprint-0-amarisg25-master/data/stars/emptyfile.csv");
//    Parse<List<String>> parseE = new Parse<>(emptyData, empty, true);
//    List<List<String>> emptyList = parseE.parser();
//    List<String> el = new ArrayList<>();
//    assertEquals(emptyList, el);
//  }
//
//  /**
//   * test a csv file with only commas and spaces
//   * @throws IOException
//   * @throws FactoryFailureException
//   */
//
//  @Test
//  public void parseComma() throws IOException, FactoryFailureException {
//    ListStringCreator comma = new ListStringCreator();
//    FileReader fruitData =
//        new FileReader("/Users/amaris/Desktop/CS32/sprint-0-amarisg25-master/data/stars/comma.csv");
//    Parse<List<String>> parseF = new Parse<>(fruitData, comma, true);
//    List<List<String>> commaList = parseF.parser();
//    List<String> com1 = Collections.emptyList();
//    List<String> com2 = Collections.emptyList();
//    List<List<String>> emp = new ArrayList<>();
//
//    emp.add(com1);
//    emp.add(com2);
//
//    assertEquals(commaList, emp);
//
//  }
//
//  /**
//   * tests a regular csv file with no header
//   * @throws IOException
//   * @throws FactoryFailureException
//   */
//
//
//  @Test
//  public void parseNoHeader() throws IOException, FactoryFailureException {
//    ListStringCreator noHeader = new ListStringCreator();
//    FileReader fruitData =
//        new FileReader("/Users/amaris/Desktop/cs32/sprint-0-amarisg25/data/stars/newHeader.csv");
//    Parse<List<String>> parseF = new Parse<>(fruitData, noHeader, false);
//    List<List<String>> noHeaderList = parseF.parser();
//    assertEquals(noHeaderList.size(), 3);
//  }
//
//  /**
//   * tests a regular csv file with header
//   * @throws IOException
//   * @throws FactoryFailureException
//   */
//
//  @Test
//  public void parseHeader() throws IOException, FactoryFailureException {
//    ListStringCreator noHeader = new ListStringCreator();
//    FileReader fruitData =
//        new FileReader("/Users/amaris/Desktop/cs32/sprint-0-amarisg25/data/stars/newHeader.csv");
//    Parse<List<String>> parseF = new Parse<>(fruitData, noHeader, true);
//    List<List<String>> noHeaderList = parseF.parser();
//    assertEquals(noHeaderList.size(), 2);
//  }
//
//  /**
//   * tests a file with only a header
//   * @throws IOException
//   * @throws FactoryFailureException
//   */
//
//  @Test
//  public void parseOnlyHeader() throws IOException, FactoryFailureException {
//    ListStringCreator onlyHeader = new ListStringCreator();
//    FileReader fruitData =
//        new FileReader("/Users/amaris/Desktop/cs32/sprint-0-amarisg25/data/stars/onlyHeaderRow.csv");
//    Parse<List<String>> parseF = new Parse<>(fruitData, onlyHeader, true);
//    List<List<String>> onlyHeaderList = parseF.parser();
//    assertEquals(onlyHeaderList.size(), 0);
//  }
//
//  /**
//   * tests a file with only one line that isn't a header
//   * @throws IOException
//   * @throws FactoryFailureException
//   */
//
//  @Test
//  public void parseOnlyNoHeader() throws IOException, FactoryFailureException {
//    ListStringCreator onlyHeader = new ListStringCreator();
//    FileReader fruitData =
//        new FileReader("/Users/amaris/Desktop/cs32/sprint-0-amarisg25/data/stars/onlyHeaderRow.csv");
//    Parse<List<String>> parseF = new Parse<>(fruitData, onlyHeader, false);
//    List<List<String>> onlyHeaderList = parseF.parser();
//    assertEquals(onlyHeaderList.size(), 1);
//  }
//}
