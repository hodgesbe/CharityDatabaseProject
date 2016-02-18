/**
 *
 * Program by: Brett Hodges
 * Template by: Marietta E. Cameron
 * Date: 11/2/15
 * Outputs all non-contributing donors.
 *
 */
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.*;

public class ConnectToDatabase {

    // JDBC driver name and database URL

    static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";
    static final String DB_URL = "jdbc:mysql://montreat.cs.unca.edu:3306/charitydb";

    //  Database credentials
    static final String USER = "CSCI343";
    static final String PASS = "DBMS9154";

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws IOException {
        Connection conn = null;
        Statement stmt = null;
        FileWriter fileWriter = new FileWriter("/Users/bHodges/Desktop/non-donors.txt");
        BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);

        try {
            //STEP 2: Register JDBC driver
            Class.forName("com.mysql.jdbc.Driver");

            //STEP 3: Open a connection
            System.out.println("Connecting to database...");
            conn = DriverManager.getConnection(DB_URL, USER, PASS);
            /**
             * Working Query:
             *
             * SELECT lastName, firstName, address, city, state, zip
             * FROM donors
             * WHERE donorID NOT IN (SELECT donorID FROM donations)
             * ORDER BY lastName ASC;
             *
             */
            //STEP 4: Execute a query
            System.out.println("Creating statement...");
            stmt = conn.createStatement();
            String sql;
            sql = "SELECT lastName, firstName, address, city, state, zip " +
                    "FROM donors " +
                    "WHERE donorID NOT IN (SELECT donorID FROM donations) " +
                    "ORDER BY lastName ASC;";
            ResultSet rs = stmt.executeQuery(sql);

            //STEP 5: Extract data from result set
            while (rs.next()) {
                //Retrieve by column name
                String last = rs.getString("lastName");
                String first = rs.getString("firstName");
                String address = rs.getString("address");
                String city = rs.getString("city");
                String state = rs.getString("state");
                String zip = rs.getString("zip");


                //Display values
                bufferedWriter.write(first+" "+last+"\n");
                bufferedWriter.write(address+"\n");
                bufferedWriter.write(city+" "+state+", "+zip+"\n");
                bufferedWriter.newLine();
            }
            //STEP 6: Clean-up environment
            bufferedWriter.close();
            rs.close();
            stmt.close();
            conn.close();
        } catch (SQLException se) {
            //Handle errors for JDBC
            se.printStackTrace();
        } catch (Exception e) {
            //Handle errors for Class.forName
            e.printStackTrace();
        } finally {
            //finally block used to close resources
            try {
                if (stmt != null) {
                    stmt.close();
                }
            } catch (SQLException se2) {
            }// nothing we can do
            try {
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException se) {
                se.printStackTrace();
            }//end finally try
        }//end try

    }//main

}//ConnectToDatabase
