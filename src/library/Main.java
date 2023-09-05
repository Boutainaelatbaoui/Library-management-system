package library;
import library.*;
import java.util.Date;

public class Main {
    public static void main(String[] args)
    {
        Author author = new Author(1, "John Doe", "An author's biography", "1990-01-01");
        System.out.println(author);

        Book book = new Book(1, "Sample Book", "A book description", "2023", "1234567890", 10, author);
        System.out.println(book);

        BookCopy bookcopy = new BookCopy(1, Status.LOST, book);
        System.out.println(bookcopy.getBookCopyInfo());

        Client client = new Client(1, "John Doe", "john@example.com", "12345", "555-555-5555", "ABC123");
        Reservation reservation = new Reservation(1, new Date(), new Date(), client, bookcopy);
        System.out.println(reservation);
    }
}
