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
    private Connection connection;

    public BookRepository(Connection connection) {
        this.connection = connection;
    }

    public List<Book> getAllAvailableBooks() {
        List<Book> books = new ArrayList<>();

        try (PreparedStatement preparedStatement = connection.prepareStatement("SELECT b.*, a.* FROM books AS b " +
                "INNER JOIN bookcopies AS bc ON b.book_id = bc.book_id " +
                "INNER JOIN authors AS a ON b.author_id = a.author_id " +
                "WHERE bc.status = 'AVAILABLE' OR bc.status = 'RETURNED'");
             ResultSet resultSet = preparedStatement.executeQuery()) {

            while (resultSet.next()) {
                int id = resultSet.getInt("book_id");
                String title = resultSet.getString("title");
                String description = resultSet.getString("description");
                String publicationYear = resultSet.getString("publication_year");
                String isbn = resultSet.getString("isbn");

                int authorId = resultSet.getInt("author_id");
                String authorName = resultSet.getString("name");
                String authorBiography = resultSet.getString("biography");
                String authorBirthdate = resultSet.getString("birthdate");

                Author author = new Author(authorName, authorBiography, authorBirthdate);

                Book book = new Book(title, description, publicationYear, isbn, author);
                books.add(book);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return books;
    }

    public List<Book> getAllBooks() {
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

                int authorId = resultSet.getInt("author_id");
                String authorName = resultSet.getString("name");
                String authorBiography = resultSet.getString("biography");
                String authorBirthdate = resultSet.getString("birthdate");

                Author author = new Author(authorName, authorBiography, authorBirthdate);

                Book book = new Book(title, description, publicationYear, isbn, author);
                books.add(book);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return books;
    }

    public void createBook(Book book) {
        Connection connection = DbConnection.getConnection();
        String insertQuery = "INSERT INTO books (title, description, publication_year, isbn, author_id) VALUES (?, ?, ?, ?, ?)";

        try (PreparedStatement preparedStatement = connection.prepareStatement(insertQuery)) {
            preparedStatement.setString(1, book.getTitle());
            preparedStatement.setString(2, book.getDescription());
            preparedStatement.setString(3, book.getPublicationYear());
            preparedStatement.setString(4, book.getIsbn());
            preparedStatement.setInt(5, book.getAuthor().getId());

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

    public List<Book> getBooksByAuthor(String authorName) {
        String query = "SELECT * " +
                "FROM books b " +
                "INNER JOIN authors a ON b.author_id = a.author_id " +
                "WHERE a.name = ?";
        List<Book> books = new ArrayList<>();

        try (Connection connection = DbConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setString(1, authorName);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    int bookId = resultSet.getInt("book_id");
                    String title = resultSet.getString("title");
                    String description = resultSet.getString("description");
                    String publicationYear = resultSet.getString("publication_year");
                    String isbn = resultSet.getString("isbn");

                    int authorId = resultSet.getInt("author_id");
                    String authorBiography = resultSet.getString("biography");
                    String authorBirthdate = resultSet.getString("birthdate");

                    Author author = new Author(authorName, authorBiography, authorBirthdate);

                    Book book = new Book(title, description, publicationYear, isbn, author);
                    books.add(book);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return books;
    }

    public Book getBookByTitle(String bookTitle) {
        String query = "SELECT * " +
                "FROM books b " +
                "INNER JOIN authors a ON b.author_id = a.author_id " +
                "WHERE b.title = ?";

        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, bookTitle);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    int id = resultSet.getInt("book_id");
                    String description = resultSet.getString("description");
                    String publicationYear = resultSet.getString("publication_year");
                    String isbn = resultSet.getString("isbn");

                    int authorId = resultSet.getInt("author_id");
                    String authorName = resultSet.getString("name");
                    String authorBiography = resultSet.getString("biography");
                    String authorBirthdate = resultSet.getString("birthdate");

                    Author author = new Author(authorName, authorBiography, authorBirthdate);
                    Book book = new Book(bookTitle, description, publicationYear, isbn, author);
                    return book;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    public void createBookCopy(BookCopy bookCopy) {
        String insertQuery = "INSERT INTO bookcopies (status, book_id) VALUES (?, ?)";

        try (Connection connection = DbConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(insertQuery)) {
            preparedStatement.setString(1, bookCopy.getStatus().toString());
            preparedStatement.setInt(2, bookCopy.getBook().getId());

            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Book getBookByIsbn(String bookIsbn) {
        String query = "SELECT * " +
                "FROM books b " +
                "INNER JOIN authors a ON b.author_id = a.author_id " +
                "WHERE b.isbn = ?";

        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, bookIsbn);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    int id = resultSet.getInt("book_id");
                    String description = resultSet.getString("description");
                    String publicationYear = resultSet.getString("publication_year");
                    String bookTitle = resultSet.getString("title");

                    int authorId = resultSet.getInt("author_id");
                    String authorName = resultSet.getString("name");
                    String authorBiography = resultSet.getString("biography");
                    String authorBirthdate = resultSet.getString("birthdate");

                    Author author = new Author(authorName, authorBiography, authorBirthdate);
                    Book book = new Book(bookTitle, description, publicationYear, bookIsbn, author);
                    return book;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    public void deleteBookByIsbn(String bookIsbn) {
        String deleteQuery = "DELETE FROM books WHERE isbn = ?";

        try (PreparedStatement preparedStatement = connection.prepareStatement(deleteQuery)) {
            preparedStatement.setString(1, bookIsbn);

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

    public void updateBookByIsbn(String bookIsbn, Book book) {
        String updateQuery = "UPDATE books SET title = ?, description = ?, publication_year = ?, isbn = ?, author_id = ? WHERE isbn = ?";

        try (PreparedStatement preparedStatement = connection.prepareStatement(updateQuery)) {
            preparedStatement.setString(1, book.getTitle());
            preparedStatement.setString(2, book.getDescription());
            preparedStatement.setString(3, book.getPublicationYear());
            preparedStatement.setString(4, book.getIsbn());
            preparedStatement.setInt(5, book.getAuthor().getId());
            preparedStatement.setString(6, bookIsbn);

            int rowsUpdated = preparedStatement.executeUpdate();
            if (rowsUpdated > 0) {
                System.out.println("Book updated successfully.");
            } else {
                System.out.println("Failed to update the book.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void updateBookCopyStatus(BookCopy bookCopy) {
        String query = "UPDATE bookcopies SET status = ? WHERE bookcopy_id = ?";

        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, bookCopy.getStatus().toString());
            preparedStatement.setInt(2, bookCopy.getId());

            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public int getTotalNumberOfBookCopiesWithStatus(String status) {
        String query = "SELECT COUNT(*) AS total_book_copies FROM bookcopies WHERE status = ?";

        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, status);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    return resultSet.getInt("total_book_copies");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return 0;
    }

    public int getBookID(Book book) {
        String query = "SELECT book_id AS id FROM books WHERE title = ?";

        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, book.getTitle());

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    return resultSet.getInt("id");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return 0;
    }
    public int getTotalNumberOfBooks() {
        String query = "SELECT COUNT(*) AS total_books FROM books";

        try (PreparedStatement preparedStatement = connection.prepareStatement(query);
             ResultSet resultSet = preparedStatement.executeQuery()) {
            if (resultSet.next()) {
                return resultSet.getInt("total_books");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return 0;
    }
    public int getTotalNumberOfBookcopies() {
        String query = "SELECT COUNT(*) AS total_books FROM bookcopies WHERE status = 'AVAILABLE' OR status = 'RETURNED'";

        try (PreparedStatement preparedStatement = connection.prepareStatement(query);
             ResultSet resultSet = preparedStatement.executeQuery()) {
            if (resultSet.next()) {
                return resultSet.getInt("total_books");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return 0;
    }
}


