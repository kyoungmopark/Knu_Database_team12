package phase3;

import java.io.*;
import java.sql.*;
import java.util.Scanner;

public class Phase3JDBC {

    public static final String URL = "jdbc:oracle:thin:@localhost:1521:orcl";
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

        PreparedStatement pstmt = null;
        String sql = "";

        Scanner scanner = new Scanner(System.in);

        try {
            conn = DriverManager.getConnection(URL, USER_UNIVERSITY, USER_PASSWORD);
            conn.setAutoCommit(false);
            stmt = conn.createStatement();
        } catch (SQLException e) {
            System.err.println("Failed to connect to DBMS: " + e.getLocalizedMessage());
            System.exit(1);
        }

cliLoop: while (true) {
            System.out.println("1. Insert new Book.");
            System.out.println("2. Update existing Book.");
            System.out.println("3. Delete existing Book.");

            System.out.println("4. Insert new Review.");
            System.out.println("5. Update existing Review.");
            System.out.println("6. Delete existing Review.");

            System.out.println("7. Insert new Account.");
            System.out.println("8. Update existing Account.");
            System.out.println("9. Delete existing Account.");

            System.out.println("10. Query data.");

            System.out.println("11. Exit.");

            System.out.println("Select what to do: ");
            int operation = Integer.parseInt(scanner.nextLine().trim());

            switch (operation) {
            case 1:
                try {
                    System.out.printf("ISBN 입력: ");
                    String ISBN = scanner.nextLine().trim();

                    System.out.printf("Title 입력: ");
                    String Title = scanner.nextLine().trim();

                    System.out.printf("Summary 입력: ");
                    String Summary = scanner.nextLine().trim();

                    System.out.printf("Language 입력: ");
                    String Language = scanner.nextLine().trim();

                    System.out.printf("Library 입력: ");
                    String Library = scanner.nextLine().trim();

                    System.out.printf("Price 입력: ");
                    int Price = Integer.parseInt(scanner.nextLine().trim());

                    System.out.printf("대출 여부 입력(대출 중 : 1, 보유  : 0): ");
                    int Is_borrowed = Integer.parseInt(scanner.nextLine().trim());

                    System.out.printf("Page 입력: ");
                    int Page = Integer.parseInt(scanner.nextLine().trim());

                    System.out.printf("Floor 입력: ");
                    int Floor = Integer.parseInt(scanner.nextLine().trim());

                    System.out.printf("Shelf number 입력: ");
                    int Shelf_number = Integer.parseInt(scanner.nextLine().trim());

                    int result = stmt.executeUpdate(
                        String.format(
                            "INSERT INTO BOOK VALUES('%s', '%s', '%s', '%s', %s, %s, %s, '%s', %s, %s)",
                            ISBN, Title, Summary, Language, Price, Is_borrowed, Page, Library, Floor, Shelf_number));
                    if (result == 1)
                        System.out.println("insert 성공!");
                    else
                        System.out.println("insert 실패!");

                    conn.commit();
                } catch (Exception e) {
                    System.out.println("Failed case 1.");
                    e.printStackTrace();
                }
                break;
            case 2:
                try {
                    System.out.print("수정할 BOOK의 ISBN 입력:" );
                    String ISBN = scanner.nextLine().trim();

                    System.out.printf("Title 입력: ");
                    String Title = scanner.nextLine().trim();

                    System.out.printf("Summary 입력: ");
                    String Summary = scanner.nextLine().trim();

                    System.out.printf("Language 입력: ");
                    String Language = scanner.nextLine().trim();

                    System.out.printf("Library 입력: ");
                    String Library = scanner.nextLine().trim();

                    System.out.printf("Price 입력: ");
                    int Price = Integer.parseInt(scanner.nextLine().trim());

                    System.out.printf("대출 여부 입력(대출 중 : 1, 보유  : 0): ");
                    int Is_borrowed = Integer.parseInt(scanner.nextLine().trim());

                    System.out.printf("Page 입력: ");
                    int Page = Integer.parseInt(scanner.nextLine().trim());

                    System.out.printf("Floor 입력: ");
                    int Floor = Integer.parseInt(scanner.nextLine().trim());

                    System.out.printf("Shelf number 입력: ");
                    int Shelf_number = Integer.parseInt(scanner.nextLine().trim());

                    int result = stmt.executeUpdate(
                        String.format(
                            "UPDATE BOOK\n" +
                                "SET Title = '%s', Summary = '%s', Language = '%s', Library = '%s', Price = %s\n" +
                                ", Is_borrowed = %s, Page = %s, Floor = %s, Shelf_number = %s\n" +
                                "WHERE ISBN = '%s'",
                            Title, Summary, Language, Library, Price, Is_borrowed, Page, Floor, Shelf_number, ISBN));
                    if (result == 0)
                        System.out.println("update 실패!");
                    else
                        System.out.println("update 성공!");

                    conn.commit();
                } catch (Exception e) {
                    System.out.println("Failed case 2: " + e.getLocalizedMessage());
                    e.printStackTrace();
                }
                break;
            case 3:
                try {
                    System.out.printf("삭제할 ISBN을 입력: ");
                    String ISBN = scanner.nextLine().trim();

                    int result = stmt.executeUpdate(
                        String.format("DELETE FROM BOOK WHERE ISBN = '%s'", ISBN));
                    if (result == 1)
                        System.out.println("delete 성공!");
                    else
                        System.out.println("delete 실패!");

                    conn.commit();
                } catch (Exception e) {
                    System.out.println("Failed case 3.");
                    e.printStackTrace();
                }
                break;
            case 4:
                try {
                    System.out.printf("Rating 입력: ");
                    int Rating = Integer.parseInt(scanner.nextLine().trim());

                    System.out.printf("Review 입력: ");
                    String Review = scanner.nextLine().trim();

                    System.out.printf("Account ID 입력: ");
                    String Account_id = scanner.nextLine().trim();

                    System.out.printf("ISBN 입력: ");
                    String Book_id = scanner.nextLine().trim();

                    int result = stmt.executeUpdate(
                        String.format(
                            "INSERT INTO RATING VALUES(%s, '%s', '%s', '%s')",
                            Rating, Review, Book_id, Account_id));
                    if (result ==1)
                        System.out.println("insert 성공!");
                    else
                        System.out.println("insert 실패!");

                    conn.commit();
                } catch (Exception e) {
                    System.out.println("Failed case 4.");
                    e.printStackTrace();
                }
                break;
            case 5:
                try {
                    System.out.printf("ISBN 입력: ");
                    String Book_id = scanner.nextLine().trim();

                    System.out.printf("Account ID 입력: ");
                    String Account_id = scanner.nextLine().trim();

                    System.out.printf("Rating 입력: ");
                    int Rating = Integer.parseInt(scanner.nextLine().trim());

                    System.out.printf("Review 입력: ");
                    String Review = scanner.nextLine().trim();

                    int result = stmt.executeUpdate(
                        String.format(
                            "UPDATE RATING\n" +
                                "SET Rating = %s, Review = '%s' WHERE Book_id = '%s' AND Account_id = '%s'",
                            Rating, Review, Book_id, Account_id));
                    if (result == 0)
                        System.out.println("update 실패!");
                    else
                        System.out.println("update 성공!");

                    conn.commit();
                } catch (Exception e) {
                    System.out.println("Failed case 5.");
                    e.printStackTrace();
                }
                break;
            case 6:
                try {
                    System.out.printf("삭제할 Rating에 해당하는 Book ID 입력: ");
                    String Book_id = scanner.nextLine().trim();

                    System.out.printf("삭제할 Rating을 작성한 Account ID 입력: ");
                    String Account_id = scanner.nextLine().trim();

                    int result = stmt.executeUpdate(
                        String.format(
                            "DELETE FROM RATING WHERE Book_id = '%s' AND Account_id = '%s'",
                            Book_id, Account_id));
                    if (result == 1)
                        System.out.println("delete 성공!");
                    else
                        System.out.println("delete 실패!");

                    conn.commit();
                } catch (Exception e) {
                    System.out.println("Failed case 6.");
                    e.printStackTrace();
                }
                break;
            case 7:
                try {
                    System.out.printf("ID 입력: ");
                    String ID = scanner.nextLine().trim();

                    System.out.printf("Password 입력: ");
                    String Password = scanner.nextLine().trim();

                    System.out.printf("Name 입력: ");
                    String Name = scanner.nextLine().trim();

                    System.out.printf("Email 입력: ");
                    String Email = scanner.nextLine().trim();

                    System.out.printf("Phone number 입력: ");
                    String Phone = scanner.nextLine().trim();

                    int result = stmt.executeUpdate(
                        String.format("INSERT INTO ACCOUNT VALUES('%s', '%s', '%s', '%s', '%s')",
                                ID, Password, Name, Email, Phone));
                    if(result == 1)
                        System.out.println("insert 성공!");
                    else
                        System.out.println("insert 실패!");

                    conn.commit();
                } catch (Exception e) {
                    System.out.println("Failed case 7.");
                    e.printStackTrace();
                }
                break;
            case 8:
                try {
                    System.out.printf("ID 입력: ");
                    String ID = scanner.nextLine().trim();

                    System.out.printf("Password 입력: ");
                    String Password = scanner.nextLine().trim();

                    System.out.printf("Name 입력: ");
                    String Name = scanner.nextLine().trim();

                    System.out.printf("Email 입력: ");
                    String Email = scanner.nextLine().trim();

                    System.out.printf("Phone number 입력: ");
                    String Phone = scanner.nextLine().trim();

                    int result = stmt.executeUpdate(
                        String.format("UPDATE ACCOUNT\n" +
                            "SET Name = '%s', Email = '%s', Phone = '%s' WHERE ID = '%s' AND Password = '%s'",
                            Name, Email, Phone, ID, Password));
                    if(result == 0)
                        System.out.println("update 실패!");
                    else
                        System.out.println("update 성공!");

                    conn.commit();
                } catch (Exception e) {
                    System.out.println("Failed case 8.");
                    e.printStackTrace();
                }
                break;
            case 9:
                try {
                    System.out.println("삭제할 Account의 ID을 입력: ");
                    String ID = scanner.nextLine().trim();

                    System.out.println("삭제할 Account의 Password을 입력: ");
                    String Password = scanner.nextLine().trim();

                    int result = stmt.executeUpdate(
                        String.format("DELETE FROM ACCOUNT WHERE ID = %s AND Password = %s",
                            ID, Password));
                    if (result == 1)
                        System.out.println("delete 성공!");
                    else
                        System.out.println("delete 실패!");

                    conn.commit();
                } catch (Exception e) {
                    System.out.println("Failed case 9.");
                    e.printStackTrace();
                }
                break;
            case 10:
                System.out.print("쿼리 선택: ");
                int queryNumber = Integer.parseInt(scanner.nextLine().trim());
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
                }
                break;
            case 11:
                break cliLoop;
            default:
                System.out.println("Invalid operation selected! Select again.");
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

	public static void query1(Connection conn, Statement stmt) {
		Scanner scanner = new Scanner(System.in);

		System.out.print("작가 이름을 입력:");
		String buffer = scanner.nextLine().trim();

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
		Scanner scanner = new Scanner(System.in);

		System.out.println("특정 언어로 자필된 책의 장르별 책 수 보여주기");
		System.out.print("언어 입력:");
		String buffer = scanner.nextLine().trim();

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
