package service;

import domain.entities.Author;
import domain.entities.Book;
import domain.entities.BookCopy;
import domain.entities.Reservation;
import domain.enums.Status;
import repository.AuthorRepository;
import repository.BookRepository;
import repository.ReservationRepository;

import java.util.List;
import java.util.Scanner;

public class BookService {
    private BookRepository bookRepository;

    public BookService(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    public static void displayAllBooks(BookRepository bookRepository, BookService bookService) {
        List<Book> books = bookRepository.getAllBooks();
        bookService.displayBooks(books);
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
                System.out.println("Invalid input. Please enter a correct ISBN (For Example: 123-1234567890).");
            }
        } while (!isValidISBN(isbn));
        return isbn;
    }

    private static boolean isValidISBN(String isbn) {
        return isbn.matches("\\d{3}-\\d{10}");
    }

    public static Author selectAuthor(AuthorRepository authorRepository, Scanner scanner) {
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
    public static void createBook(BookRepository bookRepository, Scanner scanner) {
        String title = getNonEmptyStringInput(scanner, "Book Title");
        String description = getNonEmptyStringInput(scanner, "Book Description");
        int publicationYear = getPositiveIntegerInput(scanner, "Publication Year");
        String isbn = getISBNInput(scanner);

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

        Book book = new Book(title, description, String.valueOf(publicationYear), isbn, selectedAuthor);
        bookRepository.createBook(book);
        BookService bookService = new BookService(bookRepository);
        int quantity = getPositiveIntegerInput(scanner, "Quantity");
        int bookID = bookService.getBookID(book);
        book.setId(bookID);
        bookService.createBookCopies(quantity, bookRepository, book);
        System.out.println("Bookcopies created successfully.");
    }

    public int getBookID(Book book){
        return bookRepository.getBookID(book);
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

            AuthorRepository authorRepository = new AuthorRepository();
            Author selectedAuthor = null;

            while (selectedAuthor == null) {
                selectedAuthor = selectAuthor(authorRepository, scanner);

                if (selectedAuthor == null) {
                    System.out.println("Invalid author selection. Please try again.");
                }
            }

            Book updatedBook = new Book(title, description, String.valueOf(publicationYear), isbn, selectedAuthor);
            bookRepository.updateBookByIsbn(bookIsbnToUpdate, updatedBook);
        } else {
            System.out.println("Book not found. Update failed.");
        }
    }

    public static void deleteBook(BookRepository bookRepository, Scanner scanner) {
        System.out.println("Enter Book ISBN to delete:");
        String bookIsbnToDelete = scanner.nextLine();

        bookRepository.deleteBookByIsbn(bookIsbnToDelete);
    }

    public static void searchByTitle(BookRepository bookRepository, Scanner scanner, BookService bookService) {
        System.out.println("Enter a Title:");
        String bookTitleToFind = scanner.nextLine();
        Book book = bookRepository.getBookByTitle(bookTitleToFind);
        bookService.displayBook(book);
    }

    public static void searchByAuthor(BookRepository bookRepository, Scanner scanner, BookService bookService) {
        System.out.println("Enter an Author Name:");
        String authorNameToFind = scanner.nextLine();
        List<Book> booksByAuthor = bookRepository.getBooksByAuthor(authorNameToFind);
        bookService.displayBooks(booksByAuthor);
    }

    public static void displayAvailableBooks(BookRepository bookRepository, BookService bookService) {
        List<Book> availableBooks = bookRepository.getAllAvailableBooks();
        bookService.displayBooks(availableBooks);
    }

    public void updateBookCopyStatus(BookCopy bookCopy, Status newStatus) {
        bookCopy.setStatus(newStatus);
        bookRepository.updateBookCopyStatus(bookCopy);
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

    public void createBookCopies(int quantity, BookRepository bookRepository, Book book) {
        for (int i = 0; i < quantity; i++) {
            BookCopy bookCopy = new BookCopy(Status.AVAILABLE, book);
            bookRepository.createBookCopy(bookCopy);
        }
    }


    private void printBookDetails(Book book) {
        System.out.println("Title: " + book.getTitle());
        System.out.println("Description: " + book.getDescription());
        System.out.println("Publication Year: " + book.getPublicationYear());
        System.out.print("\u001B[33m");
        System.out.println("ISBN: " + book.getIsbn());
        System.out.print("\u001B[0m");

        System.out.println("Author Name: " + book.getAuthor().getName());
        System.out.println("Author Biography: " + book.getAuthor().getBiography());
        System.out.println("Author Birthdate: " + book.getAuthor().getBirthdate());
    }
}

