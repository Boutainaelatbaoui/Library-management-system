package domain.entities;

public class Author {
    private int id;               // Primary Key
    private String name;
    private String biography;
    private String birthdate;



    // Constructor
    public Author(int id, String name, String biography, String birthdate) {
        this.id = id;
        this.name = name;
        this.biography = biography;
        this.birthdate = birthdate;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBiography() {
        return biography;
    }

    public void setBiography(String biography) {
        this.biography = biography;
    }

    public String getBirthdate() {
        return birthdate;
    }

    public void setBirthdate(String birthdate) {
        this.birthdate = birthdate;
    }

    @Override
    public String toString() {
        return "domain.entities.Author{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", biography='" + biography + '\'' +
                ", birthdate='" + birthdate + '\'' +
                '}';
    }

    public String getAuthor() {
        return biography + ", " + name + ", " + birthdate;
    }

}
