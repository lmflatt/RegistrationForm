import java.io.Serializable;

/**
 * Created by lee on 9/29/16.
 */
public class User implements Serializable {
    Integer id;
    String username;
    String address;
    String email;

    public User() {
    }

    public User(String username, String address, String email) {
        this.username = username;
        this.address = address;
        this.email = email;
    }

    public User(Integer id, String username, String address, String email) {
        this.id = id;
        this.username = username;
        this.address = address;
        this.email = email;
    }

    public Integer getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public String getAddress() {
        return address;
    }

    public String getEmail() {
        return email;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
