package managesystem.user;

import java.time.LocalDate;

public class User {
    private String username;
    private String password;
    private String name;
    private String phoneNumber;
    private String address;
    private String email;
    private LocalDate date;
    private String gender;

    public User(String username, String password, String name, String gender, LocalDate date, String phoneNumber,
                String address, String email) {
        this.username = username;
        this.password = password;
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.address = address;
        this.email = email;
        this.date = date;
        this.gender = gender;

    }

    public String getGender() {
        return gender;
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

    public LocalDate getDate() {
        return date;
    }

    public String getAddress() {
        return address;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public String getName() {
        return name;
    }

}
