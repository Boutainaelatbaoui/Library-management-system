package repository;

import domain.entities.Reservation;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Date;

public class ReservationRepository {
    private Connection connection;

    public ReservationRepository(Connection connection) {
        this.connection = connection;
    }

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

}

