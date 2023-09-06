package domain.entities;

public class Client {
    private int id;
    private String fullName;
    private String email;
    private String memberNum;
    private String telephone;
    private String cin;

    // Constructor
    public Client(int id, String fullName, String email, String memberNum, String telephone, String cin) {
        this.id = id;
        this.fullName = fullName;
        this.email = email;
        this.memberNum = memberNum;
        this.telephone = telephone;
        this.cin = cin;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getMemberNum() {
        return memberNum;
    }

    public void setMemberNum(String memberNum) {
        this.memberNum = memberNum;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public String getCin() {
        return cin;
    }

    public void setCin(String cin) {
        this.cin = cin;
    }

    @Override
    public String toString() {
        return "domain.entities.Client{" +
                "id=" + id +
                ", fullName='" + fullName + '\'' +
                ", email='" + email + '\'' +
                ", memberNum='" + memberNum + '\'' +
                ", telephone='" + telephone + '\'' +
                ", cin='" + cin + '\'' +
                '}';
    }
}

