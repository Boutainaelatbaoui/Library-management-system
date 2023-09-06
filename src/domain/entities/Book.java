package domain.entities;

public class Book {
    private int id;
    private String title;
    private String description;
    private String publicationYear;
    private String isbn;
    private int quantity;
    private Author author;

    // Constructor
    public Book(int id, String title, String description, String publicationYear, String isbn, int quantity, Author author) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.publicationYear = publicationYear;
        this.isbn = isbn;
        this.quantity = quantity;
        this.author = author;
    }

    // Getter and Setter methods for attributes
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPublicationYear() {
        return publicationYear;
    }

    public void setPublicationYear(String publicationYear) {
        this.publicationYear = publicationYear;
    }

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public Author getAuthor(Author author){
        return author;
    }

    public void setAuthor(Author author){
        this.author = author;
    }

    // toString method to represent the object as a string
    @Override
    public String toString() {
        return "domain.entities.Book{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", publicationYear='" + publicationYear + '\'' +
                ", isbn='" + isbn + '\'' +
                ", quantity=" + quantity +
                ", author=" + author +
                '}';
    }
}
