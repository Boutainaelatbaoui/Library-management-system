package dbconnection;
import java.sql.*;

public class DbConnection {
    private static String url = "jdbc:mysql://localhost:3306/library";
    private static String username = "root";
    private static String password = "";

    public static Connection getConnection() {
        try {
            return DriverManager.getConnection(url, username, password);
        } catch (SQLException e) {

            throw new RuntimeException("Failed to connect to the database.");
        }
    }
}
