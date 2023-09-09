package repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import domain.entities.Author;
import domain.entities.Book;
import domain.entities.BookCopy;
import domain.enums.Status;

public class CopyRepository {
    private static Connection connection;

    public CopyRepository(Connection connection) {
        this.connection = connection;
    }

    public static BookCopy getBookCopies(String bookTitle) {

        String query = "SELECT * " +
                "FROM bookcopies bc " +
                "INNER JOIN books b ON bc.book_id = b.book_id " +
                "INNER JOIN authors a ON b.author_id = a.author_id " +
                "WHERE b.title = ? AND bc.status = 'AVAILABLE' " + // Added single quotes around 'AVAILABLE'
                "LIMIT 1";

        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, bookTitle);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    int bookCopyId = resultSet.getInt("bookcopy_id");

                    String title = resultSet.getString("title");
                    String description = resultSet.getString("description");
                    String publicationYear = resultSet.getString("publication_year");
                    String isbn = resultSet.getString("isbn");
                    int quantity = resultSet.getInt("quantity");

                    int authorId = resultSet.getInt("author_id");
                    String authorName = resultSet.getString("name");
                    String authorBiography = resultSet.getString("biography");
                    String authorBirthdate = resultSet.getString("birthdate");

                    Author author = new Author(authorId, authorName, authorBiography, authorBirthdate);

                    Book book = new Book(title, description, publicationYear, isbn, quantity, author);

                    BookCopy bookCopy = new BookCopy(bookCopyId, Status.AVAILABLE, book);
                    return bookCopy;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

}


