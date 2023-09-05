package library;
import java.util.Date;

public class Reservation {
    private int id;
    private Date dueDate;
    private Date borrowingDate;
    private Client client;
    private BookCopy bookcopy;

    // Constructor
    public Reservation(int id, Date dueDate, Date borrowingDate, Client client, BookCopy bookcopy) {
        this.id = id;
        this.dueDate = dueDate;
        this.borrowingDate = borrowingDate;
        this.client = client;
        this.bookcopy = bookcopy;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Date getDueDate() {
        return dueDate;
    }

    public void setDueDate(Date dueDate) {
        this.dueDate = dueDate;
    }

    public Date getBorrowingDate() {
        return borrowingDate;
    }

    public void setBorrowingDate(Date borrowingDate) {
        this.borrowingDate = borrowingDate;
    }

    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    public BookCopy getBookCopy() {
        return bookcopy;
    }

    public void setBookCopy(BookCopy bookcopy) {
        this.bookcopy = bookcopy;
    }

    @Override
    public String toString() {
        return "Reservation{" +
                "id=" + id +
                ", dueDate=" + dueDate +
                ", borrowingDate=" + borrowingDate +
                ", client=" + client +
                ", bookcopy=" + bookcopy +
                '}';
    }
}

