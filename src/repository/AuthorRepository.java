package repository;
import dbconnection.DbConnection;
import domain.entities.Author;
import domain.entities.Book;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class AuthorRepository {
    public static List<Author> getAllAuthors() {
        Connection connection = DbConnection.getConnection();
        List<Author> authors = new ArrayList<>();

        try (PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM authors ");
             ResultSet resultSet = preparedStatement.executeQuery()) {

            while (resultSet.next()) {
                int authorId = resultSet.getInt("author_id");
                String authorName = resultSet.getString("name");
                String authorBiography = resultSet.getString("biography");
                String authorBirthdate = resultSet.getString("birthdate");

                Author author = new Author(authorId, authorName, authorBiography, authorBirthdate);
                authors.add(author);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return authors;
    }
}
