package service;

import domain.entities.Client;
import domain.entities.BookCopy;
import domain.entities.Reservation;
import repository.ReservationRepository;

import java.util.Calendar;
import java.util.Date;

public class ReservationService {
    private ReservationRepository reservationRepository;

    public ReservationService(ReservationRepository reservationRepository) {
        this.reservationRepository = reservationRepository;
    }

    public Reservation createReservation(Client client, BookCopy bookCopy) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.add(Calendar.DATE, 14);
        Date dueDate = calendar.getTime();

        Date borrowingDate = new Date();

        Reservation reservation = new Reservation(dueDate, borrowingDate, client, bookCopy);

        reservationRepository.saveReservation(reservation);

        return reservation;
    }

    public Reservation findReservation(String isbn, int memberNum) {
        return reservationRepository.findReservation(isbn, memberNum);
    }

}

