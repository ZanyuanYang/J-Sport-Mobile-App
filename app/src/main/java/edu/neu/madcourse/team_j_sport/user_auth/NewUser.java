package edu.neu.madcourse.team_j_sport.user_auth;

import java.io.Serializable;

public class NewUser implements Serializable {
    private static final long serialVersionUID = 1L;

    private String id;
    private String firstname;
    private String lastname;
    private String email;

    public NewUser() {
    }

    public NewUser(String id, String firstname, String lastname, String email) {
        this.id = id;
        this.firstname = firstname;
        this.lastname = lastname;
        this.email = email;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
