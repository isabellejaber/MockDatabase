import java.util.ArrayList;
import java.util.Random;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.util.*;

public class InsertArtworks {

      // Database connection
      private Connection connect = null;

      // Prepared Statement
      // This allows insert queries to run faster than issuing a new SQL
      // statement for each individual insert
      private PreparedStatement preparedStatement = null;

      public static void main(String[] args) throws Exception {

        InsertArtworks ia;
        Random random = new Random();
        int numWorks;
        ArrayList<String> data = new ArrayList<String>();
        ArrayList<String> titles = new ArrayList<String>();

        try{
          ia = new InsertArtworks();
        } catch(Exception e) {
          throw e;
        }

        try {
          File myObj = new File("artwork.txt");
          Scanner myReader = new Scanner(myObj);
          while (myReader.hasNextLine()) {
            String temp[] = (myReader.nextLine().split(","));
            titles.add(temp[1]);
            for(String i : temp) {
              data.add(i);
            }
          }
          myReader.close();
        } catch (FileNotFoundException e) {
          System.out.println("An error occurred.");
          e.printStackTrace();
        }
        // Get the list of artist IDs from the database
        String artistQuery = "Select aname from artist";
        ArrayList<String> artistNames = ia.getIdsFromDB(artistQuery, "aname");

        String ownerQuery = "Select oname from owner";
        ArrayList<String> ownerNames = ia.getIdsFromDB(ownerQuery, "oname");

        String deptQuery = "Select dept_name from department";
        ArrayList<String> dept_names = ia.getIdsFromDB(deptQuery, "dept_name");

        Integer indx = 3;
// just need at least as many artworks as artists
        for(String i : artistNames) {
          ArrayList<Integer> temp = new ArrayList<Integer>();
            // Generate a random number between 1 and 5 for the number
            // of paintings this artist has
            numWorks = random.nextInt(5) + 1;
            for(int j = 0; j < numWorks; j++) {
              temp.add(Integer.parseInt(data.get(indx)));
              indx+=3;
            }
            try{
                ia.generateArtworks(titles, numWorks, temp, dept_names, ownerNames, artistNames, random);
            } catch(Exception e) {
                throw e;
            }
        }
      }

      //
      // Constructor
      //
      public InsertArtworks() throws Exception {

          try{
              // Use JDBC
              // Class.forName("com.mysql.jdbc.Driver");

              // Connect to the database
              connect = DriverManager.getConnection(
                          "jdbc:mariadb://cslab.skidmore.edu:3306/proj_bilodeau_jaber?user=zbilodea&password=pass5763");

              // Setup the prepared statement for inserts to Reserve table
              preparedStatement = connect.prepareStatement(
                          "INSERT INTO artwork (catalogue_num,title,year_made,type,dept_name,oname,aname) values (?,?,?,?,?,?,?)");

          } catch(Exception e) {
              throw e;
          }
      } // end constructor
      //
      // Destructor
      // Close the database connection
      //
      @Override
      public void finalize() throws Throwable {
          super.finalize();
          connect.close();
      }

      //
      // getIdsFromDB
      // Runs the query passed in as an argument
      // Expects the query to return one attribute with type integer.
      // Puts the query results into an array list of integers
      //
      public ArrayList<String> getIdsFromDB(String sqlQuery, String columnLabel) throws Exception {

          Statement getIds;
          ResultSet resultSet;
          ArrayList<String> resultList = new ArrayList<String>();

          try {
              // Run the query
              getIds = connect.createStatement();
              resultSet = getIds.executeQuery(sqlQuery);

              // Put the query results into an ArrayList<Integer>
              while(resultSet.next()) {
                  resultList.add(new String(resultSet.getString(columnLabel)));
              }
          } catch(Exception e) {
              throw e;
          }

          return resultList;
      } // end getIdsFromDB
      //
      // generateReservations
      // For the given sailor, generates the given number of reservations of random boats
      //
      public void generateArtworks(ArrayList<String> titles, int numArtworks, ArrayList<Integer> catalogue_nums, ArrayList<String> dept_names, ArrayList<String> onames, ArrayList<String> anames, Random random) throws Exception {

          // For the day of the reservation, I'm starting at 1 and assuming that numRes < 31

          ArrayList<String> types = new ArrayList<String>(Arrays.asList("Painting", "Drawing", "Wood Block Print", "Sculpture", "Textile", "Pottery"));

          // Loop numRes times
          for(int i = 0; i < numArtworks; i++) {
              // catalogue_num++;
              int n = random.nextInt(titles.size());
              String title = titles.get(n);

              n = random.nextInt(dept_names.size());
              String dept_name = dept_names.get(n);

              n = random.nextInt(onames.size());
              String oname = onames.get(n);

              n = random.nextInt(anames.size());
              String aname = anames.get(n);

              n = random.nextInt(1022)+1000;
              int year_made = n;

              n = random.nextInt(types.size());
              String type = types.get(n);
              // Insert the reservation into the Reserve table
              try{
                  // Set the values of the three attributes in the insert statement
                  preparedStatement.setInt(1, catalogue_nums.get(i));
                  preparedStatement.setString(2, title);
                  preparedStatement.setInt(3, year_made);
                  preparedStatement.setString(4, type);
                  preparedStatement.setString(5, dept_name);
                  preparedStatement.setString(6, oname);
                  preparedStatement.setString(7, aname);

// title,year_made,type,dept_name,oname,aname
                  // Execute the insert
                  preparedStatement.executeUpdate();

              }catch(Exception e) {
                  throw e;
              }
          }
      } // end generateReservations


}
