package controller;
import java.util.List;
import java.util.Scanner;
import domain.entities.Author;
import domain.entities.Book;
import repository.BookRepository;
import repository.AuthorRepository;
import service.BookService;

public class BookController {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        BookRepository bookRepository = new BookRepository();

        while (true) {
            System.out.println("Choose an option:");
            System.out.println("1. Create Book");
            System.out.println("2. Read All Books");
            System.out.println("3. Update Book");
            System.out.println("4. Delete Book");
            System.out.println("5. Search By Title");
            System.out.println("6. Search By Author");
            System.out.println("7. Exit");
            System.out.println("**************************************************");

            int choice = Integer.parseInt(scanner.nextLine());

            switch (choice) {
                case 1:
                    createBook(bookRepository, scanner);
                    System.out.println("**************************************************");
                    break;
                case 2:
                    BookService bookService = new BookService();
                    bookService.displayBooks();
                    System.out.println("**************************************************");
                    break;
                case 3:
                    updateBook(bookRepository, scanner);
                    System.out.println("**************************************************");
                    break;
                case 4:
                    deleteBook(bookRepository, scanner);
                    System.out.println("**************************************************");
                    break;
                case 5:
                    System.out.println("Enter a Title:");
                    String bookTitleToFound = scanner.nextLine();
                    Book book = bookRepository.getBookByTitle(bookTitleToFound);
                    displayBook(book);
                    System.out.println("**************************************************");
                    break;
                case 6:
                    System.out.println("Enter an Author Name:");
                    String authorNameToFound = scanner.nextLine();
                    bookService = new BookService();
                    bookService.displayBooks();
                    System.out.println("**************************************************");
                    break;
                case 7:
                    System.out.println("Exiting the application.");
                    scanner.close();
                    System.exit(0);
                default:
                    System.out.println("Invalid option. Please try again.");
                    System.out.println("**************************************************");
            }
        }
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

    private static void displayBook(Book book) {
        if (book == null) {
            System.out.println("No book found.");
        } else {
            System.out.println("The book:");
            System.out.println("Title: " + book.getTitle());
            System.out.println("Description: " + book.getDescription());
            System.out.println("Publication Year: " + book.getPublicationYear());
            System.out.println("ISBN: " + book.getIsbn());
            System.out.println("Quantity: " + book.getQuantity());

            Author author = book.getAuthor();
            System.out.println("Author Name: " + author.getName());
            System.out.println("Author Biography: " + author.getBiography());
            System.out.println("Author Birthdate: " + author.getBirthdate());

            System.out.println();
        }
    }



    private static void deleteBook(BookRepository bookRepository, Scanner scanner) {
        System.out.println("Enter Book Title to delete:");
        String bookTitleToDelete = scanner.nextLine();

        bookRepository.deleteBook(bookTitleToDelete);
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
}
