package phase3;

import java.io.*;
import java.sql.*;

public class Phase3JDBC {

    public static final String URL = "jdbc:oracle:thin:@localhost:1521:orcl";
    // public static final String URL = "jdbc:oracle:thin:@localhost:1521/orclpdb1";
    public static final String USER_UNIVERSITY = "university";
    public static final String USER_PASSWORD = "comp322";

    public static void main(String[] args) {

        try {
            Class.forName("oracle.jdbc.driver.OracleDriver");
        } catch (ClassNotFoundException e) {
            System.err.println("Failed to load JDBC driver.");
            System.exit(1);
        }

        Connection conn = null;
        Statement stmt = null;

        try {
            conn = DriverManager.getConnection(URL, USER_UNIVERSITY, USER_PASSWORD);
            conn.setAutoCommit(false);
            stmt = conn.createStatement();
        } catch (SQLException e) {
            System.err.println("Failed to connect to DBMS: " + e.getLocalizedMessage());
            System.exit(1);
        }

        // CLI loop
        ResultSet rs = null;
        try {
            String sql = null;

            // Q1: Complete your query.
            sql =
                "SELECT E.Sex, AVG(Salary) AS Avg FROM EMPLOYEE E, DEPENDENT D WHERE\n" +
                "Ssn = Essn AND Relationship = 'Spouse'\n" +
                "GROUP BY E.Sex ORDER BY Avg ASC";
            rs = stmt.executeQuery(sql);
            System.out.println("<< query 1 result >>");
            System.out.println("Sex | Avg_salary");
            while (rs.next()) {
                String sex = rs.getString(1);
                float salary = rs.getFloat(2);
                System.out.println(String.format("%s | %.3f", sex, salary));
            }
            rs.close();

            System.out.println();

            // Q2: Complete your query.
            sql =
                "SELECT Ssn, Lname, Fname, Salary FROM EMPLOYEE E WHERE\n" +
                "NOT EXISTS (\n" +
                "   (SELECT Pnumber FROM PROJECT WHERE Dnum = 4)\n" +
                "   MINUS\n" +
                "   (SELECT Pnumber FROM PROJECT, WORKS_ON WHERE Pnumber = Pno AND Essn = E.Ssn)\n" +
                ") ORDER BY Salary DESC";
            rs = stmt.executeQuery(sql);
            System.out.println("<< query 2 result >>");
            System.out.println("Ssn | Lname | Fname | Salary");
            while (rs.next()) {
                String ssn = rs.getString(1);
                String lName = rs.getString(2);
                String fName = rs.getString(3);
                float salary = rs.getFloat(4);
                System.out.println(String.format("%s | %s | %s | %f", ssn, lName, fName, salary));
            }
            rs.close();

            System.out.println();

            // Q3: Complete your query.
            sql =
                "SELECT Dname, Pname, Lname, Fname FROM EMPLOYEE\n" +
                "FULL OUTER JOIN WORKS_ON ON Ssn = Essn\n" +
                "FULL OUTER JOIN PROJECT ON Pnumber = Pno\n" +
                "FULL OUTER JOIN DEPARTMENT ON Dnumber = Dnum\n" +
                "WHERE Plocation = 'Stafford'\n" +
                "ORDER BY Dnum ASC, Pname ASC";
            rs = stmt.executeQuery(sql);
            System.out.println("<< query 3 result >>");
            System.out.println("Dname | Pname | Lname | Fname");
            while (rs.next()) {
                String dName = rs.getString(1);
                String pName = rs.getString(2);
                String lName = rs.getString(3);
                String fName = rs.getString(4);
                System.out.println(String.format("%s | %s | %s | %s", dName, pName, lName, fName));
            }

            rs.close();
        } catch (SQLException e) {
            System.err.println("Something went wrong processing task2: " + e.getLocalizedMessage());
            System.exit(1);
        }

        try {
            stmt.close();
            conn.close();
        } catch (SQLException e) {
            System.err.println("Failed to clear objects: " + e.getLocalizedMessage());
            System.exit(1);
        }
    }
}
