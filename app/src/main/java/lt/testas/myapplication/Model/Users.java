package lt.testas.myapplication.Model;

public class Users {
    private String email , name , pass;
    public Users(){

    }

    public Users(String email, String name, String pass) {
        this.email = email;
        this.name = name;
        this.pass = pass;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPass() {
        return pass;
    }

    public void setPass(String pass) {
        this.pass = pass;
    }
}
