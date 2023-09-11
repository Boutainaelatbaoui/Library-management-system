package service;

import domain.entities.Book;
import domain.entities.BookCopy;
import domain.entities.Reservation;
import domain.enums.Status;
import repository.BookRepository;
import repository.ReservationRepository;

import java.util.List;

public class BookService {
    private BookRepository bookRepository;

    public BookService(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    public void updateBookCopyStatus(BookCopy bookCopy, Status newStatus) {
        bookCopy.setStatus(newStatus);
        bookRepository.updateBookCopyStatus(bookCopy);
    }
    public void decreaseBookQuantity(Book book) {
        book.setQuantity(book.getQuantity() - 1);
        bookRepository.updateBookQuantity(book);
    }
    public void updateLostBookCopiesStatus(ReservationRepository reservationRepository, BookRepository bookRepository) {
        List<Reservation> expiredReservations = reservationRepository.getExpiredReservations();
        boolean updatesMade = false;

        for (Reservation reservation : expiredReservations) {
            if (reservation.getBookCopy().getStatus() != Status.LOST) {
                reservation.getBookCopy().setStatus(Status.LOST);
                bookRepository.updateBookCopyStatus(reservation.getBookCopy());
                updatesMade = true;
            }
        }

        if (updatesMade) {
            System.out.println("Book copies with expired due dates have been marked as LOST.");
        } else {
            System.out.println("No book copies with expired due dates found.");
        }
    }


    public void displayStatistics() {
        int totalBooks = bookRepository.getTotalNumberOfBooks();
        int availableReturnBookCopies = bookRepository.getTotalNumberOfBookcopies();
        int returnedBookCopies = bookRepository.getTotalNumberOfBookCopiesWithStatus("RETURNED");
        int lostBookCopies = bookRepository.getTotalNumberOfBookCopiesWithStatus("LOST");
        int borrowedBookCopies = bookRepository.getTotalNumberOfBookCopiesWithStatus("BORROWED");

        System.out.println("Library Statistics:");
        System.out.println("Total Books: " + totalBooks);
        System.out.println("Total Available Book Copies: " + availableReturnBookCopies);
        System.out.println("Returned Book Copies: " + returnedBookCopies);
        System.out.println("Lost Book Copies: " + lostBookCopies);
        System.out.println("Total Borrowed Book Copies: " + borrowedBookCopies);
    }

    public void displayBook(Book book) {
        if (book == null) {
            System.out.println("No book found.");
        } else {
            System.out.println("The book:");
            printBookDetails(book);
            System.out.println();
        }
    }

    public void displayBooks(List<Book> books) {
        if (books.isEmpty()) {
            System.out.println("No books found.");
        } else {
            System.out.println("List of Books:");
            for (Book book : books) {
                printBookDetails(book);
                System.out.println("######################");
            }
        }
    }

    private void printBookDetails(Book book) {
        System.out.println("Title: " + book.getTitle());
        System.out.println("Description: " + book.getDescription());
        System.out.println("Publication Year: " + book.getPublicationYear());
        System.out.print("\u001B[33m");
        System.out.println("ISBN: " + book.getIsbn());
        System.out.print("\u001B[0m");

        System.out.println("Quantity: " + book.getQuantity());

        System.out.println("Author Name: " + book.getAuthor().getName());
        System.out.println("Author Biography: " + book.getAuthor().getBiography());
        System.out.println("Author Birthdate: " + book.getAuthor().getBirthdate());
    }
}

