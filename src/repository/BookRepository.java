package repository;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import dbconnection.DbConnection;
import domain.entities.*;

public class BookRepository {
    public static List<Book> getAllBooks() {
        Connection connection = DbConnection.getConnection();
        List<Book> books = new ArrayList<>();

        try (PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM books " +
                "INNER JOIN authors ON books.author_id = authors.author_id");
             ResultSet resultSet = preparedStatement.executeQuery()) {

            while (resultSet.next()) {
                int id = resultSet.getInt("book_id");
                String title = resultSet.getString("title");
                String description = resultSet.getString("description");
                String publicationYear = resultSet.getString("publication_year");
                String isbn = resultSet.getString("isbn");
                int quantity = resultSet.getInt("quantity");

                // Fetch author information from the database
                int authorId = resultSet.getInt("author_id");
                String authorName = resultSet.getString("name");
                String authorBiography = resultSet.getString("biography");
                String authorBirthdate = resultSet.getString("birthdate");

                Author author = new Author(authorId, authorName, authorBiography, authorBirthdate);

                Book book = new Book(title, description, publicationYear, isbn, quantity, author);
                books.add(book);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return books;
    }

    public void createBook(Book book) {
        Connection connection = DbConnection.getConnection();
        String insertQuery = "INSERT INTO books (title, description, publication_year, isbn, quantity, author_id) VALUES (?, ?, ?, ?, ?, ?)";

        try (PreparedStatement preparedStatement = connection.prepareStatement(insertQuery)) {
            preparedStatement.setString(1, book.getTitle());
            preparedStatement.setString(2, book.getDescription());
            preparedStatement.setString(3, book.getPublicationYear());
            preparedStatement.setString(4, book.getIsbn());
            preparedStatement.setInt(5, book.getQuantity());
            preparedStatement.setInt(6, book.getAuthor().getId());

            int rowsInserted = preparedStatement.executeUpdate();
            if (rowsInserted > 0) {
                System.out.println("Book created successfully.");
            } else {
                System.out.println("Failed to create the book.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Book getBookByTitle(String bookTitle) {
        Connection connection = DbConnection.getConnection();
        Book book = null;
        String query = "SELECT * " +
                "FROM books b " +
                "INNER JOIN authors a ON b.author_id = a.author_id " +
                "WHERE b.title = ?";

        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, bookTitle);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    int id = resultSet.getInt("id");
                    String description = resultSet.getString("description");
                    String publicationYear = resultSet.getString("publication_year");
                    String isbn = resultSet.getString("isbn");
                    int quantity = resultSet.getInt("quantity");

                    int authorId = resultSet.getInt("author_id");
                    String authorName = resultSet.getString("name");
                    String authorBiography = resultSet.getString("biography");
                    String authorBirthdate = resultSet.getString("birthdate");

                    Author author = new Author(authorId, authorName, authorBiography, authorBirthdate);
                    book = new Book(bookTitle, description, publicationYear, isbn, quantity, author);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return book;
    }

    public void deleteBook(String bookTitle) {
        Connection connection = DbConnection.getConnection();
        String deleteQuery = "DELETE FROM books WHERE title = ?";

        try (PreparedStatement preparedStatement = connection.prepareStatement(deleteQuery)) {
            preparedStatement.setString(1, bookTitle);

            int rowsAffected = preparedStatement.executeUpdate();

            if (rowsAffected > 0) {
                System.out.println("Book deleted successfully.");
            } else {
                System.out.println("Book not found. Deletion failed.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void updateBook(String bookTitle, Book book){
        Connection connection = DbConnection.getConnection();
        String updateQuery = "UPDATE books SET title = ?, description = ?, publication_year = ?, isbn = ?, quantity = ?, author_id = ? WHERE title = ?";

        try (PreparedStatement preparedStatement = connection.prepareStatement(updateQuery)) {
            preparedStatement.setString(1, book.getTitle());
            preparedStatement.setString(2, book.getDescription());
            preparedStatement.setString(3, book.getPublicationYear());
            preparedStatement.setString(4, book.getIsbn());
            preparedStatement.setInt(5, book.getQuantity());
            preparedStatement.setInt(6, book.getAuthor().getId());
            preparedStatement.setString(7, bookTitle);

            int rowsInserted = preparedStatement.executeUpdate();
            if (rowsInserted > 0) {
                System.out.println("Book updated successfully.");
            } else {
                System.out.println("Failed to create the book.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}


