package hu.mobilalk.yagta;

public class User {
    private String userName;
    private String email;
    private double testsuly;

    public User() {
    }

    public User(String userName, String email, double testsuly) {
        this.userName = userName;
        this.email = email;
        this.testsuly = testsuly;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public double getTestsuly() {
        return testsuly;
    }

    public void setTestsuly(double testsuly) {
        this.testsuly = testsuly;
    }
}
