package controller;

import java.util.List;
import java.util.Scanner;

import domain.entities.Author;
import domain.entities.Book;
import repository.AuthorRepository;
import repository.BookRepository;
import service.BookService;

public class BookController {
    public static void main(String[] args) {
        try (Scanner scanner = new Scanner(System.in)) {
            BookRepository bookRepository = new BookRepository();
            BookService bookService = new BookService(bookRepository);

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
        System.out.println("9. Exit");
        System.out.println("**************************************************");
    }

    private static int readUserChoice(Scanner scanner) {
        System.out.print("Enter your choice: ");
        int choice = scanner.nextInt();
        scanner.nextLine();
        return choice;
    }

    private static void displayAllBooks(BookRepository bookRepository, BookService bookService) {
        List<Book> books = bookRepository.getAllBooks();
        bookService.displayBooks(books);
    }

    private static void createBook(BookRepository bookRepository, Scanner scanner) {
        System.out.println("Enter Book Title:");
        String title = scanner.nextLine();

        System.out.println("Enter Book Description:");
        String description = scanner.nextLine();

        System.out.println("Enter Publication Year:");
        String publicationYear = scanner.nextLine();

        System.out.println("Enter ISBN:");
        String isbn = scanner.nextLine();

        System.out.println("Enter Quantity:");
        int quantity = Integer.parseInt(scanner.nextLine());

        AuthorRepository authorRepository = new AuthorRepository();
        List<Author> authorsList = authorRepository.getAllAuthors();

        System.out.println("Select an Author:");

        for (int i = 0; i < authorsList.size(); i++) {
            Author author = authorsList.get(i);
            System.out.println((i + 1) + ". " + author.getName());
        }

        int authorChoice = Integer.parseInt(scanner.nextLine());

        if (authorChoice >= 1 && authorChoice <= authorsList.size()) {
            Author selectedAuthor = authorsList.get(authorChoice - 1);

            Book book = new Book(title, description, publicationYear, isbn, quantity, selectedAuthor);

            bookRepository.createBook(book);
        } else {
            System.out.println("Invalid author selection.");
        }
    }

    private static void updateBook(BookRepository bookRepository, Scanner scanner) {
        System.out.println("Enter Book Title to update:");
        String bookTitleToUpdate = scanner.nextLine();

        System.out.println("Enter New Title:");
        String title = scanner.nextLine();

        System.out.println("Enter New Description:");
        String description = scanner.nextLine();

        System.out.println("Enter New Publication Year:");
        String publicationYear = scanner.nextLine();

        System.out.println("Enter New ISBN:");
        String isbn = scanner.nextLine();

        System.out.println("Enter New Quantity:");
        int quantity = Integer.parseInt(scanner.nextLine());

        AuthorRepository authorRepository = new AuthorRepository();
        List<Author> authorsList = authorRepository.getAllAuthors();

        System.out.println("Select a New Author:");

        for (int i = 0; i < authorsList.size(); i++) {
            Author author = authorsList.get(i);
            System.out.println((i + 1) + ". " + author.getName());
        }

        int authorChoice = Integer.parseInt(scanner.nextLine());

        if (authorChoice >= 1 && authorChoice <= authorsList.size()) {
            Author selectedAuthor = authorsList.get(authorChoice - 1);

            Book book = new Book(title, description, publicationYear, isbn, quantity, selectedAuthor);

            bookRepository.updateBook(bookTitleToUpdate, book);
        } else {
            System.out.println("Invalid author selection.");
        }
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
