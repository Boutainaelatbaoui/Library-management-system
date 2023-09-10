package controller;

import dbconnection.DbConnection;
import domain.entities.Book;
import domain.entities.Client;
import domain.entities.Reservation;
import domain.enums.Status;
import repository.BookRepository;
import repository.ClientRepository;
import repository.CopyRepository;
import repository.ReservationRepository;
import service.BookService;
import service.ClientService;
import service.CopyService;
import service.ReservationService;

import java.util.Scanner;
import java.sql.Connection;

public class ClientController {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        Connection connection = DbConnection.getConnection();
        ClientRepository clientRepository = new ClientRepository(connection);
        ClientService clientService = new ClientService(clientRepository);
        ReservationRepository reservationRepository = new ReservationRepository(connection);
        ReservationService reservationService = new ReservationService(reservationRepository);
        BookRepository bookRepository = new BookRepository(connection);
        CopyRepository copyRepository = new CopyRepository(connection);
        CopyService copyService = new CopyService();
        BookService bookService = new BookService(bookRepository);

        while (true) {
            System.out.println("Choose an option:");
            System.out.println("1. Check if Client Exists by CIN");
            System.out.println("2. Return to The Menu");

            int choice = Integer.parseInt(scanner.nextLine());

            switch (choice) {
                case 1:
                    System.out.println("Enter CIN to check if client exists:");
                    String cin = scanner.nextLine();
                    Client client = clientService.checkClientExistence(cin, scanner);

                    if (client != null) {
                        Book foundBook = searchByTitle(bookRepository, scanner);

                        if (foundBook != null) {
                            boolean hasAvailableCopy = copyService.hasAvailableBookCopy(foundBook.getTitle(), copyRepository);

                            if (hasAvailableCopy) {
                                System.out.println("This Book is Available in Our Library");
                                bookService.displayBook(foundBook);

                                boolean hasExistingReservation = reservationService.hasExistingReservation(client, foundBook);

                                if (hasExistingReservation) {
                                    System.out.println("You have already borrowed this book.");
                                    System.out.println("No reservation created.");
                                } else {
                                    Reservation existingReservation = reservationService.findReservation(foundBook.getIsbn(), client.getMemberNum());

                                    if (existingReservation != null) {
                                        System.out.println("You have already reserved this book.");
                                        System.out.println("No reservation created.");
                                    } else {
                                        System.out.println("Do you want to borrow the book? (yes/no)");
                                        String borrowOption = scanner.nextLine().toLowerCase();

                                        if (borrowOption.equals("yes")) {
                                            int clientReservationCount = reservationRepository.countReservationsForClient(client);
                                            if (clientReservationCount >= 5) {
                                                System.out.println("You have already reached the maximum limit of reservations.");
                                                System.out.println("No reservation created.");
                                            } else {
                                                Reservation reservation = reservationService.createReservation(client, copyRepository.getBookCopies(foundBook.getTitle()));

                                                bookService.updateBookCopyStatus(reservation.getBookCopy(), Status.BORROWED);
                                                bookService.decreaseBookQuantity(foundBook);

                                                displayReservationInformation(reservation);
                                            }
                                        } else {
                                            System.out.println("No reservation created.");
                                        }
                                    }
                                }
                            } else {
                                System.out.println("No available copies of the book.");
                            }

                        } else {
                            System.out.println("Book not found.");
                        }
                    }
                    break;
                case 2:
                    BookController.main(args);
                    break;
                default:
                    System.out.println("Invalid option. Please try again.");
            }
        }
    }


    public static Book searchByTitle(BookRepository bookRepository, Scanner scanner) {
        System.out.println("Enter a Title:");
        String bookTitleToFind = scanner.nextLine();
        Book book = bookRepository.getBookByTitle(bookTitleToFind);
        return book;
    }

    public static void displayReservationInformation(Reservation reservation) {
        System.out.println("Reservation Information:");
        System.out.println("Due Date: " + reservation.getDueDate());
        System.out.println("Borrowing Date: " + reservation.getBorrowingDate());
        System.out.println("Client: " + reservation.getClient().getFullName());
        System.out.println("Client: " + reservation.getClient().getMemberNum());
        System.out.println("Book Title: " + reservation.getBookCopy().getBook().getTitle());
        System.out.println("Book Author: " + reservation.getBookCopy().getBook().getAuthor().getName());
    }
}
