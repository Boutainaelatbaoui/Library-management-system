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
                        createBook(bookRepository, scanner);
                        break;
                    case 2:
                        displayAllBooks(bookRepository, bookService);
                        break;
                    case 3:
                        updateBook(bookRepository, scanner);
                        break;
                    case 4:
                        deleteBook(bookRepository, scanner);
                        break;
                    case 5:
                        searchByTitle(bookRepository, scanner, bookService);
                        break;
                    case 6:
                        searchByAuthor(bookRepository, scanner, bookService);
                        break;
                    case 7:
                        displayAvailableBooks(bookRepository, bookService);
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

    private static void displayAllBooks(BookRepository bookRepository, BookService bookService) {
        List<Book> books = bookRepository.getAllBooks();
        bookService.displayBooks(books);
    }

    public static void createBook(BookRepository bookRepository, Scanner scanner) {
        String title = getNonEmptyStringInput(scanner, "Book Title");
        String description = getNonEmptyStringInput(scanner, "Book Description");
        int publicationYear = getPositiveIntegerInput(scanner, "Publication Year");
        String isbn = getISBNInput(scanner);
        int quantity = getPositiveIntegerInput(scanner, "Quantity");

        AuthorRepository authorRepository = new AuthorRepository();
        Author selectedAuthor = null;

        while (selectedAuthor == null) {
            selectedAuthor = selectAuthor(authorRepository, scanner);

            if (selectedAuthor == null) {
                System.out.println("Invalid author selection. Please try again.");

                System.out.print("Create a new author? (yes/no): ");
                String createAuthorOption = scanner.nextLine().toLowerCase();

                if (createAuthorOption.equals("yes")) {
                    String authorName = getNonEmptyStringInput(scanner, "Author Name");

                    Author existingAuthor = authorRepository.findAuthorByName(authorName);

                    if (existingAuthor != null) {
                        System.out.println("Author with the same name already exists.");
                    } else {
                        String biography = getNonEmptyStringInput(scanner, "New Biography");
                        String birthdate = getNonEmptyStringInput(scanner, "New Birthdate");
                        Author newAuthor = new Author(authorName, biography, birthdate);
                        authorRepository.createAuthor(newAuthor);
                        selectedAuthor = selectAuthor(authorRepository, scanner);
                    }
                }
            }
        }


        Book book = new Book(title, description, String.valueOf(publicationYear), isbn, quantity, selectedAuthor);
        bookRepository.createBook(book);
    }

    public static void updateBook(BookRepository bookRepository, Scanner scanner) {
        System.out.println("Enter Book ISBN to update:");
        String bookIsbnToUpdate = getISBNInput(scanner);

        Book existingBook = bookRepository.getBookByIsbn(bookIsbnToUpdate);

        if (existingBook != null) {
            String title = getNonEmptyStringInput(scanner, "New Title");
            String description = getNonEmptyStringInput(scanner, "New Description");
            int publicationYear = getPositiveIntegerInput(scanner, "New Publication Year");
            String isbn = getISBNInput(scanner);
            int quantity = getPositiveIntegerInput(scanner, "New Quantity");

            AuthorRepository authorRepository = new AuthorRepository();
            Author selectedAuthor = null;

            while (selectedAuthor == null) {
                selectedAuthor = selectAuthor(authorRepository, scanner);

                if (selectedAuthor == null) {
                    System.out.println("Invalid author selection. Please try again.");
                }
            }

            Book updatedBook = new Book(title, description, String.valueOf(publicationYear), isbn, quantity, selectedAuthor);
            bookRepository.updateBookByIsbn(bookIsbnToUpdate, updatedBook);
        } else {
            System.out.println("Book not found. Update failed.");
        }
    }


    private static String getNonEmptyStringInput(Scanner scanner, String prompt) {
        String input;
        do {
            System.out.println("Enter " + prompt + ":");
            input = scanner.nextLine().trim();
            if (input.isEmpty()) {
                System.out.println("Invalid input. Please enter a non-empty string.");
            }
        } while (input.isEmpty());
        return input;
    }
    private static int getPositiveIntegerInput(Scanner scanner, String prompt) {
        int input;
        do {
            System.out.println("Enter " + prompt + ":");
            while (!scanner.hasNextInt()) {
                System.out.println("Invalid input. Please enter a positive integer.");
                scanner.next();
            }
            input = scanner.nextInt();
        } while (input <= 0);
        scanner.nextLine();
        return input;
    }

    private static String getISBNInput(Scanner scanner) {
        String isbn;
        do {
            System.out.println("Enter ISBN:");
            isbn = scanner.nextLine().trim();

            if (!isValidISBN(isbn)) {
                System.out.println("Invalid input. Please enter a correct ISBN.");
            }
        } while (!isValidISBN(isbn));
        return isbn;
    }

    private static boolean isValidISBN(String isbn) {
        return isbn.matches("\\d{3}-\\d{10}");
    }

    private static Author selectAuthor(AuthorRepository authorRepository, Scanner scanner) {
        List<Author> authorsList = authorRepository.getAllAuthors();
        System.out.println("Select an Author:");
        for (int i = 0; i < authorsList.size(); i++) {
            Author author = authorsList.get(i);
            System.out.println((i + 1) + ". " + author.getName());
        }

        int authorChoice = getPositiveIntegerInput(scanner, "Author Choice");

        if (authorChoice >= 1 && authorChoice <= authorsList.size()) {
            return authorsList.get(authorChoice - 1);
        }
        return null;
    }

    private static void deleteBook(BookRepository bookRepository, Scanner scanner) {
        System.out.println("Enter Book Title to delete:");
        String bookTitleToDelete = scanner.nextLine();

        bookRepository.deleteBook(bookTitleToDelete);
    }

    public static void searchByTitle(BookRepository bookRepository, Scanner scanner, BookService bookService) {
        System.out.println("Enter a Title:");
        String bookTitleToFind = scanner.nextLine();
        Book book = bookRepository.getBookByTitle(bookTitleToFind);
        bookService.displayBook(book);
    }

    private static void searchByAuthor(BookRepository bookRepository, Scanner scanner, BookService bookService) {
        System.out.println("Enter an Author Name:");
        String authorNameToFind = scanner.nextLine();
        List<Book> booksByAuthor = bookRepository.getBooksByAuthor(authorNameToFind);
        bookService.displayBooks(booksByAuthor);
    }

    private static void displayAvailableBooks(BookRepository bookRepository, BookService bookService) {
        List<Book> availableBooks = bookRepository.getAllAvailableBooks();
        bookService.displayBooks(availableBooks);
    }
}
