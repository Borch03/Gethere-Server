package pl.edu.agh.gethere.model;

import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.util.UUID;

/**
 * Created by Dominik on 28.12.2016.
 */

public class User {

    public User() {}

    public User(UserRole userRole) {
        this.id = UUID.randomUUID().toString();
        this.role = userRole;
    }

    private String id;

    @Size(min = 3, max = 20, message = "Username must consist of 3 to 20 characters.")
    @Pattern(regexp = "^[a-zA-Z0-9]+$", message = "Username must consist of alphanumeric characters with no spaces.")
    private String username;

    @Size(min = 4, max = 20, message = "Password cannot be shorter than 4 and longer than 20 characters.")
    private String password;

    @Pattern(regexp = "[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,4}", message = "Invalid email address.")
    @NotEmpty
    private String email;

    private UserRole role;

    public String getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getEmail() {
        return email;
    }

    public void setId(String id) { this.id = id; }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public UserRole getRole() {
        return role;
    }

    public void setRole(UserRole role) {
        this.role = role;
    }
}
