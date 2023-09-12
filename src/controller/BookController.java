package controller;

import java.sql.Connection;
import java.util.List;
import java.util.Scanner;

import dbconnection.DbConnection;
import domain.entities.Author;
import domain.entities.Book;
import domain.entities.Client;
import domain.entities.Reservation;
import domain.enums.Status;
import repository.AuthorRepository;
import repository.BookRepository;
import repository.ReservationRepository;
import service.BookService;
import service.ReservationService;

public class BookController {
    public static void main(String[] args) {
        try (Scanner scanner = new Scanner(System.in)) {
            Connection connection = DbConnection.getConnection();
            BookRepository bookRepository = new BookRepository(connection);
            BookService bookService = new BookService(bookRepository);
            ReservationRepository reservationRepository = new ReservationRepository(connection);
            ReservationService reservationService = new ReservationService(reservationRepository);

            while (true) {
                printMenu();

                int choice = readUserChoice(scanner);

                switch (choice) {
                    case 1:
                        bookService.createBook(bookRepository, scanner);
                        break;
                    case 2:
                        bookService.displayAllBooks(bookRepository, bookService);
                        break;
                    case 3:
                        bookService.updateBook(bookRepository, scanner);
                        break;
                    case 4:
                        bookService.deleteBook(bookRepository, scanner);
                        break;
                    case 5:
                        bookService.searchByTitle(bookRepository, scanner, bookService);
                        break;
                    case 6:
                        bookService.searchByAuthor(bookRepository, scanner, bookService);
                        break;
                    case 7:
                        bookService.displayAvailableBooks(bookRepository, bookService);
                        break;
                    case 8:
                        ClientController.main(args);
                        break;
                    case 9:
                        System.out.println("Enter ISBN of the book:");
                        String isbn = scanner.nextLine();
                        System.out.println("Enter Member Number of the client:");
                        int memberNum = Integer.parseInt(scanner.nextLine());

                        Reservation reservation = reservationService.findReservation(isbn, memberNum);

                        if (reservation != null) {
                            System.out.println("Reservation found:");
                            ClientController.displayReservationInformation(reservation);

                            System.out.println("Do you want to return the book? (yes/no)");
                            String returnOption = scanner.nextLine().toLowerCase();

                            if (returnOption.equals("yes")) {
                                bookService.updateBookCopyStatus(reservation.getBookCopy(), Status.RETURNED);
                                System.out.println("Book returned successfully.");
                            } else {
                                System.out.println("Book not returned.");
                            }
                        } else {
                            System.out.println("No reservation found for the given ISBN and Member Number.");
                        }
                        break;
                    case 10:
                        bookService.displayStatistics();
                        break;
                    case 11:
                        bookService.updateLostBookCopiesStatus(reservationRepository, bookRepository);
                        break;
                    case 12:
                        System.out.println("Exiting the application.");
                        return;
                    default:
                        System.out.println("Invalid option. Please try again.");
                }
            }
        }
    }

    public static void printMenu() {
        System.out.println("Choose an option:");
        System.out.println("1. Create Book");
        System.out.println("2. Read All Books");
        System.out.println("3. Update Book");
        System.out.println("4. Delete Book");
        System.out.println("5. Search By Title");
        System.out.println("6. Search By Author");
        System.out.println("7. Display Available Books");
        System.out.println("8. Borrow a Book");
        System.out.println("9. Return a Book");
        System.out.println("10. Display Statictics");
        System.out.println("11. Added 'lost' status to unreturned books");
        System.out.println("12. Exist");
        System.out.println("**************************************************");
    }

    private static int readUserChoice(Scanner scanner) {
        int choice;
        while (true) {
            System.out.print("Enter your choice: ");
            if (scanner.hasNextInt()) {
                choice = scanner.nextInt();
                scanner.nextLine();
                return choice;
            } else {
                System.out.println("Invalid input. Please enter a valid number.");
                scanner.nextLine();
            }
        }
    }

}
