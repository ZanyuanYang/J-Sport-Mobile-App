package edu.neu.madcourse.team_j_sport.EventList;

public class Events {
    public String title;
    public String summary;
    public String description;
    public String location;
    public String limitPerson;
    public String time;
    public String contact;
//    public String organizer;
    public Events(String tit, String sum, String des,
                  String loc, String lp, String time, String cont){
        this.title = tit;
        this.summary = sum;
        this.description = des;
        this.location = loc;
        this.limitPerson = lp;
        this.time = time;
        this.contact = cont;
    }
}
