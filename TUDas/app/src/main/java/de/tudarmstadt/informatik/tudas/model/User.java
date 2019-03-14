package de.tudarmstadt.informatik.tudas.model;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class User {

    private static final DateFormat dateFormat = SimpleDateFormat.getDateInstance();

    private int id;

    private String username;

    private String password;

    private String email;

    private Calendar birthday;

    public Calendar getBirthday() {
        return birthday;
    }

    public void setBirthday(Calendar birthday) {
        this.birthday = birthday;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public String toString() {
        return username + " (Birthday: " + dateFormat.format(birthday.getTime()) + ")";
    }
}
