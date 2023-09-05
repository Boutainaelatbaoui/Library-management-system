package library;
import library.*;

public class BookCopy {
    private int id;
    private Status status;
    private Book book;

    // Constructor
    public BookCopy(int id, Status status, Book book) {
        this.id = id;
        this.status = status;
        this.book = book;
    }

    public int getId() {
        return id;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public Book getBook() {
        return book;
    }

    public void setBook(Book book) {
        this.book = book;
    }

    public String getBookCopyInfo() {
        return id + " " + status;
    }

    @Override
    public String toString() {
        return "Book copy{" +
                "id=" + id +
                ", status=" + status +
                ", book=" + book +
                '}';
    }
}
