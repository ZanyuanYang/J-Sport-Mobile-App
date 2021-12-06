package edu.neu.madcourse.prev_assignment;

import java.io.Serializable;
import java.util.HashMap;

public class User implements Serializable {
    private static final long serialVersionUID = 1L;

    private long id;
    private String username;


    public User() { }

    public User(long id, String username) {
        this.id = id;
        this.username = username;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }



}
