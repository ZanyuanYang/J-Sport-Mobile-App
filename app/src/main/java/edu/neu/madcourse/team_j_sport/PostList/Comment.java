package edu.neu.madcourse.team_j_sport.PostList;

public class Comment {

    private String uid;
    private String comment;
    private String firstname;
    private String lastname;

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
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

    public Comment(String uid, String comment, String firstname, String lastname) {
        this.uid = uid;
        this.comment = comment;
        this.firstname = firstname;
        this.lastname = lastname;
    }

    public Comment() {
    }


}
