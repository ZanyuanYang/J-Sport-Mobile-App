package edu.neu.madcourse.team_j_sport.Post;

import java.util.HashMap;

public class Posts {
    public String content;
    public String date;
    public String title;
    public String uid;
    public String username;
    public Posts(String con, String dat, String tit, String uid, String un){
        this.title = tit;
        this.content = con;
        this.date = dat;
        this.uid  = uid;
        this.username = un;
    }
}
