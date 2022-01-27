package praktikumservices.stellar_burgers.entities;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import praktikumservices.stellar_burgers.generator.UserGenerator;

@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)

public class User {
    private String email;
    private String password;
    private String name;


    public User() {
    }

    public User setAllFields(String email, String password, String name) {
        this.email = email;
        this.password = password;
        this.name = name;
        return this;
    }

    public User setEmailAndPass(String email, String password) {
        this.email = email;
        this.password = password;
        return this;
    }

    public User setPasswordAndName(String password, String name) {
        this.password = password;
        this.name = name;
        return this;
    }

    public User setEmailAndName(String email, String name) {
        this.email = email;
        this.name = name;
        return this;
    }


    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public String getName() {
        return name;
    }

    public static User getRequiredData() {
        return new User().setAllFields(UserGenerator.getEmail(), UserGenerator.getPassword(), UserGenerator.getName());
    }

    public static User getEmailAndPassData() {
        return new User().setEmailAndPass(UserGenerator.getEmail(), UserGenerator.getPassword());
    }

    public static User getPasswordAndName() {
        return new User().setPasswordAndName(UserGenerator.getPassword(), UserGenerator.getName());
    }

    public static User getEmailAndName() {
        return new User().setEmailAndName(UserGenerator.getEmail(), UserGenerator.getName());
    }
}
