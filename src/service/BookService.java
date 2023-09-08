package service;

import domain.entities.Book;
import java.util.List;

public class BookService {
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
        System.out.println("ISBN: " + book.getIsbn());
        System.out.println("Quantity: " + book.getQuantity());

        System.out.println("Author Name: " + book.getAuthor().getName());
        System.out.println("Author Biography: " + book.getAuthor().getBiography());
        System.out.println("Author Birthdate: " + book.getAuthor().getBirthdate());
    }
}

