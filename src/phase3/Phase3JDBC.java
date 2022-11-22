package phase3;

import java.io.*;
import java.sql.*;
import java.util.Scanner;

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
        Scanner scanner = new Scanner(System.in);
        while (true) {
            System.out.println("1. Insert new Book.");
            System.out.println("2. Update existing Book.");
            System.out.println("3. Delete existing Book.");

            System.out.println("4. Insert new Review.");
            System.out.println("5. Update existing Review.");
            System.out.println("6. Delete existing Review.");

            System.out.println("7. Insert new Account.");
            System.out.println("8. Update existing Account.");
            System.out.println("9. Delete existing Account.");

            System.out.println("10. Insert new Admin.");
            System.out.println("11. Update existing Admin.");
            System.out.println("12. Delete existing Admin.");

            System.out.println("13. Insert new Genre.");
            System.out.println("14. Update existing Genre.");
            System.out.println("15. Delete existing Genre.");

            System.out.println("16. Insert new Publisher.");
            System.out.println("17. Update existing Publisher.");
            System.out.println("18. Delete existing Publisher.");

            System.out.println("19. Insert new Translator.");
            System.out.println("20. Update existing Translator.");
            System.out.println("21. Delete existing Translator.");

            System.out.println("22. Insert new Author.");
            System.out.println("23. Update existing Author.");
            System.out.println("24. Delete existing Author.");

            System.out.println("25. Exit.");

            System.out.println("Select what to do: ");
            int operation = scanner.nextLine();

            switch (operation) {
                case 1:
                    break;
                case 2:
                    break;
                case 3:
                    break;
                case 4:
                    break;
                case 5:
                    break;
                case 6:
                    break;
                case 7:
                    break;
                case 8:
                    break;
                case 9:
                    break;
                case 10:
                    break;
                case 11:
                    break;
                case 12:
                    break;
                case 13:
                    break;
                case 14:
                    break;
                case 15:
                    break;
                case 16:
                    break;
                case 17:
                    break;
                case 18:
                    break;
                case 19:
                    break;
                case 20:
                    break;
                case 20:
                    break;
                case 21:
                    break;
                case 22:
                    break;
                case 23:
                    break;
                case 24:
                    break;
                case 25:
                    break;
                default:
                    System.out.println("Invalid operation selected! Select again.");
                    break;
            }
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
