package service;
import domain.entities.Author;
import domain.entities.Book;
import repository.BookRepository;
import java.util.List;

public class BookService {
    private BookRepository bookRepository;
    public void displayBooks() {
        List<Book> books = bookRepository.getAllBooks();

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

                System.out.println("######################");

                Author author = book.getAuthor();
                System.out.println("Author Name: " + author.getName());
                System.out.println("Author Biography: " + author.getBiography());
                System.out.println("Author Birthdate: " + author.getBirthdate());

                System.out.println();
            }
        }
    }

}
