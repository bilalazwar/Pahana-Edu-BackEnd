package utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnectionUtil {

    private static final String JDBC_URL = "jdbc:mysql://localhost:3306/PahanaEdu?useSSL=false&serverTimezone=UTC";
    private static final String JDBC_USERNAME = "root";
    private static final String JDBC_PASSWORD = "root123";

    // Static block to load MySQL drivers
    static {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver"); // MySQL 8+ driver class
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    // Method to get a connection
    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(JDBC_URL, JDBC_USERNAME, JDBC_PASSWORD);
    }

    // Optional: Helper method to close connection, statement, resultset safely
    public static void closeConnection(Connection conn) {
        if (conn != null) {
            try { conn.close(); } catch (SQLException e) { e.printStackTrace(); }
        }
    }
}
