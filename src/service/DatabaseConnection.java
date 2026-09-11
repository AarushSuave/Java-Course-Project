package service;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

// JDBC Connection Manager demonstrating Database Connectivity and Exception Handling
public class DatabaseConnection {

    // Database configuration
    private static final String DB_URL = "jdbc:mysql://localhost:3306/rescuenet?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "root"; // Default local MySQL password

    private static Connection connection = null;
    private static boolean dbAvailable = false;

    // Static block to initialize driver
    static {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            // System.out.println("[JDBC] MySQL JDBC Driver loaded successfully.");
        } catch (ClassNotFoundException e) {
            // System.out.println("[JDBC Info] MySQL JDBC Driver not found on classpath. Using in-memory mode.");
        }
    }

    // Method to get JDBC connection
    public static Connection getConnection() {
        try {
            if (connection == null || connection.isClosed()) {
                connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
                dbAvailable = true;
            }
            return connection;
        } catch (SQLException e) {
            dbAvailable = false;
            // System.out.println("[JDBC Info] Could not connect to MySQL server. Operating in robust in-memory mode.");
            return null;
        }
    }

    public static boolean isDatabaseAvailable() {
        try {
            Connection conn = getConnection();
            return conn != null && !conn.isClosed();
        } catch (SQLException e) {
            return false;
        }
    }

    // Initialize tables if connected
    public static void initializeDatabaseTables() {
        Connection conn = getConnection();
        if (conn == null) {
            return;
        }

        try (Statement stmt = conn.createStatement()) {
            // 1. Users Table
            stmt.executeUpdate("CREATE TABLE IF NOT EXISTS users ("
                    + "id INT AUTO_INCREMENT PRIMARY KEY, "
                    + "username VARCHAR(50) UNIQUE NOT NULL, "
                    + "password VARCHAR(50) NOT NULL, "
                    + "role VARCHAR(30) NOT NULL)");

            // Insert default admin if missing
            stmt.executeUpdate("INSERT IGNORE INTO users (id, username, password, role) "
                    + "VALUES (1, 'admin', 'admin123', 'Administrator')");

            // 2. Disasters Table
            stmt.executeUpdate("CREATE TABLE IF NOT EXISTS disasters ("
                    + "disaster_id INT PRIMARY KEY, "
                    + "type VARCHAR(50) NOT NULL, "
                    + "location VARCHAR(100) NOT NULL, "
                    + "severity VARCHAR(30) NOT NULL)");

            // 3. Victims Table
            stmt.executeUpdate("CREATE TABLE IF NOT EXISTS victims ("
                    + "victim_id INT PRIMARY KEY, "
                    + "name VARCHAR(100) NOT NULL, "
                    + "age INT NOT NULL, "
                    + "phone VARCHAR(20), "
                    + "location VARCHAR(100) NOT NULL, "
                    + "medical_status VARCHAR(50), "
                    + "rescue_status VARCHAR(50), "
                    + "assigned_team_id INT DEFAULT 0, "
                    + "allocated_shelter_id INT DEFAULT 0)");

            // 4. Teams Table
            stmt.executeUpdate("CREATE TABLE IF NOT EXISTS teams ("
                    + "team_id INT PRIMARY KEY, "
                    + "team_name VARCHAR(100) NOT NULL, "
                    + "member_count INT NOT NULL, "
                    + "is_available BOOLEAN NOT NULL, "
                    + "vehicle_type VARCHAR(50) NOT NULL)");

            // 5. Shelters Table
            stmt.executeUpdate("CREATE TABLE IF NOT EXISTS shelters ("
                    + "shelter_id INT PRIMARY KEY, "
                    + "name VARCHAR(100) NOT NULL, "
                    + "location VARCHAR(100) NOT NULL, "
                    + "capacity INT NOT NULL, "
                    + "occupied INT NOT NULL)");

            System.out.println("[JDBC] Database tables initialized successfully.");
        } catch (SQLException e) {
            System.err.println("[JDBC Error] Error initializing database tables: " + e.getMessage());
        }
    }
}
