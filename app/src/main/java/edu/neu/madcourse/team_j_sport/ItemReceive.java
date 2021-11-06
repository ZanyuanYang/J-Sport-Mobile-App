package edu.neu.madcourse.team_j_sport;

import java.io.Serializable;

public class ItemReceive implements Serializable {

    private static final long serialVersionUID = 54684386984L;

    private final String imageUrl;
    private final String sender;
    private final String date;

    public ItemReceive(String imageUrl, String sender, String date) {
        this.imageUrl = imageUrl;
        this.sender = sender;
        this.date = date;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public String getSender() {
        return sender;
    }

    public String getDate() {
        return date;
    }
}
