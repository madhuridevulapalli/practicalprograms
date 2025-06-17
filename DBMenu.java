import java.sql.*;
import java.util.Scanner;

public class DBMenu {
    static final String DB_URL = "jdbc:mysql://localhost:3306/testdb1";
    static final String USER = "root";
    static final String PASS = "madhuri@123"; // Replace with your actual password

    public static void main(String[] args) {
        try (
            Connection conn = DriverManager.getConnection(DB_URL, USER, PASS);
            Scanner scanner = new Scanner(System.in);
        ) {
            System.out.println("Connected to the database!");

            while (true) {
                System.out.println("\n===== MENU =====");
                System.out.println("1. Insert User");
                System.out.println("2. View All Users");
                System.out.println("3. Exit");
                System.out.print("Choose an option: ");

                int choice = scanner.nextInt();
                scanner.nextLine(); // consume newline

                if (choice == 1) {
                    insertUser(conn, scanner);
                } else if (choice == 2) {
                    viewUsers(conn);
                } else if (choice == 3) {
                    System.out.println("Exiting...");
                    break;
                } else {
                    System.out.println("Invalid choice. Try again.");
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    static void insertUser(Connection conn, Scanner scanner) throws SQLException {
        System.out.print("Enter name: ");
        String name = scanner.nextLine();

        System.out.print("Enter email: ");
        String email = scanner.nextLine();

        String query = "INSERT INTO users (name, email) VALUES (?, ?)";
        try (PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setString(1, name);
            pstmt.setString(2, email);
            int rows = pstmt.executeUpdate();
            System.out.println(rows + " user(s) added.");
        }
    }

    static void viewUsers(Connection conn) throws SQLException {
        String query = "SELECT * FROM users";
        try (Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(query)) {
            System.out.println("\n--- User List ---");
            while (rs.next()) {
                int id = rs.getInt("id");
                String name = rs.getString("name");
                String email = rs.getString("email");
                System.out.println("ID: " + id + ", Name: " + name + ", Email: " + email);
            }
        }
    }
}
