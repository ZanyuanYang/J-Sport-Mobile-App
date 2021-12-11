package edu.neu.madcourse.team_j_sport.CommentList;

import android.icu.text.MessagePattern;

public class ItemComment {

    private final String avatar;
    private final String username;
    private final String time;
    private final String content;
    private final String commentId;

    public ItemComment(String avatar, String username, String time, String content, String commentId) {
        this.avatar = avatar;
        this.username = username;
        this.time = time;
        this.content = content;
        this.commentId = commentId;
    }

    public String getAvatar() {
        return avatar;
    }

    public String getUsername() {
        return username;
    }

    public String getTime() {
        return time;
    }

    public String getContent() {
        return content;
    }

    public String getCommentId() {
        return commentId;
    }
}
