import java.io.*;
import java.sql.*;
import java.util.Scanner;
import java.util.HashSet;
import java.util.Set;

public class DBManager {
    public static void main(String[] args) {
        String url = "jdbc:mysql://localhost:3306/companydb";
        String user = "root";
        String password = "madhuri@123";
        Scanner scanner = new Scanner(System.in);

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            try (Connection conn = DriverManager.getConnection(url, user, password)) {
                System.out.println("Connected to database: " + conn.getCatalog());
                Statement stmt = conn.createStatement();

                // Main menu loop for operations
                while (true) {
                    System.out.println("\nDatabase Operations Menu:");
                    System.out.println("1. Create Tables");
                    System.out.println("2. Insert");
                    System.out.println("3. Update");
                    System.out.println("4. Delete");
                    System.out.println("5. Select");
                    System.out.println("6. Exit");
                    System.out.print("Enter your choice (1-6): ");
                    int choice = scanner.nextInt();
                    scanner.nextLine(); // Consume newline

                    if (choice == 6) {
                        System.out.println("Exiting...");
                        break;
                    }

                    if (choice < 1 || choice > 6) {
                        System.out.println("Invalid choice. Please try again.");
                        continue;
                    }

                    // Handle Create Tables option
                    if (choice == 1) {
                        System.out.print("Enter CREATE TABLE statement directly or 'file' to read from a file: ");
                        String inputType = scanner.nextLine().trim();

                        String query;
                        if (inputType.equalsIgnoreCase("file")) {
                            System.out.print("Enter file path: ");
                            String filePath = scanner.nextLine().trim();
                            query = readQueryFromFile(filePath);
                        } else {
                            System.out.print("Enter CREATE TABLE statement: ");
                            query = scanner.nextLine().trim();
                        }

                        if (query.isEmpty()) {
                            System.out.println("No query provided.");
                            continue;
                        }

                        // Check if the query is a CREATE TABLE statement
                        if (!query.toLowerCase().startsWith("create table")) {
                            System.out.println("Invalid statement. Please provide a CREATE TABLE statement.");
                            continue;
                        }

                        // Extract the table name from the query
                        String tableName = extractTableName(query);
                        if (tableName == null) {
                            System.out.println("Could not parse table name from the CREATE TABLE statement.");
                            continue;
                        }

                        // Check if the table already exists
                        Set<String> existingTables = new HashSet<>();
                        ResultSet rs = stmt.executeQuery("SHOW TABLES");
                        while (rs.next()) {
                            existingTables.add(rs.getString(1).toUpperCase());
                        }
                        rs.close();

                        if (existingTables.contains(tableName.toUpperCase())) {
                            System.out.println("Table " + tableName + " already exists.");
                            System.out.print("Do you want to drop and recreate it? (yes/no): ");
                            String response = scanner.nextLine().trim().toLowerCase();
                            if (response.equals("yes")) {
                                stmt.executeUpdate("DROP TABLE " + tableName);
                                System.out.println("Table " + tableName + " dropped.");
                            } else {
                                System.out.println("Table creation aborted.");
                                continue;
                            }
                        }

                        // Execute the CREATE TABLE statement
                        try {
                            stmt.executeUpdate(query);
                            System.out.println("Table " + tableName + " created successfully.");
                        } catch (SQLException e) {
                            System.err.println("Error creating table: " + e.getMessage());
                        }
                        continue;
                    }

                    // For other operations (Insert, Update, Delete, Select)
                    System.out.print("Enter SQL query directly or 'file' to read from a file: ");
                    String inputType = scanner.nextLine().trim();

                    String query;
                    if (inputType.equalsIgnoreCase("file")) {
                        System.out.print("Enter file path: ");
                        String filePath = scanner.nextLine().trim();
                        query = readQueryFromFile(filePath);
                    } else {
                        System.out.print("Enter SQL query: ");
                        query = scanner.nextLine().trim();
                    }

                    if (query.isEmpty()) {
                        System.out.println("No query provided.");
                        continue;
                    }

                    try {
                        // Handle SELECT (option 5)
                        if (query.toLowerCase().startsWith("select") && choice == 5) {
                            ResultSet selectRs = stmt.executeQuery(query);
                            ResultSetMetaData rsmd = selectRs.getMetaData();
                            int columnCount = rsmd.getColumnCount();

                            // Print column headers
                            for (int i = 1; i <= columnCount; i++) {
                                System.out.print(rsmd.getColumnName(i) + "\t");
                            }
                            System.out.println("\n" + "-".repeat(50));

                            // Print rows
                            while (selectRs.next()) {
                                for (int i = 1; i <= columnCount; i++) {
                                    System.out.print(selectRs.getString(i) + "\t");
                                }
                                System.out.println();
                            }
                            selectRs.close();
                        }
                        // Handle EMP insertion with duplicate check (option 2)
                        else if (query.toLowerCase().startsWith("insert into emp") && choice == 2) {
                            // Extract EMPNO value
                            String valuesPart = query.toLowerCase().replace("insert into emp", "").trim();
                            valuesPart = valuesPart.substring(valuesPart.indexOf("(") + 1, valuesPart.indexOf(")"));
                            String[] values = valuesPart.split(",");
                            int empno = Integer.parseInt(values[0].trim());

                            ResultSet empRs = stmt.executeQuery("SELECT COUNT(*) FROM EMP WHERE EMPNO = " + empno);
                            empRs.next();
                            int count = empRs.getInt(1);
                            empRs.close();

                            if (count > 0) {
                                // Duplicate, insert into EMP_DUPLICATES
                                String dupQuery = query.replaceFirst("(?i)insert into emp", "INSERT INTO EMP_DUPLICATES");
                                int dupRows = stmt.executeUpdate(dupQuery);
                                System.out.println("Duplicate found! Inserted into EMP_DUPLICATES (" + dupRows + " row).");
                            } else {
                                // No duplicate, insert into EMP
                                int rowsAffected = stmt.executeUpdate(query);
                                System.out.println("Query OK, " + rowsAffected + " row(s) affected.");
                            }
                        }
                        // Handle other operations (INSERT, UPDATE, DELETE)
                        else {
                            int rowsAffected = stmt.executeUpdate(query);
                            System.out.println("Query OK, " + rowsAffected + " row(s) affected.");
                        }

                    } catch (SQLException e) {
                        System.err.println("SQL Error: " + e.getMessage());
                    }
                    System.out.println("--------------------------------------------------");
                }
            }
        } catch (ClassNotFoundException e) {
            System.err.println("MySQL JDBC Driver not found.");
        } catch (SQLException e) {
            System.err.println("Database connection error: " + e.getMessage());
        }
    }

    private static String extractTableName(String query) {
        // Extract table name from CREATE TABLE statement
        String[] parts = query.split("\\s+");
        for (int i = 0; i < parts.length; i++) {
            if (parts[i].equalsIgnoreCase("TABLE")) {
                if (i + 1 < parts.length) {
                    String tableName = parts[i + 1];
                    // Remove any trailing characters like ( or ;
                    tableName = tableName.replaceAll("[\\(\\);]", "").trim();
                    return tableName;
                }
            }
        }
        return null;
    }

    private static String readQueryFromFile(String filePath) {
        StringBuilder queryBuilder = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                line = line.trim();
                if (!line.isEmpty()) {
                    queryBuilder.append(line).append(" ");
                }
            }
        } catch (IOException e) {
            System.err.println("File read error: " + e.getMessage());
            return "";
        }
        return queryBuilder.toString().trim();
    }
}