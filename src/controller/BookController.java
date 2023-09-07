package controller;
import java.util.List;
import java.util.Scanner;

import domain.entities.Author;
import domain.entities.Book;
import repository.BookRepository;
import repository.AuthorRepository;

public class BookController {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        BookRepository bookRepository = new BookRepository();

        while (true) {
            System.out.println("Choose an option:");
            System.out.println("1. Create Book");
            System.out.println("2. Read All Books");
            System.out.println("3. Update Book");
            System.out.println("4. Delete Delete");
            System.out.println("5. Exit");

            int choice = Integer.parseInt(scanner.nextLine());

            switch (choice) {
                case 1:
                    createBook(bookRepository, scanner);
                    break;
                case 2:
                    List<Book> books = bookRepository.getAllBooks();
                    displayBooks(books);
                    break;
                case 3:
                    updateBook(bookRepository, scanner);
                    break;
                case 4:
                    deleteBook(bookRepository, scanner);
                    break;
                case 5:
                    System.out.println("Exiting the application.");
                    scanner.close();
                    System.exit(0);
                default:
                    System.out.println("Invalid option. Please try again.");
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
            System.out.println("Book created successfully.");
        } else {
            System.out.println("Invalid author selection.");
        }
    }

    private static void displayBooks(List<Book> books) {
        if (books.isEmpty()) {
            System.out.println("No books found.");
        } else {
            System.out.println("List of Books:");
            for (Book book : books) {
                System.out.println("Title: " + book.getTitle());
                System.out.println("Description: " + book.getDescription());
                System.out.println("Publication Year: " + book.getPublicationYear());
                System.out.println("ISBN: " + book.getIsbn());
                System.out.println("Quantity: " + book.getQuantity());

                Author author = book.getAuthor();
                System.out.println("Author ID: " + author.getId());
                System.out.println("Author Name: " + author.getName());
                System.out.println("Author Biography: " + author.getBiography());
                System.out.println("Author Birthdate: " + author.getBirthdate());

                System.out.println();
            }
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
        String newTitle = scanner.nextLine();

        System.out.println("Enter New Description:");
        String newDescription = scanner.nextLine();

        System.out.println("Enter New Publication Year:");
        String newPublicationYear = scanner.nextLine();

        System.out.println("Enter New ISBN:");
        String newIsbn = scanner.nextLine();

        System.out.println("Enter New Quantity:");
        int newQuantity = Integer.parseInt(scanner.nextLine());

        boolean updated = bookRepository.updateBookByTitle(bookTitleToUpdate, newTitle, newDescription, newPublicationYear, newIsbn, newQuantity, newAuthor);

        if (updated) {
            System.out.println("Book updated successfully.");
        } else {
            System.out.println("Book not found. Update failed.");
        }
    }

}
