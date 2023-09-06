package dbconnection;
import java.sql.*;

public class DbConnection {
    private String url;
    private String username;
    private String password;

    public DbConnection (String url, String username, String password){
        this.url = url;
        this.username = username;
        this.password = password;
    }

    public void Connect() {
        try {
            Connection con = DriverManager.getConnection(url, username, password);
            Statement statement = con.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT * FROM books");
            while (resultSet.next()) {
                System.out.println("Title: " + resultSet.getString("title"));
            }
            con.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
