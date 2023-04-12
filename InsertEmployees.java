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

public class InsertEmployees {

      // Database connection
      private Connection connect = null;

      // Prepared Statement
      // This allows insert queries to run faster than issuing a new SQL
      // statement for each individual insert
      private PreparedStatement preparedStatement = null;

      public static void main(String[] args) throws Exception {

        InsertEmployees ie;
        Random random = new Random();
        int numWorks;
        ArrayList<String> data = new ArrayList<String>();

        try{
          ie = new InsertEmployees();
        } catch(Exception e) {
          throw e;
        }

        try {
          File myObj = new File("employee.txt");
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


        // Get the list of IDs from the database
        String deptQuery = "Select dept_name from department";
        ArrayList<String> dept_names = ie.getIdsFromDB(deptQuery, "dept_name");

// just need at least as many artworks as artists
        for(Integer i = 0; i < Integer.parseInt(data.get(data.size()-2)); i++) {
            try{
                // Call the generateReservations method to insert data into
                // Reserves for this sailor
                ie.generateEmployees(3*(i+1), data, dept_names, random);
            } catch(Exception e) {
                throw e;
            }
        }
      }

      //
      // Constructor
      //
      public InsertEmployees() throws Exception {

          try{

              // Connect to the database
              connect = DriverManager.getConnection(
                          "jdbc:mariadb://cslab.skidmore.edu:3306/proj_bilodeau_jaber?user=zbilodea&password=pass5763");

              // Setup the prepared statement for inserts to Reserve table
              preparedStatement = connect.prepareStatement(
                          "INSERT INTO employee (ename, id_num, salary, dept_name) values (?,?,?,?)");

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

      public ArrayList<Integer> getIdNumsFromDB(String sqlQuery, String columnLabel) throws Exception {

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
      public void generateEmployees(Integer start_indx, ArrayList<String> data, ArrayList<String> dept_names, Random random) throws Exception {
              int n = random.nextInt(dept_names.size());
              String dept_name = dept_names.get(n);

              String ename = data.get(start_indx);
              Integer id_num = Integer.parseInt(data.get(start_indx+1));
              Integer salary = Integer.parseInt(data.get(start_indx+2));
              try{
                  // Set the values of the three attributes in the insert statement
                  preparedStatement.setString(1, ename);
                  preparedStatement.setInt(2, id_num);
                  preparedStatement.setInt(3, salary);
                  preparedStatement.setString(4, dept_name);

                  // Execute the insert
                  preparedStatement.executeUpdate();

              }catch(Exception e) {
                  throw e;
              }
        }
    } // end generateReservations
