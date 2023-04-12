import java.util.ArrayList;

/**
* Test program for our Database API.
*
* Compile this program with:
javac -cp .:./mariadb-java-client-2.7.3.jar Phase5JABERandBILODEAUapi.java Phase5JABERandBILODEAUapi_test.java
*
* Run this program with:
java -cp .:./mariadb-java-client-2.7.3.jar Phase5JABERandBILODEAUapi_test
*/
public class APITest {


  /**
  * Prints each string in the ArrayList on a separate line.
  *
  * @param result The list of strings to print
  */
  private static void printResult(ArrayList<String> result) {
    for(String s : result) {
      System.out.println(s);
    }
  }

  /**
  * Program execution begins at the main method.
  *
  * @param args Not used
  */
  public static void main(String[] args) {

    // Create a BoatRentalAPI object
    API br = new API();

    // List for the query result
    ArrayList<String> result;

    // Call the method for Query 1, then print the result
    result = br.runQ1("Jim Hum");
    System.out.println("\nQuery 1 ---- Works by Jim Hum:");
    printResult(result);

    // Call the method for Query 2, then print the result
    result = br.runQ2("Painting");
    System.out.println("\n\nQuery 2 ---- Paintings:");
    printResult(result);

    // Call the method for Query 3, then print the result
    result = br.runQ3("European Paintings", "Asian Art");
    System.out.println("\n\nQuery 3 ---- Works in the European Paintings and Asian Art departments:");
    printResult(result);

    // Call the method for Query 4, then print the result
    result = br.runQ4("Drawings and Paintings");
    System.out.println("\n\nQuery 4 ---- Number of works in Drawings and Painting department:");
    printResult(result);

    // Call the method for Query 5, then print the result
    result = br.runQ5(1);
    System.out.println("\n\nQuery 5 ---- Owners who own more than 1 number of pieces of art:");
    printResult(result);

    // Call the method for Query 6, then print the result
    result = br.runQ6(9);
    System.out.println("\n\nQuery 6 ---- Owners who own a piece of art made before a certain date");
    printResult(result);

  }
}
