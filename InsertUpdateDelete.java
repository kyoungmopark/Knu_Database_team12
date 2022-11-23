package jdbc;

import java.io.*;
import java.sql.*;
import java.util.Scanner;

public class phase3 {

    public static final String URL = "jdbc:oracle:thin:@localhost:1521:xe";
    // public static final String URL = "jdbc:oracle:thin:@localhost:1521/orclpdb1";
    public static final String USER_UNIVERSITY = "hr";
    public static final String USER_PASSWORD = "hr";

    public static void main(String[] args) throws SQLException {

        try {
            Class.forName("oracle.jdbc.driver.OracleDriver");
        } catch (ClassNotFoundException e) {
            System.err.println("Failed to load JDBC driver.");
            System.exit(1);
        }

        Connection conn = null;
        Statement stmt = null;
        PreparedStatement pstmt = null;
        String sql = "";

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
            int operation = scanner.nextInt();

            switch (operation) {
                case 1:
                	try {
                		Scanner sc = new Scanner(System.in);
                		System.out.printf("ISBN 입력: ");
                		String ISBN = sc.nextLine();
                		
                		System.out.printf("Title 입력: ");
                		String Title = sc.nextLine();
                		
                		System.out.printf("Summary 입력: ");
                		String Summary = sc.nextLine();
                		
                		System.out.printf("Language 입력: ");
                		String Language = sc.nextLine();
                		
                		System.out.printf("Library 입력: ");
                		String Library = sc.nextLine();
                		
                		System.out.printf("Price 입력: ");
                		int Price = sc.nextInt();
                		
                		System.out.printf("대출 여부 입력(대출 중 : 1, 보유  : 0): ");
                		int Is_borrowed = sc.nextInt();
                		
                		System.out.printf("Page 입력: ");
                		int Page = sc.nextInt();
              
                		
                		System.out.printf("Floor 입력: ");
                		int Floor = sc.nextInt();
                		
                		System.out.printf("Shelf number 입력: ");
                		int Shelf_number = sc.nextInt();
                		
                		sql = "INSERT INTO BOOK VALUES(?,?,?,?,?,?,?,?,?,?)";
                		pstmt = conn.prepareStatement(sql);
                		pstmt.setString(1,ISBN);
                		pstmt.setString(2,Title);
                		pstmt.setString(3,Summary);
                		pstmt.setString(4,Language);
                		pstmt.setString(5,Library);
                		pstmt.setInt(6,Price);
                		pstmt.setInt(7,Is_borrowed);
                		pstmt.setInt(8,Page);
                		pstmt.setInt(9,Floor);
                		pstmt.setInt(10, Shelf_number);
                		
                		int result = pstmt.executeUpdate();
                		if(result ==1) System.out.println("insert 성공!");
                		else System.out.println("insert 실패!");  
                	}catch (Exception e) {
                		e.printStackTrace();
                	}
                    break;
                case 2:
                	Scanner sc = new Scanner(System.in);
                	System.out.print("수정할 BOOK의 ISBN 입력:" );
                	String ISBN = sc.nextLine();
                	System.out.print("수정할 항목을 선택하세요. 1.Title 2.Summary"
                			+ "3.Language 4.Library 5.Price 6.대출 여부 7.Page "
                			+ "8.Floor 9.Shelf number");
                	String input = sc.nextLine();
                	sql = null; 
                	switch(input) {
                	case "1":
                		System.out.print("수정할 Title 입력: ");
                		String Title = sc.nextLine();
                		sql = "UPDATE BOOK SET TITLE=? WHERE ISBN=?";
                		pstmt = conn.prepareStatement(sql);
                		pstmt.setString(1, Title);
                		pstmt.setString(2,ISBN);
                		break;
                		
                	case "2":
                		System.out.print("수정할 Summary 입력: ");
                		String Summary = sc.nextLine();
                		sql = "UPDATE BOOK SET SUMMARY=? WHERE ISBN=?";
                		pstmt = conn.prepareStatement(sql);
                		pstmt.setString(1, Summary);
                		pstmt.setString(2,ISBN);
                		break;
                	case "3":
                		System.out.print("수정할 Language 입력: ");
                		String Language = sc.nextLine();
                		sql = "UPDATE BOOK SET LANGUAGE=? WHERE ISBN=?";
                		pstmt = conn.prepareStatement(sql);
                		pstmt.setString(1, Language);
                		pstmt.setString(2,ISBN);
                		break;
                	case "4":
                		System.out.print("수정할 Library 입력: ");
                		String Library = sc.nextLine();
                		sql = "UPDATE BOOK SET LIBRARY=? WHERE ISBN=?";
                		pstmt = conn.prepareStatement(sql);
                		pstmt.setString(1, Library);
                		pstmt.setString(2,ISBN);
                		break;
                	case "5":
                		System.out.print("수정할 Price 입력: ");
                		int Price = sc.nextInt();
                		sql = "UPDATE BOOK SET PRICE=? WHERE ISBN=?";
                		pstmt = conn.prepareStatement(sql);
                		pstmt.setInt(1, Price);
                		pstmt.setString(2,ISBN);
                		break;
                	case "6":
                		System.out.print("수정할 대출정보 입력(대출중 : 1, 보유중 : 0): ");
                		int Is_borrowed = sc.nextInt();
                		sql = "UPDATE BOOK SET IS_BORROWED=? WHERE ISBN=?";
                		pstmt = conn.prepareStatement(sql);
                		pstmt.setInt(1, Is_borrowed);
                		pstmt.setString(2,ISBN);
                		break;
                	case "7":
                		System.out.print("수정할 Page 입력 : ");
                		int Page = sc.nextInt();
                		sql = "UPDATE BOOK SET PAGE=? WHERE ISBN=?";
                		pstmt = conn.prepareStatement(sql);
                		pstmt.setInt(1, Page);
                		pstmt.setString(2,ISBN);
                		break;
                	case "8":
                		System.out.print("수정할 Floor 입력 : ");
                		int Floor = sc.nextInt();
                		sql = "UPDATE BOOK SET FLOOR=? WHERE ISBN=?";
                		pstmt = conn.prepareStatement(sql);
                		pstmt.setInt(1, Floor);
                		pstmt.setString(2,ISBN);
                		break;
                	case "9": 
                		System.out.print("수정할 Shelf number 입력 : ");
                		int Shelf_number = sc.nextInt();
                		sql = "UPDATE BOOK SET SHELF_NUMBER=? WHERE ISBN=?";
                		pstmt = conn.prepareStatement(sql);
                		pstmt.setInt(1, Shelf_number);
                		pstmt.setString(2,ISBN);
                		break;
                	}
                	int result = pstmt.executeUpdate();
                	if (result == 1)
                		System.out.println("update 성공!");
                	else
                		System.out.println("update 실패!");
                	
                    break;
                case 3:
                	try {
                	sc = new Scanner(System.in);
                	System.out.println("삭제할 ISBN을 입력 : ");
                	ISBN = sc.nextLine();
                	sql = "DELETE FROM BOOK WHERE ISBN=?";
                	pstmt=conn.prepareStatement(sql);
                	pstmt.setString(1, ISBN);
                	
                	result = pstmt.executeUpdate();
                	if (result == 1)
                		System.out.println("delete 성공!");
                	else
                		System.out.println("delete 실패!");
               
                	}catch (Exception e) {
                		e.printStackTrace();
                	}
                    break;
                case 4:
                	try {
                		//execute an SQL statement.
                		sc = new Scanner(System.in);
                		System.out.printf("Rating 입력: ");
                		int Rating = sc.nextInt();
                		
                		System.out.printf("Review 입력: ");
                		String Review = sc.nextLine();                
                		
                		System.out.printf("Account ID 입력: ");
                		String Account_id = sc.nextLine(); 
                		
                		System.out.printf("ISBN 입력: ");
                		String Book_id = sc.nextLine(); 
                		
                		sql = "INSERT INTO RATING VALUES(?,?,?,?)";
                		pstmt = conn.prepareStatement(sql);
                		pstmt.setInt(1,Rating);
                		pstmt.setString(2,Review);
                		pstmt.setString(3,Account_id);
                		pstmt.setString(4, Book_id);
                		
                		
                		result = pstmt.executeUpdate();
                		if(result ==1) System.out.println("insert 성공!");
                		else System.out.println("insert 실패!");  
                	}catch (Exception e) {
                		e.printStackTrace();
                	}
                    break;
                case 5:
                sc = new Scanner(System.in);
                	System.out.print("수정할 BOOK의 ISBN 입력:" );
                	String Book_id = sc.nextLine();
                	System.out.print("수정할 BOOK의 RATING을 작성한 ACCOUNT ID 입력:");
                	String Account_id = sc.nextLine();
                	System.out.print("수정할 항목을 선택하세요. 1.Rating 2.Review");
                	input = sc.nextLine();
                	sql = null; 
                	switch(input) {
                	case "1":
                		System.out.print("수정할 Rating 입력: ");
                		String Rating = sc.nextLine();
                		sql = "UPDATE RATING SET RATING=? WHERE BOOK_ID=? AND ACCOUNT_ID=?";
                		pstmt = conn.prepareStatement(sql);
                		pstmt.setString(1, Rating);
                		pstmt.setString(2,Book_id);
                		pstmt.setString(3,  Account_id);
                		break;
                		
                	case "2":
                		System.out.print("수정할 Summary 입력: ");
                		String Summary = sc.nextLine();
                		sql = "UPDATE RATING SET SUMMARY=? WHERE BOOK_ID=? AND ACCOUNT_ID=?";
                		pstmt = conn.prepareStatement(sql);
                		pstmt.setString(1, Summary);
                		pstmt.setString(2, Book_id);
                		pstmt.setString(3, Account_id);
                		break;
                	}
                	result = pstmt.executeUpdate();
                	if (result == 1)
                		System.out.println("update 성공!");
                	else
                		System.out.println("update 실패!");
               
                    break;
                case 6:
                	try {
                	sc = new Scanner(System.in);
                	System.out.println("삭제할 Rating을 작성한 Account ID 입력 : ");
                	Account_id = sc.nextLine();
                	System.out.println("삭제할 Rating에 해당하는 Book ID 입력 : ");
                	Book_id = sc.nextLine();
                	sql = "DELETE FROM RATING WHERE ACCOUNT_ID=? AND BOOK_ID=?";
                	pstmt=conn.prepareStatement(sql);
                	pstmt.setString(1, Account_id);
                	pstmt.setString(2, Book_id);
                	
                	result = pstmt.executeUpdate();
                	if (result == 1)
                		System.out.println("delete 성공!");
                	else
                		System.out.println("delete 실패!");
               
                	}catch (Exception e) {
                		e.printStackTrace();
                	}
                    break;
                case 7:
                	try {
                		sc = new Scanner(System.in);
                		System.out.printf("ID 입력: ");
                		String ID = sc.nextLine();
                		
                		System.out.printf("Password 입력: ");
                		String Password = sc.nextLine();
                		
                		System.out.printf("Name 입력: ");
                		String Name = sc.nextLine();
                		
                		System.out.printf("Email 입력: ");
                		String Email = sc.nextLine();
                		
                		System.out.printf("Phone number 입력: ");
                		String Phone = sc.nextLine();
                		
             
                		
                		sql = "INSERT INTO ACCOUNT VALUES(?,?,?,?,?)";
                		pstmt = conn.prepareStatement(sql);
                		pstmt.setString(1,ID);
                		pstmt.setString(2,Password);
                		pstmt.setString(3,Name);
                		pstmt.setString(4,Email);
                		pstmt.setString(5,Phone);
                		
                		result = pstmt.executeUpdate();
                		if(result ==1) System.out.println("insert 성공!");
                		else System.out.println("insert 실패!");  
                	}catch (Exception e) {
                		e.printStackTrace();
                	}
                    break;
                case 8:
                	sc = new Scanner(System.in);
                	System.out.print("수정할 ACCOUNT의 ID 입력:" );
                	String ID = sc.nextLine();
                	System.out.print("수정할 ACCOUNT의 PASSWORD 입력: ");
                	String Password = sc.nextLine();
                	System.out.print("수정할 항목을 선택하세요. 1.Name 2.Email 3.Phone");
                	input = sc.nextLine();
                	sql = null; 
                	switch(input) {
                	case "1":
                		System.out.print("수정할 Name 입력: ");
                		String Name = sc.nextLine();
                		sql = "UPDATE ACCOUNT SET NAME=? WHERE ID=? AND PASSWORD=?";
                		pstmt = conn.prepareStatement(sql);
                		pstmt.setString(1, Name);
                		pstmt.setString(2, ID);
                		pstmt.setString(3, Password);
                		break;
                		
                	case "2":
                		System.out.print("수정할 Email 입력: ");
                		String Email = sc.nextLine();
                		sql = "UPDATE ACCOUNT SET EMAIL=? WHERE ID=? AND PASSWORD=?";
                		pstmt = conn.prepareStatement(sql);
                		pstmt.setString(1, Email);
                		pstmt.setString(2, ID);
                		pstmt.setString(3, Password);
                		break;
                		
                	case "3":
                		System.out.print("수정할 Phone 입력: ");
                		String Phone = sc.nextLine();
                		sql = "UPDATE ACCOUNT SET PHONE=? WHERE ID=? AND PASSWORD=?";
                		pstmt = conn.prepareStatement(sql);
                		pstmt.setString(1, Phone);
                		pstmt.setString(2, ID);
                		pstmt.setString(3, Password);
                		break;
                	}
                	result = pstmt.executeUpdate();
                	if (result == 1)
                		System.out.println("update 성공!");
                	else
                		System.out.println("update 실패!");
               
                    break;
                case 9:
                	try {
                	sc = new Scanner(System.in);
                	System.out.println("삭제할 Account의 ID을 입력 : ");
                	ID = sc.nextLine();
                	System.out.println("삭제할 Account의 Password을 입력 : ");
                	Password = sc.nextLine();
                	sql = "DELETE FROM ACCOUNT WHERE ID=? AND PASSWORD =?";
                	pstmt=conn.prepareStatement(sql);
                	pstmt.setString(1, ID);
                	pstmt.setString(2, Password);
                	
                	result = pstmt.executeUpdate();
                	if (result == 1)
                		System.out.println("delete 성공!");
                	else
                		System.out.println("delete 실패!");
               
                	}catch (Exception e) {
                		e.printStackTrace();
                	}
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
        try {
            stmt.close();
            conn.close();
        } catch (SQLException e) {
            System.err.println("Failed to clear objects: " + e.getLocalizedMessage());
            System.exit(1);
        }
    }
    }
}