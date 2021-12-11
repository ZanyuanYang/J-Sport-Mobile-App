package edu.neu.madcourse.team_j_sport.CommentList;

public class Comment {

    private String uid;
    private String comment;
    private String username;
    private String commentTime;

    public String getCommentTime() {
        return commentTime;
    }

    public void setCommentTime(String commentTime) {
        this.commentTime = commentTime;
    }



    public Comment(String uid, String comment, String username, String commentTime) {
        this.uid = uid;
        this.comment = comment;
        this.username = username;
        this.commentTime = commentTime;
    }

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

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }



    public Comment() {
    }


}
