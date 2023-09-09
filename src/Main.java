import dbconnection.DbConnection;
import domain.entities.Book;
import domain.entities.Author;
import domain.entities.BookCopy;
import domain.entities.Client;
import domain.entities.Reservation;
import domain.enums.Status;

import java.util.Date;

public class Main {
    public static void main(String[] args)
    {
        Author author = new Author(1,"John Doe", "An author's biography", "1990-01-01");
        System.out.println(author);

        Book book = new Book("Sample domain.entities.Book", "A book description", "2023", "1234567890", 10, author);
        System.out.println(book);

        BookCopy bookcopy = new BookCopy(1, Status.LOST, book);
        System.out.println(bookcopy.getBookCopyInfo());

        Client client = new Client("John Doe", "john@example.com", "12345", 555-555-5555, "ABC123");
        DbConnection.getConnection();
    }
}
