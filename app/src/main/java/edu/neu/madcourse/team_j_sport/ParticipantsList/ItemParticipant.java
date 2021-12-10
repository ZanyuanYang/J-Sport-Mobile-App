package edu.neu.madcourse.team_j_sport.ParticipantsList;

public class ItemParticipant {

    private final String uid;
    private final String username;
    private final String email;

    public ItemParticipant(String uid, String username, String email) {
        this.uid = uid;
        this.username = username;
        this.email = email;
    }

    public String getUid() {
        return uid;
    }

    public String getUsername() {
        return username;
    }

    public String getEmail() {
        return email;
    }
}
