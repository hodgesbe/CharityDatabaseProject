/**
 * Created by bHodges on 11/5/15.
 */

import java.sql.*;
import java.util.Scanner;

public class UpdateDatabase {

    static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";
    static final String DB_URL = "jdbc:mysql://montreat.cs.unca.edu:3306/charitydb";
    static final String USER = "CSCI343";
    static final String PASS = "DBMS9154";
    Connection conn = null;
    Statement stmt = null;
    Scanner sc = new Scanner(System.in);


    public UpdateDatabase() {
        try {
            Class.forName("com.mysql.jdbc.Driver");
            conn = DriverManager.getConnection(DB_URL, USER, PASS);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        System.out.println("Please enter '1' to add donor, '2' to add company, '3' to add donation, or '4' to quit: ");
        int commandNum = sc.nextInt();
        sc.nextLine();
        while (commandNum != 4){
            if (commandNum == 3){
                addDonor();
                System.out.println("Please enter '1' to add donor, '2' to add company, '3' to add donation, or '4' to quit: ");
                commandNum = sc.nextInt();
            }
        }
    }


     void addDonor() {
         System.out.println("Please enter donor's first name: ");
         String firstName = sc.nextLine();
         System.out.println("Please enter donor's last name: ");
         String lastName = sc.nextLine();
         System.out.println("Please enter donor's address: ");
         String address = sc.nextLine();
         System.out.println("Please enter donor's city: ");
         String city = sc.nextLine();
         System.out.println("Please enter donor's state: ");
         String state = sc.nextLine();
         System.out.println("Please enter donor's zip code: ");
         int zip = sc.nextInt();

         PreparedStatement pstmt = null;
         PreparedStatement pstmtAssignment = null;
         ResultSet rs = null;
         try {
             stmt = conn.createStatement();
             stmt.execute("LOCK TABLES donors WRITE");
             if (checkForDonor(firstName, lastName, address, city, state, zip)) {
                 System.out.println("Sorry, this donor already exists in database! Please enter '3' to add a new donor or '4' to quit: ");
             } else {
                 System.out.println("Adding new donor");
             }
             int donorID = maxID();

             String sqlInsert = "INSERT INTO donors(donorID, lastName, firstName, address, city, state, zip)"
                     +"VALUES(?,?,?,?,?,?,?)";
             stmt = conn.createStatement();
             pstmt = conn.prepareStatement(sqlInsert, Statement.RETURN_GENERATED_KEYS);

             pstmt.setInt(1, donorID);
             pstmt.setString(2, lastName);
             pstmt.setString(3, firstName);
             pstmt.setString(4, address);
             pstmt.setString(5, city);
             pstmt.setString(6, state);
             pstmt.setInt(7, zip);

             pstmt.executeUpdate();

             stmt.execute("UNLOCK TABLES");
             stmt.close();
             conn.close();


         } catch (SQLException e) {
             e.printStackTrace();
         } finally {
        try {
            if(rs != null)  rs.close();
            if(pstmt != null) pstmt.close();
            if(pstmtAssignment != null) pstmtAssignment.close();
            if(conn != null) conn.close();

        } catch (SQLException e) {
            System.out.println(e.getMessage());
            }
         }

     }

    boolean checkForDonor(String firstName, String lastName, String address, String city,
                       String state, int zip){
        boolean check = false;
        try {
            stmt = conn.createStatement();
            String sql;
            sql = "SELECT lastName FROM donors WHERE lastName = '"+lastName+"' AND firstName = '"+firstName+"' AND address = '"+
                    address+"' AND city = '"+city+"' AND state = '"+state+"' AND zip = '"+zip+"';";
            ResultSet rs = stmt.executeQuery(sql);
            //System.out.println(rs.getString(lastName));
            if (rs==null){
                check = true;
            }
            rs.close();
            stmt.close();
        } catch (SQLException se) {
            se.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return check;
    }

    int maxID() {
        int max = 0;
        Statement stmt = null;
        try {
            stmt = conn.createStatement();
            String sql;
            sql = "SELECT donorID FROM donors ORDER BY donorID DESC LIMIT 1 OFFSET 0;";
            ResultSet rs = stmt.executeQuery(sql);
            rs.next();
            max = rs.getInt("donorID");
            rs.close();
            stmt.close();
        } catch (SQLException se) {
            se.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return max+1;
    }





    public static void main(String[] args){
        UpdateDatabase test1 = new UpdateDatabase();

    }


}//UpdateDatabase


