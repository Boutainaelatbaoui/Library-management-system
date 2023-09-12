package domain.entities;

import domain.enums.Status;

public class BookCopy {
    private int id;
    private Status status;
    private Book book;

    // Constructor
    public BookCopy(Status status, Book book) {
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

    public void setId(int id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "domain.entities.Book copy{" +
                "id=" + id +
                ", status=" + status +
                ", book=" + book +
                '}';
    }
}
