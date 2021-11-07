package edu.neu.madcourse.team_j_sport;

import java.io.Serializable;

public class ItemReceive implements Serializable {

    private static final long serialVersionUID = 54684386984L;

    private final String imageName;
    private final String sender;
    private final String date;

    public ItemReceive(String imageName, String sender, String date) {
        this.imageName = imageName;
        this.sender = sender;
        this.date = date;
    }

    public String getImageName() {
        return imageName;
    }

    public String getSender() {
        return sender;
    }

    public String getDate() {
        return date;
    }
}
