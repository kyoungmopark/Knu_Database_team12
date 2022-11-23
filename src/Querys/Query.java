package Querys;

import java.sql.*;
import java.util.Scanner;

public class Query {
	// oracle database user name, password -> url
	public static final String URL = "jdbc:oracle:thin:@localhost:1521:orcl";
	public static final String USER_UNIVERSITY ="pbook";
	public static final String USER_PASSWORD ="pbook322";
			
	public static void main(String[] args) {
		/*******************************************************
		 ******************* 1) CONNECTION  *******************
		 *******************************************************/
		Connection conn = null; // Connection object
		Statement stmt = null;	// Statement object

		try {
			// Load a JDBC driver for Oracle DBMS
			Class.forName("oracle.jdbc.driver.OracleDriver");
			// Get a Connection object 
			System.out.println("Get a Connection object: Success!");
		}catch(ClassNotFoundException e) {
			System.err.println("error = " + e.getMessage());
			System.exit(1);
		}

		// Make a connection
		try{
			conn = DriverManager.getConnection(URL, USER_UNIVERSITY, USER_PASSWORD); 
			System.out.println("Connected.");
		}catch(SQLException ex) {
			ex.printStackTrace();
			System.err.println("Cannot get a connection: " + ex.getLocalizedMessage());
			System.err.println("Cannot get a connection: " + ex.getMessage());
			System.exit(1);
		}
		
		try {
			conn.setAutoCommit(false); // auto-commit disabled  
			// Create a statement object
			stmt = conn.createStatement();
		}catch(SQLException ex) {
			System.err.println("sql error = " + ex.getMessage());
			System.exit(1);
		}


		/******************************************************* 
		 ********************Start Query************************
		 *******************************************************/
		
		Scanner sc = new Scanner(System.in);
		
		System.out.print("쿼리 선택: ");
		int queryNumber = sc.nextInt();
		
		switch(queryNumber) {
			case 1: query1(conn, stmt); break;
			case 2: query2(conn, stmt); break;
			case 3: query3(conn, stmt); break;
			case 4: query4(conn, stmt); break;
			case 5: query5(conn, stmt); break;
			case 6: query6(conn, stmt); break;
			case 7: query7(conn, stmt); break;
			case 8: query8(conn, stmt); break;
			case 9: query9(conn, stmt); break;
			case 10: query10(conn, stmt); break;
			default: break;
		}
		
		/*******************************************************/

		// Release database resources.
		try {
			// Close the Statement object.
			stmt.close(); 
			// Close the Connection object.
			conn.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static void query1(Connection conn, Statement stmt) {
		Scanner sc = new Scanner(System.in);

		System.out.print("작가 이름을 입력:");
		String buffer = sc.nextLine();
		
		ResultSet rs = null;
		try {
			String query = "select book.title 제목, author.name 작가, book.is_borrowed 대출여부, book.library 도서관, book.floor 층, book.shelf_number 서가번호 "
					+ "from book, authored, author "
					+ "where book.isbn = authored.book_id and authored.author_id = author.id "
					+ "and author.name like '%" + buffer + "%' ";
			rs = stmt.executeQuery(query);
			System.out.println("<< query 1 result >>");
			ResultSetMetaData rsmd = rs.getMetaData();
			int cnt = rsmd.getColumnCount();
			for(int i=1; i <= cnt; i++) {
				System.out.print(rsmd.getColumnName(i));
				if(i+1 <= cnt) {
					System.out.print(" | ");
				}
			}
			System.out.println("\n-------------------------------");

			while(rs.next()) {
				// Fill out your code
				System.out.println(rs.getString(1)+" | "+rs.getString(2)+" | "
									+rs.getString(3)+" | "+rs.getString(4)+" | "
									+rs.getString(5)+" | "+rs.getString(6));
			}
			rs.close();
			
			System.out.println();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static void query2(Connection conn, Statement stmt) {
		System.out.println("사용자 대출 날짜와 반납 날짜");
		
		ResultSet rs = null;
		try {
			String query = "select book.title 제목, account.name 대출자, borrow.borrow_date 대출날짜, borrow.return_date 반납날짜 "
					+ "from borrow, book, account "
					+ "where borrow.book_id = book.isbn and borrow.account_id = account.id "
					+ "order by borrow.return_date DESC ";
			rs = stmt.executeQuery(query);
			System.out.println("<< query 2 result >>");
			ResultSetMetaData rsmd = rs.getMetaData();
			int cnt = rsmd.getColumnCount();
			for(int i=1; i <= cnt; i++) {
				System.out.print(rsmd.getColumnName(i));
				if(i+1 <= cnt) {
					System.out.print(" | ");
				}
			}
			System.out.println("\n-------------------------------");

			while(rs.next()) {
				// Fill out your code
				System.out.println(rs.getString(1)+" | "+rs.getString(2)+" | "
									+rs.getString(3)+" | "+rs.getString(4));
			}
			rs.close();
			
			System.out.println();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static void query3(Connection conn, Statement stmt) {
		System.out.println("책을 가장 많이 출판한 출판사순으로 정렬");
		
		ResultSet rs = null;
		try {
			String query = "select publisher.name 출판사, count(*) 책_수 "
					+ "from publisher, published_by, book "
					+ "where publisher.id = published_by.pub_id and published_by.book_id = book.isbn "
					+ "group by publisher.name order by 책_수 DESC ";
			rs = stmt.executeQuery(query);
			System.out.println("<< query 3 result >>");
			ResultSetMetaData rsmd = rs.getMetaData();
			int cnt = rsmd.getColumnCount();
			for(int i=1; i <= cnt; i++) {
				System.out.print(rsmd.getColumnName(i));
				if(i+1 <= cnt) {
					System.out.print(" | ");
				}
			}
			System.out.println("\n-------------------------------");

			while(rs.next()) {
				// Fill out your code
				System.out.println(rs.getString(1)+" | "+rs.getString(2));
			}
			rs.close();
			
			System.out.println();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static void query4(Connection conn, Statement stmt) {
		System.out.println("모든 리뷰 평균 점수보다 높은 평점을 받은 책들");
		
		ResultSet rs = null;
		try {
			String query = "select book.title 제목, author.name 작가, rating.rating 평점 "
					+ "from rating, book, author, authored "
					+ "where author.id = authored.author_id and authored.book_id = book.isbn and book.isbn = rating.book_id "
					+ "and rating.rating > (select avg(r.rating) from rating r) ";
			rs = stmt.executeQuery(query);
			System.out.println("<< query 4 result >>");
			ResultSetMetaData rsmd = rs.getMetaData();
			int cnt = rsmd.getColumnCount();
			for(int i=1; i <= cnt; i++) {
				System.out.print(rsmd.getColumnName(i));
				if(i+1 <= cnt) {
					System.out.print(" | ");
				}
			}
			System.out.println("\n-------------------------------");

			while(rs.next()) {
				// Fill out your code
				System.out.println(rs.getString(1)+" | "+rs.getString(2)+" | "
									+rs.getString(3));
			}
			rs.close();
			
			System.out.println();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static void query5(Connection conn, Statement stmt) {
		Scanner sc = new Scanner(System.in);

		System.out.println("입력한 작가의 책들 중 리뷰가 있는 책만 보여주기");
		System.out.print("작가 이름 입력: ");
		String buffer = sc.next();
		
		ResultSet rs = null;
		try {
			String query = "select book.title 제목, author.name 작가  "
					+ "from book, author, authored "
					+ "where book.isbn = authored.book_id and authored.author_id = author.id "
					+ "and author.name like '%" + buffer + "%' "
					+ "and EXISTS(select book.isbn from rating where book.isbn = rating.book_id) ";

			rs = stmt.executeQuery(query);
			System.out.println("<< query 5 result >>");
			ResultSetMetaData rsmd = rs.getMetaData();
			int cnt = rsmd.getColumnCount();
			for(int i=1; i <= cnt; i++) {
				System.out.print(rsmd.getColumnName(i));
				if(i+1 <= cnt) {
					System.out.print(" | ");
				}
			}
			System.out.println("\n-------------------------------");

			while(rs.next()) {
				// Fill out your code
				System.out.println(rs.getString(1)+" | "+rs.getString(2));
			}
			rs.close();
			
			System.out.println();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	// 여섯
	public static void query6(Connection conn, Statement stmt) {
		Scanner sc = new Scanner(System.in);

		System.out.println("번역된 책(대출 안된)");
		System.out.print("원본 언어:");
		String originalLan = sc.next();
		System.out.print("번역 언어:");
		String transLan = sc.next();
		
		ResultSet rs = null;
		try {
			String query = "select book.title 제목, book.library 도서관, book.floor 층, book.shelf_number 서기번호 "
					+ "from translator, translated, book "
					+ "where translator.id = translated.translator_id and translated.book_id = book.isbn and book.is_borrowed = 0 "
					+ "and book.language = '"+originalLan+"' "
					+ "and translator.language = '"+transLan+"' ";
			rs = stmt.executeQuery(query);
			System.out.println("<< query 6 result >>");
			ResultSetMetaData rsmd = rs.getMetaData();
			int cnt = rsmd.getColumnCount();
			for(int i=1; i <= cnt; i++) {
				System.out.print(rsmd.getColumnName(i));
				if(i+1 <= cnt) {
					System.out.print(" | ");
				}
			}
			System.out.println("\n-------------------------------");

			while(rs.next()) {
				// Fill out your code
				System.out.println(rs.getString(1)+" | "+rs.getString(2)+" | "
									+rs.getString(3)+" | "+rs.getString(4));
			}
			rs.close();
			
			System.out.println();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	// 수정
	public static void query7(Connection conn, Statement stmt) {
		Scanner sc = new Scanner(System.in);

		System.out.println("도서과, 장르 입력하면 리뷰가 있는 책을 보여준다:");
		System.out.print("도서과 이름:");
		String lib = sc.next();
		System.out.print("장르:");
		String genre = sc.next();
		
		ResultSet rs = null;
		try {
			String query = "select book.title 제목, book.is_borrowed 대출여부, book.library 도서관, book.floor 층, book.shelf_number 서기번호 "
					+ "from genre, belong, book, rating "
					+ "where genre.id = belong.genre_id and belong.book_id = book.isbn and rating.book_id = book.isbn "
					+ "and genre.genre = '"+genre+"' "
					+ "and book.library = '"+lib+"' ";
			rs = stmt.executeQuery(query);
			System.out.println("<< query 7 result >>");
			ResultSetMetaData rsmd = rs.getMetaData();
			int cnt = rsmd.getColumnCount();
			for(int i=1; i < cnt; i++) {
				System.out.print(rsmd.getColumnName(i));
				if(i+1 < cnt) {
					System.out.print(" | ");
				}
			}
			System.out.println("\n-------------------------------");

			while(rs.next()) {
				// Fill out your code
				System.out.println(rs.getString(1)+" | "+rs.getString(2)+" | "
									+rs.getString(3)+" | "+rs.getString(4)+" | "
									+rs.getString(5));
			}
			rs.close();
			
			System.out.println();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static void query8(Connection conn, Statement stmt) {
		Scanner sc = new Scanner(System.in);

		System.out.println("특정 언어로 자필된 책의 장르별 책 수 보여주기");
		System.out.print("언어 입력:");
		String buffer = sc.nextLine();
		
		ResultSet rs = null;
		try {
			String query = "select genre.genre 장르, count(*) 책_수 "
					+ "from book, genre, belong "
					+ "where book.isbn = belong.book_id and belong.genre_id = genre.id "
					+ "and book.language = '" + buffer + "' "
					+ "group by genre.genre "
					+ "order by 책_수 DESC ";
			rs = stmt.executeQuery(query);
			System.out.println("<< query 8 result >>");
			ResultSetMetaData rsmd = rs.getMetaData();
			int cnt = rsmd.getColumnCount();
			for(int i=1; i <= cnt; i++) {
				System.out.print(rsmd.getColumnName(i));
				if(i+1 <= cnt) {
					System.out.print(" | ");
				}
			}
			System.out.println("\n-------------------------------");

			while(rs.next()) {
				// Fill out your code
				System.out.println(rs.getString(1)+" | "+rs.getString(2));
			}
			rs.close();
			
			System.out.println();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}	

	public static void query9(Connection conn, Statement stmt) {
		System.out.println("리뷰 많이 남긴 책 순위");
		
		ResultSet rs = null;
		try {
			String query = "select book.title 제목, count(book.title) 리뷰_수 "
					+ "from book, rating "
					+ "where book.isbn = rating.book_id "
					+ "group by book.title "
					+ "order by 리뷰_수 DESC ";
			rs = stmt.executeQuery(query);
			System.out.println("<< query 9 result >>");
			ResultSetMetaData rsmd = rs.getMetaData();
			int cnt = rsmd.getColumnCount();
			for(int i=1; i <= cnt; i++) {
				System.out.print(rsmd.getColumnName(i));
				if(i+1 <= cnt) {
					System.out.print(" | ");
				}
			}
			System.out.println("\n-------------------------------");

			while(rs.next()) {
				// Fill out your code
				System.out.println(rs.getString(1)+" | "+rs.getString(2));
			}
			rs.close();
			
			System.out.println();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}	

	public static void query10(Connection conn, Statement stmt) {
		Scanner sc = new Scanner(System.in);

		System.out.println("입력한 도서관에 있는 책의 목록");

		System.out.print("첫째 도서관 이름:");
		String lib1 = sc.next();
		System.out.print("첫째 도서관 층:");
		int flr1 = sc.nextInt();
		System.out.print("첫째 도서관 서가번호:");
		int sn1 = sc.nextInt();
		
		System.out.print("둘째 도서관 이름:");
		String lib2 = sc.next();
		System.out.print("둘째 도서관 층:");
		int flr2 = sc.nextInt();
		System.out.print("둘째 도서관 서가번호:");
		int sn2 = sc.nextInt();
		
		ResultSet rs = null;
		try {
			String query = "select b.title 제목, b.language 언어, b.library 도서관, b.floor 층, b.shelf_number 서가번호 from book b "
					+ "where b.library like '%" + lib1 + "%' and b.floor = " + flr1 + " and b.shelf_number = "+sn1+" "
					+ "union "
					+ "select bb.title, bb.language, bb.library, bb.floor, bb.shelf_number from book bb "
					+ "where bb.library like '%" + lib2 + "%' and bb.floor = " + flr2 + " and bb.shelf_number = "+sn2+" ";
			rs = stmt.executeQuery(query);

			System.out.println("<< query 10 result >>");
			ResultSetMetaData rsmd = rs.getMetaData();
			int cnt = rsmd.getColumnCount();
			for(int i=1; i <= cnt; i++) {
				System.out.print(rsmd.getColumnName(i));
				if(i+1 <= cnt) {
					System.out.print(" | ");
				}
			}
			System.out.println("\n-------------------------------");

			while(rs.next()) {
				// Fill out your code
				System.out.println(rs.getString(1)+" | "+rs.getString(2)+" | "
									+rs.getString(3)+" | "+rs.getString(4)+" | "
									+rs.getString(5));
			}
			rs.close();
			
			System.out.println();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}


