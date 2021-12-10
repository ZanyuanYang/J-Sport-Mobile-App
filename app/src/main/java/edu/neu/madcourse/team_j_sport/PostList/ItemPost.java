package edu.neu.madcourse.team_j_sport.PostList;

public class ItemPost {

    private final String avatar;
    private final String title;
    private final String content;
    private final String postId;

    public ItemPost(String avatar, String title, String content, String postId) {
        this.avatar = avatar;
        this.title = title;
        this.content = content;
        this.postId = postId;
    }

    public String getAvatar() {
        return avatar;
    }

    public String getTitle() {
        return title;
    }

    public String getContent() {
        return content;
    }

    public String getPostId() {
        return postId;
    }
}
