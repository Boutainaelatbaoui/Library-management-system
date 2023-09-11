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

                Author author = new Author(authorName, authorBiography, authorBirthdate);
                author.setId(authorId);
                authors.add(author);

            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return authors;
    }

    public void createAuthor(Author author) {
        try (Connection connection = DbConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO authors (name, biography, birthdate) VALUES (?, ?, ?)")) {

            preparedStatement.setString(1, author.getName());
            preparedStatement.setString(2, author.getBiography());
            preparedStatement.setString(3, author.getBirthdate());

            int rowsInserted = preparedStatement.executeUpdate();
            if (rowsInserted > 0) {
                System.out.println("Author created successfully.");
            } else {
                System.out.println("Failed to create the author.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Author findAuthorByName(String authorName) {
        try (Connection connection = DbConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM authors WHERE name = ?")) {

            preparedStatement.setString(1, authorName);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    int authorId = resultSet.getInt("author_id");
                    authorName = resultSet.getString("name");
                    String authorBiography = resultSet.getString("biography");
                    String authorBirthdate = resultSet.getString("birthdate");
                    Author author = new  Author(authorName, authorBiography, authorBirthdate);
                    author.setId(authorId);
                    return author;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

}
