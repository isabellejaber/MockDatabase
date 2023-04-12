import java.sql.*;
import java.util.ArrayList;
/**
Database API
*/
public class Phase5JABERandBILODEAUapi {

  /** Name of the database */
  private static final String dbName = "proj_bilodeau_jaber";

  /** Database username */
  private static final String dbUser = "ijaber";

  /** Database password */
  private static final String dbPass = "pass8329";

  /** A database connection */
  Connection dbConnect;

  /** Prepared statement object for Query 1 */
  PreparedStatement psQ1;

  /** Prepared statement object for Query 2 */
  PreparedStatement psQ2;

  /** Prepared statement object for Query 3 */
  PreparedStatement psQ3;

  /** Prepared statement object for Query 4 */
  PreparedStatement psQ4;

  /** Prepared statement object for Query 5 */
  PreparedStatement psQ5;

  /** Prepared statement object for Query 6 */
  PreparedStatement psQ6;
  /**
  * Constructor establishes the database connection and initializes the
  * prepared statements.
  */
  public API() {

    // Using try-catch for the SQLException because I don't want the application
    // layer to need to use any items from the java.sql package.

    try {
      // Connect to the database
      this.dbConnect = DriverManager.getConnection(
        "jdbc:mariadb://cslab.skidmore.edu:3306/" + dbName + "?user="
        + dbUser + "&password=" + dbPass);

        // Initialize prepared statements

        // Query 1: Retrieve the catalogue numbers and titles of the pieces of art made by a particular artist
        String q1SQL = "SELECT catalogue_num, title FROM artwork WHERE aname = ?";
        this.psQ1 = dbConnect.prepareStatement(q1SQL);

        // Query 2: Retrieve the artist name and the title of the pieces that are of a particular type
        String q2SQL = "SELECT a.aname, p.title "
                        + "FROM artist a, artwork p "
                        +  "WHERE type = ? AND a.aname = p.aname";
        this.psQ2 = dbConnect.prepareStatement(q2SQL);

        // Query 3 Retrieve the titles and department of pieces that are in one of two departments
        String q3SQL = "SELECT DISTINCT aw.title, aw.dept_name "
                        + "FROM artwork aw "
                        + "WHERE aw.dept_name = ? OR aw.dept_name = ?";
        this.psQ3 = dbConnect.prepareStatement(q3SQL);

        // Query 4 Retrieve the number of paintings in a particular department
        String q4SQL = "SELECT COUNT(*) AS owner_num "
                        + "FROM artwork aw "
                        + "WHERE aw.dept_name = ? GROUP BY aw.dept_name";
        this.psQ4 = dbConnect.prepareStatement(q4SQL);

        // Query 5 Retrieve the names of the owners who own more than X number of pieces of art
        String q5SQL = "SELECT DISTINCT ow.oname "
                        + "FROM owned_by ow, owner o "
                        + "WHERE ow.oname = o.oname "
                        + "GROUP BY ow.oname "
                        + "HAVING COUNT(ow.oname) > ?";
        this.psQ5 = dbConnect.prepareStatement(q5SQL);

        // Query 6 Retrieve the names of owners who own a piece of art made before that of a certain piece
        String q6SQL = "SELECT DISTINCT o.oname "
                        + "FROM owned_by o, artwork a "
                        + "WHERE o.oname = a.oname AND a.year_made < (SELECT a1.year_made "
                            + "FROM artwork a1 "
                            + "WHERE catalogue_num = ?)";
        this.psQ6 = dbConnect.prepareStatement(q6SQL);

    }
    catch(SQLException e) {
      e.printStackTrace();
      System.out.println("Terminating program");
      System.exit(1);
    }

  } // end of constructor

  /**
  * Runs Query 1: Retrieve the catalogue numbers and titles of the pieces of art made by a particular artist
  *
  * @param name The boat color
  * @return List containing each row of the result represented as a string.
  */
  public ArrayList<String> runQ1(String name) {

    ArrayList<String> result = new ArrayList<String>();

    try {
      // Set the query parameter
      this.psQ1.setString(1, name);

      // Execute the query
      ResultSet rs = this.psQ1.executeQuery();

      // Process the result set. Create a string for each row and add that string
      // to a list.
      while(rs.next()) {
        int id = rs.getInt("catalogue_num");
        String title = rs.getString("title");
        String row = id + ", " + title;
        result.add(row);
      }
    } catch(SQLException e) {
      e.printStackTrace();
      System.out.println("Terminating program");
      System.exit(1);
    }

    return result;
  }

  /**
  * Runs Query 2: Retrieve the artist name and the title of the pieces that are of a particular type
  *
  * @param rating The artwork type
  * @return List containing each row of the result represented as a string.
  */
  public ArrayList<String> runQ2(String type) {

    ArrayList<String> result = new ArrayList<String>();

    try {
      // Set the query parameter
      this.psQ2.setString(1, type);

      // Execute the query
      ResultSet rs = this.psQ2.executeQuery();

      // Process the result set. Create a string for each row and add that string
      // to a list.
      while(rs.next()) {
        String name = rs.getString("aname");
        String title = rs.getString("title");
        String row  = name + ", " + title;
        result.add(row);
      }
    } catch(SQLException e) {
      e.printStackTrace();
      System.out.println("Terminating program");
      System.exit(1);
    }

    return result;
  }

  /**
  * Runs Query 3: Retrieve the titles and department of pieces that are in one of two departments.
  *
  * @param dept_name_1
  * @param dept_name_2
  * @return List containing each row of the result represented as a string.
  */
  public ArrayList<String> runQ3(String dept_name_1, String dept_name_2) {

    ArrayList<String> result = new ArrayList<String>();

    try {
      // Set the query parameter
      this.psQ3.setString(1, dept_name_1);
      this.psQ3.setString(2, dept_name_2);

      // Execute the query
      ResultSet rs = this.psQ3.executeQuery();

      // Process the result set. Create a string for each row and add that string
      // to a list.
      while(rs.next()) {
        String title = rs.getString("title");
        String dept_name = rs.getString("dept_name");
        String row = title + ", " + dept_name;
        result.add(row);
      }
    }
    catch(SQLException e) {
      e.printStackTrace();
      System.out.println("Terminating program");
      System.exit(1);
    }

    return result;
  }

  /**
  * Runs Query 4: Retrieve the number of paintings in a particular department.
  *
  * @param dept_name
  * @return List containing each row of the result represented as a string.
  */
  public ArrayList<String> runQ4(String dept_name) {

    ArrayList<String> result = new ArrayList<String>();

    try {
      // Set the query parameter
      this.psQ4.setString(1, dept_name);

      // Execute the query
      ResultSet rs = this.psQ4.executeQuery();

      // Process the result set. Create a string for each row and add that string
      // to a list.
      while(rs.next()) {
        String count = rs.getString("owner_num");
        String row = count;
        result.add(row);
      }
    }
    catch(SQLException e) {
      e.printStackTrace();
      System.out.println("Terminating program");
      System.exit(1);
    }

    return result;
  }

  /**
  * Runs Query 5: Retrieve the names of the owners who own more than X number of pieces of art.
  *
  * @param number
  * @return List containing each row of the result represented as a string.
  */
  public ArrayList<String> runQ5(int num) {

    ArrayList<String> result = new ArrayList<String>();

    try {
      // Set the query parameter
      this.psQ5.setInt(1, num);

      // Execute the query
      ResultSet rs = this.psQ5.executeQuery();

      // Process the result set. Create a string for each row and add that string
      // to a list.
      while(rs.next()) {
        String name = rs.getString("oname");
        String row = name;
        result.add(row);
      }
    }
    catch(SQLException e) {
      e.printStackTrace();
      System.out.println("Terminating program");
      System.exit(1);
    }

    return result;
  }

  /**
  * Runs Query 6 : Retrieve the names of owners who own a piece of art made before that of a certain piece.
  *
  * @param salary
  * @return List containing each row of the result represented as a string.
  */

  public ArrayList<String> runQ6(int catalogue_num) {

    ArrayList<String> result = new ArrayList<String>();

    try {
      // Set the query parameter
      this.psQ6.setInt(1, catalogue_num);

      // Execute the query
      ResultSet rs = this.psQ6.executeQuery();

      // Process the result set. Create a string for each row and add that string
      // to a list.
      while(rs.next()) {
        String name = rs.getString("oname");
        String row = name;
        result.add(row);
      }
    }
    catch(SQLException e) {
      e.printStackTrace();
      System.out.println("Terminating program");
      System.exit(1);
    }

    return result;
  }

  public static void main(String[] args) {
    API new_api = new API();
    new_api.runQ1("'Albert Bierstadt'");
    new_api.runQ2("'Painting'");
    new_api.runQ3("'Drawings and Paintings'", "'European Paintings'");
    new_api.runQ4("'European Paintings'");
    new_api.runQ5(1);
    new_api.runQ6(004);
  }
}
