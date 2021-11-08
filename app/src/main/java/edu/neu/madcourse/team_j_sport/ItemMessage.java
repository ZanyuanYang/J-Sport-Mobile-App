package edu.neu.madcourse.team_j_sport;

import java.io.Serializable;

public class ItemMessage implements Serializable {

    private static final long serialVersionUID = 54684386984L;

    private final String imageName;
    private final String userName;
    private final String date;

    public ItemMessage(String imageName, String userName, String date) {
        this.imageName = imageName;
        this.userName = userName;
        this.date = date;
    }

    public String getImageName() {
        return imageName;
    }

    public String getUserName() {
        return userName;
    }

    public String getDate() {
        return date;
    }
}
