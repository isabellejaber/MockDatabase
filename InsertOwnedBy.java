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

public class InsertOwnedBy {

      // Database connection
      private Connection connect = null;

      // Prepared Statement
      // This allows insert queries to run faster than issuing a new SQL
      // statement for each individual insert
      private PreparedStatement preparedStatement = null;

      public static void main(String[] args) throws Exception {

        InsertOwnedBy io;
        Random random = new Random();
        int numWorks;
        ArrayList<String> data = new ArrayList<String>();

        try{
          io = new InsertOwnedBy();
        } catch(Exception e) {
          throw e;
        }

        try {
          File myObj = new File("owned_by.txt");
          Scanner myReader = new Scanner(myObj);
          while (myReader.hasNextLine()) {
            String temp[] = (myReader.nextLine().split(","));
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
        String artworkQuery = "Select catalogue_num from artwork";
        ArrayList<Integer> catalogue_nums = io.getCatalogueNumsFromDB(artworkQuery, "catalogue_num");

        String ownerQuery = "Select oname from owner";
        ArrayList<String> onames = io.getIdsFromDB(ownerQuery, "oname");


// just need at least as many artworks as artists
        for(Integer i = 0; i < onames.size(); i++) {

            // Generate a random number between 1 and 5 for the number
            // of paintings this artist has
            int a_indx = random.nextInt(catalogue_nums.size());
            try{
                // Call the generateReservations method to insert data into
                // Reserves for this sailor

                io.generateOwnedBy(a_indx, i, data, catalogue_nums, onames, random);
                catalogue_nums.remove(a_indx);
            } catch(Exception e) {
                throw e;
            }
        }
        while(catalogue_nums.size() >= 1) {
            int o_indx = random.nextInt(onames.size());
            int a_indx = random.nextInt(catalogue_nums.size());
            try{
                io.generateOwnedBy(a_indx, o_indx, data, catalogue_nums, onames, random);
                catalogue_nums.remove(a_indx);
            } catch(Exception e) {
                throw e;
            }
        }
      }

      //
      // Constructor
      //
      public InsertOwnedBy() throws Exception {

          try{
              // Connect to the database
              connect = DriverManager.getConnection(
                          "jdbc:mariadb://cslab.skidmore.edu:3306/proj_bilodeau_jaber?user=zbilodea&password=pass5763");

              // Setup the prepared statement for inserts to Reserve table
              preparedStatement = connect.prepareStatement(
                          "INSERT INTO owned_by (catalogue_num, oname, date_received, date_returned) values (?,?,?,?)");
                          // "INSERT INTO owned_by (catalogue_num, oname) VALUES (catalogue_num,oname) values (?,?)");


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

      public ArrayList<Integer> getCatalogueNumsFromDB(String sqlQuery, String columnLabel) throws Exception {

          Statement getIds;
          ResultSet resultSet;
          ArrayList<Integer> resultList = new ArrayList<Integer>();

          try {
              // Run the query
              getIds = connect.createStatement();
              resultSet = getIds.executeQuery(sqlQuery);

              // Put the query results into an ArrayList<Integer>
              while(resultSet.next()) {
                  resultList.add(new Integer(resultSet.getInt(columnLabel)));
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
      public void generateOwnedBy(Integer a_indx, Integer o_indx, ArrayList<String> data, ArrayList<Integer> catalogue_nums, ArrayList<String> onames, Random random) throws Exception {
              int catalogue_num = catalogue_nums.get(a_indx);

              String oname = onames.get(o_indx);

              String date_received = data.get(a_indx*2+2);

              String date_returned = data.get(a_indx*2+3);

              // Insert the reservation into the Reserve table
              try{
                  // Set the values of the three attributes in the insert statement
                  preparedStatement.setInt(1, catalogue_num);
                  preparedStatement.setString(2, oname);
                  preparedStatement.setString(3, date_received);
                  preparedStatement.setString(4, date_returned);

// title,year_made,type,dept_name,oname,aname
                  // Execute the insert
                  preparedStatement.executeUpdate();
              }catch(Exception e) {
                  throw e;
              }

      } // end generateReservations


}
