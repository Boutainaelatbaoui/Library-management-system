package repository;

import domain.entities.*;
import domain.enums.Status;
import service.BookService;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;

public class ReservationRepository {
    private Connection connection;

    public ReservationRepository(Connection connection) {
        this.connection = connection;
    }
    ClientRepository clientRepository = new ClientRepository(connection);
    CopyRepository copyRepository = new CopyRepository(connection);

    public void saveReservation(Reservation reservation) {
        String insertQuery = "INSERT INTO reservations (borrowing_date, due_date, bookcopy_id, client_id) " +
                "VALUES (?, ?, ?, ?)";

        Date borrowingDate = new Date();

        try (PreparedStatement preparedStatement = connection.prepareStatement(insertQuery)) {
            preparedStatement.setDate(1, new java.sql.Date(borrowingDate.getTime()));
            preparedStatement.setDate(2, new java.sql.Date(reservation.getDueDate().getTime()));
            preparedStatement.setInt(3, reservation.getBookCopy().getId());
            preparedStatement.setInt(4, reservation.getClient().getId());

            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Reservation findReservation(String isbn, int memberNum) {
        String query = "SELECT * FROM reservations " +
                "INNER JOIN bookcopies ON reservations.bookcopy_id = bookcopies.bookcopy_id " +
                "INNER JOIN books ON bookcopies.book_id = books.book_id " +
                "INNER JOIN clients ON reservations.client_id = clients.client_id " +
                "INNER JOIN authors ON authors.author_id = books.author_id " +
                "WHERE books.isbn = ? AND clients.member_num = ?";

        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, isbn);
            preparedStatement.setInt(2, memberNum);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    int reservationId = resultSet.getInt("reservation_id");
                    Date dueDate = resultSet.getDate("due_date");
                    Date borrowingDate = resultSet.getDate("borrowing_date");
                    int bookCopyId = resultSet.getInt("bookcopy_id");

                    String title = resultSet.getString("title");
                    String description = resultSet.getString("description");
                    String publicationYear = resultSet.getString("publication_year");
                    int quantity = resultSet.getInt("quantity");

                    int authorId = resultSet.getInt("author_id");
                    String authorName = resultSet.getString("name");
                    String authorBiography = resultSet.getString("biography");
                    String authorBirthdate = resultSet.getString("birthdate");

                    Author author = new Author(authorId, authorName, authorBiography, authorBirthdate);

                    Book book = new Book(title, description, publicationYear, isbn, quantity, author);

                    BookCopy bookCopy = new BookCopy(bookCopyId, Status.AVAILABLE, book);

                    Client client = new Client();
                    client.setId(resultSet.getInt("client_id"));
                    client.setFullName(resultSet.getString("full_name"));
                    client.setEmail(resultSet.getString("email"));
                    client.setCin(resultSet.getString("cin"));
                    client.setMemberNum(memberNum);
                    client.setTelephone(resultSet.getString("telephone"));

                    return new Reservation(dueDate, borrowingDate, client, bookCopy);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

}

