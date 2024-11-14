package mx.uv.fei.logic.domain;

public class Admin {
    private String name;
    private String firstSurname;
    private String secondSurname;
    private String password;
    private int adminId;

    public void setName(String name) {
        this.name = name;
    }

    public void setFirstSurname(String firstSurname) {
        this.firstSurname = firstSurname;
    }

    public void setSecondSurname(String secondSurname) {
        this.secondSurname = secondSurname;
    }

    public int getAdminId() {
        return adminId;
    }

    public void setAdgetAdminId(int adminId) {
        this.adminId = adminId;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public String getFirstSurname() {
        return firstSurname;
    }

    public String getSecondSurname() {
        return secondSurname;
    }

    public String getPassword() {
        return password;
    }
}
