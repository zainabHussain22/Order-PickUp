package final_project2;

public class User {
    protected String username;
    protected String password;

    public User(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public boolean login(String inputPassword) {
        return this.password.equals(inputPassword);
    }
}